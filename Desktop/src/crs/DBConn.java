/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ralph
 */
public class DBConn {
   
    /*private static String user = "team06";
    private static String pass = "t3xtb00k";
    private static String url = "jdbc:mysql://dbserver.mss.icics.ubc.ca/team06";*/
    private static String user = "root";
    private static String pass = "";
    private static String url = "jdbc:mysql://localhost:3306/crs";
    
    private static Connection myConn;
    
    public DBConn(){
      
    
    }
    
    public static boolean getConn(){
        try {
            myConn = DriverManager.getConnection(url, user, pass);
            System.out.println("DB Connected");
            return true;
            
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("DB Connect fail");
            return false;
        } 
    
    }

    
    public static ArrayList< HashMap<String, String> > getResult(String sql) {
        ResultSet myRs = null;
        ResultSetMetaData rsmd = null;
        HashMap<String, String> record = null;
        ArrayList< HashMap<String, String> > data = new ArrayList< HashMap<String, String> >();
        if(getConn()){
            try {
                Statement myStmt = myConn.createStatement();
                myRs = myStmt.executeQuery(sql);
                rsmd = myRs.getMetaData();
                while(myRs.next()){
                    record = new HashMap<String, String>();
                    for( int i = 1; i <= rsmd.getColumnCount(); i ++ ){
                        String columnName = rsmd.getColumnName(i).toLowerCase();
                        record.put( columnName, myRs.getString( columnName ) );
                    }
                    data.add( record );
                }
                //System.out.println("result get");  
                
            
            } catch (Exception exc) {
                System.out.println("get result fail");  
                exc.printStackTrace();

            }
            try {            
                myConn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConn.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
            
    }
    
    public static int itemInsert(String sql){
        int count=0;
        if(getConn()){     
            
            try {
                Statement myStmt = myConn.createStatement();
                count = myStmt.executeUpdate(sql);
                //System.out.println("inserted");
                       
            } catch (Exception exc) {
                System.out.println("insertion fail");
                try { 
                    myConn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConn.class.getName()).log(Level.SEVERE, null, ex);
                }
                exc.printStackTrace();
            } 
            try { 
                myConn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConn.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return count;
                
    }
    
    public static int itemUpdate(String sql){
        int count=0;
        if(getConn()){     
            
            try {
                Statement myStmt = myConn.createStatement();
                count = myStmt.executeUpdate(sql);
               // System.out.println("updated");
                       
            } catch (Exception exc) {
                System.out.println("update fail");
                try { 
                    myConn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBConn.class.getName()).log(Level.SEVERE, null, ex);
                }
                exc.printStackTrace();
            } 
            try { 
                myConn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConn.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return count;
                
    }
    
    
    
    
    
    
}
