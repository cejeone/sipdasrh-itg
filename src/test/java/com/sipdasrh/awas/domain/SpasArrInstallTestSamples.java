package com.sipdasrh.awas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpasArrInstallTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SpasArrInstall getSpasArrInstallSample1() {
        return new SpasArrInstall().id(1L).namaInstalasi("namaInstalasi1").urlInstalasi("urlInstalasi1").urlEwsGis("urlEwsGis1");
    }

    public static SpasArrInstall getSpasArrInstallSample2() {
        return new SpasArrInstall().id(2L).namaInstalasi("namaInstalasi2").urlInstalasi("urlInstalasi2").urlEwsGis("urlEwsGis2");
    }

    public static SpasArrInstall getSpasArrInstallRandomSampleGenerator() {
        return new SpasArrInstall()
            .id(longCount.incrementAndGet())
            .namaInstalasi(UUID.randomUUID().toString())
            .urlInstalasi(UUID.randomUUID().toString())
            .urlEwsGis(UUID.randomUUID().toString());
    }
}
