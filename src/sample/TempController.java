package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;

/**
 * Created by frang on 21-Jan-17.
 */
public class TempController {

    private boolean debug = true;

    private Connection connection;

    @FXML
    private TextField txtDesiredTemp = new TextField();

    public TempController(){
        Database database = new Database();
        this.connection = database.getConnection();

    }

    @FXML
    private void initialize(){
       //load data from db, set current desired temp etc





    }

    @FXML
    private void onMouseClick(){



    }



}
