package com.test.pavedroad.intcollabsnotification.core;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@EnableConfigurationProperties
public class MongoProperties {
	private String username;
	private String password;
	private String hostname;
	private String database;
	private String uri;
	private Integer port;
	private Integer minPoolSize;
	private Integer maxPoolSize;
}
