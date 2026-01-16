package gui;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BranchFrame extends JFrame {
    
    private DefaultTableModel model;
    private JTable table;
    
    public BranchFrame() {
        setTitle("Branch Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        String[] columns = { "Code", "Street", "Number", "City", "Phone"};
        
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        
        JButton btnRefresh = new JButton("Load Data");
        JButton btnAdd = new JButton("Add Branch"); 
        JButton btnDelete = new JButton("Delete Branch");
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete); 
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addBranch());
        btnDelete.addActionListener(e -> deleteBranch());
        
        loadData();
    }
        
    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT * FROM branch";
        
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while(rs.next()) {
                int code = rs.getInt("br_code");      
                String street = rs.getString("br_street");
                String number = rs.getString("br_num");
                String city = rs.getString("br_city"); 
                
                String phone = "N/A"; 
               
                String sqlPhone = "SELECT ph_number FROM phones WHERE ph_br_code = " + code + " LIMIT 1";
                try (Statement stmt2 = conn.createStatement();
                     ResultSet rs2 = stmt2.executeQuery(sqlPhone)) {
                    if (rs2.next()) {
                        phone = rs2.getString("ph_number");
                    }
                }
                model.addRow(new Object[]{code, street, number, city});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
    
    private void addBranch() {
        String code = JOptionPane.showInputDialog(this, "Branch Code:");
        String street = JOptionPane.showInputDialog(this, "Street:");
        String number = JOptionPane.showInputDialog(this, "Number:");
        String city = JOptionPane.showInputDialog(this, "City:");
        String phone = JOptionPane.showInputDialog(this, "Phone Number:");
        
        if (code == null || street == null) return;
        
        String sqlBranch = "INSERT INTO branch (br_code, br_street, br_num, br_city) VALUES (?, ?, ?, ?)";
        String sqlPhone = "INSERT INTO phones (ph_br_code, ph_number) VALUES (?, ?)";
        try (Connection conn = DBConnection.connect()) {
            conn.setAutoCommit(false);
        
             try(PreparedStatement pstmt1 = conn.prepareStatement(sqlBranch)) {

            pstmt1.setInt(1, Integer.parseInt(code)); 
            pstmt1.setString(2, street);
            pstmt1.setString(3, number);
            pstmt1.setString(4, city);
            pstmt1.executeUpdate();
            }
             
            if (phone != null && !phone.isEmpty()) {
                try (PreparedStatement pstmt2 = conn.prepareStatement(sqlPhone)) {
                    pstmt2.setInt(1, Integer.parseInt(code)); 
                    pstmt2.setString(2, phone);               
                    pstmt2.executeUpdate();
                }
            } 
            conn.commit(); // Οριστικοποίηση και των δύο
            JOptionPane.showMessageDialog(this, "Success! Branch and Phone added.");
            loadData();
            
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Branch Code must be a number!");
        }
        
    }

    private void deleteBranch() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a branch to delete.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0); 

        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this branch?", "Delete", JOptionPane.YES_NO_OPTION);
        
        if (answer == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM branch WHERE br_code = ?";

            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Branch deleted successfully.");
                    loadData();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting branch: " + e.getMessage());
            }
        }
    }
    
public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BranchFrame().setVisible(true);
        });
    }

}


