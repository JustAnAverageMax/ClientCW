package Main;

import Forms.StartPage;
import Model.Message;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.IOException;

public class ClientMain {
    public static final int port = 8080;
    public static final String ip = "localhost";
    public static JFrame frame;
    public static void changePanel(JPanel panel){
        frame.setContentPane(panel);
        frame.pack();
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        frame = new JFrame("App");
        changePanel(new StartPage().startPagePanel);
    }
}
