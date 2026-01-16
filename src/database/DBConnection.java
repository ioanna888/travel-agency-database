package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/travel_agency"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "1234"; 

    public static Connection connect() {
        Connection con = null;
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ ΕΠΙΤΥΧΙΑ: Η σύνδεση με τη βάση έγινε κανονικά!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ΣΦΑΛΜΑ: Δεν βρέθηκε ο Driver! Η βιβλιοθήκη δεν μπήκε σωστά.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ ΣΦΑΛΜΑ: Λάθος στοιχεία σύνδεσης (Όνομα βάσης ή Κωδικός).");
            e.printStackTrace();
        }
        return con;
    }
    
    
    public static void main(String[] args) {
        connect();
    }
}

