import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException  {
        Virsi virsiGasStation = new Virsi();
        FuelPrices virsiFuelPrices = virsiGasStation.getFuelPrices();

        System.out.println("Current Virši fuel prices for \n95: " + virsiFuelPrices.getFuel95Price() + "\n98: "
                + virsiFuelPrices.getFuel98Price() + "\nDiesel: " + virsiFuelPrices.getFuelDieselPrice() + "\nat " + virsiFuelPrices.getTimestamp() + "\nLPG: " + virsiFuelPrices.getFuelLpgPrice());


        CircleK circleKGasStation = new CircleK();
        FuelPrices circleKFuelPrices = circleKGasStation.getFuelPrices();

        System.out.println("Current CircleK fuel prices for \n95: " + circleKFuelPrices.getFuel95Price() + "\n98: "
                + circleKFuelPrices.getFuel98Price() + "\nDiesel: " + circleKFuelPrices.getFuelDieselPrice() + "\nat " + circleKFuelPrices.getTimestamp() + "\nLPG: " + circleKFuelPrices.getFuelLpgPrice());

        Neste nesteGasStation = new Neste();
        FuelPrices nesteFuelPrices = nesteGasStation.getFuelPrices();

        System.out.println("Current Neste fuel prices for \n95: " + nesteFuelPrices.getFuel95Price() + "\n98: "
                + nesteFuelPrices.getFuel98Price() + "\nDiesel: " + nesteFuelPrices.getFuelDieselPrice() + "\nat " + nesteFuelPrices.getTimestamp() + "\nLPG: " + nesteFuelPrices.getFuelLpgPrice());

        GotikaAuto gotikaAutoGasStation = new GotikaAuto();
        FuelPrices gotikaAutoFuelPrices = gotikaAutoGasStation.getFuelPrices();

        System.out.println("Current GotikaAuto fuel prices for \n95: " + gotikaAutoFuelPrices.getFuel95Price() + "\n98: "
                + gotikaAutoFuelPrices.getFuel98Price() + "\nDiesel: " + gotikaAutoFuelPrices.getFuelDieselPrice() + "\nat " + gotikaAutoFuelPrices.getTimestamp() + "\nLPG: " + gotikaAutoFuelPrices.getFuelLpgPrice());

    }
}