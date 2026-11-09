import java.util.Random;
import java.util.Scanner;

/*
Dice layout:

Dice A: 1, *, 3, **, 5, 6
Dice B: 1, 2, *, 4, **, 6

*  = shift one slider once
** = shift one slider twice, or shift two different sliders once each

Two numbers: player can move one piece by the sum of the two numbers, or
move two separate pieces by the individual numbers
One number and one star: player moves one piece by the number and uses the
star, in whichever order they choose
Two stars: player loses their turn
*/

public class diceCycle2 {

    //star faces are stored as negative values
    public static final int STAR = -1;
    public static final int DOUBLE_STAR = -2;

    private static final int[] FACES_A = {1, STAR, 3, DOUBLE_STAR, 5, 6};
    private static final int[] FACES_B = {1, 2, STAR, 4, DOUBLE_STAR, 6};

    private final Random rng = new Random();

    private int valueA;
    private int valueB;
    private boolean rolled = false;



    //rolls both dice and stores the results
    public void roll() {
        valueA = FACES_A[rng.nextInt(6)];
        valueB = FACES_B[rng.nextInt(6)];
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

    //both dice are numbers
    public int getSum() {
        return valueA + valueB;
    }

    public static boolean isStar(int value) {
        return value < 0;
    }

    public boolean bothNumbers() {
        return !isStar(valueA) && !isStar(valueB);
    }

    public boolean bothStars() {
        return isStar(valueA) && isStar(valueB);
    }

    //call at the end of a turn so the next player has to roll again
    public void reset() {
        valueA = 0;
        valueB = 0;
        rolled = false;
    }

    private static String faceToString(int value) {
        if (value == STAR) {
            return "*";
        }
        if (value == DOUBLE_STAR) {
            return "**";
        }
        return String.valueOf(value);
    }

    //placeholder actions 
    private void movePiece(int pieceIndex, int spaces) {
        System.out.println("Moved piece " + (pieceIndex + 1) + " by " + spaces + " spaces");
    }

    //left or right depends on the sliders orientation to the board
    private void shiftSlider(int sliderIndex) {
        System.out.println("Shifted slider " + (sliderIndex + 1));
    }



    //option 1 move one piece by the sum of both dice
    public boolean moveOnePieceBySum(int pieceIndex) {
        if (!rolled) {
            System.out.println("Roll the dice first.");
            return false;
        }
        if (!bothNumbers()) {
            System.out.println("Both dice need to be numbers.");
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
        if (!bothNumbers()) {
            System.out.println("Both dice need to be numbers.");
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

    //single star, shift slider once
    public boolean shiftOneSliderOnce(int slider) {
        if (!validSlider(slider)) {
            return false;
        }

        shiftSlider(slider);
        return true;
    }

    //double star option 1, shift one slider twice
    public boolean shiftOneSliderTwice(int slider) {
        if (!validSlider(slider)) {
            return false;
        }

        shiftSlider(slider);
        shiftSlider(slider);
        return true;
    }

    //double star option 2, shift two different sliders once each
    public boolean shiftTwoSliders(int sliderOne, int sliderTwo) {
        if (!validSlider(sliderOne) || !validSlider(sliderTwo)) {
            return false;
        }
        if (sliderOne == sliderTwo) {
            System.out.println("Choose two different sliders.");
            return false;
        }

        shiftSlider(sliderOne);
        shiftSlider(sliderTwo);
        return true;
    }


    private boolean validPiece(int pieceIndex) {
        if (pieceIndex < 0 || pieceIndex > 3) {
            System.out.println("Piece must be between 1 and 4.");
            return false;
        }
        return true;
    }

    private boolean validSlider(int sliderIndex) {
        if (sliderIndex < 0 || sliderIndex > 3) {
            System.out.println("Slider must be between 1 and 4.");
            return false;
        }
        return true;
    }


    //rolls the dice, then lets the player choose what to do
    public void takeTurn(Scanner input) {
        roll();
        System.out.println("Die A: " + faceToString(valueA) + "   Die B: " + faceToString(valueB));

        if (bothStars()) {
            System.out.println("Stars on both dice, you lose your turn.");
        } else if (bothNumbers()) {
            numberTurn(input);
        } else {
            mixedTurn(input);
        }

        reset();
    }

    //both dice are numbers
    private void numberTurn(Scanner input) {
        System.out.println("1) Move one piece by the sum (" + getSum() + ")");
        System.out.println("2) Move two pieces, one by " + valueA + " and one by " + valueB);

        boolean moved = false;
        while (!moved) {
            if (readChoice(input) == 1) {
                int piece = readPiece(input, "Which piece (1-4)? ");
                moved = moveOnePieceBySum(piece);
            } else {
                int pieceOne = readPiece(input, "Piece to move " + valueA + " spaces (1-4): ");
                int pieceTwo = readPiece(input, "Piece to move " + valueB + " spaces (1-4): ");
                moved = moveTwoPiecesSeparately(pieceOne, pieceTwo);
            }
        }
    }

    //one number and one star, player picks which to do first
    private void mixedTurn(Scanner input) {
        int number = isStar(valueA) ? valueB : valueA;
        int star = isStar(valueA) ? valueA : valueB;

        System.out.println("1) Move a piece " + number + " spaces first, then use " + faceToString(star));
        System.out.println("2) Use " + faceToString(star) + " first, then move a piece " + number + " spaces");

        if (readChoice(input) == 1) {
            pieceTurn(input, number);
            starTurn(input, star);
        } else {
            starTurn(input, star);
            pieceTurn(input, number);
        }
    }

    //asks which piece to move by a single die
    private void pieceTurn(Scanner input, int spaces) {
        int piece = readPiece(input, "Which piece moves " + spaces + " spaces (1-4)? ");
        while (!validPiece(piece)) {
            piece = readPiece(input, "Which piece moves " + spaces + " spaces (1-4)? ");
        }

        movePiece(piece, spaces);
    }

    //asks how to use a * or ** roll
    private void starTurn(Scanner input, int star) {
        boolean shifted = false;

        if (star == STAR) {
            while (!shifted) {
                int slider = readSlider(input, "Which slider to shift (1-4)? ");
                shifted = shiftOneSliderOnce(slider);
            }
            return;
        }

        System.out.println("1) Shift one slider twice");
        System.out.println("2) Shift two different sliders once each");

        while (!shifted) {
            if (readChoice(input) == 1) {
                int slider = readSlider(input, "Which slider (1-4)? ");
                shifted = shiftOneSliderTwice(slider);
            } else {
                int sliderOne = readSlider(input, "First slider (1-4): ");
                int sliderTwo = readSlider(input, "Second slider (1-4): ");
                shifted = shiftTwoSliders(sliderOne, sliderTwo);
            }
        }
    }

    //keeps asking until the player picks option 1 or 2
    private int readChoice(Scanner input) {
        while (true) {
            int choice = readInt(input, "Choose 1 or 2: ");
            if (choice == 1 || choice == 2) {
                return choice;
            }
            System.out.println("Please enter 1 or 2.");
        }
    }

    //player sees pieces and sliders as 1-4, the code uses 0-3
    private int readPiece(Scanner input, String prompt) {
        return readInt(input, prompt) - 1;
    }

    private int readSlider(Scanner input, String prompt) {
        return readInt(input, prompt) - 1;
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
        diceCycle2 dice = new diceCycle2();
        Scanner input = new Scanner(System.in);

        dice.takeTurn(input);

        input.close();
    }
}
