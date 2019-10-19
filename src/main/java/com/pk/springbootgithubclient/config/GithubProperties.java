package com.pk.springbootgithubclient.config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("github")
@Validated
public class GithubProperties {
    @NotNull
    private String eventsUrl;
    @Email
    private String username;
    @NotNull
    private String password;
}