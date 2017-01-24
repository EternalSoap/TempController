package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class MainController {

    boolean debug = true;

    @FXML
    private AnchorPane anchorPaneMain = new AnchorPane();
    @FXML
    private ToggleGroup toggleGroupHeating = new ToggleGroup();
    @FXML
    private TabPane tabPaneContext;
    @FXML
    private SpaceController spaceController = new SpaceController();
    @FXML
    private RoomController roomController = new RoomController();
    @FXML
    private TempController tempController = new TempController();
    @FXML
    private SensorController sensorController = new SensorController();
    @FXML
    private Tab tabTemp;
    @FXML
    private Tab tabSpace;
    @FXML
    private Tab tabRoom;
    @FXML
    private Tab tabSensor;
    @FXML
    private Tab tabSchedule;
    @FXML
    private Tab tabHistory;

    public MainController(){

        tabPaneContext =(TabPane) anchorPaneMain.lookup("#tabPaneContent");



    }

    @FXML
    private void initialize(){
        tabPaneContext =(TabPane) anchorPaneMain.lookup("#tabPaneContent");

        tabPaneContext.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {

                        Main.debugOutput(debug, "Changed");

                        if(newValue == tabTemp){

                            tempController.reinitialize();

                        }else if (newValue == tabSpace){

                            spaceController.reinitialize();

                        }else if (newValue == tabRoom){

                            roomController.reinitialize();

                        }else if (newValue == tabSensor){

                            sensorController.reinitialize();

                        }else if (newValue == tabSchedule){

                        }else if (newValue == tabHistory){

                        }

                    }
                }
        );

    }



}
