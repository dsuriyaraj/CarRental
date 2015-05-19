/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import static crs.CRS.checkVlicense;
import static crs.CRS.getCategoryList;
import static crs.CRS.getLocationList;
import crs.classes.Branch;
import crs.classes.Vehicle;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Manager_AddController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setLocation(dropLocation_add);
        setVtype(dropVType_add);     
        
      
    }  
    
    @FXML
    private Label alert_add;
    
    @FXML
    private TextField input_addName;
    @FXML
    private TextField input_addVlicense;
    @FXML
    private TextField input_addYear;
    @FXML
    private TextField inpute_addInitialPrice;
    
    @FXML
    private ComboBox dropLocation_add;
    @FXML
    private ComboBox dropCategory_add;
    @FXML
    private ComboBox dropVType_add;
    @FXML
    private Button addButton;
    
    

    

  
     @FXML
    private void onVtype(ActionEvent event){
        if( dropVType_add.getValue()==null   ) return;
        dropCategory_add.getItems().clear();
        setCatelog( dropVType_add.getValue().toString()    ,dropCategory_add);
        alert_add.setVisible(false);
    }
  
    
    
            
    @FXML
    private void addVehicle(ActionEvent event) {
        alert_add.setVisible(false);
        if(  dropCategory_add.getValue()==null  ) {
            alert_add.setText("category not set");
            alert_add.setVisible(true);
            return;
        
        }
       
        if(  dropLocation_add.getValue()==null  )     {
            alert_add.setText("location not set");
           alert_add.setVisible(true);
            return;
        }
        
         if(  input_addVlicense.getText().isEmpty()  )     {
            alert_add.setText("license not set");
            alert_add.setVisible(true);
            return;
        }
          if(  input_addName.getText().isEmpty()  )     {
            alert_add.setText("name not set");
            alert_add.setVisible(true);
            return;
        }
          if(  input_addYear.getText().isEmpty()  )     {
            alert_add.setText("year not set");
            alert_add.setVisible(true);
            return;
        }
        if(  inpute_addInitialPrice.getText().isEmpty()  )     {
            alert_add.setText("price not set");
            alert_add.setVisible(true);
            return;
        }
         
        
        int BranchID= CRS.getBranchid(dropLocation_add.getValue().toString());
        String name = input_addName.getText();
        String vtype = dropVType_add.getValue().toString();
        String category = dropCategory_add.getValue().toString();
        String location = dropLocation_add.getValue().toString();
        
        int vlicense =-1;
        try{
            vlicense = Integer.parseInt(  input_addVlicense.getText()  )  ;
        
        }catch(NumberFormatException ex){
            alert_add.setText("vlicense not valid");
            alert_add.setVisible(true);
            return;
        }
        
        
        
       if( checkVlicense(vlicense)==1  ){
           alert_add.setText("vlicense exists");
            alert_add.setVisible(true);
            return;
       
       } 
        
        Double price = -1.0;
        try{
          price = Double.parseDouble( inpute_addInitialPrice.getText()   );
        } catch(NumberFormatException ex){
            alert_add.setText("price not valid");
            alert_add.setVisible(true);
            return;
        }
        
        int  year = -1;
        try{
            year = Integer.parseInt(  input_addYear.getText() );
        
        }catch(NumberFormatException ex){
            alert_add.setText("year not valid");
            alert_add.setVisible(true);
            return;
        }
        
        if( name.isEmpty() )  {
            alert_add.setText("name not set");
            alert_add.setVisible(true);
            return;
        }      
        if( vlicense <=0)  {
            alert_add.setText("vlicense not valid");
            alert_add.setVisible(true);
            return;
        }
        if( price<0 )  {
            alert_add.setText("price not valid");
            alert_add.setVisible(true);
            return;
        }
        if( year<=2000|| year>2015  ){
            alert_add.setText("year not valid");
            alert_add.setVisible(true);
            return;
        }
        
        
        
        Vehicle vc = new Vehicle();       
        vc.BranchID = BranchID;
        vc.Vname = name;
        vc.Vtype_name = vtype;
        vc.category = category; 
        vc.status = 1;
        vc.Year = year;
        vc.initial_price = price;
        vc.Vlicense = vlicense;    
        int count = CRS.addNewVehicle(vc);
        if(count==-2){
            //vlicense existed
            alert_add.setText("fail,vlicense existed");
            alert_add.setVisible(true);
            return;
        }else if(count==1){
            alert_add.setText("vehicle added!");
            alert_add.setVisible(true);
        }
        
        input_addName.setText(""); 
        input_addVlicense.setText("");
        input_addYear.setText("");
        inpute_addInitialPrice.setText("");
        
    }
    
       private void setLocation(ComboBox cmb){
        ArrayList<Branch> list = new ArrayList<Branch>();
        list = getLocationList();
        ObservableList<String> options =  FXCollections.observableArrayList();        
        for(int i=0; i<list.size()  ;i++){
            options.add( list.get(i).location+"" );
        }
        
        cmb.setItems(options);   
    
    }
    
    private void setCatelog(String vtype,ComboBox cmb){  //need to fix
        ArrayList<String> list = new ArrayList<String>();
        
        list = getCategoryList(vtype);
        ObservableList<String> options =  FXCollections.observableArrayList();

        for(int i=0; i<list.size()  ;i++){
            options.add( list.get(i) );
        }
   
        cmb.setItems(options);
        
    
    }
        
    private void setVtype(ComboBox cmb){
        cmb.getItems().clear();
        ArrayList<String> list = new ArrayList<String>();
        ObservableList<String> options =  FXCollections.observableArrayList();        
            options.add( "Car" );           
            options.add( "Truck" );
        cmb.setItems(options);
    
    }
    

    
}
