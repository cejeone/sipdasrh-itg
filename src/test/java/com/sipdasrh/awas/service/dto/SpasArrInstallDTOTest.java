package com.sipdasrh.awas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sipdasrh.awas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpasArrInstallDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpasArrInstallDTO.class);
        SpasArrInstallDTO spasArrInstallDTO1 = new SpasArrInstallDTO();
        spasArrInstallDTO1.setId(1L);
        SpasArrInstallDTO spasArrInstallDTO2 = new SpasArrInstallDTO();
        assertThat(spasArrInstallDTO1).isNotEqualTo(spasArrInstallDTO2);
        spasArrInstallDTO2.setId(spasArrInstallDTO1.getId());
        assertThat(spasArrInstallDTO1).isEqualTo(spasArrInstallDTO2);
        spasArrInstallDTO2.setId(2L);
        assertThat(spasArrInstallDTO1).isNotEqualTo(spasArrInstallDTO2);
        spasArrInstallDTO1.setId(null);
        assertThat(spasArrInstallDTO1).isNotEqualTo(spasArrInstallDTO2);
    }
}
