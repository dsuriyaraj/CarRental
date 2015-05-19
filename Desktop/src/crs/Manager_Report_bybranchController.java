/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crs;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static crs.CRS.getBranchid;
import static crs.CRS.getDailyRentalByBranch;
import static crs.CRS.getDailyRentalByCategory;
import static crs.CRS.getLocationList;
import static crs.CRS.getRentalByCategory;
import static crs.Manager_DailyReturnController.Vlist;
import static crs.Manager_Report_DailyController.Vlist;
import crs.classes.Branch;
import crs.classes.Rental;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;

/**
 * FXML Controller class
 *
 * @author Ralph
 */
public class Manager_Report_bybranchController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setLocation(combo_loc); 
        label_result.setLayoutX(button_download.getLayoutX());
        label_result.setLayoutY(button_download.getLayoutY()+40 );
    }   
    @FXML
    private ComboBox combo_loc;   
    @FXML
    private DatePicker report_datepicker;
    @FXML
    private TableView table_vehicle;
    @FXML
    private TableView table_byloc;  
    @FXML
    private TableView table_bycategory;
    @FXML
    private BarChart barchart_dailyreport;
    @FXML
    private CategoryAxis Caxis_dailyreport;
    @FXML
    private NumberAxis Naxis_dailyreport;
    
    @FXML
    private Button button_download;
    @FXML
    private Button button_summary;
    @FXML
    private Label label_result;
    @FXML
    private TextField totalField;  
    @FXML
    private Pane pane_summary; 
    
    static ArrayList<Rental> cdata;
    static ArrayList<crs.classes.Vehicle> Vlist;
    String date;
    int bid=-1;
    
    
     private void setLocation(ComboBox cmb){
        ArrayList<Branch> list = new ArrayList<Branch>();
        list = getLocationList();
        ObservableList<String> options =  FXCollections.observableArrayList();  
        
        for(int i=0; i<list.size()  ;i++){
            options.add( list.get(i).location+"" );
        }
        
        cmb.setItems(options);   
    
    }
     
     
    @FXML
    private void  onChangeDate(ActionEvent event){
        date = report_datepicker.getValue().toString();
        if( bid<0 ) return;
        table_vehicle.setVisible(true);
        pane_summary.setVisible(false);
        Vlist = CRS.getDailyRental(date,bid);         
        cdata = getRentalByCategory(date);
    }
    
    
    @FXML
    private void  onCombo(ActionEvent event){
        bid = getBranchid(    combo_loc.getValue().toString() )  ;
        if( date.isEmpty() ) return;
        table_vehicle.setVisible(true);
        pane_summary.setVisible(false);
        Vlist = CRS.getDailyRental(date,bid);         
        cdata = getRentalByCategory(date);
    }
    
    @FXML
    private void  onView(ActionEvent event){
        if( report_datepicker.getValue()==null || combo_loc.getValue()==null ) return;
        String date = report_datepicker.getValue().toString();
         bid = getBranchid(    combo_loc.getValue().toString() )  ;
        Vlist = CRS.getDailyRental(date,bid);         
        cdata = getRentalByCategory(date);
        table_vehicle.setVisible(true);
        pane_summary.setVisible(false);
        
        setTable(Vlist,table_vehicle);
        totalField.setText(Vlist.size()+"" );
        

    }
    @FXML
    private void  onSummary(ActionEvent event){
        if( report_datepicker.getValue()==null || combo_loc.getValue()==null ) return;
        
        table_vehicle.setVisible(false);  
        String date = report_datepicker.getValue().toString();
         bid = getBranchid(    combo_loc.getValue().toString() )  ;
        Vlist = CRS.getDailyRental(date,bid);         
        cdata = getRentalByCategory(date);
        pane_summary.setVisible(true);
        pane_summary.setLayoutX(80);
        pane_summary.setLayoutY(170);      
       // HashMap<String, Integer>  data = getDailyRentalByCategory(  date  );
        //setChart(data,barchart_dailyreport,Caxis_dailyreport,Naxis_dailyreport);

        setTableC(cdata,table_bycategory);
        setTable(Vlist,table_vehicle);
        totalField.setText(Vlist.size()+"" );
        
    }
    
      @FXML
    private void onDownload(ActionEvent event){
        if( report_datepicker.getValue()==null || combo_loc.getValue()==null ) return;
        String date = report_datepicker.getValue().toString();
         bid = getBranchid(    combo_loc.getValue().toString() )  ;
        Vlist = CRS.getDailyRental(date,bid);         
        cdata = getRentalByCategory(date);
        String filename = report_datepicker.getValue().toString()+"_DailyRental_"+ combo_loc.getValue().toString() +".pdf";
                DirectoryChooserBuilder builder = DirectoryChooserBuilder.create();
                builder.title("Download report to");
                String cwd = System.getProperty("user.dir");
                File file = new File(cwd);
                builder.initialDirectory(file);
                DirectoryChooser chooser = builder.build();
                File chosenDir = chooser.showDialog(null);
                if (chosenDir != null) {
                  System.out.println(chosenDir.getAbsolutePath());
                } else {
                 System.out.print("no directory chosen");
                }
        
                
                
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(chosenDir.getAbsolutePath()+"/"+ filename));
          
            document.open();
            Paragraph paragraph = new Paragraph();
            paragraph.add("Daily Report of "+ report_datepicker.getValue().toString()+"  Location: "+combo_loc.getValue().toString() );
            paragraph.setAlignment(Element.ALIGN_CENTER); 
            paragraph.setSpacingAfter(40); 
            document.add(paragraph);
            document.add(createPDFTableVehicle(Vlist));
            paragraph.clear();
            paragraph.add("  ");
            paragraph.setSpacingAfter(20); 
            document.add(paragraph);
            document.add(createPDFTableByCategory(cdata));
            
            document.close();
            label_result.setText("Download successfully");
            label_result.setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Manager_ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(Manager_ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static PdfPTable createPDFTableVehicle(ArrayList<crs.classes.Vehicle> list){
    
     PdfPTable table = new PdfPTable(4);
   
        PdfPCell cell; 
        // now we add a cell with rowspan 2
        /*cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);*/
        // we add the four remaining cells with addCell()
        table.addCell("Vlicense");
        table.addCell("Vname");
        table.addCell("Category");
        table.addCell("Vtype name");
        for( int i=0;i<list.size();i++ ){           
            table.addCell(  list.get(i).Vlicense+"" );
            table.addCell(  list.get(i).Vname+"" );
            table.addCell(  list.get(i).Vtype_name+"" );
            table.addCell(  list.get(i).category+"" );
        }
        return table;
    }
    
    
    
    public static PdfPTable createPDFTableByCategory(ArrayList<crs.classes.Rental> list){    
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell; 
        table.addCell("Category");
        table.addCell("Number");
        for( int i=0;i<list.size();i++ ){           
            table.addCell(  list.get(i).typename );
            table.addCell(  list.get(i).number+"" );
        }
        return table;
    }
    
 
    
     private void setTable(ArrayList<crs.classes.Vehicle> list,TableView<crs.classes.Vehicle> table){
        
        TableColumn<crs.classes.Vehicle,String> VIDCol = new TableColumn<>("Vlicense");
        VIDCol.setMinWidth(90);
        TableColumn<crs.classes.Vehicle,String> VtypeCol = new TableColumn<>("category");
        VtypeCol.setMinWidth(70); 
        TableColumn<crs.classes.Vehicle,String> typeCol = new TableColumn<>("Vtype_name");
        typeCol.setMinWidth(90); 
        TableColumn<crs.classes.Vehicle,String> nameCol = new TableColumn<>("Vname");
        nameCol.setMinWidth(90); 
      
        
        table.getColumns().clear();        
        table.getColumns().addAll(VIDCol,nameCol,VtypeCol,typeCol);
       if(list.size()==0){
        return;
       }
        ObservableList<crs.classes.Vehicle> myData = FXCollections.observableArrayList();
        for(int i=0; i<list.size();i++){
            myData.add(list.get(i));
        }
   
        VIDCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("Vlicense"));
        VtypeCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("Vtype_name"));
        typeCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("category"));
        nameCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Vehicle, String>("Vname"));    

        table.setItems(myData);
        
 
    }
    

    
    private void setTableC(ArrayList<Rental> list,TableView table){
        
        TableColumn<crs.classes.Rental,String> branchCol = new TableColumn<>("category");
        branchCol.setMinWidth(90);
        TableColumn<crs.classes.Rental,String> numberCol = new TableColumn<>("number");
        numberCol.setMinWidth(70); 
      
        
        table.getColumns().clear();        
        table.getColumns().addAll(branchCol,numberCol);
       if(list.size()==0){
        return;
       }
        ObservableList<crs.classes.Rental> myData = FXCollections.observableArrayList();
        for(int i=0; i<list.size();i++){
            myData.add(list.get(i));
        }
   
        branchCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Rental, String>("typename"));
        numberCol.setCellValueFactory(
                new PropertyValueFactory<crs.classes.Rental, String>("number"));    

        table.setItems(myData);

    }
    
    
    
    
    
}
