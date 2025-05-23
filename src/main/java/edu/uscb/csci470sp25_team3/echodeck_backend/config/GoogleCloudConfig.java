// This configuration file sets up and authenticates Google Cloud Storage for uploading/downloading files

package edu.uscb.csci470sp25_team3.echodeck_backend.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Configuration
public class GoogleCloudConfig {

    @Bean
    public Storage googleCloudStorage() throws IOException {
        // Try environment variable first (for Render purposes)
        String path = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        // Then if not set use our local file path
        if (path == null || path.isBlank()) {
            path = "src/main/resources/echodeck-key.json";
        }
        
        // Load the credentials from the file
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(path));
        
        // Use the credentials to build and return a Google Cloud Storage object
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
