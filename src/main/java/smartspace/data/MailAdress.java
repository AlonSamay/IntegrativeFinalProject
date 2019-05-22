package smartspace.data;

import java.util.Objects;

public class MailAdress {
    private String mail;

    public MailAdress(){

    }
    public MailAdress(String mail){
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object obj) {
        return mail.equals(((MailAdress)obj).mail);
    }
}
