package sample;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

    private static boolean debug = true;

    private Stage primaryStage;

    private static final String selectedSpaceQuery = "Select odabraniProstor from Korisnik where korisnikID = 1";

    private static ObservableList<Space> observableListSpace;
    private static ObservableList<Room> observableListRoom;
    private static ObservableList<Sensor> observableListSensor;
    private static ObservableList<Choice> observableListChoices;
    private static ObservableList<SensorInfo> observableListSensorInfo;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Temperature Controller");

        Scene mainScene = new Scene(root,800,600);


        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);


    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }


    public static ObservableList<Room> getObservableListRoom() {

        observableListRoom = FXCollections.observableArrayList(Room.getRoomList(getSelectedSpace()));
        observableListRoom.addListener((ListChangeListener<Room>) c -> {
            while(c.next()){
                if(c.wasAdded()){

                    for (Room r :
                            c.getAddedSubList()) {
                        if (r.getRoomID() == -1) {
                            r.addToDB();
                        }
                        }

                }else if(c.wasRemoved()){

                    for (Room r :
                            c.getRemoved()) {
                        r.removeFromDB();
                    }

                }
            }
        });

        return observableListRoom;

    }


    public static ObservableList<Space> getObservableListSpace (){

        observableListSpace = FXCollections.observableArrayList(Space.getSpaceList());
        observableListSpace.addListener((ListChangeListener<Space>) c -> {

            while (c.next()){

                if(c.wasAdded()){

                    for (Space s :
                            c.getAddedSubList()) {
                        if(s.getSpaceID() == -1)
                        {
                            s.addToDB();
                        }

                    }

                }else if(c.wasRemoved()){

                    for (Space s :
                            c.getRemoved()) {
                        s.removeFromDB();
                    }

                }

            }

        });

        return observableListSpace;

    }


    public static ObservableList<Sensor> getObservableListSensor() {

        observableListSensor = FXCollections.observableArrayList(Sensor.getSensorList());

        return observableListSensor;

    }


    public static ObservableList<Choice> getChoices(ObservableList<?> observableList){

        observableListChoices = FXCollections.observableArrayList();

        for (Object item :
                observableList) {
            if(item.getClass() == Sensor.class){ // is a sensor

                Sensor s = (Sensor) item;
                observableListChoices.add(new Choice(s.getSensorID(), "Senzor "+s.getSensorID()));

            }else if (item.getClass() == Room.class){ // is a room

                Room r = (Room) item;
                observableListChoices.add(new Choice(r.getRoomID(),r.getRoomName()));

            }
        }
        return observableListChoices;
    }


    public static ObservableList<SensorInfo> getObservableListSensorInfo (){

        observableListSensorInfo = FXCollections.observableArrayList(param -> new Observable[]{
                param.roomNameProperty(),
                param.roomIDProperty()
        });
        observableListSensorInfo.addAll(SensorInfo.getSensorInfoList());
        observableListSensorInfo.addListener((ListChangeListener<SensorInfo>) c -> {
            while(c.next()){
                if(c.wasAdded()){

                    for (SensorInfo s :
                            c.getAddedSubList()) {
                        s.addToDB();
                    }

                }else if(c.wasRemoved()){

                    for (SensorInfo s :
                            c.getRemoved()) {
                        s.removeFromDB();
                    }
                }
            }
        });

        return observableListSensorInfo;
    }


    public static int getSelectedSpace (){

        int selectedSpace = 0;

        Database database = new Database();
        Connection connection = database.getConnection();

        PreparedStatement preparedStatementSelectedSpace = null;
        try {
            preparedStatementSelectedSpace = connection.prepareStatement(selectedSpaceQuery);
            ResultSet resultSetSelectedSpace = preparedStatementSelectedSpace.executeQuery();
            if(!resultSetSelectedSpace.isBeforeFirst()) { // empty set

                Main.debugOutput(debug,"Empty set");
            }

            while(resultSetSelectedSpace.next()){
                selectedSpace = resultSetSelectedSpace.getInt("odabraniProstor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedSpace;
    }


    public static void debugOutput(boolean debug,String text){
        if(debug == true){
            System.out.println(text);
        }
    }



}


