package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by frang on 22-Jan-17.
 */
public class Room {



    private IntegerProperty roomID;
    private StringProperty roomName;
    private IntegerProperty spaceID;

    public Room(int roomID, String roomName, int spaceID){
        this.roomID = new SimpleIntegerProperty(roomID);
        this.roomName = new SimpleStringProperty(roomName);
        this.spaceID = new SimpleIntegerProperty(spaceID);

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

    public String getRoomName() {
        return roomName.get();
    }

    public StringProperty roomNameProperty() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName.set(roomName);
    }

    public int getSpaceID() {
        return spaceID.get();
    }

    public IntegerProperty spaceIDProperty() {
        return spaceID;
    }

    public void setSpaceID(int spaceID) {
        this.spaceID.set(spaceID);
    }


    public static ArrayList<Room> getRoomList(int selectedSpace) {

        ArrayList<Room> roomsList = new ArrayList();

        try {
            Database database = new Database();
            Connection connection = database.getConnection();
            String getRoomListQuery = "Select * from Soba where prostorId = ?";
            PreparedStatement ps = connection.prepareStatement(getRoomListQuery);
            ps.setInt(1,selectedSpace);
            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()) { //empty set

                return roomsList;

            }

            while(rs.next()){
                Room room = new Room(rs.getInt("sobaID"),
                        rs.getString("imeSobe"),
                        rs.getInt("prostorID"));
                roomsList.add(room);

            }
            database.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomsList;

    }


    public void addToDB() {

        Database database = new Database();
        Connection connection = database.getConnection();
        String insertRoomQuery = "insert into Soba values (default,?,?)";
        int newID = -1;
        try {
            PreparedStatement ps = connection.prepareStatement(insertRoomQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,this.getRoomName());
            ps.setInt(2,this.getSpaceID());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            while(rs.next()){
                newID = rs.getInt(1);
            }

            this.setRoomID(newID);

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeFromDB() {

        Database database = new Database();
        Connection connection = database.getConnection();
        String removeRoomQuery = "delete from Soba where sobaID = ?";
        String updateSensorsQuery = "update Senzor set sobaID = -1, status = 0 where sobaID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(removeRoomQuery);
            ps.setInt(1,this.getRoomID());
            ps.execute();

            ps = connection.prepareStatement(updateSensorsQuery);
            ps.setInt(1,this.getRoomID());
            ps.execute();

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
