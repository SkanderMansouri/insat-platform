package com.pif.insatplatform.service.mapper;

import com.pif.insatplatform.domain.*;
import com.pif.insatplatform.service.dto.SlackUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SlackUser and its DTO SlackUserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SlackUserMapper extends EntityMapper<SlackUserDTO, SlackUser> {



    default SlackUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        SlackUser slackUser = new SlackUser();
        slackUser.setId(id);
        return slackUser;
    }
}
