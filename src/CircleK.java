
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CircleK {
    private final String circleKUrl = "https://www.circlek.lv/priv%C4%81tperson%C4%81m/degvielas-cenas";

    public FuelPrices getFuelPrices() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(circleKUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        String fuel95CssSelector = ".ck-striped-table > tbody:nth-child(2) > tr:nth-child(1) > td:nth-child(2)";
        String fuel98CssSelector = ".ck-striped-table > tbody:nth-child(2) > tr:nth-child(2) > td:nth-child(2)";
        String fuelDieselCssSelector = ".ck-striped-table > tbody:nth-child(2) > tr:nth-child(3) > td:nth-child(2)";
        String fuelLpgCssSelector = ".ck-striped-table > tbody:nth-child(2) > tr:nth-child(5) > td:nth-child(2)";
        Document doc = Jsoup.parse(responseBody);

        Elements fuel95elements = doc.select(fuel95CssSelector);
        Double fuel95Price = Double.parseDouble(fuel95elements.html().replaceAll(" EUR", ""));

        Elements fuel98Elements = doc.select(fuel98CssSelector);
        Double fuel98Price = Double.parseDouble(fuel98Elements.html().replaceAll(" EUR", ""));

        Elements fuelDieselElements = doc.select(fuelDieselCssSelector);
        Double fuelDieselPrice = Double.parseDouble(fuelDieselElements.html().replaceAll(" EUR", ""));

        Elements fuelLpgElements = doc.select(fuelLpgCssSelector);
        Double fuelLpgPrice = Double.parseDouble(fuelLpgElements.html().replaceAll(" EUR", ""));

        FuelPrices fuelPrices = new FuelPrices(fuel95Price, fuel98Price, fuelDieselPrice, fuelLpgPrice);
        return fuelPrices;
    }
}
