import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

//https://github.com/kzars/SGT_34_01/blob/master/src/db/Users.java

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {

        String dbURL = "jdbc:mysql://localhost:3306/gas_tracker";
        String username = "root";
        String password = "12345";


        Connection conn = DriverManager.getConnection(dbURL, username, password);
        //collectDataFromWebsites(conn);
        CircleK circleKGasStation = new CircleK(conn);
        FuelPrices debugCircleK = circleKGasStation.getCurrentFuelPrices();
        boolean quit = false;
        int choice = 0;
        //printInstructions();
        while (!quit) {

            System.out.println("Welcome! Choose your option ");

            try {
                printInstructions();
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        CurrentPrices(conn);
                        break;
                        //completed
                    case 2:
                        //TODO CREATE METHODS  FOR HISTORIC DATES
                        System.out.println("Enter your choice");
                        instructionHistoricalPrices();
                        int choiceHistoricalPrices = 0;
                        choiceHistoricalPrices = scanner.nextInt();
                        switch (choiceHistoricalPrices) {
                            case 1:
                                //choose specific date
                                specificDate();
                                break;
                            case 2:
                                //choose date range
                                dateRange();
                                int choiceDateRange = 0;
                                choiceDateRange = scanner.nextInt();
                                switch (choiceDateRange) {
                                    case 1:
                                        chooseGasStation();
                                        break;
                                }
                        }
                    case 3:
                        quit = true;
                        break;
                    default:
                        System.out.println("Input not valid (1-3)");
                        break;
                }

            }catch (InputMismatchException e) {
                System.err.println("Wrong input! Enter correct number, please");
                scanner.nextLine();
            }
        }

    }

    private static void collectDataFromWebsites(Connection conn) throws SQLException, IOException, InterruptedException{
        System.out.println("Retrieving data from gas stations");

        CircleK circleKGasStation = new CircleK(conn);
        circleKGasStation.retrieveFuelPrices();
        System.out.println("Retrieved data from \"CircleK\"");

        Virsi virsiGasStation = new Virsi(conn);
        virsiGasStation.retrieveFuelPrices();
        System.out.println("Retrieved data from \"Virši\"");

        Neste nesteGasStation = new Neste(conn);
        nesteGasStation.retrieveFuelPrices();
        System.out.println("Retrieved data from \"Neste\"");

        GotikaAuto gotikaAutoGasStation = new GotikaAuto(conn);
        gotikaAutoGasStation.retrieveFuelPrices();
        System.out.println("Retrieved data from \"GotikaAuto\"");
    }

    private static void printInstructions(){
        System.out.println("\nPress");
        System.out.println("\t 1 - To print current prices for gas stations");
        System.out.println("\t 2 - To get historical prices");
        System.out.println("\t 3 - To quit the application");
    }
    private static void CurrentPrices(Connection conn) throws SQLException {
        CircleK circleKGasStation = new CircleK(conn);
        FuelPrices circleKFuelPrices = circleKGasStation.getCurrentFuelPrices();

        System.out.println("Current CircleK fuel prices for \n95: " + circleKFuelPrices.getFuel95Price() + "\n98: "
                + circleKFuelPrices.getFuel98Price() + "\nDiesel: " + circleKFuelPrices.getFuelDieselPrice() + "\nLPG: " + circleKFuelPrices.getFuelLpgPrice() + "\nat " + circleKFuelPrices.getTimestamp() + "\n");

        Neste nesteGasStation = new Neste(conn);
        FuelPrices nesteFuelPrices = nesteGasStation.getCurrentFuelPrices();

        System.out.println("Current Neste fuel prices for \n95: " + nesteFuelPrices.getFuel95Price() + "\n98: "
                + nesteFuelPrices.getFuel98Price() + "\nDiesel: " + nesteFuelPrices.getFuelDieselPrice() + "\nLPG: " + "not available" + "\nat " + nesteFuelPrices.getTimestamp() + "\n");

        Virsi virsiGasStation = new Virsi(conn);
        FuelPrices virsiFuelPrices = virsiGasStation.getCurrentFuelPrices();

        System.out.println("Current Virši fuel prices for \n95: " + virsiFuelPrices.getFuel95Price() + "\n98: "
                + virsiFuelPrices.getFuel98Price() + "\nDiesel: " + virsiFuelPrices.getFuelDieselPrice() + "\nLPG: " + virsiFuelPrices.getFuelLpgPrice() + "\nat " + virsiFuelPrices.getTimestamp() + "\n");

        GotikaAuto gotikaAutoGasStation = new GotikaAuto(conn);
        FuelPrices gotikaAutoFuelPrices = gotikaAutoGasStation.getCurrentFuelPrices();

        System.out.println("Current GotikaAuto fuel prices for \n95: " + gotikaAutoFuelPrices.getFuel95Price() + "\n98: "
                + "not available" + "\nDiesel: " + gotikaAutoFuelPrices.getFuelDieselPrice() + "\nLPG: " + "not available" + "\nat " + gotikaAutoFuelPrices.getTimestamp() + "\n");

    }

    private static void instructionHistoricalPrices(){
        System.out.println("\nPress");
        System.out.println("\t 1 - To choose specific date");
        System.out.println("\t 2 - To enter date range");
        System.out.println("\t 3 - To quit the application");
    }

    private static void specificDate(){
        System.out.println("Enter date in mm.dd.yyyy format");
        //import SQL library Date?
        //date validation
        Date date = null;
        String specificDate = scanner.nextLine();
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            formatter.setLenient(false);
            date = formatter.parse(specificDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //how to get data for specific date?????????
    }

    private static void dateRange(){
        System.out.println("Enter first date in dd.MM.yyyy format");
        Date date = null;
        String dateRange1 = scanner.nextLine();
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            formatter.setLenient(false);
            date = formatter.parse(dateRange1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Enter second date in dd.MM.yyyy format");
        String dateRange2 = scanner.nextLine();
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            formatter.setLenient(false);
            date = formatter.parse(dateRange1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private static void chooseGasStation(){

        boolean quit = false;
        int gasStationChoice = 0;

        while (!quit){

            System.out.println("Choose gas station");
            System.out.println("\nPress");
            System.out.println("\t 1 - To choose " + "CircleK");
            System.out.println("\t 2 - To choose " + "Virši");
            System.out.println("\t 3 - To choose " + "Neste");
            System.out.println("\t 4 - To choose " + "GotikaAuto");

            try {
                gasStationChoice = scanner.nextInt();
                scanner.nextLine();

                switch (gasStationChoice){
                    case 1:
                        dateRangeCircleK();
                        break;
                    case 2:
                        dateRangeVirsi();
                        break;
                    case 3:
                        dateRangeNeste();
                        break;
                    case 4:
                        dateRangeGotikaAuto();
                        break;
                    default:
                        System.out.println("Input not valid (1-3)");
                        break;
                }
            }catch (InputMismatchException e){
                System.err.println("Wrong input! Integers only!");
                scanner.nextLine();
            }
        }
    }

    //how to get data for specific date range and gas station?????????
    private static void dateRangeCircleK(){

    }

    private static void dateRangeVirsi(){

    }

    private static void dateRangeNeste(){

    }

    private static void dateRangeGotikaAuto(){

    }




}