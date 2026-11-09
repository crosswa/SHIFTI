import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;


public class Main extends Application {

    // the dice is rolled once at the start of every turn.
    private static final diceCycle1 dice = new diceCycle1();

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
            Main.class.getResource("/Board.fxml")
        );

        Parent root = loader.load();

        BoardController controller = loader.getController();

        Client client = new Client("127.0.0.1", 5000, message -> {
            Platform.runLater(() -> {
                String[] data = message.split("\\|");
                if(data[0].equals("MOVE")){
                    int player = Integer.parseInt(data[1]);
                    int row = Integer.parseInt(data[2]);
                    int col = Integer.parseInt(data[3]);
                    controller.recieveMove(player, row, col);
                }
            });
        });

        Scene scene = new Scene(root);

        /*
         JavaFX already runs an event loop 
         Each key press is one step of the loop: 
         handleKey moves the current player's piece, checks for a win, and passes the turn.
         Once isGameOver() is true, handleKey ignores input, which is what ends the loop.
         */
        scene.setOnKeyPressed(event -> {
            int playerBefore = controller.getCurrentPlayer();
            controller.handleKey(event.getCode());

            // turn is passed to the other player
            if (!controller.isGameOver()
                    && controller.getCurrentPlayer() != playerBefore) {
                rollForTurn(controller);
            }

            updateTitle(stage, controller);

            // add networking in later cycle
        });

        stage.setTitle("Minotaur's Maze");
        stage.setScene(scene);
        stage.setResizable(false);
        rollForTurn(controller);          // player 1's opening roll
        updateTitle(stage, controller);
        stage.show();

        root.requestFocus();
    }

    // rolls both dice; the sum is how many steps the active player may take
    private static void rollForTurn(BoardController controller) {
        dice.roll();
        controller.setMovesLeft(dice.getSum());
    }

    // window title: player's turn, the roll, and moves left - or the winner.
    private static void updateTitle(Stage stage, BoardController controller) {
        if (controller.isGameOver()) {
            stage.setTitle("Minotaur's Maze - Player "
                    + controller.getCurrentPlayer() + " wins!");
        } else {
            stage.setTitle("Minotaur's Maze - Player "
                    + controller.getCurrentPlayer() + "'s turn | Rolled "
                    + dice.getValueA() + " + " + dice.getValueB()
                    + " | Moves left: " + controller.getMovesLeft());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}