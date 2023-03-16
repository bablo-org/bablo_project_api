package com.github.bablo_org.bablo_project.api.configuration;

import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean
    GoogleCredentials credentials(@Value("${profile}") String profile) throws Exception {
        if ("local".equalsIgnoreCase(profile)) {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("secrets.json");
            return GoogleCredentials.fromStream(serviceAccount);
        } else {
            return GoogleCredentials.getApplicationDefault();
        }
    }

    @Bean
    Firestore firestoreClient(@Value("${project.id}") String projectId, GoogleCredentials credentials) {
        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build();

        return firestoreOptions.getService();
    }
}
