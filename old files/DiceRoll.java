import java.util.List;
import java.util.Random;
import java.util.Scanner;

 /*
Dice layout:
Die A: 1, *, 3, **, 5, 6
Die B: 1, 2, *, 4, **, 6

Single * shifts one slider once
Double * shifts one slider twice, or two sliders once

If a player rolls stars on both dice, they lose their turn entirely

If a player rolls numbers on both dice, they can choose to move one piece by the sum of the two numbers,
or move two different pieces by the individual numbers

If a player rolls one star and one number, they can choose to shift a slider (1 shift for *, 2 shifts for **),
then move one piece by the number value (player chooses which action to do first, and which piece to move)

Each player has 4 pieces 
*/

public class DiceRoll {

    enum FaceType {NUMBER, STAR}


    static class Face {
        final FaceType type;
        final int value; // for NUMBER: the pips (1-6). for STAR: 1 (single) or 2 (double)

        Face(FaceType type, int value) {
            this.type = type;
            this.value = value;
        }

        static Face number(int n) {
            return new Face(FaceType.NUMBER, n);
        }

        static Face star(int count) {
            return new Face(FaceType.STAR, count);
        }

    
        public String toString() {
            if (type == FaceType.NUMBER) return String.valueOf(value);
            return value == 1 ? "*" : "**";
        }
    }     

    static class Die {
        private final Face[] faces;
        private final Random rng = new Random();

        Die(Face[] faces) {
            this.faces = faces;
        }

        Face roll() {
            return faces[rng.nextInt(6)];
        }
    }

    // Two dice written in outline
    static Die dieA() {
        return new Die(new Face[]{              
                Face.number(1), Face.star(1), Face.number(3),  //Dice A faces: 1, *, 3, **, 5, 6
                Face.star(2), Face.number(5), Face.number(6)
        });
    }
 
    static Die dieB() {
        return new Die(new Face[]{
                Face.number(1), Face.number(2), Face.star(1),  //Dice B faces: 1, 2, *, 4, **, 6
                Face.number(4), Face.star(2), Face.number(6)
        });
    }

    //Placeholder actions
    static void movePiece(int pieceNumber, int spaces) {
        System.out.println("Moved piece " + pieceNumber + " by " + spaces + " spaces");
    }

    static void shiftSlider(int sliderNumber, int shifts) {
        System.out.println("Shift slider " + sliderNumber + " by " + shifts + " step(s)");
    }

    //turn logic

    static void playTurn(Face rollA, Face rollB, Scanner scanner) {
        System.out.println("Die A: " + rollA + "   Die B: " + rollB);

        boolean starA = rollA.type == FaceType.STAR;
        boolean starB = rollB.type == FaceType.STAR;  //Prints both rolls, then checks each face's type 
                                                      // and stores them as true/false in staA/starB

        if (starA && starB) {
            //Both stars: lose the turn
            System.out.println("Both dice show stars. Turn lost!");
            return;
        }

        if (!starA && !starB) {
            //Both numbers: move one piece by the sum, or two pieces individually
            handleBothNumbers(rollA.value, rollB.value, scanner);
            return;
        }

        // One star, one number
        Face starFace = starA ? rollA : rollB;
        Face numberFace = starA ? rollB : rollA;
        handleStarAndNumber(starFace.value, numberFace.value, scanner);
    }
            
    private static void handleBothNumbers(int valueA, int valueB, Scanner scanner) {
        System.out.println("Choose how to use your roll:");
        System.out.println("  1) Move one piece by the sum (" + (valueA + valueB) +  ")");
        System.out.println("  2) Move two different pieces by " + valueA + " and " + valueB + " separately");
        int choice = readInt(scanner, 1, 2);

        if (choice == 1) {
            int piece = askPieceNumber(scanner, "Which piece moves " + (valueA + valueB) + "?");
            movePiece(piece, valueA + valueB);
        } else {
            int pieceOne = askPieceNumber(scanner, "Which piece moves " + valueA + "?");
            movePiece(pieceOne, valueA);
            int pieceTwo = askPieceNumber(scanner, "Which piece moves " + valueB + "?");
            movePiece(pieceTwo, valueB);
        }
    }

    private static void handleStarAndNumber(int starValue, int numberValue, Scanner scanner) {
        System.out.println("You rolled a star (" + (starValue == 1 ? "*" : "**") + ") and a " + numberValue + ".");
        System.out.println("Choose which to do first:");
        System.out.println("  1) Shift slider(s) first, then move a piece");
        System.out.println("  2) Move a piece first, then shift slider(s)");
        int order = readInt(scanner, 1, 2);

        if (order == 1) {
            doSliderShift(starValue, scanner);
            int piece = askPieceNumber(scanner, "Which piece moves " + numberValue + "?");
            movePiece(piece, numberValue);
        } else {
            int piece = askPieceNumber(scanner, "Which piece moves " + numberValue + "?");
            movePiece(piece, numberValue);
            doSliderShift(starValue, scanner);
        }
    }

    private static void doSliderShift(int starValue, Scanner scanner) {
        if (starValue == 1) {
            // Single star: one slider, one shift
            int slider = askSliderNumber(scanner, "Which slider shifts once?");
            shiftSlider(slider, 1);
        } else {
            // Double star: one slider twice, or two sliders once each
            System.out.println("Double star - choose:");
            System.out.println("  1) Shift one slider twice");
            System.out.println("  2) Shift two different sliders once each");
            int choice = readInt(scanner, 1, 2);

            if (choice == 1) {
                int slider = askSliderNumber(scanner, "Which slider shifts twice?");
                shiftSlider(slider, 2);
            } else {
                int sliderOne = askSliderNumber(scanner, "First slider to shift once?");
                shiftSlider(sliderOne, 1);
                int sliderTwo = askSliderNumber(scanner, "Second slider to shift once?");
                shiftSlider(sliderTwo, 1);
            }
        }
    }

    // ---- Input helpers ----

    private static int askPieceNumber(Scanner scanner, String prompt) {
        System.out.println(prompt + " (1-4)");
        return readInt(scanner, 1, 4);
    }

    private static int askSliderNumber(Scanner scanner, String prompt) {
        System.out.println(prompt + " (enter slider number)");
        return readInt(scanner, 1, 4);
    }

    private static int readInt(Scanner scanner, int min, int max) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {
                
            }
            System.out.println("Enter a number between " + min + " and " + max + ":");
        }
    }

    public static void main(String[] args) {
        Die a = dieA();
        Die b = dieB();
        Scanner scanner = new Scanner(System.in);

        Face rollA = a.roll();
        Face rollB = b.roll();

        playTurn(rollA, rollB, scanner);
}

}