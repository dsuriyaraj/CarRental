/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import crs.classes.Vehicle;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Manager_SoldController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        updateSoldTable();
      sold_datepicker.setLayoutX( input_sold_date.getLayoutX() );
      sold_datepicker.setLayoutY( input_sold_date.getLayoutY() ); 
    }    
      @FXML
    private Pane soldPane;
    
    @FXML
    private Label alert_sold;
    @FXML
    private TableView  SoldTable;
    @FXML
    private ComboBox dropSoldto;
    @FXML
    private TextField input_sold_price;
    @FXML
    private TextField input_sold_soldto;
    @FXML
    private TextField input_sold_date;
    @FXML
    private DatePicker sold_datepicker;
    @FXML
    private Button soldButton;
    
    private int VID_toSell = -1;
      
    @FXML
    private void gotoSellOut(ActionEvent event) {
      
      soldPane.setVisible(true);
      soldPane.setLayoutX(100);
      soldPane.setLayoutY(200); 
      updateSoldTable();
      
    }
    @FXML
    private void onSoldTo(ActionEvent event) {
        alert_sold.setVisible(false);
        if(  VID_toSell<0   ){
            alert_sold.setText("vehicle not selected");
            alert_sold.setVisible(true);
            return;
        
        }
        if(  sold_datepicker.getValue()==null ){ 
            alert_sold.setText("date not set");
            alert_sold.setVisible(true);
            return;        
        }
        
        double price = 0;
        try{
          price = Double.parseDouble( input_sold_price.getText()   );
          if(price<=0){ 
              alert_sold.setText("price not valid");
              alert_sold.setVisible(true);
              return;
          }
        } catch(NumberFormatException ex){ 
            alert_sold.setText("price not valid");
            alert_sold.setVisible(true);
            return;
        }
       
        String soldto = input_sold_soldto.getText();
        String date = sold_datepicker.getValue().toString();//yyyy-mm-dd
        int count =  CRS.setSold(VID_toSell, date, price, soldto);
        System.out.println("count="+count);
        if(count>=1){        
            alert_sold.setText("sold successfully"); 
            alert_sold.setVisible(true);
            sold_datepicker.setValue(null);
            input_sold_soldto.setText("");
            input_sold_price.setText("");
        }
        updateSoldTable();
    }
    
    
    private void updateSoldTable(){
        
        ArrayList<Vehicle> list =  new ArrayList<Vehicle>();
        list = CRS.getVehicleListForRentOrSell(0);
      
  
        setTable(list,SoldTable);
    
    }
        
    private void setSoldTo(ComboBox cmb){
        ArrayList<String> list = new ArrayList<String>();
        ObservableList<String> options =  FXCollections.observableArrayList();
            options.add("Customer");
            options.add( "Dealer" );       
        cmb.setItems(options);
    
    }
    
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
        TableColumn<Vehicle,String> sell_priceCol = new TableColumn<>("selling price");
        sell_priceCol.setMinWidth(80);
        
        table.getColumns().clear();        
        table.getColumns().addAll(VIDCol,nameCol,LocationCol,VtypeCol,typeCol,yearCol,priceCol,sell_priceCol);
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
        sell_priceCol.setCellValueFactory(
                new PropertyValueFactory<Vehicle, String>("selling_price"));
        

        table.setItems(myData);
        table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Vehicle>() {
            public void onChanged(ListChangeListener.Change<? extends Vehicle> c) {

                for (Vehicle p : c.getList()) {
                    VID_toSell = p.getVlicense();
                }

            }
        });
 
    }
    
}
