package sample;

import javafx.beans.property.*;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by frang on 25-Jan-17.
 */
public class Schedule {

    private IntegerProperty scheduleID;
    private ObjectProperty<LocalDate> startDate;
    private ObjectProperty<LocalDate> endDate;
    private IntegerProperty dayTemp;
    private IntegerProperty nightTemp;
    private BooleanProperty isInfinite;

    public Schedule(Integer scheduleID, LocalDate startDate, LocalDate endDate, Integer dayTemp, Integer nightTemp){

        this.scheduleID = new SimpleIntegerProperty(scheduleID);
        this.startDate = new SimpleObjectProperty<>(startDate);
        if(endDate != null){
            this.endDate = new SimpleObjectProperty<>(endDate);
            this.isInfinite = new SimpleBooleanProperty(false);
        }else{
            this.isInfinite = new SimpleBooleanProperty(true);
        }
        this.dayTemp = new SimpleIntegerProperty(dayTemp);
        if (nightTemp !=null){
            this.nightTemp = new SimpleIntegerProperty(nightTemp);
        }
    }

    public int getScheduleID() {
        return scheduleID.get();
    }

    public IntegerProperty scheduleIDProperty() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID.set(scheduleID);
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public int getDayTemp() {
        return dayTemp.get();
    }

    public IntegerProperty dayTempProperty() {
        return dayTemp;
    }

    public void setDayTemp(int dayTemp) {
        this.dayTemp.set(dayTemp);
    }

    public int getNightTemp() {
        return nightTemp.get();
    }

    public IntegerProperty nightTempProperty() {
        return nightTemp;
    }

    public void setNightTemp(int nightTemp) {
        this.nightTemp.set(nightTemp);
    }

    public boolean isIsInfinite() {
        return isInfinite.get();
    }

    public BooleanProperty isInfiniteProperty() {
        return isInfinite;
    }

    public void setIsInfinite(boolean isInfinite) {
        this.isInfinite.set(isInfinite);
    }

    public static ArrayList<Schedule> getScheduleList() {

        ArrayList<Schedule> scheduleArrayList = new ArrayList<>();
        Database database = new Database();
        Connection connection = database.getConnection();
        String getScheduleListQuery = "SELECT Raspored.rasporedID, Raspored.datumOd,Raspored.datumDo,Raspored.tempDan,Raspored.tempNoc from Raspored join Prostor_Raspored on Raspored.rasporedID = Prostor_Raspored.rasporedID join Prostor on Prostor.prostorID = Prostor_Raspored.prostorID join Korisnik on Prostor.prostorID = Korisnik.odabraniProstor";

        try {
            PreparedStatement ps = connection.prepareStatement(getScheduleListQuery);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                scheduleArrayList.add(new Schedule(
                        rs.getInt(1),
                        rs.getTimestamp(2).toLocalDateTime().toLocalDate(),
                        (rs.getTimestamp(3))==null ?null: rs.getTimestamp(3).toLocalDateTime().toLocalDate(),
                        rs.getInt(4),
                        rs.getInt(5)
                ));

            }
            database.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scheduleArrayList;
    }

    public void addToDB() {

        Database database = new Database();
        Connection connection = database.getConnection();
        String insertScheduleQuery = "insert into Raspored values (default,?,?,?,?)";

        try {
            PreparedStatement ps = connection.prepareStatement(insertScheduleQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1,Timestamp.valueOf(this.getStartDate().atStartOfDay()));
            if(this.endDateProperty() == null){
                ps.setNull(2,Types.TIMESTAMP);
            }else{
                ps.setTimestamp(2,Timestamp.valueOf(this.getEndDate().atStartOfDay()));
            }

            ps.setInt(3,this.getDayTemp());
            if(this.nightTempProperty() == null){
                ps.setNull(4, Types.INTEGER);
            }else{
                ps.setInt(4,this.getNightTemp());
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                this.setScheduleID(rs.getInt(1));
            }

            String insertSpaceScheduleQuery = "insert into Prostor_Raspored values (?,?)";
            ps = connection.prepareStatement(insertSpaceScheduleQuery);
            ps.setInt(1,Main.getSelectedSpace());
            ps.setInt(2,this.getScheduleID());

            ps.execute();

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFromDB() {

        Database database = new Database();
        Connection connection = database.getConnection();
        String removeScheduleQuery = "delete from Raspored where rasporedID = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(removeScheduleQuery);
            ps.setInt(1,this.getScheduleID());
            ps.execute();

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
        checks if two schedules are overlapping and returns true if so
     */
    public boolean isOverlapping(){

        boolean isOverlapping = false;
        ArrayList<Schedule> arrayListSchedules = getScheduleList();
        if(arrayListSchedules.size() == 0) return false;

        for (Schedule s :
                arrayListSchedules) {
            if(s.isIsInfinite() || this.isIsInfinite()){// no end dates, just check the start (not gonna work, needs separate checks for list item and this)

                if(s.getStartDate().isBefore(this.getStartDate())) return true;
                if(this.getStartDate().isBefore(s.getStartDate())) return true;

                //TODO infinites

            }else if(s.getStartDate().isBefore(this.getEndDate()) && s.getEndDate().isAfter(this.getStartDate())){ // true if overlap
                isOverlapping = true;
            }
        }
        return isOverlapping;
    }

}
