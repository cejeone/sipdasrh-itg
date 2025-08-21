package com.sipdasrh.awas.service.mapper;

import com.sipdasrh.awas.domain.Spas;
import com.sipdasrh.awas.domain.SpasArrInstall;
import com.sipdasrh.awas.domain.SpasArrLog;
import com.sipdasrh.awas.service.dto.SpasArrInstallDTO;
import com.sipdasrh.awas.service.dto.SpasArrLogDTO;
import com.sipdasrh.awas.service.dto.SpasDTO;
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
    @Mapping(target = "spas", source = "spas", qualifiedByName = "spasId")
    SpasArrInstallDTO toDtoSpasArrInstallId(SpasArrInstall spasArrInstall);

    @Named("spasId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "namaSpas", source = "namaSpas")
    SpasDTO toDtoSpasId(Spas spas);
}
