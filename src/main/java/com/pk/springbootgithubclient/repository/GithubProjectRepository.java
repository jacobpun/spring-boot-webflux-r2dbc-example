package com.pk.springbootgithubclient.repository;

import com.pk.springbootgithubclient.model.GithubProject;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface GithubProjectRepository extends ReactiveCrudRepository<GithubProject, Long>{
    @Query("SELECT ID, ORG_NAME, REPO_NAME FROM GITHUB_PROJECT WHERE REPO_NAME = $1")
    Mono<GithubProject> findByRepoName(String repoName);
}