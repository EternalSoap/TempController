package sample;

import javafx.beans.property.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by frang on 24-Jan-17.
 */
public class SensorInfo {

    IntegerProperty sensorID;
    StringProperty roomName;
    IntegerProperty status;
    IntegerProperty roomID;

    public SensorInfo (int sensorID, String roomName, int status,Integer roomID){
        this.sensorID = new SimpleIntegerProperty(sensorID);
        this.roomName = new SimpleStringProperty(roomName);
        this.status = new SimpleIntegerProperty(status);
        if(roomID != null){
            this.roomID = new SimpleIntegerProperty(roomID);
        }else{
            this.roomID = new SimpleIntegerProperty(-1);
        }
    }

    public SensorInfo (int sensorID, Integer roomID, int status){
        this.sensorID = new SimpleIntegerProperty(sensorID);
        this.roomID = new SimpleIntegerProperty(roomID);
        this.status = new SimpleIntegerProperty(status);
    }

    public int getSensorID() {
        return sensorID.get();
    }

    public IntegerProperty sensorIDProperty() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID.set(sensorID);
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

    public int getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {

        switch (this.status.getValue()){
            case 1: return new SimpleStringProperty("Dodan u sobu");
            case 0: return new SimpleStringProperty("Nije dodan u sobu");
            default : return new SimpleStringProperty("Nepoznat");
        }
    }

    public void setStatus(int status) {
        this.status.set(status);
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

    public static ArrayList<SensorInfo> getSensorInfoList() {

        ArrayList<SensorInfo> arrayListSensorInfo = new ArrayList<>();
        Database database = new Database();
        Connection connection = database.getConnection();
        String getSensorInfoListQuery = "select Senzor.senzorID,Soba.imeSobe,Senzor.status,Soba.sobaID from Soba join Korisnik on Soba.prostorID = Korisnik.odabraniProstor join Senzor on Senzor.sobaID = Soba.sobaID";
        String getUnregisteredSensorInfoQuery = "select senzorID from Senzor where sobaID = -1";

        try {
            PreparedStatement ps = connection.prepareStatement(getSensorInfoListQuery);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                arrayListSensorInfo.add(new SensorInfo(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4)
                ));
            }

            ps = connection.prepareStatement(getUnregisteredSensorInfoQuery);
            rs = ps.executeQuery();

            while(rs.next()){
                arrayListSensorInfo.add(new SensorInfo(
                        rs.getInt(1),
                        "Nije dodan u sobu",
                        0,
                        -1
                ));
            }
                database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayListSensorInfo;
    }

    public void addToDB() {

        Database database = new Database();
        Connection connection = database.getConnection();
        String getRoomNameQuery = "select imeSobe from Soba where sobaID = ?";
        String updateSensorQuery = "update Senzor set sobaID = ? where senzorID = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(getRoomNameQuery);
            ps.setInt(1,this.getRoomID());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                this.setRoomName(rs.getString(1));
            }

            ps = connection.prepareStatement(updateSensorQuery);
            ps.setInt(1,this.getRoomID());
            ps.setInt(2,this.getSensorID());

            ps.execute();

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeFromDB() {

        Database database = new Database();
        Connection connection = database.getConnection();
        String unregisterSensorQuery = "update Senzor set sobaID = -1, status = 0 where senzorID = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(unregisterSensorQuery);
            ps.setInt(1,this.getSensorID());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
