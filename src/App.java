import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
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

    static Collection<Vehicle> db = new ArrayList<Vehicle>();

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
        Collection<Vehicle> resultList = new ArrayList<Vehicle>();
        Collection<String> menuOptions = new ArrayList<String>();
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
        brandMenu.drawMenu();
    }

    public static void browseByVehicleType(Scanner scanner) {
        System.out.println("browseByVehicleType");
    }

    public static void filterByNumberOfPassenger(Scanner scanner) {
        System.out.println("filterByNumberOfPassenger");
    }

}