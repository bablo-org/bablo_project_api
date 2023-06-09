package com.github.bablo_org.bablo_project.api.configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.bablo_org.bablo_project.api.model.token.CurrencyRatesApiToken;
import com.github.bablo_org.bablo_project.api.model.token.TelegramToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    @Bean
    GoogleIdTokenVerifier googleIdTokenVerifier(@Value("${project.id}") String projectId) {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                                                 new GsonFactory())
                .setAudience(Collections.singletonList(projectId))
                .build();
    }

    @Bean
    @Primary
    ObjectMapper jsonMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Bean
    @SneakyThrows
    TelegramToken telegramToken() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("telegram-token.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            return new TelegramToken(br.readLine());
        }
    }

    @Bean
    @SneakyThrows
    CurrencyRatesApiToken currencyRatesApiToken() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("exchange-rates-token.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            return new CurrencyRatesApiToken(br.readLine());
        }
    }
}