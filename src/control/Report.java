/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.InventoryJpaController;
import control.ProductJpaController;
import control.SaleJpaController;
import entity.Sale;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JTable;

/**
 *
 * @author magic
 */
public class Report {

    private String str = "null";
    private ResultSet database = null;
    private final EntityManagerFactory emf;
    private final SaleJpaController saleController;
    private SimpleDateFormat sdf;
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public Report() {

        // creation of class variables
        emf = Persistence.createEntityManagerFactory("DP2PU");
        saleController = new SaleJpaController(emf);
        sdf = new SimpleDateFormat(DATE_FORMAT);
    }

    public List<String[]> retrieveDatabase(String month, String year) {

        //creates lists of sales
        List<Sale> results = saleController.getSaleByMonthAndYear(month, year);
        List<String[]> data = new ArrayList<>();

        try {
            String[] columnNames = {"Sales ID", "Product ID", "Qty", "Price", "SaleDate"};
            for (Sale sale : results) {
                data.add(new String[]{
                sale.getSalePK().getSaleId() + "",
                sale.getSalePK().getProdId() + "",
                sale.getSaleQty() + "",
                sale.getSalePrice() + "",
                sdf.format(sale.getSaleDate())
                });
            }
        } catch (Exception e) {
        }
        return data;
    }

    public void monthlyReport(String month, String year) throws IOException, SQLException {
        PrintWriter out = new PrintWriter("output.txt");

        List<String[]> database2 = new ArrayList<>();
        database2 = this.retrieveDatabase(month, year);
        String[] columnNames = {"Sales ID", "Product ID", "Qty", "Price", "SaleDate"};
        out.println("Monthly Report");
        out.println("-----------------------------------------");
        out.println("Sales");
        out.println("-----------------------------------------");
        JTable table = new JTable(database2.toArray(new Object[][]{}), columnNames);
        for (int row = 0; row < table.getRowCount(); row++) {

            for (int col = 0; col < table.getColumnCount(); col++) {
                out.print(table.getColumnName(col));
                out.print(": ");
                out.println(table.getValueAt(row, col));
            }
        }
        out.close();
     
    }
    /*
      try {
         String tempStr = "";
         int rowcount = 0;
         if (database2.last()) {
            rowcount = database2.getRow();
            database2.beforeFirst();
            
            }
        while (database2.next()) {
            for (int i = 0; i < rowcount; i++){
                String val = database2.getString(rowcount);
                out.println(val);
                out.println("success");
            }
            
        }
       }
      
      finally {

         if (out != null) {
            out.close();
         }
         
        }
      return str;
    }*/

}
