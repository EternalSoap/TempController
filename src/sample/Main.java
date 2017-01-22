package sample;

import javafx.application.Application;
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

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Temperature Controller");

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
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


