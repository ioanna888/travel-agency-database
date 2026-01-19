import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AccommodationFrame extends JFrame {private DefaultTableModel model;
    private JTable table;

    public AccommodationFrame() {
        setTitle("Accommodation Management");
        setSize(1200, 600); // Μεγαλύτερο παράθυρο λόγω πολλών στηλών
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // name stilon
        String[] columns = {
                "ID", "Name", "Type", "Rating", "Stars", "Status",
                "City", "Street", "Num", "Zip", "Phone", "Email",
                "Rooms", "Price", "Facilities", "Dest ID"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // scroll , table too big
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // sizes for better view
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Name

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Κουμπιά
        JPanel buttonPanel = new JPanel();
        JButton btnRefresh = new JButton("Load Data");
        JButton btnAdd = new JButton("Add Accommodation");
        JButton btnDelete = new JButton("Delete Accommodation");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addAccommodation());
        btnDelete.addActionListener(e -> deleteAccommodation());

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT * FROM accommodation";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("acc_id"),
                        rs.getString("acc_name"),
                        rs.getString("acc_type"),
                        rs.getDouble("acc_rating"),
                        rs.getInt("acc_stars"),
                        rs.getString("acc_status"),
                        rs.getString("acc_city"),
                        rs.getString("acc_street"),
                        rs.getInt("acc_street_number"),
                        rs.getString("acc_zipcode"),
                        rs.getString("acc_phone"),
                        rs.getString("acc_email"),
                        rs.getInt("acc_total_rooms"),
                        rs.getDouble("acc_price_per_room"),
                        rs.getString("acc_facilities"),
                        rs.getInt("acc_dst_id")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void addAccommodation() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5)); // Grid με 2 στήλες


        JTextField txtName = new JTextField();

        // enum box for type of accommodation
        String[] types = {"HOTEL", "HOSTEL", "RESORT", "APARTMENT", "ROOM"};
        JComboBox<String> cmbType = new JComboBox<>(types);

        JTextField txtRating = new JTextField("0.00");
        JTextField txtStars = new JTextField("0");

        // enum box for status
        String[] statuses = {"ACTIVE", "INACTIVE"};
        JComboBox<String> cmbStatus = new JComboBox<>(statuses);

        JTextField txtCity = new JTextField();
        JTextField txtStreet = new JTextField();
        JTextField txtNum = new JTextField();
        JTextField txtZip = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtRooms = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtFacilities = new JTextField();
        JTextField txtDestId = new JTextField();

        // add panel for ratings
        panel.add(new JLabel("Name:")); panel.add(txtName);
        panel.add(new JLabel("Type:")); panel.add(cmbType);
        panel.add(new JLabel("Rating (0-10):")); panel.add(txtRating);
        panel.add(new JLabel("Stars (1-5):")); panel.add(txtStars);
        panel.add(new JLabel("Status:")); panel.add(cmbStatus);
        panel.add(new JLabel("City:")); panel.add(txtCity);
        panel.add(new JLabel("Street:")); panel.add(txtStreet);
        panel.add(new JLabel("Street Number:")); panel.add(txtNum);
        panel.add(new JLabel("Zip Code:")); panel.add(txtZip);
        panel.add(new JLabel("Phone:")); panel.add(txtPhone);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Total Rooms:")); panel.add(txtRooms);
        panel.add(new JLabel("Price per Room:")); panel.add(txtPrice);
        panel.add(new JLabel("Facilities (comma separated):")); panel.add(txtFacilities);
        panel.add(new JLabel("Destination ID (Must exist):")); panel.add(txtDestId);

        // dialog window with the panel inside
        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add New Accommodation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO accommodation (acc_name, acc_type, acc_rating, acc_stars, acc_status, acc_city, acc_street, acc_street_number, acc_zipcode, acc_phone, acc_email, acc_total_rooms, acc_price_per_room, acc_facilities, acc_dst_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, txtName.getText());
                pstmt.setString(2, (String) cmbType.getSelectedItem());
                pstmt.setDouble(3, Double.parseDouble(txtRating.getText()));
                pstmt.setInt(4, Integer.parseInt(txtStars.getText()));
                pstmt.setString(5, (String) cmbStatus.getSelectedItem());
                pstmt.setString(6, txtCity.getText());
                pstmt.setString(7, txtStreet.getText());
                pstmt.setInt(8, Integer.parseInt(txtNum.getText()));
                pstmt.setString(9, txtZip.getText());
                pstmt.setString(10, txtPhone.getText());
                pstmt.setString(11, txtEmail.getText());
                pstmt.setInt(12, Integer.parseInt(txtRooms.getText()));
                pstmt.setDouble(13, Double.parseDouble(txtPrice.getText()));
                pstmt.setString(14, txtFacilities.getText());
                pstmt.setInt(15, Integer.parseInt(txtDestId.getText())); // Πρέπει να υπάρχει στο destination table

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Accommodation added successfully!");
                loadData();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Please check number fields (Stars, Price, Rooms, IDs).");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        }

    }

    private void deleteAccommodation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete accommodation with ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM accommodation WHERE acc_id = ?";
            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Deleted successfully.");
                loadData();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting: " + e.getMessage());
            }
        }

    }
}
