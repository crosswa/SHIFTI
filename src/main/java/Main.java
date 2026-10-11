
/*
game cycle workflow (cycle 1)

get board gui initially set up
have pieces functional
have dice rolled connected


*/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


// public class Main {
//     static Client client;
//     static Server server;

//     public static void main(String[] args) {
//         // //server =  new Server(5000);
//         // client = new Client("127.0.0.1",5000);
        
//     }

// }

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
            Main.class.getResource("/Board.fxml")
        );

        Parent root = loader.load();

        BoardController controller = loader.getController();

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
            controller.handleKey(event.getCode());
        });

        stage.setTitle("Minotaur's Maze");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}