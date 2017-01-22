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

    private int selectedSpace = 0;
    private Connection connection;
    private static final String selectedSpaceQuery = "Select odabraniProstor from Korisnik where korisnikID = 1";
    private static final String spacesListQuery = "Select * from Prostor where korisnikID = 1";

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

    //TODO add change listeners to observable lists (preferably a class that handles sql stuff)
    private ObservableList<Space> spacesList = FXCollections.observableArrayList();

    public SpaceController(){

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
            if(s.getSpaceID()==0) {
                s.addToDB();
            }

        }

    }

    @FXML
    private void initialize(){
        Database database = new Database();
        this.connection = database.getConnection();
        tableColumnSpaceName.setCellValueFactory(cellData -> cellData.getValue().spaceNameProperty());

        try {
            PreparedStatement preparedStatementSelectedSpace = connection.prepareStatement(selectedSpaceQuery);

            ResultSet resultSetSelectedSpace = preparedStatementSelectedSpace.executeQuery();


            if(!resultSetSelectedSpace.isBeforeFirst()) { // empty set

                Main.debugOutput(debug,"Empty set");
            }

            while(resultSetSelectedSpace.next()){
                this.selectedSpace = resultSetSelectedSpace.getInt("odabraniProstor");
            }

            PreparedStatement preparedStatementSpacesList = connection.prepareStatement(spacesListQuery);
            ResultSet resultSetSpacesList = preparedStatementSpacesList.executeQuery();




            if(!resultSetSpacesList.isBeforeFirst()){ // empty set
                //not sure if this is a problem
                //return;

            }

            while(resultSetSpacesList.next()){
                Space space = new Space(resultSetSpacesList.getInt("prostorID"),
                        resultSetSpacesList.getString("imeProstora"),
                        (resultSetSpacesList.getInt("grijanjeUpaljeno") == 0?false:true),
                        resultSetSpacesList.getInt("korisnikID"));

                spacesList.add(space);

            }

            tableViewSpace.setItems(spacesList);



            database.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void onSelectButtonClicked(){

        Space space = tableViewSpace.getSelectionModel().getSelectedItem();
        if(space !=null) {
            this.selectedSpace = space.getSpaceID();
            Main.debugOutput(debug, "" + selectedSpace);
        }
    }

    @FXML
    private void onAddButtonClicked(){

        String spaceName = txtSpaceName.getText();
        if(spaceName != null){
            Space space = new Space(0,spaceName,false,1);
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



}
