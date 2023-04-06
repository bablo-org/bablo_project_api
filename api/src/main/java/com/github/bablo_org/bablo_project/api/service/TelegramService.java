package com.github.bablo_org.bablo_project.api.service;

import static java.util.Arrays.asList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.model.telegram.BaseResponse;
import com.github.bablo_org.bablo_project.api.model.telegram.GetUpdatesResponse;
import com.github.bablo_org.bablo_project.api.model.telegram.TelegramToken;
import com.github.bablo_org.bablo_project.api.model.telegram.Update;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private static final int GET_UPDATES_LIMIT = 100;

    private static final String GET_UPDATES_TEMPLATE = "https://api.telegram.org/bot%s/getUpdates?limit=%s";

    private static final String SEND_MESSAGE_TEMPLATE = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

    private static final String HELLO_MESSAGE = "Hello! I will notify you if anything interesting will happen";

    private final HttpClient client = HttpClient.newHttpClient();

    private final TelegramToken token;

    private final ObjectMapper mapper;

    @SneakyThrows
    public String resolveId(String username) {
        String uri = String.format(GET_UPDATES_TEMPLATE, token.getToken(), GET_UPDATES_LIMIT);
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Update> updates = asList(mapper.readValue(response.body(), GetUpdatesResponse.class).result);

        Collections.reverse(updates);
        for (Update update : updates) {
            if (update.message.from.userName.equals(username)) {
                String userId = String.valueOf(update.message.from.id);
                sendMessage(HELLO_MESSAGE, userId);
                return userId;
            }
        }

        throw new RuntimeException("message from this user is not found in last " + GET_UPDATES_LIMIT);
    }

    @SneakyThrows
    public void sendMessage(String message, String userId) {
        String uri = String.format(SEND_MESSAGE_TEMPLATE, token.getToken(), userId, message);
        HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        BaseResponse<?> res = mapper.readValue(response.body(), BaseResponse.class);
        if (!res.ok) {
            throw new RuntimeException("bad response on sending message: " + res);
        }
    }
}
