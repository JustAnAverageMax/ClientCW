package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Client;
import Model.Message;
import Model.Visit;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

public class ClientVisits {
    public ClientVisits(Visit[] visits, Client client){
        Vector<String> formattedVisits = new Vector<>();
        if(visits.length == 0){
            this.notFoundLabel.setText("Не было запланировано ни одного посещения");
        }
        else {
            Gson gson = new Gson();
            for (Visit v : visits) {

                String serviceName = null;
                String employeeName = null;
                Map<String, String> params = new HashMap<String, String>() {
                    {
                        put("serviceID", Integer.toString(v.getServiceId()));
                        put("employeeID", Integer.toString(v.getEmployeeId()));
                    }
                };

                Message request = new Message("GetServiceNameByID", params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    serviceName = handler.readLine();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                request = new Message("GetEmployeeNameByID", params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    employeeName = handler.readLine();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                formattedVisits.add(serviceName + " " + employeeName + " " + v.getDate() + " " + v.getTime());
            }
            this.visits.setListData(formattedVisits);
        }
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new ClientPage(client).clientPagePanel);
            }
        });
    }
    public JPanel clientVisitsPanel;
    private JButton записатьсяНаУслугуButton;
    private JButton backButton;
    private JLabel notFoundLabel;
    private JList visits;
}
