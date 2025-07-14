package com.sipdasrh.awas.service.mapper;

import static com.sipdasrh.awas.domain.SpasArrInstallAsserts.*;
import static com.sipdasrh.awas.domain.SpasArrInstallTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpasArrInstallMapperTest {

    private SpasArrInstallMapper spasArrInstallMapper;

    @BeforeEach
    void setUp() {
        spasArrInstallMapper = new SpasArrInstallMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSpasArrInstallSample1();
        var actual = spasArrInstallMapper.toEntity(spasArrInstallMapper.toDto(expected));
        assertSpasArrInstallAllPropertiesEquals(expected, actual);
    }
}
