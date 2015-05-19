/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ListReservationsController implements Initializable {
    @FXML private TableView<ReservationCustomer> table_reservationcustomer;
    @FXML private TableColumn<ReservationCustomer,Integer> Confno;
    @FXML private TableColumn<ReservationCustomer,Integer> Vlicense;
    @FXML private TableColumn<ReservationCustomer,Integer> Branchid;
    @FXML private TableColumn<ReservationCustomer,String> Vtypename;
    @FXML private TableColumn<ReservationCustomer,String> Pickup;
    @FXML private TableColumn<ReservationCustomer,String> Dropoff;
    @FXML private Label label;
    private Integer ConfNo;
    
        final private String user= "root";
        final private String pass= "";
        
        public Integer ConfNo() {
            return ConfNo;
        }
        
        @FXML
        public void setInfo(String Info) {
          
        List<ReservationCustomer> list = new ArrayList<>();
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            System.out.println("Select * from Reservation where Phone_number='"+Info+"'");
            ResultSet myRs =  myStmt.executeQuery("Select * from Reservation where Phone_number='"+Info+"'" );
            while (myRs.next()) { System.out.println("ConfoBo:"+myRs.getInt("Confno")+" Vlicense:"+myRs.getInt("Vlicense"));
                list.add(  new ReservationCustomer (myRs.getInt("Confno"),myRs.getInt("Vlicense"),myRs.getInt("BranchID"),myRs.getString("Vtype_name"),myRs.getString("Pickup_time"),myRs.getString("Dropoff_time") )); } 
            if (myConn != null) myConn.close();
        } catch (Exception exc) {exc.printStackTrace();}  
          
         //label.setText("Yes controller runs");
        ObservableList<ReservationCustomer> observableList = FXCollections.observableList(list);
        
        Confno.setCellValueFactory(new PropertyValueFactory<>("Confno"));
        Vlicense.setCellValueFactory(new PropertyValueFactory<>("Vlicense"));
        Branchid.setCellValueFactory(new PropertyValueFactory<>("Branchid"));
        Vtypename.setCellValueFactory(new PropertyValueFactory<>("Vtypename"));
        Pickup.setCellValueFactory(new PropertyValueFactory<>("Pickup"));
        Dropoff.setCellValueFactory(new PropertyValueFactory<>("Dropoff"));
        
        table_reservationcustomer.setItems(observableList);  
        
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        
 table_reservationcustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (table_reservationcustomer.getSelectionModel().getSelectedItem() != null) {
               ReservationCustomer rc = table_reservationcustomer.getSelectionModel().getSelectedItem();
                ConfNo = rc.getConfno();
            }
        });   
    
}
}
