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

public class Customer extends User {
    private List<CartItem> cart;
    private JTabbedPane tabbedPane;
    
    public Customer(String username) {
        super(username);
        this.cart = new ArrayList<>();
    }
    
    public void showDashboard() {
        JFrame frame = new JFrame("Customer Dashboard - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ImageIcon icon = new ImageIcon("C:/Users/HP/Downloads/make an icon for an imaginary clothing store.png");
        frame.setIconImage(icon.getImage());
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Shop", createShoppingPanel());
        
        tabbedPane.addTab("Cart (0)", createCartPanel());
        
        tabbedPane.addTab("Order History", createOrderHistoryPanel());
        
        frame.add(tabbedPane);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void updateCartTabTitle() {
        if (tabbedPane != null) {
            tabbedPane.setTitleAt(1, "Cart (" + cart.size() + ")");
        }
    }
    
    private JPanel createShoppingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"ID", "Name", "Brand", "Size", "Style", "Season", "Type", "Price", "Stock"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable itemsTable = new JTable(tableModel);
        itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        loadAvailableItems(tableModel);
        

        JPanel addToCartPanel = new JPanel(new FlowLayout());
        JLabel quantityLabel = new JLabel("Quantity:");
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton addToCartButton = new JButton("Add to Cart");
        JButton refreshButton = new JButton("Refresh");
        
        addToCartPanel.add(quantityLabel);
        addToCartPanel.add(quantitySpinner);
        addToCartPanel.add(addToCartButton);
        addToCartPanel.add(refreshButton);
        
        addToCartButton.addActionListener(e -> {
            int selectedRow = itemsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select an item to add to cart.");
                return;
            }
            
            try {
                int itemId = (Integer) tableModel.getValueAt(selectedRow, 0);
                int requestedQuantity = (Integer) quantitySpinner.getValue();
                int availableStock = (Integer) tableModel.getValueAt(selectedRow, 8);
                
                if (requestedQuantity > availableStock) {
                    JOptionPane.showMessageDialog(panel, 
                        "Only " + availableStock + " items available in stock.");
                    return;
                }
                
                ClothingItem item = getItemById(itemId);
                if (item != null) {
                    addToCart(item, requestedQuantity);
                    updateCartTabTitle();
                    JOptionPane.showMessageDialog(panel, 
                        "Added " + requestedQuantity + " x " + item.getName() + " to cart.\nCart now has " + cart.size() + " item(s).");
                    quantitySpinner.setValue(1);
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });
        
        refreshButton.addActionListener(e -> loadAvailableItems(tableModel));
        
        panel.add(new JScrollPane(itemsTable), BorderLayout.CENTER);
        panel.add(addToCartPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultListModel<CartItem> cartListModel = new DefaultListModel<>();
        JList<CartItem> cartList = new JList<>(cartListModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        updateCartDisplay(cartListModel);
        

        JLabel totalLabel = new JLabel("Total: $0.00");
        updateTotalLabel(totalLabel);
        

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Remove Item");
        JButton clearCartButton = new JButton("Clear Cart");
        JButton checkoutButton = new JButton("Checkout");
        JButton refreshCartButton = new JButton("Refresh Cart");
        
        buttonPanel.add(removeButton);
        buttonPanel.add(clearCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(refreshCartButton);
        
        
        removeButton.addActionListener(e -> {
            CartItem selectedItem = cartList.getSelectedValue();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(panel, "Please select an item to remove.");
                return;
            }
            
            cart.remove(selectedItem);
            updateCartDisplay(cartListModel);
            updateTotalLabel(totalLabel);
            updateCartTabTitle();
            JOptionPane.showMessageDialog(panel, "Item removed from cart.");
        });
        
        clearCartButton.addActionListener(e -> {
            if (!cart.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(panel,
                    "Are you sure you want to clear the cart?",
                    "Clear Cart", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    cart.clear();
                    updateCartDisplay(cartListModel);
                    updateTotalLabel(totalLabel);
                    updateCartTabTitle();
                    JOptionPane.showMessageDialog(panel, "Cart cleared.");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Cart is already empty.");
            }
        });
        
        checkoutButton.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Cart is empty.");
                return;
            }
            
            try {
                processCheckout();
                cart.clear();
                updateCartDisplay(cartListModel);
                updateTotalLabel(totalLabel);
                updateCartTabTitle();
                JOptionPane.showMessageDialog(panel, "Order placed successfully!");
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error processing order: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage());
            }
        });
        
        refreshCartButton.addActionListener(e -> {
            updateCartDisplay(cartListModel);
            updateTotalLabel(totalLabel);
            updateCartTabTitle();
        });
        
    
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Shopping Cart", SwingConstants.CENTER), BorderLayout.NORTH);
        topPanel.add(totalLabel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createOrderHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        

        String[] columnNames = {"Order ID", "Item", "Brand", "Quantity", "Price", "Total", "Status"};
        DefaultTableModel orderTableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable orderHistoryTable = new JTable(orderTableModel);
        loadOrderHistory(orderTableModel);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadOrderHistory(orderTableModel));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        
        panel.add(new JLabel("Order History", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(orderHistoryTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadAvailableItems(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        
        String sql = "SELECT * FROM clothing WHERE stock > 0 ORDER BY name";
        
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
            JOptionPane.showMessageDialog(null, "Error loading items: " + e.getMessage());
        }
    }
    
    private ClothingItem getItemById(int itemId) throws SQLException {
        String sql = "SELECT * FROM clothing WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, itemId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClothingItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("size"),
                        rs.getString("style"),
                        rs.getString("season"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }
        }
        return null;
    }
    
    private void addToCart(ClothingItem item, int quantity) {
        System.out.println("Adding to cart: " + item.getName() + " x" + quantity);
        
        for (CartItem cartItem : cart) {
            if (cartItem.getItem().getId() == item.getId()) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                System.out.println("Updated existing cart item. New quantity: " + cartItem.getQuantity());
                return;
            }
        }
        
        cart.add(new CartItem(item, quantity));
        System.out.println("Added new item to cart. Cart size now: " + cart.size());
    }
    
    private void updateCartDisplay(DefaultListModel<CartItem> listModel) {
        listModel.clear();
        System.out.println("Updating cart display. Cart has " + cart.size() + " items.");
        for (CartItem cartItem : cart) {
            listModel.addElement(cartItem);
            System.out.println("Added to display: " + cartItem.toString());
        }
    }
    
    private void updateTotalLabel(JLabel totalLabel) {
        double total = cart.stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }
    
    private void processCheckout() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        
        try {
            conn.setAutoCommit(false);
            
            for (CartItem cartItem : cart) {
                String checkStockSql = "SELECT stock FROM clothing WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(checkStockSql)) {
                    ps.setInt(1, cartItem.getItem().getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException("Item not found: " + cartItem.getItem().getName());
                        }
                        int availableStock = rs.getInt("stock");
                        if (availableStock < cartItem.getQuantity()) {
                            throw new IllegalArgumentException(
                                "Insufficient stock for " + cartItem.getItem().getName() + 
                                ". Available: " + availableStock + ", Requested: " + cartItem.getQuantity());
                        }
                    }
                }
            }
            
            String insertOrderSql = "INSERT INTO orders (username, item_id, quantity, status) VALUES (?, ?, ?, 'Pending')";
            String updateStockSql = "UPDATE clothing SET stock = stock - ? WHERE id = ?";
            
            try (PreparedStatement orderPs = conn.prepareStatement(insertOrderSql);
                 PreparedStatement stockPs = conn.prepareStatement(updateStockSql)) {
                
                for (CartItem cartItem : cart) {
                    orderPs.setString(1, username);
                    orderPs.setInt(2, cartItem.getItem().getId());
                    orderPs.setInt(3, cartItem.getQuantity());
                    orderPs.executeUpdate();
                    
                    stockPs.setInt(1, cartItem.getQuantity());
                    stockPs.setInt(2, cartItem.getItem().getId());
                    stockPs.executeUpdate();
                }
            }
            
            conn.commit();
            
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
    
    private void loadOrderHistory(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        
        String sql = """
            SELECT o.id, c.name, c.brand, o.quantity, c.price, o.status 
            FROM orders o 
            JOIN clothing c ON o.item_id = c.id 
            WHERE o.username = ? 
            ORDER BY o.id DESC
        """;
        
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");
                    double total = price * quantity;
                    
                    Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        quantity,
                        "$" + String.format("%.2f", price),
                        "$" + String.format("%.2f", total),
                        rs.getString("status")
                    };
                    tableModel.addRow(row);
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading order history: " + e.getMessage());
        }
    }
}
