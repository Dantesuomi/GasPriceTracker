
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;

public class CircleK {
    private final String circleKUrl = "https://www.circlek.lv/priv%C4%81tperson%C4%81m/degvielas-cenas";

    private final String gasStationName = "CircleK";

    private Connection conn;

    public CircleK (Connection conn){
        this.conn = conn;
    }

    public FuelPrices getCurrentFuelPrices() throws SQLException {
        String sql = "SELECT fuel_95_price, fuel_98_price, fuel_diesel_price, fuel_lpg_price, date_time FROM fuel_prices WHERE gas_station = 'CircleK' ORDER BY date_time DESC LIMIT 1";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        FuelPrices fuelPrices = new FuelPrices();
        fuelPrices.setFuel95Price(resultSet.getDouble("fuel_95_price"));
        fuelPrices.setFuel98Price(resultSet.getDouble("fuel_98_price"));
        fuelPrices.setFuelDieselPrice(resultSet.getDouble("fuel_diesel_price"));
        fuelPrices.setFuelLpgPrice(resultSet.getDouble("fuel_lpg_price"));

        fuelPrices.setTimestamp(resultSet.getTimestamp("date_time").toLocalDateTime());

        return fuelPrices;
    }

    public FuelPrices retrieveFuelPrices() throws IOException, InterruptedException, SQLException {
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
        saveFuelPricesToDB(fuelPrices);
        return fuelPrices;
    }

    private void saveFuelPricesToDB (FuelPrices fuelPrices) throws SQLException {
        String sql = "INSERT INTO fuel_prices (fuel_95_price, fuel_98_price, fuel_diesel_price, fuel_lpg_price, gas_station, date_time) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setDouble(1, fuelPrices.getFuel95Price());
        statement.setDouble(2, fuelPrices.getFuel98Price());
        statement.setDouble(3, fuelPrices.getFuelDieselPrice());
        statement.setDouble(4, fuelPrices.getFuelLpgPrice());
        statement.setString(5, gasStationName);
        statement.setString(6, fuelPrices.getTimestamp().toString());

        int rowInserted = statement.executeUpdate();

        if(rowInserted > 0){
            System.out.println(gasStationName + " prices were inserted");
        }else {
            System.out.println("Something went wrong");
        }
    }
}
