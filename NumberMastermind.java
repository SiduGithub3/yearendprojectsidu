import java.util.*;

public class NumberMastermind {
    
    private static int[] secretNumber = new int[4];
    private static final int MAX_ATTEMPTS = 12; // Increased from 10 for better balance
    private static List<String> guessHistory = new ArrayList<>();
    private static List<String> feedbackHistory = new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Display enhanced welcome message and rules
        displayWelcomeMessage();
        
        // Ask for difficulty level
        int difficulty = selectDifficulty(scanner);
        generateSecretNumber(difficulty);
        
        int attempts = 0;
        boolean gameWon = false;
        
        while (attempts < MAX_ATTEMPTS && !gameWon) {
            displayGameState(attempts);
            
            System.out.print("Enter your guess: ");
            String input = scanner.nextLine().trim();
            
            // Handle special commands
            if (handleSpecialCommands(input)) {
                continue;
            }
            
            // Validate input
            String validationError = validateInput(input);
            if (validationError != null) {
                System.out.println("ERROR: " + validationError);
                continue;
            }
            
            int[] guess = convertStringToArray(input);
            attempts++;
            
            // Calculate feedback
            int bulls = countBulls(guess);
            int cows = countCows(guess) - bulls;
            
            // Store guess and feedback in history
            String feedback = String.format("Bulls: %d, Cows: %d", bulls, cows);
            guessHistory.add(input);
            feedbackHistory.add(feedback);
            
            // Display feedback with visual enhancement
            displayFeedback(bulls, cows, attempts);
            
            // Check for win condition
            if (bulls == 4) {
                displayWinMessage(attempts);
                gameWon = true;
            }
        }
        
        if (!gameWon) {
            displayLoseMessage();
        }
        
        // Ask if they want to play again
        askPlayAgain(scanner);
        scanner.close();
    }
    
    private static void displayWelcomeMessage() {
        System.out.println(">>> =====================================");
        System.out.println("    WELCOME TO NUMBER MASTERMIND!");
        System.out.println("===================================== <<<");
        System.out.println();
        System.out.println("GAME RULES:");
        System.out.println("   * Guess a 4-digit secret number");
        System.out.println("   * All digits must be unique (no repeats)");
        System.out.println("   * You have 12 attempts to crack the code");
        System.out.println();
        System.out.println("FEEDBACK SYSTEM:");
        System.out.println("   * BULLS = Correct digit in correct position");
        System.out.println("   * COWS  = Correct digit in wrong position");
        System.out.println();
        System.out.println("SPECIAL COMMANDS:");
        System.out.println("   * Type 'hint' for a strategic tip");
        System.out.println("   * Type 'history' to see your previous guesses");
        System.out.println("   * Type 'rules' to review the rules");
        System.out.println("   * Type 'quit' to exit the game");
        System.out.println();
    }
    
    private static int selectDifficulty(Scanner scanner) {
        System.out.println("SELECT DIFFICULTY LEVEL:");
        System.out.println("   1. [EASY]   - Secret number can start with 0");
        System.out.println("   2. [MEDIUM] - Secret number cannot start with 0");
        System.out.println("   3. [HARD]   - No zeros allowed anywhere");
        System.out.print("\nChoose difficulty (1-3): ");
        
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= 3) {
                    String[] levels = {"EASY", "MEDIUM", "HARD"};
                    System.out.println(">>> " + levels[choice-1] + " mode selected!\n");
                    return choice;
                }
                System.out.print("Please enter 1, 2, or 3: ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number (1-3): ");
            }
        }
    }
    
    private static void generateSecretNumber(int difficulty) {
        Random rand = new Random();
        Set<Integer> usedDigits = new HashSet<>();
        
        for (int i = 0; i < 4; i++) {
            int digit;
            do {
                digit = rand.nextInt(10);
                
                // Apply difficulty rules
                if (difficulty == 2 && i == 0 && digit == 0) continue; // Medium: no leading zero
                if (difficulty == 3 && digit == 0) continue; // Hard: no zeros at all
                
            } while (usedDigits.contains(digit));
            
            secretNumber[i] = digit;
            usedDigits.add(digit);
        }
    }
    
    private static void displayGameState(int attempts) {
        System.out.println("\n" + "=".repeat(50));
        System.out.printf(">>> ATTEMPT %d/%d <<<\n", attempts + 1, MAX_ATTEMPTS);
        
        if (!guessHistory.isEmpty()) {
            System.out.println("\nRECENT GUESSES:");
            int start = Math.max(0, guessHistory.size() - 3);
            for (int i = start; i < guessHistory.size(); i++) {
                System.out.printf("   %s -> %s\n", guessHistory.get(i), feedbackHistory.get(i));
            }
        }
        System.out.println("=".repeat(50));
    }
    
    private static boolean handleSpecialCommands(String input) {
        switch (input.toLowerCase()) {
            case "hint":
                displayHint();
                return true;
            case "history":
                displayHistory();
                return true;
            case "rules":
                displayRules();
                return true;
            case "quit":
                System.out.println("Thanks for playing! Goodbye!");
                System.exit(0);
                return true;
            default:
                return false;
        }
    }
    
    private static void displayHint() {
        System.out.println("\nSTRATEGY HINTS:");
        if (guessHistory.isEmpty()) {
            System.out.println("   * Start with digits spread across 0-9 (e.g., 1234, 5678)");
            System.out.println("   * This maximizes information from your first guess");
        } else {
            System.out.println("   * Focus on digits that gave you cows - they're in the secret!");
            System.out.println("   * Try different positions for digits that were cows");
            System.out.println("   * Eliminate digits that gave no bulls or cows");
        }
    }
    
    private static void displayHistory() {
        if (guessHistory.isEmpty()) {
            System.out.println("\nNo guesses yet!");
            return;
        }
        
        System.out.println("\nGUESS HISTORY:");
        for (int i = 0; i < guessHistory.size(); i++) {
            System.out.printf("   %2d. %s -> %s\n", i + 1, guessHistory.get(i), feedbackHistory.get(i));
        }
    }
    
    private static void displayRules() {
        System.out.println("\nDETAILED RULES:");
        System.out.println("   * The secret is a 4-digit number with unique digits");
        System.out.println("   * Enter exactly 4 digits (0-9) with no repeats");
        System.out.println("   * Bulls = right digit, right position");
        System.out.println("   * Cows = right digit, wrong position");
        System.out.println("   * Win by getting 4 bulls (perfect match!)");
    }
    
    private static String validateInput(String input) {
        // Check length
        if (input.length() != 4) {
            return "Input must be exactly 4 digits long.";
        }
        
        // Check if all characters are digits
        if (!input.matches("\\d{4}")) {
            return "Input must contain only digits (0-9).";
        }
        
        // Check for duplicate digits
        Set<Character> digits = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (!digits.add(c)) {
                return "All digits must be unique (no repeating digits).";
            }
        }
        
        return null; // Valid input
    }
    
    private static int[] convertStringToArray(String input) {
        int[] arr = new int[4];
        for (int i = 0; i < 4; i++) {
            arr[i] = input.charAt(i) - '0';
        }
        return arr;
    }
    
    private static int countBulls(int[] guess) {
        int bulls = 0;
        for (int i = 0; i < 4; i++) {
            if (guess[i] == secretNumber[i]) {
                bulls++;
            }
        }
        return bulls;
    }
    
    private static int countCows(int[] guess) {
        int cows = 0;
        for (int digit : guess) {
            for (int secretDigit : secretNumber) {
                if (digit == secretDigit) {
                    cows++;
                    break; // Count each secret digit only once
                }
            }
        }
        return cows;
    }
    
    private static void displayFeedback(int bulls, int cows, int attempt) {
        System.out.println();
        System.out.printf(">>> ATTEMPT %d RESULT:\n", attempt);
        System.out.printf("   Bulls: %d  |  Cows: %d\n", bulls, cows);
        
        // Visual progress indicator
        String progress = "#".repeat(bulls) + "-".repeat(4 - bulls);
        System.out.printf("   Progress: [%s] %d/4\n", progress, bulls);
        
        // Encouraging messages based on performance
        if (bulls == 3) {
            System.out.println("   >>> So close! One more digit to go!");
        } else if (bulls == 2) {
            System.out.println("   >>> Great progress! You're halfway there!");
        } else if (bulls + cows >= 3) {
            System.out.println("   >>> Good work! Try repositioning those digits!");
        } else if (bulls + cows == 0) {
            System.out.println("   >>> None of these digits are in the secret. Try completely different ones!");
        }
    }
    
    private static void displayWinMessage(int attempts) {
        System.out.println("\n>>> ===================================");
        System.out.println("    *** CONGRATULATIONS! YOU WON! ***");
        System.out.println("=================================== <<<");
        System.out.printf(">>> You cracked the code in %d attempts!\n", attempts);
        
        String secretCode = "";
        for (int digit : secretNumber) {
            secretCode += digit;
        }
        System.out.printf(">>> The secret number was: %s\n", secretCode);
        
        // Performance rating
        if (attempts <= 4) {
            System.out.println("*** MASTERMIND! Incredible performance!");
        } else if (attempts <= 7) {
            System.out.println("*** EXCELLENT! Great logical thinking!");
        } else if (attempts <= 10) {
            System.out.println("*** GOOD JOB! You figured it out!");
        } else {
            System.out.println("*** VICTORY! You never gave up!");
        }
    }
    
    private static void displayLoseMessage() {
        System.out.println("\n>>> ===================================");
        System.out.println("    *** GAME OVER - OUT OF ATTEMPTS ***");
        System.out.println("=================================== <<<");
        
        String secretCode = "";
        for (int digit : secretNumber) {
            secretCode += digit;
        }
        System.out.printf(">>> The secret number was: %s\n", secretCode);
        System.out.println(">>> Don't give up! Every master was once a beginner!");
    }
    
    private static void askPlayAgain(Scanner scanner) {
        System.out.print("\nWould you like to play again? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("y") || response.equals("yes")) {
            // Reset game state
            guessHistory.clear();
            feedbackHistory.clear();
            System.out.println("\n" + ">>> ".repeat(15));
            main(new String[]{}); // Restart the game
        } else {
            System.out.println("Thanks for playing Number Mastermind! See you next time!");
        }
    }
}