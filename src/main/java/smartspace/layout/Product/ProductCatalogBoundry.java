package smartspace.layout.Product;

import smartspace.data.Catalog.Product.ProductEntity;
import smartspace.layout.ElementBoundary;

public class ProductCatalogBoundry extends ElementBoundary {

    private static final String PRICE = "price";
    private static final String IMAGE_URL = "imageUrl";
    private static final String AMOUNT = "amount";

    private String imageUrl; //src in client
    private String price;
    private int amount;

    public ProductCatalogBoundry() {

    }
    public ProductCatalogBoundry(ProductEntity entity) {
        // elementEntity
        super(entity);

        // product entity fields
        this.imageUrl = (String) entity.getMoreAttributes().get(IMAGE_URL);
        this.price = (String) entity.getMoreAttributes().get(PRICE);
        this.amount = (int) entity.getMoreAttributes().get(AMOUNT);

    }

    public ProductEntity convertToEntity(){
        ProductEntity entity = (ProductEntity) super.convertToEntity();

        // product entity fields
        entity.setImageUrl(this.imageUrl);
        entity.setPrice(this.price);
        entity.setAmmount(this.amount);

        return entity;

    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
