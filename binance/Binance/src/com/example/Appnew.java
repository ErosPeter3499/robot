import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Appnew {
    private static final String BASE_URL = "https://api.binance.com";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8086), 0);
        server.createContext("/btc-price", new BtcPriceHandler());
        server.createContext("/btc-price-history", new BtcPriceHistoryHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 808666....");
    }

    static class BtcPriceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                double btcPrice;
                try {
                    btcPrice = getBTCPrice();
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, -1); // 500 Internal Server Error
                    return;
                }

                JSONObject json = new JSONObject();
                json.put("price", btcPrice);
                String response = json.toString();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Add this line
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Add this line
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }

        private static double getBTCPrice() throws Exception {
            System.out.println("Getting BTC price...");
            String endpoint = "/api/v3/ticker/price?symbol=BTCUSDT";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + endpoint))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            return jsonObject.getDouble("price");
        }
    }

    static class BtcPriceHistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                JSONArray btcPriceHistory;
                try {
                    btcPriceHistory = getBTCPriceHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, -1); // 500 Internal Server Error
                    return;
                }

                String response = btcPriceHistory.toString();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Add this line
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Add this line
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }

        private static JSONArray getBTCPriceHistory() throws Exception {
            System.out.println("Getting BTC price history...");
            String endpoint = "/api/v3/klines?symbol=BTCUSDT&interval=1d&limit=10"; // Last 10 days
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + endpoint))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONArray(response.body());
        }
    }
}