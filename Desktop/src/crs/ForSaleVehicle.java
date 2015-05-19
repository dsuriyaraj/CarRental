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
public class ForSaleVehicle {
    
    private final SimpleStringProperty location;
    private final SimpleStringProperty type;
    private final SimpleIntegerProperty vlicense;
    private final SimpleIntegerProperty price;
    
    public ForSaleVehicle (String location,String type, int vlicense, int price) {
        this.location = new SimpleStringProperty(location);
        this.type = new SimpleStringProperty(type);
        this.vlicense = new SimpleIntegerProperty(vlicense);  
        this.price = new SimpleIntegerProperty(price); 
        //System.out.println("License:"+this.vlicense+" Name:"+this.name+" Year:"+this.year);
    }
    
    public String getLocation() {
        return location.get();
    }
    public String getType() {
        return type.get();
    }
    public Integer getVlicense() {
        return  vlicense.get();
    }
    public Integer getPrice() {
        return  price.get();
    }
    
}
