import java.util.Random;
import java.util.Scanner;

/*
Dice layout:

Dice A: 1, 2, 3, 4, 5, 6
Dice B: 1, 2, 3, 4, 5, 6

When rolling the dice, player has option to
move one piece by the sum of the two numbers, or
move two separate pieces by the individual numbers
*/

public class diceCycle1 {

    private final Random rng = new Random();

    private int valueA;
    private int valueB;
    private boolean rolled = false;



    //rolls both dice and stores the results
    public void roll() {
        valueA = rng.nextInt(6) + 1;
        valueB = rng.nextInt(6) + 1;
        rolled = true;
    }

    public boolean hasRolled() {
        return rolled;
    }

    public int getValueA() {
        return valueA;
    }

    public int getValueB() {
        return valueB;
    }

    public int getSum() {
        return valueA + valueB;
    }

    //call at the end of a turn so the next player has to roll again
    public void reset() {
        valueA = 0;
        valueB = 0;
        rolled = false;
    }

    //placeholder actio

    private void movePiece(int pieceIndex, int spaces) {
        System.out.println("Moved piece " + (pieceIndex + 1) + " by " + spaces + " spaces");
    }

  

    //option 1 move one piece by the sum of both dice
    public boolean moveOnePieceBySum(int pieceIndex) {
        if (!rolled) {
            System.out.println("Roll the dice first.");
            return false;
        }
        if (!validPiece(pieceIndex)) {
            return false;
        }

        movePiece(pieceIndex, getSum());
        return true;
    }

    //option 2 move two different pieces, one by each die
    //pieceOne moves by die A, pieceTwo moves by die B
    public boolean moveTwoPiecesSeparately(int pieceOne, int pieceTwo) {
        if (!rolled) {
            System.out.println("Roll the dice first.");
            return false;
        }
        if (!validPiece(pieceOne) || !validPiece(pieceTwo)) {
            return false;
        }
        if (pieceOne == pieceTwo) {
            System.out.println("Choose two different pieces.");
            return false;
        }

        movePiece(pieceOne, valueA);
        movePiece(pieceTwo, valueB);
        return true;
    }

   
    private boolean validPiece(int pieceIndex) {
        if (pieceIndex < 0 || pieceIndex > 3) {
            System.out.println("Piece must be between 1 and 4.");
            return false;
        }
        return true;
    }


    //rolls the dice, then lets the player choose how to move
    public void takeTurn(Scanner input) {
        roll();
        System.out.println("Die A: " + valueA + "   Die B: " + valueB);
        System.out.println("1) Move one piece by the sum (" + getSum() + ")");
        System.out.println("2) Move two pieces, one by " + valueA + " and one by " + valueB);

        boolean moved = false;
        while (!moved) {
            int choice = readInt(input, "Choose 1 or 2: ");

            if (choice == 1) {
                // Player sees pieces as 1-4, PieceMovement uses 0-3
                int piece = readInt(input, "Which piece (1-4)? ") - 1;
                moved = moveOnePieceBySum(piece);
            } else if (choice == 2) {
                int pieceOne = readInt(input, "Piece to move " + valueA + " spaces (1-4): ") - 1;
                int pieceTwo = readInt(input, "Piece to move " + valueB + " spaces (1-4): ") - 1;
                moved = moveTwoPiecesSeparately(pieceOne, pieceTwo);
            } else {
                System.out.println("Please enter 1 or 2.");
            }
        }

        reset();
    }

    //keeps asking until the player types a whole number
    private int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                return input.nextInt();
            }
            System.out.println("Please enter a number.");
            input.next();
        }
    }

    //test
    public static void main(String[] args) {
        diceCycle1 dice = new diceCycle1();
        Scanner input = new Scanner(System.in);

        dice.takeTurn(input);

        input.close();
    }
}