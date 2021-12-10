package Forms;

import Main.ClientMain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInRole {
    private JButton signInAsAdmin;
    public JPanel signInRolePanel;
    private JButton signInAsClient;

    public SignInRole() {
        signInAsAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AuthForm("Admin").authPanel);
            }
        });
        signInAsClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AuthForm("Client").authPanel);
            }
        });
    }
}
