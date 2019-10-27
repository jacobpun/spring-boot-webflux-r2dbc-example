package com.pk.springbootgithubclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
public class DbConnectionFactoryConfig {
    @Bean
	public ConnectionFactory factory(DbProperties props) {
		return ConnectionFactories.get(
			ConnectionFactoryOptions.builder()
				.option(DRIVER, "pool")
				.option(PROTOCOL, "postgresql")
				.option(HOST, props.getHost())
				.option(PORT, props.getPort()) 
				.option(USER, props.getUserName())
				.option(PASSWORD, props.getPassword())
				.option(DATABASE, props.getDbName())
				.build()
		);
	}
}