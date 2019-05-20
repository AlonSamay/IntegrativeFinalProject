package smartspace.logic;

import org.apache.commons.validator.routines.EmailValidator;
import smartspace.data.MailAdress;
import smartspace.data.UserRole;

import java.util.Map;

public class Validator {

    boolean isValid(String str) {
        return str != null && !str.trim().isEmpty();
    }

//    <E> boolean isValid(Class <E> x) {
//       return x != null;
//    }

    boolean isValid (UserRole role){
        return role != null;
    }

    boolean isValid(MailAdress mail){
        return EmailValidator.getInstance().isValid(mail.getMail());
    }

    boolean isValid(Long x) {
        return x != null && x > 0;
    }

    boolean isValid(Double x) {
        return x != null && x > 0;
    }

    boolean isValid(Map<String,Object> checkMap) {
        for (String key : checkMap.keySet())
            if(!this.isValid(key) || checkMap.get(key) == null)
                return false;
        return true;
    }


}
