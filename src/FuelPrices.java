import java.time.LocalDateTime;

public class FuelPrices {
    private double fuel95Price = 0;
    private double fuel98Price = 0;
    private double fuelDieselPrice = 0;
    private double fuelLpgPrice = 0;
    private LocalDateTime timestamp;

    FuelPrices(){
        this.timestamp = LocalDateTime.now();
    }

    FuelPrices(double fuel95Price, double fuel98Price, double fuelDieselPrice){
        this.fuel95Price = fuel95Price;
        this.fuel98Price = fuel98Price;
        this.fuelDieselPrice = fuelDieselPrice;
        this.timestamp = LocalDateTime.now();
    }

    FuelPrices(double fuel95Price, double fuel98Price, double fuelDieselPrice, double fuelLpgPrice){
        this.fuel95Price = fuel95Price;
        this.fuel98Price = fuel98Price;
        this.fuelDieselPrice = fuelDieselPrice;
        this.fuelLpgPrice= fuelLpgPrice;
        this.timestamp = LocalDateTime.now();
    }

    public void setFuel95Price(double fuel95Price){
        this.fuel95Price = fuel95Price;
    }

    public void setFuel98Price(double fuel98Price){
        this.fuel98Price = fuel98Price;
    }

    public void setFuelDieselPrice(double fuelDieselPrice){
        this.fuelDieselPrice = fuelDieselPrice;
    }

    public void setFuelLpgPrice(double fuelLpgPrice){
        this.fuelLpgPrice = fuelLpgPrice;
    }

    public double getFuel95Price(){
        return this.fuel95Price;
    }

    public double getFuel98Price(){
        return this.fuel98Price;
    }

    public double getFuelDieselPrice(){
        return this.fuelDieselPrice;
    }

    public double getFuelLpgPrice(){
        return this.fuelLpgPrice;
    }

    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }
}
