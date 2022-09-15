import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * App
 * 
 * 0.0.1
 *
 * 2022-09-11 17:34:42
 * 
 */

public class App {

    static ArrayList<Vehicle> db = new ArrayList<Vehicle>();

    public static void main(String[] args) throws Exception {
        generateDatabase();
        mainMenu();
    }

    public static void generateDatabase() throws FileNotFoundException {
        Scanner scanner = new Scanner(
                new File("/Users/jacobpyke/Documents/University Projects/Further Programming/s3755145/Fleet.csv"));
        scanner.nextLine();
        while (scanner.hasNext()) {
            String[] item = scanner.nextLine().split(",");
            db.add(new Vehicle(item[0], item[1], item[2], item[3], Integer.parseInt(item[4]),
                    Integer.parseInt(item[5]),
                    item[6], Integer.parseInt(item[7]), Integer.parseInt(item[8]), Integer.parseInt(item[9]),
                    parseDiscount(item[10])));
        }
        scanner.close();
    }

    public static int parseDiscount(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        String[] mainMenuOptions = { "Search by brand", "Browse by vehicle type", "Filter by number of passenger" };
        ChoiceMenu mainMenu = new ChoiceMenu("Main Menu", null, mainMenuOptions, scanner);
        var exited = true;
        while (exited) {
            try {
                MenuItem option = mainMenu.drawMenu();
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

    public static void searchByBrand(Scanner scanner) {
        ArrayList<Vehicle> resultList = new ArrayList<Vehicle>();
        ArrayList<String> menuOptions = new ArrayList<String>();
        System.out.println("Please enter the brand name");
        String input = scanner.nextLine();
        for (Vehicle vehicle : db) {
            if (vehicle.getBrand().contains(input)) {
                resultList.add(vehicle);
            }
        }
        for (Vehicle vehicle : resultList) {
            menuOptions.add(vehicle.getVehicleID() + " - " + vehicle.getBrand() + " " + vehicle.getModel() + " "
                    + vehicle.getType() + " with " + vehicle.getNoOfSeats() + " seats");
        }
        String[] optionsArray = new String[menuOptions.size()];
        ChoiceMenu brandMenu = new ChoiceMenu("Select from matching list", null, menuOptions.toArray(optionsArray),
                scanner);
        MenuItem result = brandMenu.drawMenu();
        if (result.getIndex() != 9) {
            bookVehicle(resultList.get(result.getIndex()), scanner);
        }
    }

    public static void browseByVehicleType(Scanner scanner) {
        ArrayList<Vehicle> resultList = new ArrayList<Vehicle>();
        ArrayList<String> menuOptions = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        for (Vehicle vehicle : db) {
            if (!types.contains(vehicle.getType())) {
                types.add(vehicle.getType());
            }
        }
        String[] typeOptionsArray = new String[types.size()];
        ChoiceMenu typeOptionsMenu = new ChoiceMenu("Browse by type of vehicle", null,
                types.toArray(typeOptionsArray),
                scanner);
        MenuItem typeSelection = typeOptionsMenu.drawMenu();
        if (typeSelection.getItemName() != "Exit") {
            for (Vehicle vehicle : db) {
                if (vehicle.getType().contains(typeSelection.getItemName())) {
                    resultList.add(vehicle);
                }
            }
            for (Vehicle vehicle : resultList) {
                menuOptions.add(vehicle.getVehicleID() + " - " + vehicle.getBrand() + " " + vehicle.getModel() + " "
                        + vehicle.getType() + " with " + vehicle.getNoOfSeats() + " seats");
            }
            String[] optionsArray = new String[menuOptions.size()];
            ChoiceMenu resultsMenu = new ChoiceMenu("Select from matching list", null,
                    menuOptions.toArray(optionsArray),
                    scanner);
            MenuItem result = resultsMenu.drawMenu();
            if (result.getIndex() != 9) {
                bookVehicle(resultList.get(result.getIndex()), scanner);
            }

        }
    }

    public static void filterByNumberOfPassenger(Scanner scanner) {
        ArrayList<Vehicle> resultList = new ArrayList<Vehicle>();
        ArrayList<String> menuOptions = new ArrayList<String>();
        System.out.println("Please enter the number of passengers");
        int input = scanner.nextInt();
        // This nextLine() call needs to be made to discard the \n from the previous
        // parseInt() call made from the menu.
        scanner.nextLine();
        for (Vehicle vehicle : db) {
            if (vehicle.getNoOfSeats() > input) {
                resultList.add(vehicle);
            }
        }
        for (Vehicle vehicle : resultList) {
            menuOptions.add(vehicle.getVehicleID() + " - " + vehicle.getBrand() + " " + vehicle.getModel() + " "
                    + vehicle.getType() + " with " + vehicle.getNoOfSeats() + " seats");
        }
        String[] optionsArray = new String[menuOptions.size()];
        ChoiceMenu passengerNumberMenu = new ChoiceMenu("Select from matching list", null,
                menuOptions.toArray(optionsArray),
                scanner);
        MenuItem result = passengerNumberMenu.drawMenu();
        if (result.getIndex() != 9) {
            bookVehicle(resultList.get(result.getIndex()), scanner);
        }
    }

    public static void bookVehicle(Vehicle vehicle, Scanner scanner) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter exportFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate;
        LocalDate endDate;
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

        int totalRentalCost = vehicle.getRentalPerDay() * period.getDays();
        int rentWithDiscount = totalRentalCost - vehicle.getDiscount();
        int totalInsuranceCost = vehicle.getInsurancePerDay() * period.getDays();
        int total = rentWithDiscount + totalInsuranceCost + vehicle.getServiceFee();

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
        System.out.println("Rental:\t\t\t" + "$" + totalRentalCost + "\t($" + vehicle.getRentalPerDay() + " * "
                + period.getDays() + " days)");
        System.out.println("Discounted Price:\t" + "$" + rentWithDiscount + "\t("
                + (vehicle.getDiscount() == 0 ? "Discount is not applicable" : ("$" + vehicle.getDiscount())) + ")");
        System.out.println("Insurance:\t\t" + "$" + totalInsuranceCost + "\t($" + vehicle.getInsurancePerDay() + " * "
                + period.getDays() + " days)");
        System.out.println("Service Fee:\t\t$" + vehicle.getServiceFee());
        System.out.println("Total:\t\t\t$" + total);

        while (true) {
            System.out.println("Would you like to reserve (Y/N)?");
            String input = scanner.nextLine();
            if (input.equals("N")) {
                return;
            } else if (input.equals("Y")) {
                break;
            }
        }

        String givenName = "";

        while (true) {
            System.out.println("Please provide your given name");
            String input = scanner.nextLine();
            if (!input.equals("")) {
                givenName = input;
                break;
            }
        }

        String surname = "";

        while (true) {
            System.out.println("Please provide your surname");
            String input = scanner.nextLine();
            if (!input.equals("")) {
                surname = input;
                break;
            }
        }

        String email = "";
        while (true) {
            System.out.println("Please provide your email");
            String input = scanner.nextLine();
            if (!input.equals("")) {
                email = input;
                break;
            }
        }

        int passengerCount = 0;

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
        System.out.println("Total Payment:\t\t$" + total);
    }
}