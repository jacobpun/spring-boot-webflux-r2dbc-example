package com.pk.springbootgithubclient.service;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

import java.util.concurrent.atomic.AtomicInteger;

import com.pk.springbootgithubclient.config.GithubApiProperties;
import com.pk.springbootgithubclient.model.RepositoryEvent;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Mono;

@Component
public class GithubClient {
    private final String eventsUrl;
    private final WebClient webClient;

    public GithubClient(GithubApiProperties gp, MeterRegistry registry) {
        var guage = registry.gauge("github.requests.remaining", new AtomicInteger(0));
        this.eventsUrl = gp.getEventsUrl();
        this.webClient = WebClient.builder()
                .filter(basicAuthentication(gp.getUsername(), gp.getPassword()))
                .filter(rateLimitFilter(guage))
                .build();
    }

    public Mono<RepositoryEvent[]> fetchEvents(String owner, String repo) {
        return this.webClient.get().uri(eventsUrl, owner, repo).retrieve().bodyToMono(RepositoryEvent[].class);
    }

    private ExchangeFilterFunction rateLimitFilter(AtomicInteger guage) {
        return (req, next) -> next.exchange(req).doOnNext(rsp -> setRateLimit(rsp, guage));
    }

    private void setRateLimit(ClientResponse resp, AtomicInteger guage) {
        var remaining = resp.headers().asHttpHeaders().getFirst("X-RateLimit-Remaining");
        guage.set(Integer.parseInt(remaining));
    }
}