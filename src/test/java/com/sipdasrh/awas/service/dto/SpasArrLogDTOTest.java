package com.sipdasrh.awas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sipdasrh.awas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpasArrLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpasArrLogDTO.class);
        SpasArrLogDTO spasArrLogDTO1 = new SpasArrLogDTO();
        spasArrLogDTO1.setId(1L);
        SpasArrLogDTO spasArrLogDTO2 = new SpasArrLogDTO();
        assertThat(spasArrLogDTO1).isNotEqualTo(spasArrLogDTO2);
        spasArrLogDTO2.setId(spasArrLogDTO1.getId());
        assertThat(spasArrLogDTO1).isEqualTo(spasArrLogDTO2);
        spasArrLogDTO2.setId(2L);
        assertThat(spasArrLogDTO1).isNotEqualTo(spasArrLogDTO2);
        spasArrLogDTO1.setId(null);
        assertThat(spasArrLogDTO1).isNotEqualTo(spasArrLogDTO2);
    }
}
