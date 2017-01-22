package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


    public static ObservableList<Room> getRoomList(int selectedSpace) {

        ObservableList<Room> roomsList = FXCollections.observableArrayList();

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


}
