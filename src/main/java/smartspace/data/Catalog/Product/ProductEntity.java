package smartspace.data.Catalog.Product;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

import java.util.Date;
import java.util.Map;

public class ProductEntity extends ElementEntity {
    private String imageUrl; //src in client
    private String price;
    private int amount;
    private String description;
    private ProductColor color;
    private ProductSize size;
    private ProductRate rate;

    private static final String PRICE = "price";
    private static final String IMAGE_URL = "imageUrl";
    private static final String AMOUNT = "amount";
    private static final String COLOR = "color";
    private static final String DESC = "description";
    private static final String SIZE = "size";
    private static final String RATE = "rate";


    public  ProductEntity(){

    }

    public ProductEntity(String name, String type, Location location, Date creationTimeStamp,
                         String creatorEmail, String creatorSmartSpace, boolean expired, Map<String, Object> moreAttributes) {
        super(name, "product", location, creationTimeStamp, creatorEmail, creatorSmartSpace, expired, moreAttributes);
        this.imageUrl = (String) moreAttributes.get(IMAGE_URL);
        this.price = (String) moreAttributes.get(PRICE);
        this.amount = (int) moreAttributes.get(AMOUNT);
        this.description = (String) moreAttributes.get(DESC);
        this.color =(ProductColor) moreAttributes.get(COLOR);
        this.size = (ProductSize) moreAttributes.get(SIZE);
        this.rate = (ProductRate) moreAttributes.get(RATE);
    }

    public ProductColor getColor() {
        return color;
    }

    public void setColor(ProductColor color) {
        this.color = color;
    }

    public ProductSize getSize() {
        return size;
    }

    public void setSize(ProductSize productSize) {
        this.size = productSize;
    }

    public ProductRate getRate() {
        return rate;
    }

    public void setRate(ProductRate rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAmmount(int amount) {
        this.amount = amount;
    }

    public int getAmmount() {
        return amount;
    }
}
