import javafx.application.Application;
import javafx.application.Platform;
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

        Client[] clientHolder = new Client[1];

        clientHolder[0] = new Client("127.0.0.1", 5000, message -> {
            Platform.runLater(() -> {

                String[] data = message.split("\\|");

            if(data[0].equals("PLAYER")) {
                int playerNum = Integer.parseInt(data[1]);

                System.out.println("You are player " + playerNum);

                if(playerNum == 1) {
                    rollForTurn(controller);
                    sendState(clientHolder[0], controller);
                    updateTitle(stage, controller);
                }
            }
            if(data[0].equals("STATE")) {
                    int p1row = Integer.parseInt(data[1]);
                    int p1col = Integer.parseInt(data[2]);

                    int p2row = Integer.parseInt(data[3]);
                    int p2col = Integer.parseInt(data[4]);

                    int player = Integer.parseInt(data[5]);
                    int moves = Integer.parseInt(data[6]);

                    int dieA = Integer.parseInt(data[7]);
                    int dieB = Integer.parseInt(data[8]);
        
                    controller.receiveState(
                        p1row, p1col, p2row, p2col,
                        player, moves, dieA, dieB);

                    dice.setValues(dieA, dieB);
                    
                    updateTitle(stage, controller);
                }
            });
        });

        Client client = clientHolder[0];
        controller.setClient(client);


        Scene scene = new Scene(root);

        /*
         JavaFX already runs an event loop 
         Each key press is one step of the loop: 
         handleKey moves the current player's piece, checks for a win, and passes the turn.
         Once isGameOver() is true, handleKey ignores input, which is what ends the loop.
         */
        scene.setOnKeyPressed(event -> {
            int oldPlayer = controller.getCurrentPlayer();
            
            int [] oldP1 = controller.getPlayerPosition();
            int [] oldP2 = controller.getPlayer2Position();

            controller.handleKey(event.getCode());

            int[] newP1 = controller.getPlayerPosition();
            int[] newP2 = controller.getPlayer2Position();

            boolean moved = 
            oldP1[0] != newP1[0] || 
            oldP1[1] != newP1[1] ||
            oldP2[0] != newP2[0] ||
            oldP2[1] != newP2[1];

            if(moved) {
                if(controller.getCurrentPlayer() != oldPlayer && 
                !controller.isGameOver()) {
                    rollForTurn(controller);
                }
                sendState(client, controller);
            }

            /*int playerBefore = controller.getCurrentPlayer();
            controller.handleKey(event.getCode());

            // turn is passed to the other player
            if (!controller.isGameOver()
                    && controller.getCurrentPlayer() != playerBefore) {
                rollForTurn(controller);
            }
            */
            updateTitle(stage, controller);

            // add networking in later cycle


        });

        stage.setScene(scene);
        stage.setResizable(false);

        //rollForTurn(controller);          // player 1's opening roll
        updateTitle(stage, controller);
        stage.show();
        root.requestFocus();
    }

    
    private static void sendState(Client client, BoardController controller) {
        // send the current state of the board to the other player
        int[] p1 = controller.getPlayerPosition();
        int[] p2 = controller.getPlayer2Position();

        String message = "STATE|"
                + p1[0] + "|"
                + p1[1] + "|" 
                + p2[0] + "|" 
                + p2[1] + "|"
                + controller.getCurrentPlayer() + "|"
                + controller.getMovesLeft() + "|"
                + dice.getValueA() + "|"
                + dice.getValueB();

        client.sendMessage(message);
    }


    // rolls both dice; the sum is how many steps the active player may take
    private static void rollForTurn(BoardController controller) {
        dice.roll();
        controller.setMovesLeft(dice.getSum());
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
