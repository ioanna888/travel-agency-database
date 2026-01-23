import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class GuideFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public GuideFrame() {
        setTitle("Guide Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // stiles gia guide
        String[] columns = { "Guide AT", "CV"};

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton btnRefresh = new JButton("Load Data");
        JButton btnAdd = new JButton("Add Guide");
        JButton btnDelete = new JButton("Delete Guide");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        add(buttonPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> addGuide());
        btnDelete.addActionListener(e -> deleteGuide());

        loadData();
    }

    // method to find the workers
    private Map<String, String> getAvailableWorkers() {
        Map<String, String> workers = new HashMap<>();

        // try to find the workers that are not already guides
        String sql = "SELECT wrk_AT, wrk_name, wrk_lname " +
                "FROM worker " +
                "WHERE wrk_AT NOT IN (SELECT gui_AT FROM guide) " +
                "ORDER BY wrk_AT";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                String at = rs.getString("wrk_AT");
                String name = rs.getString("wrk_name");
                String lname = rs.getString("wrk_lname");

                String display = at + " - " + name + " " + lname;

                workers.put(display, at);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading workers: " + e.getMessage());
        }

        return workers;
    }

    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT * FROM guide";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                String gui_at = rs.getString("gui_AT");
                String cv = rs.getString("gui_cv");

                model.addRow(new Object[]{gui_at, cv});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
        }
    }

    private void addGuide() {
        // getting the available workers to add them
        Map<String, String> workers = getAvailableWorkers();

        // check if there are any available workers
        if (workers.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No available workers! All workers are already guides or no workers exist.");
            return;
        }

        // turn the array into a dropdown list
        String[] workerOptions = workers.keySet().toArray(new String[0]);

        // show the dropdown list
        String selectedWorker = (String) JOptionPane.showInputDialog(
                this,
                "Select Worker:",
                "Worker Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                workerOptions,
                workerOptions[0]
        );

        // cancel
        if (selectedWorker == null) return;

        // gettint the id of the worker
        String gui_at = workers.get(selectedWorker);

        // asking for a cv
        String cv = JOptionPane.showInputDialog(this, "Guide CV (Description):");

        // if they press cancel during the cv phase stop
        // epistrofi null an exo keno cv
        if (cv == null) return;

        String sql = "INSERT INTO guide (gui_AT, gui_cv) VALUES (?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, gui_at);
            pstmt.setString(2, cv);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Success! Guide added.");
                loadData();
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) {
                JOptionPane.showMessageDialog(this, "Error: This AT does not exist in the Worker table!\nYou must create the Worker first.");
            } else if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Error: This Worker is already a Guide!");
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            }
        }
    }

    private void deleteGuide() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a guide to delete.");
            return;
        }

        String gui_at = (String) model.getValueAt(selectedRow, 0);
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Guide?", "Delete", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM guide WHERE gui_AT = ?";

            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, gui_at);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Guide deleted successfully.");
                    loadData();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting guide: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GuideFrame().setVisible(true);
        });
    }
}