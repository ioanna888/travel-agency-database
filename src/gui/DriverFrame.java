package gui;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DriverFrame extends JFrame {
    
    private DefaultTableModel model;
    private JTable table;
    
    public DriverFrame() {
        setTitle("Drivers Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        String[] columns = { "Driver AT ", "License", "Route", "Experience"};
        
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        
        JButton btnRefresh = new JButton("Load Data");
        JButton btnAdd = new JButton("Add Driver"); 
        JButton btnDelete = new JButton("Delete Driver");
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete); 
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addDriver());
        btnDelete.addActionListener(e -> deleteDriver());
        
        loadData();
    }
        
    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT * FROM driver";
        
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while(rs.next()) {
                String drv_at = rs.getString("drv_AT");      
                String license = rs.getString("drv_license");
                String route = rs.getString("drv_route");
                int experience = rs.getInt("drv_experience");
                
               
                
                model.addRow(new Object[]{drv_at, license, route, experience});
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
        }
    }
    
    
    private void addDriver() {
        String drv_at = JOptionPane.showInputDialog(this, "Worker AT (Must exist in Worker table):");
        if (drv_at == null || drv_at.trim().isEmpty()) return;
        
        String[] licenses = {"A", "B", "C", "D"};
        String license = (String) JOptionPane.showInputDialog(this, 
                "Select License Type:", "License", 
                JOptionPane.QUESTION_MESSAGE, null, licenses, licenses[1]);
        
        String[] routes = {"LOCAL", "ABROAD"};
        String route = (String) JOptionPane.showInputDialog(this, 
                "Select Route Type:", "Route", 
                JOptionPane.QUESTION_MESSAGE, null, routes, routes[0]);
        
        String expStr = JOptionPane.showInputDialog(this, "Experience (Years):");
        if (license == null || route == null || expStr == null) return;

        String sql = "INSERT INTO driver (drv_AT, drv_license, drv_route, drv_experience) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, drv_at);
            pstmt.setString(2, license);
            pstmt.setString(3, route);
            
            try {
                pstmt.setInt(4, Integer.parseInt(expStr));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Experience must be a number!");
                return;
            }

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Success! Driver added.");
                loadData();
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) { 
                JOptionPane.showMessageDialog(this, "Error: This AT does not exist in the Worker table!\nYou must create the Worker first.");
            } else if (e.getErrorCode() == 1062) { 
                JOptionPane.showMessageDialog(this, "Error: This Worker is already a Driver!");
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            }
        }
    }
        
    

    private void deleteDriver() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a driver to delete.");
            return;
        }

        String drv_at = (String) model.getValueAt(selectedRow, 0);
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Driver?", "Delete", JOptionPane.YES_NO_OPTION);
        
        if (answer == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM driver WHERE drv_AT = ?";

            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, drv_at);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Driver deleted successfully.");
                    loadData();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting driver: " + e.getMessage());
            }
        }
    }
    
public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        new DriverFrame().setVisible(true);
        });
    }

}


