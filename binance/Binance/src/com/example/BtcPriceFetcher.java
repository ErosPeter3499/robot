import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class BtcPriceFetcher {

    private static final String BASE_URL = "https://api.binance.com";

    public static void main(String[] args) {
        try {
            double btcPrice = getBTCPrice();
            System.out.println("Current BTC Price: " + btcPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double getBTCPrice() throws IOException, InterruptedException {
        String endpoint = "/api/v3/ticker/price?symbol=BTCUSDT";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        return json.getDouble("price");
    }
}