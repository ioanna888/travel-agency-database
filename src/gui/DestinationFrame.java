import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DestinationFrame extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public DestinationFrame() {
        setTitle("Destination Management");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // dtiles me vasi pinaka sql
        String[] columns = {"ID", "Name", "Description", "Type", "Language Code", "Location ID"};

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // button panels
        JPanel buttonPanel = new JPanel();

        JButton btnRefresh = new JButton("Load Data");
        JButton btnAdd = new JButton("Add Destination");
        JButton btnDelete = new JButton("Delete Destination");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.SOUTH);
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addDestination());
        btnDelete.addActionListener(e -> deleteDestination());

        // Φόρτωση δεδομένων στην αρχή
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT * FROM destination";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("dst_id");
                String name = rs.getString("dst_name");
                String descr = rs.getString("dst_descr");
                String rtype = rs.getString("dst_rtype");
                String langCode = rs.getString("dst_language_code");
                int location = rs.getInt("dst_location");

                model.addRow(new Object[]{id, name, descr, rtype, langCode, location});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addDestination() {
        try {
            // ID (Integer)
            String idStr = JOptionPane.showInputDialog(this, "Destination ID:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            int id = Integer.parseInt(idStr);

            // Name & Description (Strings)
            String name = JOptionPane.showInputDialog(this, "Destination Name:");
            String descr = JOptionPane.showInputDialog(this, "Description:");

            // Type (ENUM: LOCAL or ABROAD) - Χρήση λίστας για αποφυγή λαθών
            Object[] types = {"LOCAL", "ABROAD"};
            String rtype = (String) JOptionPane.showInputDialog(this,
                    "Select Type:", "Type",
                    JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

            // Language Code (Foreign Key)
            String langCode = JOptionPane.showInputDialog(this, "Language Code (must exist in language_ref):");

            // Location (Integer)
            String locStr = JOptionPane.showInputDialog(this, "Location ID:");
            int location = Integer.parseInt(locStr);

            // SQL Insert
            String sql = "INSERT INTO destination (dst_id, dst_name, dst_descr, dst_rtype, dst_language_code, dst_location) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, descr);
                pstmt.setString(4, rtype);
                pstmt.setString(5, langCode);
                pstmt.setInt(6, location);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Success! Destination added.");
                    loadData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: ID and Location must be numbers!");
        }
    }

    private void deleteDestination() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a destination to delete.");
            return;
        }

        // Το ID είναι στην 1η στήλη (index 0)
        int id = (int) model.getValueAt(selectedRow, 0);

        int answer = JOptionPane.showConfirmDialog(this, "Delete destination with ID: " + id + "?", "Delete", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM destination WHERE dst_id = ?";

            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Destination deleted.");
                    loadData();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting: " + e.getMessage());
            }
        }
    }

}
