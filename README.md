# Gas Price Tracker 

## Description
This application was created to collect and store information about the prices from various gas stations
websites. The main goal was to create program that can provide gas prices for specific period of time.

### Application uses these websites to collect gas prices: 
CircleK gas station: https://www.circlek.lv/priv%C4%81tperson%C4%81m/degvielas-cenas \
Virši gas station: https://www.virsi.lv/lv/privatpersonam/degviela/degvielas-un-elektrouzlades-cenas \
Neste gas station: https://www.neste.lv/lv/content/degvielas-cenas \
GotikaAuto gas station: https://www.gotikaauto.lv/index.php

## Installation

Application requires : 
* JDK 19.0.1
* MySql 8.0.32
* MySql connector for java
* IntelliJ IDEA 2022 

Application was tested on Windows 10 and using mentioned software versions

### Installation process: 
* Clone this repository using git using HTTPS or SSH 
* Import table schema gas_table_schema.sql into database
* In the Main.java configure database parameters 
* Launch the project using IntelliJ
* Enjoy :)

## Project creation process 
There were several stages of project creation:

1. Defining the application workflow scheme
2. Writing of container class - FuelPrices that contained:
   * properties (variables) for storing price data
   * several constructor methods with different parameters
   * "setter" and "getter" methods to set and retrieve data from object and use it in other classes
3. For each gas station were created separate classes. 
4. Next step - investigation, how to retrieve data from websites using jsoup library (retrieveFuelPrices method)
   * each website code was inspected to get a selector for fuel price (CSS selector)
   * because of website structure some prices were displayed with additional elements and were removed 
5. In Main.java I established a connection to database
6. In order to get possibility to store and collect data, SQL table schema was created
7. To store data for each gas station was created a method in each gas station class (saveFuelPricesToDB)
8. In Main.java was created a user interface using while loop and switch-case branching. For validation a try-catch 
exception handling was used
9. To give user information about current prices, a corresponding method was created (CurrentPrices)
10. To give user information about historical prices, application logic divides into two directions: 
    * get prices for specific date for all gas stations 
    * get prices for date range for single selected gas station 
11. Specific date gas prices retrieving process: 
    * created a method in each gas station class to get data from SQL (getSpecificDateFuelPrices)
    * created a method in Main.java calling previous method and using try-catch for validation
12. Date range gas prices retrieving process: 
    * created Arraylist in each gas station class in order to get multiple prices from database. For validation a try-catch
      exception handling was used
    * created method "dateRange" in Main.java 
13. After date range choice, user is proposed to choose a specific gas station, which led to creation of a "chooseGasStation" method
which contains switch-case branching and a try-catch validation. 
14. To quit the application a method System.exit is used.
15. To get date information, a LocalDate java library is used. For user to enter a date in a format "dd-MM-yyyy"
    a DateTimeFormatter java library is used.
