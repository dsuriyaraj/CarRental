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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class Clerk_AvailableVehiclesController implements Initializable {

    @FXML private ComboBox _location,vehicletype;
    @FXML private ComboBox hh1,hh2,mm1,mm2;
    @FXML private DatePicker From,To;
    @FXML private Label message;
    @FXML private TableView<AvailableVehicle> table_available_vehicles;
    @FXML private TableColumn<AvailableVehicle,String>  Location;
    @FXML private TableColumn<AvailableVehicle,String>  Name;
    @FXML private TableColumn<AvailableVehicle,String>  Type;
    @FXML private TableColumn<AvailableVehicle,Integer>  Vlicense;
    final private String user= "root";
    final private String pass= "";
    
    public void Populate_Table() {
        Object Branch_ID;
        String Pickup_Date;
        String Dropoff_Date;
        Boolean Date1Valid=true;
        Boolean Date2Valid=true;
        if( ( null==From.getValue()) || (null==hh1.getValue()) || (null==mm1.getValue()) )
        {  Date1Valid = false; message.setText("Invalid 'From' Date & Time"); }
        if( ( null==To.getValue()) || (null==hh2.getValue()) || (null==mm2.getValue()) )
        {  Date2Valid = false; message.setText("Invalid 'To' Date & Time"); }
      
        if(Date1Valid && Date2Valid) {
        
        List<AvailableVehicle> list = new ArrayList<AvailableVehicle>();
        try {
            
              message.setText(" ");
              Pickup_Date = From.getValue() + " " + hh1.getValue() + ":" +mm1.getValue()+ ":00";
        
              Dropoff_Date =  To.getValue() + " " + hh2.getValue() + ":" +mm2.getValue()+ ":00";
            
            
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
            Statement myStmt = myConn.createStatement();
            Statement myStmt1 = myConn.createStatement();
           
             ResultSet myRs = myStmt.executeQuery("select * from Branch where Location='"+(String)_location.getValue() +"'");
             int count=0;
             while(myRs.next()) 
             { count++; } 
             if(count==0) {Branch_ID = "";}
             else { myRs.previous(); Branch_ID = myRs.getInt("BranchID"); }
             
             String Type = (String)vehicletype.getValue();
             if(Type==null) Type = "";
                 
                     
             String SQL = " select distinct v.BranchID,v.Vtype_name,v.Vname,v.Vlicense\n" +
                                    "from Vehicle v\n" +
                                    "where 1=1\n" +
                                    "and v.Vlicense not in\n" +
                                    "(select Vlicense from ((select distinct v.Vlicense from Vehicle v, RentalAgreement r\n" +
                                    "where 1=1\n" +
                                    "and v.Vlicense=r.Vlicense \n" +
                                    "and r.Dropoff_time > '"+Pickup_Date +"' and r.Pickup_time < '"+Dropoff_Date +"')\n" +
                                    "union\n" +
                                    "(select distinct v.Vlicense from Vehicle v,Reservation res\n" +
                                    "where 1=1\n" +
                                    "and v.Vlicense=res.Vlicense \n" +
                                    "and res.Dropoff_time > '"+Pickup_Date +"' and res.Pickup_time < '"+Dropoff_Date +"')) t1)\n" +
                                    "and v.Vtype_name like '%"+ Type+"%'\n" +
                                    "and v.BranchID like '%"+Branch_ID+"%'\n" +
                            "group by v.BranchID,v.Vtype_name,v.Vname,v.Vlicense"; 
             
            myRs =  myStmt.executeQuery(SQL);
            
            while (myRs.next()) { 
                ResultSet myRs1 = myStmt1.executeQuery("select Location from Branch where BranchID="+myRs.getInt("BranchID")+"");
                myRs1.next();    String _Location = myRs1.getString("location");
                myRs1.close();
                list.add(new AvailableVehicle  (_Location, myRs.getString("Vname"),myRs.getString("Vtype_name"), myRs.getInt("Vlicense")   )); 
            }         
            
            if (myConn != null) myConn.close();
            
        } catch (Exception exc) {exc.printStackTrace();}  
         
        ObservableList<AvailableVehicle> observableList = FXCollections.observableList(list);
        Location.setCellValueFactory(new PropertyValueFactory<>("Location"));
        Name.setCellValueFactory(new PropertyValueFactory<AvailableVehicle,String>("Name"));
        Type.setCellValueFactory(new PropertyValueFactory<AvailableVehicle,String>("Type"));
        Vlicense.setCellValueFactory(new PropertyValueFactory<AvailableVehicle,Integer>("Vlicense"));
        table_available_vehicles.setItems(observableList);    
        
        }
        
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
            while (myRs.next()) { listvehicletype.add(myRs.getString("Vtype_name")); }  
            if (myConn != null) myConn.close();
        } catch (Exception exc) {exc.printStackTrace();}  
         
        ObservableList<String> observableList = FXCollections.observableList(list);
        _location.setItems(observableList);

         ObservableList<String> observableVehicleList = FXCollections.observableList(listvehicletype);
         vehicletype.setItems(observableVehicleList);
         
         List hour1= new ArrayList();
         List hour2= new ArrayList();
         List minute1= new ArrayList();
         List minute2= new ArrayList();
         
          for(int i=0;i<24;i++) {  
             hour1.add(String.format("%02d",i)); 
             hour2.add(String.format("%02d",i));
         }
         for(int i=0;i<60;i++) { 
             minute1.add(String.format("%02d",i)); 
           //  second1.add(String.format("%02d", i)); 
             minute2.add(String.format("%02d",i)); 
           //  second2.add(String.format("%02d", i)); 
         }
         
           ObservableList hours1 = FXCollections.observableList(hour1);
        ObservableList minutes1= FXCollections.observableList(minute1);
    //    ObservableList seconds1 = FXCollections.observableList(second1);
        ObservableList hours2 = FXCollections.observableList(hour2);
        ObservableList minutes2= FXCollections.observableList(minute2);
        
         hh1.setItems(hours1);
        mm1.setItems(minutes1);
//        s1.setItems(seconds1);
        hh2.setItems(hours2);
        mm2.setItems(minutes2);
         
         
         
    }    
    
}
