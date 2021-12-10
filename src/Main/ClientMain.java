package Main;
import Forms.StartPage;
import javax.swing.*;


public class ClientMain {
    public static final int port = 8080;
    public static final String ip = "localhost";
    public static JFrame frame;
    public static void changePanel(JPanel panel){
        frame.setContentPane(panel);
        frame.pack();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        frame = new JFrame("App");
        changePanel(new StartPage().startPagePanel);
    }
}
