package com.ezh.taskbook.kvserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class KVServer {

    public static final int PORT = 8078;
    private final String API_KEY;
    private HttpServer server;
    private Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        API_KEY = generateApiKey();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", (exchange) -> {
            try {
                System.out.println("\n/register");
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        sendText(exchange, API_KEY);
                        break;
                    default:
                        System.out.println("/register waiting for a GET request, but received " + exchange.getRequestMethod());
                        exchange.sendResponseHeaders(405, 0);
                }
            } finally {
                exchange.close();
            }
        });
        server.createContext("/save", (exchange) -> {
            try {
                System.out.println("\n/save");
                if (!hasAuth(exchange)) {
                    System.out.println("The request is unauthorized, need a parameter in the query API_KEY with the value of the api key");
                    exchange.sendResponseHeaders(403, 0);
                    return;
                }
                switch (exchange.getRequestMethod()) {
                    case "POST":
                        String key = exchange.getRequestURI().getPath().substring("/save/".length());  //.../save/<key>?API_KEY=4454545
                        if (key.isEmpty()) {
                            System.out.println("The key to save is empty. It is specified in the path: /save/{key}");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        String value = readText(exchange);
                        if (value.isEmpty()) {
                            System.out.println("Value to save is empty. Value is specified in the request body");
                            exchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        data.put(key, value);
                        System.out.println("Value for the key " + key + " successfully updated!");
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    default:
                        System.out.println("/save waiting for a POST request, but received: " + exchange.getRequestMethod());
                        exchange.sendResponseHeaders(405, 0);
                }
            } finally {
                exchange.close();
            }
        });
        server.createContext("/load", (exchange) -> {
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
                            System.out.println("The key to save is empty. It is specified in the path: /load/{key}");
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
        });
    }

    public void start() {
        System.out.println("Server starts (KVServer) on the port " + PORT);
        System.out.println("Open in the browser http://localhost:" + PORT + "/");
        System.out.println("API_KEY: " + API_KEY);
        server.start();
    }

    private String generateApiKey() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange exchange) {
        String rawQuery = exchange.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_KEY=" + API_KEY) || rawQuery.contains("API_KEY=DEBUG"));
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }
}
