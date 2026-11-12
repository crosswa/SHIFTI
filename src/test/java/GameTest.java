import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameTest {
    private record Cell(int row, int col) {}

    private static final Cell RED = new Cell(12, 1);
    private static final Cell TEAL = new Cell(2, 11);
    private static final KeyCode[] DIRECTIONS = {
        KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT
    };

    private static void require(boolean condition, String detail) {
        if (!condition) {
            throw new AssertionError(detail);
        }
    }

    private static void passed(int number, String name) {
        System.out.printf("PASS TC-%02d %s%n", number, name);
    }

    private static Cell neighbor(Cell cell, KeyCode key) {
        return switch (key) {
            case UP -> new Cell(cell.row() - 1, cell.col());
            case DOWN -> new Cell(cell.row() + 1, cell.col());
            case LEFT -> new Cell(cell.row(), cell.col() - 1);
            case RIGHT -> new Cell(cell.row(), cell.col() + 1);
            default -> throw new IllegalArgumentException(key.toString());
        };
    }

    private static KeyCode direction(Cell from, Cell to) {
        for (KeyCode key : DIRECTIONS) {
            if (neighbor(from, key).equals(to)) {
                return key;
            }
        }
        throw new AssertionError("Non-adjacent cells: " + from + " and " + to);
    }

    private static void press(Scene scene, KeyCode key) {
        Event.fireEvent(scene, new KeyEvent(KeyEvent.KEY_PRESSED, "", "", key,
                false, false, false, false));
    }

    private static boolean imageIs(ImageView view, String filename) {
        return view.getImage().getUrl().endsWith("/" + filename);
    }

    private static Cell position(GridPane grid, int player) {
        return grid.getChildren().stream()
                .filter(ImageView.class::isInstance)
                .map(ImageView.class::cast)
                .filter(view -> imageIs(view, "player" + player + ".png"))
                .map(view -> new Cell(GridPane.getRowIndex(view), GridPane.getColumnIndex(view) - 1))
                .findFirst().orElseThrow();
    }

    private static List<Button> shiftButtons(GridPane grid) {
        return grid.getChildren().stream().filter(Button.class::isInstance)
                .map(Button.class::cast).toList();
    }

    private static char[][] readTerrain(GridPane grid) {
        char[][] terrain = new char[15][13];
        for (var node : grid.getChildren()) {
            if (node instanceof ImageView view && (imageIs(view, "path.png") || imageIs(view, "water.png"))) {
                int row = GridPane.getRowIndex(view);
                int col = GridPane.getColumnIndex(view) - 1;
                terrain[row][col] = imageIs(view, "path.png") ? 'S' : 'W';
            }
        }
        return terrain;
    }

    private static List<Cell> route(GridPane grid, Cell start, Cell goal) {
        char[][] terrain = readTerrain(grid);
        Cell[][] previous = new Cell[15][13];
        ArrayDeque<Cell> queue = new ArrayDeque<>();
        queue.add(start);
        previous[start.row()][start.col()] = start;

        while (!queue.isEmpty()) {
            Cell current = queue.removeFirst();
            if (current.equals(goal)) {
                List<Cell> path = new ArrayList<>();
                for (Cell cell = goal; !cell.equals(start); cell = previous[cell.row()][cell.col()]) {
                    path.add(cell);
                }
                Collections.reverse(path);
                return path;
            }
            for (KeyCode key : DIRECTIONS) {
                Cell next = neighbor(current, key);
                int row = next.row();
                int col = next.col();
                if (BoardLayout.isPlayable(row, col) && previous[row][col] == null
                        && (terrain[row][col] == 'S' || next.equals(goal))) {
                    previous[row][col] = current;
                    queue.addLast(next);
                }
            }
        }
        throw new AssertionError("No route between the entrances");
    }

    private static Object field(Object owner, String name) throws ReflectiveOperationException {
        Field field = owner.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(owner);
    }

    // Select actual die faces while leaving the board generation random.
    private static void nextRoll(Random dice, int firstFaceIndex, int secondFaceIndex) {
        for (long seed = 0; seed < 1000; seed++) {
            Random sample = new Random(seed);
            if (sample.nextInt(6) == firstFaceIndex && sample.nextInt(6) == secondFaceIndex) {
                dice.setSeed(seed);
                return;
            }
        }
        throw new AssertionError("Could not arrange the requested dice faces");
    }

    private static void testBlockedMovesAndCapture() throws Exception {
        FXMLLoader loader = new FXMLLoader(GameTest.class.getResource("/Board.fxml"));
        loader.load();
        BoardController controller = loader.getController();
        GridPane grid = (GridPane) loader.getNamespace().get("boardGrid");
        List<Cell> path = route(grid, RED, TEAL);

        // Place the piece beside a water tile in the current randomized layout.
        char[][] terrain = readTerrain(grid);
        Cell water = null;
        Cell shore = null;
        for (int row = 1; row <= 13 && shore == null; row++) {
            for (int col = 0; col < 13 && shore == null; col++) {
                if (terrain[row][col] != 'S') continue;
                Cell stone = new Cell(row, col);
                for (KeyCode key : DIRECTIONS) {
                    Cell next = neighbor(stone, key);
                    if (BoardLayout.isPlayable(next.row(), next.col()) && terrain[next.row()][next.col()] == 'W') {
                        water = next;
                        shore = stone;
                        break;
                    }
                }
            }
        }
        require(shore != null, "Expected a stone tile next to water");
        controller.recieveMove(1, shore.row(), shore.col());
        controller.beginTurn(2, 0);
        controller.handleKey(direction(shore, water));
        require(position(grid, 1).equals(shore) && controller.getMovesLeft() == 2,
                "Water must block movement without charging a move");
        passed(4, "Water blocks movement");

        controller.recieveMove(1, 1, 5);
        controller.beginTurn(2, 0);
        controller.handleKey(KeyCode.UP);
        require(position(grid, 1).equals(new Cell(1, 5)) && controller.getMovesLeft() == 2,
                "Top buffer row must block movement");
        passed(5, "Playable edge blocks movement");

        Cell start = path.get(0);
        Cell meeting = path.get(1);
        controller.recieveMove(1, start.row(), start.col());
        controller.recieveMove(2, meeting.row(), meeting.col());
        controller.beginTurn(2, 0);
        controller.handleKey(direction(start, meeting));
        require(position(grid, 1).equals(meeting) && position(grid, 2).equals(TEAL),
                "Capture must keep the mover in place and return the opponent to the teal entrance");
        Label banner = (Label) loader.getNamespace().get("turnBanner");
        require(banner.getText().contains("Player 1 captured Player 2"), "Capture banner must name both players");
        passed(10, "Capture moves the opponent's sprite and shows a banner");
    }

    private static void testMain(Stage stage) throws Exception {
        Main main = new Main();
        Random dice = (Random) field(field(main, "dice"), "rng");
        main.start(stage);
        Scene scene = stage.getScene();
        GridPane grid = (GridPane) scene.lookup("#boardGrid");
        Button roll = (Button) scene.lookup("#rollDiceButton");
        Label banner = (Label) scene.lookup("#turnBanner");

        long tiles = grid.getChildren().stream().filter(ImageView.class::isInstance).count() - 2;
        long doors = grid.getChildren().stream().filter(GridPane.class::isInstance).count();
        require(tiles == 179 && doors == 4 && shiftButtons(grid).size() == 26,
                "Expected 179 terrain views, four 2×2 doors, and 26 shift buttons");
        List<Cell> path = route(grid, RED, TEAL);
        passed(1, "Board geometry and playable opening route");
        require(position(grid, 1).equals(RED) && position(grid, 2).equals(TEAL), "Starting sprites");
        passed(2, "Both sprites start at their door entrances");

        require(!roll.isDisabled() && banner.getText().contains("Player 1"), "Game starts awaiting a roll");
        press(scene, direction(RED, path.get(0)));
        require(position(grid, 1).equals(RED), "Cannot move before rolling");
        nextRoll(dice, 0, 0); // 1 + 1
        roll.fire();
        require(roll.isDisabled() && stage.getTitle().contains("Moves left: 2 | Shifts left: 0"),
                "Numeric roll gives two moves and disables rolling");
        passed(3, "Manual roll starts the turn");
        press(scene, KeyCode.A);
        require(position(grid, 1).equals(RED), "Unrelated key must not move a piece");
        passed(6, "Non-arrow keys are ignored during movement");

        press(scene, direction(RED, path.get(0)));
        require(position(grid, 1).equals(path.get(0)) && position(grid, 2).equals(TEAL),
                "Only the active sprite moves one tile");
        passed(7, "Legal arrow-key step moves the active sprite");
        require(stage.getTitle().contains("Player 1") && stage.getTitle().contains("Moves left: 1"),
                "A legal step spends one move without advancing the turn");
        passed(8, "A step deducts one move");

        press(scene, direction(path.get(0), path.get(1)));
        require(!roll.isDisabled() && stage.getTitle().contains("Player 2")
                && stage.getTitle().contains("Roll the dice"), "Next player must roll manually");
        passed(9, "Last move passes the turn and re-enables the die");

        nextRoll(dice, 0, 2); // 1 + *
        roll.fire();
        require(stage.getTitle().contains("Moves left: 1 | Shifts left: 1"), "Mixed roll gives both budgets");
        Cell tealNext = route(grid, TEAL, RED).get(0);
        Button shift = shiftButtons(grid).stream()
                .filter(button -> button.isVisible() && !button.isDisabled()
                        && GridPane.getRowIndex(button) != tealNext.row())
                .findFirst().orElseThrow();
        shift.fire();
        require(shiftButtons(grid).stream().noneMatch(Button::isVisible)
                && stage.getTitle().contains("Shifts left: 0"), "Spending the only shift hides the arrows");
        passed(11, "Star roll enables shifting and spends the shift on click");

        press(scene, direction(TEAL, tealNext));
        require(!roll.isDisabled(), "A move after shifting completes the mixed turn");
        nextRoll(dice, 1, 2); // * + *
        roll.fire();
        require(!roll.isDisabled() && stage.getTitle().contains("Player 2")
                && banner.getText().contains("Turn skipped"), "Two star faces skip the turn");
        passed(12, "Two star faces pass the turn");

        // A fresh game keeps the maze unshifted. Player 2 skips while Player 1 follows the route.
        stage.close();
        Stage finish = new Stage();
        try {
            main.start(finish);
            Scene finishScene = finish.getScene();
            GridPane finishGrid = (GridPane) finishScene.lookup("#boardGrid");
            Button finishRoll = (Button) finishScene.lookup("#rollDiceButton");
            Cell previous = RED;
            List<Cell> winningPath = route(finishGrid, RED, TEAL);
            for (int index = 0; index < winningPath.size(); index++) {
                if (index % 2 == 0) {
                    if (index > 0) {
                        nextRoll(dice, 1, 2);
                        finishRoll.fire();
                    }
                    nextRoll(dice, 0, 0);
                    finishRoll.fire();
                }
                Cell next = winningPath.get(index);
                press(finishScene, direction(previous, next));
                require(position(finishGrid, 1).equals(next), "Sprite follows the winning route");
                previous = next;
            }
            Label win = (Label) finishScene.lookup("#turnBanner");
            require(finish.getTitle().contains("Player 1 wins!") && win.getText().contains("escaped"),
                    "Goal entrance must announce a win");
            press(finishScene, KeyCode.UP);
            require(position(finishGrid, 1).equals(TEAL), "Movement stays disabled after the win");
            passed(13, "Full game reaches the goal, displays a win, and stops movement");
        } finally {
            finish.close();
        }
    }

    public static void main(String[] args) {
        try {
            runTests();
        } catch (Exception failure) {
            failure.printStackTrace();
            System.exit(1);
        }
        // Main may have started a socket listener thread.
        System.exit(0);
    }

    private static void runTests() throws Exception {
        CompletableFuture<Void> result = new CompletableFuture<>();
        Platform.startup(() -> {
            Platform.setImplicitExit(false);
            Stage stage = new Stage();
            try {
                testBlockedMovesAndCapture();
                testMain(stage);
                result.complete(null);
            } catch (Throwable error) {
                result.completeExceptionally(error);
            } finally {
                stage.close();
            }
        });
        try {
            result.get(30, TimeUnit.SECONDS);
        } finally {
            Platform.exit();
        }
        System.out.println("13/13 gameplay checks passed.");
    }
}
