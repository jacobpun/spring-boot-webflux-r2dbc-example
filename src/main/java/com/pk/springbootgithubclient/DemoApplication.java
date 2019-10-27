package com.pk.springbootgithubclient;
import com.pk.springbootgithubclient.config.DbProperties;
import com.pk.springbootgithubclient.config.GithubApiProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication 
@EnableR2dbcRepositories
@EnableConfigurationProperties({GithubApiProperties.class, DbProperties.class})
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
