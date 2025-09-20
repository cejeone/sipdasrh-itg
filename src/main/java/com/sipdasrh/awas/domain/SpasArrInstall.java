package com.sipdasrh.awas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SpasArrInstall.
 */
@Entity
@Table(name = "spas_arr_install")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpasArrInstall implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nama_instalasi")
    private String namaInstalasi;

    @Column(name = "url_instalasi")
    private String urlInstalasi;

    @Column(name = "lat_instalasi")
    private Double latInstalasi;

    @Column(name = "long_instalasi")
    private Double longInstalasi;

    @Column(name = "threshold_instalasi")
    private Double thresholdInstalasi;

    @Column(name = "url_ews_gis")
    private Integer urlEwsGis;

    @Column(name = "url_ews_gis_1")
    private Integer urlEwsGis1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "spasArrInstall")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "spasArrInstall" }, allowSetters = true)
    private Set<SpasArrLog> logs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "installs" }, allowSetters = true)
    private Spas spas;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SpasArrInstall id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaInstalasi() {
        return this.namaInstalasi;
    }

    public SpasArrInstall namaInstalasi(String namaInstalasi) {
        this.setNamaInstalasi(namaInstalasi);
        return this;
    }

    public void setNamaInstalasi(String namaInstalasi) {
        this.namaInstalasi = namaInstalasi;
    }

    public String getUrlInstalasi() {
        return this.urlInstalasi;
    }

    public SpasArrInstall urlInstalasi(String urlInstalasi) {
        this.setUrlInstalasi(urlInstalasi);
        return this;
    }

    public void setUrlInstalasi(String urlInstalasi) {
        this.urlInstalasi = urlInstalasi;
    }

    public Double getLatInstalasi() {
        return this.latInstalasi;
    }

    public SpasArrInstall latInstalasi(Double latInstalasi) {
        this.setLatInstalasi(latInstalasi);
        return this;
    }

    public void setLatInstalasi(Double latInstalasi) {
        this.latInstalasi = latInstalasi;
    }

    public Double getLongInstalasi() {
        return this.longInstalasi;
    }

    public SpasArrInstall longInstalasi(Double longInstalasi) {
        this.setLongInstalasi(longInstalasi);
        return this;
    }

    public void setLongInstalasi(Double longInstalasi) {
        this.longInstalasi = longInstalasi;
    }

    public Double getThresholdInstalasi() {
        return this.thresholdInstalasi;
    }

    public SpasArrInstall thresholdInstalasi(Double thresholdInstalasi) {
        this.setThresholdInstalasi(thresholdInstalasi);
        return this;
    }

    public void setThresholdInstalasi(Double thresholdInstalasi) {
        this.thresholdInstalasi = thresholdInstalasi;
    }

    public Integer getUrlEwsGis() {
        return this.urlEwsGis;
    }

    public SpasArrInstall urlEwsGis(Integer urlEwsGis) {
        this.setUrlEwsGis(urlEwsGis);
        return this;
    }

    public void setUrlEwsGis(Integer urlEwsGis) {
        this.urlEwsGis = urlEwsGis;
    }

    public Set<SpasArrLog> getLogs() {
        return this.logs;
    }

    public void setLogs(Set<SpasArrLog> spasArrLogs) {
        if (this.logs != null) {
            this.logs.forEach(i -> i.setSpasArrInstall(null));
        }
        if (spasArrLogs != null) {
            spasArrLogs.forEach(i -> i.setSpasArrInstall(this));
        }
        this.logs = spasArrLogs;
    }

    public SpasArrInstall logs(Set<SpasArrLog> spasArrLogs) {
        this.setLogs(spasArrLogs);
        return this;
    }

    public SpasArrInstall addLog(SpasArrLog spasArrLog) {
        this.logs.add(spasArrLog);
        spasArrLog.setSpasArrInstall(this);
        return this;
    }

    public SpasArrInstall removeLog(SpasArrLog spasArrLog) {
        this.logs.remove(spasArrLog);
        spasArrLog.setSpasArrInstall(null);
        return this;
    }

    public Spas getSpas() {
        return this.spas;
    }

    public void setSpas(Spas spas) {
        this.spas = spas;
    }

    public SpasArrInstall spas(Spas spas) {
        this.setSpas(spas);
        return this;
    }

    public Integer getUrlEwsGis1() {
        return urlEwsGis1;
    }

    public void setUrlEwsGis1(Integer urlEwsGis1) {
        this.urlEwsGis1 = urlEwsGis1;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpasArrInstall)) {
            return false;
        }
        return getId() != null && getId().equals(((SpasArrInstall) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpasArrInstall{" +
            "id=" + getId() +
            ", namaInstalasi='" + getNamaInstalasi() + "'" +
            ", urlInstalasi='" + getUrlInstalasi() + "'" +
            ", latInstalasi=" + getLatInstalasi() +
            ", longInstalasi=" + getLongInstalasi() +
            ", thresholdInstalasi=" + getThresholdInstalasi() +
            ", urlEwsGis=" + getUrlEwsGis() +
            "}";
    }
}
