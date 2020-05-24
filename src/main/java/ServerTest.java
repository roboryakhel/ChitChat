import java.io.IOException;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) {
        for (int i=0; i<10; i++) {
            try {
                new Socket("127.0.0.1", 5000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("This is the map: " + Server.clientSockets.toString());
    }
}
