package com.vault.hometask._config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.vault.hometask.*")
@EntityScan(basePackages = "com.vault.hometask.*")
@EnableJpaRepositories(basePackages = "com.vault.hometask.*")
public class VaultHomeprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaultHomeprojectApplication.class, args);
    }

}
