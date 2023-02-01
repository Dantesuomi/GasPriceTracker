
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GotikaAuto {
    private final String gotikaAutoUrl = "https://www.gotikaauto.lv/index.php";

    private final String gasStationName = "GotikaAuto";

    private Connection conn;

    public GotikaAuto (Connection conn){
        this.conn = conn;
    }

    public FuelPrices getCurrentFuelPrices() throws SQLException {
        String sql = "SELECT fuel_95_price, fuel_diesel_price, date_time FROM fuel_prices WHERE gas_station = 'GotikaAuto' ORDER BY date_time DESC LIMIT 1";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        FuelPrices fuelPrices = new FuelPrices();
        fuelPrices.setFuel95Price(resultSet.getDouble("fuel_95_price"));

        fuelPrices.setFuelDieselPrice(resultSet.getDouble("fuel_diesel_price"));

        fuelPrices.setTimestamp(resultSet.getTimestamp("date_time").toLocalDateTime());

        return fuelPrices;
    }

    public FuelPrices getSpecificDateFuelPrices(LocalDate date) throws SQLException {
        String sql = "SELECT fuel_95_price, fuel_diesel_price, gas_station, date_time FROM fuel_prices WHERE gas_station = '" + gasStationName + "' AND DATE(date_time) = '" + date.toString() + "' ORDER  BY date_time DESC LIMIT 1;";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        try {
            FuelPrices fuelPrices = new FuelPrices();
            fuelPrices.setFuel95Price(resultSet.getDouble("fuel_95_price"));
            fuelPrices.setFuelDieselPrice(resultSet.getDouble("fuel_diesel_price"));
            fuelPrices.setTimestamp(resultSet.getTimestamp("date_time").toLocalDateTime());

            return fuelPrices;
        } catch (Exception e){
            return null;
        }
    }

    public ArrayList<FuelPrices> getDateRangePrices(LocalDate initialDate, LocalDate lastDate) throws SQLException {
        String sql = "SELECT fuel_95_price, fuel_diesel_price, gas_station, date_time FROM fuel_prices WHERE gas_station = '" + gasStationName + "'AND DATE(date_time) >='" + initialDate + "'AND DATE(date_time) <= '" + lastDate + "' ORDER  BY date_time DESC;";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ArrayList<FuelPrices> fuelPricesList = new ArrayList<FuelPrices>();
        try {
            while (resultSet.next()){
                FuelPrices fuelPrices = new FuelPrices();
                fuelPrices.setFuel95Price(resultSet.getDouble("fuel_95_price"));

                fuelPrices.setFuelDieselPrice(resultSet.getDouble("fuel_diesel_price"));

                fuelPrices.setTimestamp(resultSet.getTimestamp("date_time").toLocalDateTime());
                fuelPricesList.add(fuelPrices);
            }
            return fuelPricesList;
        } catch (Exception e){
            return null;
        }
    }

    public FuelPrices retrieveFuelPrices() throws IOException, InterruptedException, SQLException {
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
        fuelPrices.setTimestamp(LocalDateTime.now());
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

