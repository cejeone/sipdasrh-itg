package com.sipdasrh.awas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sipdasrh.awas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpasDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpasDTO.class);
        SpasDTO spasDTO1 = new SpasDTO();
        spasDTO1.setId(1L);
        SpasDTO spasDTO2 = new SpasDTO();
        assertThat(spasDTO1).isNotEqualTo(spasDTO2);
        spasDTO2.setId(spasDTO1.getId());
        assertThat(spasDTO1).isEqualTo(spasDTO2);
        spasDTO2.setId(2L);
        assertThat(spasDTO1).isNotEqualTo(spasDTO2);
        spasDTO1.setId(null);
        assertThat(spasDTO1).isNotEqualTo(spasDTO2);
    }
}
