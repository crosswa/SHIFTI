import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class BoardController {

    private static final int ROWS = 15;
    private static final int COLS = 13;
    private static final int TILE_SIZE = 40;

    @FXML
    private GridPane boardGrid;

    private final String[] boardChar = BoardLayout.DEFAULT;

    private final StackPane[][] cells =
            new StackPane[ROWS][COLS];

    private final char[][] board =
            new char[ROWS][COLS];

    // Regular board tiles
    private Image path;
    private Image water;

    // Red door: top-left, top-right, bottom-left, bottom-right
    private Image redTL;
    private Image redTR;
    private Image redBL;
    private Image redBR;

    // Teal door
    private Image tealTL;
    private Image tealTR;
    private Image tealBL;
    private Image tealBR;

    // Green door
    private Image greenTL;
    private Image greenTR;
    private Image greenBL;
    private Image greenBR;

    // Yellow door
    private Image yellowTL;
    private Image yellowTR;
    private Image yellowBL;
    private Image yellowBR;

    // Players
    private Image player1;
    private Image player2;

    // Initial player positions
    private int player1Row = 13;
    private int player1Col = 0;

    private int player2Row = 0;
    private int player2Col = 11;

    @FXML
    public void initialize() {

        // Load regular tiles
        path = loadImage("path.png");
        water = loadImage("water.png");

        // Load red door pieces
        redTL = loadImage("redTL.png");
        redTR = loadImage("redTR.png");
        redBL = loadImage("redBL.png");
        redBR = loadImage("redBR.png");

        // Load teal door pieces
        tealTL = loadImage("tealTL.png");
        tealTR = loadImage("tealTR.png");
        tealBL = loadImage("tealBL.png");
        tealBR = loadImage("tealBR.png");

        // Load green door pieces
        greenTL = loadImage("greenTL.png");
        greenTR = loadImage("greenTR.png");
        greenBL = loadImage("greenBL.png");
        greenBR = loadImage("greenBR.png");

        // Load yellow door pieces
        yellowTL = loadImage("yellowTL.png");
        yellowTR = loadImage("yellowTR.png");
        yellowBL = loadImage("yellowBL.png");
        yellowBR = loadImage("yellowBR.png");

        // Load player images
        player1 = loadImage("player1.png");
        player2 = loadImage("player2.png");

        createMap();
        createCells();
        drawBoard();
    }

    /**
     * Loads an image from src/main/resources/images.
     */
    private Image loadImage(String filename) {

        var url = getClass().getResource(
                "/images/" + filename
        );

        if (url == null) {
            throw new IllegalStateException(
                    "Missing image: /images/" + filename
            );
        }

        return new Image(url.toExternalForm());
    }

    /**
     * Copies BoardLayout.DEFAULT into the logical board.
     * Every row must contain exactly COLS characters.
     */
    private void createMap() {

        if (boardChar.length != ROWS) {
            throw new IllegalStateException(
                    "Board must have " + ROWS
                            + " rows, but has "
                            + boardChar.length
            );
        }

        for (int row = 0; row < ROWS; row++) {

            if (boardChar[row].length() != COLS) {
                throw new IllegalStateException(
                        "Row " + row
                                + " must have " + COLS
                                + " columns, but has "
                                + boardChar[row].length()
                );
            }

            for (int col = 0; col < COLS; col++) {

                char tile = boardChar[row].charAt(col);

                if (tile != 'S'
                        && tile != 'W'
                        && tile != '0'
                        && tile != '1'
                        && tile != '2'
                        && tile != '3') {

                    throw new IllegalStateException(
                            "Invalid tile '" + tile
                                    + "' at row " + row
                                    + ", column " + col
                    );
                }

                board[row][col] = tile;
            }
        }
    }

    /**
     * Creates a fixed-size cell for every logical grid position.
     */
    private void createCells() {

        boardGrid.getChildren().clear();

        boardGrid.setHgap(0);
        boardGrid.setVgap(0);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                StackPane cell = new StackPane();

                cell.setMinSize(TILE_SIZE, TILE_SIZE);
                cell.setPrefSize(TILE_SIZE, TILE_SIZE);
                cell.setMaxSize(TILE_SIZE, TILE_SIZE);

                cells[row][col] = cell;

                boardGrid.add(cell, col, row);
            }
        }
    }

    /**
     * Creates an ImageView sized to exactly one board cell.
     */
    private ImageView makeView(Image image) {

        ImageView view = new ImageView(image);

        view.setFitWidth(TILE_SIZE);
        view.setFitHeight(TILE_SIZE);
        view.setPreserveRatio(false);
        view.setSmooth(false);
        view.setMouseTransparent(true);

        return view;
    }

    /**
     * Draws one door piece at a grid position.
     */
    private void drawDoorPiece(
            int row,
            int col,
            Image image) {

        if (row < 0 || row >= ROWS
                || col < 0 || col >= COLS) {
            return;
        }

        cells[row][col].getChildren().add(
                makeView(image)
        );
    }

    /**
     * Draws a door using four separate images.
     *
     * topRow and topCol identify the door's top-left cell.
     */
    private void drawDoor(
            int topRow,
            int topCol,
            Image topLeft,
            Image topRight,
            Image bottomLeft,
            Image bottomRight) {

        drawDoorPiece(
                topRow,
                topCol,
                topLeft
        );

        drawDoorPiece(
                topRow,
                topCol + 1,
                topRight
        );

        drawDoorPiece(
                topRow + 1,
                topCol,
                bottomLeft
        );

        drawDoorPiece(
                topRow + 1,
                topCol + 1,
                bottomRight
        );
    }

    /**
     * Draws the board, doors, and players.
     */
    private void drawBoard() {

        // Draw the regular tiles first.
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                StackPane cell = cells[row][col];
                cell.getChildren().clear();

                switch (board[row][col]) {

                    case 'S':
                        cell.getChildren().add(
                                makeView(path)
                        );
                        break;

                    case 'W':
                        cell.getChildren().add(
                                makeView(water)
                        );
                        break;

                    case '0':
                    case '1':
                    case '2':
                    case '3':
                        // Door artwork is drawn separately.
                        break;

                    default:
                        throw new IllegalStateException(
                                "Unknown board tile: "
                                        + board[row][col]
                        );
                }
            }
        }

        /*
         * Draw the four 2x2 doors.
         *
         * 0 = red, top-left
         * 1 = teal, top-right
         * 2 = green, bottom-left
         * 3 = yellow, bottom-right
         *
         * Opposite door pairs:
         * Red <-> Yellow
         * Teal <-> Green
         */

        // Green door: top-left
        drawDoor(
                0, 0,
                greenTL, greenTR,
                greenBL, greenBR
        );

        // Teal door: top-right
        drawDoor(
                0, COLS - 2,
                tealTL, tealTR,
                tealBL, tealBR
        );

        // Red door: bottom-left
        drawDoor(
                ROWS - 2, 0,
                redTL, redTR,
                redBL, redBR
        );

        // Yellow door: bottom-right
        drawDoor(
                ROWS - 2, COLS - 2,
                yellowTL, yellowTR,
                yellowBL, yellowBR
        );

        // Draw player 1 on top of the board.
        drawPlayer(
                player1Row,
                player1Col,
                player1
        );

        // Draw player 2 on top of the board.
        drawPlayer(
                player2Row,
                player2Col,
                player2
        );
    }

    /**
     * Draws a player at the specified position.
     */
    private void drawPlayer(
            int row,
            int col,
            Image playerImage) {

        if (row < 0 || row >= ROWS
                || col < 0 || col >= COLS) {
            return;
        }

        cells[row][col].getChildren().add(
                makeView(playerImage)
        );
    }

    /**
     * Handles movement for player 1.
     * Call this method when an arrow key is pressed.
     */
    public void handleKey(KeyCode key) {

        int newRow = player1Row;
        int newCol = player1Col;

        switch (key) {

            case UP:
                newRow--;
                break;

            case DOWN:
                newRow++;
                break;

            case LEFT:
                newCol--;
                break;

            case RIGHT:
                newCol++;
                break;

            default:
                return;
        }

        // Keep movement inside the board.
        if (newRow < 0 || newRow >= ROWS
                || newCol < 0 || newCol >= COLS) {
            return;
        }

        // Water cannot be entered.
        if (board[newRow][newCol] == 'W') {
            return;
        }

        player1Row = newRow;
        player1Col = newCol;

        drawBoard();
    }

    /**
     * Returns player 1's current position as {row, column}.
     */
    public int[] getPlayerPosition() {

        return new int[]{
                player1Row,
                player1Col
        };
    }

    /**
     * Returns player 2's current position as {row, column}.
     */
    public int[] getPlayer2Position() {

        return new int[]{
                player2Row,
                player2Col
        };
    }
}

