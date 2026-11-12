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

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
            System.out.println("Sent: " + message);
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    // Send message to server
    public void sendMove(String message) {

        try {
            out.writeUTF(message);
            out.flush();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        javafx.application.Application.launch(Main.class, args);
    }
}