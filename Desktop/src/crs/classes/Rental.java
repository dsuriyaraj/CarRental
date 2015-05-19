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
public class Rental {
    public String branchLoc;
    public int branchid;
    public int number;
    public String typename;
    public double amount;

    public String getBranchLoc() {
        return branchLoc;
    }

    public void setBranchLoc(String branchLoc) {
        this.branchLoc = branchLoc;
    }

    public int getBranchid() {
        return branchid;
    }

    public void setBranchid(int branchid) {
        this.branchid = branchid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
