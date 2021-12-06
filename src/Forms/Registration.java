package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Client;
import Model.Message;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Registration {


    public JPanel registrationPanel;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JTextField numberField;
    private JTextField emailField;
    private JTextField addressField;
    private JButton registrationButton;

    public Registration() {
        registrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gson gson = new Gson();
                Client client = new Client();
                client.setFirstName(nameField.getText());
                client.setLastName(surnameField.getText());
                client.setLogin(loginField.getText());
                String password = new String(passwordField.getPassword());
                client.setPassword(password);
                String phoneNumber = numberField.getText();

                client.setPhoneNumber(phoneNumber.replaceFirst("(\\d{2})(\\d{3})(\\d{2})(\\d+)", "+375 ($1) $2-$3-$4"));

                client.setEmail(emailField.getText());
                client.setAddress(addressField.getText());

                Map<String, String> params = new HashMap<String, String>(){
                    {
                        put("Model", gson.toJson(client));
                    }
                };

                Message request = new Message("AddClient", params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)) {
                    System.out.println("Connected");
                    handler.writeLine(gson.toJson(request, Message.class));
                    if(handler.readLine().equals("OK")) {
                        ClientMain.changePanel(new ClientPage(client).clientPagePanel);
                    }
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
    }
}
