import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends User {
    
    public Admin(String username) {
        super(username);
    }
    
    public void showDashboard() {
        JFrame frame = new JFrame("Admin Dashboard - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("C:/Users/HP/Downloads/make an icon for an imaginary clothing store.png"); 
        frame.setIconImage(icon.getImage());

        String[] columnNames = {"Username", "Role"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        JTable userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        loadUsers(tableModel);
        
        JButton deleteButton = new JButton("Delete User");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);
        
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a user to delete.");
                    return;
                }
                
                String selectedUsername = (String) tableModel.getValueAt(selectedRow, 0);
                
                if (selectedUsername.equals(username)) {
                    JOptionPane.showMessageDialog(frame, "You cannot delete your own account.");
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(frame, 
                    "Are you sure you want to delete user: " + selectedUsername + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        deleteUser(selectedUsername);
                        loadUsers(tableModel);
                        JOptionPane.showMessageDialog(frame, "User deleted successfully.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Error deleting user: " + ex.getMessage());
                    }
                }
            }
        });
        
        refreshButton.addActionListener(e -> loadUsers(tableModel));
        
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginFrame().setVisible(true);
        });
        
        frame.add(new JLabel("User Management", SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(new JScrollPane(userTable), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void loadUsers(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        
        String sql = "SELECT username, role FROM users ORDER BY role, username";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String[] row = {rs.getString("username"), rs.getString("role")};
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading users: " + e.getMessage());
        }
    }
    
    private void deleteUser(String usernameToDelete) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usernameToDelete);
            ps.executeUpdate();
        }
    }
}
