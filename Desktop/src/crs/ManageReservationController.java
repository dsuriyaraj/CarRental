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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ManageReservationController implements Initializable {

    
    @FXML private Pane content_managereservation;
    
    @FXML
    public void CancelReservation() throws IOException {
        content_managereservation.getChildren().clear();
        content_managereservation.getChildren().add(FXMLLoader.load(getClass().getResource("CancelReservation.fxml")));     
    }
    
    @FXML
    public void RentViaReservation() throws IOException {
        content_managereservation.getChildren().clear();
        content_managereservation.getChildren().add(FXMLLoader.load(getClass().getResource("RentViaReservation.fxml")));     
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        try { content_managereservation.getChildren().add(FXMLLoader.load(getClass().getResource("RentViaReservation.fxml")));} 
        catch (IOException ex) { Logger.getLogger(ManageCustomerController.class.getName()).log(Level.SEVERE, null, ex);}
    }    
    
}
