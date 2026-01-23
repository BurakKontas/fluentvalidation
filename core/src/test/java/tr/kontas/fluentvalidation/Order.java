package tr.kontas.fluentvalidation;

public class Order {
    private boolean expressShipping;
    private boolean premiumMember;
    private double totalAmount;
    private ShippingSpeed shippingSpeed;
    private String paymentMethod;
    private boolean pickup;
    private boolean digitalProduct;
    private boolean international;
    private double customsValue;
    private String deliveryAddress;

    // Getters and setters...
    public boolean isExpressShipping() { return expressShipping; }
    public void setExpressShipping(boolean expressShipping) { this.expressShipping = expressShipping; }
    public boolean isPremiumMember() { return premiumMember; }
    public void setPremiumMember(boolean premiumMember) { this.premiumMember = premiumMember; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public ShippingSpeed getShippingSpeed() { return shippingSpeed; }
    public void setShippingSpeed(ShippingSpeed shippingSpeed) { this.shippingSpeed = shippingSpeed; }
}
