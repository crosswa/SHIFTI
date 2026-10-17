import java.util.Random; 



public class Board_shift {

//Offsets for the board that are set as we described them on monday (hopefully this works?)


    public static final int FIRST_STRIP_ROW = 2;
    public static final int NUM_SLIDERS = 4;
    public static final int ROWS_PER_SLIDER = 2;


    public static final int MIN_OFFSET = -2;
    public static final int MAX_OFFSET = 0;
    public static final int START_OFFSET = -1;




    private static final int[] offsets = new int[NUM_SLIDERS];


    static {
        resetOffsets();
    }


    public static void resetOffsets () {
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
    for (int row = FIRST_STRIP_ROW; row <= lastStripRow; row++){
        for (int col = 0; col < grid[row].length; col++){
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
    
//If +1, slider moves right, -1, slider mmoves left. If it cant exceed the distance, it would have to go out of range
// and change nothing. 

    public static boolean shiftSlider(int slider, int amount) {
        if (!canShift(slider, amount)) return false;

        offsets[slider-1] += amount;
        return true; 
    }

//Whats on the board at the time, returns 1 for tiles and 0 for water
//boardCol is 0-12. Fixed rows use the starting offset, so board col 0 = matrix col 1.
    public static int tileAt(int[][] grid, int boardRow, int boardCol) {
        int slider = sliderForRow(boardRow);
        int offset = (slider == 0) ? START_OFFSET : offsets[slider - 1];

        int matrixCol = boardCol - offset;
        if (matrixCol < 0 || matrixCol >= grid[boardRow].length) return 0;
        return grid[boardRow][matrixCol];
        }
    }


    
    
    





