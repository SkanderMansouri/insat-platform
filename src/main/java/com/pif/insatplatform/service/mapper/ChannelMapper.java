package com.pif.insatplatform.service.mapper;

import com.pif.insatplatform.domain.*;
import com.pif.insatplatform.service.dto.ChannelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Channel and its DTO ChannelDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChannelMapper extends EntityMapper<ChannelDTO, Channel> {



    default Channel fromId(Long id) {
        if (id == null) {
            return null;
        }
        Channel channel = new Channel();
        channel.setId(id);
        return channel;
    }
}
