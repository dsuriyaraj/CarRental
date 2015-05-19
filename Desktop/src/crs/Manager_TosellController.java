/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import static crs.CRS.getCategoryList;
import static crs.CRS.getLocationList;
import crs.classes.Branch;
import crs.classes.Vehicle;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Manager_TosellController implements Initializable {

    /**
     * Initializes the controller class.
     */
   
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    
            

      setLocation(dropLocation_sale);
      setVtype(dropVtype_sell);
      
      updateTosellTable();
      setUpToSell();
    }   
    
   @FXML
    private Pane toSellPane;
    @FXML
    private TextField dropYears;
    @FXML
    private ComboBox dropCategory_sale;
    @FXML
    private ComboBox dropLocation_sale;
    @FXML
    private ComboBox dropVtype_sell;
    @FXML
    private TableView tosellTable;
    @FXML
    private TextField  selling_price_tosale;
    @FXML
    private Label alert_tosell;    
    @FXML
    private Label alert_tosell2;
    @FXML
    private Button searchButton;
    @FXML
    private Button moveButton;
    
         
    private int VID_toSell = -1;
    
    


    private void setUpToSell(){
        
        selling_price_tosale.setText("");     
        VID_toSell = -1;
    
    }
    
    
    @FXML
    private void ondropVtype_sell(ActionEvent event){
           if(dropVtype_sell.getValue()==null) return;
           String category = dropVtype_sell.getValue().toString();    
           if(  category=="Any type") {
               dropCategory_sale.setValue("");
           }
           else {
               dropCategory_sale.getItems().clear();
               setCatelog(category,dropCategory_sale);
           }
           
    }
    
   
    
    @FXML
    private void searchListByLCY(ActionEvent event)  {  
        alert_tosell.setVisible(false);
        if( dropYears.getText().isEmpty()  ) {
            alert_tosell.setText("Please give year");
            alert_tosell.setVisible(true);
            return;
        }
        
         updateTosellTable();
      
    }
    
    private void updateTosellTable(){
        
        ArrayList<Vehicle> list =  new ArrayList<Vehicle>();
        int boughtYear = -1;

        int year_sell=-1;
        if(  !dropYears.getText().isEmpty()) 
            try{
            year_sell = Integer.parseInt(   dropYears.getText().toString()  )  ;
            
            }catch(NumberFormatException ex){
                return;
            }
        
         
         
        String category_sell = "";
        String location_sell = "";
        if( dropCategory_sale.getValue()!=null   ){
            category_sell = dropCategory_sale.getValue().toString();
        }
        if( dropLocation_sale.getValue()!=null   ){
            location_sell = dropLocation_sale.getValue().toString();
        }
        
        if( year_sell < 0 ){
            list =  CRS.getVehicleListForRentOrSell(1);//rent:1 sale:0
        }else{     
       
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            Date thisYear = new Date();
            boughtYear = Integer.parseInt( dateFormat.format(thisYear).toString())-year_sell;
   
            list = CRS.getVehicleListByCLY(category_sell,CRS.getBranchid(location_sell) ,boughtYear  );   
   
        }
  
        setTable(list,tosellTable);
    
    }
    
    
    /**********start of dialogStage**************/
    
    public int dialogFlag_sell=-1;
    
    @FXML
    private void moveToSellList(ActionEvent event){
        
        alert_tosell2.setVisible(false);
        double price = -1;
        try{
          price = Double.parseDouble( selling_price_tosale.getText()   );
          if(price<0){
            alert_tosell2.setText("price not valid");
            alert_tosell2.setVisible(true);
            return;
          }
        } catch(NumberFormatException ex){
            alert_tosell2.setText("price not valid");
            alert_tosell2.setVisible(true);
            return;
        }
        if(VID_toSell<0){ 
            alert_tosell2.setText("vehicle not selected");
            alert_tosell2.setVisible(true);            
            return;
        }
        if(price<=0){ 
            alert_tosell2.setText("price not valid");
            alert_tosell2.setVisible(true);            
            return;
        }
        
        popDialog("Are you sure to put it on sale?");

    }
    
    private void putTosale(){
        double selling_price = Double.parseDouble(selling_price_tosale.getText()) ;
        
        if( dialogFlag_sell == 1){
            int count = CRS.setToSell(VID_toSell,selling_price);
            if(count>=1){ 
                
            alert_tosell2.setText("move to cell successfully");
            alert_tosell2.setVisible(true); 
                updateTosellTable();
            }else {                
                alert_tosell2.setText("move to sell fail");
                alert_tosell2.setVisible(true); 
            }
        }
    
    }
    @FXML
    private Pane dialogpane;
    @FXML
    private Button okbutton;
    @FXML
    private Button cancelbutton;
    
    
    private void popDialog(String text){
        
         
        Stage dialogStage = new Stage();
        
        okbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            dialogFlag_sell = 1;
            putTosale();
            dialogStage.close();
            
        }
        });
        
        cancelbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            dialogFlag_sell = 0;
            dialogStage.close();
        }
        });

        
        dialogStage.initModality(Modality.WINDOW_MODAL);
     
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(dialogpane).build()));
        dialogStage.show();
        
        
    }
   
    /**********end of dialogStage**************/

    private void setTable(ArrayList<Vehicle> list,TableView<Vehicle> table){
        
        TableColumn<Vehicle,String> VIDCol = new TableColumn<>("Vlicense");
        VIDCol.setMinWidth(70);
        TableColumn<Vehicle,String> VtypeCol = new TableColumn<>("Vtype_name");
        VtypeCol.setMinWidth(70); 
        TableColumn<Vehicle,String> typeCol = new TableColumn<>("category");
        typeCol.setMinWidth(90); 
        TableColumn<Vehicle,String> nameCol = new TableColumn<>("Vname");
        nameCol.setMinWidth(90); 
        TableColumn<Vehicle,String> LocationCol = new TableColumn<>("Branch_location");
        LocationCol.setMinWidth(160);
        TableColumn<Vehicle,String> yearCol = new TableColumn<>("Year");
        yearCol.setMinWidth(70);
        TableColumn<Vehicle,String> priceCol = new TableColumn<>("initial_price");
        priceCol.setMinWidth(80);
        
        table.getColumns().clear();        
        table.getColumns().addAll(VIDCol,nameCol,LocationCol,VtypeCol,typeCol,yearCol,priceCol);
       if(list.size()==0){
        return;
       }
        ObservableList<Vehicle> myData = FXCollections.observableArrayList();
        for(int i=0; i<list.size();i++){
            myData.add(list.get(i));
        }
   
        VIDCol.setCellValueFactory(
                new PropertyValueFactory<Vehicle, String>("Vlicense"));
        VtypeCol.setCellValueFactory(
                new PropertyValueFactory<Vehicle, String>("Vtype_name"));
        typeCol.setCellValueFactory(
                new PropertyValueFactory<Vehicle, String>("category"));
        nameCol.setCellValueFactory(
                new PropertyValueFactory<Vehicle, String>("Vname"));
        LocationCol.setCellValueFactory(
                new PropertyValueFactory<Vehicle, String>("Branch_location"));
        yearCol.setCellValueFactory(
                new PropertyValueFactory<Vehicle, String>("Year"));
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Vehicle, String>("initial_price"));
        

        table.setItems(myData);
        table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Vehicle>() {
            public void onChanged(ListChangeListener.Change<? extends Vehicle> c) {

                for (Vehicle p : c.getList()) {
                    VID_toSell = p.getVlicense();
                }

            }
        });
 
    }
    
     
    
    private void setLocation(ComboBox cmb){
        ArrayList<Branch> list = new ArrayList<Branch>();
        list = getLocationList();
        ObservableList<String> options =  FXCollections.observableArrayList();
        options.add("Any location");
        
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
       // cmb.getItems().clear();
        ArrayList<String> list = new ArrayList<String>();
        ObservableList<String> options =  FXCollections.observableArrayList();
            options.add( "Any type" );
            options.add( "Car" );           
            options.add( "Truck" );
        cmb.setItems(options);
    
    }
    



    
    
}
