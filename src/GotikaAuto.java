
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GotikaAuto {
    private final String gotikaAutoUrl = "https://www.gotikaauto.lv/index.php";

    private final String gasStationName = "GotikaAuto";

    private Connection conn;

    public GotikaAuto (Connection conn){
        this.conn = conn;
    }

    public FuelPrices getFuelPrices() throws IOException, InterruptedException, SQLException {
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
        saveFuelPricesToDB(fuelPrices);
        return fuelPrices;
    }

    private void saveFuelPricesToDB (FuelPrices fuelPrices) throws SQLException {
        String sql = "INSERT INTO fuel_prices (fuel_95_price, fuel_diesel_price, gas_station, date_time) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setDouble(1, fuelPrices.getFuel95Price());
        statement.setDouble(2, fuelPrices.getFuelDieselPrice());
        statement.setString(3, gasStationName);
        statement.setString(4, fuelPrices.getTimestamp().toString());

        int rowInserted = statement.executeUpdate();

        if(rowInserted > 0){
            System.out.println(gasStationName + " prices were inserted");
        }else {
            System.out.println("Something went wrong");
        }
    }
}

