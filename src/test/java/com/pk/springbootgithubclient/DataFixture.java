package com.pk.springbootgithubclient;

import java.util.List;

import com.pk.springbootgithubclient.model.GithubProject;

public class DataFixture {
    public static List<GithubProject> testProjects = List.of(
        new GithubProject(1L, "casenet", "server"),
        new GithubProject(2L, "casenet", "api")
    );
}