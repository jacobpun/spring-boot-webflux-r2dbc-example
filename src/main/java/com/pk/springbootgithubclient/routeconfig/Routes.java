package com.pk.springbootgithubclient.routeconfig;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.Arrays;

import com.pk.springbootgithubclient.model.GithubProject;
import com.pk.springbootgithubclient.model.RepositoryEvent;
import com.pk.springbootgithubclient.repository.GithubProjectRepository;
import com.pk.springbootgithubclient.service.GithubClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class Routes {
    private final GithubProjectRepository repo;
    private final GithubClient client;
    
    @Bean
    public RouterFunction<ServerResponse> routeConfig() {
        return route()
            .GET("/repos", accept(MediaType.TEXT_PLAIN), this::fetchRepos)
            .GET("/events/{repoName}", this::fetchEvents)
            .GET("/admin/projects", this::fetchProjects)
            .build();
    }

    private Mono<ServerResponse> fetchRepos(ServerRequest req) {
        Flux<String> repoNameFlux = repo.findAll().map(p-> p.getRepoName() + "\r\n");
        return ok().body(repoNameFlux, String.class);
    }

    private Mono<ServerResponse> fetchEvents(ServerRequest req) {
        var gitRepoName = req.pathVariable("repoName");        
        return repo.findByRepoName(gitRepoName)
                    .flatMap(proj -> client.fetchEvents(proj.getOrgName(), proj.getRepoName()))
                    .map(Arrays::asList)
                    .flatMap(events -> ok().body(Flux.fromIterable(events), RepositoryEvent.class))
                    .switchIfEmpty(notFound().build());
    }

    private Mono<ServerResponse> fetchProjects(ServerRequest req) {
        return ok().body(this.repo.findAll(), GithubProject.class);
    }
}