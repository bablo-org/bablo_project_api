package com.github.bablo_org.bablo_project.currency_job;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Application {

    public static void main(String[] args) throws IOException {
        if (1 == 2) {

// Setting URL
            String url_str = "https://v6.exchangerate-api.com/v6/ee38339f5db785019960d1dd/latest/USD";

// Making Request
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

// Convert to JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

// Accessing object
            //String req_result = jsonobj.get("result").getAsString();
            System.out.println(jsonobj);
        } else {

            TestImportJson resource = new TestImportJson( );
            String jsonString = resource.getResource("api response.json");

            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            System.out.println(jsonObject);

        }
    }
}
