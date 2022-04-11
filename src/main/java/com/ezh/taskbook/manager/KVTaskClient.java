package com.ezh.taskbook.manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String serverKey = "";
    private HttpRequest request;
    private HttpClient client;
    private URI url;

    public KVTaskClient(URI urlKvServer) {
        client = HttpClient.newHttpClient();
        url = urlKvServer;
        register();
    }

    private void register() {
        URI pathToRegister = URI.create(url.toString().concat("register"));
        request = HttpRequest.newBuilder()
                .GET()
                .uri(pathToRegister)
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            serverKey = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String json) {
        String pathToSave = url.toString().concat(String.format("save/%s?API_KEY=%s", key, serverKey));
        URI path = URI.create(pathToSave);
        request = HttpRequest.newBuilder()
                .uri(path)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load(String key) {
        String response = "";
        String pathToSave = url.toString().concat(String.format("load/%s?API_KEY=%s", key, serverKey));
        URI path = URI.create(pathToSave);
        request = HttpRequest.newBuilder()
                .uri(path)
                .GET()
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}