package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Client;
import Model.Employee;
import Model.Message;
import Model.Service;
import com.google.gson.Gson;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateVisitPage {
    public JPanel createVisitPanel;
    private JComboBox servicesCB;
    private JPanel dateLabel;
    private JComboBox comboBox2;
    private JPanel datePanel;
    private JPanel timePanel;
    private JDateChooser dateChooser;

    public CreateVisitPage(Client client){
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
        dateChooser = new JDateChooser();
        datePanel.add(dateChooser);
        servicesCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Service selectedService = (Service) servicesCB.getSelectedItem();
                if(selectedService!=null) {
                    Map<String, String> params = new HashMap<String, String>() {
                        {
                            put("groupID", Integer.toString(selectedService.getGroupId()));
                        }
                    };
                    Message request = new Message("GetEmployeesByServiceGroupID", params);
                    try (ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                        handler.writeLine(gson.toJson(request));
                        String response = handler.readLine();
                        Employee[] employees = gson.fromJson(response, Employee[].class);
                        comboBox2.removeAllItems();
                        for (Employee emp :
                                employees) {
                            comboBox2.addItem(emp);
                        }
                    }
                    catch (IOException ex){
                        ex.printStackTrace();
                    }
                }

                System.out.println(servicesCB.getSelectedItem());
            }
        });
    }
}
