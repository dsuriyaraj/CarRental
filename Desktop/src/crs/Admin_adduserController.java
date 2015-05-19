/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import static crs.CRS.addUser;
import static crs.CRS.checkUserName;
import crs.classes.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Ralph
 */



public class Admin_adduserController implements Initializable {
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setCombo(combo_type );
        result_label.setVisible(false); 
         result_label.setText("");
    }    
    
    
    
    @FXML
    private TextField empname;
    @FXML
    private TextField username;
    @FXML
    private TextField password;    
    @FXML
    private ComboBox combo_type;
    @FXML
    private Label result_label;
    @FXML
    private Button saveButton;
    
    
    private void setCombo(ComboBox cmb){
     result_label.setText("");
      combo_type.getItems().clear();
        ArrayList<String> list = new ArrayList<String>();
        ObservableList<String> options =  FXCollections.observableArrayList();        
            options.add( "Clerk" );           
            options.add( "Manager" );      
            options.add( "Admin" );
        combo_type.setItems(options);
    }
    
    @FXML
    private void onAdd(ActionEvent event) { 
        result_label.setText("");
        result_label.setVisible(false);
        if( getField()==false ){
            return;
        } 
        result_label.setVisible(false);
        User user = new User();
        user.emp_name = empname.getText();
        user.username = username.getText();
        user.password = password.getText();       
        user.type = combo_type.getValue().toString();
        if( checkUserName(user.username)==-1){
            result_label.setText("");
            setResult("username exists");  
            return;
        }else if( addUser(user)==1){
             result_label.setText("");
            setResult("success");  
            clearFied();
        }else{
             result_label.setText("");
            setResult("fail,please try again later.");  
        
        }
        
        
        
    }
    
    private boolean getField(){
        if( empname.getText().isEmpty() ){  
             result_label.setText("");
            setResult("empname not valid"); 
            return false;        
        }
        if( username.getText().isEmpty()   ){
             result_label.setText("");
            setResult("username not valid"); 
            return false; 
        }
        if(password.getText().isEmpty()  ){
             result_label.setText("");
            setResult("password not valid"); 
            return false; 
        
        }
        if( combo_type.getValue()==null  ){
             result_label.setText("");
            setResult("type not valid");
            return false; 
        
        
        }
           
        return true;
    }
    
    private void  clearFied(){
         
        empname.setText("");
        username.setText("");
        password.setText("");
    }
    
    private void setResult(String text){
         //result_label.setText("");
        result_label.setText(text);      
        result_label.setVisible(true);
        
    
    }
    
    
    
    
}
