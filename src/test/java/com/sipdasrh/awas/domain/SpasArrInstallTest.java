package com.sipdasrh.awas.domain;

import static com.sipdasrh.awas.domain.SpasArrInstallTestSamples.*;
import static com.sipdasrh.awas.domain.SpasArrLogTestSamples.*;
import static com.sipdasrh.awas.domain.SpasTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.sipdasrh.awas.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SpasArrInstallTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpasArrInstall.class);
        SpasArrInstall spasArrInstall1 = getSpasArrInstallSample1();
        SpasArrInstall spasArrInstall2 = new SpasArrInstall();
        assertThat(spasArrInstall1).isNotEqualTo(spasArrInstall2);

        spasArrInstall2.setId(spasArrInstall1.getId());
        assertThat(spasArrInstall1).isEqualTo(spasArrInstall2);

        spasArrInstall2 = getSpasArrInstallSample2();
        assertThat(spasArrInstall1).isNotEqualTo(spasArrInstall2);
    }

    @Test
    void logTest() {
        SpasArrInstall spasArrInstall = getSpasArrInstallRandomSampleGenerator();
        SpasArrLog spasArrLogBack = getSpasArrLogRandomSampleGenerator();

        spasArrInstall.addLog(spasArrLogBack);
        assertThat(spasArrInstall.getLogs()).containsOnly(spasArrLogBack);
        assertThat(spasArrLogBack.getSpasArrInstall()).isEqualTo(spasArrInstall);

        spasArrInstall.removeLog(spasArrLogBack);
        assertThat(spasArrInstall.getLogs()).doesNotContain(spasArrLogBack);
        assertThat(spasArrLogBack.getSpasArrInstall()).isNull();

        spasArrInstall.logs(new HashSet<>(Set.of(spasArrLogBack)));
        assertThat(spasArrInstall.getLogs()).containsOnly(spasArrLogBack);
        assertThat(spasArrLogBack.getSpasArrInstall()).isEqualTo(spasArrInstall);

        spasArrInstall.setLogs(new HashSet<>());
        assertThat(spasArrInstall.getLogs()).doesNotContain(spasArrLogBack);
        assertThat(spasArrLogBack.getSpasArrInstall()).isNull();
    }

    @Test
    void spasTest() {
        SpasArrInstall spasArrInstall = getSpasArrInstallRandomSampleGenerator();
        Spas spasBack = getSpasRandomSampleGenerator();

        spasArrInstall.setSpas(spasBack);
        assertThat(spasArrInstall.getSpas()).isEqualTo(spasBack);

        spasArrInstall.spas(null);
        assertThat(spasArrInstall.getSpas()).isNull();
    }
}
