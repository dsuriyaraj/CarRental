/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bhupinder
 */
public class ProcessReturnController implements Initializable {

    @FXML private ComboBox choice_rental_id,hh,mm,combo_tankfull;
    @FXML private Pane content_processreturn_section1,pane_clubmember,content_processreturn_section2;
    @FXML private Button button_rid,button_continue;
    @FXML private TextField rental_id,ds1,ds2,ds3,use_points,reading_odometer;
    @FXML private Label message1,estimated_cost,member_points,message2;
    @FXML private DatePicker from,to,return_date;
    private String Pickup_DateTime,ReturnDate,customer_name;
    private Float bc=0f,ic=0f,ec=0f,oc=0f;
    private Integer choice;
    private long r_week=0,r_day=0,r_hour=0,r_day_raw=0;
    private Integer Odometer=0,Vlicense,Rent_ID;
    private String customer_phone;
    static Integer Total_points=0;
    private Integer readingodometer=0,equipment;
    private Boolean isValidCustomer = false,isPriceCalculated=false;
    private Integer clubmember=0,roadstar;
    final private String user= "root";
    final private String pass= "";
    
    
     
   @FXML public void Toggle() {
       if("Phone & Dates"==choice_rental_id.getValue()) {
            content_processreturn_section1.setTranslateY(-35); 
            content_processreturn_section1.setVisible(true);
            rental_id.setVisible(false);
            button_rid.setTranslateX(-60);
            button_rid.setTranslateY(140); 
            choice = 1;
        }
       else { 
           button_rid.setTranslateY(0);
           rental_id.setVisible(true);
           content_processreturn_section1.setTranslateY(0); 
           button_rid.setTranslateX(0);
           content_processreturn_section1.setVisible(false);
          choice = 0;
       }
   }
    
   @FXML public void LocateRecord() throws SQLException {
       
       Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
       Statement myStmt = myConn.createStatement();
       ResultSet rs;
       
       if(choice==1) {
           
           Integer _ds1,_ds2,_ds3;
            Boolean isvalid_phone = true;
            Boolean isValidDate = true;
            try   {
                    _ds1 = Integer.parseInt((String)ds1.getText());
                    _ds2 = Integer.parseInt((String)ds2.getText());
                    _ds3 = Integer.parseInt((String)ds3.getText());
                    message1.setText(" ");
            }
            catch (NumberFormatException e) {message1.setText("Invalid Phone Number"); isvalid_phone = false;}
            
            if( (null==from.getValue()) || (null==to.getValue()))
            { isValidDate = false; message1.setText("'From' or 'To' Date missing");  }
            
            if(isvalid_phone && isValidDate) { 
                
                customer_phone = ds1.getText()+"-"+ds2.getText()+"-"+ds3.getText();  
                String sql = "Select * from RentalAgreement WHERE Phone_number='"+customer_phone+"' and DATE(Pickup_time)='"+from.getValue() +"' and DATE(Dropoff_time)='"+to.getValue() +"'";
                System.out.println(sql);
                rs = myStmt.executeQuery(sql);
                int count=0;
                while(rs.next()){
                    count++;

                }
                if(count==1) {
                    rs.previous();
                    Rent_ID        = rs.getInt("RentId");
                    Odometer        = rs.getInt("Odometer");
                    Pickup_DateTime = rs.getString("Pickup_time");
                    Pickup_DateTime = Pickup_DateTime.substring(0, Pickup_DateTime.length() - 2);
                    System.out.println("Pickup DatTime :"+Pickup_DateTime);
                    Vlicense        = rs.getInt("Vlicense");
                    equipment       = rs.getInt("Equipment");
                    
                    isValidCustomer = true;
                    
                    message1.setText("Record Located");
                    sql = "SELECT Clubmember,Roadstar,Name from Customer WHERE Phone_number='"+customer_phone+"'";
                    rs = myStmt.executeQuery(sql); 
                    rs.next();
                    clubmember =  rs.getInt("Clubmember");
                    roadstar =  rs.getInt("Roadstar");
                    customer_name = rs.getString("Name");
                    if(clubmember==1) {
                        pane_clubmember.setVisible(true);
                        content_processreturn_section2.setTranslateY(40);
                        rs = myStmt.executeQuery("Select Points from ClubMember  WHERE Phone_number='"+customer_phone+"'");
                        rs.next();
                        Total_points = rs.getInt("Points");
                        member_points.setText(Integer.toString(Total_points));
                    }
                    else {
                        pane_clubmember.setVisible(false);
                        content_processreturn_section2.setTranslateY(0);
                    }
                    
                }
                else message1.setText("No record found .. ");
            }
       }
       else {
                Integer rentalid=0;
                Boolean isvalid_confirmation = true;
           
                try {
                        rentalid = Integer.parseInt((String)rental_id.getText());
                        message1.setText(" ");
                }
                catch (NumberFormatException e) {message1.setText("Invalid Rental number"); isvalid_confirmation = false;}
                
                if(isvalid_confirmation)  {
                    String sql = "SELECT * from RentalAgreement WHERE RentId="+rentalid+"";
                    System.out.println(sql);
                        rs = myStmt.executeQuery(sql);
                        int count=0;
                        while(rs.next()){
                            count++;
                            
                        }
                        if(count==1)  {
                            rs.previous();
                            Rent_ID        = rs.getInt("RentId");
                            Odometer        = rs.getInt("Odometer");
                            Pickup_DateTime = rs.getString("Pickup_time");
                            Pickup_DateTime = Pickup_DateTime.substring(0, Pickup_DateTime.length() - 2);
                            System.out.println("Pickup DatTime :"+Pickup_DateTime);
                            Vlicense        = rs.getInt("Vlicense");
                            equipment       = rs.getInt("Equipment");
                            customer_phone =  rs.getString("Phone_number");
                            
                            
                            isValidCustomer = true;
                            message1.setText("Record Located"); 
                            
                            sql = "SELECT Clubmember,Roadstar,Name from Customer WHERE Phone_number='"+customer_phone+"'";
                            rs = myStmt.executeQuery(sql); 
                            rs.next();
                            clubmember =  rs.getInt("Clubmember");
                            roadstar =  rs.getInt("Roadstar");
                            customer_name = rs.getString("Name");
                            if(clubmember==1) {
                                pane_clubmember.setVisible(true);
                                content_processreturn_section2.setTranslateY(40);
                                rs = myStmt.executeQuery("Select Points from ClubMember WHERE Phone_number='"+customer_phone+"'");
                                rs.next();
                                 Total_points = rs.getInt("Points");
                                 member_points.setText(Integer.toString(Total_points));
                                 
                            }
                            else {
                             pane_clubmember.setVisible(false);
                             content_processreturn_section2.setTranslateY(0);
                            }
 
                        }
                        else message1.setText("No record found ..");
                }      
       }         
   }
   
   @FXML
   public void Load_ReturnConfirmation() throws IOException, SQLException {
       if(isPriceCalculated) {
       
       Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
       Statement myStmt = myConn.createStatement();
       ResultSet rs;
       String sql = "SELECT Vname from Vehicle WHERE Vlicense="+Vlicense+"";
       rs = myStmt.executeQuery(sql); 
       rs.next();   String Name = rs.getString("Vname");
       
        Integer Tankfull=0;
        if("Yes"==combo_tankfull.getValue()) Tankfull = 1;

        String sql_ = "INSERT INTO ReturnVehicle (RentID,Dropoff_time,Fulltank,Odometer,Cost) VALUES ("+Rent_ID+",'"+ReturnDate+"',"+Tankfull+","+readingodometer+","+(bc+ic+ec+oc)+")";
        System.out.println(sql_);
        myStmt.executeUpdate(sql_);

       FXMLLoader loader = new FXMLLoader();       
       loader.setLocation(getClass().getResource("ReturnConfirmation.fxml"));
       loader.load();
       Parent p = loader.getRoot();
       Stage stage = new Stage();
       stage.setScene(new Scene(p));
       stage.initModality(Modality.APPLICATION_MODAL);
       stage.initOwner(button_continue.getScene().getWindow());
       ReturnConfirmationController controller = loader.getController();
       
       Object points = " ";
       if(clubmember==1) {
           rs = myStmt.executeQuery("select Points from ClubMember where Phone_number='"+customer_phone+"'");
           rs.next(); points = rs.getInt("Points");
           
       }
       Object reduced_insurance = " ";
       if(roadstar==1)
       {
           reduced_insurance = "(50% Reduction)";
       }
       
       controller.setInfo( Name, Pickup_DateTime, ReturnDate , r_week, r_day, r_hour,bc,ic,ec,oc,customer_name,customer_phone,clubmember,points,reduced_insurance );
       stage.showAndWait();
       
       }
       else {
           message2.setText("Please compute costs,first ..");
       }
       
   }
   
   public void TotalCost() throws ParseException, SQLException {
       
       Boolean isvalid_points = true;
       Boolean isvalidDate = true;
       Boolean isvalidReturnDate=true;
       String sql_sub_points="";
       String sql_add_points="";
       Integer points=0;
       Integer sector = 0;
       long temp;
       float unit_points;
      long week=0,day=0,hour=0,day_raw=0;
       
       if( (null==return_date.getValue()) || (null==hh.getValue()) || (null==mm.getValue()))
       {isvalidDate = false;  message2.setText("Return Datetime incomplete ..");}
       else {
           ReturnDate=return_date.getValue()+" "+hh.getValue()+":"+mm.getValue()+":00";
            HashMap<String, Long> map = TotalTime(Pickup_DateTime,ReturnDate);
             week = r_day= map.get("week");
             day  =r_day= map.get("day");
             hour = r_hour=map.get("hour");
             day_raw = r_day_raw= map.get("day_raw");
       }
    
       if(clubmember==1) {   
           try   {
                    points = Integer.parseInt((String)use_points.getText());
                    message2.setText(" ");            
            }
            catch (NumberFormatException e) {message2.setText("Use numeric points"); isvalid_points = false;}        
       }
       
       
       
       Boolean isValid_readingodometer=true;
       try   {
                    readingodometer = Integer.parseInt((String)reading_odometer.getText());
                    message2.setText(" ");
                    //if(!isvalid_points) message2.setText("Use numeric points or points exceed maximum usable points");
            }
            catch (NumberFormatException e) {message2.setText("Odometer reading should be numeric"); isValid_readingodometer = false;}
       
       if (Total_points < points ) { 
           isvalid_points = false;
           message2.setText("Points exceeding maximum usable points");
       }
       if(readingodometer < Odometer) {
          isValid_readingodometer = false; 
          message2.setText("Incorrect odometer reading");
       }
       if(!isValidCustomer) message1.setText("Please validate Rental Info");
       
       if(isvalidDate) { 
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
           String Date1Copy = Pickup_DateTime.substring(0,Pickup_DateTime.length() - 8);
           String Date2Copy = ReturnDate.substring(0,ReturnDate.length() - 8);
           System.out.println("Date1 = "+Date1Copy);
           System.out.println("Date2 = "+Date2Copy);
           Date From = format.parse(Date1Copy);
           Date To =  format.parse(Date2Copy);
           if(To.compareTo(From)<0)
           { isvalidReturnDate= false;  message2.setText("Return date before Checkout date ...");}
       }
       
       if(isValidCustomer && isValid_readingodometer && isvalid_points && (readingodometer - Odometer>=0) && isvalidDate && isvalidReturnDate) {
           Boolean isOK = true;
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crs", user, pass);
           Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("select Vtype_name from Vehicle where Vlicense='"+Vlicense +"'");
           myRs.next(); 
           String V_Type = myRs.getString("Vtype_name");
           
                      if( (points >=1000)) {
                Integer remainder = 0;
               if(V_Type.equals("Luxury")||V_Type.equals("SUV")||V_Type.equals("Van")||V_Type.equals("12-foot")||V_Type.equals("15-foot")||V_Type.equals("24-foot")||V_Type.equals("Box Truck")||V_Type.equals("Cargo Van"))
               {    
                    sector = 1; 
                    remainder = points%1500;
                    unit_points = points/1500; 
                    
                    System.out.println("Unit point Sector 1 :"+unit_points);
                    
                    if(remainder>0){ message2.setText("Points should be mutiple of 1500.."); isOK = false;}
                    else {
                        message2.setText("");
                        sql_sub_points = "UPDATE Clubmember SET Points = Points -"+unit_points*1500+" WHERE Phone_number='"+customer_phone+"'";
                        myStmt.executeUpdate(sql_sub_points);
                    }
                    
               }
               else {
                   sector = 2;
                   remainder = points%1000;
                   unit_points = points/1000;
                    System.out.println("Unit point Sector 2 :"+unit_points);
                    if(remainder>0){ message2.setText("Points should be mutiple of 1000.."); isOK = false; }
                    else {
                        message2.setText("");
                        sql_sub_points = "UPDATE Clubmember SET Points = Points -"+unit_points*1000+" WHERE Phone_number='"+customer_phone+"'";
                        myStmt.executeUpdate(sql_sub_points);
                    }
               }
               
               if(isOK) {
              if((day >=0)&& (unit_points>=1.0f) ) {
                   day = day - (long)unit_points;
                   temp = day;
                   if(temp<0) {
                       day = 0;
                       temp = temp*(-1);
                       
                       if(sector==1)
                           sql_add_points = "UPDATE ClubMember SET Points = Points +"+temp*1500+" WHERE Phone_number='"+customer_phone+"'";
                       else
                           sql_add_points = "UPDATE ClubMember SET Points = Points +"+temp*1000+" WHERE Phone_number='"+customer_phone+"'";
                       
                       myStmt.executeUpdate( sql_add_points);
                   }
                       
               }
              
                      }
           }
       
       
       
      if(isOK) {
       
       myRs = myStmt.executeQuery("select * from VehicleType where Vtype_name='"+V_Type +"'");
       myRs.next();
                      
                      
       float price3 = 0.00f;
       if( (readingodometer - Odometer) > 500)
           price3 = oc=  myRs.getFloat("Km_rate")*(readingodometer - Odometer -500);
       System.out.println("Price 3 :"+price3);
       
       
       float ins_drate = myRs.getFloat("Ins_drate");
       float ins_wrate = myRs.getFloat("Ins_wrate");
       float ins_hrate = myRs.getFloat("Ins_hrate");
       
       if(roadstar==1) {
           ins_drate = (float) (ins_drate/2.0);
           ins_wrate = (float) (ins_wrate/2.0);
           ins_hrate = (float) (ins_hrate/2.0);
           
       }
       System.out.println("Insurance Daily :"+ins_drate+" Weekly: "+ins_wrate+" Hourly :"+ins_hrate);
      
       
       float _price1 = bc =  day*(myRs.getFloat("Daily_rate"))  +  
                     week*(myRs.getFloat("Weekly_rate")) + 
                     hour*(myRs.getFloat("Hourly_rate")) ; 
       
       float price1_ = ic =day*(ins_drate)  +  
                        week*(ins_wrate) + 
                        hour*(ins_hrate) ; 
       float price1 = _price1 + price1_;
       System.out.println("Price 1 :"+price1);
       
       float price2 = 0.00f;
           System.out.println("select  EquipName1,EquipName2,EquipQuantity1,EquipQuantity2 from Equipment_details where EquipConf="+equipment+"");
           myRs = myStmt.executeQuery("select EquipName1,EquipName2,EquipQuantity1,EquipQuantity2 from Equipment_details where EquipConf="+equipment+"");
           myRs.next();
           String Equipment1 = myRs.getString("EquipName1");
           String Equipment2 = myRs.getString("EquipName2");
           Integer EquipmentQuantity1 = myRs.getInt("EquipQuantity1");
           Integer EquipmentQuantity2 = myRs.getInt("EquipQuantity2");
           
        String SQL1 = "select * from Additional_equipment where EquipmentName='"+Equipment1+"'";
        String SQL2 = "select * from Additional_equipment where EquipmentName='"+Equipment2+"'";
        
        myRs = myStmt.executeQuery(SQL1); myRs.next();
        float _price2 = EquipmentQuantity1*(day_raw*(myRs.getFloat("Daily_rate")) + hour*(myRs.getFloat("Hourly_rate")));  
        System.out.println("Price estimation Equipment 1 -> "+_price2);
        
        myRs = myStmt.executeQuery(SQL2); myRs.next();
        float price2_  = EquipmentQuantity2*(day_raw*(myRs.getFloat("Daily_rate")) + hour*(myRs.getFloat("Hourly_rate")));  
        System.out.println("Price estimation Equipment 2 -> "+price2_);
           
           
           
           
           price2 = ec = price2_ + _price2;   
       
       System.out.println("Price 2 :"+price2);
       
       float total_cost = price1+price2+price3;
       estimated_cost.setText(String.format("%.2f",total_cost)+"  CAD"); 
       
       if(clubmember==1) {
            float total_points = (float) ((total_cost)/5.0);
            String _sql = "UPDATE ClubMember SET Points = Points +"+total_points+", Amount_spent=Amount_spent+"+total_cost+" WHERE Phone_number='"+customer_phone+"'";  
             System.out.println(_sql);
            myStmt.executeUpdate(_sql);
       }
     
        String sql = "UPDATE Vehicle SET Odometer ="+readingodometer +" WHERE Vlicense="+Vlicense+"";
        System.out.println(sql);
        myStmt.executeUpdate(sql);

        sql = "UPDATE RentalAgreement SET CardNo=NULL,ExpiryDate=NULL,CardType=NULL,Dlicense=NULL  WHERE RentId="+Rent_ID+"";
        System.out.println(sql);
        myStmt.executeUpdate(sql);

       isPriceCalculated = true;
       
      }
       
       
       
       }
   }
   
   
   
   
    public HashMap<String,Long> TotalTime(String Date1,String Date2)  throws ParseException{
       
       final HashMap<String, Long> map = new HashMap<String, Long>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
	Date d1 = null;
	Date d2 = null;
        
        d1 = format.parse(Date1);
        d2 = format.parse(Date2);
        System.out.println("Date 1 = "+Date1);
        System.out.println("\nDate 2 = "+Date2);
	long diff = d2.getTime() - d1.getTime();

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffWeek = diffDays/7;
        long diffdays_raw = diffDays ;

        if( diffWeek >= (long)1) 
            diffDays = diffDays%7;
        else 
            diffWeek = 0;

        map.put("week",diffWeek);
        map.put("day",diffDays);
        map.put("hour",diffHours);
        map.put("minute",diffMinutes);
        map.put("day_raw",diffdays_raw);
       
       return map;
   }
   
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        content_processreturn_section1.setVisible(false);
        //content_processreturn_section2.setVisible(true);
        pane_clubmember.setVisible(false);
        List hour= new ArrayList();
        List minute= new ArrayList();
        
         for(int i=0;i<24;i++) {  
             hour.add(String.format("%02d",i)); 
         }
         for(int i=0;i<60;i++) { 
             minute.add(String.format("%02d",i));       
         }
         
        ObservableList hours = FXCollections.observableList(hour);
        ObservableList minutes= FXCollections.observableList(minute);
        
        hh.setItems(hours);
        mm.setItems(minutes);
        
        List _choice = new ArrayList();
             _choice.add("Phone & Dates");
             _choice.add("Rental Id");
             
        List choice_ = new ArrayList();
             choice_.add("Yes");
             choice_.add("No");
              
         ObservableList RID = FXCollections.observableList(_choice);
         ObservableList TF = FXCollections.observableList(choice_);
         choice_rental_id.setItems(RID);
         combo_tankfull.setItems(TF);
         combo_tankfull.setValue("Yes"); 
         choice_rental_id.setValue("Rental Id");      
         choice =0;  
              
    }    
    
}
