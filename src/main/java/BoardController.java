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


// ************** board generation **********************

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

    // One Piece per player: index 0 = player 1, index 1 = player 2.
    // Each Piece stores its own row/col, starting at that player's door.
    private final Piece[] pieces = {
            new Piece(13, 0),   // player 1 starts bottom-left
            new Piece(0, 11)    // player 2 starts top-right
    };

    private int currentPlayer = 1;
    private boolean gameOver = false;
    private int movesLeft = 0;

    @FXML
    public void initialize() {

        // regular tiles
        path = loadImage("path.png");
        water = loadImage("water.png");

        // red door pieces
        redTL = loadImage("redTL.png");
        redTR = loadImage("redTR.png");
        redBL = loadImage("redBL.png");
        redBR = loadImage("redBR.png");

        // teal door pieces
        tealTL = loadImage("tealTL.png");
        tealTR = loadImage("tealTR.png");
        tealBL = loadImage("tealBL.png");
        tealBR = loadImage("tealBR.png");

        // green door pieces
        greenTL = loadImage("greenTL.png");
        greenTR = loadImage("greenTR.png");
        greenBL = loadImage("greenBL.png");
        greenBR = loadImage("greenBR.png");

        // yellow door pieces
        yellowTL = loadImage("yellowTL.png");
        yellowTR = loadImage("yellowTR.png");
        yellowBL = loadImage("yellowBL.png");
        yellowBR = loadImage("yellowBR.png");

        // player images
        player1 = loadImage("player1.png");
        player2 = loadImage("player2.png");

        createMap();
        createCells();
        drawBoard();
    }

    // loads an image from src/main/resources/images.
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

    // copies BoardLayout.DEFAULT into the logical board, will be updated when board shifting is added
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

    // fixed-size cell for every logical grid position is created
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

    private ImageView makeView(Image image) {

        ImageView view = new ImageView(image);

        view.setFitWidth(TILE_SIZE);
        view.setFitHeight(TILE_SIZE);
        view.setPreserveRatio(false);
        view.setSmooth(false);
        view.setMouseTransparent(true);

        return view;
    }
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
    private void drawBoard() {
        // draw the regular tiles first.
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
                        // door artwork is drawn separately.
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
         draw the four 2x2 doors.
         0 = red, top-left
         1 = teal, top-right
         2 = green, bottom-left
         3 = yellow, bottom-right

         Opposite door pairs:
         Red <-> Yellow
         Teal <-> Green
         */

        // green door: top-left
        drawDoor(
                0, 0,
                greenTL, greenTR,
                greenBL, greenBR
        );

        // teal door: top-right
        drawDoor(
                0, COLS - 2,
                tealTL, tealTR,
                tealBL, tealBR
        );

        // red door: bottom-left
        drawDoor(
                ROWS - 2, 0,
                redTL, redTR,
                redBL, redBR
        );

        // yellow door: bottom-right
        drawDoor(
                ROWS - 2, COLS - 2,
                yellowTL, yellowTR,
                yellowBL, yellowBR
        );

        // draw player 1 on top of the board.
        drawPlayer(
                pieces[0].getRow(),
                pieces[0].getCol(),
                player1
        );

        // draw player 2 on top of the board.
        drawPlayer(
                pieces[1].getRow(),
                pieces[1].getCol(),
                player2
        );
    }

    // draws a player at the specified position.
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

    // ************ logic + helper functions ***************

    // handles movement for the player whose turn it is, called when an arrow key is pressed.
    public void handleKey(KeyCode key) {
        if (gameOver || movesLeft <= 0) {
            return;
        }
        Piece piece = pieces[currentPlayer - 1];
        int newRow = piece.getRow();
        int newCol = piece.getCol();
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
        if (newRow < 0 || newRow >= ROWS
                || newCol < 0 || newCol >= COLS) {
            return;
        }
        if (board[newRow][newCol] == 'W') {
            return;
        }

        // legal move: store it on the piece and use up one move.
        piece.moveTo(newRow, newCol);
        movesLeft--;
 
        if (piece.isWin(currentPlayer)) {
            gameOver = true;
        } else {
            // if piece landed on the other player's piece, send it back to its start.
            // checked on every step, so it also applies mid-turn
            PieceCapture.captureAll(piece, pieces);
 
            if (movesLeft == 0) {
                currentPlayer = (currentPlayer == 1) ? 2 : 1;
            }
        }
 
        drawBoard();
    }

    // helper functions
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setMovesLeft(int moves) {
        movesLeft = moves;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int[] getPlayerPosition() {

        return new int[]{
                pieces[0].getRow(),
                pieces[0].getCol()
        };
    }
    public int[] getPlayer2Position() {

        return new int[]{
                pieces[1].getRow(),
                pieces[1].getCol()
        };
    }
}