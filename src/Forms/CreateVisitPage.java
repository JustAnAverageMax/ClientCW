package Forms;

import Main.ClientMain;
import Main.ConnectionHandler;
import Model.*;
import com.google.gson.Gson;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateVisitPage {
    public JPanel createVisitPanel;
    private JComboBox servicesCB;
    private JComboBox employeesCB;
    private JPanel datePanel;
    private JButton сreateButton;
    private JButton backButton;
    private JSpinner spinner1;
    private JDateChooser dateChooser;

    public CreateVisitPage(Client client){
        Visit createVisit = new Visit();
        createVisit.setClientId(client.getId());
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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24); // 24 == 12 PM == 00:00:00
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(calendar.getTime());

        spinner1.setModel(model);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner1, "HH:mm:ss");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); // this makes what you want
        formatter.setOverwriteMode(true);

        spinner1.setEditor(editor);

        servicesCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Service selectedService = (Service) servicesCB.getSelectedItem();

                if(selectedService!=null) {
                    createVisit.setServiceId(selectedService.getId());
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
                System.out.println(servicesCB.getSelectedItem());
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientMain.changePanel(new ClientPage(client).clientPagePanel);
            }
        });
        employeesCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employee employee = (Employee) employeesCB.getSelectedItem();
                if(employee!=null){
                    createVisit.setEmployeeId(employee.getId());
                }
            }
        });
        сreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date(dateChooser.getDate().getTime());
                createVisit.setDate(date);

                SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
                String spinnerValue = formater.format(spinner1.getValue());
                Time time = new Time(Time.valueOf(spinnerValue).getTime());
                createVisit.setTime(time);
                Gson gson = new Gson();
                try(ConnectionHandler handler = new ConnectionHandler(ClientMain.ip, ClientMain.port)){
                    Map<String, String> params = new HashMap<String, String>(){
                        {
                            put("Model", gson.toJson(createVisit));
                        }
                    };
                    Message request = new Message("AddVisit", params);
                    handler.writeLine(gson.toJson(request));
                    String response = handler.readLine();

                    ClientMain.changePanel(new ClientPage(client).clientPagePanel);

                }catch(IOException ex){

                }
            }
        });
    }
}
