/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.Color;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.modelmbean.ModelMBean;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import util.DbCon;

/**
 *
 * @author user
 */
public class DashBoard extends javax.swing.JFrame {

    DbCon con = new DbCon();
    PreparedStatement ps;
    String sql = "";
    ResultSet rs;

    LocalDate currentDate = LocalDate.now();
    java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);

    public DashBoard() {
        initComponents();
        getAllProduct();
        getProductNameToCombo();
        getAllPurchase();
        todayPurchaseReport();
        totalPurchaseReport();
        todaySalesReport();
        monthlySalesReport();
    }

    public void todaySalesReport() {

        sql = "select sum(actualPrice) from sales where salesDate=?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, sqlDate);

            rs = ps.executeQuery();
            
            while(rs.next()){
                Float totalPrice=rs.getFloat("sum(actualPrice)");
                lblTodaySales.setText(totalPrice+ "");                
            }


        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    public void monthlySalesReport() {
      
        
//        System.out.println(sqlDate.toString().substring(0, 8));
        sql = "select sum(actualPrice) from sales where salesDate like ?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, sqlDate.toString().substring(0, 8)+"%");

            rs = ps.executeQuery();
            
            while(rs.next()){
                Float totalPrice=rs.getFloat("sum(actualPrice)");
                lblTotalSales.setText(totalPrice+ "");                
            }


        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void todayPurchaseReport() {

        sql = "select sum(totalPrice) from purcahse where date=?";
        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, sqlDate);

            rs = ps.executeQuery();
            
            while(rs.next()){
                Float totalPrice=rs.getFloat("sum(totalPrice)");
                lblTodayPurchase.setText(totalPrice+ "");                
            }


        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    public void totalPurchaseReport() {

        sql = "select sum(totalPrice) from purcahse";
        try {
            ps = con.getCon().prepareStatement(sql);
            

            rs = ps.executeQuery();
            
            while(rs.next()){
                Float totalPrice=rs.getFloat("sum(totalPrice)");
                lblTotalPurchase.setText(totalPrice+ "");                
            }


        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    String[] productColumns = {"Product ID", "Product Name", "Product Catagory", "Product Code"};
    String[] purchaseColumns = {"Purchase ID", "Product Name", "UnitPrice", "Qunatity", "Total Prices", "Date"};

    public void updateStockSales() {

        sql = "update stock set quantity=quantity-? where pName= ?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, Float.parseFloat(txtSalesQuantity.getText().trim()));
            ps.setString(2, txtSalesProductName.getSelectedItem().toString());

            ps.executeUpdate();

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateStockPurchase() {

        sql = "update stock set quantity=quantity+? where pName= ?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setFloat(1, Float.parseFloat(txtPurcahseQuantity.getText().trim()));
            ps.setString(2, comboPurchaseProductName.getSelectedItem().toString());

            ps.executeUpdate();

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addProductToStock() {
        sql = "insert into stock(pName, quantity) values(?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setFloat(2, 0.0f);

            ps.executeUpdate();

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getProductNameToCombo() {

        sql = "select name from product";
        comboPurchaseProductName.removeAllItems();
        txtSalesProductName.removeAllItems();

        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");

                comboPurchaseProductName.addItem(name);
                txtSalesProductName.addItem(name);

            }

            rs.close();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getAllProduct() {

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(productColumns);

        tblProduct.setModel(model);

        sql = "select * from product";

        try {

            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("productId");
                String name = rs.getString("name");
                String catagory = rs.getString("catagory");
                String code = rs.getString("productCode");

                model.addRow(new Object[]{id, name, catagory, code});

            }
            rs.close();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getAllPurchase() {

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(purchaseColumns);

        tblPurchase.setModel(model);

        sql = "select * from purcahse";

        try {

            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("purcahseId");
                String name = rs.getString("name");
                float quantity = rs.getFloat("quantity");
                float unitprice = rs.getFloat("unitPrice");
                float totalPrice = rs.getFloat("totalPrice");
                java.util.Date date = rs.getDate("date");

                model.addRow(new Object[]{id, name, unitprice, quantity, totalPrice, date});

            }
            rs.close();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates new form DashBoard
     */
    // calculate total Price 
    public float getTotalPrice() {

        float unitPrice = Float.parseFloat(txtSalesUnitPrice.getText().trim());
        float quantity = Float.parseFloat(txtSalesQuantity.getText().trim());

        float totalPrice = unitPrice * quantity;

        return totalPrice;
    }

    public float getActualPrice() {
        float discount = 0.0f;
        float totalPrice = getTotalPrice();
        String discountStr = txtSalesDicount.getText().trim();

        discount = Float.parseFloat(discountStr);
        float discountamount = totalPrice * discount / 100;

        float actualPrice = totalPrice - discountamount;

        return actualPrice;

    }

    // First Date is Java.SQL date and parameter date is Util date
    public java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }

        return null;
    }

    public float getDiscountAmount() {

        return getTotalPrice() - getActualPrice();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnReport = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        btnSales = new javax.swing.JButton();
        btnPurchase = new javax.swing.JButton();
        btnProduct = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        menu = new javax.swing.JTabbedPane();
        home = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        lblTodayPurchase = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lblTodaySales = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        lblTodayDue = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        lblTotalPurchase = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        lblTotalSales = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        lblTotalDues = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        sales = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtSalesSID = new javax.swing.JTextField();
        txtSalesUnitPrice = new javax.swing.JTextField();
        txtSalesQuantity = new javax.swing.JTextField();
        txtSalesDicount = new javax.swing.JTextField();
        txtSalesActualPice = new javax.swing.JTextField();
        txtSalesCashReceived = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        txtSalesTotalPrice = new javax.swing.JTextField();
        txtSalesDueAmount = new javax.swing.JTextField();
        btnSalesSubmit = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtSalesProductName = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCheckout = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnSalesAddToCart = new javax.swing.JButton();
        salesdate = new com.toedter.calendar.JDateChooser();
        purchase = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtPurcahseProductId = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtPurcahseUnitPrice = new javax.swing.JTextField();
        txtPurcahseQuantity = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtPurcahseTotalPrice = new javax.swing.JTextField();
        btnPurchaseSave = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPurchase = new javax.swing.JTable();
        comboPurchaseProductName = new javax.swing.JComboBox<>();
        purchasedate = new com.toedter.calendar.JDateChooser();
        product = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtProductId = new javax.swing.JTextField();
        txtProductName = new javax.swing.JTextField();
        txtProductCode = new javax.swing.JTextField();
        btnProductSave = new javax.swing.JButton();
        btnProductUpdate = new javax.swing.JButton();
        btnProductDelete = new javax.swing.JButton();
        btnProductReset = new javax.swing.JButton();
        txtProductCatagory = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();
        report = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        radioReportPurchase = new javax.swing.JRadioButton();
        radioReportSales = new javax.swing.JRadioButton();
        radioReportStock = new javax.swing.JRadioButton();
        btnReportView = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblReport = new javax.swing.JTable();
        reportdatefrom = new com.toedter.calendar.JDateChooser();
        reportdateto = new com.toedter.calendar.JDateChooser();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(242, 97, 52));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 3, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CCSL/JEE");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 90));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 90));

        jPanel2.setBackground(new java.awt.Color(242, 220, 175));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnReport.setText("Report");
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        jPanel2.add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 210, 70));

        btnHome.setText("Home");
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHomeMouseExited(evt);
            }
        });
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });
        jPanel2.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 70));

        btnSales.setText("Sales");
        btnSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalesMouseExited(evt);
            }
        });
        btnSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalesActionPerformed(evt);
            }
        });
        jPanel2.add(btnSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 210, 70));

        btnPurchase.setText("Purchase");
        btnPurchase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPurchaseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPurchaseMouseExited(evt);
            }
        });
        btnPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchaseActionPerformed(evt);
            }
        });
        jPanel2.add(btnPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 210, 70));

        btnProduct.setText("Product");
        btnProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductActionPerformed(evt);
            }
        });
        jPanel2.add(btnProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 210, 70));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 210, 590));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(199, 198, 179));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Home");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, 0, 990, 70));

        lblTodayPurchase.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTodayPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodayPurchase.setText("0.00");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Today Purchase");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTodayPurchase, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(0, 95, Short.MAX_VALUE)
                .addComponent(lblTodayPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 67, Short.MAX_VALUE)))
        );

        lblTodaySales.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTodaySales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodaySales.setText("0.00");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Today Sales");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTodaySales, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 95, Short.MAX_VALUE)
                .addComponent(lblTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 67, Short.MAX_VALUE)))
        );

        lblTodayDue.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTodayDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTodayDue.setText("0.00");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Today Due");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTodayDue, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 83, Short.MAX_VALUE)
                .addComponent(lblTodayDue, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 67, Short.MAX_VALUE)))
        );

        lblTotalPurchase.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTotalPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalPurchase.setText("0.00");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Total Purchase");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTotalPurchase, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 83, Short.MAX_VALUE)
                .addComponent(lblTotalPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 67, Short.MAX_VALUE)))
        );

        lblTotalSales.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTotalSales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalSales.setText("0.00");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Total Sales");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTotalSales, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addGap(0, 83, Short.MAX_VALUE)
                .addComponent(lblTotalSales, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel20Layout.createSequentialGroup()
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 67, Short.MAX_VALUE)))
        );

        lblTotalDues.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTotalDues.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalDues.setText("0.00");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Total Due");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTotalDues, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 71, Short.MAX_VALUE)
                .addComponent(lblTotalDues, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 67, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(homeLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 138, Short.MAX_VALUE))
        );

        menu.addTab("tab1", home);

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Sales");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 60));

        jLabel4.setText("SID");

        jLabel5.setText("Product Name");

        jLabel6.setText("Unit Price");

        jLabel7.setText("Quantity");

        jLabel8.setText("Total Price");

        jLabel9.setText("Discount");

        jLabel10.setText("Actual Price");

        jLabel11.setText("Cash Received");

        jLabel12.setText("Due Amount");

        jLabel13.setText("Date");

        jLabel14.setText("Customer Name");

        txtSalesUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesUnitPriceFocusLost(evt);
            }
        });

        txtSalesQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesQuantityFocusLost(evt);
            }
        });

        txtSalesDicount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSalesDicountFocusGained(evt);
            }
        });
        txtSalesDicount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSalesDicountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSalesDicountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSalesDicountKeyTyped(evt);
            }
        });

        txtSalesCashReceived.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalesCashReceivedFocusLost(evt);
            }
        });

        btnSalesSubmit.setText("Submit");
        btnSalesSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesSubmitMouseClicked(evt);
            }
        });

        jButton3.setText("Reset");

        tblCheckout.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblCheckout);

        jButton1.setText("Checkout");

        jButton2.setText("Delete");

        btnSalesAddToCart.setText("Add to Cart");
        btnSalesAddToCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalesAddToCartMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSalesSID)
                                    .addComponent(txtSalesProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(49, 49, 49)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtSalesDueAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                    .addComponent(txtSalesCashReceived, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSalesActualPice, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSalesDicount)
                                    .addComponent(txtSalesUnitPrice, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSalesQuantity, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSalesTotalPrice, javax.swing.GroupLayout.Alignment.LEADING))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8)
                                .addComponent(jLabel9)
                                .addComponent(jLabel10)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(72, 72, 72)))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(salesdate, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(242, 242, 242))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(btnSalesSubmit)
                .addGap(18, 18, 18)
                .addComponent(btnSalesAddToCart)
                .addGap(31, 31, 31)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addComponent(jButton2)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtSalesSID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtSalesProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSalesUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSalesQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSalesTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSalesDicount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSalesActualPice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtSalesCashReceived, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtSalesDueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(salesdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSalesSubmit)
                            .addComponent(jButton3)
                            .addComponent(btnSalesAddToCart))))
                .addContainerGap(95, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout salesLayout = new javax.swing.GroupLayout(sales);
        sales.setLayout(salesLayout);
        salesLayout.setHorizontalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        salesLayout.setVerticalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab2", sales);

        jPanel13.setBackground(new java.awt.Color(0, 102, 102));
        jPanel13.setForeground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Purchase");
        jPanel13.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 73));

        jLabel21.setText("Purchase ID");

        jLabel22.setText("Product Name");

        jLabel23.setText("Quantiry");

        jLabel24.setText("Unit Price");

        jLabel25.setText("TotalPrice");

        jLabel26.setText("Date");

        btnPurchaseSave.setText("Save");
        btnPurchaseSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPurchaseSaveMouseClicked(evt);
            }
        });

        jButton4.setText("Update");

        jButton5.setText("Delete");

        jButton6.setText("Reset");

        tblPurchase.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblPurchase);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPurcahseQuantity)
                            .addComponent(txtPurcahseTotalPrice)
                            .addComponent(txtPurcahseUnitPrice)
                            .addComponent(comboPurchaseProductName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(txtPurcahseProductId, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(purchasedate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPurchaseSave)
                            .addComponent(jButton5))
                        .addGap(77, 77, 77)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6)
                            .addComponent(jButton4))))
                .addGap(138, 138, 138)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtPurcahseProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(comboPurchaseProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPurcahseUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtPurcahseQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtPurcahseTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(purchasedate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPurchaseSave)
                            .addComponent(jButton4))
                        .addGap(49, 49, 49)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton6))))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout purchaseLayout = new javax.swing.GroupLayout(purchase);
        purchase.setLayout(purchaseLayout);
        purchaseLayout.setHorizontalGroup(
            purchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        purchaseLayout.setVerticalGroup(
            purchaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchaseLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab3", purchase);

        jPanel7.setBackground(new java.awt.Color(0, 153, 204));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Product");
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 70));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setText("Product ID");

        jLabel17.setText("Product Name");

        jLabel18.setText("Catagory");

        jLabel19.setText("Product Code");

        btnProductSave.setText("Save");
        btnProductSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductSaveMouseClicked(evt);
            }
        });
        btnProductSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductSaveActionPerformed(evt);
            }
        });

        btnProductUpdate.setText("Update");
        btnProductUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductUpdateMouseClicked(evt);
            }
        });
        btnProductUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductUpdateActionPerformed(evt);
            }
        });

        btnProductDelete.setText("Delete");
        btnProductDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProductDeleteMouseClicked(evt);
            }
        });
        btnProductDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductDeleteActionPerformed(evt);
            }
        });

        btnProductReset.setText("Reset");

        txtProductCatagory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mouse", "Key Board", "Monitor", "Printer", "Mother Board", "Ram" }));

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduct);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel12Layout.createSequentialGroup()
                                    .addGap(39, 39, 39)
                                    .addComponent(txtProductId))
                                .addGroup(jPanel12Layout.createSequentialGroup()
                                    .addGap(36, 36, 36)
                                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 3, Short.MAX_VALUE))))
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel18)
                                .addComponent(jLabel19))
                            .addGap(40, 40, 40)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtProductCode)
                                .addGroup(jPanel12Layout.createSequentialGroup()
                                    .addComponent(txtProductCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnProductSave)
                            .addComponent(btnProductDelete))
                        .addGap(96, 96, 96)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnProductReset)
                            .addComponent(btnProductUpdate))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(txtProductCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtProductCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProductSave)
                            .addComponent(btnProductUpdate))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProductDelete)
                            .addComponent(btnProductReset))))
                .addContainerGap(238, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout productLayout = new javax.swing.GroupLayout(product);
        product.setLayout(productLayout);
        productLayout.setHorizontalGroup(
            productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        productLayout.setVerticalGroup(
            productLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        menu.addTab("tab4", product);

        jPanel8.setBackground(new java.awt.Color(153, 153, 153));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Report");
        jPanel8.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1075, 75));

        jLabel28.setText("From");

        jLabel29.setText("To");

        buttonGroup1.add(radioReportPurchase);
        radioReportPurchase.setText("Purchase");

        buttonGroup1.add(radioReportSales);
        radioReportSales.setText("Sales");

        buttonGroup1.add(radioReportStock);
        radioReportStock.setText("Stock");

        btnReportView.setText("View");
        btnReportView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportViewMouseClicked(evt);
            }
        });

        tblReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblReport);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(reportdatefrom, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reportdateto, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(radioReportPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(radioReportSales, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(radioReportStock, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(btnReportView, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(590, 590, 590))))
            .addComponent(jScrollPane3)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                            .addGap(5, 5, 5)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(reportdatefrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(reportdateto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioReportPurchase)
                    .addComponent(radioReportSales)
                    .addComponent(radioReportStock)
                    .addComponent(btnReportView))
                .addGap(51, 51, 51)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout reportLayout = new javax.swing.GroupLayout(report);
        report.setLayout(reportLayout);
        reportLayout.setHorizontalGroup(
            reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportLayout.createSequentialGroup()
                .addGroup(reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 1083, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        reportLayout.setVerticalGroup(
            reportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu.addTab("tab5", report);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 589, Short.MAX_VALUE)
        );

        menu.addTab("tab6", jPanel9);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 589, Short.MAX_VALUE)
        );

        menu.addTab("tab7", jPanel10);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 589, Short.MAX_VALUE)
        );

        menu.addTab("tab8", jPanel11);

        jPanel3.add(menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 990, 620));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 990, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(0);
        todayPurchaseReport();
        totalPurchaseReport();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalesActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(1);
    }//GEN-LAST:event_btnSalesActionPerformed

    private void btnPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchaseActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(2);
    }//GEN-LAST:event_btnPurchaseActionPerformed

    private void btnHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseEntered
        // TODO add your handling code here:
        btnHome.setBackground(new Color(17, 50, 13));
    }//GEN-LAST:event_btnHomeMouseEntered

    private void btnHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseExited
        // TODO add your handling code here:
        btnHome.setBackground(getBackground());
    }//GEN-LAST:event_btnHomeMouseExited

    private void btnSalesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseEntered
        // TODO add your handling code here:
        btnSales.setBackground(new Color(17, 50, 13));
    }//GEN-LAST:event_btnSalesMouseEntered

    private void btnSalesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesMouseExited
        // TODO add your handling code here:
        btnSales.setBackground(getBackground());
    }//GEN-LAST:event_btnSalesMouseExited

    private void btnPurchaseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseEntered
        // TODO add your handling code here:
        btnPurchase.setBackground(new Color(17, 50, 13));
    }//GEN-LAST:event_btnPurchaseMouseEntered

    private void btnPurchaseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseMouseExited
        // TODO add your handling code here:
        btnPurchase.setBackground(getBackground());
    }//GEN-LAST:event_btnPurchaseMouseExited

    private void txtSalesQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesQuantityFocusLost
        // TODO add your handling code here:
        try {
            if (txtSalesUnitPrice.getText().trim().isEmpty()) {

                txtSalesUnitPrice.requestFocus();
            } else if (!txtSalesQuantity.getText().trim().isEmpty()) {
                txtSalesTotalPrice.setText(getTotalPrice() + "");

            } else {
                JOptionPane.showMessageDialog(rootPane, "Quantity can not be empty ");
                txtSalesQuantity.requestFocus();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Error " + e.getMessage());
        }


    }//GEN-LAST:event_txtSalesQuantityFocusLost

    private void txtSalesUnitPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesUnitPriceFocusLost
        // TODO add your handling code here:
        try {

            if (!txtSalesUnitPrice.getText().trim().isEmpty()) {
                // Your code when the text field is not empty
            } else {
                JOptionPane.showMessageDialog(rootPane, "Unit Price cannot be empty");
                txtSalesUnitPrice.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "An error occurred: " + e.getMessage());
        }

    }//GEN-LAST:event_txtSalesUnitPriceFocusLost

    private void txtSalesDicountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesDicountKeyPressed
        // TODO add your handling code here:
        txtSalesActualPice.setText(getActualPrice() + "");
    }//GEN-LAST:event_txtSalesDicountKeyPressed

    private void txtSalesDicountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesDicountKeyReleased
        // TODO add your handling code here:
        txtSalesActualPice.setText(getActualPrice() + "");
    }//GEN-LAST:event_txtSalesDicountKeyReleased

    private void txtSalesDicountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalesDicountKeyTyped
        // TODO add your handling code here:
        txtSalesActualPice.setText(getActualPrice() + "");
    }//GEN-LAST:event_txtSalesDicountKeyTyped

    private void txtSalesDicountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesDicountFocusGained
        // TODO add your handling code here:
        txtSalesDicount.setText(0 + "");
    }//GEN-LAST:event_txtSalesDicountFocusGained

    private void txtSalesCashReceivedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalesCashReceivedFocusLost
        // TODO add your handling code here:
        float dueAmount = getActualPrice() - Float.parseFloat(txtSalesCashReceived.getText().trim());

        txtSalesDueAmount.setText(dueAmount + "");
    }//GEN-LAST:event_txtSalesCashReceivedFocusLost

    private void btnSalesSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesSubmitMouseClicked
        // TODO add your handling code here:

        sql = "insert into sales(name, unitPrice, quantity, actualPrice,"
                + " discount, dueAmount, salerId, salesDate)"
                + "     values(?,?,?,?,?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtSalesProductName.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(txtSalesUnitPrice.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtSalesQuantity.getText().trim()));
            ps.setFloat(4, getActualPrice());
            ps.setFloat(5, getDiscountAmount());
            ps.setFloat(6, Float.parseFloat(txtSalesDueAmount.getText()));
            ps.setInt(7, 1);
            ps.setDate(8, convertUtilDateToSqlDate(salesdate.getDate()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Submitted");
            updateStockSales();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not Submit");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSalesSubmitMouseClicked

    private void btnProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(3);
    }//GEN-LAST:event_btnProductActionPerformed

    private void btnProductSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductSaveMouseClicked
        // TODO add your handling code here:

        sql = "insert into product(name, catagory, productCode) values(?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setString(2, txtProductCatagory.getSelectedItem().toString());
            ps.setString(3, txtProductCode.getText().trim());

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Save");
            getAllProduct();

            getProductNameToCombo();
            addProductToStock();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not Save");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnProductSaveMouseClicked

    private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductMouseClicked
        // TODO add your handling code here:

        int row = tblProduct.getSelectedRow();

        String id = tblProduct.getModel().getValueAt(row, 0).toString();
        String name = tblProduct.getModel().getValueAt(row, 1).toString();
        String catagory = tblProduct.getModel().getValueAt(row, 2).toString();
        String code = tblProduct.getModel().getValueAt(row, 3).toString();

        txtProductId.setText(id);
        txtProductName.setText(name);
        txtProductCatagory.setSelectedItem(catagory);
        txtProductCode.setText(code);

    }//GEN-LAST:event_tblProductMouseClicked

    private void btnProductDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductDeleteMouseClicked
        // TODO add your handling code here:
        sql = "delete from  product where productId=?";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtProductId.getText().trim()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Deleted");
            getAllProduct();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not Delete");

            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnProductDeleteMouseClicked

    private void btnPurchaseSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPurchaseSaveMouseClicked
        // TODO add your handling code here:

        sql = "insert into purcahse(name,quantity, unitPrice, totalPrice, date)"
                + " values(?,?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, comboPurchaseProductName.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(txtPurcahseQuantity.getText().trim()));
            ps.setFloat(3, Float.parseFloat(txtPurcahseUnitPrice.getText().trim()));
            ps.setFloat(4, Float.parseFloat(txtPurcahseTotalPrice.getText().trim()));
            ps.setDate(5, convertUtilDateToSqlDate(purchasedate.getDate()));

            ps.executeUpdate();

            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Data Save");
            updateStockPurchase();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Data not Save");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnPurchaseSaveMouseClicked

    public void updateStockProductName(int stockId) {
        sql = "update stock set pName=? where stockId=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setInt(2, stockId);

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void btnProductUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProductUpdateMouseClicked
        // TODO add your handling code here:
        sql = "update product set name=?, catagory=?, productCode=? where productId=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtProductName.getText().trim());
            ps.setString(2, txtProductCatagory.getSelectedItem().toString());
            ps.setString(3, txtProductCode.getText().trim());
            ps.setInt(4, Integer.parseInt(txtProductId.getText()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            updateStockProductName(Integer.parseInt(txtProductId.getText()));

            JOptionPane.showMessageDialog(rootPane, "Product Updated");
            getAllProduct();

            getProductNameToCombo();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Product not update");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnProductUpdateMouseClicked

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        // TODO add your handling code here:

        menu.setSelectedIndex(4);
    }//GEN-LAST:event_btnReportActionPerformed

    public void getPurchaseReportByDate(java.util.Date fromDate, java.util.Date toDate) {
        String[] columnNames = {"Product Name", "Unit Price", "Quantity", "Total Amount", "Date"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        tblReport.setModel(model);

        sql = "select * from purcahse where date between ? and ?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {

                String name = rs.getString("name");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float totalPrice = rs.getFloat("totalPrice");
                java.util.Date date = rs.getDate("date");

                model.addRow(new Object[]{name, unitPrice, quantity, totalPrice, date});
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getSalesReportByDate(java.util.Date fromDate, java.util.Date toDate) {
        String[] columnNames = {"Product Name", "Unit Price", "Quantity", "Discount", "Due Amount", "Total Amount"};

        DefaultTableModel model = new DefaultTableModel();

        model.setColumnIdentifiers(columnNames);
        tblReport.setModel(model);

        sql = "select * from sales where salesDate between ? and ?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();

            while (rs.next()) {

                String name = rs.getString("name");
                float unitPrice = rs.getFloat("unitPrice");
                float quantity = rs.getFloat("quantity");
                float discount = rs.getFloat("discount");
                float dueAmount = rs.getFloat("dueAmount");
                float actualPrice = rs.getFloat("actualPrice");

                model.addRow(new Object[]{name, unitPrice, quantity, discount, dueAmount, actualPrice});
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getStockReportByDate() {

        String[] columnNames = {"Product Name", "Quantity"};
        DefaultTableModel model = new DefaultTableModel();

        model.setColumnIdentifiers(columnNames);
        tblReport.setModel(model);

        sql = "select * from stock";

        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                String name = rs.getString("pName");

                float quantity = rs.getFloat("quantity");

                model.addRow(new Object[]{name, quantity});
            }

            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    private void btnReportViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportViewMouseClicked
        // TODO add your handling code here:

        if (radioReportPurchase.isSelected()) {
            getPurchaseReportByDate(reportdatefrom.getDate(), reportdateto.getDate());

        } else if (radioReportSales.isSelected()) {
            getSalesReportByDate(reportdatefrom.getDate(), reportdateto.getDate());

        } else if (radioReportStock.isSelected()) {
            getStockReportByDate();

        }

    }//GEN-LAST:event_btnReportViewMouseClicked

    private void btnSalesAddToCartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalesAddToCartMouseClicked
        // TODO add your handling code here:
        String[] columns = {"Product name", "Unit Price", "Quantity", "Actual Price"};
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setColumnIdentifiers(columns);

        tblCheckout.setModel(dtm);

        String name = txtSalesProductName.getSelectedItem().toString();
        float unitPrice = Float.parseFloat(txtSalesUnitPrice.getText().trim());
        float quantity = Float.parseFloat(txtSalesQuantity.getText().trim());
        float actualPrice = Float.parseFloat(txtSalesActualPice.getText().trim());

        List<Object> productList = new ArrayList<>();
        productList.add(new Object[]{name, unitPrice, quantity, actualPrice});

        for (Object i : productList) {
            dtm.addRow((Object[]) i);
        }

        txtSalesUnitPrice.setText(null);
        txtSalesQuantity.setText(null);
        txtSalesActualPice.setText(null);

    }//GEN-LAST:event_btnSalesAddToCartMouseClicked

    private void btnProductUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProductUpdateActionPerformed

    private void btnProductDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProductDeleteActionPerformed

    private void btnProductSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProductSaveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new DashBoard().setVisible(true);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnProduct;
    private javax.swing.JButton btnProductDelete;
    private javax.swing.JButton btnProductReset;
    private javax.swing.JButton btnProductSave;
    private javax.swing.JButton btnProductUpdate;
    private javax.swing.JButton btnPurchase;
    private javax.swing.JButton btnPurchaseSave;
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnReportView;
    private javax.swing.JButton btnSales;
    private javax.swing.JButton btnSalesAddToCart;
    private javax.swing.JButton btnSalesSubmit;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> comboPurchaseProductName;
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JLabel lblTodayDue;
    private javax.swing.JLabel lblTodayPurchase;
    private javax.swing.JLabel lblTodaySales;
    private javax.swing.JLabel lblTotalDues;
    private javax.swing.JLabel lblTotalPurchase;
    private javax.swing.JLabel lblTotalSales;
    private javax.swing.JTabbedPane menu;
    private javax.swing.JPanel product;
    private javax.swing.JPanel purchase;
    private com.toedter.calendar.JDateChooser purchasedate;
    private javax.swing.JRadioButton radioReportPurchase;
    private javax.swing.JRadioButton radioReportSales;
    private javax.swing.JRadioButton radioReportStock;
    private javax.swing.JPanel report;
    private com.toedter.calendar.JDateChooser reportdatefrom;
    private com.toedter.calendar.JDateChooser reportdateto;
    private javax.swing.JPanel sales;
    private com.toedter.calendar.JDateChooser salesdate;
    private javax.swing.JTable tblCheckout;
    private javax.swing.JTable tblProduct;
    private javax.swing.JTable tblPurchase;
    private javax.swing.JTable tblReport;
    private javax.swing.JComboBox<String> txtProductCatagory;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtPurcahseProductId;
    private javax.swing.JTextField txtPurcahseQuantity;
    private javax.swing.JTextField txtPurcahseTotalPrice;
    private javax.swing.JTextField txtPurcahseUnitPrice;
    private javax.swing.JTextField txtSalesActualPice;
    private javax.swing.JTextField txtSalesCashReceived;
    private javax.swing.JTextField txtSalesDicount;
    private javax.swing.JTextField txtSalesDueAmount;
    private javax.swing.JComboBox<String> txtSalesProductName;
    private javax.swing.JTextField txtSalesQuantity;
    private javax.swing.JTextField txtSalesSID;
    private javax.swing.JTextField txtSalesTotalPrice;
    private javax.swing.JTextField txtSalesUnitPrice;
    // End of variables declaration//GEN-END:variables
}
