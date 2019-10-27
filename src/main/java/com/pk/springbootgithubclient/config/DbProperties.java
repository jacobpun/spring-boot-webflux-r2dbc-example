package com.pk.springbootgithubclient.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties("db")
@Data
@Validated
public class DbProperties {
    @NotNull private String host;
    @NotNull private int port;
    @NotNull private String userName;
    @NotNull private String password;
    @NotNull private String dbName;
}