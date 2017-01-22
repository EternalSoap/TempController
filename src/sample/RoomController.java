package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by frang on 21-Jan-17.
 */
public class RoomController {

    private Connection connection;

    private static final String getRoomQuery = "Select * from Soba where prostorId = ?";

    @FXML
    private TableView tableViewRoom = new TableView();
    @FXML
    private Button btnRoomAdd = new Button();
    @FXML
    private Button btnRoomDelete = new Button();
    @FXML
    private TextField txtRoomName = new TextField();



    public RoomController(){
        Database database = new Database();
        this.connection = database.getConnection();
    }

    @FXML
    private void initialize(){
        //get data from the db
        try {
            PreparedStatement preparedStatementRooms = connection.prepareStatement(getRoomQuery);
            preparedStatementRooms.setInt(1,1);



        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
