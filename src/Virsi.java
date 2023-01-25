import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Virsi {
    private final String virsiUrl = "https://www.virsi.lv/lv/privatpersonam/degviela/degvielas-un-elektrouzlades-cenas";

    public FuelPrices getFuelPrices() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(virsiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        String fuel95CssSelector = "div.prices-block:nth-child(2) > div:nth-child(1) > ul:nth-child(2) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > p:nth-child(2) > span:nth-child(2)";
        String fuel98CssSelector = "div.prices-block:nth-child(2) > div:nth-child(1) > ul:nth-child(2) > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > p:nth-child(2) > span:nth-child(2)";
        String fuelDieselCssSelector = "div.prices-block:nth-child(2) > div:nth-child(1) > ul:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > p:nth-child(2) > span:nth-child(2)";
        String fuelLpgCssSelector = "div.price-card:nth-child(5) > div:nth-child(1) > div:nth-child(1) > p:nth-child(2) > span:nth-child(2)";

        Document doc = Jsoup.parse(responseBody);

        Elements fuel95elements = doc.select(fuel95CssSelector);
        Double fuel95Price = Double.parseDouble(fuel95elements.html());

        Elements fuel98Elements = doc.select(fuel98CssSelector);
        Double fuel98Price = Double.parseDouble(fuel98Elements.html());

        Elements fuelDieselElements = doc.select(fuelDieselCssSelector);
        Double fuelDieselPrice = Double.parseDouble(fuelDieselElements.html());

        Elements fuelLpgElements = doc.select(fuelLpgCssSelector);
        Double fuelLpgPrice = Double.parseDouble(fuelLpgElements.html());

        FuelPrices fuelPrices = new FuelPrices(fuel95Price, fuel98Price, fuelDieselPrice, fuelLpgPrice);
        return fuelPrices;
    }
}
