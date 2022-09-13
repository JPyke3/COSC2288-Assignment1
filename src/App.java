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

    public static void main(String[] args) throws Exception {
        mainMenu();
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
        System.out.println("searchByBrand");
    }

    public static void browseByVehicleType(Scanner scanner) {
        System.out.println("browseByVehicleType");
    }

    public static void filterByNumberOfPassenger(Scanner scanner) {
        System.out.println("filterByNumberOfPassenger");
    }

}