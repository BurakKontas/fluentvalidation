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
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public boolean isPickup() { return pickup; }
    public void setPickup(boolean pickup) { this.pickup = pickup; }
    public boolean isDigitalProduct() { return digitalProduct; }
    public void setDigitalProduct(boolean digitalProduct) { this.digitalProduct = digitalProduct; }
    public boolean isInternational() { return international; }
    public void setInternational(boolean international) { this.international = international; }
    public double getCustomsValue() { return customsValue; }
    public void setCustomsValue(double customsValue) { this.customsValue = customsValue; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
}
