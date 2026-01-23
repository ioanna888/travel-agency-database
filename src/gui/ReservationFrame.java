import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReservationFrame extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public ReservationFrame() {
        setTitle("Reservation Management");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // pinakas
        model = new DefaultTableModel(new String[]{"Trip ID", "Seat #", "Cust ID", "Status", "Cost"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // buttons
        JPanel buttonPanel = new JPanel();
        JButton btnRefresh = new JButton("Load");
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // Λειτουργίες
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addReservation());
        btnDelete.addActionListener(e -> deleteReservation());

        loadData();
    }

    private void loadData() {
        model.setRowCount(0); // clean of what already exists

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM reservation")) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("res_tr_id"),
                        rs.getInt("res_seatnum"),
                        rs.getInt("res_cust_id"),
                        rs.getString("res_status"),
                        rs.getDouble("res_total_cost")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void addReservation() {

        JTextField txtTrip = new JTextField();
        JTextField txtSeat = new JTextField();
        JTextField txtCust = new JTextField();
        JTextField txtCost = new JTextField();
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"PENDING", "CONFIRMED", "PAID", "CANCELLED"});

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Trip ID:")); panel.add(txtTrip);
        panel.add(new JLabel("Seat Num:")); panel.add(txtSeat);
        panel.add(new JLabel("Cust ID:")); panel.add(txtCust);
        panel.add(new JLabel("Status:")); panel.add(cmbStatus);
        panel.add(new JLabel("Cost:")); panel.add(txtCost);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Reservation", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "INSERT INTO reservation (res_tr_id, res_seatnum, res_cust_id, res_status, res_total_cost) VALUES (?, ?, ?, ?, ?)")) {

                pstmt.setInt(1, Integer.parseInt(txtTrip.getText().trim()));
                pstmt.setInt(2, Integer.parseInt(txtSeat.getText().trim()));
                pstmt.setInt(3, Integer.parseInt(txtCust.getText().trim()));
                pstmt.setString(4, (String) cmbStatus.getSelectedItem());
                pstmt.setDouble(5, Double.parseDouble(txtCost.getText().trim()));

                pstmt.executeUpdate();
                loadData(); // ananeoneatai
                JOptionPane.showMessageDialog(this, "Added Successfully!");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            }
        }
    }

    private void deleteReservation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }

        int tripId = (int) model.getValueAt(row, 0);
        int seatNum = (int) model.getValueAt(row, 1);

        if (JOptionPane.showConfirmDialog(this, "Delete reservation?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM reservation WHERE res_tr_id = ? AND res_seatnum = ?")) {

                pstmt.setInt(1, tripId);
                pstmt.setInt(2, seatNum);
                pstmt.executeUpdate();

                loadData(); // Αnaneosi
                JOptionPane.showMessageDialog(this, "Deleted Successfully!");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}
