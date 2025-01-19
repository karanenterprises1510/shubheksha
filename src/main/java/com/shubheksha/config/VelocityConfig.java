package com.shubheksha.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.velocity.VelocityEngineFactory;

@Configuration
public class VelocityConfig {
	@Bean
	VelocityEngine getVelocityEngine() throws IOException {
		final VelocityEngineFactory factory = new VelocityEngineFactory();
		final Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		factory.setVelocityProperties(props);
		return factory.createVelocityEngine();
	}
}
