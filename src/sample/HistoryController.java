package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by frang on 27.1.2017..
 */
public class HistoryController {



    @FXML
    private AnchorPane anchorPaneHistory = new AnchorPane();
    @FXML
    private DatePicker datePickerDate = new DatePicker();
    @FXML
    private AreaChart<Number,Number> areaChartHistory;
    @FXML
    private ChoiceBox<Choice> choiceBoxRoom;

    private static ObservableList<Node> observableListChildren;
    private static ObservableList<HistoricData> observableListHistory;
    private static ObservableList<Choice> observableListRoom;

    public HistoryController(){}

    @FXML
    private void initialize(){

        observableListChildren = anchorPaneHistory.getChildren();

        areaChartHistory = (AreaChart<Number, Number>) observableListChildren.get(0);
        choiceBoxRoom = (ChoiceBox<Choice>) observableListChildren.get(1);

        observableListRoom = Main.getChoices(Main.getObservableListRoom());

        choiceBoxRoom.setItems(observableListRoom);

        areaChartHistory.setTitle("Prosječna temperatura po satu");



    }


    public void reinitialize() {

        areaChartHistory = (AreaChart<Number, Number>) observableListChildren.get(0);
        choiceBoxRoom = (ChoiceBox<Choice>) observableListChildren.get(1);

        observableListRoom = Main.getChoices(Main.getObservableListRoom());

        choiceBoxRoom.setItems(observableListRoom);

    }

    @FXML
    private void onShowButtonClicked(){

        LocalDate localDate = datePickerDate.getValue();
        XYChart.Series<Number,Number> series = new XYChart.Series<>();

        Choice room = choiceBoxRoom.getValue();
        if(localDate != null && room != null) {
            series.setName(room.displayString);
            observableListHistory = FXCollections.observableArrayList(HistoricData.getHistoricData(localDate,room.id));
            for (HistoricData hd :
                    observableListHistory) {
                series.getData().add(new XYChart.Data<>(hd.getHours(),hd.getTemps()));
            }

            areaChartHistory.getData().clear();

            areaChartHistory.getData().addAll(series);

        }
    }

}
