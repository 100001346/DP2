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
import entity.Inventory;
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
    private final InventoryJpaController inventoryController;
    private SimpleDateFormat sdf;
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public Report() {

        // creation of class variables
        emf = Persistence.createEntityManagerFactory("DP2PU");
        saleController = new SaleJpaController(emf);
        inventoryController = new InventoryJpaController(emf);
        sdf = new SimpleDateFormat(DATE_FORMAT);
    }

    
    public List<String[]> retrieveInventoryDatabase() {

        //creates lists of sales
        List<Inventory> results = inventoryController.findInventoryEntities();
        List<String[]> data = new ArrayList<>();
        try {
            String[] columnNames = {"Product ID", "Qty", "InvLow", "InvOrder"};
            for (Inventory inventory : results) {
                data.add(new String[]{
                inventory.getProdId() + "",
                inventory.getInvQty() + "",
                inventory.getInvLow() + "",
                inventory.getInvOrder() + ""
                });
            }
        } catch (Exception e) {
        }
        return data;
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
    
        public List<String[]> retrieveWeeklyDatabase(String week, String year) {

        //creates lists of sales
        List<Sale> results = saleController.getSaleByWeekAndYear(week, year);
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

    public void monthlyReport(String week, String year) throws IOException, SQLException {
        PrintWriter out = new PrintWriter(new File("MonthlyOutput.csv"));

        StringBuilder sb = new StringBuilder();
        List<String[]> database2 = new ArrayList<>();
        database2 = this.retrieveDatabase(week, year);
        String[] columnNames = {"Sales ID", "Product ID", "Qty", "Price", "SaleDate"};
        sb.append("Monthly Report");
        sb.append('\n');
        sb.append("Sales");
        sb.append('\n');
        sb.append("Sales ID");
        sb.append(',');
        sb.append("Product ID");
        sb.append(',');
        sb.append("Qty");
        sb.append(',');
        sb.append("Price");
        sb.append(',');
        sb.append("SaleDate");
        sb.append('\n');
        
        
        JTable table = new JTable(database2.toArray(new Object[][]{}), columnNames);
        for (int row = 0; row < table.getRowCount(); row++) {

            for (int col = 0; col < table.getColumnCount(); col++) {
                sb.append(table.getValueAt(row, col));
                sb.append(',');
            }
            sb.append('\n');
        }
        out.write(sb.toString());
        out.close();
     
    }

   public void weeklyReport(String week, String year) throws IOException, SQLException {
        PrintWriter out = new PrintWriter("WeeklyOutput.txt");

        List<String[]> database2 = new ArrayList<>();
        database2 = this.retrieveWeeklyDatabase(week, year);
        String[] columnNames = {"Sales ID", "Product ID", "Qty", "Price", "SaleDate"};
        out.println("Weekly Report");
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
   
      public void inventoryReport() throws IOException, SQLException {
        PrintWriter out = new PrintWriter("InventoryOutput.txt");

        List<String[]> database2 = new ArrayList<>();
        database2 = this.retrieveInventoryDatabase();
        String[] columnNames = {"Product ID", "Qty", "InvLow", "InvOrder"};
        out.println("Inventory Report");
        out.println("-----------------------------------------");
        out.println("Inventory");
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
    /* WORKING MONTHLY PRINTING
         public void monthlyReport(String week, String year) throws IOException, SQLException {
        PrintWriter out = new PrintWriter("MonthlyOutput.txt");

        List<String[]> database2 = new ArrayList<>();
        database2 = this.retrieveDatabase(week, year);
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
      */
}
