package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;


/**
 * Created by frang on 23-Jan-17.
 */
public class SensorController {

    private boolean debug = true;


    @FXML
    private ChoiceBox<Choice> choiceBoxSensor;
    @FXML
    private ChoiceBox<Choice> choiceBoxRoom;
    @FXML
    private AnchorPane anchorPaneSensor = new AnchorPane();
    @FXML
    private TableView<SensorInfo> tableViewContent;
    @FXML
    private TableColumn<SensorInfo,Number> tableColumnSensorID = new TableColumn<>();
    @FXML
    private TableColumn<SensorInfo,String> tableColumnRoomName = new TableColumn<>();
    @FXML
    private TableColumn<SensorInfo,String> tableColumnSensorStatus = new TableColumn<>();

    private static ObservableList<Node> observableListChildren;
    private static ObservableList<Choice> observableListSensor;
    private static ObservableList<Choice> observableListRoom;
    private static ObservableList <SensorInfo> observableListSensorInfo;


    public SensorController(){

    }

    @FXML
    private void initialize(){

        observableListChildren = anchorPaneSensor.getChildren();
        choiceBoxSensor = (ChoiceBox<Choice>) observableListChildren.get(3);
        choiceBoxRoom = (ChoiceBox<Choice>) observableListChildren.get(5);
        tableViewContent = (TableView) observableListChildren.get(4);

        observableListRoom = Main.getChoices(Main.getObservableListRoom());
        observableListSensor = Main.getChoices(Main.getObservableListSensor());
        observableListSensorInfo = Main.getObservableListSensorInfo();

        tableColumnSensorID.setCellValueFactory(cellData -> cellData.getValue().sensorIDProperty());
        tableColumnRoomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        tableColumnSensorStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        choiceBoxSensor.setItems(observableListSensor);
        choiceBoxRoom.setItems(observableListRoom);
        tableViewContent.setItems(observableListSensorInfo);



    }

    @FXML
    private void onAddButtonClicked(){

        int sensorID = -1, roomID = -1;
        String roomName = "";
        Choice choiceSensor = choiceBoxSensor.getSelectionModel().getSelectedItem();
        Choice choiceRoom = choiceBoxRoom.getSelectionModel().getSelectedItem();
        if(choiceSensor != null && choiceRoom != null){
            sensorID = choiceSensor.id;
            roomID = choiceRoom.id;
            roomName = choiceRoom.displayString;
        }

        Main.debugOutput(debug,""+ sensorID + " " + roomID);

        if(sensorID >= 0 && roomID >= 0 && roomName !="") {
            for (int i = 0; i < observableListSensorInfo.size(); i++) {

                if(observableListSensorInfo.get(i).getSensorID() == sensorID)
                {
                    SensorInfo sensorInfo = new SensorInfo(sensorID,roomName,1,roomID);
                    observableListSensorInfo.set(i,sensorInfo);

                }
            }
        }
    }

    @FXML
    private void onRemoveButtonClicked(){

        SensorInfo sensorInfo = tableViewContent.getSelectionModel().getSelectedItem();
        if(sensorInfo != null){

            observableListSensorInfo.remove(sensorInfo);

        }

        reinitialize();



    }


    public void reinitialize() {

        choiceBoxSensor = (ChoiceBox<Choice>) observableListChildren.get(3);
        choiceBoxRoom = (ChoiceBox<Choice>) observableListChildren.get(5);
        tableViewContent = (TableView<SensorInfo>) observableListChildren.get(4);

        observableListRoom = Main.getChoices(Main.getObservableListRoom());
        observableListSensor = Main.getChoices(Main.getObservableListSensor());
        observableListSensorInfo = Main.getObservableListSensorInfo();

        choiceBoxSensor.setItems(observableListSensor);
        choiceBoxRoom.setItems(observableListRoom);
        tableViewContent.setItems(observableListSensorInfo);

        tableViewContent.refresh();

    }
}


