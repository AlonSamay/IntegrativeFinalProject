package smartspace.layout.Product;

import smartspace.data.Catalog.Product.ProductColor;
import smartspace.data.Catalog.Product.ProductEntity;
import smartspace.data.Catalog.Product.ProductRate;
import smartspace.data.Catalog.Product.ProductSize;
import smartspace.layout.Product.ProductCatalogBoundry;

public class ProductOverviewBoundry extends ProductCatalogBoundry {

    private static final String COLOR = "color";
    private static final String DESCRIPTION = "description";
    private static final String SIZE = "size";
    private static final String RATE = "rate";

    private String color;
    private String description;
    private String size;
    private String rate;

    public ProductOverviewBoundry() {
    }

    public ProductOverviewBoundry(ProductEntity entity) {
        super(entity);
        this.color = (String)entity.getMoreAttributes().get(COLOR);
        this.description = (String)entity.getMoreAttributes().get(DESCRIPTION);
        this.size = (String)entity.getMoreAttributes().get(SIZE);
        this.rate = (String)entity.getMoreAttributes().get(RATE);
    }

    public ProductEntity convertToEntity(){
        ProductEntity entity = super.convertToEntity();

        entity.setColor(ProductColor.valueOf(this.color));
        entity.setDescription(this.description);
        entity.setSize(ProductSize.valueOf(this.size));
        entity.setRate(ProductRate.valueOf(rate));

        return entity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
