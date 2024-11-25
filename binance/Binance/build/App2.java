package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

public class App2 {
    private static final String API_KEY = "AyuexAroQnLqUw9FxyLNNra50ECgG3Np38uBgo2VgaL2MqozOLjnSkYA0LeEQLDx1";
    private static final String SECRET_KEY = "xMBIszrvGSGKGU23D7EsYyBtiYDgObtpC6R7tKHZPNGXG4BSahlVR9mpQCUPklgD1";
    private static final String BASE_URL = "https://api.binance.com";


    public static void main(String[] args) {
        System.out.println("Hello, World!!!!!!!!!!!!!!!!!!!faszom");
        accessBinanceServer();
    }

    public static void accessBinanceServer() {
        try {
            System.out.println("Getting BTC price...");
            String endpoint = "/api/v3/ticker/price?symbol=BTCUSDT";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + endpoint))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response body for debugging
            System.out.println("Response Body: " + response.body());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to get BTC price: " + response.statusCode());
            }

            JSONObject jsonObject = new JSONObject(response.body());
            System.out.println("BTC Price: " + jsonObject.getDouble("price"));
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double getBTCPrice() throws Exception {
        System.out.println("Getting BTC price...\n\n\n");
        String endpoint = "/api/v3/ticker/price?symbol=BTCUSDT";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + endpoint))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonObject = new JSONObject(response.body());
        return jsonObject.getDouble("price");
    }
    
    private static String generateSignature(String data, String key) throws Exception {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256HMAC.init(secretKey);
        byte[] hash = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}