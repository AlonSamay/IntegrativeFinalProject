package smartspace.data;

public class EmailAddress {
    private String address;

    public EmailAddress(){
    }
    public EmailAddress(String mail){
        this.address = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        return address.equals(((EmailAddress)obj).address);
    }
}
