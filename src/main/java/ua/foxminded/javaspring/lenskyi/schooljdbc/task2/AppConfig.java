package ua.foxminded.javaspring.lenskyi.schooljdbc.task2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.FileReader;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.RandomDataCreator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Configuration
@ComponentScan(basePackageClasses = AppConfig.class)
@EnableAspectJAutoProxy
public class AppConfig {

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

    @Bean
    public FileReader fileReader() {
        return new FileReader();
    }

    @Bean
    public RandomDataCreator randomDataCreator() {
        return new RandomDataCreator(fileReader(), rand());
    }
}