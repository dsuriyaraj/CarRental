/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class EquipmentListController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private Label equip1,equip2,quantityslider1,quantityslider2;
    @FXML Slider equipslider1,equipslider2;
    private Integer Quantity1=0;
    private Integer Quantity2=0;
    
    public void setInfo(String Equip1,String Equip2) {
        equip1.setText(Equip1);
        equip2.setText(Equip2);
    }
    
    public int[] getQuantities() {
        int[] List = {0,0};
        List[0] = Quantity1;
        List[1] = Quantity2;
        return List;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         equipslider1.valueProperty().addListener(
         new ChangeListener<Number>() 
         {
            @Override
            public void changed(ObservableValue<? extends Number> ov, 
               Number oldValue, Number newValue) 
            {
              Quantity1 = newValue.intValue();
               quantityslider1.setText(Quantity1.toString());
            }
         }
      );
         
         equipslider2.valueProperty().addListener(
         new ChangeListener<Number>() 
         {
            @Override
            public void changed(ObservableValue<? extends Number> ov, 
               Number oldValue, Number newValue) 
            {
              Quantity2 = newValue.intValue();
               quantityslider2.setText(Quantity2.toString());
            }
         }
      );
      
             
             
    }    
    
}
