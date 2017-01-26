package sample;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

/**
 * Created by frang on 25-Jan-17.
 */
public class ScheduleController {

    private boolean debug = true;

    @FXML
    private DatePicker datePickerStartDate = new DatePicker();
    @FXML
    private DatePicker datePickerEndDate = new DatePicker();
    @FXML
    private TableView<Schedule> tableViewSchedule;
    @FXML
    private AnchorPane anchorPaneSchedule = new AnchorPane();
    @FXML
    private TableColumn<Schedule, LocalDate> tableColumnStartDate = new TableColumn<>();
    @FXML
    private TableColumn<Schedule, LocalDate> tableColumnEndDate = new TableColumn<>();
    @FXML
    private TableColumn<Schedule, Number> tableColumnDayTemp = new TableColumn<>();
    @FXML
    private TableColumn<Schedule,Number> tableColumnNightTemp = new TableColumn<>();
    @FXML
    private ChoiceBox<Choice> choiceBoxDayTemp;
    @FXML
    private ChoiceBox<Choice> choiceBoxNightTemp;


    private static ObservableList<Node> observableListChildren;
    private static ObservableList<Schedule> observableListSchedule;
    private static ObservableList<Choice> observableListTempChoice;

    public ScheduleController() {}

    @FXML
    private void initialize(){

        observableListChildren = anchorPaneSchedule.getChildren();
        tableViewSchedule = (TableView<Schedule>) observableListChildren.get(0);
        choiceBoxDayTemp = (ChoiceBox<Choice>) observableListChildren.get(7);
        choiceBoxNightTemp = (ChoiceBox<Choice>) observableListChildren.get(8);

        observableListSchedule = Main.getObservableListSchedule();
        observableListTempChoice = Main.getObservableListTempChoice(15,30);

        tableColumnStartDate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        tableColumnEndDate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        tableColumnDayTemp.setCellValueFactory(cellData -> cellData.getValue().dayTempProperty());
        tableColumnNightTemp.setCellValueFactory(cellData -> cellData.getValue().nightTempProperty());

        tableViewSchedule.setItems(observableListSchedule);
        choiceBoxDayTemp.setItems(observableListTempChoice);
        choiceBoxNightTemp.setItems(observableListTempChoice);

    }


    public void reinitialize() {

        tableViewSchedule = (TableView<Schedule>) observableListChildren.get(0);
        choiceBoxDayTemp = (ChoiceBox<Choice>) observableListChildren.get(7);
        choiceBoxNightTemp = (ChoiceBox<Choice>) observableListChildren.get(8);

        observableListSchedule = Main.getObservableListSchedule();
        observableListTempChoice = Main.getObservableListTempChoice(15,30);

        tableColumnStartDate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        tableColumnEndDate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        tableColumnDayTemp.setCellValueFactory(cellData -> cellData.getValue().dayTempProperty());
        tableColumnNightTemp.setCellValueFactory(cellData -> cellData.getValue().nightTempProperty());

        tableViewSchedule.setItems(observableListSchedule);
        choiceBoxDayTemp.setItems(observableListTempChoice);
        choiceBoxNightTemp.setItems(observableListTempChoice);

    }


    @FXML
    private void onAddButtonClicked(){

        Choice dayTempChoice = choiceBoxDayTemp.getValue();
        Choice nightTempChoice = choiceBoxNightTemp.getValue();
        LocalDate startDate = datePickerStartDate.getValue();
        LocalDate endDate = datePickerEndDate.getValue();

        if(endDate !=null && !Schedule.isCorrect(startDate,endDate)) return;

            if (startDate != null && dayTempChoice != null) {
                Schedule schedule = new Schedule(
                        -1,
                        startDate,
                        endDate,
                        dayTempChoice.id,
                        nightTempChoice == null ? null : nightTempChoice.id
                );
                if (!schedule.isOverlapping()) {

                    observableListSchedule.add(schedule);

                }
            }

    }

    @FXML
    private void onRemoveButtonClicked(){

        Schedule schedule = tableViewSchedule.getSelectionModel().getSelectedItem();

        observableListSchedule.remove(schedule);

    }

}
