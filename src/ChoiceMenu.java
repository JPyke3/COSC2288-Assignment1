
/*
 * Menu
 * 
 * 0.0.1
 *
 * 2022-09-11 17:34:42
 * 
 */
import java.util.Scanner;

public class ChoiceMenu {
    // Define Class Variables
    String title;
    String subtitle;
    String[] options;
    Scanner scanner;

    // Define initailiser
    ChoiceMenu(String title, String subtitle, String[] options, Scanner scanner) {
        this.title = title;
        this.subtitle = subtitle;
        this.options = options;
        this.scanner = scanner;
    }

    /***
     * Function to draw the menu and accept option input
     * 
     * @return The option as a string
     * @throws UnknownError
     */
    MenuItem drawMenu() throws UnknownError {
        // Define function variables
        boolean inputRecieved = true;
        int optionsIndex = 0;
        int input;
        String rawInputValue = "";
        // Print the title in a nice format
        System.out.println("-------------------------");
        System.out.println(title);
        System.out.println("-------------------------");

        // Print the subtitle, if present
        if (subtitle != null) {
            System.out.println(subtitle);
            System.out.println("-------------------------");
        }

        while (inputRecieved) {
            printOptions(optionsIndex);
            System.out.println("-------------------------");
            System.out.println("Please input your option");
            input = Integer.parseInt(scanner.next());
            if (input == 9) {
                inputRecieved = false;
                return new MenuItem(9, "Exit");
            } else {
                if (input == 4) {
                    if (options.length > optionsIndex * 3 + 3) {
                        optionsIndex += 1;
                    }
                } else if (input == 5) {
                    if (optionsIndex != 0) {
                        optionsIndex -= 1;
                    }
                } else {
                    inputRecieved = false;
                    return new MenuItem(input + (optionsIndex * 3), options[input + (optionsIndex * 3)]);
                }
            }

        }

        return new MenuItem(8, "Error");
    }

    /***
     * Prints the list of options for the user to input to
     * 
     * @param multiplier The index of menu option sets to multiply by
     */
    void printOptions(Integer multiplier) {
        Integer trueOptionMultiplier = multiplier * 3;
        if (options.length - trueOptionMultiplier > 3) {
            for (int i = 0; i < 3; i++) {
                System.out.println("  " + i + ") " + options[i + trueOptionMultiplier]);

            }
            System.out.println("  " + 4 + ") Next");
        } else {
            for (int i = trueOptionMultiplier; i < options.length; i++) {
                System.out.println("  " + (i - trueOptionMultiplier) + ") " + options[i]);
            }
        }
        if (multiplier > 0) {
            System.out.println("  " + 5 + ") Back");
        }
        System.out.println("  " + 9 + ") Exit");
    }
}
