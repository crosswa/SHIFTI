import java.util.Random;
import java.util.Scanner;



public class BoardShift {

//Alright you know the drill, grab the row and column, initialize the offsets first


public static final int FIRST_STRIP_ROW = 2;
public static final int NUM_SlIDERS = 4;
public static final int ROWS_PER_SLIDER = 2;

public static final int MIN_OFFSET = -2;
    public static final int MAX_OFFSET = 0;
    public static final int START_OFFSET = -1;




private static final int [] offsets = new int [NUM_SlIDERS];


static {
    resetOffSets();
}

public static void resetOffsets() {
        java.util.Arrays.fill(offsets, START_OFFSET);
    }

    public static int getOffSet(int slider) {
        return offsets[slider - 1];
    }


    public static int sliderForRow(int row) {
        int lastStripRow = FIRST_STRIP_ROW + NUM_SLIDERS * ROWS_PER_SLIDER - 1;
        if (row < FIRST_STRIP_ROW || row > lastStripRow) return 0;
        return (row - FIRST_STRIP_ROW) / ROWS_PER_SLIDER + 1;
    }

    public static void changeGrid(int[][] grid) {
        Random random = new Random();
        int lastStripRow = FIRST_STRIP_ROW + NUM_SLIDERS * ROWS_PER_SLIDER - 1;
        for (int row = FIRST_STRIP_ROW; row <= lastStripRow; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 1 && random.nextBoolean()) {
                    grid[row][col] = 0;
                }
            }
        }
    }

    public static boolean canShift(int slider, int amount) {
        int target = offsets[slider - 1] + amount;
        return target >= MIN_OFFSET && target <= MAX_OFFSET;
    }

    // +1 = slider moves right, -1 = left. Returns false and changes nothing if out of range.
    public static boolean shiftSlider(int slider, int amount) {
        if (!canShift(slider, amount)) return false;
        offsets[slider - 1] += amount;
        return true;
    }

    // 1 = tile, 0 = water
    public static int tileAt(int[][] grid, int boardRow, int boardCol) {
        int slider = sliderForRow(boardRow);
        int offset = (slider == 0) ? START_OFFSET : offsets[slider - 1];

        int matrixCol = boardCol - offset;
        if (matrixCol < 0 || matrixCol >= grid[boardRow].length) return 0;
        return grid[boardRow][matrixCol];
    }


    public static void doSliderShift(int starValue, Scanner input) {
        if (starValue == 1) {
            int slider = askSlider(input, "Which slider shifts once (1-4)? ");
            doOneShift(slider, input);
        } else if (starValue == 2) {
            System.out.println("Double star - choose:");
            System.out.println("  1) Shift one slider twice");
            System.out.println("  2) Shift two different sliders once each");
            int choice = readInt(input, "Choose 1 or 2: ", 1, 2);

            if (choice == 1) {
                int slider = askSlider(input, "Which slider shifts twice (1-4)? ");
                doOneShift(slider, input);
                doOneShift(slider, input);
            } else {
                int first = askSlider(input, "First slider to shift once (1-4)? ");
                doOneShift(first, input);

                int second;
                do {
                    second = askSlider(input, "Second slider to shift once (1-4)? ");
                    if (second == first) System.out.println("Choose a different slider.");
                } while (second == first);
                doOneShift(second, input);
            }
        }
    }


    private static void doOneShift(int slider, Scanner input) {
        while (true) {
            System.out.println("Shift slider " + slider + " which way? (current offset "
                    + getOffSet(slider) + ")");
            System.out.println("  1) Left   2) Right");
            int amount = readInt(input, "Choose 1 or 2: ", 1, 2) == 1 ? -1 : 1;

            if (shiftSlider(slider, amount)) {
                System.out.println("Slider " + slider + " is now at offset " + getOffSet(slider) + ".");
                return;
            }
            System.out.println("Slider " + slider + " can't go that way. Try the other direction.");
        }
    }


    private static int askSlider(Scanner input, String prompt) {
        return readInt(input, prompt, 1, NUM_SLIDERS);
    }

    private static int readInt(Scanner input, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                int val = input.nextInt();
                if (val >= min && val <= max) return val;
                System.out.println("Enter a number between " + min + " and " + max + ".");
            } else {
                System.out.println("Please enter a number.");
                input.next();
            }
        }
    }
}


