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
        controller.setOnAction(() -> {
            if (!controller.isGameOver() && controller.isTurnComplete()) {
                controller.advanceTurn();
                rollForTurn(controller);
            }
            updateTitle(stage, controller);
        });

        stage.setScene(scene);
        stage.setResizable(false);
        rollForTurn(controller);
        updateTitle(stage, controller);
        stage.show();
        root.requestFocus();
    }

    private void rollForTurn(BoardController controller) {
        while (true) {
            dice.roll();
            int a = dice.getValueA();
            int b = dice.getValueB();
            if (!dice.bothStars()) {
                int moves = Math.max(0, a) + Math.max(0, b);
                int shifts = Math.max(0, -a) + Math.max(0, -b);
                controller.beginTurn(moves, shifts);
                if (!controller.isTurnComplete()) {
                    return;
                }
            }
            controller.advanceTurn();
        }
    }

    private static String face(int value) {
        return diceCycle2.isStar(value) ? "*".repeat(-value) : Integer.toString(value);
    }

    private void updateTitle(Stage stage, BoardController controller) {
        String title = "Minotaur's Maze - Player " + controller.getCurrentPlayer();
        if (controller.isGameOver()) {
            stage.setTitle(title + " wins!");
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
