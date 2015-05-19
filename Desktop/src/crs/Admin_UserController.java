/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Admin_UserController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        gotoPane(main_pane_manage,"Admin_manageuser.fxml");
         
         
    }   
    @FXML
    private Pane main_pane;
    @FXML
    private Pane main_pane_add;
    @FXML
    private Pane main_pane_manage;
    
    
    @FXML
    private void onAdd(ActionEvent event)  {
        gotoPane(main_pane_add,"Admin_adduser.fxml");
       // gotoPane(main_pane_manage,"Admin_manageuser.fxml");
    }
    @FXML
    private void onManage(ActionEvent event)  {
        gotoPane(main_pane_manage,"Admin_manageuser.fxml");
    } 
    
  
    

    
    private void gotoPane(Pane gotoPane,String fxml){
        try {
           
           main_pane.getChildren().remove(main_pane_manage);  
           main_pane.getChildren().remove(main_pane_add);          
           
           gotoPane.getChildren().add( FXMLLoader.load( getClass().getResource(fxml) )   );    
           gotoPane.setLayoutX(0);
           gotoPane.setLayoutY(0);     
           main_pane.getChildren().add( gotoPane );  
           
        } catch (IOException ex) {
            Logger.getLogger(Admin_UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
       
    
    
}
