package smartspace.data.util;

import smartspace.data.*;

import java.util.Date;
import java.util.Map;

public interface EntityFactory {

    ActionEntity
    createNewAction(
            String elementId,
            String elementSmartSpace,
            String actionType,
            Date creationTimeStamp,
            String playerEmail,
            String playerSmartSpace,
            Map<String,Object> moreAttributes
            );

    ElementEntity
    createNewElement(
            String name,
            String type,
            Location location,
            Date creationTimeStamp,
            String creatorEmail,
            String creatorSmartSpace,
            boolean expired,
            Map<String,Object> moreAttributes
    );

    UserEntity
    createNewUser(
            String userSmartSpace,
            String username,
            String avatar,
            UserRole role,
            Long points
    );
}


