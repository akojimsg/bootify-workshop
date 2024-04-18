package com.akojimsg.bootify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestApplication {
  @Bean
  @ServiceConnection
  PostgreSQLContainer postgreSQLContainer(){
    return new PostgreSQLContainer("postgres:16-alpine");
  }

  public static void main(String[] args) {
    SpringApplication
        .from(App::main)
        .with(TestApplication.class)
        .run(args);
  }
}
