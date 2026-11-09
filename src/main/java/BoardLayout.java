import java.util.List;

public class BoardLayout {

    public static final int ROWS = 15;
    public static final int COLS = 13;
    public static final int FIRST_PLAYABLE_ROW = 1;
    public static final int LAST_PLAYABLE_ROW = ROWS - 2;
    public static final int PLAYABLE_ROWS = LAST_PLAYABLE_ROW - FIRST_PLAYABLE_ROW + 1;
    public static final int PLAYABLE_COLS = COLS;

    public record Door(String color, int row, int col, int entranceRow, int entranceCol) {
        public static final int SIZE = 2;

        public boolean covers(int tileRow, int tileCol) {
            return tileRow >= row && tileRow < row + SIZE
                    && tileCol >= col && tileCol < col + SIZE;
        }

        public boolean isEntrance(int tileRow, int tileCol) {
            return tileRow == entranceRow && tileCol == entranceCol;
        }
    }

    private static final int BOTTOM_DOOR_ROW = LAST_PLAYABLE_ROW - Door.SIZE + 1;
    private static final int RIGHT_DOOR_COL = COLS - Door.SIZE;

    public static final Door GREEN = new Door("green", FIRST_PLAYABLE_ROW, 0, FIRST_PLAYABLE_ROW, 1);
    public static final Door TEAL = new Door("teal", FIRST_PLAYABLE_ROW, RIGHT_DOOR_COL, FIRST_PLAYABLE_ROW + 1, RIGHT_DOOR_COL);
    public static final Door RED = new Door("red", BOTTOM_DOOR_ROW, 0, BOTTOM_DOOR_ROW, 1);
    public static final Door YELLOW = new Door("yellow", BOTTOM_DOOR_ROW, RIGHT_DOOR_COL, BOTTOM_DOOR_ROW, COLS - 1);
    public static final List<Door> DOORS = List.of(GREEN, TEAL, RED, YELLOW);

    private final BoardShift boardShift = new BoardShift();

    // S = stone, W = water. The doors are defined independently of the terrain.
    public static final String[] DEFAULT = {
        "SSWWSSSWWSSSS", // row 0: top buffer
        "SSSSWWWWWWWSS",
        "SSWSSSSSSSSSS",
        "WWWWWWSWWWWWS",
        "SSWWWWSWWWWWS",
        "SSSWWWSWWSWWW",
        "WSSSSSSSSSSSS",
        "SSSSWWSWWSWWW",
        "WWSSWWWWWWWWS",
        "SSSSWWWWWWWWW",
        "SWWWSSSSSSSSS",
        "SSSSWWWWWWWWS",
        "SSWSWWWWWWWSS",
        "SSSWWWWWWWWSS",
        "SSSWWSWSWSWSS"  // row 14: bottom buffer
    };

    public String[] createBoard(){
        String[] board = DEFAULT;
        return board;

    }
    public static boolean isPlayable(int row, int col) {
        return row >= FIRST_PLAYABLE_ROW && row <= LAST_PLAYABLE_ROW
                && col >= 0 && col < COLS;
    }

    public static Door doorAt(int row, int col) {
        for (Door door : DOORS) {
            if (door.covers(row, col)) {
                return door;
            }
        }
        return null;
    }

    public boolean isShiftableTile(int row, int col) {
        return isPlayable(row, col) && doorAt(row, col) == null;
    }

    public char tileAt(int row, int col) {
        if (!isShiftableTile(row, col)) {
            return DEFAULT[row].charAt(col);
        }

        int slider = BoardShift.sliderForRow(row);
        int displacement = boardShift.getOffset(slider) - BoardShift.START_OFFSET;
        int sourceCol = col - displacement;
        return isShiftableTile(row, sourceCol) ? DEFAULT[row].charAt(sourceCol) : 'W';
    }

    public boolean canShift(int row, int amount) {
        return boardShift.canShift(BoardShift.sliderForRow(row), amount);
    }

    public boolean shiftSlider(int row, int amount) {
        return boardShift.shiftSlider(BoardShift.sliderForRow(row), amount);
    }
}
