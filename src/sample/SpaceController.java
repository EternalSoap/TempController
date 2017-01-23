package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by frang on 21-Jan-17.
 */
public class SpaceController {

    private boolean debug = true;

    private Connection connection;



    @FXML
    private TableView<Space> tableViewSpace = new TableView();
    @FXML
    private TableColumn<Space,String> tableColumnSpaceName = new TableColumn<>();
    @FXML
    private TextField txtSpaceName = new TextField();
    @FXML
    private Button btnSpaceAdd = new Button();
    @FXML
    private Button btnSpaceDelete = new Button();
    @FXML
    private Button btnSpaceSelect = new Button();

    private ObservableList<Space> spacesList = FXCollections.observableArrayList();


    public SpaceController(){



    }

    private void updateItem(ListChangeListener.Change<? extends Space> c) {
        //not sure if i need this tbh
    }

    private void removeItem(ListChangeListener.Change<? extends Space> c) {

        for(Space s : c.getRemoved()){
            Main.debugOutput(debug, s.getSpaceName());

            s.removeFromDB();

        }

    }

    private void addItem(ListChangeListener.Change<? extends Space> c) {

        for(Space s : c.getAddedSubList()){
            Main.debugOutput(debug, s.getSpaceName());
            if(s.getSpaceID()==-1   ) {
                s.addToDB();
            }

        }

    }

    @FXML
    private void initialize(){
        spacesList.addListener(new ListChangeListener<Space>() {
            @Override
            public void onChanged(Change<? extends Space> c) {
                while(c.next()){
                    if(c.wasUpdated()){
                        // update c in database
                        updateItem(c);

                    }else if(c.wasAdded()){
                        // create new item in database

                        addItem(c);

                    }else if(c.wasRemoved()){
                        // delete item from database
                        removeItem(c);
                    }

                }
            }
        });

        tableColumnSpaceName.setCellValueFactory(cellData -> cellData.getValue().spaceNameProperty());

        spacesList = Space.getSpaceList();
        tableViewSpace.setItems(spacesList);

    }

    @FXML
    private void onSelectButtonClicked(){

        Space space = tableViewSpace.getSelectionModel().getSelectedItem();
        if(space !=null) {
            Database database = new Database();
            Connection connection = database.getConnection();
            String updateSelectedSpaceQuery = "update Korisnik set odabraniProstor = ? where korisnikID = 1";
            try {
                PreparedStatement ps = connection.prepareStatement(updateSelectedSpaceQuery);
                ps.setInt(1,space.getSpaceID());
                ps.execute();
                database.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    private void onAddButtonClicked(){

        String spaceName = txtSpaceName.getText();
        if(spaceName != null){
            Space space = new Space(-1,spaceName,false,1);
            spacesList.add(space);
        }

    }

    @FXML
    private void onRemoveButtonClicked(){

        Space space = tableViewSpace.getSelectionModel().getSelectedItem();
        if(space !=null){
            spacesList.remove(space);
        }

    }


    public void reinitialize() {

        initialize();

    }
}
