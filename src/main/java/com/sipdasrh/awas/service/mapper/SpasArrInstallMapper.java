package com.sipdasrh.awas.service.mapper;

import com.sipdasrh.awas.domain.Spas;
import com.sipdasrh.awas.domain.SpasArrInstall;
import com.sipdasrh.awas.service.dto.SpasArrInstallDTO;
import com.sipdasrh.awas.service.dto.SpasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpasArrInstall} and its DTO {@link SpasArrInstallDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpasArrInstallMapper extends EntityMapper<SpasArrInstallDTO, SpasArrInstall> {
    @Mapping(target = "spas", source = "spas", qualifiedByName = "spasId")
    SpasArrInstallDTO toDto(SpasArrInstall s);

    @Named("spasId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SpasDTO toDtoSpasId(Spas spas);
}
