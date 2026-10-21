import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private  Socket s = null;
    private ServerSocket ss = null;
    private DataInputStream in = null;
    public Server(int port){
        try{
            ss = new ServerSocket(port);
            System.out.println("Server Started");
            System.out.println("Waiting for client");

            while(true){
                s = ss.accept();
                System.out.println("New Client accepted: " + s.getRemoteSocketAddress());
                //Thread clientThread = new Thread(new ClientHandler(s));
                //clientThread.start();
                in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                String m = "";

                while(true){
                    try{
                        m = in.readUTF();
                        System.out.println(m);
                        if(m.equals("Over")){
                            System.out.println("Closing connection");
                            if(in != null) in.close();
                            if(s != null) s.close();
                            if(ss != null) ss.close();
                            break;
                        }
                    }catch(IOException e){
                        System.out.println(e);
                        if(in != null) in.close();
                        if(s != null) s.close();
                        break;
                    }
                }
            }
    }catch(IOException i){
            System.out.println(i);
    }finally{
        try{
            if(in != null) in.close();
        }catch(IOException i){
            System.out.println(i);
        }
    }
}
    public static void main (String args[]){
        Server s = new Server(5000);
    }
}
