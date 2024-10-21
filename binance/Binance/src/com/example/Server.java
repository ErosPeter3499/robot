import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
    private static Integer btcCounter = 0; 
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/btc-price", new BtcPriceHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Serveruj started on port 8080");
    }

    static class BtcPriceHandler implements HttpHandler {
        private static final String BASE_URL = "https://api.binance.com";
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                double btcPrice = getBTCPrice();
                JSONObject json = new JSONObject();
                json.put("price", btcPrice);

                String response = json.toString();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }

        private double getBTCPrice2() {
            // Replace this with the actual logic to get the BTC price from Binance
            // return 50000.0; // Placeholder value
            System.out.println("Getting BTC price...ttt"+btcCounter);
            String endpoint = "/api/v3/ticker/price?symbol=BTCUSDT";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .build();
            //btcCounter++;
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject json = new JSONObject(response.body());
                return json.getDouble("price");
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0;
            }
        }
    }
}