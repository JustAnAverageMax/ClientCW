package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Admin;
import Model.Client;
import Model.Message;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthForm {
    public JPanel authPanel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton signinbutton;
    private JLabel errorLabel;
    private JButton backButton;

    public AuthForm(String role) {
        signinbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                Map<String, String> params = new HashMap<>(){
                    {
                        put("Login", loginField.getText());
                        put("Password", password);
                    }
                };

                Gson gson = new Gson();

                String response = null;
                String command = "Auth" + role;
                Message request = new Message(command, params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    response = handler.readLine();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
                if (response.equals("Failed")) {
                    errorLabel.setText("Ошибка. Пользователь с такими логином и паролем не найден");
                } else {
                    try (ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)) {
                        params.put("ID", response);
                        request = new Message("Get" + role + "ByID", params);
                        handler.writeLine(gson.toJson(request));
                        switch (role) {
                            case "Client":
                                Client client = gson.fromJson(handler.readLine(), Client.class);
                                ClientMain.changePanel(new ClientPage(client).clientPagePanel);
                                break;
                            case "Admin":
                                Admin admin = gson.fromJson(handler.readLine(), Admin.class);
                                ClientMain.changePanel(new AdminPage(admin).adminPagePanel);
                        }
                    }
                    catch(IOException ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new StartPage().startPagePanel);
            }
        });
    }
}
