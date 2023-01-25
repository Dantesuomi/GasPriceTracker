
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Neste {
    private final String nesteUrl = "https://www.neste.lv/lv/content/degvielas-cenas";

    public FuelPrices getFuelPrices() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(nesteUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        String fuel95CssSelector = ".field__item > table:nth-child(3) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2) > p:nth-child(1) > span:nth-child(1) > strong:nth-child(1)";
        String fuel98CssSelector = ".field__item > table:nth-child(3) > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2) > p:nth-child(1) > span:nth-child(1) > strong:nth-child(1)";
        String fuelDieselCssSelector = ".field__item > table:nth-child(3) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2) > p:nth-child(1) > span:nth-child(1) > strong:nth-child(1)";

        Document doc = Jsoup.parse(responseBody);

        Elements fuel95elements = doc.select(fuel95CssSelector);
        Double fuel95Price = Double.parseDouble(fuel95elements.html());

        Elements fuel98Elements = doc.select(fuel98CssSelector);
        Double fuel98Price = Double.parseDouble(fuel98Elements.html());

        Elements fuelDieselElements = doc.select(fuelDieselCssSelector);
        Double fuelDieselPrice = Double.parseDouble(fuelDieselElements.html());



        FuelPrices fuelPrices = new FuelPrices(fuel95Price, fuel98Price, fuelDieselPrice);
        return fuelPrices;
    }
}

