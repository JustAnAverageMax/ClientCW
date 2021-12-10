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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AdminClients {
    public AdminClients(Client[] clients, Admin admin){
        Gson gson = new Gson();
        if(clients.length == 0){
            this.notFoundLabel.setText("На данный момент не зарегистрировано ни одного клиента");
        }
        else {

            for (Client client :
                    clients) {
                Map<String, String> params = new HashMap<String, String>(){
                    {
                        put("ID", Integer.toString(client.getId()));
                    }
                };
                Message request = new Message("GetClientVisitsCountByID", params);
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    handler.writeLine(gson.toJson(request));
                    client.setVisitsCount(Integer.parseInt(handler.readLine()));
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
            this.clientsList.setListData(clients);
        }
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new AdminPage(admin).adminPagePanel);
            }
        });

        banButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, String> params = new HashMap<String, String>(){
                    {
                        put("clientsToBan", gson.toJson(clientsList.getSelectedValuesList()));
                    }
                };
                Message request = new Message("BanClients", params);
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
    }
    public JPanel adminClientsPanel;
    private JList clientsList;
    private JButton backButton;
    private JLabel notFoundLabel;
    private JButton banButton;
}
