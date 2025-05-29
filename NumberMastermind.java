import java.util.*;

public class NumberMastermind {

    // Stores the randomly generated secret number
    private static int[] secretNumber = new int[4];
    
    // Maximum number of attempts allowed
    private static final int MAX_ATTEMPTS = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        generateSecretNumber(); // Create the random 4-digit number

        System.out.println("Welcome to Number Mastermind!");
        System.out.println("Try to guess the 4-digit number with no repeating digits.");
        System.out.println("You will receive feedback in the form of 'bulls' and 'cows'.");
        System.out.println("Bulls = correct digit and correct position, Cows = correct digit but wrong position.");

        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("\nEnter your 4-digit guess: ");
            String input = scanner.nextLine();

            // Validate the input
            if (!isValidInput(input)) {
                System.out.println("Invalid input. Make sure it's a 4-digit number with no repeating digits.");
                continue;
            }

            int[] guess = convertStringToArray(input);

            // Calculate bulls and cows
            int bulls = countBulls(guess);
            int cows = countCows(guess) - bulls; // Subtract bulls since they are counted in cows too

            attempts++;
            System.out.println("Bulls: " + bulls + ", Cows: " + cows);

            // Check for win
            if (bulls == 4) {
                System.out.println("\nCongratulations! You guessed the number in " + attempts + " tries.");
                return;
            }
        }

        // If user didn't guess in time, show the number
        System.out.print("\nYou've used all attempts. The correct number was: ");
        for (int num : secretNumber) {
            System.out.print(num);
        }
        System.out.println();
    }

    // Generates a random 4-digit number with unique digits
    private static void generateSecretNumber() {
        Random rand = new Random();
        Set<Integer> usedDigits = new HashSet<>();

        int index = 0;
        while (index < 4) {
            int digit = rand.nextInt(10);
            if (!usedDigits.contains(digit)) {
                secretNumber[index] = digit;
                usedDigits.add(digit);
                index++;
            }
        }
    }

    // Validates that the input is a 4-digit number with no repeating digits
    private static boolean isValidInput(String input) {
        if (input.length() != 4 || !input.matches("\\d{4}")) {
            return false;
        }
        Set<Character> digits = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (!digits.add(c)) {
                return false; // Duplicate digit found
            }
        }
        return true;
    }

    // Converts the string input into an array of integers
    private static int[] convertStringToArray(String input) {
        int[] arr = new int[4];
        for (int i = 0; i < 4; i++) {
            arr[i] = input.charAt(i) - '0';
        }
        return arr;
    }

    // Counts how many digits are correct and in the correct position
    private static int countBulls(int[] guess) {
        int bulls = 0;
        for (int i = 0; i < 4; i++) {
            if (guess[i] == secretNumber[i]) {
                bulls++;
            }
        }
        return bulls;
    }

    // Counts how many digits are correct (regardless of position)
    private static int countCows(int[] guess) {
        int cows = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (guess[i] == secretNumber[j]) {
                    cows++;
                }
            }
        }
        return cows;
    }
}
