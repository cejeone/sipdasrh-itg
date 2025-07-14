package com.sipdasrh.awas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sipdasrh.awas.domain.Spas} entity. This class is used
 * in {@link com.sipdasrh.awas.web.rest.SpasResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /spas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpasCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter namaSpas;

    private StringFilter namaManufaktur;

    private IntegerFilter tipeSpas;

    private LongFilter installId;

    private Boolean distinct;

    public SpasCriteria() {}

    public SpasCriteria(SpasCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.namaSpas = other.optionalNamaSpas().map(StringFilter::copy).orElse(null);
        this.namaManufaktur = other.optionalNamaManufaktur().map(StringFilter::copy).orElse(null);
        this.tipeSpas = other.optionalTipeSpas().map(IntegerFilter::copy).orElse(null);
        this.installId = other.optionalInstallId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SpasCriteria copy() {
        return new SpasCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNamaSpas() {
        return namaSpas;
    }

    public Optional<StringFilter> optionalNamaSpas() {
        return Optional.ofNullable(namaSpas);
    }

    public StringFilter namaSpas() {
        if (namaSpas == null) {
            setNamaSpas(new StringFilter());
        }
        return namaSpas;
    }

    public void setNamaSpas(StringFilter namaSpas) {
        this.namaSpas = namaSpas;
    }

    public StringFilter getNamaManufaktur() {
        return namaManufaktur;
    }

    public Optional<StringFilter> optionalNamaManufaktur() {
        return Optional.ofNullable(namaManufaktur);
    }

    public StringFilter namaManufaktur() {
        if (namaManufaktur == null) {
            setNamaManufaktur(new StringFilter());
        }
        return namaManufaktur;
    }

    public void setNamaManufaktur(StringFilter namaManufaktur) {
        this.namaManufaktur = namaManufaktur;
    }

    public IntegerFilter getTipeSpas() {
        return tipeSpas;
    }

    public Optional<IntegerFilter> optionalTipeSpas() {
        return Optional.ofNullable(tipeSpas);
    }

    public IntegerFilter tipeSpas() {
        if (tipeSpas == null) {
            setTipeSpas(new IntegerFilter());
        }
        return tipeSpas;
    }

    public void setTipeSpas(IntegerFilter tipeSpas) {
        this.tipeSpas = tipeSpas;
    }

    public LongFilter getInstallId() {
        return installId;
    }

    public Optional<LongFilter> optionalInstallId() {
        return Optional.ofNullable(installId);
    }

    public LongFilter installId() {
        if (installId == null) {
            setInstallId(new LongFilter());
        }
        return installId;
    }

    public void setInstallId(LongFilter installId) {
        this.installId = installId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SpasCriteria that = (SpasCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(namaSpas, that.namaSpas) &&
            Objects.equals(namaManufaktur, that.namaManufaktur) &&
            Objects.equals(tipeSpas, that.tipeSpas) &&
            Objects.equals(installId, that.installId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, namaSpas, namaManufaktur, tipeSpas, installId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpasCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNamaSpas().map(f -> "namaSpas=" + f + ", ").orElse("") +
            optionalNamaManufaktur().map(f -> "namaManufaktur=" + f + ", ").orElse("") +
            optionalTipeSpas().map(f -> "tipeSpas=" + f + ", ").orElse("") +
            optionalInstallId().map(f -> "installId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
