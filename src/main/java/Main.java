import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private final diceCycle2 dice = new diceCycle2();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Board.fxml"));
        Parent root = loader.load();
        BoardController controller = loader.getController();

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> controller.handleKey(event.getCode()));
        controller.setOnRollDice(() -> {
            rollForTurn(stage, controller);
            root.requestFocus();
        });
        controller.setOnAction(() -> {
            if (!controller.isGameOver() && controller.isTurnComplete()) {
                controller.advanceTurn();
                prepareTurn(stage, controller);
            } else {
                updateTitle(stage, controller);
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        prepareTurn(stage, controller);
        stage.show();
        root.requestFocus();
    }

    private void prepareTurn(Stage stage, BoardController controller) {
        dice.reset();
        controller.beginTurn(0, 0);
        controller.setRollAvailable(true);
        controller.showTurnBanner();
        updateTitle(stage, controller);
    }

    private void rollForTurn(Stage stage, BoardController controller) {
        if (dice.hasRolled() || controller.isGameOver()) {
            return;
        }

        dice.roll();
        controller.setRollAvailable(false);
        int a = dice.getValueA();
        int b = dice.getValueB();
        controller.showRoll(face(a), face(b));
        if (dice.bothStars()) {
            int skippedPlayer = controller.getCurrentPlayer();
            controller.advanceTurn();
            prepareTurn(stage, controller);
            controller.showBanner("Player " + skippedPlayer + " rolled double stars! Turn skipped!");
            return;
        }
        int moves = Math.max(0, a) + Math.max(0, b);
        int shifts = Math.max(0, -a) + Math.max(0, -b);
        controller.beginTurn(moves, shifts);
        if (!controller.isTurnComplete()) {
            updateTitle(stage, controller);
        }
        
    }

    private static String face(int value) {
        return diceCycle2.isStar(value) ? "*".repeat(-value) : Integer.toString(value);
    }

    private void updateTitle(Stage stage, BoardController controller) {
        String title = "Minotaur's Maze - Player " + controller.getCurrentPlayer();
        if (controller.isGameOver()) {
            controller.showWin();
            stage.setTitle(title + " wins!");
        } else if (!dice.hasRolled()) {
            stage.setTitle(title + "'s turn | Roll the dice");
        } else {
            stage.setTitle(title + "'s turn | Rolled "
                    + face(dice.getValueA()) + " + " + face(dice.getValueB())
                    + " | Moves left: " + controller.getMovesLeft()
                    + " | Shifts left: " + controller.getShiftsLeft());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
