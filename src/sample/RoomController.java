package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Created by frang on 21-Jan-17.
 */
public class RoomController {

    private boolean debug = true;


    @FXML
    private AnchorPane anchorPaneRoom = new AnchorPane();
    @FXML
    private TableView<Room> tableViewRoom;
    @FXML
    private TableColumn<Room,String> tableColumnRoomName = new TableColumn<>();
    @FXML
    private Button btnRoomAdd = new Button();
    @FXML
    private Button btnRoomDelete = new Button();
    @FXML
    private TextField txtRoomName = new TextField();

    private static ObservableList<Room> roomsList = FXCollections.observableArrayList();
    private static ObservableList<Node> observableListChildren;



    public RoomController(){

    }

    @FXML
    private void initialize(){

        //get data from the db
        observableListChildren = anchorPaneRoom.getChildren();
        tableViewRoom = (TableView) observableListChildren.get(0);


        tableColumnRoomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());

        int selectedSpace = Main.getSelectedSpace();
        Main.debugOutput(debug, ""+selectedSpace);

        roomsList = Main.getObservableListRoom();

        tableViewRoom.setItems(roomsList);
        tableViewRoom.refresh();




    }

    public void reinitialize() {

        tableViewRoom = (TableView) observableListChildren.get(0);

        roomsList = Main.getObservableListRoom();

        tableViewRoom.setItems(roomsList);

        tableViewRoom.refresh();


    }

    @FXML
    private void onAddButtonClicked(){

        String roomName = txtRoomName.getText();
        int selectedSpace = Main.getSelectedSpace();
        if(roomName !=null){
            Room room = new Room(-1,roomName,selectedSpace);
            roomsList.add(room);
        }

    }

    @FXML
    private void onRemoveButtonClicked(){
        Main.debugOutput(debug,"REMOVE");

        Room room = tableViewRoom.getSelectionModel().getSelectedItem();
        if(room!=null) {
            roomsList.remove(room);
        }



    }

}
