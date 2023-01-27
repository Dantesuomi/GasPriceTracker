import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, SQLException {

        String dbURL = "jdbc:mysql://localhost:3306/gas_tracker";
        String username = "root";
        String password = "12345";


        Connection conn = DriverManager.getConnection(dbURL,username,password);
        CircleK circleKGasStation = new CircleK(conn);
        FuelPrices circleKFuelPrices = circleKGasStation.getFuelPrices();

        System.out.println("Current CircleK fuel prices for \n95: " + circleKFuelPrices.getFuel95Price() + "\n98: "
                + circleKFuelPrices.getFuel98Price() + "\nDiesel: " + circleKFuelPrices.getFuelDieselPrice() + "\nat " + circleKFuelPrices.getTimestamp() + "\nLPG: " + circleKFuelPrices.getFuelLpgPrice());

        Virsi virsiGasStation = new Virsi(conn);
        FuelPrices virsiFuelPrices = virsiGasStation.getFuelPrices();

        System.out.println("Current Virši fuel prices for \n95: " + virsiFuelPrices.getFuel95Price() + "\n98: "
                + virsiFuelPrices.getFuel98Price() + "\nDiesel: " + virsiFuelPrices.getFuelDieselPrice() + "\nat " + virsiFuelPrices.getTimestamp() + "\nLPG: " + virsiFuelPrices.getFuelLpgPrice());

        Neste nesteGasStation = new Neste(conn);
        FuelPrices nesteFuelPrices = nesteGasStation.getFuelPrices();

        System.out.println("Current Neste fuel prices for \n95: " + nesteFuelPrices.getFuel95Price() + "\n98: "
                + nesteFuelPrices.getFuel98Price() + "\nDiesel: " + nesteFuelPrices.getFuelDieselPrice() + "\nat " + nesteFuelPrices.getTimestamp() + "\nLPG: " + nesteFuelPrices.getFuelLpgPrice());

        GotikaAuto gotikaAutoGasStation = new GotikaAuto(conn);
        FuelPrices gotikaAutoFuelPrices = gotikaAutoGasStation.getFuelPrices();

        System.out.println("Current GotikaAuto fuel prices for \n95: " + gotikaAutoFuelPrices.getFuel95Price() + "\n98: "
                + gotikaAutoFuelPrices.getFuel98Price() + "\nDiesel: " + gotikaAutoFuelPrices.getFuelDieselPrice() + "\nat " + gotikaAutoFuelPrices.getTimestamp() + "\nLPG: " + gotikaAutoFuelPrices.getFuelLpgPrice());

    }
}