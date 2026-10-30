import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    //public int players;
    //public int[] playermoves; 

    private Socket s = null;
    private BufferedReader in = null;
    private DataOutputStream out = null;

    public Client(String addr, int port) {
        try {
            s = new Socket(addr, port);
            System.out.println("Connected");

            in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("IN");
            out = new DataOutputStream(s.getOutputStream());

        } catch (UnknownHostException u) {
            System.out.println(u);
            return;
        } catch (IOException i) {
            System.out.println(i);
            return;
        }

        String m = "";
        System.out.println("Enter message to send to server (type 'Over' to exit):");
        while(true){
            if(m.equals("Over")){
                try{
                    if(in != null) in.close();
                    if(out != null) out.close();
                    if(s != null) s.close();
                }
                catch(IOException i){
                    System.out.println(i);
                }
            
                break;
            }
            try {
                m = in.readLine();
                out.writeUTF(m);
                out.flush();
            } catch (IOException i) {
                    System.out.println(i);
                }
        }
    }

        public static void main(String args[]) {
        String host = "2.tcp.ngrok.io";
        int port = ;
        Client c = new Client(host, port);
    }
}