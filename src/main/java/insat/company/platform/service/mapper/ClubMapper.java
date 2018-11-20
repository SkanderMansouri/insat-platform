package insat.company.platform.service.mapper;

import insat.company.platform.domain.*;
import insat.company.platform.service.dto.ClubDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Club and its DTO ClubDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClubMapper extends EntityMapper<ClubDTO, Club> {



    default Club fromId(Long id) {
        if (id == null) {
            return null;
        }
        Club club = new Club();
        club.setId(id);
        return club;
    }
}
