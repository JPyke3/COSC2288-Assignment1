public class Vehicle {
    String vehicleID;
    String brand;
    String model;
    String type;
    int yearOfManufacture;
    int noOfSeats;
    String color;
    int rentalPerDay;
    int insurancePerDay;
    int serviceFee;
    int discount;

    public Vehicle(String vehicleID, String brand, String model, String type, int yearOfManufacture, int noOfSeats,
            String color, int rentalPerDay, int insurancePerDay, int serviceFee, int discount) {
        this.vehicleID = vehicleID;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.yearOfManufacture = yearOfManufacture;
        this.noOfSeats = noOfSeats;
        this.color = color;
        this.rentalPerDay = rentalPerDay;
        this.insurancePerDay = insurancePerDay;
        this.serviceFee = serviceFee;
        this.discount = discount;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public String getColor() {
        return color;
    }

    public int getRentalPerDay() {
        return rentalPerDay;
    }

    public int getInsurancePerDay() {
        return insurancePerDay;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public int getDiscount() {
        return discount;
    }

}
