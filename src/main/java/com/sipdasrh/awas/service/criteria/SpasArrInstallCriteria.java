package com.sipdasrh.awas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.sipdasrh.awas.domain.SpasArrInstall} entity. This class is used
 * in {@link com.sipdasrh.awas.web.rest.SpasArrInstallResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /spas-arr-installs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpasArrInstallCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter namaInstalasi;

    private StringFilter urlInstalasi;

    private DoubleFilter latInstalasi;

    private DoubleFilter longInstalasi;

    private DoubleFilter thresholdInstalasi;

    private StringFilter urlEwsGis;

    private LongFilter logId;

    private LongFilter spasId;

    private Boolean distinct;

    public SpasArrInstallCriteria() {}

    public SpasArrInstallCriteria(SpasArrInstallCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.namaInstalasi = other.optionalNamaInstalasi().map(StringFilter::copy).orElse(null);
        this.urlInstalasi = other.optionalUrlInstalasi().map(StringFilter::copy).orElse(null);
        this.latInstalasi = other.optionalLatInstalasi().map(DoubleFilter::copy).orElse(null);
        this.longInstalasi = other.optionalLongInstalasi().map(DoubleFilter::copy).orElse(null);
        this.thresholdInstalasi = other.optionalThresholdInstalasi().map(DoubleFilter::copy).orElse(null);
        this.urlEwsGis = other.optionalUrlEwsGis().map(StringFilter::copy).orElse(null);
        this.logId = other.optionalLogId().map(LongFilter::copy).orElse(null);
        this.spasId = other.optionalSpasId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SpasArrInstallCriteria copy() {
        return new SpasArrInstallCriteria(this);
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

    public StringFilter getNamaInstalasi() {
        return namaInstalasi;
    }

    public Optional<StringFilter> optionalNamaInstalasi() {
        return Optional.ofNullable(namaInstalasi);
    }

    public StringFilter namaInstalasi() {
        if (namaInstalasi == null) {
            setNamaInstalasi(new StringFilter());
        }
        return namaInstalasi;
    }

    public void setNamaInstalasi(StringFilter namaInstalasi) {
        this.namaInstalasi = namaInstalasi;
    }

    public StringFilter getUrlInstalasi() {
        return urlInstalasi;
    }

    public Optional<StringFilter> optionalUrlInstalasi() {
        return Optional.ofNullable(urlInstalasi);
    }

    public StringFilter urlInstalasi() {
        if (urlInstalasi == null) {
            setUrlInstalasi(new StringFilter());
        }
        return urlInstalasi;
    }

    public void setUrlInstalasi(StringFilter urlInstalasi) {
        this.urlInstalasi = urlInstalasi;
    }

    public DoubleFilter getLatInstalasi() {
        return latInstalasi;
    }

    public Optional<DoubleFilter> optionalLatInstalasi() {
        return Optional.ofNullable(latInstalasi);
    }

    public DoubleFilter latInstalasi() {
        if (latInstalasi == null) {
            setLatInstalasi(new DoubleFilter());
        }
        return latInstalasi;
    }

    public void setLatInstalasi(DoubleFilter latInstalasi) {
        this.latInstalasi = latInstalasi;
    }

    public DoubleFilter getLongInstalasi() {
        return longInstalasi;
    }

    public Optional<DoubleFilter> optionalLongInstalasi() {
        return Optional.ofNullable(longInstalasi);
    }

    public DoubleFilter longInstalasi() {
        if (longInstalasi == null) {
            setLongInstalasi(new DoubleFilter());
        }
        return longInstalasi;
    }

    public void setLongInstalasi(DoubleFilter longInstalasi) {
        this.longInstalasi = longInstalasi;
    }

    public DoubleFilter getThresholdInstalasi() {
        return thresholdInstalasi;
    }

    public Optional<DoubleFilter> optionalThresholdInstalasi() {
        return Optional.ofNullable(thresholdInstalasi);
    }

    public DoubleFilter thresholdInstalasi() {
        if (thresholdInstalasi == null) {
            setThresholdInstalasi(new DoubleFilter());
        }
        return thresholdInstalasi;
    }

    public void setThresholdInstalasi(DoubleFilter thresholdInstalasi) {
        this.thresholdInstalasi = thresholdInstalasi;
    }

    public StringFilter getUrlEwsGis() {
        return urlEwsGis;
    }

    public Optional<StringFilter> optionalUrlEwsGis() {
        return Optional.ofNullable(urlEwsGis);
    }

    public StringFilter urlEwsGis() {
        if (urlEwsGis == null) {
            setUrlEwsGis(new StringFilter());
        }
        return urlEwsGis;
    }

    public void setUrlEwsGis(StringFilter urlEwsGis) {
        this.urlEwsGis = urlEwsGis;
    }

    public LongFilter getLogId() {
        return logId;
    }

    public Optional<LongFilter> optionalLogId() {
        return Optional.ofNullable(logId);
    }

    public LongFilter logId() {
        if (logId == null) {
            setLogId(new LongFilter());
        }
        return logId;
    }

    public void setLogId(LongFilter logId) {
        this.logId = logId;
    }

    public LongFilter getSpasId() {
        return spasId;
    }

    public Optional<LongFilter> optionalSpasId() {
        return Optional.ofNullable(spasId);
    }

    public LongFilter spasId() {
        if (spasId == null) {
            setSpasId(new LongFilter());
        }
        return spasId;
    }

    public void setSpasId(LongFilter spasId) {
        this.spasId = spasId;
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
        final SpasArrInstallCriteria that = (SpasArrInstallCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(namaInstalasi, that.namaInstalasi) &&
            Objects.equals(urlInstalasi, that.urlInstalasi) &&
            Objects.equals(latInstalasi, that.latInstalasi) &&
            Objects.equals(longInstalasi, that.longInstalasi) &&
            Objects.equals(thresholdInstalasi, that.thresholdInstalasi) &&
            Objects.equals(urlEwsGis, that.urlEwsGis) &&
            Objects.equals(logId, that.logId) &&
            Objects.equals(spasId, that.spasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            namaInstalasi,
            urlInstalasi,
            latInstalasi,
            longInstalasi,
            thresholdInstalasi,
            urlEwsGis,
            logId,
            spasId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpasArrInstallCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNamaInstalasi().map(f -> "namaInstalasi=" + f + ", ").orElse("") +
            optionalUrlInstalasi().map(f -> "urlInstalasi=" + f + ", ").orElse("") +
            optionalLatInstalasi().map(f -> "latInstalasi=" + f + ", ").orElse("") +
            optionalLongInstalasi().map(f -> "longInstalasi=" + f + ", ").orElse("") +
            optionalThresholdInstalasi().map(f -> "thresholdInstalasi=" + f + ", ").orElse("") +
            optionalUrlEwsGis().map(f -> "urlEwsGis=" + f + ", ").orElse("") +
            optionalLogId().map(f -> "logId=" + f + ", ").orElse("") +
            optionalSpasId().map(f -> "spasId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
