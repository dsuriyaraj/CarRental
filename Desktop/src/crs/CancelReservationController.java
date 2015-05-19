/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class CancelReservationController implements Initializable {

   @FXML private ComboBox choice_cancel;
   @FXML private Pane content_cancelreservation;
   @FXML private TextField confirmationnumber,ds1,ds2,ds3;
   @FXML private Button button;
   @FXML private Label message;
   @FXML private DatePicker from,to;
   private Integer choice;
   final private String user= "root";
   final private String pass= "";
    
    
   @FXML 
   public void Toggle() {
       if("Phone & Dates"==choice_cancel.getValue()) {
            content_cancelreservation.setTranslateY(-35); 
            content_cancelreservation.setVisible(true);
            confirmationnumber.setVisible(false);
            button.setTranslateY(134); 
            choice = 1;
        }
       else { 
           button.setTranslateY(0);
           confirmationnumber.setVisible(true);
           content_cancelreservation.setTranslateY(0); 
           content_cancelreservation.setVisible(false);
           choice = 0;
       }
   }
   
   
   @FXML public void DeleteReservation() throws SQLException {
       
       Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
       Statement myStmt = myConn.createStatement();
       
       if(choice==1) {
            
            Integer _ds1,_ds2,_ds3;
            Boolean isvalid_phone = true;
            try   {
                    _ds1 = Integer.parseInt((String)ds1.getText());
                    _ds2 = Integer.parseInt((String)ds2.getText());
                    _ds3 = Integer.parseInt((String)ds3.getText());
                    message.setText(" ");
            }
            catch (NumberFormatException e) {message.setText("Invalid Phone Number"); isvalid_phone = false;}
            
            if(  (null!=from.getValue()) && (null!=to.getValue())   ) {
                if(isvalid_phone) { 

                    String customer_phone = ds1.getText()+"-"+ds2.getText()+"-"+ds3.getText();  
                    String sql = "DELETE FROM Reservation WHERE Phone_number='"+customer_phone+"' and DATE(Pickup_time)='"+from.getValue() +"' and DATE(Dropoff_time)='"+to.getValue() +"'";
                    System.out.println(sql);
                    
                    try {
                    int row = myStmt.executeUpdate(sql);
                    if(row ==1)
                        message.setText("Reservation cancelled");
                    else
                        message.setText("No record to delete");
                    } catch (SQLIntegrityConstraintViolationException | MySQLIntegrityConstraintViolationException ex) { 
                        message.setText("RentalAgreemnt exists,can't cancel");
                        
                    } 
                    
                    
                    
                }
            } else {message.setText("From or To Datetime missing");}
       }
       else {
            Integer confirmation=0;
            Boolean isvalid_confirmation = true;
            try   {
                    confirmation = Integer.parseInt((String)confirmationnumber.getText());
                    message.setText(" ");
            }
            catch (NumberFormatException e) {message.setText("Invalid Confirmation number"); isvalid_confirmation = false;}
           
            if(isvalid_confirmation)  {
                
                String sql = "DELETE FROM Reservation WHERE Confno='"+confirmation+"'";
                try {
                    int row = myStmt.executeUpdate(sql);
                    if(row ==1)
                        message.setText("Reservation cancelled");
                    else
                        message.setText("No record to delete");
                    } catch (SQLIntegrityConstraintViolationException | MySQLIntegrityConstraintViolationException ex) {message.setText("RentalAgreemnt exists,can't cancel");
                      } 
                
                
            }
            
            
           
       }
       
       if (myConn != null) myConn.close();
       
   }
   
   
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choice = 0;
        content_cancelreservation.setVisible(false);
        List choice = new ArrayList();
              choice.add("Phone & Dates");
              choice.add("Confirmation");
          
         ObservableList cancel = FXCollections.observableList(choice);
         choice_cancel.setItems(cancel);
         choice_cancel.setValue("Confirmation");
        
    }    
    
}
