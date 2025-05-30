import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginFrame() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
    }
    
    private void setupLayout() {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10); 

    Font labelFont = new Font("Arial", Font.PLAIN, 16); 
    Font fieldFont = new Font("Arial", Font.PLAIN, 16);

    gbc.gridx = 0; gbc.gridy = 0;
    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(labelFont);
    add(usernameLabel, gbc);

    gbc.gridx = 1; gbc.gridy = 0;
    usernameField.setFont(fieldFont);
    usernameField.setPreferredSize(new Dimension(200, 30));
    add(usernameField, gbc);

    gbc.gridx = 0; gbc.gridy = 1;
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(labelFont);
    add(passwordLabel, gbc);

    gbc.gridx = 1; gbc.gridy = 1;
    passwordField.setFont(fieldFont);
    passwordField.setPreferredSize(new Dimension(200, 30));
    add(passwordField, gbc);

    gbc.gridx = 0; gbc.gridy = 2;
    gbc.gridwidth = 2;
    loginButton.setFont(labelFont);
    loginButton.setPreferredSize(new Dimension(120, 35));
    add(loginButton, gbc);

    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(new LoginActionListener());
        ActionListener loginAction = new LoginActionListener();
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
    }
    
    private void setupFrame() {
        setTitle("Warehouse Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ImageIcon icon = new ImageIcon("C:/Users/HP/Downloads/make an icon for an imaginary clothing store.png"); 
        setIconImage(icon.getImage());
        
        setPreferredSize(new Dimension(500, 300));

        pack();
        setLocationRelativeTo(null);
    }
    
    private class LoginActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Please enter both username and password.", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                String role = authenticateUser(username, password);
                if (role != null) {
                    dispose();
                    openUserDashboard(username, role);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                        "Invalid username or password.", 
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Database error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        }
        return null;
    }
    
    private void openUserDashboard(String username, String role) {
        User user = switch (role) {
            case "Admin" -> new Admin(username);
            case "Employee" -> new Employee(username);
            case "Customer" -> new Customer(username);
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
        user.showDashboard();
    }
}
