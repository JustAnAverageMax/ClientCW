package Forms;

import Main.ClientMain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPage{
    private JButton signInButton;
    private JButton signUpButton;
    public JPanel startPagePanel;

    public StartPage() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new Registration().registrationPanel);
            }
        });
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AuthForm().authPanel);
            }
        });
    }
}
