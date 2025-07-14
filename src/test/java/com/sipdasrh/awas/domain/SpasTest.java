package com.sipdasrh.awas.domain;

import static com.sipdasrh.awas.domain.SpasArrInstallTestSamples.*;
import static com.sipdasrh.awas.domain.SpasTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sipdasrh.awas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SpasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spas.class);
        Spas spas1 = getSpasSample1();
        Spas spas2 = new Spas();
        assertThat(spas1).isNotEqualTo(spas2);

        spas2.setId(spas1.getId());
        assertThat(spas1).isEqualTo(spas2);

        spas2 = getSpasSample2();
        assertThat(spas1).isNotEqualTo(spas2);
    }

    @Test
    void installTest() {
        Spas spas = getSpasRandomSampleGenerator();
        SpasArrInstall spasArrInstallBack = getSpasArrInstallRandomSampleGenerator();

        spas.addInstall(spasArrInstallBack);
        assertThat(spas.getInstalls()).containsOnly(spasArrInstallBack);
        assertThat(spasArrInstallBack.getSpas()).isEqualTo(spas);

        spas.removeInstall(spasArrInstallBack);
        assertThat(spas.getInstalls()).doesNotContain(spasArrInstallBack);
        assertThat(spasArrInstallBack.getSpas()).isNull();

        spas.installs(new HashSet<>(Set.of(spasArrInstallBack)));
        assertThat(spas.getInstalls()).containsOnly(spasArrInstallBack);
        assertThat(spasArrInstallBack.getSpas()).isEqualTo(spas);

        spas.setInstalls(new HashSet<>());
        assertThat(spas.getInstalls()).doesNotContain(spasArrInstallBack);
        assertThat(spasArrInstallBack.getSpas()).isNull();
    }
}
