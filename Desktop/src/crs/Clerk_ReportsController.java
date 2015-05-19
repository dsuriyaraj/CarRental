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
public class Clerk_ReportsController implements Initializable {
    
    @FXML private Pane content_report;
    /**
     * Initializes the controller class.
     */
    
     @FXML
    private void LoadForSaleVehicles(ActionEvent event)throws IOException  {
       
        content_report.getChildren().clear();
   content_report.getChildren().add(FXMLLoader.load(getClass().getResource("Clerk_ForSaleVehicles.fxml")));
    }
    
     @FXML
    private void LoadAvailableVehicles(ActionEvent event)throws IOException  {
       
        content_report.getChildren().clear();
        content_report.getChildren().add(FXMLLoader.load(getClass().getResource("Clerk_AvailableVehicles.fxml")));
    }
    
      @FXML
    private void LoadOverdue(ActionEvent event)throws IOException  {
       
        content_report.getChildren().clear();
        content_report.getChildren().add(FXMLLoader.load(getClass().getResource("Clerk_Overdue.fxml")));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            content_report.getChildren().add(FXMLLoader.load(getClass().getResource("Clerk_AvailableVehicles.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(Clerk_ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
