package com.github.bablo_org.bablo_project.api.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private Firestore firestore;

    @GetMapping("/")
    String hello() {
        return "Hello!";
    }

    @GetMapping(path = "/db", produces = MediaType.APPLICATION_JSON_VALUE)
    Object db() throws Exception {
        Map<String, Object> data = new LinkedHashMap<>();

        for (CollectionReference next : firestore.listCollections()) {
            List<String> documents = next.get().get().getDocuments()
                    .stream()
                    .map(QueryDocumentSnapshot::toString)
                    .collect(Collectors.toList());
            data.put(next.getId(), documents);
        }

        return data;
    }
}
