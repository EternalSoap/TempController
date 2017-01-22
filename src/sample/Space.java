package sample;

import javafx.beans.property.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by frang on 22-Jan-17.
 */
public class Space {

    /* Model class for a space the system is used in (ex. a house)*/


    private final IntegerProperty spaceID;
    private final StringProperty spaceName;
    private final BooleanProperty heatingOn;
    private final IntegerProperty userID;

    public Space(){
        this(0,null,null,0);
    }

    public Space(int spaceID, String spaceName, Boolean heatingOn, int userID) {

        this.spaceID = new SimpleIntegerProperty(spaceID);
        this.spaceName = new SimpleStringProperty(spaceName);
        this.heatingOn = new SimpleBooleanProperty(heatingOn);
        this.userID = new SimpleIntegerProperty(userID);

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

    public String getSpaceName() {
        return spaceName.get();
    }

    public StringProperty spaceNameProperty() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName.set(spaceName);
    }

    public boolean isHeatingOn() {
        return heatingOn.get();
    }

    public BooleanProperty heatingOnProperty() {
        return heatingOn;
    }

    public void setHeatingOn(boolean heatingOn) {
        this.heatingOn.set(heatingOn);
    }

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public void addToDB(){

        Database database = new Database();
        Connection connection = database.getConnection();
        String insertSpaceQuery = "Insert into Prostor values (default,?,?,1)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSpaceQuery);
            preparedStatement.setString(1,this.getSpaceName());
            preparedStatement.setInt(2,(this.isHeatingOn()==true?1:0));
            preparedStatement.execute();

            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void removeFromDB() {

        Database database = new Database();
        Connection connection = database.getConnection();
        String deleteSpaceQuery = "delete from Prostor where prostorID = ?";


        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(deleteSpaceQuery);
            preparedStatement.setInt(1,this.getSpaceID());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
