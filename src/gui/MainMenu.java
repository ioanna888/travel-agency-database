package gui;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        
        setTitle("Travel Agency System"); 
        setSize(400, 400); // Το μέγεθος
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

        
        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); 
        add(titleLabel, BorderLayout.NORTH);

        
        JPanel buttonPanel = new JPanel();
        
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); 

        // creating the buttons
        JButton btnVehicles = new JButton("Vehicle Management");
        JButton btnBranches = new JButton("Branch Management");
        JButton btnDrivers = new JButton("Drivers Management "); 
        JButton btnExit = new JButton("Exit");

        
        btnVehicles.addActionListener(e -> {
            new VehicleFrame().setVisible(true);
        });

        btnBranches.addActionListener(e -> {
            new BranchFrame().setVisible(true);
        });
        
        btnDrivers.addActionListener(e -> {
            new DriverFrame().setVisible(true);
        });
                
        btnExit.addActionListener(e -> {
            System.exit(0);
        });

        buttonPanel.add(btnVehicles);
        buttonPanel.add(btnBranches);
        buttonPanel.add(btnDrivers); 
        buttonPanel.add(btnExit);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}

