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
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ListVehiclesController implements Initializable {
    @FXML private TableView<Vehicle> tablevehicles;
    @FXML private TableColumn<Vehicle,Integer> Vlicense;
    @FXML private TableColumn<Vehicle,String> Name;
    @FXML private TableColumn<Vehicle,Integer> Year;
    private Mediator0 currentInfo;
    @FXML private Label label;
    private Integer license;
   
    final private String user= "root";
    final private String pass= "";
    
    public Integer V_ID() {
        return license;
    }
   
    
    public void setInfo(Mediator0 Info) {
           
        List<Vehicle> list = new ArrayList<Vehicle>();
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs =  myStmt.executeQuery("Select * from Vehicle where BranchID="+Info.getdata2()+" and Vtype_name='"+Info.getdata1()+"' and Status=1 order by Vlicense" );
            while (myRs.next()) { list.add(new Vehicle  (myRs.getInt("Vlicense"), myRs.getString("Vname"),myRs.getInt("Year")    )); }         
            if (myConn != null) myConn.close();
        } catch (Exception exc) {exc.printStackTrace();}  
         
        ObservableList<Vehicle> observableList = FXCollections.observableList(list);
        Vlicense.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("Vlicense"));
        Name.setCellValueFactory(new PropertyValueFactory<Vehicle,String>("Name"));
        Year.setCellValueFactory(new PropertyValueFactory<Vehicle,Integer>("Year"));

        tablevehicles.setItems(observableList);    
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
        //setInfo();
 tablevehicles.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (tablevehicles.getSelectionModel().getSelectedItem() != null) {
               Vehicle vehicle = tablevehicles.getSelectionModel().getSelectedItem();
                license = vehicle.getVlicense();
            }
        });
        
        
    }    
    
}
