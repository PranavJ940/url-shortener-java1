package com.example.url_shortener_java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static UrlShortenerService urlService;

    public static void main(String[] args) throws IOException, SQLException {
        urlService = new UrlShortenerService();
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);

        server.createContext("/shorten", new ShortenHandler());
        server.createContext("/r", new RedirectHandler());
        server.createContext("/", new StaticHandler());

        server.setExecutor(null);
        server.start();
    }

    static class ShortenHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                String body = sb.toString();

                String url = body.replace("url=", "").trim();
                String shortCode = urlService.shorten(url);

                String response = "{\"shortUrl\": \"/r/" + shortCode + "\"}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class RedirectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String code = exchange.getRequestURI().getPath().replace("/r/", "");
            String url = urlService.getOriginalUrl(code);
            if (url != null) {
                exchange.getResponseHeaders().add("Location", url);
                exchange.sendResponseHeaders(302, -1);
            } else {
                String response = "URL not found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if ("/".equals(path) || "/index.html".equals(path)) {
                InputStream is = Main.class.getResourceAsStream("/frontend/index.html");
                if (is == null) {
                    String response = "Frontend not found";
                    exchange.sendResponseHeaders(404, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                byte[] bytes = is.readAllBytes();
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.getResponseBody().close();
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        }
    }
}