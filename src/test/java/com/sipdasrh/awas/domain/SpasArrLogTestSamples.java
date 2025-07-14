package com.sipdasrh.awas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpasArrLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SpasArrLog getSpasArrLogSample1() {
        return new SpasArrLog().id(1L).logValue("logValue1");
    }

    public static SpasArrLog getSpasArrLogSample2() {
        return new SpasArrLog().id(2L).logValue("logValue2");
    }

    public static SpasArrLog getSpasArrLogRandomSampleGenerator() {
        return new SpasArrLog().id(longCount.incrementAndGet()).logValue(UUID.randomUUID().toString());
    }
}
