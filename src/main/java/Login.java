import javax.swing.*;
import java.awt.*;

public class Login {
    private String username = "user";
    private String password = "0000";
    private JTextField uNameField;
    private JTextField passField;
    private JButton loginButton;

    public Login() {
        // make login screen

        JFrame frame = new JFrame("Login");
        JPanel panel = new JPanel();
        uNameField = new JTextField(20);
        passField = new JTextField(20);
        loginButton = new JButton("Login");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(uNameField);
        panel.add(passField);
        panel.add(loginButton);

        frame.add(panel);
        frame.setSize(300, 400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}
