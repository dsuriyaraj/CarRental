/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;


import com.mysql.jdbc.Util;
import static crs.CRS.getCategoryList;
import crs.classes.Price;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Manager_SetrateController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         setVtype(dropVType_rate);   
         setNotEditable();
    }    
    @FXML
    private Pane setRatePane;
    @FXML
    private ComboBox dropVType_rate;
    @FXML
    private ComboBox dropCategory_rate;
    @FXML
    private TextField hourly_rate;
    @FXML
    private TextField ins_hour;
    @FXML
    private TextField daily_rate;
    @FXML
    private TextField ins_daily;
    @FXML
    private TextField weekly_rate;
    @FXML
    private TextField ins_week;
    @FXML
    private TextField per_km_rate;
    @FXML
    private Button  button_save_rate;
    @FXML
    private Button  button_update_rate;
    
    @FXML
    private Label alert_setrate;
    
    @FXML
    private void onVtype_rate(ActionEvent event){
    if( dropVType_rate.getValue()==null   ) return;
    setCatelog( dropVType_rate.getValue().toString(),dropCategory_rate);
    }
    

    
    @FXML
    private void onCategory_rate(ActionEvent event){
        if( dropCategory_rate.getValue()==null  ) return;
        String type = dropCategory_rate.getValue().toString();
        showRates(type);
    }
    
    private void showRates(String type){
        Price price = new Price(); 
        price = CRS.getPrice(type);
        hourly_rate.setText( formatPrice( price.hourly_rate));
        ins_hour.setText( formatPrice( price.ins_hrate));
        daily_rate.setText( formatPrice(price.daily_rate));
        ins_daily.setText(formatPrice( price.ins_drate));
        weekly_rate.setText( formatPrice(price.weekly_rate));
        ins_week.setText( formatPrice(price.ins_wrate));
        per_km_rate.setText(formatPrice( price.km_rate));     
    
    }
    
    
    public static String formatPrice(double d) {
       DecimalFormat df = new DecimalFormat("0.00");
        String num = df.format(d);
        return num;
    }
    
    @FXML
    private void  on_button_update_rate(ActionEvent event){
        if( dropCategory_rate.getValue()==null ) return;
        setEditable();
        
        button_update_rate.setVisible(false);
        button_save_rate.setLayoutX(button_update_rate.getLayoutX()  );
        button_save_rate.setLayoutY(button_update_rate.getLayoutY()  );
        button_save_rate.setVisible(true);
        
    }
    @FXML
    private void  on_button_save_rate(ActionEvent event) {
        //setNotEditable();
        alert_setrate.setVisible(false);
        
        if( dropCategory_rate.getValue()==null  ){
            alert_setrate.setVisible(true);
            alert_setrate.setText("category not set");       
            return;
        }
        
        
        String type = dropCategory_rate.getValue().toString();        
        double hrate = getValue(hourly_rate,"hourly rate");        
        double drate = getValue(daily_rate,"daily rate");        
        double wrate = getValue(weekly_rate,"weekly rate");        
        double ihrate = getValue(ins_hour,"hourly insurance rate");        
        double idrate = getValue(ins_daily,"daily insurance rate");        
        double iwrate = getValue(ins_week,"weekly insurance rate");        
        double km_rate = getValue(per_km_rate,"per km rate");
        if( hrate<0 || drate<0 || wrate<0 || ihrate<0 || idrate<0 || iwrate<0 || km_rate<0  ) {
             
            return;
        }
        
        Price price = new Price();      
        
        price.vtype_name = type;
        price.hourly_rate = hrate;
        price.daily_rate = drate;
        price.weekly_rate = wrate;
        price.ins_hrate =ihrate;
        price.ins_drate = idrate;
        price.ins_wrate = iwrate;
        price.km_rate = km_rate;
        int count = CRS.setPrice(price);
        if(count==1){
            System.out.println("inserted");
            alert_setrate.setText("updated");
            alert_setrate.setVisible(true);
             showRates(price.vtype_name);
            setNotEditable();
        }
        else System.out.println("fail");
        button_update_rate.setVisible(true);
        button_save_rate.setVisible(false);
    
    }
    
    private double getValue(TextField tf,String name){
        double price = -1;
    try{
          price = Double.parseDouble( tf.getText()   );
        } catch(NumberFormatException ex){
            alert_setrate.setText(name+" not valid");
            alert_setrate.setVisible(true);
            return -1;
        }
    if(price<0){
        alert_setrate.setText(name+" not valid");
            alert_setrate.setVisible(true);
            return -1;
    }
    return price;
    
    }

    
    //set all the text fields to editable or not 
    private void setNotEditable(){
        
        hourly_rate.setEditable(false);
        ins_hour.setEditable(false);
        ins_daily.setEditable(false);
        weekly_rate.setEditable(false);
        daily_rate.setEditable(false);
        ins_week.setEditable(false);
        per_km_rate.setEditable(false);
    }
    
    private void setEditable(){
        
        hourly_rate.setEditable(true);
        ins_hour.setEditable(true);
        ins_daily.setEditable(true);
        weekly_rate.setEditable(true);
        daily_rate.setEditable(true);
        ins_week.setEditable(true);
        per_km_rate.setEditable(true);  
    }
 
       private void setVtype(ComboBox cmb){
        cmb.getItems().clear();
        ArrayList<String> list = new ArrayList<String>();
        ObservableList<String> options =  FXCollections.observableArrayList();        
            options.add( "Car" );           
            options.add( "Truck" );
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
        
}
