import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Employee extends User {
    
    public Employee(String username) {
        super(username);
    }
    
    public void showDashboard() {
        JFrame frame = new JFrame("Employee Dashboard - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ImageIcon icon = new ImageIcon("C:/Users/HP/Downloads/make an icon for an imaginary clothing store.png"); 
        frame.setIconImage(icon.getImage());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inventory", createInventoryPanel());
        tabbedPane.addTab("Orders", createOrdersPanel());
        
        frame.add(tabbedPane);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"ID", "Name", "Brand", "Size", "Style", "Season", "Type", "Price", "Stock"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable inventoryTable = new JTable(tableModel);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        loadInventory(tableModel);
        
        JButton addButton = new JButton("Add Item");
        JButton updateButton = new JButton("Update Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton refreshButton = new JButton("Refresh");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        addButton.addActionListener(e -> showAddItemDialog(tableModel));
        
        updateButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select an item to update.");
                return;
            }
            showUpdateItemDialog(tableModel, selectedRow);
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select an item to delete.");
                return;
            }
            
            int itemId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String itemName = (String) tableModel.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(panel,
                "Are you sure you want to delete: " + itemName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    deleteItem(itemId);
                    loadInventory(tableModel);
                    JOptionPane.showMessageDialog(panel, "Item deleted successfully.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error deleting item: " + ex.getMessage());
                }
            }
        });
        
        refreshButton.addActionListener(e -> loadInventory(tableModel));
        
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"Order ID", "Customer", "Item", "Quantity", "Status"};
        DefaultTableModel orderTableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable ordersTable = new JTable(orderTableModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        loadOrders(orderTableModel);
        
        JButton completeButton = new JButton("Mark as Completed");
        JButton refreshButton = new JButton("Refresh");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(completeButton);
        buttonPanel.add(refreshButton);
        
        completeButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select an order.");
                return;
            }
            
            int orderId = (Integer) orderTableModel.getValueAt(selectedRow, 0);
            String currentStatus = (String) orderTableModel.getValueAt(selectedRow, 4);
            
            if ("Completed".equals(currentStatus)) {
                JOptionPane.showMessageDialog(panel, "Order is already completed.");
                return;
            }
            
            try {
                updateOrderStatus(orderId, "Completed");
                loadOrders(orderTableModel);
                JOptionPane.showMessageDialog(panel, "Order marked as completed.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error updating order: " + ex.getMessage());
            }
        });
        
        refreshButton.addActionListener(e -> loadOrders(orderTableModel));
        
        panel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadInventory(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);

        String sql = "SELECT * FROM clothing ORDER BY name";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getString("size"),
                    rs.getString("style"),
                    rs.getString("season"),
                    rs.getString("type"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading inventory: " + e.getMessage());
        }
    }
    
    private void loadOrders(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        
        String sql = """
            SELECT o.id, o.username, c.name, o.quantity, o.status 
            FROM orders o 
            JOIN clothing c ON o.item_id = c.id 
            ORDER BY o.status, o.id
        """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading orders: " + e.getMessage());
        }
    }
    
    private void showAddItemDialog(DefaultTableModel tableModel) {
        JDialog dialog = new JDialog((Frame) null, "Add New Item", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField nameField = new JTextField(15);
        JTextField brandField = new JTextField(15);
        JTextField sizeField = new JTextField(15);
        JTextField styleField = new JTextField(15);
        JTextField seasonField = new JTextField(15);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"TopWear", "BottomWear"});
        JTextField priceField = new JTextField(15);
        JTextField stockField = new JTextField(15);
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(brandField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Size:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(sizeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Style:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(styleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Season:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(seasonField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(typeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(priceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(stockField, gbc);
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        saveButton.addActionListener(e -> {
            try {
                ClothingItem item = new ClothingItem();
                item.setName(nameField.getText().trim());
                item.setBrand(brandField.getText().trim());
                item.setSize(sizeField.getText().trim());
                item.setStyle(styleField.getText().trim());
                item.setSeason(seasonField.getText().trim());
                item.setType((String) typeCombo.getSelectedItem());
                item.setPrice(Double.parseDouble(priceField.getText().trim()));
                item.setStock(Integer.parseInt(stockField.getText().trim()));
                
                if (item.getName().isEmpty() || item.getBrand().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name and Brand are required.");
                    return;
                }
                
                addItem(item);
                loadInventory(tableModel);
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Item added successfully.");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price and stock.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error adding item: " + ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    private void showUpdateItemDialog(DefaultTableModel tableModel, int selectedRow) {
        int itemId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentBrand = (String) tableModel.getValueAt(selectedRow, 2);
        String currentSize = (String) tableModel.getValueAt(selectedRow, 3);
        String currentStyle = (String) tableModel.getValueAt(selectedRow, 4);
        String currentSeason = (String) tableModel.getValueAt(selectedRow, 5);
        String currentType = (String) tableModel.getValueAt(selectedRow, 6);
        Double currentPrice = (Double) tableModel.getValueAt(selectedRow, 7);
        Integer currentStock = (Integer) tableModel.getValueAt(selectedRow, 8);
        
        JDialog dialog = new JDialog((Frame) null, "Update Item", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField nameField = new JTextField(currentName, 15);
        JTextField brandField = new JTextField(currentBrand, 15);
        JTextField sizeField = new JTextField(currentSize, 15);
        JTextField styleField = new JTextField(currentStyle, 15);
        JTextField seasonField = new JTextField(currentSeason, 15);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"TopWear", "BottomWear"});
        typeCombo.setSelectedItem(currentType);
        JTextField priceField = new JTextField(currentPrice.toString(), 15);
        JTextField stockField = new JTextField(currentStock.toString(), 15);
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(brandField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Size:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(sizeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Style:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(styleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Season:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(seasonField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(typeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(priceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; dialog.add(stockField, gbc);
        
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        updateButton.addActionListener(e -> {
            try {
                ClothingItem item = new ClothingItem();
                item.setId(itemId);
                item.setName(nameField.getText().trim());
                item.setBrand(brandField.getText().trim());
                item.setSize(sizeField.getText().trim());
                item.setStyle(styleField.getText().trim());
                item.setSeason(seasonField.getText().trim());
                item.setType((String) typeCombo.getSelectedItem());
                item.setPrice(Double.parseDouble(priceField.getText().trim()));
                item.setStock(Integer.parseInt(stockField.getText().trim()));
                
                if (item.getName().isEmpty() || item.getBrand().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name and Brand are required.");
                    return;
                }
                
                updateItem(item);
                loadInventory(tableModel);
                dialog.dispose();
                JOptionPane.showMessageDialog(null, "Item updated successfully.");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price and stock.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error updating item: " + ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    private void addItem(ClothingItem item) throws SQLException {
        String sql = "INSERT INTO clothing (name, brand, size, style, season, type, price, stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, item.getName());
            ps.setString(2, item.getBrand());
            ps.setString(3, item.getSize());
            ps.setString(4, item.getStyle());
            ps.setString(5, item.getSeason());
            ps.setString(6, item.getType());
            ps.setDouble(7, item.getPrice());
            ps.setInt(8, item.getStock());
            
            ps.executeUpdate();
        }
    }
    
    private void updateItem(ClothingItem item) throws SQLException {
        String sql = "UPDATE clothing SET name=?, brand=?, size=?, style=?, season=?, type=?, price=?, stock=? WHERE id=?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, item.getName());
            ps.setString(2, item.getBrand());
            ps.setString(3, item.getSize());
            ps.setString(4, item.getStyle());
            ps.setString(5, item.getSeason());
            ps.setString(6, item.getType());
            ps.setDouble(7, item.getPrice());
            ps.setInt(8, item.getStock());
            ps.setInt(9, item.getId());
            
            ps.executeUpdate();
        }
    }
    
    private void deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM clothing WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }
    
    private void updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }
}
