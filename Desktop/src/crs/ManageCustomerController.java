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
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ManageCustomerController implements Initializable {
    @FXML
    private Pane contentpane_managecustomer;

    /**
     * Initializes the controller class.
     */
    @FXML
    private void LoadRegisterCustomer() throws IOException {    
        contentpane_managecustomer.getChildren().clear();
        contentpane_managecustomer.getChildren().add(FXMLLoader.load(getClass().getResource("RegisterCustomer.fxml")));    
    }
    
    @FXML
    private void LoadClubMember() throws IOException {    
        contentpane_managecustomer.getChildren().clear();
        contentpane_managecustomer.getChildren().add(FXMLLoader.load(getClass().getResource("ClubMember.fxml")));    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
    
            contentpane_managecustomer.getChildren().add(FXMLLoader.load(getClass().getResource("RegisterCustomer.fxml")));
        } catch (IOException ex) {
            Logger.getLogger(ManageCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }    
    
}
