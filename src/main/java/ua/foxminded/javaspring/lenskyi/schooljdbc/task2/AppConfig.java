package ua.foxminded.javaspring.lenskyi.schooljdbc.task2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Configuration
@ComponentScan(basePackageClasses = AppConfig.class)
public class AppConfig {

    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String DB_USER;
    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;
    @Value("${spring.datasource.driver-class-name}")
    private String DB_DRIVER_NAME;

    @Bean
    public Random rand() {
        Random rand = new Random();
        try {
            rand = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rand;
    }
}