
public class BoardLayout {

    /*
        SHIFTI board layout
        
        Board size: 15 rows x 13 columns
        
        13x 13 should be flip flopped and the 1 and 15th rows should be not accessable since they are shifted on and off the board.
        
        S = Stone/path (walkable)
        W = Water (blocked)
        
        0 = Red door    (top-left)
        1 = Teal door   (top-right)
        2 = Green door  (bottom-left)
        3 = Yellow door (bottom-right)
        
        Opposite door pairs:
        Red   <-> Yellow
        Green <-> Teal
        
        The green-to-teal route is connected.

        there is a default layout for now, in future cycles this class will be used 
        to create more randomized layouts and handles board shifting.
     */
    public static final int ROWS = 15;
    public static final int COLS = 13;
    private String[] board;

// The first and last physical rows are outside the playable board.
    public static final int FIRST_PLAYABLE_ROW = 1;
    public static final int LAST_PLAYABLE_ROW = 13;

    public static final int PLAYABLE_ROWS = 13;
    public static final int PLAYABLE_COLS = 13;

    private final BoardShift boardShift;

    public BoardLayout() {
        boardShift = new BoardShift();
    }

    public static final String[] DEFAULT = {
        "0SWWSSSWWSS1S",
        "SSWSSWSWSSSSS",
        "SWWSSSSWSWSSW",
        "SWWWSSSSSWSWS",
        "SSWWWSSSSWSSW",
        "WSWWWSWWWSWWW",
        "WSWWWSWWWSWWW",
        "WSSSSSSWWSWWW",
        "WSWWWSWWWSWWW",
        "WSWWWSWWWSWWW",
        "WSWWSSSWWSWWW",
        "WSWWWSWWWSWWW",
        "WSWWWSWWWSWWW",
        "2SSSWSWSWSS3S",
        "SSSWWSWSWSWSS"

    };

    public static boolean isPlayable(int row, int col) {
        return row >= FIRST_PLAYABLE_ROW
                && row <= LAST_PLAYABLE_ROW
                && col >= 0
                && col < COLS;
    }

    public String[] createBoard() {

        String[] board = DEFAULT.clone();

        // Randomization will go here.
        return board;
    }

    public char tileAt(int row, int col) {
    int slider = BoardShift.sliderForColumn(col);
    int displacement = slider == 0 ? 0
            : boardShift.getOffset(slider) - BoardShift.START_OFFSET;

    return DEFAULT[row - displacement].charAt(col);
}

    // Board shifting will eventually use these
    public boolean shiftSlider(int slider, int amount) {
        return boardShift.shiftSlider(slider, amount);
    }

    public int getSliderOffset(int slider) {
        return boardShift.getOffset(slider);
    }

    public boolean canShift(int slider, int amount) {
        return boardShift.canShift(slider, amount);
    }

    public void resetShifts() {
        boardShift.resetOffsets();
    }
}
