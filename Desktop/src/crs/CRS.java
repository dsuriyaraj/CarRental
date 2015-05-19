package crs;

import crs.classes.Branch;
import crs.classes.Price;
import crs.classes.Rental;
import crs.classes.User;
import crs.classes.Vehicle;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author User
 */
public class CRS extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/inc/screen1.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static DBConn dbc = new DBConn();
    
    /*hannah*/
    public static ArrayList<Branch> getLocationList() {
        
        ArrayList<Branch> result = new ArrayList();
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT * FROM Branch ORDER BY location");
        for( HashMap<String, String> record : data ){
            Branch branch = new Branch();
            branch.BranchID = Integer.parseInt( record.get("branchid") ) ;
            branch.location = record.get("location");
            branch.City = record.get("city");
            result.add(branch);
            
        }
        return result;
    
    
    }
    
    
    
    /*hannah*/
    public static ArrayList<String> getCategoryList(String category) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT DISTINCT Vtype_name  FROM Vehicle WHERE category='"+category+"'");
        //data = dbc.getResult("SELECT Type FROM price WHERE VType = '"+vtype+"'");
        for( HashMap<String, String> record : data ){
            String str = new String(); 
            str = record.get("vtype_name");           
            result.add(str);
            
        }
        return result;
    
    }
    
     /*  Manage vehicles  */
    
      /*****vicky**********/
    public static ArrayList<Vehicle> getVehicleListForRentOrSell(int forRentFlag){
        
        ArrayList<Vehicle> result = new ArrayList<Vehicle>();
        
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        String sql = "SELECT * FROM `Vehicle` A,`Branch` B WHERE forRentFlag = 1 AND B.`BranchID` = A.`BranchID` ORDER BY vtype_name,category,A.`BranchID`";
        if( forRentFlag==0  ){
            sql = "SELECT * FROM `Vehicle` A,`Branch` B ,`ForsaleVehicles` C WHERE C.`Vlicense` = A.`Vlicense` AND  forRentFlag =0 AND B.`BranchID` = A.`BranchID` ORDER BY vtype_name,category,A.`BranchID`";
       
        }
        data = dbc.getResult(sql);
        
        for( HashMap<String, String> record : data ){
            Vehicle vc = new Vehicle(); 
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");
            vc.BranchID = Integer.parseInt( record.get("branchid"));
            vc.Vtype_name = record.get("category");
            vc.category = record.get("vtype_name"); 
            vc.Year = Integer.parseInt( record.get("year"));
            vc.initial_price = Double.parseDouble(record.get("initial_price"));
            vc.forRentFlag = Integer.parseInt( record.get("forrentflag"));
            vc.status = Integer.parseInt( record.get("status"));
            vc.Branch_location = record.get("location");
            if(forRentFlag==0)  vc.selling_price = Double.parseDouble(record.get("saleprice"));
            result.add(vc);          
        }
       
        return result;  
    }
    
    public static double getSellingPrice(int vlicense){
        double price = 0;
    
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT saleprice FROM ForsaleVehicles WHERE Vlicense="+vlicense);
        for( HashMap<String, String> record : data ){
            price = Double.parseDouble(  record.get("saleprice") ) ;
        }
        return price;
    
    }
        
    
    
    /*****vicky**********/
    public static Vehicle getVehicleByID(String license_id){
        
        Vehicle vc = new Vehicle();    
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT * FROM Vehicle WHERE Vlicense="+license_id);
        for( HashMap<String, String> record : data ){
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");
            vc.BranchID = Integer.parseInt( record.get("branchid"));
            vc.Vtype_name =record.get("category");
            vc.category = record.get("vtype_name");
            vc.Year = Integer.parseInt( record.get("year"));
            vc.initial_price = Double.parseDouble( record.get("initial_price"));
            vc.forRentFlag = Integer.parseInt( record.get("forrentflag"));
            vc.status = Integer.parseInt( record.get("status"));
         //   vc.Branch_location = getBranchLocationById(vc.BranchID);          
        }
        return vc;
    }
    

 
 
    
    /*****hannah**********/
    public static ArrayList<Vehicle> getVehicleListByCLY(String vtypename,int locationid,int year){
        
        ArrayList<Vehicle> result = new ArrayList<Vehicle>();
        
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        String sql = "SELECT * FROM `Vehicle` A,`Branch` B WHERE A.`ForRentFlag`=1 AND A.`BranchID` =B.`BranchID` AND A.`Year` <= "+year+" ";
        if( vtypename=="" && locationid<=0 ){
            //both not set
            System.out.println("only year set year:"+year);
        }else if(  vtypename!="" && locationid>0   ){
            //both set
            sql += " AND B.`BranchID` ="+locationid+" AND vtype_name='"+vtypename+"'";
        }else if(  vtypename=="" && locationid>0   ){
            //only location set
            sql += " AND B.`BranchID` ="+locationid;
        }else if(  vtypename!="" && locationid<=0   ){
            //only category set
            sql += " AND Vtype_name='"+vtypename+"'";            
        }
        sql += "  ORDER BY vtype_name,category,B.`BranchID` ";
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Vehicle vc = new Vehicle(); 
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");       
            vc.Vtype_name = record.get("category");
            vc.category = record.get("vtype_name");
            vc.initial_price = Double.parseDouble( record.get("initial_price") );
            vc.BranchID = Integer.parseInt( record.get("branchid"));    
            vc.status = Integer.parseInt( record.get("status"));
            vc.Year = Integer.parseInt( record.get("year"));
            vc.forRentFlag = Integer.parseInt( record.get("forrentflag")); 
            vc.Branch_location =record.get("location");
            result.add(vc);
            
        }
        
        
        return result;  
    
    }
        /**hannah**/
    public static String getBranchLocationById(int bid){
            ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
            data = dbc.getResult("SELECT location FROM Branch WHERE BranchID = "+bid);
            String location="";
            for( HashMap<String, String> record : data ){
                location = record.get("location")  ;           
                //result.add(str);   
            }
        return location;  
    
    
    }
    
 
    
     /*****vicky**********/
       public static int getBranchid(String branch_address) {
        
            //ArrayList<String> result = new ArrayList<String>();
            ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
            data = dbc.getResult("SELECT BranchID FROM Branch WHERE location = '"+branch_address+"'");
            int BranchID = -1;
            for( HashMap<String, String> record : data ){
                BranchID = Integer.parseInt( record.get("branchid") ) ;           
                //result.add(str);   
            }
        return BranchID;  
        }
       
       
       

    /*****vicky**********/
        public static int checkVlicense(int vl){
        int count=0;
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT count(*) number  FROM Vehicle where Vlicense="+vl); 
        for( HashMap<String, String> record : data ){
                count = Integer.parseInt( record.get("number"));                  
        }
        int result = count>0?1:0;
        return result;
    
    }
        
        
       public static int addNewVehicle(Vehicle vc) {
           ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
            data = dbc.getResult("SELECT COUNT(*) AS number FROM Vehicle WHERE vlicense = "+vc.Vlicense);
            int count = -1;
            for( HashMap<String, String> record : data ){
                count = Integer.parseInt( record.get("number") ) ;        
                
            }
            if(count==1) return -2;
            
            count = dbc.itemInsert("INSERT INTO Vehicle (BranchID,Vlicense,Vname,Vtype_name,category,Year,initial_price,status,forRentFlag ) VALUES ("
                +vc.BranchID+","+vc.Vlicense+",'"+vc.Vname+"','"+vc.category +"','"+vc.Vtype_name+"',"+vc.Year+","+vc.initial_price+",1,1)"); 
            return count;
       }

    /***hannah**/
       public static int setToSell(int VID,double price){
            int count = dbc.itemUpdate("UPDATE Vehicle SET forRentFlag = 0,STATUS=0 WHERE Vlicense = "+VID); 
            count+= dbc.itemInsert("INSERT INTO `ForsaleVehicles`(Vlicense,SalePrice,SoldFlag) VALUES("+VID+","+price+",0)");
            return count;
        }
       
       /**hannah****/
        public static int setSold(int VID,String date,double price,String soldTo) {
        //delete this vehicle from table
            int count = dbc.itemUpdate("UPDATE ForsaleVehicles SET SoldFlag=1,SoldPrice="+price+",SoldDate='"+date+"',SoldTo='"+soldTo+"' WHERE Vlicense="+VID);       
            count += dbc.itemUpdate("UPDATE `Vehicle` SET forRentFlag=2 WHERE Vlicense="+VID);
            
            return  count;
        }
       
 
       
    
     /*****vicky**********/
       public static Price getPrice(String type){
       //getprice and return
            Price price = new Price();  
            ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
            data = dbc.getResult("SELECT * FROM VehicleType WHERE vtype_name='"+type+"'");
            for( HashMap<String, String> record : data ){
                price.vtype_name = record.get("vtype_name");
                price.daily_rate = Double.parseDouble( record.get("daily_rate") ) ;   
                price.weekly_rate = Double.parseDouble( record.get("weekly_rate") ) ; 
                price.hourly_rate =  Double.parseDouble( record.get("hourly_rate") ) ; 
                price.km_rate =  Double.parseDouble( record.get("km_rate") ) ; 
                price.ins_wrate =  Double.parseDouble( record.get("ins_wrate") ) ; 
                price.ins_drate =  Double.parseDouble( record.get("ins_drate") ) ; 
                price.ins_hrate = Double.parseDouble( record.get("ins_hrate") ) ;
            }
        //return vc;  
           return price;
       }
       
       
    /*****vicky**********/
    public static int setPrice(Price price){
           //update price of a type
          int count =  dbc.itemUpdate("UPDATE VehicleType SET vtype_name ='"+price.vtype_name+"'"+
                   ",daily_rate="+price.daily_rate+
                   ",weekly_rate="+price.weekly_rate+
                   ",hourly_rate="+price.hourly_rate+
                   ",km_rate="+price.km_rate+
                   ",ins_wrate="+price.ins_wrate+
                   ",ins_drate="+price.ins_drate+
                   ",ins_hrate="+price.ins_hrate+" WHERE vtype_name = '"+price.vtype_name+"'");
          return count;
          
       }

    
    /* manage users************************/
    
    /*****vicky**********/
    public static int addUser(User user){
            
        int count = 0;     
        count = 
        count = dbc.itemInsert("INSERT INTO Employees (Emp_name, Username, Password,Type ) VALUES ('"
               +user.emp_name+"','"+user.username+"','"+user.password+"','"+user.type+"')"); 
        //System.out.println("name"+user.emp_name+" "+user.username+" "+user.password+" "+user.type);
        return count;
    }
    
    public static int checkUserName(String username){
        int count=0;
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT count(*) number  FROM Employees where Username='"+username+"'"); 
        for( HashMap<String, String> record : data ){
                count = Integer.parseInt( record.get("number"));                  
        }
        int result = count>0?-1:0;
        return result;
    
    }
    
    /*****vicky**********/
    public static ArrayList<User> getUserList(){
        ArrayList<User> result = new ArrayList<User>();
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT *  FROM Employees");
        //data = dbc.getResult("SELECT Type FROM price WHERE VType = '"+vtype+"'");
        for( HashMap<String, String> record : data ){
            User user = new User(); 
            user.emp_id = Integer.parseInt( record.get("emp_id") ) ;
            user.emp_name = record.get("emp_name");
            user.password = record.get("password");
            user.username = record.get("username");
            user.type = record.get("type");           
            result.add(user);           
        }
        return result;
    }
     public static ArrayList<User> getUserListByKeyword(String keyword){
        ArrayList<User> result = new ArrayList<User>();
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        data = dbc.getResult("SELECT *  FROM Employees WHERE emp_name LIKE '%"+keyword+"%' OR username LIKE '%"+keyword+"%'");
        //data = dbc.getResult("SELECT Type FROM price WHERE VType = '"+vtype+"'");
        for( HashMap<String, String> record : data ){
            User user = new User(); 
            user.emp_id = Integer.parseInt( record.get("emp_id") ) ;
            user.emp_name = record.get("emp_name");
            user.password = record.get("password");
            user.username = record.get("username");
            user.type = record.get("type");           
            result.add(user);           
        }
        return result;
    }
    
    
    /*****vicky**********/
    public static int updateUser(User user){
        int count =  dbc.itemUpdate("UPDATE Employees SET password = '"+user.password
                +"' WHERE Emp_id = "+user.emp_id);
        return count;
    }
    
    public static int removeUser(int userid){
        int count =  dbc.itemUpdate("Delete from Employees where Emp_id ="+userid);
        return count;
    }
    
    public static String getValidation(User user){
        
        return "";
    
    }
    
    /**********************rental***********************************/
    
   public static ArrayList<Vehicle> getDailyRental(String date,int loc){
        
        ArrayList<Vehicle> result = new ArrayList<Vehicle>();
        
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        String sql = "";
        if(loc<0){
            sql ="SELECT B.`BranchID`,A.`Vlicense`,vname,vtype_name,category,C.`Location` FROM `RentalAgreement` A,`Vehicle` B,`Branch` C WHERE A.`Vlicense` = B.`Vlicense`  AND B.`BranchID`=C.`BranchID`   AND DATE(Pickup_time)='"
                +date+"'   ORDER BY branchid,vtype_name";
        }else{
             sql ="SELECT A.`Vlicense`,vname,vtype_name,category,location FROM `RentalAgreement` A,`Vehicle` B ,`Branch` C  WHERE A.`Vlicense` = B.`Vlicense` AND B.`BranchID`=C.`BranchID`  AND DATE(Pickup_time)='"
                +date+"' AND B.`BranchID`="+loc+"   ORDER BY vtype_name";
        }
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Vehicle vc = new Vehicle(); 
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");       
            vc.Vtype_name = record.get("category");
            vc.category = record.get("vtype_name");
            if(loc<0)  vc.BranchID = Integer.parseInt( record.get("branchid"));    
            vc.Branch_location = record.get("location");
            result.add(vc);
            
        }
        
        
        return result;  
    
    }
   
   
     
   public static HashMap<String, Integer> getDailyRentalByCategory(String date){
        HashMap<String, Integer>  result = new HashMap<String, Integer> ();  
       
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        String sql = "SELECT vtype_name,COUNT(*) AS number FROM `RentalAgreement` A,`Vehicle` B  WHERE A.`Vlicense` = B.`Vlicense`  AND DATE(Pickup_time)='"
                +date+"'  GROUP BY vtype_name";
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            String category = record.get("vtype_name");
            int number = Integer.parseInt( record.get("number") ) ;
            result.put(category, number);            
        }
        
        
       return result;
   }
   
      public static ArrayList<Rental> getDailyRentalByBranch(String date){
        ArrayList<Rental> result = new ArrayList<Rental>();  
       
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        
        String sql = "SELECT C.`BranchID`,COUNT(*) AS number FROM `RentalAgreement` A,`Vehicle` B ,`Branch` C  WHERE A.`Vlicense` = B.`Vlicense` AND B.`BranchID`=C.`BranchID` AND DATE(Pickup_time)='"+
                date+"'  GROUP BY C.`BranchID`";
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Rental rental = new Rental();
            rental.branchLoc = getBranchLocationById( Integer.parseInt( record.get("branchid")  ) );
            rental.number =   Integer.parseInt( record.get("number"));
            result.add(rental);
        }
        
        
       return result;
   }
   
   
        public static ArrayList<Rental> getRentalByCategory(String date){
        ArrayList<Rental> result = new ArrayList<Rental>();  
       
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        String sql = "SELECT B.`Vtype_name`,COUNT(*) AS number FROM `RentalAgreement` A,`Vehicle` B  WHERE A.`Vlicense` = B.`Vlicense`  AND DATE(Pickup_time)='"+
                date+"'  GROUP BY B.`Vtype_name`";
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Rental rental = new Rental();
            rental.typename = record.get("vtype_name")   ;
            rental.number =   Integer.parseInt( record.get("number"));
            result.add(rental);
        }
        
        
       return result;
   }
      
      
   
   
   
   
   
   /****************************return***********************/
   
   public static ArrayList<Vehicle> getDailyReturn(String date,int loc){
        
        ArrayList<Vehicle> result = new ArrayList<Vehicle>();
        
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        String sql = "";
        if(loc<0){
            sql ="SELECT A.`Vlicense`,B.`BranchID`,vname,vtype_name,category,C.`Cost`,location  FROM `RentalAgreement` A,`Vehicle` B ,`returnvehicle` C,`Branch` D  WHERE A.`Vlicense` = B.`Vlicense`  AND B.`BranchID`=D.`BranchID` AND c.`RentId`=A.`RentId`   AND DATE(C.`Dropoff_time`)='"
                    +date+"'   ORDER BY vtype_name,B.`BranchID`";
        }else{
                sql ="SELECT A.`Vlicense`,vname,vtype_name,category,C.`Cost`,location  FROM `RentalAgreement` A,`Vehicle` B ,`returnvehicle` C,`Branch` D  WHERE A.`Vlicense` = B.`Vlicense` AND B.`BranchID`=D.`BranchID` AND c.`RentId`=A.`RentId`   AND DATE(C.`Dropoff_time`)='"
                       +date+"' AND B.`BranchID`="+loc+"  ORDER BY vtype_name";
        }
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Vehicle vc = new Vehicle(); 
            vc.Vlicense = Integer.parseInt( record.get("vlicense") ) ;
            vc.Vname = record.get("vname");       
            vc.Vtype_name = record.get("category");
            vc.category = record.get("vtype_name");
            if(loc<0)  vc.BranchID = Integer.parseInt( record.get("branchid"));    
            vc.Branch_location =  record.get("location");
            vc.initial_price = Double.parseDouble(  record.get("cost") );
            result.add(vc);
            
        }
        
        
        return result;  
    
    }
   
   
 
      public static ArrayList<Rental> getDailyReturnByBranch(String date){
        ArrayList<Rental> result = new ArrayList<Rental>();  
       
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        
        String sql = "SELECT branchid,COUNT(*) AS number, SUM(C.`Cost`) AS cost FROM `RentalAgreement` A,`Vehicle` B ,`returnvehicle` C  WHERE A.`Vlicense` = B.`Vlicense`  AND C.`RentId` = A.`RentId` AND DATE(C.`Dropoff_time`)='"
                +date+"'  GROUP BY branchid";
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Rental rental = new Rental();
            rental.branchLoc = getBranchLocationById( Integer.parseInt( record.get("branchid")  ) );
            rental.number =   Integer.parseInt( record.get("number"));
            rental.amount = Double.parseDouble(record.get("cost"));
            result.add(rental);
        }
        
        
       return result;
   }
      
          public static ArrayList<Rental> getReturnByCategory(String date){
        ArrayList<Rental> result = new ArrayList<Rental>();  
       
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        
        String sql = "SELECT B.`Vtype_name`,COUNT(*) AS number, SUM(C.`Cost`) AS cost FROM `RentalAgreement` A,`Vehicle` B ,`returnvehicle` C  WHERE A.`Vlicense` = B.`Vlicense`  AND C.`RentId` = A.`RentId` AND DATE(C.`Dropoff_time`)='"
                +date+"'  GROUP BY B.`Vtype_name`";
        data = dbc.getResult(sql);
        for( HashMap<String, String> record : data ){
            Rental rental = new Rental();
            rental.typename = record.get("vtype_name");
            rental.number =   Integer.parseInt( record.get("number"));
            rental.amount = Double.parseDouble(record.get("cost"));
            result.add(rental);
        }
        
        
       return result;
   }  
      

    
       
}