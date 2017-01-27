package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
    @SuppressWarnings({ "unchecked", "rawtypes" })

    final NumberAxis yAxis = new NumberAxis();
    final DateAxis xAxis = new DateAxis();

    @FXML
    private AnchorPane anchorPaneHistory = new AnchorPane();
    @FXML
    private DatePicker datePickerDate = new DatePicker();
    @FXML
    private AreaChart<Date,Number> areaChartHistory;


    public HistoryController(){}

    @FXML
    private void initialize() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        xAxis.setLabel("Datum");
        yAxis.setLabel("Temperatura");



        XYChart.Series<Date,Number> series = new XYChart.Series<>();
        series.setName("Events this Year");
        series.getData().add(new XYChart.Data(dateFormat.parse("11/1/2014"), 23));
        series.getData().add(new XYChart.Data(dateFormat.parse("09/2/2014"), 14));
        series.getData().add(new XYChart.Data(dateFormat.parse("22/3/2014"), 15));
        series.getData().add(new XYChart.Data(dateFormat.parse("14/4/2014"), 24));
        series.getData().add(new XYChart.Data(dateFormat.parse("22/5/2014"), 34));
        series.getData().add(new XYChart.Data(dateFormat.parse("07/6/2014"), 36));
        series.getData().add(new XYChart.Data(dateFormat.parse("22/7/2014"), 22));
        series.getData().add(new XYChart.Data(dateFormat.parse("21/8/2014"), 45));
        series.getData().add(new XYChart.Data(dateFormat.parse("04/9/2014"), 43));
        series.getData().add(new XYChart.Data(dateFormat.parse("22/10/2014"), 17));
        series.getData().add(new XYChart.Data(dateFormat.parse("30/11/2014"), 29));
        series.getData().add(new XYChart.Data(dateFormat.parse("10/12/2014"), 25));

        areaChartHistory = new AreaChart<Date, Number>(xAxis,yAxis);
        areaChartHistory.setTitle("Test");


        areaChartHistory.getData().add(series);

    }

    public void reinitialize() throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        xAxis.setLabel("Datum");
        yAxis.setLabel("Temperatura");



        XYChart.Series<Date,Number> series = new XYChart.Series<>();
        series.setName("Events this Year");
        series.getData().add(new XYChart.Data(dateFormat.parse("11/1/2014"), 23));
        series.getData().add(new XYChart.Data(dateFormat.parse("09/2/2014"), 14));
        series.getData().add(new XYChart.Data(dateFormat.parse("22/3/2014"), 15));
        series.getData().add(new XYChart.Data(dateFormat.parse("14/4/2014"), 24));
        series.getData().add(new XYChart.Data(dateFormat.parse("22/5/2014"), 34));
        series.getData().add(new XYChart.Data(dateFormat.parse("07/6/2014"), 36));
        series.getData().add(new XYChart.Data(dateFormat.parse("22/7/2014"), 22));
        series.getData().add(new XYChart.Data(dateFormat.parse("21/8/2014"), 45));
        series.getData().add(new XYChart.Data(dateFormat.parse("04/9/2014"), 43));
        series.getData().add(new XYChart.Data(dateFormat.parse("22/10/2014"), 17));
        series.getData().add(new XYChart.Data(dateFormat.parse("30/11/2014"), 29));
        series.getData().add(new XYChart.Data(dateFormat.parse("10/12/2014"), 25));

        areaChartHistory = new AreaChart<Date, Number>(xAxis,yAxis);
        areaChartHistory.setTitle("Test");


        areaChartHistory.getData().add(series);
    }
}
