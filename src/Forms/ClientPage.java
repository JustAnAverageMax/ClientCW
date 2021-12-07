package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.Client;
import Model.Message;
import Model.Visit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientPage {
    public ClientPage(Client client) {
        clientName.setText(client.getFirstName());
        clientSurname.setText(client.getLastName());
        clientLogin.setText(client.getLogin());
        showVisitsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    Map<String, String> params = new HashMap<String, String>(){
                        {
                            put("ID", Integer.toString(client.getId()));
                        }
                    };
                    Message request = new Message("GetClientVisits", params);
                    Gson gson = new Gson();

                    handler.writeLine(gson.toJson(request));
                    String response = handler.readLine();



                    Visit[] visits = gson.fromJson(response, Visit[].class);

                    ClientMain.changePanel(new ClientVisits(visits, client).clientVisitsPanel);



                }
                catch(IOException ex){
                    ex.printStackTrace();
                }


            }
        });
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new CreateVisitPage(client).createVisitPanel);
            }
        };
        //showVisitsButton.addActionListener(listener);
        makeVisit.addActionListener(listener);
    }

    public JPanel clientPagePanel;
    private JLabel clientName;
    private JLabel clientSurname;
    private JLabel clientLogin;
    private JButton showVisitsButton;
    private JButton makeVisit;
}
