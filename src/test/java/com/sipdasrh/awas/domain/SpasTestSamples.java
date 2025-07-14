package com.sipdasrh.awas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SpasTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Spas getSpasSample1() {
        return new Spas().id(1L).namaSpas("namaSpas1").namaManufaktur("namaManufaktur1").tipeSpas(1);
    }

    public static Spas getSpasSample2() {
        return new Spas().id(2L).namaSpas("namaSpas2").namaManufaktur("namaManufaktur2").tipeSpas(2);
    }

    public static Spas getSpasRandomSampleGenerator() {
        return new Spas()
            .id(longCount.incrementAndGet())
            .namaSpas(UUID.randomUUID().toString())
            .namaManufaktur(UUID.randomUUID().toString())
            .tipeSpas(intCount.incrementAndGet());
    }
}
