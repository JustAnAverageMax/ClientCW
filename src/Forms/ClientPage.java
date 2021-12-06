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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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

                    handler.writeLine(gson.toJson(request, Message.class));
                    String response = handler.readLine();

                    Vector<Visit> visits = gson.fromJson(response, Vector.class);

                    ClientMain.changePanel(new ClientVisits(visits).clientVisitsPanel);



                }
                catch(IOException ex){
                    ex.printStackTrace();
                }


            }
        });
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        };
        showVisitsButton.addActionListener(listener);
        makeVisit.addActionListener(listener);
    }

    public JPanel clientPagePanel;
    private JLabel clientName;
    private JLabel clientSurname;
    private JLabel clientLogin;
    private JButton showVisitsButton;
    private JButton makeVisit;
}
