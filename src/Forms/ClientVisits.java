package Forms;

import Model.Visit;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;

public class ClientVisits {
    public ClientVisits(Vector<Visit> visits){
        Vector<String> properVisits = new Vector<>();
        if(visits.isEmpty()){
            this.notFoundLabel.setText("Не было запланировано ни одного посещения");
        }
        else {
            for (Visit v :
                    visits) {
                properVisits.add(v.getDate() + ". " + v.getTime() + " | " + v.getServiceId());
            }
            this.visits.setListData(properVisits);
        }
    }
    public JPanel clientVisitsPanel;
    private JButton записатьсяНаУслугуButton;
    private JButton backButton;
    private JLabel notFoundLabel;
    private JList visits;
}
