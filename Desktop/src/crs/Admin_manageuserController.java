/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import static crs.CRS.getUserList;
import static crs.CRS.removeUser;
import static crs.CRS.updateUser;
import crs.classes.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Admin_manageuserController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        getList(); 
        
    }    
    
    @FXML
    private TextField newpassword;
    @FXML
    private TableView table_user;
    @FXML
    private Label result1;
    @FXML
    private Label result2;
    @FXML
    private Button removeButton;
    @FXML
    private Button resetButton;
    @FXML
    private TextField keyword;
    
    
    private int UserId=-1;
    
    
    @FXML
    private void onsearch(ActionEvent event) {
        if(keyword.getText().isEmpty()){
            getList();
        }
        String keywords = keyword.getText().toString();
        ArrayList<crs.classes.User> list = new  ArrayList<crs.classes.User>();
        list = CRS.getUserListByKeyword(keywords);
        setTable(list ,table_user);
        
    }
    @FXML
    private void onSetpw(ActionEvent event) { 
   
        result1.setVisible(false);
        result2.setVisible(false); 
        if( UserId<=0   ){  
            result1.setVisible(true);
            result1.setText("Please select one user.");
            return;
        }
        if(   newpassword.getText().isEmpty()  ){  
            result1.setVisible(true);            
            result1.setText("Please enter new password.");
            return;
        }
        User user = new User();
        user.password = newpassword.getText();
        user.emp_id = UserId;
        if(updateUser(user)==1){ 
            result1.setText("success");
            result1.setVisible(true);
        }else{ 
            result1.setText("fail");
            result1.setVisible(true);
        }
        
        
        
    }
    @FXML
    private void onRemove(ActionEvent event) {
        result1.setVisible(false);
        result2.setVisible(false); 
        if(UserId<=0){
        result2.setText("Please select one user.");
            result2.setVisible(true);
            return; 
        }       
        
        popDialog("Are you sure to remove user?");
        
      
        
    }
    
    private void removeSelectedUser(){
        if( removeUser(UserId)==1 ){ 
            result2.setVisible(true);
            result2.setText("success");
            getList();
        }
         
    
    }
    
    private void getList(){    
    
        ArrayList<crs.classes.User> list = new  ArrayList<crs.classes.User>();
        list = getUserList();
        setTable(list ,table_user);
    }
    
     private void setTable(ArrayList<crs.classes.User> list,TableView  table){
        
        TableColumn<crs.classes.User,String> enameCol = new TableColumn<>("employee name");
        enameCol.setMinWidth(132);
        TableColumn<crs.classes.User,String> usernameCol = new TableColumn<>("user name");
        usernameCol.setMinWidth(120); 
        TableColumn<crs.classes.User,String> positionCol = new TableColumn<>("position");
        positionCol.setMinWidth(100);  
        
        table.getColumns().clear();        
        table.getColumns().addAll(enameCol,usernameCol,positionCol );
        
       if(list.size()==0||list==null){
        return;
       }
       
       
        ObservableList<crs.classes.User> myData = FXCollections.observableArrayList();
        for(int i=0; i<list.size();i++){
            myData.add(list.get(i));
        }
   
        enameCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.User, String>("emp_name"));
        usernameCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.User, String>("username"));
        positionCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.User, String>("type")); 
        

        table.setItems(myData);
        table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<crs.classes.User>() {
            public void onChanged(ListChangeListener.Change<? extends crs.classes.User> c) {

                for (crs.classes.User p : c.getList()) {
                    UserId =  p.emp_id;
                }

            }
        });
 
    }
     /**********start of dialogStage**************/
    
     public int dialogFlag_sell=-1;
    
 
    
  
    @FXML
    private Pane dialogpane;
    @FXML
    private Button okbutton;
    @FXML
    private Button cancelbutton;
    
    
    private void popDialog(String text){
        
         
        Stage dialogStage = new Stage();
        
        okbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            dialogFlag_sell = 1;
            removeSelectedUser();
            dialogStage.close();
            
        }
        });
        
        cancelbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            dialogFlag_sell = 0;
            dialogStage.close();
        }
        });

        
        dialogStage.initModality(Modality.WINDOW_MODAL);
     
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(dialogpane).build()));
        dialogStage.show();
        
        
    }
 
   
    /**********end of dialogStage**************/
}
