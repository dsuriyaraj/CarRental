/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

/**
 *
 * @author User
 */
public class LoginController implements Initializable {
    
    @FXML
    private Label fail_login_message,Clerk,Manager,Admin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    final private String user= "root";
    final private String pass= "";
    private Integer Login_user; //1 = clerk 2=manager 3=Amin
    
    @FXML
    private void goTomenu(ActionEvent event)throws IOException  {
       
        if(ValidLogin()) // Commented for testing
        {
            switch(Login_user) {
        case 2:
            change_scene("menu_manager.fxml",event);
        break;
        case 3:
            change_scene("menu_admin.fxml",event);
        break;
        default:
            change_scene("menu_clerk.fxml",event);
    }
        
        }
        else
          fail_login_message.setText("Login Failed,try again ..");
    }
    
    @FXML
    public void Login_clerk() {
        Login_user = 1;
        Clerk.setVisible(true);
        Manager.setVisible(false);
        Admin.setVisible(false);
    }
    
    @FXML
    public void Login_manager() {
        Login_user= 2;
        Clerk.setVisible(false);
        Manager.setVisible(true);
        Admin.setVisible(false);
    }
    
      @FXML
    public void Login_admin() {
        Login_user = 3;
        Clerk.setVisible(false);
        Manager.setVisible(false);
        Admin.setVisible(true);
    }
    
    
    
    @FXML
    private void goToScreen3(ActionEvent event) {
        System.out.println("You clicked me!");
         
    }
    public boolean ValidLogin() {
        String Type=null;
        switch(Login_user) {
        case 2:
            Type = "Manager";
        break;
        case 3:
            Type = "Admin";
        break;
        default:
            Type="Clerk";
    }
        boolean valid = true;
        try {
         
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from Employees where Username='"+  username.getText()+"' and Password='"+password.getText()+"' and Type='"+Type +"'");
            int count = 0;
            while (myRs.next()) { count++; }
            if(count==0) valid = false;
            if (myConn != null) myConn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }  
        return valid;
    }
    public void change_scene(String fxml,ActionEvent event) throws IOException {
      
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource(fxml));
       loader.load();
       Parent p = loader.getRoot();
       Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
       stage.setScene(new Scene(p));
       stage.show();    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Login_user = 1;
        Clerk.setVisible(false);
        Manager.setVisible(false);
        Admin.setVisible(false);
    }    
    
}
