package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Pair;

public class IssueBookController {

    @FXML
    private ToggleGroup group;
    @FXML
    private RadioButton radio_out;
    @FXML
    private RadioButton radio_readRoom;
    @FXML
    private Button submit;
    @FXML
    private TextField userId;


    AlertMessage alert = new AlertMessage();

    public IssueBookController(){

    }

    public Pair<Integer,Integer> getInfoToIssue() throws NullPointerException{
            String idS = userId.getText();
            int id = 0;
            if(idS != null || !idS.trim().isEmpty()){
                id = Integer.parseInt(idS);
            }
            int radio = Integer.parseInt(((RadioButton) group.getSelectedToggle()).getId());

        submit.setOnAction(this::closeStage);
            return new Pair<>(id, radio);
    }

    public void closeStage(ActionEvent event) {
        if(event.getSource() == submit){
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        }
    }
}
