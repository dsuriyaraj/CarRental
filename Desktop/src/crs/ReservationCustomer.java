/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Bhupinder
 */
public class ReservationCustomer {
    
    private final SimpleIntegerProperty confno;
    private final SimpleIntegerProperty vlicense;
    private final SimpleIntegerProperty branchid;
    private final SimpleStringProperty  vtypename;
    private final SimpleStringProperty  pickup;
    private final SimpleStringProperty  dropoff;
    
    public ReservationCustomer (int confno,int vlicense,int branchid,String vtypename, String pickup,String dropoff) {
        this.confno =        new SimpleIntegerProperty(confno);
        this.vlicense =      new SimpleIntegerProperty(vlicense);
        this.branchid =      new SimpleIntegerProperty(branchid);
        this.vtypename =    new SimpleStringProperty(vtypename);
        this.dropoff =  new SimpleStringProperty(dropoff); 
        this.pickup =   new SimpleStringProperty(pickup);   
      //  System.out.println("ConfNo :"+this.ConfNo+" Vlicense :"+this.Vlicense+" BranchID :"+this.BranchID+" Vtype_name :"+this.Vtype_name+" DropOff :"+this.Dropoff+" PickUp :"+this.Pickup );
    }
    
    public Integer getConfno()      {return confno.get();}
    public Integer getVlicense()    {return vlicense.get();}
    public Integer getBranchid()    {return branchid.get();}
    public String getVtypename()   {return vtypename.get(); }
    public String getPickup()  {return pickup.get();}
    public String getDropoff() {return dropoff.get();}
    
}
