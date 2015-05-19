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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 *
 * @author Bhupinder
 */
public class Clerk_OverdueController implements Initializable{
    
    @FXML private ComboBox _location,vehicletype;
     @FXML private TableView<AvailableVehicle> table_overdue;
    @FXML private TableColumn<AvailableVehicle,String>  Location;
    @FXML private TableColumn<AvailableVehicle,String>  Name;
    @FXML private TableColumn<AvailableVehicle,String>  Type;
    @FXML private TableColumn<AvailableVehicle,Integer>  Vlicense;
    final private String user= "root";
    final private String pass= "";
    
    public void Populate_Table() throws SQLException {
        Object Branch_ID;
        Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
        Statement myStmt = myConn.createStatement();
        Statement myStmt1 = myConn.createStatement();
        
        ResultSet myRs = myStmt.executeQuery("select * from Branch where Location='"+(String)_location.getValue() +"'");
             int count=0;
             while(myRs.next()) 
             { count++; } 
             if(count==0) {Branch_ID = "";}
             else { myRs.previous(); Branch_ID = myRs.getInt("BranchID"); }
             
             String VType = (String)vehicletype.getValue();
             if(VType==null) VType = "";
        List<AvailableVehicle> list = new ArrayList<>();
        String SQL = "select distinct v.BranchID,v.Vtype_name,v.Vname,v.Vlicense\n" +
                            "from RentalAgreement r, Vehicle v\n" +
                            "where 1=1\n" +
                            "and v.Vlicense=r.Vlicense \n" +
                            "and r.Dropoff_time<=(select sysdate() from dual)\n" +
                            "and v.Vtype_name like '%"+VType+"%'\n" +
                            "and v.BranchID like '%"+Branch_ID+"%' \n" +
                            "and not exists\n" +
                            "(select 1 from ReturnVehicle ret\n" +
                            "where 1=1\n" +
                            "and ret.RentId=r.RentId )\n" +
                      "group by v.BranchID,v.Vtype_name,v.Vname,v.Vlicense";
        
        myRs =  myStmt.executeQuery(SQL);
            
            while (myRs.next()) { 
                ResultSet myRs1 = myStmt1.executeQuery("select Location from Branch where BranchID="+myRs.getInt("BranchID")+"");
                myRs1.next();    String _Location = myRs1.getString("Location");
                myRs1.close();
                list.add(new AvailableVehicle  (_Location, myRs.getString("Vname"),myRs.getString("Vtype_name"), myRs.getInt("Vlicense")    )); 
            }         
       
         
        ObservableList<AvailableVehicle> observableList = FXCollections.observableList(list);
        Location.setCellValueFactory(new PropertyValueFactory<>("Location"));
        Name.setCellValueFactory(new PropertyValueFactory<AvailableVehicle,String>("Name"));
        Type.setCellValueFactory(new PropertyValueFactory<AvailableVehicle,String>("Type"));
        Vlicense.setCellValueFactory(new PropertyValueFactory<AvailableVehicle,Integer>("Vlicense"));

        table_overdue.setItems(observableList); 
        
        if (myConn != null) myConn.close();
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> list = new ArrayList<String>();
        List<String> listvehicletype = new ArrayList<String>();
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("select Location from Branch");
            while (myRs.next()) { list.add(myRs.getString("Location")); }  
            myRs = myStmt.executeQuery("select Vtype_name from VehicleType");
            while (myRs.next()) { listvehicletype.add(myRs.getString("vtype_name")); }  
        } catch (Exception exc) {exc.printStackTrace();}  
         
        ObservableList<String> observableList = FXCollections.observableList(list);
        _location.setItems(observableList);

         ObservableList<String> observableVehicleList = FXCollections.observableList(listvehicletype);
         vehicletype.setItems(observableVehicleList);
    }    
    
    
}
