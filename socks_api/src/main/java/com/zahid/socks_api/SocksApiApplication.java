package com.zahid.socks_api;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocksApiApplication {
	public static final Logger logger = LoggerFactory.getLogger(SocksApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SocksApiApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
