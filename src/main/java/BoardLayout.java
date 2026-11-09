import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BoardLayout {

    private static final int RANDOM_SWAP_ATTEMPTS = 30;
    private static final int[][] NEIGHBORS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private record Cell(int row, int col) {}

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

    // S = stone, W = water. the doors are defined independently.
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

    private String[] board = DEFAULT.clone();

    // board randomizer -> uses array lists to store random locations for the stones/water
    public String[] createBoard() {
        char[][] randomBoard = new char[ROWS][];
        List<Cell> stones = new ArrayList<>();
        List<Cell> water = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            randomBoard[row] = DEFAULT[row].toCharArray();
            for (int col = 0; col < COLS; col++) {
                if (isShiftableTile(row, col)) {
                    List<Cell> cells = randomBoard[row][col] == 'S' ? stones : water;
                    cells.add(new Cell(row, col));
                }
            }
        }

        if (!hasOpeningRoute(randomBoard)) {
            throw new IllegalStateException("DEFAULT needs a route from the red door to the teal door");
        }

        var random = ThreadLocalRandom.current();
        for (int attempt = 0; attempt < RANDOM_SWAP_ATTEMPTS
                && !stones.isEmpty() && !water.isEmpty(); attempt++) {
            int stoneIndex = random.nextInt(stones.size());
            int waterIndex = random.nextInt(water.size());
            Cell stone = stones.get(stoneIndex);
            Cell wet = water.get(waterIndex);
            randomBoard[stone.row()][stone.col()] = 'W';
            randomBoard[wet.row()][wet.col()] = 'S';

            if (hasOpeningRoute(randomBoard)) {
                stones.set(stoneIndex, wet);
                water.set(waterIndex, stone);
            } else {
                randomBoard[stone.row()][stone.col()] = 'S';
                randomBoard[wet.row()][wet.col()] = 'W';
            }
        }

        for (int row = 0; row < ROWS; row++) {
            board[row] = new String(randomBoard[row]);
        }
        boardShift.resetOffsets();
        return board.clone();
    }

    // uses a queue to ensure there is an opening route in the start of the game, to ensure theres no unwinnable games
    // the route is reversible, so a single search checks both players' opening paths.
    private boolean hasOpeningRoute(char[][] candidate) {
        boolean[][] visited = new boolean[ROWS][COLS];
        ArrayDeque<Cell> pending = new ArrayDeque<>();
        pending.add(new Cell(RED.entranceRow(), RED.entranceCol()));
        visited[RED.entranceRow()][RED.entranceCol()] = true;

        while (!pending.isEmpty()) {
            Cell cell = pending.removeFirst();
            for (int[] direction : NEIGHBORS) {
                int row = cell.row() + direction[0];
                int col = cell.col() + direction[1];
                if (TEAL.isEntrance(row, col)) {
                    return true;
                }
                if (isShiftableTile(row, col) && !visited[row][col] && candidate[row][col] == 'S') {
                    visited[row][col] = true;
                    pending.addLast(new Cell(row, col));
                }
            }
        }
        return false;
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
            return board[row].charAt(col);
        }

        int slider = BoardShift.sliderForRow(row);
        int displacement = boardShift.getOffset(slider) - BoardShift.START_OFFSET;
        int sourceCol = col - displacement;
        return isShiftableTile(row, sourceCol) ? board[row].charAt(sourceCol) : 'W';
    }

    public boolean canShift(int row, int amount) {
        return boardShift.canShift(BoardShift.sliderForRow(row), amount);
    }

    public boolean shiftSlider(int row, int amount) {
        return boardShift.shiftSlider(BoardShift.sliderForRow(row), amount);
    }
}
