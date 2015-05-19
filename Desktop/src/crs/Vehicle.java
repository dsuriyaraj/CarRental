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
public class Vehicle {
    private final SimpleIntegerProperty vlicense;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty year;
    
    public Vehicle (int vlicense,String name, int year) {
        this.vlicense = new SimpleIntegerProperty(vlicense);
        this.name = new SimpleStringProperty(name);
        this.year = new SimpleIntegerProperty(year);  
        System.out.println("License:"+this.vlicense+" Name:"+this.name+" Year:"+this.year);
    }
    
    public Integer getVlicense() {
        return vlicense.get();
    }
    public String getName() {
        return name.get();
    }
    public Integer getYear() {
        return year.get();
    }
}
