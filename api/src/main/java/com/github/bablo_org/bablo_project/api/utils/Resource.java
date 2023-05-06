package com.github.bablo_org.bablo_project.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Resource {

    public String getResource(String resource) {
        StringBuilder json = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass()
                                .getClassLoader()
                                .getResourceAsStream(resource)),
                        StandardCharsets.UTF_8))) {
            String str;
            while ((str = in.readLine()) != null)
                json.append(str);
        } catch (IOException e) {
            throw new RuntimeException("Caught exception reading resource " + resource, e);
        }
        return json.toString();
    }
}
