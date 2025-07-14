package com.sipdasrh.awas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SpasCriteriaTest {

    @Test
    void newSpasCriteriaHasAllFiltersNullTest() {
        var spasCriteria = new SpasCriteria();
        assertThat(spasCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void spasCriteriaFluentMethodsCreatesFiltersTest() {
        var spasCriteria = new SpasCriteria();

        setAllFilters(spasCriteria);

        assertThat(spasCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void spasCriteriaCopyCreatesNullFilterTest() {
        var spasCriteria = new SpasCriteria();
        var copy = spasCriteria.copy();

        assertThat(spasCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(spasCriteria)
        );
    }

    @Test
    void spasCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var spasCriteria = new SpasCriteria();
        setAllFilters(spasCriteria);

        var copy = spasCriteria.copy();

        assertThat(spasCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(spasCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var spasCriteria = new SpasCriteria();

        assertThat(spasCriteria).hasToString("SpasCriteria{}");
    }

    private static void setAllFilters(SpasCriteria spasCriteria) {
        spasCriteria.id();
        spasCriteria.namaSpas();
        spasCriteria.namaManufaktur();
        spasCriteria.tipeSpas();
        spasCriteria.installId();
        spasCriteria.distinct();
    }

    private static Condition<SpasCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNamaSpas()) &&
                condition.apply(criteria.getNamaManufaktur()) &&
                condition.apply(criteria.getTipeSpas()) &&
                condition.apply(criteria.getInstallId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SpasCriteria> copyFiltersAre(SpasCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNamaSpas(), copy.getNamaSpas()) &&
                condition.apply(criteria.getNamaManufaktur(), copy.getNamaManufaktur()) &&
                condition.apply(criteria.getTipeSpas(), copy.getTipeSpas()) &&
                condition.apply(criteria.getInstallId(), copy.getInstallId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
