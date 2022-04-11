package com.ezh.taskbook.kvserver.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class RegisterHandler extends AbstractHttpHandler {

    private final String apiKey;

    public RegisterHandler(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            System.out.println("\n/register");
            switch (exchange.getRequestMethod()) {
                case "GET":
                    sendText(exchange, apiKey);
                    break;
                default:
                    System.out.println("/register waiting for a GET request, but received " + exchange.getRequestMethod());
                    exchange.sendResponseHeaders(405, 0);
            }
        } finally {
            exchange.close();
        }
    }
}
