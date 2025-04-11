package edu.uscb.csci470sp25_team3.echodeck_backend.config;

import java.io.FileInputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;

@Configuration
public class GoogleCloudConfig {
    @Bean
    public Storage googleCloudStorage() throws IOException {
        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream("src/main/resources/echodeck-key.json")))
                .build()
                .getService();
    }
}