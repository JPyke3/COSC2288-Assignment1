package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
 * App
 * 
 * 0.0.1
 *
 * 2022-09-11 17:34:42
 * 
 */

public class Main {

    // App wide variables
    static ArrayList<Vehicle> db = new ArrayList<Vehicle>(); // Database variable to store all car information

    public static void main(String[] args) {
        // Initially generate the database and fill the `db` variable
        try {
            db = generateDatabase(
                    "/Users/jacobpyke/Documents/University Projects/Further Programming/s3755145/Fleet.csv");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find the database file \"Fleet.csv\".");
            System.exit(0);
        }
        // Load the main menu
        mainMenu();
    }

    /**
     * Method used to generate the database and write it to the `db` variable
     * 
     * @throws FileNotFoundException
     */
    public static ArrayList<Vehicle> generateDatabase(String filePath) throws FileNotFoundException {
        ArrayList<Vehicle> tempDb = new ArrayList<Vehicle>();
        // Initialise single use scanner that will close by the end of this function and
        // load the CSV file
        Scanner scanner = new Scanner(
                new File(filePath));
        // Go the to the next line, the CSV title isn't needed to input all the data.
        scanner.nextLine();
        // While loop that will run when there is more information to be scanned
        while (scanner.hasNext()) {
            // Create an array of strings that is split by commas, this will store all car
            // information
            String[] item = scanner.nextLine().split(",");
            // Write the information to the DB
            Integer discount = null;
            try {
                discount = parseDiscount(item[10]);
            } catch (NumberFormatException e) {
                discount = 0;
            } finally {
                tempDb.add(new Vehicle(item[0], item[1], item[2], item[3], Integer.parseInt(item[4]),
                        Integer.parseInt(item[5]),
                        item[6], Integer.parseInt(item[7]), Integer.parseInt(item[8]), Integer.parseInt(item[9]),
                        discount));
            }
        }
        // Close the scanner to avoid memory leaks
        scanner.close();
        return tempDb;
    }

    /**
     * Method used to parse the string of a discount to an int if it is just a
     * number, if it isn't (For example, if "N/A" is used) then return 0
     * 
     * @param value The initial value to parse
     * @return 0 if there was any symbol other than numbers present, or the value of
     *         the string as a int
     */
    public static Integer parseDiscount(String value) throws NumberFormatException {
        return Integer.parseInt(value);
    }

    /**
     * Method used to draw the main menu, this will also be the loop that keeps the
     * app running unless the `exited` var is set to `true`
     */
    public static void mainMenu() {
        // Initialise a scanner, this scanner will be passed to all subsequent methods
        Scanner scanner = new Scanner(System.in);
        // Array of strings that stores the menu options to pass to the @ChoiceMenu
        String[] mainMenuOptions = { "Search by brand", "Browse by vehicle type", "Filter by number of passenger" };
        // Initialise a new menu that has all the options
        ChoiceMenu mainMenu = new ChoiceMenu("Main Menu", null, mainMenuOptions, scanner);
        // Define the exit variable
        var exited = true;
        while (exited) {
            try {
                // Draw the menu and write the result to the option variable
                MenuItem option = mainMenu.drawMenu();
                // Switch the option's result string
                switch (option.getItemName()) {
                    case "Search by brand":
                        searchByBrand(scanner);
                        break;
                    case "Browse by vehicle type":
                        browseByVehicleType(scanner);
                        break;
                    case "Filter by number of passenger":
                        filterByNumberOfPassenger(scanner);
                        break;
                    // If Exit is called, then close the scanner and exit the app
                    case "Exit":
                        exited = false;
                        scanner.close();
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                System.out.println("An unknown error occurred in the Menu" + e);
            }

        }

    }

    /**
     * Method used to take the user input, and then search the Database for all the
     * brands that match the text provided
     * 
     * @param scanner The scanner needs to be passed to the method to avoid a memory
     *                leak
     */
    public static void searchByBrand(Scanner scanner) {
        ArrayList<Vehicle> resultList = new ArrayList<Vehicle>(); // Variable for storing the list of results, i.e the
                                                                  // matches
        ArrayList<String> menuOptions = new ArrayList<String>(); // Variable for storing the menu options, this is the
                                                                 // strings used to create the menu
        String input = null;
        boolean valid = false;
        while (!valid) {
            // Write the user input to a string
            System.out.println("Please enter the brand name");
            input = scanner.nextLine();
            try {
                if (input.equals("")) {
                    throw new NoInputException("No Input Detected! Please try again.");
                } else {
                    valid = true;
                }
            } catch (NoInputException e) {
                System.out.println(e);
            }
        }
        // Loop through the DB
        for (Vehicle vehicle : db) {
            // If the vehicle's brand contains the user input, then add to resultList
            if (vehicle.getBrand().contains(input)) {
                resultList.add(vehicle);
            }
        }
        // Loop through the result list
        for (Vehicle vehicle : resultList) {
            // For every vehicle in the result list, construct a string used for the menu
            menuOptions.add(vehicle.getVehicleID() + " - " + vehicle.getBrand() + " " + vehicle.getModel() + " "
                    + vehicle.getType() + " with " + vehicle.getNoOfSeats() + " seats");
        }
        // Create an Array from the ArrayList that will be passed to the menu class
        String[] optionsArray = new String[menuOptions.size()];
        // Create the menu with the selected options
        ChoiceMenu brandMenu = new ChoiceMenu("Select from matching list", null, menuOptions.toArray(optionsArray),
                scanner);
        // Draw the menu and save the result to a variable
        MenuItem result = brandMenu.drawMenu();
        // If the result was exit, then just complete the function, otherwise call
        // bookVehicle with the result as a vehicle
        if (result.getIndex() != 9) {
            bookVehicle(resultList.get(result.getIndex()), scanner);
        }
    }

    /**
     * Method that will read every unique vehicle type from the database, then list
     * all the vehicles with a given type
     * 
     * @param scanner The scanner needs to be passed to the method to avoid a memory
     *                leak
     */
    public static void browseByVehicleType(Scanner scanner) {
        ArrayList<Vehicle> resultList = new ArrayList<Vehicle>(); // Variable for storing the list of results, i.e the
                                                                  // matches
        ArrayList<String> menuOptions = new ArrayList<String>(); // Variable for storing the menu options, this is the
                                                                 // strings used to create the menu
        ArrayList<String> types = new ArrayList<String>(); // Variable to store all the unique vehicle types from the DB
        // Loop through the database
        for (Vehicle vehicle : db) {
            // Check if the types array already contains the given type
            if (!types.contains(vehicle.getType())) {
                // If it does not, then add the type to the DB
                types.add(vehicle.getType());
            }
        }
        // Construct a new array with the same size as the ArrayList of types for use in
        // the menu
        String[] typeOptionsArray = new String[types.size()];
        // Construct a menu with all the types
        ChoiceMenu typeOptionsMenu = new ChoiceMenu("Browse by type of vehicle", null,
                types.toArray(typeOptionsArray), scanner);
        // Draw the menu and write the selection result to a variable
        MenuItem typeSelection = typeOptionsMenu.drawMenu();
        // Return the function if the user exited the selection
        if (typeSelection.getItemName().equals("Exit")) {
            return;
        }
        // Loop through the DB
        for (Vehicle vehicle : db) {
            // For every vehicle of the type that the user selected, add to the results list
            if (vehicle.getType().contains(typeSelection.getItemName())) {
                resultList.add(vehicle);
            }
        }
        // Loop through all the vehicles in the results list
        for (Vehicle vehicle : resultList) {
            // Construct menu options for each of the result vehicles that are more user
            // friendly for the menu
            menuOptions.add(vehicle.getVehicleID() + " - " + vehicle.getBrand() + " " + vehicle.getModel() + " "
                    + vehicle.getType() + " with " + vehicle.getNoOfSeats() + " seats");
        }
        // Construct a new array that is as long as the ArrayList to use for the menu
        String[] optionsArray = new String[menuOptions.size()];
        // Construct a menu for the resulting vehicles
        ChoiceMenu resultsMenu = new ChoiceMenu("Select from matching list", null,
                menuOptions.toArray(optionsArray),
                scanner);
        // Draw the menu and write the result to a variable
        MenuItem result = resultsMenu.drawMenu();
        // If the result was exit, then just complete the function, otherwise call
        // bookVehicle with the result as a vehicle
        if (result.getIndex() != 9) {
            bookVehicle(resultList.get(result.getIndex()), scanner);
        }

    }

    public static void filterByNumberOfPassenger(Scanner scanner) {
        ArrayList<Vehicle> resultList = new ArrayList<Vehicle>(); // Variable for storing the list of results, i.e the
                                                                  // matches
        ArrayList<String> menuOptions = new ArrayList<String>(); // Variable for storing the menu options, this is the
                                                                 // strings used to create the menu
        System.out.println("Please enter the number of passengers");
        boolean valid = false;
        int input = 0;
        while (!valid) {
            try {
                input = scanner.nextInt();
                if (input == 0) {
                    throw new OutOfRangeException("0 Passengers is not valid! Please enter a higher passenger count");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.");
            } catch (OutOfRangeException e) {
                System.out.println(e);
            }
        }
        // This nextLine() call needs to be made to discard the \n from the previous
        // nextInt() call
        scanner.nextLine();
        // Loop through the database
        for (Vehicle vehicle : db) {
            // If the vehicles number of seats is larger than the user input, add the result
            // to the results list
            if (vehicle.getNoOfSeats() > input) {
                resultList.add(vehicle);
            }
        }
        // Loop through the results list
        for (Vehicle vehicle : resultList) {
            // Construct menu options for each of the result vehicles that are more user
            // friendly for the menu
            menuOptions.add(vehicle.getVehicleID() + " - " + vehicle.getBrand() + " " + vehicle.getModel() + " "
                    + vehicle.getType() + " with " + vehicle.getNoOfSeats() + " seats");
        }
        // Construct a new array that is as long as the ArrayList to use for the menu
        String[] optionsArray = new String[menuOptions.size()];
        // Construct a menu for the resulting vehicles
        ChoiceMenu passengerNumberMenu = new ChoiceMenu("Select from matching list", null,
                menuOptions.toArray(optionsArray),
                scanner);
        // Draw the menu and write the result to a variable
        MenuItem result = passengerNumberMenu.drawMenu();
        // If the result was exit, then just complete the function, otherwise call
        // bookVehicle with the result as a vehicle
        if (result.getIndex() != 9) {
            bookVehicle(resultList.get(result.getIndex()), scanner);
        }
    }

    /**
     * Method used for the vehicle booking process. Used for accepting user
     * information and verifying a user would like to proceed with a booking
     * 
     * @param vehicle The vehicle the user would like to book
     * @param scanner The scanner needs to be passed to the method to avoid a memory
     *                leak
     */
    public static void bookVehicle(Vehicle vehicle, Scanner scanner) {
        // The date format used that the user provides
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // The date format used when the app outputs the dates in the summary
        DateTimeFormatter exportFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // The variables for the start and end dates
        LocalDate startDate;
        LocalDate endDate;
        // Variables used for the summary
        String givenName = "";
        String surname = "";
        String email = "";
        int passengerCount = 0;

        // Loop until user provides "good" input
        while (true) {
            System.out.println("Provide Dates");
            System.out.println("-------------------------");
            System.out.println("Start Date:");
            var input = scanner.nextLine();
            // Perform better date validation
            if (input != "") {
                startDate = LocalDate.parse(input, dateFormat);
                break;
            }
        }

        // Loop until user provides "good" input
        while (true) {
            System.out.println("End Date:");
            var input = scanner.nextLine();
            // Perform better date validation
            if (input != "") {
                endDate = LocalDate.parse(input, dateFormat);
                break;
            }
        }

        Period period = Period.between(startDate, endDate);

        HashMap<String, Integer> totalMap = calculateTotals(vehicle, period);

        System.out.println("-------------------------");
        System.out.println("Show vehicle details");
        System.out.println("-------------------------");
        System.out.println("Vehicle:\t\t" + vehicle.getVehicleID());
        System.out.println("Brand:\t\t\t" + vehicle.getBrand());
        System.out.println("Model:\t\t\t" + vehicle.getModel());
        System.out.println("Type of Vehicle:\t" + vehicle.getType());
        System.out.println("Year of Manufacture:\t" + vehicle.getYearOfManufacture());
        System.out.println("No. Of seats:\t\t" + vehicle.getNoOfSeats());
        System.out.println("Colour:\t\t\t" + vehicle.getColor());
        System.out.println(
                "Rental:\t\t\t" + "$" + totalMap.get("TotalRentalCost") + "\t($" + vehicle.getRentalPerDay() + " * "
                        + period.getDays() + " days)");
        System.out.println("Discounted Price:\t" + "$" + totalMap.get("RentWithDiscount") + "\t("
                + (vehicle.getDiscount() == 0 ? "Discount is not applicable" : ("$" + vehicle.getDiscount())) + ")");
        System.out.println("Insurance:\t\t" + "$" + totalMap.get("TotalInsuranceCost") + "\t($"
                + vehicle.getInsurancePerDay() + " * "
                + period.getDays() + " days)");
        System.out.println("Service Fee:\t\t$" + vehicle.getServiceFee());
        System.out.println("Total:\t\t\t$" + totalMap.get("Total"));

        // Loop until user provides "good" input
        while (true) {
            System.out.println("Would you like to reserve (Y/N)?");
            String input = scanner.nextLine();
            if (input.equals("N")) {
                return;
            } else if (input.equals("Y")) {
                break;
            }
        }

        // Loop until user provides "good" input
        while (true) {
            System.out.println("Please provide your given name");
            String input = scanner.nextLine();
            if (!input.equals("")) {
                givenName = input;
                break;
            }
        }

        // Loop until user provides "good" input
        while (true) {
            System.out.println("Please provide your surname");
            String input = scanner.nextLine();
            if (!input.equals("")) {
                surname = input;
                break;
            }
        }

        // Loop until user provides "good" input
        while (true) {
            System.out.println("Please provide your email");
            String input = scanner.nextLine();
            if (!input.equals("")) {
                email = input;
                break;
            }
        }

        while (true) {
            System.out.println("Please provide the passenger count");
            int input = scanner.nextInt();
            scanner.nextLine();
            if (input != 0) {
                input = passengerCount;
                break;
            }
        }

        while (true) {
            System.out.println("Confirm and Pay? (Y/N)?");
            String input = scanner.nextLine();
            if (input.equals("N")) {
                return;
            } else if (input.equals("Y")) {
                break;
            }
        }
        System.out.println("Congratulations! Your vehicle is booked. A receipt has been sent to your email.");
        System.out.println("We will soon be in touch before your pick-up date.");
        System.out.println("-------------------------");
        System.out.println("Confirmation Details");
        System.out.println("-------------------------");
        System.out.println("Name:\t\t\t" + givenName + " " + surname);
        System.out.println("Email:\t\t\t" + email);
        System.out.println("Your Vehicle:\t\t" + vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getType()
                + " with " + vehicle.getNoOfSeats() + " seats");
        System.out.println("Number of passengers:\t" + passengerCount);
        System.out.println("Pick-up Date:\t\t" + startDate.format(exportFormat));
        System.out.println("Drop-off Date:\t\t" + endDate.format(exportFormat));
        System.out.println("Total Payment:\t\t$" + totalMap.get("Total"));
    }

    public static HashMap<String, Integer> calculateTotals(Vehicle vehicle, Period period) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        result.put("TotalRentalCost", vehicle.getRentalPerDay() * period.getDays());
        result.put("RentWithDiscount", result.get("TotalRentalCost") - vehicle.getDiscount());
        result.put("TotalInsuranceCost", vehicle.getInsurancePerDay() * period.getDays());
        result.put("Total",
                result.get("RentWithDiscount") + result.get("TotalInsuranceCost") + vehicle.getServiceFee());
        return result;
    }
}