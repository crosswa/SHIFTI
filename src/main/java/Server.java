import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private static CopyOnWriteArrayList<ClientHandler> clients =
            new CopyOnWriteArrayList<>();
    int numClients = 2;

    public Server(String addr, int port) {

        try {
            InetAddress bindAddress = InetAddress.getByName(addr);

            ServerSocket ss = new ServerSocket(port, 50, bindAddress);

            System.out.println("Server Started " + addr + ":" + port);
            System.out.println("Waiting for clients");

            for(int i = 0; i < 2; i++) {
                try{
                ProcessBuilder pb = new ProcessBuilder(
                        "java", "-cp", System.getProperty("java.class.path"), "Client");
                pb.redirectErrorStream(true);
                pb.inheritIO();

                Process clientProcess = pb.start();
                }catch (IOException e) {
                    System.out.println("Error starting client process: " + e.getMessage());
                }
            }


            while (true) {

                Socket s = ss.accept();

                System.out.println("New Client accepted: "
                        + s.getRemoteSocketAddress());
                        

                ClientHandler client = new ClientHandler(s);

                new Thread(client).start();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void broadcast(String message, ClientHandler sender) {

        for (ClientHandler client : clients) {

            if (client != sender) {

                try {
                    client.send(message);

                } catch (IOException e) {
                    System.out.println("Could not send message");
                }
            }
        }
    }

    // Handles each connected client separately
    static class ClientHandler implements Runnable {

        private Socket s;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) {
            s = socket;
        }

        public void run() {

            try {
                in = new DataInputStream(
                        new BufferedInputStream(s.getInputStream()));

                out = new DataOutputStream(s.getOutputStream());

                clients.add(this);

                while (true) {

                    String m = in.readUTF();

                    System.out.println("Received: " + m);

                    if (m.equals("Over")) {
                        break;
                    }

                    broadcast(m, this);
                }

            } catch (IOException e) {
                System.out.println("Client disconnected");

            } finally {

                clients.remove(this);

                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (s != null) s.close();

                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }

        public synchronized void send(String message)
                throws IOException {

            out.writeUTF(message);
            out.flush();
        }
    }

    public static void main(String[] args) {
        Server s = new Server("127.0.0.1", 5000);
    }
}