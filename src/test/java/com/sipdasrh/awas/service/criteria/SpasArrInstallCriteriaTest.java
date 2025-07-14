package com.sipdasrh.awas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SpasArrInstallCriteriaTest {

    @Test
    void newSpasArrInstallCriteriaHasAllFiltersNullTest() {
        var spasArrInstallCriteria = new SpasArrInstallCriteria();
        assertThat(spasArrInstallCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void spasArrInstallCriteriaFluentMethodsCreatesFiltersTest() {
        var spasArrInstallCriteria = new SpasArrInstallCriteria();

        setAllFilters(spasArrInstallCriteria);

        assertThat(spasArrInstallCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void spasArrInstallCriteriaCopyCreatesNullFilterTest() {
        var spasArrInstallCriteria = new SpasArrInstallCriteria();
        var copy = spasArrInstallCriteria.copy();

        assertThat(spasArrInstallCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(spasArrInstallCriteria)
        );
    }

    @Test
    void spasArrInstallCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var spasArrInstallCriteria = new SpasArrInstallCriteria();
        setAllFilters(spasArrInstallCriteria);

        var copy = spasArrInstallCriteria.copy();

        assertThat(spasArrInstallCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(spasArrInstallCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var spasArrInstallCriteria = new SpasArrInstallCriteria();

        assertThat(spasArrInstallCriteria).hasToString("SpasArrInstallCriteria{}");
    }

    private static void setAllFilters(SpasArrInstallCriteria spasArrInstallCriteria) {
        spasArrInstallCriteria.id();
        spasArrInstallCriteria.namaInstalasi();
        spasArrInstallCriteria.urlInstalasi();
        spasArrInstallCriteria.latInstalasi();
        spasArrInstallCriteria.longInstalasi();
        spasArrInstallCriteria.thresholdInstalasi();
        spasArrInstallCriteria.urlEwsGis();
        spasArrInstallCriteria.logId();
        spasArrInstallCriteria.spasId();
        spasArrInstallCriteria.distinct();
    }

    private static Condition<SpasArrInstallCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNamaInstalasi()) &&
                condition.apply(criteria.getUrlInstalasi()) &&
                condition.apply(criteria.getLatInstalasi()) &&
                condition.apply(criteria.getLongInstalasi()) &&
                condition.apply(criteria.getThresholdInstalasi()) &&
                condition.apply(criteria.getUrlEwsGis()) &&
                condition.apply(criteria.getLogId()) &&
                condition.apply(criteria.getSpasId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SpasArrInstallCriteria> copyFiltersAre(
        SpasArrInstallCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNamaInstalasi(), copy.getNamaInstalasi()) &&
                condition.apply(criteria.getUrlInstalasi(), copy.getUrlInstalasi()) &&
                condition.apply(criteria.getLatInstalasi(), copy.getLatInstalasi()) &&
                condition.apply(criteria.getLongInstalasi(), copy.getLongInstalasi()) &&
                condition.apply(criteria.getThresholdInstalasi(), copy.getThresholdInstalasi()) &&
                condition.apply(criteria.getUrlEwsGis(), copy.getUrlEwsGis()) &&
                condition.apply(criteria.getLogId(), copy.getLogId()) &&
                condition.apply(criteria.getSpasId(), copy.getSpasId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
