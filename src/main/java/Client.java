import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client {
    JTextArea incoming;
    JTextField outgoing;
    PrintWriter writer;
    BufferedReader reader;
    Socket sock;
    boolean isLoggedOut; // the client isn't connected until they press the login button


    public void go(){
        // make gui and register listener wth send button
        // call networking method
        JFrame frame = new JFrame("Very Simple Chat Client");
        JPanel panel = new JPanel();
        JPanel infoPanel = new JPanel();
        JTextArea myName = new JTextArea(1, 25);
        JTextArea clientsInChat = new JTextArea(15, 25);
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        JButton logoutButton = new JButton("Logout");
        JButton loginButton = new JButton("Login");
        sendButton.addActionListener(new SendButtonListener());
        logoutButton.addActionListener(new LogoutButtonListener());
        loginButton.addActionListener(new LoginButtonListener());
        panel.add(qScroller);
        panel.add(outgoing);
        panel.add(sendButton);
        panel.add(logoutButton);
        panel.add(loginButton);
        infoPanel.add(myName);
        infoPanel.add(clientsInChat);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.EAST, infoPanel);
        //setUpNetworking();

        //Thread readerThread = new Thread(new IncomingReader());
        //readerThread.start();
        frame.setSize(400, 500);
        frame.setVisible(true);

        isLoggedOut = true;
    }

    private void setUpNetworking() {
        // make socket then make print writer
        // assign printwriter to writer instance variable
        try {
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader strReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(strReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("Network Established");

            Thread readerThread = new Thread(new IncomingReader());
            readerThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // get the text from text field
            // send it to server using the writer
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch(Exception e) {
                e.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class LogoutButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                writer.println("logout");
                writer.flush();
                isLoggedOut = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // doesnt log back in as the same client
    public class LoginButtonListener implements ActionListener {
        public void actionPerformed (ActionEvent event) {
            try {
                if (isLoggedOut)
                    setUpNetworking();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    incoming.append(message + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Client().go();
    }
}
