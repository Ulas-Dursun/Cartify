package com.ulasdursun.cartify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CartifyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(CartifyApplication.class, args);
        Environment env = ctx.getEnvironment();
        String port = env.getProperty("server.port", "8080");

        System.out.println("""
                
                ╔══════════════════════════════════════════════╗
                ║           CARTIFY API STARTED                ║
                ╠══════════════════════════════════════════════╣
                ║  App:     http://localhost:%s              ║
                ║  Swagger: http://localhost:%s/swagger-ui.html║
                ╚══════════════════════════════════════════════╝
                """.formatted(port, port));
    }
}