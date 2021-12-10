package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Admin;
import Model.Message;
import Model.RandomStringGenerator;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminCreationPage {
    public JPanel adminCreationPanel;
    private JButton backButton;
    private JLabel loginLabel;
    private JLabel passwordLabel;

    public AdminCreationPage(Admin admin) {
        Gson gson = new Gson();
        Map<String, String> params = new HashMap<String, String>() {
            {
                put("Get", "Count");
            }
        };
        Message request = new Message("GetAdminAccountsCount", params);
        try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
            handler.writeLine(gson.toJson(request));
            String response = handler.readLine();
            String newAdminLogin = "Administrator_" + (Integer.parseInt(response) + 1);
            Admin newAdmin = new Admin();
            newAdmin.setLogin(newAdminLogin);
            loginLabel.setText(newAdminLogin);
            String password = RandomStringGenerator.generateString(5, "abcdefghijkmlnopqrstuvwxyz1234567890");
            newAdmin.setPassword(password);
            passwordLabel.setText(password);

            params.put("Model", gson.toJson(newAdmin));
            request = new Message("AddAdmin", params);
            try (ConnectionHandler hnd = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                hnd.writeLine(gson.toJson(request));

            }
            catch (IOException exc){
                exc.printStackTrace();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AdminPage(admin).adminPagePanel);
            }
        });
    }
}
