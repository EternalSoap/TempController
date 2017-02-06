package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.Connection;

/**
 * Created by frang on 21-Jan-17.
 */
public class TempController {

    private boolean debug = true;

    private Connection connection;

    @FXML
    private ChoiceBox<Choice> choiceBoxDesiredTemp = new ChoiceBox<>();

    public TempController(){

    }

    @FXML
    private void initialize(){
       //load data from db, set current desired temp etc

        choiceBoxDesiredTemp.setItems(Main.getObservableListTempChoice(15,30,true));


    }



    public void reinitialize() {

        initialize();

    }
}
