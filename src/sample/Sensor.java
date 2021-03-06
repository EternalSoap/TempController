package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


import java.sql.*;
import java.util.ArrayList;

/**
 * Created by frang on 23-Jan-17.
 */
public class Sensor {

    private IntegerProperty sensorID;
    private IntegerProperty roomID;
    private IntegerProperty status;

    public Sensor(int sensorID, int roomID, int status){
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

    public int getRoomID() {
        return roomID.get();
    }

    public IntegerProperty roomIDProperty() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID.set(roomID);
    }

    public int getStatus() {
        return status.get();
    }

    public IntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status.set(status);
    }

    public static ArrayList<Sensor> getSensorList(){

        ArrayList<Sensor> sensorArrayList = new ArrayList<>();
        Database database = new Database();
        Connection connection = database.getConnection();
        // query to get all the sensors for the selected space and unregistered sensors
        String getSensorListQuery = "select * from Senzor where Senzor.sobaID in (select Soba.sobaID from Soba,Korisnik where Soba.prostorID = Korisnik.odabraniProstor) or Senzor.sobaID = -1";

        try {
            PreparedStatement ps = connection.prepareStatement(getSensorListQuery);

            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()) { // empty set

            }
            while (rs.next()){
                sensorArrayList.add(new Sensor(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3)
                ));
            }

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sensorArrayList;

    }

    public void addToDB(){

        // not needed cause sensors get added automatically on the server side

    }

    /*
        unregister the sensor
     */
    public static void removeFromDB(){

        // not needed, everything is done in SensorInfo

    }


}
