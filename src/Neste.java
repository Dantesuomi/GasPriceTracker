
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;

public class Neste {
    private final String nesteUrl = "https://www.neste.lv/lv/content/degvielas-cenas";

    private final String gasStationName = "Neste";

    private Connection conn;

    public Neste (Connection conn){
        this.conn = conn;
    }

    public FuelPrices getCurrentFuelPrices() throws SQLException {
        String sql = "SELECT fuel_95_price, fuel_98_price, fuel_diesel_price, date_time FROM fuel_prices WHERE gas_station = 'Neste' ORDER BY date_time DESC LIMIT 1";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        FuelPrices fuelPrices = new FuelPrices();
        fuelPrices.setFuel95Price(resultSet.getDouble("fuel_95_price"));
        fuelPrices.setFuel98Price(resultSet.getDouble("fuel_98_price"));
        fuelPrices.setFuelDieselPrice(resultSet.getDouble("fuel_diesel_price"));


        fuelPrices.setTimestamp(resultSet.getTimestamp("date_time").toLocalDateTime());

        return fuelPrices;
    }

    public FuelPrices retrieveFuelPrices() throws IOException, InterruptedException, SQLException {
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
        saveFuelPricesToDB(fuelPrices);
        return fuelPrices;
    }
    private void saveFuelPricesToDB (FuelPrices fuelPrices) throws SQLException {
        String sql = "INSERT INTO fuel_prices (fuel_95_price, fuel_98_price, fuel_diesel_price, gas_station, date_time) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setDouble(1, fuelPrices.getFuel95Price());
        statement.setDouble(2, fuelPrices.getFuel98Price());
        statement.setDouble(3, fuelPrices.getFuelDieselPrice());
        statement.setString(4, gasStationName);
        statement.setString(5, fuelPrices.getTimestamp().toString());

        int rowInserted = statement.executeUpdate();

        if(rowInserted > 0){
            System.out.println(gasStationName + " prices were inserted");
        }else {
            System.out.println("Something went wrong");
        }
    }
}

