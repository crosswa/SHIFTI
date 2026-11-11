import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class BoardController {

    private static final int TILE_SIZE = 40;
    private final BoardLayout boardLayout = new BoardLayout();

    @FXML
    private GridPane boardGrid;
    @FXML
    private Label turnBanner;
    @FXML
    private Button rollDiceButton;

    private SequentialTransition turnBannerAnimation;

    private final ImageView[][] tiles = new ImageView[BoardLayout.ROWS][BoardLayout.COLS];
    private final Button[][] shiftButtons = new Button[BoardLayout.ROWS][2];

    private Image path;
    private Image water;

    private final Piece[] pieces = {
            new Piece(BoardLayout.RED.entranceRow(), BoardLayout.RED.entranceCol()),
            new Piece(BoardLayout.TEAL.entranceRow(), BoardLayout.TEAL.entranceCol())
    };
    private final ImageView[] sprites = new ImageView[pieces.length];

    private int currentPlayer = 1;
    private int oppPlayer;
    private boolean gameOver;
    private int movesLeft;
    private int shiftsLeft;
    private Runnable onAction = () -> {};

    // *** BOARD BUILDING ***
    @FXML
    public void initialize() {
        PauseTransition hold = new PauseTransition(Duration.seconds(2));
        FadeTransition fade = new FadeTransition(Duration.millis(500), turnBanner);
        fade.setFromValue(1);
        fade.setToValue(0);
        turnBannerAnimation = new SequentialTransition(hold, fade);
        turnBannerAnimation.setOnFinished(event -> turnBanner.setVisible(false));

        boardLayout.createBoard();
        path = loadImage("path.png");
        water = loadImage("water.png");

        createCells();
        for (BoardLayout.Door door : BoardLayout.DOORS) {
            addDoor(door);
        }

        for (int i = 0; i < pieces.length; i++) {
            sprites[i] = makeView(loadImage("player" + (i + 1) + ".png"));
            boardGrid.getChildren().add(sprites[i]);
        }
        drawBoard();
    }

    private Image loadImage(String filename) {
        var url = getClass().getResource("/images/" + filename);
        if (url == null) {
            throw new IllegalStateException("Missing image: /images/" + filename);
        }
        return new Image(url.toExternalForm());
    }

    private void createCells() {
        for (int row = 0; row < BoardLayout.ROWS; row++) {
            for (int col = 0; col < BoardLayout.COLS; col++) {
                if (BoardLayout.doorAt(row, col) != null) {
                    continue;
                }
                ImageView tile = makeView(water);
                if (!BoardLayout.isPlayable(row, col)) {
                    tile.setOpacity(0.55);
                }
                tiles[row][col] = tile;
                boardGrid.add(tile, col + 1, row);
            }
        }

        Image leftArrow = loadImage("shift-left.png");
        Image rightArrow = loadImage("shift-right.png");
        for (int row = BoardLayout.FIRST_PLAYABLE_ROW; row <= BoardLayout.LAST_PLAYABLE_ROW; row++) {
            shiftButtons[row][0] = addShiftButton(row, -1, 0, leftArrow);
            shiftButtons[row][1] = addShiftButton(row, 1, BoardLayout.COLS + 1, rightArrow);
        }
    }

    private Button addShiftButton(int row, int direction, int gridCol, Image image) {
        ImageView arrow = makeView(image);
        arrow.setFitWidth(TILE_SIZE - 8);
        arrow.setFitHeight(TILE_SIZE - 8);
        arrow.setPreserveRatio(true);

        Button button = new Button("", arrow);
        button.setPrefSize(TILE_SIZE, TILE_SIZE);
        button.setPadding(Insets.EMPTY);
        button.setStyle("-fx-background-color: transparent;");
        button.setFocusTraversable(false);
        button.setAccessibleText("Shift row " + row + (direction < 0 ? " left" : " right"));
        button.setOnAction(event -> requestShift(row, direction));
        boardGrid.add(button, gridCol, row);
        return button;
    }

    private ImageView makeView(Image image) {
        ImageView view = new ImageView(image);
        view.setFitWidth(TILE_SIZE);
        view.setFitHeight(TILE_SIZE);
        view.setSmooth(false);
        view.setMouseTransparent(true);
        return view;
    }

    private void addDoor(BoardLayout.Door door) {
        GridPane artwork = new GridPane();
        artwork.add(makeView(loadImage(door.color() + "TL.png")), 0, 0);
        artwork.add(makeView(loadImage(door.color() + "TR.png")), 1, 0);
        artwork.add(makeView(loadImage(door.color() + "BL.png")), 0, 1);
        artwork.add(makeView(loadImage(door.color() + "BR.png")), 1, 1);
        artwork.setMouseTransparent(true);
        boardGrid.add(artwork, door.col() + 1, door.row(), BoardLayout.Door.SIZE, BoardLayout.Door.SIZE);
    }

    private void drawBoard() {
        for (int row = 0; row < BoardLayout.ROWS; row++) {
            for (int col = 0; col < BoardLayout.COLS; col++) {
                if (tiles[row][col] != null) {
                    tiles[row][col].setImage(boardLayout.tileAt(row, col) == 'S' ? path : water);
                }
            }
        }
        for (int i = 0; i < pieces.length; i++) {
            GridPane.setConstraints(sprites[i], pieces[i].getCol() + 1, pieces[i].getRow());
        }
        updateShiftButtons();
    }

    // *** LOGIC BEHIND BOARD ***
    private void updateShiftButtons() {
        boolean show = !gameOver && shiftsLeft > 0;
        for (int row = BoardLayout.FIRST_PLAYABLE_ROW; row <= BoardLayout.LAST_PLAYABLE_ROW; row++) {
            shiftButtons[row][0].setVisible(show);
            shiftButtons[row][1].setVisible(show);
            shiftButtons[row][0].setDisable(!show || !canRequestShift(row, -1));
            shiftButtons[row][1].setDisable(!show || !canRequestShift(row, 1));
        }
    }

    private boolean ridesSlider(Piece piece, int row) {
        return piece.getRow() == row && boardLayout.isShiftableTile(row, piece.getCol());
    }

    private boolean canRequestShift(int row, int amount) {
        if (!boardLayout.canShift(row, amount)) {
            return false;
        }
        for (Piece piece : pieces) {
            if (ridesSlider(piece, row) && !boardLayout.isShiftableTile(row, piece.getCol() + amount)) {
                return false;
            }
        }
        return true;
    }

    private void requestShift(int row, int amount) {
        if (gameOver || shiftsLeft == 0 || !canRequestShift(row, amount)) {
            return;
        }

        boardLayout.shiftSlider(row, amount);
        for (Piece piece : pieces) {
            if (ridesSlider(piece, row)) {
                piece.moveTo(row, piece.getCol() + amount);
            }
        }
        shiftsLeft--;
        drawBoard();
        onAction.run();
    }

    private boolean canEnter(int row, int col) {
        if (!BoardLayout.isPlayable(row, col)) {
            return false;
        }
        BoardLayout.Door door = BoardLayout.doorAt(row, col);
        if (door != null) {
            return door == Piece.goalFor(currentPlayer) && door.isEntrance(row, col);
        }
        return boardLayout.tileAt(row, col) == 'S';
    }

    public void handleKey(KeyCode key) {
        if (gameOver || movesLeft <= 0) {
            return;
        }
        Piece piece = pieces[currentPlayer - 1];
        int newRow = piece.getRow();
        int newCol = piece.getCol();
        switch (key) {
            case UP -> newRow--;
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
            default -> { return; }
        }
        if (!canEnter(newRow, newCol)) {
            return;
        }

        piece.moveTo(newRow, newCol);
        movesLeft--;

        boolean captured = false;
        if (piece.isWin(currentPlayer)) {
            gameOver = true;
        } else {
            captured = PieceCapture.captureAll(piece, pieces);
        }

        drawBoard();
        onAction.run();

        if (captured) {
            showCapture();
        }
    }

    public void setOnRollDice(Runnable rollDice) {
        rollDiceButton.setOnAction(event -> rollDice.run());
    }

    public void setRollAvailable(boolean available) {
        rollDiceButton.setDisable(!available);
    }

    public void setOnAction(Runnable onAction) {
        this.onAction = onAction;
    }

    public void beginTurn(int moves, int shifts) {
        movesLeft = moves;
        shiftsLeft = shifts;
        updateShiftButtons();
    }

    public boolean isTurnComplete() {
        if (shiftsLeft > 0) {
            return false;
        }
        if (movesLeft == 0) {
            return true;
        }
        Piece piece = pieces[currentPlayer - 1];
        int row = piece.getRow();
        int col = piece.getCol();
        return !canEnter(row - 1, col) && !canEnter(row + 1, col)
                && !canEnter(row, col - 1) && !canEnter(row, col + 1);
    }

    public void advanceTurn() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    // *** BANNER DISPLAY ***
    public void showBanner(String message) {
        turnBannerAnimation.stop();
        turnBanner.setText(message);
        turnBanner.setOpacity(1);
        turnBanner.setVisible(true);
        turnBannerAnimation.playFromStart();
    }

    public void showTurnBanner() {
        showBanner("Player " + currentPlayer + "’s turn");
    }
    public void showRoll(String a, String b) {
        showBanner("Roll: " + a + " + " + b);
    }

    public void showCapture(){
        showBanner("Player " + currentPlayer + " captured Player " + getOppPlayer() + "'s piece!");
    }

    // *** HELPERS ***
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getOppPlayer() {
        if (currentPlayer==1){
            return 2;
        }
        else {
            return 1;
        }
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public int getShiftsLeft() {
        return shiftsLeft;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int[] getPlayerPosition() {
        return new int[]{pieces[0].getRow(), pieces[0].getCol()};
    }

    public int[] getPlayer2Position() {
        return new int[]{pieces[1].getRow(), pieces[1].getCol()};
    }
}
