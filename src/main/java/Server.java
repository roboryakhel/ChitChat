import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;



// This server can accept only two connections. It has only two client IDs to give out.
public class Server {
    ArrayList<PrintWriter> clientOutputStreams;
   public static HashMap<Socket, Integer> clientSockets;
   private int port = 5000;

    public class ClientHandler implements Runnable {
        String cliName;
        BufferedReader reader;
        Socket sock;
        boolean isLoggedIn;

        public ClientHandler(Socket cliSock, String name) {
            try {
                cliName = name;
                sock = cliSock;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } // close constructor

        public void run() {
            String message;
            try {
                while ((message=reader.readLine()) != null) {
                    String tmp = cliName+": ";
                    System.out.println("read " + tmp+message);

                    // bug: is the client is closed without closing socket, the loop runs
                    // infinitely printing out null
                    if (message.equals("logout")) {
                        this.isLoggedIn = false;
                        this.sock.close();
                        System.out.println(cliName + " logged out");
                        // todo: Remove the client output stream from clientOutputStreams
                        break;
                    }

                    // either send it to a specified client or everyone
                    // so far its everyone
                    tellEveryone(tmp+message);
                    message=null;
                } // close while
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server().go();
    }

    public void go() {
        clientOutputStreams = new ArrayList<>();
        clientSockets = new HashMap<>();
        int cliNum = 0;
        try {
            ServerSocket srvSock = new ServerSocket(port);

            while(true) {
                Socket cliSock = srvSock.accept();
                PrintWriter writer = new PrintWriter(cliSock.getOutputStream());
                clientOutputStreams.add(writer);
                clientSockets.put(cliSock, cliNum);

                Thread t = new Thread(new ClientHandler(cliSock, "Client" + cliNum));
                t.start();
                System.out.println("Got a connection with Client" + cliNum);
                cliNum++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    } // close go

    public void tellEveryone(String message) {
        Iterator<PrintWriter> it = clientOutputStreams.iterator();
        while(it.hasNext()) {
            try {
                PrintWriter writer =  it.next();
                writer.println(message);
                writer.flush();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } // end while
    } // close tellEveryone
} //close class