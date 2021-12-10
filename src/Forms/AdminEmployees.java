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
import java.util.HashMap;
import java.util.Map;

public class AdminEmployees {
    public JPanel adminEmployeesPanel;
    private JList employeesList;
    private JButton fireButton;
    private JButton backButton;
    private JLabel notFoundLabel;

    public AdminEmployees(Employee[] employees, Admin admin) {
        Gson gson = new Gson();
        if(employees.length == 0){
            this.notFoundLabel.setText("На данный момент не зарегистрировано ни одного клиента");
        }
        else {
            this.employeesList.setListData(employees);
        }
        fireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, String> params = new HashMap<String, String>(){
                    {
                        put("employeesToFire", gson.toJson(employeesList.getSelectedValuesList()));
                    }
                };
                Message request = new Message("FireEmployees", params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    if(handler.readLine().equals("OK")){
                        ClientMain.changePanel(new AdminPage(admin).adminPagePanel);
                    }
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AdminPage(admin).adminPagePanel);
            }
        });
    }
}
