package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by frang on 21-Jan-17.
 */
public class RoomController {

    private boolean debug = true;



    @FXML
    private TableView tableViewRoom = new TableView();
    @FXML
    private TableColumn<Room,String> tableColumnRoomName = new TableColumn<>();
    @FXML
    private Button btnRoomAdd = new Button();
    @FXML
    private Button btnRoomDelete = new Button();
    @FXML
    private TextField txtRoomName = new TextField();

    private ObservableList<Room> roomsList = FXCollections.observableArrayList();



    public RoomController(){
    }

    @FXML
    private void initialize(){
        //get data from the db


        tableColumnRoomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());

        int selectedSpace = Main.getSelectedSpace();
        Main.debugOutput(debug, ""+selectedSpace);

        roomsList = Room.getRoomList(selectedSpace);


        tableViewRoom.setItems(roomsList);




    }

}
