package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    private ObservableList<Room> roomsList = FXCollections.observableArrayList();
    private static ObservableList<Node> observableListChildren;



    public RoomController(){


    }

    private void removeItem(ListChangeListener.Change<? extends Room> c) {

        for(Room r : c.getRemoved()){
            r.removeFromDB();
        }
    }

    private void addItem(ListChangeListener.Change<? extends Room> c) {

        for(Room r : c.getAddedSubList()){
            if(r.getRoomID()==-1){
                r.addToDB();
            }
        }

    }

    @FXML
    private void initialize(){

        //get data from the db
        observableListChildren = anchorPaneRoom.getChildren();
        tableViewRoom = (TableView) observableListChildren.get(0);


        tableColumnRoomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());

        int selectedSpace = Main.getSelectedSpace();
        Main.debugOutput(debug, ""+selectedSpace);

        roomsList = Room.getRoomList(selectedSpace);
        roomsList.addListener(new ListChangeListener<Room>() {
            @Override
            public void onChanged(Change<? extends Room> c) {

                Main.debugOutput(debug,"List Change");

                while (c.next()) {
                    if (c.wasAdded()) {

                        addItem(c);

                    } else if (c.wasRemoved()) {

                        removeItem(c);

                    }

                }
            }
        });


        tableViewRoom.setItems(roomsList);
        tableViewRoom.refresh();




    }

    public void reinitialize() {

        tableViewRoom = (TableView) observableListChildren.get(0);

        int selectedSpace = Main.getSelectedSpace();
        Main.debugOutput(debug, ""+selectedSpace);

        roomsList = Room.getRoomList(selectedSpace);
        roomsList.addListener(new ListChangeListener<Room>() {
            @Override
            public void onChanged(Change<? extends Room> c) {

                Main.debugOutput(debug,"List Change");

                while (c.next()) {
                    if (c.wasAdded()) {

                        addItem(c);

                    } else if (c.wasRemoved()) {

                        removeItem(c);

                    }

                }
            }
        });

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
