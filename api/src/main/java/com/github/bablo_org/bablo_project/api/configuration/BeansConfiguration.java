package com.github.bablo_org.bablo_project.api.configuration;

import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class BeansConfiguration {

    @Bean
    @Profile("local")
    GoogleCredentials credentialsLocal() throws Exception {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("secrets.json");
        return GoogleCredentials.fromStream(serviceAccount);
    }

    @Bean
    @Profile("!local")
    GoogleCredentials credentialsCloud() throws Exception {
        return GoogleCredentials.getApplicationDefault();
    }

    @Bean
    Firestore firestoreClient(@Value("${project.id}") String projectId, GoogleCredentials credentials) {
        FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build();

        return firestoreOptions.getService();
    }

    @Bean
    Storage cloudStorage(@Value("${project.id}") String projectId, GoogleCredentials credentials) {
        StorageOptions options = StorageOptions.getDefaultInstance().toBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build();

        return options.getService();
    }

    @Bean
    FirebaseApp firebase(@Value("${project.id}") String projectId, GoogleCredentials credentials) {
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(firebaseOptions);
    }

    @Bean
    FirebaseAuth firebaseAuth(FirebaseApp firebase) {
        return FirebaseAuth.getInstance(firebase);
    }
}
