package com.sipdasrh.awas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SpasArrLog.
 */
@Entity
@Table(name = "spas_arr_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpasArrLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "time_log")
    private ZonedDateTime timeLog;

    @Column(name = "time_retrieve")
    private ZonedDateTime timeRetrieve;

    @Column(name = "log_value")
    private String logValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "logs", "spas" }, allowSetters = true)
    private SpasArrInstall spasArrInstall;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SpasArrLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeLog() {
        return this.timeLog;
    }

    public SpasArrLog timeLog(ZonedDateTime timeLog) {
        this.setTimeLog(timeLog);
        return this;
    }

    public void setTimeLog(ZonedDateTime timeLog) {
        this.timeLog = timeLog;
    }

    public ZonedDateTime getTimeRetrieve() {
        return this.timeRetrieve;
    }

    public SpasArrLog timeRetrieve(ZonedDateTime timeRetrieve) {
        this.setTimeRetrieve(timeRetrieve);
        return this;
    }

    public void setTimeRetrieve(ZonedDateTime timeRetrieve) {
        this.timeRetrieve = timeRetrieve;
    }

    public String getLogValue() {
        return this.logValue;
    }

    public SpasArrLog logValue(String logValue) {
        this.setLogValue(logValue);
        return this;
    }

    public void setLogValue(String logValue) {
        this.logValue = logValue;
    }

    public SpasArrInstall getSpasArrInstall() {
        return this.spasArrInstall;
    }

    public void setSpasArrInstall(SpasArrInstall spasArrInstall) {
        this.spasArrInstall = spasArrInstall;
    }

    public SpasArrLog spasArrInstall(SpasArrInstall spasArrInstall) {
        this.setSpasArrInstall(spasArrInstall);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpasArrLog)) {
            return false;
        }
        return getId() != null && getId().equals(((SpasArrLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpasArrLog{" +
            "id=" + getId() +
            ", timeLog='" + getTimeLog() + "'" +
            ", timeRetrieve='" + getTimeRetrieve() + "'" +
            ", logValue='" + getLogValue() + "'" +
            "}";
    }
}
