package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.*;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;

public class ClientVisits {
    public ClientVisits(Visit[] visits, Client client){
        Vector<Visit> formattedVisits = new Vector<>();
        ArrayList<Visit> visitsToRemove = new ArrayList<>();
        if(visits.length == 0){
            this.notFoundLabel.setText("Не было запланировано ни одного посещения");
        }
        else {
            Gson gson = new Gson();
            for (Visit v : visits) {

                Map<String, String> params = new HashMap<String, String>() {
                    {
                        put("serviceID", Integer.toString(v.getServiceId()));
                        put("employeeID", Integer.toString(v.getEmployeeId()));
                    }
                };

                Message request = new Message("GetService", params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    v.setService(gson.fromJson(handler.readLine(), Service.class));
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                request = new Message("GetEmployee", params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    v.setEmployee(gson.fromJson(handler.readLine(), Employee.class));
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                formattedVisits.add(v);

            }
            this.visitsList.setListData(formattedVisits);
        }
        this.visitsList.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                for (Object obj :
                        visitsList.getSelectedValuesList()) {
                    if(!visitsToRemove.contains(obj)) {
                        visitsToRemove.add((Visit) obj);
                    }
                }
                System.out.println(visitsList.getSelectedValuesList());
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new ClientPage(client).clientPagePanel);
            }
        });

        removeVisits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gson gson = new Gson();
                Map<String, String> params = new HashMap<String, String>(){
                    {
                        put("visitsToRemove", gson.toJson(visitsToRemove));
                    }
                };
                Message request = new Message("RemoveVisits", params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    if(handler.readLine().equals("OK")){
                        ClientMain.changePanel(new ClientPage(client).clientPagePanel);
                    }

                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        createVisit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new CreateVisitPage(client).createVisitPanel);
            }
        });
    }
    public JPanel clientVisitsPanel;
    private JButton createVisit;
    private JButton backButton;
    private JLabel notFoundLabel;
    private JList visitsList;
    private JButton removeVisits;
}
