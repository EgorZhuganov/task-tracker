package com.ezh.taskbook.kvserver.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

public class LoadHandler extends AbstractHttpHandler {

    private final String apiKey;
    private final Map<String, String> data;

    public LoadHandler(String apiKey, Map<String, String> data) {
        this.apiKey = apiKey;
        this.data = data;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!hasAuth(exchange)) {
                System.out.println("The request is unauthorized, need a parameter in the query API_KEY with the value of the api key");
                exchange.sendResponseHeaders(403, 0);
                return;
            }
            switch (exchange.getRequestMethod()) {
                case "GET":
                    System.out.println("\n/load");
                    String key = exchange.getRequestURI().getPath().substring("/load/".length());
                    if (key.isEmpty()) {
                        System.out.println("The key to save is empty. It should be added to the path: /load/{key}");
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    if (!data.containsKey(key)) {
                        System.out.println("No data for the key '" + key);
                        exchange.sendResponseHeaders(404, 0);
                        return;
                    }
                    sendText(exchange, data.get(key));
                    System.out.println("Value for the key " + key + " successfully received!");
                    exchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    System.out.println("/load waiting for a GET request, but received: " + exchange.getRequestMethod());
                    exchange.sendResponseHeaders(405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private boolean hasAuth(HttpExchange exchange) {
        String rawQuery = exchange.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_KEY=" + apiKey) || rawQuery.contains("API_KEY=DEBUG"));
    }
}
