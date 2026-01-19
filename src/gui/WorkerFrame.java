import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

        public class WorkerFrame extends javax.swing.JFrame {
            private DefaultTableModel model;
            private JTable table;

            public WorkerFrame() {
                setTitle("Worker Management");

                setSize(900, 500);

                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                setLocationRelativeTo(null);

                setLayout(new BorderLayout());


                // stiles
                String[] columns = {"AT (ID)", "Name", "Last Name ", "email", "Salary", "Branch Code"};

                model = new

                        DefaultTableModel(columns, 0);

                table = new

                        JTable(model);

                add(new JScrollPane(table), BorderLayout.CENTER);

                //butoons panel
                JPanel buttonPanel = new JPanel();

                JButton btnRefresh = new JButton("Load Data");
                JButton btnAdd = new JButton("Add Worker");
                JButton btnDelete = new JButton("Delete Worker");

                buttonPanel.add(btnRefresh);
                buttonPanel.add(btnAdd);
                buttonPanel.add(btnDelete);

                add(buttonPanel, BorderLayout.SOUTH);

                btnRefresh.addActionListener(e -> loadData());
                btnAdd.addActionListener(e -> addWorker());
                btnDelete.addActionListener(e -> deleteWorker());

                loadData();
            }

                private void loadData() {
                    model.setRowCount(0); // katharizo pinaka
                    String sql = "select * from worker";

                    try (Connection conn = DBConnection.connect();
                         Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(sql)) {

                        while (rs.next()) {
                            String at = rs.getString("wrk_AT");
                            String name = rs.getString("wrk_name");
                            String lname = rs.getString("wrk_lname");
                            String email = rs.getString("wrk_email");
                            double salary = rs.getDouble("wrk_salary");
                            int branchCode = rs.getInt("wrk_br_code");

                            model.addRow( new Object[]{at,name,lname,email,salary,branchCode} );
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Sql Error" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                private void addWorker() {
                    String at = JOptionPane.showInputDialog(this, "Worker AT (ID):");
                    if (at == null || at.trim().isEmpty()) return;

                    String name = JOptionPane.showInputDialog(this, "First Name:");
                    String lname = JOptionPane.showInputDialog(this, "Last Name:");
                    String email = JOptionPane.showInputDialog(this, "Email:");
                    String salaryStr = JOptionPane.showInputDialog(this, "Salary:");
                    String branchCodeStr = JOptionPane.showInputDialog(this, "Branch Code:");

                    String sql = "INSERT INTO worker (wrk_AT, wrk_name, wrk_lname,wrk_email,wrk_salary, wrk_br_code) VALUES (?, ?, ? , ? , ?, ?) ";

                    try (Connection conn = DBConnection.connect();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        // Μετατροπές και ανάθεση τιμών
                        pstmt.setString(1, at);
                        pstmt.setString(2, name);
                        pstmt.setString(3, lname);
                        pstmt.setString(4, email);

                        // Μετατροπή μισθού σε δεκαδικό
                        pstmt.setDouble(5, Double.parseDouble(salaryStr));

                        // Μετατροπή κωδικού καταστήματος σε ακέραιο
                        pstmt.setInt(6, Integer.parseInt(branchCodeStr));

                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Success! Worker added.");
                            loadData(); // Ανανέωση πίνακα
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Error: Salary and Branch Code must be numbers!");
                    } catch (SQLException e) {
                        // Εδώ θα πιάσει και το λάθος αν βάλεις Branch Code που δεν υπάρχει
                        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
                    }
                }

                private void deleteWorker() {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(this, "Select a worker to delete.");
                        return;
                    }

                    // Primary key from 1rst stili dld index 0
                    String at = (String) model.getValueAt(selectedRow, 0);

                    int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete worker: " + at + "?", "Delete", JOptionPane.YES_NO_OPTION);

                    if (answer == JOptionPane.YES_OPTION) {
                        String sql = "DELETE FROM worker WHERE wrk_AT = ?";

                        try (Connection conn = DBConnection.connect();
                             PreparedStatement pstmt = conn.prepareStatement(sql)) {

                            pstmt.setString(1, at);
                            pstmt.executeUpdate();

                            JOptionPane.showMessageDialog(this, "Worker deleted.");
                            loadData(); // Ανανέωση

                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(this, "Error deleting worker: " + e.getMessage());
                        }
                    }
                }
            }