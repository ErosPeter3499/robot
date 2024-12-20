import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServerOriginal {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/btc-price", new BtcPriceHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server original started on port 8081");

        //InetAddress localHost = InetAddress.getLocalHost();
        //String localIpAddress = localHost.getHostAddress();
        System.out.println("Server original running at http://" + InetAddress.getLocalHost().getHostAddress() + ":8080/btc-price");  
    }

    static class BtcPriceHandler implements HttpHandler {
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

        private double getBTCPrice() {
            // Replace this with the actual logic to get the BTC price from Binance
            return 50000.0; // Placeholder value
        }
    }
}