package com.sipdasrh.awas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SpasArrLogCriteriaTest {

    @Test
    void newSpasArrLogCriteriaHasAllFiltersNullTest() {
        var spasArrLogCriteria = new SpasArrLogCriteria();
        assertThat(spasArrLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void spasArrLogCriteriaFluentMethodsCreatesFiltersTest() {
        var spasArrLogCriteria = new SpasArrLogCriteria();

        setAllFilters(spasArrLogCriteria);

        assertThat(spasArrLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void spasArrLogCriteriaCopyCreatesNullFilterTest() {
        var spasArrLogCriteria = new SpasArrLogCriteria();
        var copy = spasArrLogCriteria.copy();

        assertThat(spasArrLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(spasArrLogCriteria)
        );
    }

    @Test
    void spasArrLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var spasArrLogCriteria = new SpasArrLogCriteria();
        setAllFilters(spasArrLogCriteria);

        var copy = spasArrLogCriteria.copy();

        assertThat(spasArrLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(spasArrLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var spasArrLogCriteria = new SpasArrLogCriteria();

        assertThat(spasArrLogCriteria).hasToString("SpasArrLogCriteria{}");
    }

    private static void setAllFilters(SpasArrLogCriteria spasArrLogCriteria) {
        spasArrLogCriteria.id();
        spasArrLogCriteria.timeLog();
        spasArrLogCriteria.timeRetrieve();
        spasArrLogCriteria.logValue();
        spasArrLogCriteria.spasArrInstallId();
        spasArrLogCriteria.distinct();
    }

    private static Condition<SpasArrLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTimeLog()) &&
                condition.apply(criteria.getTimeRetrieve()) &&
                condition.apply(criteria.getLogValue()) &&
                condition.apply(criteria.getSpasArrInstallId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SpasArrLogCriteria> copyFiltersAre(SpasArrLogCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTimeLog(), copy.getTimeLog()) &&
                condition.apply(criteria.getTimeRetrieve(), copy.getTimeRetrieve()) &&
                condition.apply(criteria.getLogValue(), copy.getLogValue()) &&
                condition.apply(criteria.getSpasArrInstallId(), copy.getSpasArrInstallId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
