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

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ReturnConfirmationController implements Initializable {
   @FXML private Label transaction_0,transaction_1,transaction_2,transaction_3,transaction_4,transaction_5,cs_0,cs_1,cs_2,cs_3,cs_4,cust_0,cust_1,cust_2,points_label,reduced_insurance;

   @FXML
   public void setInfo(String Name,String Pickup_DateTime,String ReturnDate,Long week,Long Day,Long Hour,Float BC,Float IC,Float EC,Float OC,String cusName,String cusPhone,Integer isClubmember, Object points,Object reducedinsurance ) {
       transaction_0.setText(Name);
       transaction_1.setText(Pickup_DateTime);
       transaction_2.setText(ReturnDate);
       transaction_3.setText(week.toString());
       transaction_4.setText(Day.toString());
       transaction_5.setText(Hour.toString());
       cs_0.setText(BC.toString()); 
       cs_1.setText(IC.toString());
       cs_2.setText(EC.toString());
       cs_3.setText(OC.toString());
       Float TC= BC+EC+IC+OC;
       cs_4.setText(TC.toString()+" CAD");
       cust_0.setText(cusName);
       cust_1.setText(cusPhone);
       if(isClubmember==1) points_label.setVisible(true);
       cust_2.setText(points.toString());
       reduced_insurance.setText(reducedinsurance.toString());
       
   }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        points_label.setVisible(false);
    }    
    
}
