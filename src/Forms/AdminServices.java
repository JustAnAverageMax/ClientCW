package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Admin;
import Model.Message;
import Model.Service;
import Model.ServiceGroup;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServices {
    public JPanel adminServicePanel;
    private JComboBox servicesCB;
    private JTextField servicePrice;
    private JButton changePrice;
    private JTextField newNameTF;
    private JButton changeName;
    private JComboBox serviceGroupCB;
    private JButton changeServiceGroup;
    private JButton backButton;
    private JTextPane serviceDescription;
    private JButton changeServiceDescription;

    private Service selectedService;
    private ServiceGroup selectedServiceGroup;

    private void updateService(Service serviceToUpdate){
        Gson gson = new Gson();
        Map<String, String> params = new HashMap<String, String>();
        params.put("Model", gson.toJson(selectedService));
        Message request = new Message("UpdateService", params);
        try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)) {
            handler.writeLine(gson.toJson(request));
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public AdminServices(Admin admin) {
        selectedService = new Service();
        selectedServiceGroup = new ServiceGroup();
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
                params.put("serviceID", Integer.toString(s.getId()));
                params.put("groupID", Integer.toString(s.getGroupId()));
                request = new Message("GetServiceGroupByServiceID", params);
                try (ConnectionHandler handler1 = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler1.writeLine(gson.toJson(request));
                    s.setServiceGroup(gson.fromJson(handler1.readLine(), ServiceGroup.class));
                }catch(IOException ex){
                    ex.printStackTrace();
                }
                servicesCB.addItem(s);
            }

        }catch(IOException ex){
            ex.printStackTrace();
        }
        request = new Message("GetAllServiceGroups", params);
        try (ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
            handler.writeLine(gson.toJson(request));
            String response = handler.readLine();
            ServiceGroup[] serviceGroups = gson.fromJson(response, ServiceGroup[].class);

            for (ServiceGroup sg:
                 serviceGroups) {
                serviceGroupCB.addItem(sg);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }


        changeName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedService = (Service) servicesCB.getSelectedItem();
                if(selectedService!=null){
                    selectedService.setName(newNameTF.getText());
                    updateService(selectedService);
                }
            }
        });
        changePrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedService = (Service) servicesCB.getSelectedItem();
                if(selectedService!=null){
                    selectedService.setPrice(Integer.parseInt(servicePrice.getText()));
                    updateService(selectedService);
                }
            }
        });
        changeServiceGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedService = (Service) servicesCB.getSelectedItem();
                selectedServiceGroup = (ServiceGroup) serviceGroupCB.getSelectedItem();
                if(selectedService!=null && selectedServiceGroup!=null){
                    selectedService.setGroupId(selectedServiceGroup.getId());
                    selectedService.setServiceGroup(selectedServiceGroup);
                    updateService(selectedService);

                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AdminPage(admin).adminPagePanel);
            }
        });
        servicesCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedService = (Service) servicesCB.getSelectedItem();
                newNameTF.setText(selectedService.getName());
                serviceGroupCB.setSelectedItem(selectedService.getServiceGroup());
                servicePrice.setText(Integer.toString(selectedService.getPrice()));
                serviceDescription.setText(selectedService.getDescription());
            }
        });
        serviceGroupCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        changeServiceDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedService!=null){
                    selectedService.setDescription(serviceDescription.getText());
                    updateService(selectedService);
                }
            }
        });
    }
}
