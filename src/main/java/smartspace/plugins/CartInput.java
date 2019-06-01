package smartspace.plugins;

import smartspace.data.UserKey;

import java.util.Date;

public class CartInput {
    //TODO : add validator for fields as cartOwnerId, prdoucts and amount
    private UserKey cartOwnerId;
    private String[] products;
    private float amount;
    private String address;
    private String creditCardNumber;
    private Date expiryDate;
    private int cvv;
    private int creditCardOwnerId;


    public CartInput() {
    }
    public CartInput(UserKey cartOwnerId){
        this.cartOwnerId = cartOwnerId;
        this.amount=0;
        this.address ="N/A";
    }

    public UserKey getCartOwnerId() {
        return cartOwnerId;
    }

    public void setCartOwnerId(UserKey cartOwnerId) {
        this.cartOwnerId = cartOwnerId;
    }

    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public int getCreditCardOwnerId() {
        return creditCardOwnerId;
    }

    public void setCreditCardOwnerId(int creditCardOwnerId) {
        this.creditCardOwnerId = creditCardOwnerId;
    }


}
