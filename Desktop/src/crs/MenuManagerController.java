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
 * @author Bhupinder
 */
public class MenuManagerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private Pane content_pane,menu_clerk,menu_manager;
    
     @FXML
    private void LoadManageVehicle(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Vehicle.fxml")));
    }
    
      @FXML
    private void LoadRate(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Setrate.fxml")));
    }
    
        @FXML
    private void LoadReports(ActionEvent event)throws IOException  {
       
        content_pane.getChildren().clear();
   content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Report.fxml")));
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            content_pane.getChildren().add(FXMLLoader.load(getClass().getResource("Manager_Vehicle.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(MenuClerkController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
