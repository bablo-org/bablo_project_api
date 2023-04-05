package com.github.bablo_org.bablo_project.api.job;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TestImportJson {
    public static void main(String[] args) {
        TestImportJson resource = new TestImportJson( );
        String jsonString = resource.getResource("api response.json");
        System.out.println(jsonString);
        System.out.println("finish");

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        System.out.println(jsonObject);
        System.out.println(jsonObject.get("phonetype"));
        System.out.println(jsonObject.get("cat"));

    }

    public String getResource(String resource) {
        StringBuilder json = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(resource)),
                            StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null)
                json.append(str);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Caught exception reading resource " + resource, e);
        }
        return json.toString();
    }

}
