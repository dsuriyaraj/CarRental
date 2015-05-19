/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;


import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.lang.Exception;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ClubMemberController implements Initializable {

    @FXML private TextField cust_name_m;
    @FXML private TextField location_m,city_m;
    @FXML private Label membership_status;
    @FXML DatePicker date_membership;
    final private String user= "root";
    final private String pass= "";
    
    @FXML private void Reset() {
        
        cust_name_m.setText("");
        location_m.setText("");
        city_m.setText("");
        membership_status.setText("");
        date_membership.setValue(null);
    }
    
    @FXML private void Submit() throws SQLException {
        
        
            String Address = (String)location_m.getText()+", "+(String)city_m.getText(); 
         
            if((null==date_membership.getValue()) || (null==cust_name_m.getText()) || (null==location_m.getText()) || (null==city_m.getText()) ) {
                membership_status.setText("All Fields required !");
            }
            else {
         
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            membership_status.setText(" ");
            Statement myStmt = myConn.createStatement();
            String sql = "SELECT * from Customer where Name='"+(String)cust_name_m.getText()+"' and Address='"+location_m.getText()+"' and City='"+city_m.getText()+"'";
            ResultSet myRs = myStmt.executeQuery(sql);
            int count = 0;
            while (myRs.next()) { count++; }
            if(count==0) {membership_status.setText("Please verify info");}
            else {
                myRs.previous();
                String Phone = myRs.getString("Phone_number");
                sql = "INSERT INTO ClubMember (Phone_number,Mem_date) " +
                         "VALUES ('"+Phone+"','"+date_membership.getValue()+"')";
                
                Boolean isOk= true;
                try {
                myStmt.executeUpdate(sql);
                membership_status.setText("Membership done !");
                } 
                catch (SQLIntegrityConstraintViolationException | MySQLIntegrityConstraintViolationException ex) 
                { isOk=false; membership_status.setText("Customer already a member ..");}  
                
                if(isOk) {
                    sql = "UPDATE Customer SET Clubmember=1 WHERE Phone_number='"+Phone+"'";
                    myStmt.executeUpdate(sql);
                }
            
            
            
            }
                if (myConn != null) myConn.close();
         
            }
         
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
