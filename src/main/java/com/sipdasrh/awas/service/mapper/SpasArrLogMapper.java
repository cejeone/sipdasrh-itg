package com.sipdasrh.awas.service.mapper;

import com.sipdasrh.awas.domain.SpasArrInstall;
import com.sipdasrh.awas.domain.SpasArrLog;
import com.sipdasrh.awas.service.dto.SpasArrInstallDTO;
import com.sipdasrh.awas.service.dto.SpasArrLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpasArrLog} and its DTO {@link SpasArrLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpasArrLogMapper extends EntityMapper<SpasArrLogDTO, SpasArrLog> {
    @Mapping(target = "spasArrInstall", source = "spasArrInstall", qualifiedByName = "spasArrInstallId")
    SpasArrLogDTO toDto(SpasArrLog s);

    @Named("spasArrInstallId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "namaInstalasi", source = "namaInstalasi")
    SpasArrInstallDTO toDtoSpasArrInstallId(SpasArrInstall spasArrInstall);
}
