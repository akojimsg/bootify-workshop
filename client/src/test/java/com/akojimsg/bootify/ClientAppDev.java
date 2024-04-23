package com.akojimsg.bootify;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientAppDev {
  public static void main(String[] args) {
    SpringApplication
        .from(ClientApp::main)
        .with(ClientAppDev.class)
        .run(args);
  }
}
