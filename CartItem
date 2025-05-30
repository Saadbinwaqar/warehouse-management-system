public class CartItem {
    private ClothingItem item;
    private int quantity;
    
    public CartItem(ClothingItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
    
    public ClothingItem getItem() { return item; }
    public void setItem(ClothingItem item) { this.item = item; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getTotalPrice() {
        return item.getPrice() * quantity;
    }
    
    public String toString() {
        return item.getName() + " x" + quantity + " = $" + String.format("%.2f", getTotalPrice());
    }
}
