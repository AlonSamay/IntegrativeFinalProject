package smartspace.data.util;

import org.springframework.stereotype.Component;
import smartspace.data.*;

import java.util.Date;
import java.util.Map;

@Component
public class EntityFactoryImpl implements EntityFactory {


    @Override
    public ActionEntity createNewAction(String elementId,
                                        String elementSmartSpace,
                                        String actionType,
                                        Date creationTimeStamp,
                                        String playerEmail,
                                        String playerSmartSpace,
                                        Map<String, Object> moreAttributes) {
        return new ActionEntity(
                elementId,
                elementSmartSpace,
                actionType,
                creationTimeStamp,
                playerEmail,
                playerSmartSpace,
                moreAttributes);
    }

    @Override
    public ElementEntity createNewElement(String name,
                                          String type,
                                          Location location,
                                          Date creationTimeStamp,
                                          String creatorEmail,
                                          String creatorSmartSpace,
                                          boolean expired,
                                          Map<String, Object> moreAttributes) {
        return new ElementEntity(
                name,
                type,
                location,
                creationTimeStamp,
                creatorEmail,
                creatorSmartSpace,
                expired,
                moreAttributes);
    }

    @Override
    public UserEntity createNewUser(String username,
                                    String avatar,
                                    UserRole role,
                                    Long points) {
        return new UserEntity(
                username,
                avatar,
                role,
                points);
    }
}
