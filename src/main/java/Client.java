import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {

    private Socket s;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(String addr, int port, Consumer<String> receiver) {

        try {
            s = new Socket(addr, port);

            System.out.println("Connected");

            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());

            // Listen for messages from server
            new Thread(() -> {

                try {
                    while (true) {

                        String message = in.readUTF();

                        receiver.accept(message);
                    }

                } catch (IOException e) {
                    System.out.println("Disconnected from server");
                }

            }).start();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

      // Send movement to server
    public void sendMove(int player, int row, int col) {

        try {
            out.writeUTF("MOVE|" + player + "|" + row + "|" + col);
            System.out.println("Sent: MOVE|" + player + "|" + row + "|" + col);
            out.flush();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {

        Client c = new Client("127.0.0.1", 5000, message -> {
            System.out.println("Received: " + message);
        });

        javafx.application.Application.launch(Main.class, args);
    }
}