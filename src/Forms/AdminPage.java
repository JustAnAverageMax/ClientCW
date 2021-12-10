package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Admin;
import Model.Client;
import Model.Employee;
import Model.Message;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class AdminPage {
    public JPanel adminPagePanel;
    private JLabel adminLogin;
    private JButton showClientsButton;
    private JButton showEmployeesButton;
    private JButton showVisitsButton;
    private JButton createAdminButton;
    private JButton exitButton;

    public AdminPage(Admin admin) {
        adminLogin.setText(admin.getLogin());
        showClientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, String> params = new HashMap<String, String>(){
                    {
                        put("Get", "All");
                    }
                };
                Message request = new Message("GetAllClients", params);
                Gson gson = new Gson();
                try (ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    Client[] clients = gson.fromJson(handler.readLine(), Client[].class);
                    ClientMain.changePanel(new AdminClients(clients, admin).adminClientsPanel);
                }catch(IOException ex){
                    ex.printStackTrace();
                }

            }
        });
        showEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, String> params = new HashMap<String, String>(){
                    {
                        put("Get", "All");
                    }
                };
                Message request = new Message("GetAllEmployees", params);
                Gson gson = new Gson();
                try (ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    Employee[] employees = gson.fromJson(handler.readLine(), Employee[].class);
                    ClientMain.changePanel(new AdminEmployees(employees, admin).adminEmployeesPanel);
                }catch(IOException ex){
                    ex.printStackTrace();
                }

            }
        });
        showVisitsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AdminServices(admin).adminServicePanel);
            }
        });
        createAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AdminCreationPage(admin).adminCreationPanel);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AuthForm("Admin").authPanel);
            }
        });
    }
}
