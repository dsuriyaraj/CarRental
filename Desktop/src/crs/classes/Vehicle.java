/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs.classes;

import crs.DBConn;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ralph
 */
public class Vehicle {
    public int Vlicense;
    public String Vname;
    public String Vtype_name;
    public String category;
    public int Year;
    public double initial_price;
    public int BranchID;
    public int forRentFlag;
    public int status;
    public String Branch_location;
    public double selling_price;

    public Vehicle() {
    }

    public Vehicle(int Vlicense, String Vname, String Vtype_name, String category, int Year, double initial_price, int BranchID, int forRentFlag, int status) {
        this.Vlicense = Vlicense;
        this.Vname = Vname;
        this.Vtype_name = Vtype_name;
        this.category = category;
        this.Year = Year;
        this.initial_price = initial_price;
        this.BranchID = BranchID;
        this.forRentFlag = forRentFlag;
        this.status = status;
    }

    public int getVlicense() {
        return Vlicense;
    }

    public void setVlicense(int Vlicense) {
        this.Vlicense = Vlicense;
    }

    public String getVname() {
        return Vname;
    }

    public void setVname(String Vname) {
        this.Vname = Vname;
    }

    public String getVtype_name() {
        return Vtype_name;
    }

    public void setVtype_name(String Vtype_name) {
        this.Vtype_name = Vtype_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int Year) {
        this.Year = Year;
    }

    public double getInitial_price() {
        return initial_price;
    }

    public void setInitial_price(double initial_price) {
        this.initial_price = initial_price;
    }

    public int getBranchID() {
        return BranchID;
    }

    public void setBranchID(int BranchID) {
        this.BranchID = BranchID;
    }

    public int getForRentFlag() {
        return forRentFlag;
    }

    public void setForRentFlag(int forRentFlag) {
        this.forRentFlag = forRentFlag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBranch_location() {
        return Branch_location;
    }

    public void setBranch_location(String Branch_location) {
        this.Branch_location = Branch_location;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

  
    
}
