import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;


public class TripFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public TripFrame() {
        setTitle("Trip Management");
        setSize(1200, 600); // Μεγάλο παράθυρο λόγω πολλών στηλών
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {
                "ID", "Departure", "Return", "Max Seats",
                "Cost (Adult)", "Cost (Child)", "Status",
                "Min Part.", "Branch", "Guide AT", "Driver AT"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // to fit the columns
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // Departure
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Return

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton btnRefresh = new JButton("Load Data");
        JButton btnAdd = new JButton("Add Trip");
        JButton btnDelete = new JButton("Delete Trip");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addTrip());
        btnDelete.addActionListener(e -> deleteTrip());

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT * FROM trip";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("tr_id"),
                        rs.getString("tr_departure"), // departure date is string {Y-M_D)
                        rs.getString("tr_return"),
                        rs.getInt("tr_maxseats"),
                        rs.getDouble("tr_cost_adult"),
                        rs.getDouble("tr_cost_child"),
                        rs.getString("tr_status"),
                        rs.getInt("tr_min_participants"),
                        rs.getInt("tr_br_code"),
                        rs.getString("tr_gui_AT"),
                        rs.getString("tr_drv_AT")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
        }
    }

    private void addTrip() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField txtId = new JTextField();
        JTextField txtDep = new JTextField();
        txtDep.setToolTipText("YYYY-MM-DD HH:MM:SS");

        JTextField txtRet = new JTextField();
        txtRet.setToolTipText("YYYY-MM-DD HH:MM:SS");

        JTextField txtMaxSeats = new JTextField();
        JTextField txtCostAdult = new JTextField();
        JTextField txtCostChild = new JTextField();

        // Dropdown for enum status(
        String[] statuses = {"PLANNED", "CONFIRMED", "ACTIVE", "COMPLETED", "CANCELLED"};
        JComboBox<String> cmbStatus = new JComboBox<>(statuses);

        JTextField txtMinPart = new JTextField();
        JTextField txtBranch = new JTextField(); // Foreign Key for branch
        JTextField txtGuide = new JTextField();  // Foreign Key of guide
        JTextField txtDriver = new JTextField(); // Foreign Key of driver

        // add details to the panel
        panel.add(new JLabel("Trip ID:")); panel.add(txtId);
        panel.add(new JLabel("Departure (YYYY-MM-DD HH:MM:SS):")); panel.add(txtDep);
        panel.add(new JLabel("Return (YYYY-MM-DD HH:MM:SS):")); panel.add(txtRet);
        panel.add(new JLabel("Max Seats:")); panel.add(txtMaxSeats);
        panel.add(new JLabel("Cost Adult:")); panel.add(txtCostAdult);
        panel.add(new JLabel("Cost Child:")); panel.add(txtCostChild);
        panel.add(new JLabel("Status:")); panel.add(cmbStatus);
        panel.add(new JLabel("Min Participants:")); panel.add(txtMinPart);
        panel.add(new JLabel("Branch Code (Must Exist):")); panel.add(txtBranch);
        panel.add(new JLabel("Guide AT (Must Exist):")); panel.add(txtGuide);
        panel.add(new JLabel("Driver AT (Must Exist):")); panel.add(txtDriver);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Trip", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            // check if main keys exist ypoxreotika keys
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Trip ID is required!");
                return;
            }

            String sql = "INSERT INTO trip (tr_id, tr_departure, tr_return, tr_maxseats, tr_cost_adult, tr_cost_child, tr_status, tr_min_participants, tr_br_code, tr_gui_AT, tr_drv_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, Integer.parseInt(txtId.getText().trim()));

                // dates as strings
                pstmt.setString(2, txtDep.getText().trim());
                pstmt.setString(3, txtRet.getText().trim());

                pstmt.setInt(4, Integer.parseInt(txtMaxSeats.getText().trim()));
                pstmt.setDouble(5, Double.parseDouble(txtCostAdult.getText().trim()));
                pstmt.setDouble(6, Double.parseDouble(txtCostChild.getText().trim()));
                pstmt.setString(7, (String) cmbStatus.getSelectedItem());
                pstmt.setInt(8, Integer.parseInt(txtMinPart.getText().trim()));

                // Foreign Keys (Branch, Guide, Driver)
                // an kena null allios xreiazetai timi
                if(txtBranch.getText().trim().isEmpty()) pstmt.setNull(9, Types.INTEGER);
                else pstmt.setInt(9, Integer.parseInt(txtBranch.getText().trim()));

                if(txtGuide.getText().trim().isEmpty()) pstmt.setNull(10, Types.VARCHAR);
                else pstmt.setString(10, txtGuide.getText().trim());

                if(txtDriver.getText().trim().isEmpty()) pstmt.setNull(11, Types.VARCHAR);
                else pstmt.setString(11, txtDriver.getText().trim());

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Success! Trip added.");
                loadData();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error: Check your numbers (ID, Seats, Cost, Branch Code).");
            } catch (SQLException e) {
                // Έλεγχος για λάθη Foreign Key
                if (e.getErrorCode() == 1452) {
                    JOptionPane.showMessageDialog(this, "Error: Invalid Branch Code, Guide AT, or Driver AT.\nThey must exist in their respective tables!");
                } else if (e.getErrorCode() == 1062) {
                    JOptionPane.showMessageDialog(this, "Error: Trip ID already exists!");
                } else {
                    JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
                }
            }
        }
    }

    private void deleteTrip() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trip to delete.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Trip ID: " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM trip WHERE tr_id = ?";

            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Trip deleted successfully.");
                    loadData();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting: " + e.getMessage());
            }
        }
    }

}
