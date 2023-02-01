import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {

        String dbURL = "jdbc:mysql://localhost:3306/gas_tracker";
        String username = "root";
        String password = "12345";


        Connection conn = DriverManager.getConnection(dbURL, username, password);
        collectDataFromWebsites(conn);
        boolean quit = false;
        int choice;
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
                    case 2:
                        System.out.println("Enter your choice");
                        instructionHistoricalPrices();
                        int choiceHistoricalPrices;
                        choiceHistoricalPrices = scanner.nextInt();
                        switch (choiceHistoricalPrices) {
                            case 1:
                                specificDate(conn);
                                break;
                            case 2:
                                //choose date range
                                dateRange(conn);
                                break;
                        }
                        break;
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

        Virsi virsiGasStation = new Virsi(conn);
        virsiGasStation.retrieveFuelPrices();

        Neste nesteGasStation = new Neste(conn);
        nesteGasStation.retrieveFuelPrices();

        GotikaAuto gotikaAutoGasStation = new GotikaAuto(conn);
        gotikaAutoGasStation.retrieveFuelPrices();
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

        Neste nesteGasStation = new Neste(conn);
        FuelPrices nesteFuelPrices = nesteGasStation.getCurrentFuelPrices();


        Virsi virsiGasStation = new Virsi(conn);
        FuelPrices virsiFuelPrices = virsiGasStation.getCurrentFuelPrices();


        GotikaAuto gotikaAutoGasStation = new GotikaAuto(conn);
        FuelPrices gotikaAutoFuelPrices = gotikaAutoGasStation.getCurrentFuelPrices();


        printPrices(circleKFuelPrices, nesteFuelPrices, virsiFuelPrices, gotikaAutoFuelPrices);

    }

    private static void printPrices(FuelPrices circleKFuelPrices, FuelPrices nesteFuelPrices, FuelPrices virsiFuelPrices, FuelPrices gotikaAutoFuelPrices){

        System.out.println("Current CircleK fuel prices for \n95: " + circleKFuelPrices.getFuel95Price() + "\n98: "
                + circleKFuelPrices.getFuel98Price() + "\nDiesel: " + circleKFuelPrices.getFuelDieselPrice() + "\nLPG: " + circleKFuelPrices.getFuelLpgPrice() + "\nat " + circleKFuelPrices.getTimestamp() + "\n");

        System.out.println("Current Neste fuel prices for \n95: " + nesteFuelPrices.getFuel95Price() + "\n98: "
                + nesteFuelPrices.getFuel98Price() + "\nDiesel: " + nesteFuelPrices.getFuelDieselPrice() + "\nLPG: " + "not available" + "\nat " + nesteFuelPrices.getTimestamp() + "\n");

        System.out.println("Current Virši fuel prices for \n95: " + virsiFuelPrices.getFuel95Price() + "\n98: "
                + virsiFuelPrices.getFuel98Price() + "\nDiesel: " + virsiFuelPrices.getFuelDieselPrice() + "\nLPG: " + virsiFuelPrices.getFuelLpgPrice() + "\nat " + virsiFuelPrices.getTimestamp() + "\n");

        System.out.println("Current GotikaAuto fuel prices for \n95: " + gotikaAutoFuelPrices.getFuel95Price() + "\n98: "
                + "not available" + "\nDiesel: " + gotikaAutoFuelPrices.getFuelDieselPrice() + "\nLPG: " + "not available" + "\nat " + gotikaAutoFuelPrices.getTimestamp() + "\n");
    }

    private static void instructionHistoricalPrices(){
        System.out.println("\nPress");
        System.out.println("\t 1 - To choose specific date");
        System.out.println("\t 2 - To enter date range");
        System.out.println("\t 3 - To quit the application");
    }

    private static void specificDate(Connection conn) throws SQLException{
        //FROM 27.01.2023
        System.out.println("Enter date in dd-MM-yyyy format");
        scanner.nextLine();
        LocalDate parsedDate = null;
        String date = scanner.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ROOT);
            parsedDate = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            System.out.println("Invalid date!");
            return;
        }
        CircleK circleK = new CircleK(conn);
        FuelPrices circleKFuelPrices = circleK.getSpecificDateFuelPrices(parsedDate);

        Neste neste = new Neste(conn);
        FuelPrices nesteFuelPrices = neste.getSpecificDateFuelPrices(parsedDate);

        Virsi virsi = new Virsi(conn);
        FuelPrices virsiFuelPrices = virsi.getSpecificDateFuelPrices(parsedDate);

        GotikaAuto gotikaAuto = new GotikaAuto(conn);
        FuelPrices gotikaAutoFuelPrices = gotikaAuto.getSpecificDateFuelPrices(parsedDate);
        if (circleKFuelPrices == null || nesteFuelPrices == null || virsiFuelPrices == null || gotikaAutoFuelPrices == null){
            System.out.println("Fuel prices are not available for this date");
        } else {
            printPrices(circleKFuelPrices, nesteFuelPrices, virsiFuelPrices, gotikaAutoFuelPrices);
        }
    }

    private static void dateRange(Connection conn) throws SQLException {
        System.out.println("Enter first date in dd-MM-yyyy format");
        scanner.nextLine();
        LocalDate initialParsedDate = null;
        String initialDate = scanner.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ROOT);
            initialParsedDate = LocalDate.parse(initialDate, formatter);
        } catch (Exception e) {
            System.out.println("Invalid date!");
            return; //get to the main menu
        }
        System.out.println("Enter second date in dd-MM-yyyy format");
        LocalDate lastParsedDate = null;
        String lastDate = scanner.nextLine();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ROOT);
            lastParsedDate = LocalDate.parse(lastDate, formatter);
        } catch (Exception e) {
            System.out.println("Invalid date!");
            return;
        }
        chooseGasStation(conn, initialParsedDate, lastParsedDate);
    }

    private static void chooseGasStation(Connection conn, LocalDate initialDate, LocalDate lastDate) throws SQLException {
        boolean quit = false;
        int gasStationChoice;
        while (!quit) {
            System.out.println("Choose gas station");
            System.out.println("\nPress");
            System.out.println("\t 1 - To choose " + "CircleK");
            System.out.println("\t 2 - To choose " + "Virši");
            System.out.println("\t 3 - To choose " + "Neste");
            System.out.println("\t 4 - To choose " + "GotikaAuto");

            try {
                gasStationChoice = scanner.nextInt();
                scanner.nextLine();

                switch (gasStationChoice) {
                    case 1:
                        dateRangeCircleK(conn, initialDate, lastDate);
                        break;
                    case 2:
                        dateRangeVirsi(conn, initialDate, lastDate);
                        break;
                    case 3:
                        dateRangeNeste(conn, initialDate, lastDate);
                        break;
                    case 4:
                        dateRangeGotikaAuto(conn, initialDate, lastDate);
                        break;
                    default:
                        System.out.println("Input not valid (1-4)");
                        break;
                }
                System.out.println("Do you want to get another prices? (y/n)");
                String answer = scanner.nextLine();
                if(answer.equals("y".toLowerCase())){
                    quit = true;
                }
                else {
                    System.exit(0);
                }

            } catch (InputMismatchException e) {
                System.err.println("Wrong input! Enter correct number, please");
                scanner.nextLine();
            }
        }
    }


    private static void dateRangeCircleK(Connection conn, LocalDate initialDate, LocalDate lastDate) throws SQLException {
        CircleK circleK = new CircleK(conn);
        ArrayList<FuelPrices> circleKDateRangePrices = circleK.getDateRangePrices(initialDate, lastDate);
        printDateRanges(circleKDateRangePrices, "CircleK");
    }

    private static void printDateRanges(ArrayList<FuelPrices> dateRangePrices, String gasStationName){
        System.out.println("Prices for specified date range for " + gasStationName + " :");
        for (FuelPrices fuelPrices : dateRangePrices){
            System.out.println("95: " + fuelPrices.getFuel95Price() + " 98: "
                    + fuelPrices.getFuel98Price() + " Diesel: " + fuelPrices.getFuelDieselPrice() + " LPG: " + fuelPrices.getFuelLpgPrice() + " at " + fuelPrices.getTimestamp());
        }
    }

    private static void dateRangeVirsi(Connection conn, LocalDate initialDate, LocalDate lastDate) throws SQLException {
        Virsi virsi = new Virsi(conn);
        ArrayList<FuelPrices> virsiDateRangePrices = virsi.getDateRangePrices(initialDate, lastDate);
        printDateRanges(virsiDateRangePrices, "Virši");
    }

    private static void dateRangeNeste(Connection conn, LocalDate initialDate, LocalDate lastDate) throws SQLException {
        Neste neste = new Neste(conn);
        ArrayList<FuelPrices> nesteDateRangePrices = neste.getDateRangePrices(initialDate, lastDate);
        printDateRanges(nesteDateRangePrices, "Neste");
    }

    private static void dateRangeGotikaAuto(Connection conn, LocalDate initialDate, LocalDate lastDate) throws SQLException {
        GotikaAuto gotikaAuto = new GotikaAuto(conn);
        ArrayList<FuelPrices> gotikaAutoDateRangePrices = gotikaAuto.getDateRangePrices(initialDate, lastDate);
        printDateRanges(gotikaAutoDateRangePrices, "GotikaAuto");
    }
}