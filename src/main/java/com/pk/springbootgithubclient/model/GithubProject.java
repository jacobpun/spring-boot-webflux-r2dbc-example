package com.pk.springbootgithubclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubProject {    
    private Long id;
    private String orgName;
    private String repoName;
}