package com.pk.springbootgithubclient;

import static org.mockito.Mockito.when;

import com.pk.springbootgithubclient.repository.GithubProjectRepository;
import com.pk.springbootgithubclient.routeconfig.Routes;
import com.pk.springbootgithubclient.service.GithubClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import static java.util.stream.Collectors.toList;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Mock private GithubProjectRepository repo;
	@Mock private GithubClient gitClient;
	private WebTestClient testClient;

	@Before
	public void setup() {
		this.testClient = WebTestClient.bindToRouterFunction(new Routes(repo, gitClient).routeConfig())
									.build();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldFetchRepos() {
		when(repo.findAll()).thenReturn(Flux.fromIterable(DataFixture.testProjects));
		var expectedRepos = DataFixture.testProjects.stream().map(p -> p.getRepoName()).collect(toList());
		testClient.get()
					.uri("/repos")
					.accept(MediaType.TEXT_PLAIN)
					.exchange()
					.expectStatus().is2xxSuccessful()
					.expectBodyList(String.class)
					.isEqualTo(expectedRepos);
	}	
}
