package com.sipdasrh.awas.service.mapper;

import com.sipdasrh.awas.domain.Spas;
import com.sipdasrh.awas.service.dto.SpasDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Spas} and its DTO {@link SpasDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpasMapper extends EntityMapper<SpasDTO, Spas> {}
