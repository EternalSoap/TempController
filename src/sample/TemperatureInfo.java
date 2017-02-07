package sample;

import javafx.beans.property.*;
import javafx.scene.control.Label;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by frang on 04-Feb-17.
 */
public class TemperatureInfo {

    private static final int night = 0;
    private static final int morning = 6;
    private static final int defaultTemp = 20;

    private IntegerProperty roomID;
    private StringProperty roomName;
    private DoubleProperty roomTemp;



    public TemperatureInfo(){

        this.roomID = new SimpleIntegerProperty();
        this.roomName = new SimpleStringProperty();
        this.roomTemp = new SimpleDoubleProperty();

    }

    public String getRoomName() {
        return roomName.get();
    }

    public StringProperty roomNameProperty() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName.set(roomName);
    }

    public double getRoomTemp() {
        return roomTemp.get();
    }

    public DoubleProperty roomTempProperty() {
        return roomTemp;
    }

    public void setRoomTemp(double roomTemp) {
        this.roomTemp.set(roomTemp);
    }

    public int getRoomID() {
        return roomID.get();
    }

    public IntegerProperty roomIDProperty() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID.set(roomID);
    }

    public static ArrayList<TemperatureInfo> getTemperatureInfoList(){

        ArrayList<TemperatureInfo> arrayListTempInfo = new ArrayList<>();
        Database database = new Database();
        Connection connection = database.getConnection();
        String getRoomsFromSpaceQuery = "select DISTINCT Soba.sobaID, Soba.imeSobe from Soba join Mjerenje on Soba.sobaID = Mjerenje.sobaID,Korisnik where Korisnik.odabraniProstor =Soba.prostorID";
        String getTempForRoomQuery = "select temperatura from Mjerenje where Mjerenje.sobaID = ? order by Mjerenje.mjerenjeID desc limit 1";

        try {
            PreparedStatement ps = connection.prepareStatement(getRoomsFromSpaceQuery);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                TemperatureInfo temperatureInfo = new TemperatureInfo();
                temperatureInfo.setRoomID(rs.getInt(1));
                temperatureInfo.setRoomName(rs.getString(2));

                PreparedStatement getTemp = connection.prepareStatement(getTempForRoomQuery);
                getTemp.setInt(1,temperatureInfo.getRoomID());

                ResultSet resultSetTemp = getTemp.executeQuery();
                while(resultSetTemp.next()){
                    temperatureInfo.setRoomTemp(resultSetTemp.getDouble(1));
                }
                arrayListTempInfo.add(temperatureInfo);

            }



            database.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayListTempInfo;
    }

    public static int getCurrentDesiredTemp(int selectedSpace) {

        return (getManualTemp(selectedSpace)!=0?getManualTemp(selectedSpace):(getValidScheduleTemps(selectedSpace)!=0?getValidScheduleTemps(selectedSpace):defaultTemp));

    }

    /*
       gets the manually set desired temp (in case it's set)
    */
    private static int getManualTemp(int spaceID){
        int desiredTemp = 0;

        Database database = new Database();
        Connection connection = database.getConnection();
        String getManualTempQuery = "select Prostor.rucnaTemperatura from Prostor where Prostor.prostorID = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(getManualTempQuery);
            ps.setInt(1,spaceID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                desiredTemp = rs.getInt(1);
            }

            database.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return desiredTemp;
    }

    /*
        gets the current desired temp (depending on time) from a valid schedule for this space
     */
    private static int getValidScheduleTemps (int spaceID){
        int desiredTemp =0;

        Database database = new Database();
        Connection connection = database.getConnection();
        String getValidScheduleTempsQuery = "select Raspored.tempDan,Raspored.tempNoc from Raspored join Prostor_Raspored on Raspored.rasporedID = Prostor_Raspored.rasporedID join Prostor on Prostor.prostorID = Prostor_Raspored.prostorID where Prostor.prostorID = ? and (Raspored.datumOd <= ? and (Raspored.datumDo > ? or Raspored.datumDo IS NULL ))";


        try {
            PreparedStatement ps = connection.prepareStatement(getValidScheduleTempsQuery);

            ps.setInt(1,spaceID);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            ps.setTimestamp(3,Timestamp.valueOf(LocalDate.now().atStartOfDay()));

            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()){ // empty set, return 0 and use the default temperature (20)
                return 0;
            }

            while(rs.next()){
                if(!isNight(LocalDateTime.now())){ // get day temps from schedule

                    desiredTemp = rs.getInt(1);

                }else{ // try to get night temp, if it's not set (sql returns 0) use day temp instead

                    desiredTemp = (rs.getInt(2)!=0?rs.getInt(2):rs.getInt(1));

                }
            }

            database.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return desiredTemp;
    }

    private static boolean isNight (LocalDateTime localDateTime){

        // night heating begins at 00.00, morning begins at 6am as defined in the "night" and "morning" constants

        if(localDateTime.getHour() >= night && localDateTime.getHour() <= morning){
            return true;
        }
        return false;

    }


    public static void setManualTemp(Choice selectedManualTemp) {

        Database database = new Database();
        Connection connection = database.getConnection();
        String setManualTempQuery = "update Prostor set rucnaTemperatura = ? where prostorID = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(setManualTempQuery);

            if(selectedManualTemp.id == 0){ // we should set null

                ps.setNull(1, Types.INTEGER);
                ps.setInt(2,Main.getSelectedSpace());
                ps.execute();

            }else{
                ps.setInt(1,selectedManualTemp.id);
                ps.setInt(2,Main.getSelectedSpace());
                ps.execute();
            }

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
