package gui;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VehicleFrame extends JFrame {
    //variables we will use for the class
    private DefaultTableModel model;
    private JTable table;
    
    // the constructor
    public VehicleFrame() {
        //settings of the table
        setTitle("Vehicle management ");
        setSize(800,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        //details of the table
        String[] columns = { "ID", "Brand", "Model", "License Plate", "Type", "Seats", "Status", "Kilometres"};
        
        model = new DefaultTableModel(columns,0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
       JPanel buttonPanel = new JPanel();
        
        JButton btnRefresh = new JButton("Load data");
        JButton btnAdd = new JButton("Add Vehicle "); 
        JButton btnDelete = new JButton("Delete Vehicle");
        JButton btnEdit = new JButton("Edit Vehicle");
        
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete); 
        buttonPanel.add(btnEdit);

        
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addVehicle());
        btnDelete.addActionListener(e -> deleteVehicle());
        btnEdit.addActionListener(e -> editVehicle());
       
    }
        
        private void loadData() {
            
            model.setRowCount(0);
            String sql = "SELECT * FROM vehicles";
            
            try (Connection conn = DBConnection.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql))
                
            {
                while(rs.next()) {
                    int id = rs.getInt("ve_id");     
                    String brand = rs.getString("ve_brand");
                    String modelStr = rs.getString("ve_model");
                    String plate = rs.getString("ve_license_plate"); 
                    String type = rs.getString("ve_type");   
                    int seats = rs.getInt("ve_seats");
                    String status = rs.getString("ve_status");
                    int km = rs.getInt("ve_km");
                    
                    
                    model.addRow(new Object[]{id, brand, modelStr, plate, type, seats, status, km});
                
                }
            } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error in Sql " + e.getMessage());
            e.printStackTrace(); 
                
            }
        }
        
        private void addVehicle() {
            String brand = JOptionPane.showInputDialog(this,"Brand:");
            String modelStr = JOptionPane.showInputDialog(this, "Model:");
            String license_plate = JOptionPane.showInputDialog(this, "License Plate:");
        
            String type = JOptionPane.showInputDialog(this, "Type (Car, Bus, Van):");
            String seatsStr = JOptionPane.showInputDialog(this, "Seats:");
            
            String brCodeStr = JOptionPane.showInputDialog(this, "Branch Code ID:");
            
            
            // default 
            String status = "Available"; 
            int km = 0;
            
            // checking if the user pressed cancel or left space
            if (brand == null || license_plate == null || brand.isEmpty() || license_plate.isEmpty()) {
            return;
        }
            String sql = "INSERT INTO vehicles (ve_brand, ve_model, ve_license_plate, ve_seats, ve_km, ve_type, ve_status, ve_br_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            
            try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

          
            pstmt.setString(1, brand);
            pstmt.setString(2, modelStr);
            pstmt.setString(3, license_plate);
            pstmt.setString(4, type);
            
            // 4. ve_seats
            try {
                pstmt.setInt(4, Integer.parseInt(seatsStr)); 
            } catch (NumberFormatException ex) {
                pstmt.setInt(4, 4); // default αν βάλει λάθος νούμερο
            }
            
            // 5. ve_km
            pstmt.setInt(5, km);
            
            // 6. ve_type
            pstmt.setString(6, type);
            
            // 7. ve_status
            pstmt.setString(7, status);
            try {
                pstmt.setInt(8, Integer.parseInt(brCodeStr));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Branch Code! Must be a number.");
                return; // Σταματάμε εδώ αν έδωσε λάθος κωδικό
            }
            
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Succes! the vehicle has been added. ");
                loadData(); // reloading the table to see the new vehicle we added
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error in DataBase " + e.getMessage());
        }
    }
        
        
    //delete method
        private void deleteVehicle() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a vehicle from the table. ");
                return;
            }
            
            int id = (int) model.getValueAt(selectedRow, 0);
            int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this vehicle ;", "Delete", JOptionPane.YES_NO_OPTION);
            
            if (answer == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM vehicles WHERE ve_id = ?";
            
            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "The vehicle has been deleted successfully.");
                    loadData(); 
                }
                } catch (SQLException e) {
                
                JOptionPane.showMessageDialog(this, "Error during delete " + e.getMessage());
            }
            }
        }
        
        private void editVehicle (){
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a vehicle to edit.");
                return;
                
            }
            
            int id = (int) model.getValueAt(selectedRow, 0);
            String currentBrand = (String) model.getValueAt(selectedRow, 1);
            String currentModel = (String) model.getValueAt(selectedRow, 2);
            String currentPlate = (String) model.getValueAt(selectedRow, 3);
            String currentType = (String) model.getValueAt(selectedRow, 4);
            int currentSeats = (int) model.getValueAt(selectedRow, 5);
            String currentStatus = (String) model.getValueAt(selectedRow, 6);
            int currentKm = (int) model.getValueAt(selectedRow, 7);
            
        String newBrand = (String) JOptionPane.showInputDialog(this, "Edit Brand:", "Edit Vehicle", JOptionPane.PLAIN_MESSAGE, null, null, currentBrand);
        if (newBrand == null) return; // Αν πατήσει Cancel, σταματάμε

        String newModel = (String) JOptionPane.showInputDialog(this, "Edit Model:", "Edit Vehicle", JOptionPane.PLAIN_MESSAGE, null, null, currentModel);
        if (newModel == null) return;

        String newPlate = (String) JOptionPane.showInputDialog(this, "Edit Plate:", "Edit Vehicle", JOptionPane.PLAIN_MESSAGE, null, null, currentPlate);
        if (newPlate == null) return;
        
        String newType = (String) JOptionPane.showInputDialog(this, "Edit Type:", "Edit Vehicle", JOptionPane.PLAIN_MESSAGE, null, null, currentType);
        if (newType == null) return;

        String newSeatsStr = (String) JOptionPane.showInputDialog(this, "Edit Seats:", "Edit Vehicle", JOptionPane.PLAIN_MESSAGE, null, null, String.valueOf(currentSeats));
        if (newSeatsStr == null) return;
        
        String newStatus = (String) JOptionPane.showInputDialog(this, "Edit Status:", "Edit Vehicle", JOptionPane.PLAIN_MESSAGE, null, null, currentStatus);
        if (newStatus == null) return;
        
        String newKmStr = (String) JOptionPane.showInputDialog(this, "Edit KM:", "Edit Vehicle", JOptionPane.PLAIN_MESSAGE, null, null, String.valueOf(currentKm));
        if (newKmStr == null) return;
        
        String sql = "UPDATE vehicles SET ve_brand=?, ve_model=?, ve_license_plate=?, ve_type=?, ve_seats=?, ve_status=?, ve_km=? WHERE ve_id=?";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newBrand);
            pstmt.setString(2, newModel);
            pstmt.setString(3, newPlate);
            pstmt.setString(4, newType);
            pstmt.setInt(5, Integer.parseInt(newSeatsStr));
            pstmt.setString(6, newStatus);
            pstmt.setInt(7, Integer.parseInt(newKmStr));
            pstmt.setInt(8, id); // Το WHERE ve_id = ? μπαίνει στο τέλος

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Vehicle updated successfully!");
                loadData(); 
            }
            
            } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Seats/KM.");
        }
    }
    
      
            
        
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VehicleFrame().setVisible(true);
        });
    }
}




    
    



