package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;

/**
 * Created by frang on 21-Jan-17.
 */
public class TempController {

    private boolean debug = true;

    private Connection connection;

    @FXML
    private ChoiceBox<Choice> choiceBoxDesiredTemp = new ChoiceBox<>();
    @FXML
    private Label labelCurrentTemp;
    @FXML
    private TableView<TemperatureInfo> tableViewRoomTemp;
    @FXML
    private AnchorPane anchorPaneTemp = new AnchorPane();
    @FXML
    private TableColumn<TemperatureInfo,String> tableColumnRoomName = new TableColumn<>();
    @FXML
    private TableColumn<TemperatureInfo,Number> tableColumnRoomTemp = new TableColumn<>();

    private static ObservableList<Node> observableListChildren;
    private static ObservableList<TemperatureInfo> observableListTemperatureInfo;


    public TempController(){}

    @FXML
    private void initialize(){
       //load data from db, set current desired temp etc

        observableListChildren = anchorPaneTemp.getChildren();
        tableViewRoomTemp = (TableView<TemperatureInfo>) observableListChildren.get(0);
        labelCurrentTemp = (Label) observableListChildren.get(2);

        observableListTemperatureInfo = Main.getObservableListTemperatureInfo();

        tableColumnRoomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        tableColumnRoomTemp.setCellValueFactory(cellData -> cellData.getValue().roomTempProperty());

        choiceBoxDesiredTemp.setItems(Main.getObservableListTempChoice(15,30,true));
        tableViewRoomTemp.setItems(observableListTemperatureInfo);
        labelCurrentTemp.setText(String.valueOf(TemperatureInfo.getCurrentDesiredTemp(Main.getSelectedSpace())));



    }


    public void reinitialize() {

        tableViewRoomTemp = (TableView<TemperatureInfo>) observableListChildren.get(0);
        labelCurrentTemp = (Label) observableListChildren.get(2);

        observableListTemperatureInfo = Main.getObservableListTemperatureInfo();

        tableColumnRoomName.setCellValueFactory(cellData -> cellData.getValue().roomNameProperty());
        tableColumnRoomTemp.setCellValueFactory(cellData -> cellData.getValue().roomTempProperty());

        choiceBoxDesiredTemp.setItems(Main.getObservableListTempChoice(15,30,true));
        tableViewRoomTemp.setItems(observableListTemperatureInfo);
        labelCurrentTemp.setText(String.valueOf(TemperatureInfo.getCurrentDesiredTemp(Main.getSelectedSpace())));

    }


    @FXML
    private void onSelectButtonClicked(){

        Choice selectedManualTemp = choiceBoxDesiredTemp.getValue();

        TemperatureInfo.setManualTemp(selectedManualTemp);

        reinitialize();

    }


}
