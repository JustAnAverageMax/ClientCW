package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Employee;
import Model.Message;
import Model.Client;
import Model.Service;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServicesForm {
    public ServicesForm(Client client){
        Gson gson = new Gson();
        Map<String, String> params = new HashMap<String, String>(){
            {
                put("get", "all");
            }

        };
        Message request = new Message("GetAllServices", params);
        try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
            handler.writeLine(gson.toJson(request));
            String response = handler.readLine();
            Service[] services = gson.fromJson(response, Service[].class);

            for (Service s :
                    services) {
                servicesCB.addItem(s);
            }

        }catch(IOException ex){
            ex.printStackTrace();
        }
        employeesCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employee employee = (Employee) employeesCB.getSelectedItem();
                if(employee!=null) {
                    employeeEmailLabel.setText(employee.getEmail());
                    employeeNameLabel.setText(employee.getLastName() + " " + employee.getFirstName() + " " + employee.getPatronymic());
                    employeeNumberLabel.setText(employee.getPhoneNumber());
                }
            }
        });
        servicesCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Service service = (Service) servicesCB.getSelectedItem();

                serviceNameLabel.setText(service.getName());
                servicePriceLabel.setText(service.getPrice() + " руб.");
                serviceDescLabel.setText(service.getDescription());
                employeeEmailLabel.setText(null);
                employeeNameLabel.setText(null);
                employeeNumberLabel.setText(null);

                Map<String, String> params = new HashMap<String, String>() {
                    {
                        put("groupID", Integer.toString(service.getGroupId()));
                    }
                };
                Message request = new Message("GetEmployeesByServiceGroupID", params);
                try (ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    String response = handler.readLine();
                    Employee[] employees = gson.fromJson(response, Employee[].class);
                    employeesCB.removeAllItems();
                    for (Employee emp :
                            employees) {
                        employeesCB.addItem(emp);
                    }
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new ClientPage(client).clientPagePanel);
            }
        });
    }
    private JComboBox servicesCB;
    private JComboBox employeesCB;
    private JLabel serviceNameLabel;
    private JLabel servicePriceLabel;
    private JLabel serviceDescLabel;
    private JLabel employeeNameLabel;
    private JLabel employeeEmailLabel;
    private JLabel employeeNumberLabel;
    public JPanel servicesFormPanel;
    private JButton backButton;
}
