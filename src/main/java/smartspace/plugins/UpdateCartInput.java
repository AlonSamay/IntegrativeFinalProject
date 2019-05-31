package smartspace.plugins;

import smartspace.data.ElementEntity;
import smartspace.data.UserKey;

public class UpdateCartInput {
    private UserKey player;
    private ElementEntity[] products;
    private float amoount;
    private String adresss;

    public UpdateCartInput() {
    }

    public UserKey getPlayer() {
        return player;
    }

    public void setPlayer(UserKey player) {
        this.player = player;
    }

    public ElementEntity[] getProducts() {
        return products;
    }

    public void setProducts(ElementEntity[] products) {
        this.products = products;
    }

    public float getAmoount() {
        return amoount;
    }

    public void setAmoount(float amoount) {
        this.amoount = amoount;
    }

    public String getAdresss() {
        return adresss;
    }

    public void setAdresss(String adresss) {
        this.adresss = adresss;
    }


}
