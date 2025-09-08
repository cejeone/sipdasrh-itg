package com.sipdasrh.awas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Spas.
 */
@Entity
@Table(name = "spas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Spas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nama_spas")
    private String namaSpas;

    @Column(name = "nama_manufaktur")
    private String namaManufaktur;

    @Column(name = "tipe_spas")
    private Integer tipeSpas;

    public String getNamaBpdas() {
        return namaBpdas;
    }

    public void setNamaBpdas(String namaBpdas) {
        this.namaBpdas = namaBpdas;
    }

    @Column(name = "nama_bpdas")
    private String namaBpdas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "spas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "logs", "spas" }, allowSetters = true)
    private Set<SpasArrInstall> installs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Spas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaSpas() {
        return this.namaSpas;
    }

    public Spas namaSpas(String namaSpas) {
        this.setNamaSpas(namaSpas);
        return this;
    }

    public void setNamaSpas(String namaSpas) {
        this.namaSpas = namaSpas;
    }

    public String getNamaManufaktur() {
        return this.namaManufaktur;
    }

    public Spas namaManufaktur(String namaManufaktur) {
        this.setNamaManufaktur(namaManufaktur);
        return this;
    }

    public void setNamaManufaktur(String namaManufaktur) {
        this.namaManufaktur = namaManufaktur;
    }

    public Integer getTipeSpas() {
        return this.tipeSpas;
    }

    public Spas tipeSpas(Integer tipeSpas) {
        this.setTipeSpas(tipeSpas);
        return this;
    }

    public void setTipeSpas(Integer tipeSpas) {
        this.tipeSpas = tipeSpas;
    }

    public Set<SpasArrInstall> getInstalls() {
        return this.installs;
    }

    public void setInstalls(Set<SpasArrInstall> spasArrInstalls) {
        if (this.installs != null) {
            this.installs.forEach(i -> i.setSpas(null));
        }
        if (spasArrInstalls != null) {
            spasArrInstalls.forEach(i -> i.setSpas(this));
        }
        this.installs = spasArrInstalls;
    }

    public Spas installs(Set<SpasArrInstall> spasArrInstalls) {
        this.setInstalls(spasArrInstalls);
        return this;
    }

    public Spas addInstall(SpasArrInstall spasArrInstall) {
        this.installs.add(spasArrInstall);
        spasArrInstall.setSpas(this);
        return this;
    }

    public Spas removeInstall(SpasArrInstall spasArrInstall) {
        this.installs.remove(spasArrInstall);
        spasArrInstall.setSpas(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spas)) {
            return false;
        }
        return getId() != null && getId().equals(((Spas) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Spas{" +
            "id=" + getId() +
            ", namaSpas='" + getNamaSpas() + "'" +
            ", namaManufaktur='" + getNamaManufaktur() + "'" +
            ", tipeSpas=" + getTipeSpas() +
            "}";
    }
}
