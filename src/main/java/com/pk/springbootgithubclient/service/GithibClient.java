package com.pk.springbootgithubclient.service;

import java.util.concurrent.atomic.AtomicInteger;

import com.pk.springbootgithubclient.config.GithubProperties;
import com.pk.springbootgithubclient.model.RepositoryEvent;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Mono;

@Component
public class GithibClient {
    private final String eventsUrl;
    private final WebClient webClient;
    private AtomicInteger guage;

    public GithibClient(GithubProperties gp, MeterRegistry registry) {
        this.guage = registry.gauge("github.requests.remaining", new AtomicInteger(0));
        this.eventsUrl = gp.getEventsUrl();
        this.webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(gp.getUsername(), gp.getPassword()))
                .filter(rateLimitFilter())
                .build();
    }

    public Mono<RepositoryEvent[]> fetchEvents(String owner, String repo) {
        return this.webClient.get().uri(eventsUrl, owner, repo).retrieve().bodyToMono(RepositoryEvent[].class);
    }

    private ExchangeFilterFunction rateLimitFilter() {
        return (req, next) -> next.exchange(req).doOnNext(this::setRateLimit);
    }

    private void setRateLimit(ClientResponse resp) {
        var remaining = resp.headers().asHttpHeaders().getFirst("X-RateLimit-Remaining");
        this.guage.set(Integer.parseInt(remaining));
    }
}