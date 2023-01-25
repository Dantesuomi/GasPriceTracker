
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GotikaAuto {
    private final String gotikaAutoUrl = "https://www.gotikaauto.lv/index.php";

    public FuelPrices getFuelPrices() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(gotikaAutoUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        String fuel95CssSelector = ".nav__prices > a:nth-child(1) > div:nth-child(1) > dd:nth-child(2)";

        String fuelDieselCssSelector = ".nav__prices > a:nth-child(1) > div:nth-child(2) > dd:nth-child(2)";

        Document doc = Jsoup.parse(responseBody);

        Elements fuel95elements = doc.select(fuel95CssSelector);
        Double fuel95Price = Double.parseDouble(fuel95elements.html().replaceAll(",", ".").replaceAll(" €", ""));


        Elements fuelDieselElements = doc.select(fuelDieselCssSelector);
        Double fuelDieselPrice = Double.parseDouble(fuelDieselElements.html().replaceAll(",", ".").replaceAll(" €", ""));

        FuelPrices fuelPrices = new FuelPrices();
        fuelPrices.setFuel95Price(fuel95Price);
        fuelPrices.setFuelDieselPrice(fuelDieselPrice);
        return fuelPrices;
    }
}

