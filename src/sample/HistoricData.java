package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by frang on 29-Jan-17.
 */
public class HistoricData {

    DoubleProperty hours;
    DoubleProperty temps;

    public HistoricData(int hours, double temp){
        this.hours = new SimpleDoubleProperty(hours);
        this.temps = new SimpleDoubleProperty(temp);
    }


    public double getHours() {
        return hours.get();
    }

    public DoubleProperty hoursProperty() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours.set(hours);
    }

    public double getTemps() {
        return temps.get();
    }

    public DoubleProperty tempsProperty() {
        return temps;
    }

    public void setTemps(double temps) {
        this.temps.set(temps);
    }

    public static ArrayList<HistoricData> getHistoricData(LocalDate date, int roomID){

        ArrayList<HistoricData> arrayListHistoricData = new ArrayList<>();
        AverageHelper averageHelper[] = new AverageHelper[25];
        for(int i=0;i<averageHelper.length;i++){
            averageHelper[i] = new AverageHelper();
        }

        Database database = new Database();
        Connection connection = database.getConnection();
        LocalDate upperLimit = date.plusDays(1);
        String getHistoricDataQuery = "select * from Mjerenje where datumVrijeme >= ? and datumVrijeme < ? and sobaID = ? ";

        try {
            PreparedStatement ps = connection.prepareStatement(getHistoricDataQuery);
            ps.setTimestamp(1, Timestamp.valueOf(date.atStartOfDay()));
            ps.setTimestamp(2,Timestamp.valueOf(upperLimit.atStartOfDay()));
            ps.setInt(3,roomID);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                LocalTime localTime = rs.getTimestamp(3).toLocalDateTime().toLocalTime();
                int hour = localTime.getHour();

                averageHelper[hour].addTemp(rs.getDouble(4));

            }

            for(int i=0;i<averageHelper.length; i++){
                if(averageHelper[i].numVals != 0){
                    arrayListHistoricData.add(new HistoricData(i,averageHelper[i].average()));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arrayListHistoricData;

    }

}
