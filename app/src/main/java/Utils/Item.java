package Utils;

/**
 * Created by user on 3/17/2016.
 */
public class Item {
    private String image_path;
    private String flavour;
    private int Quantity;
    private int flavourId;
    private int itemId;
    private String ItemName;
    private double price;
    private double discount_price;
    private int orderId;
    private double discount_per_product;

    public Item(){}

    public Item(int itemId, String image_path, String flavour, int Quantity, String ItemName, double price){
        this.itemId = itemId;
        this.image_path = image_path;
        this.flavour = flavour;
        this.Quantity = Quantity;
        this.ItemName = ItemName;
        this.price = price;
    }

    public Item(int itemId, String image_path, String flavour, int Quantity, String ItemName, double price, double discount_price){
        this.itemId = itemId;
        this.image_path = image_path;
        this.flavour = flavour;
        this.Quantity = Quantity;
        this.ItemName = ItemName;
        this.price = price;
        this.discount_price = discount_price;
    }

    public Item(String image_path, String flavour, int Quantity){
        this.image_path = image_path;
        this.flavour = flavour;
        this.Quantity = Quantity;
    }

    public Item(String ItemName, int Quantity, double price){
        this.ItemName = ItemName;
        this.Quantity = Quantity;
        this.price = price;
    }

    public Item(String ItemName, int Quantity, double price,double discount_per_product){
        this.ItemName = ItemName;
        this.Quantity = Quantity;
        this.price = price;
        this.discount_per_product = discount_per_product;
    }

    public double getDiscount_per_product() {
        return discount_per_product;
    }

    public void setDiscount_per_product(double discount_per_product) {
        this.discount_per_product = discount_per_product;
    }

    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    public int getFlavourId() {
        return flavourId;
    }

    public void setFlavourId(int flavourId) {
        this.flavourId = flavourId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
