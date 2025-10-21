package com.example.ApiUser;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@SpringBootApplication
public class ApiUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUserApplication.class, args);
    }

    @Bean
    public Cloudinary cloudinary(@Value("${CLOUDINARY_URL}") String cloudinaryUrl) {
//        Dotenv dotenv = Dotenv.load();
//        String cloudinaryUrl = @Value("${CLOUDINARY_URL}");
        return new Cloudinary(cloudinaryUrl);
    }
}
