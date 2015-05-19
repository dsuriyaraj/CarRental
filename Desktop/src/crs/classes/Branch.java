/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs.classes;

/**
 *
 * @author Ralph
 */
public class Branch {
    public int BranchID;
    public String location;
    public String City;

    public Branch(){
    
    }
    public Branch(int BranchID, String Address, String City) {
        this.BranchID = BranchID;
        this.location = Address;
        this.City = City;
    
    }

    public int getBranchID() {
        return BranchID;
    }

    public void setBranchID(int BranchID) {
        this.BranchID = BranchID;
    }

    public String getAddress() {
        return location;
    }

    public void setAddress(String Address) {
        this.location = Address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

}
