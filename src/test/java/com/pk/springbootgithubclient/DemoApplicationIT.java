package com.pk.springbootgithubclient;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApplicationIT {

    @Autowired WebTestClient testClient;
    
    @Test
    @WithMockUser(roles = {"USER"})
    public void shouldFetchRepos() {
		var expectedRepos = List.of("spring-boot", "initializer", "sagan");
		testClient.get()
					.uri("/repos")
					.accept(MediaType.TEXT_PLAIN)
					.exchange()
					.expectStatus().is2xxSuccessful()
					.expectBodyList(String.class)
					.isEqualTo(expectedRepos);
	}    
}