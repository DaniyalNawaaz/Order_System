package Utils;

/**
 * Created by user on 3/28/2016.
 */
public class Product_Flavour {
    private int id;
    private int productId;
    private int flavourId;
    private String flavourName;
    private String imagePath;
    private double price;

    public Product_Flavour(){}

    public Product_Flavour(int productId,int flavourId,String flavourName,String imagePath,double price){
        this.productId = productId;
        this.flavourId = flavourId;
        this.flavourName = flavourName;
        this.imagePath = imagePath;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getFlavourId() {
        return flavourId;
    }

    public void setFlavourId(int flavourId) {
        this.flavourId = flavourId;
    }

    public String getFlavourName() {
        return flavourName;
    }

    public void setFlavourName(String flavourName) {
        this.flavourName = flavourName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
