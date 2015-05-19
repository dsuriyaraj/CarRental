/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class RegisterCustomerController implements Initializable {

    @FXML private TextField cust_name,location,city;
    @FXML private TextField ds1,ds2,ds3;
    @FXML private ComboBox roadstar;
    @FXML private Label registration_status;
    private Integer road_star;
    final private String user= "root";
    final private String pass= "";
    
    @FXML private void ToggleRoadStar() {
        if("Yes"==roadstar.getValue())
            road_star=1;
        else
            road_star=0;
    }
    
     @FXML private void Reset() {
        road_star = 0;
        roadstar.setValue("No");
        cust_name.setText("");
        location.setText("");
        city.setText("");
        ds1.setText("");
        ds2.setText("");
        ds3.setText("");
        registration_status.setText("");
        
    }
    
    @FXML private void Submit() throws SQLException {
        
            Boolean _validlocation = true;
            Boolean _validcity = true;
            Boolean _validname = true;
            String _location = location.getText();
            
            if( (null == _location) ||  ("".equals(_location) )) 
            {registration_status.setText("Adress missing !"); _validlocation = false;}
            System.out.println("Location="+_location);
            
            String _city = city.getText();
            if((null==_city) || ("".equals(_city)))
            {registration_status.setText("City missing !"); _validcity = false; }
            
            String _name = cust_name.getText();
            if((null==_name)|| ("".equals(_name)))
            {registration_status.setText("Customer name missing !"); _validname = false; }
        
            Integer _ds1,_ds2,_ds3;
            Boolean isValidPhone = true;
            String DS1 = (String)ds1.getText();
            String DS2 = (String)ds2.getText();
            String DS3 = (String)ds3.getText();
            
            if( (3==DS1.length()) && (3==DS2.length()) && (4==DS3.length()) ) {
                try   {
                        _ds1 = Integer.parseInt((String)ds1.getText());
                        _ds2 = Integer.parseInt((String)ds2.getText());
                        _ds3 = Integer.parseInt((String)ds3.getText());

                }
                catch (NumberFormatException e) {registration_status.setText("Invalid Phone Number"); isValidPhone  = false;}
            }
            else
            { isValidPhone  = false; registration_status.setText("Invalid Phone Number");}
            
        if(isValidPhone && _validcity && _validlocation && _validname ) {
            
                   Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
                   String phone =(String)ds1.getText() +"-"+ (String)ds2.getText() +"-"+ (String)ds3.getText();
                   String Address = (String)location.getText()+", "+(String)city.getText();
                   Statement myStmt = myConn.createStatement();
                   String sql = "INSERT INTO Customer (Phone_number,Name,Address,City,Roadstar) " +
                                "VALUES ('"+phone+"','"  + (String)cust_name.getText()+ "','"+_location+"','"+ _city+"',"+road_star+")";
                   try {
                   myStmt.executeUpdate(sql);
                   registration_status.setText("Customer added !");
               } catch (SQLIntegrityConstraintViolationException | MySQLIntegrityConstraintViolationException ex) { registration_status.setText("Customer already exists ..");}  
       
        }
         
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        road_star=0;
        List  rs = new ArrayList();
              rs.add("Yes");
              rs.add("No");
        ObservableList RS = FXCollections.observableList(rs);
        roadstar.setItems(RS);
        roadstar.setValue("No");
        
              
    }    
    
}
