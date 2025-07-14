package com.sipdasrh.awas.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sipdasrh.awas.domain.SpasArrInstall} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpasArrInstallDTO implements Serializable {

    private Long id;

    private String namaInstalasi;

    private String urlInstalasi;

    private Double latInstalasi;

    private Double longInstalasi;

    private Double thresholdInstalasi;

    private String urlEwsGis;

    private SpasDTO spas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaInstalasi() {
        return namaInstalasi;
    }

    public void setNamaInstalasi(String namaInstalasi) {
        this.namaInstalasi = namaInstalasi;
    }

    public String getUrlInstalasi() {
        return urlInstalasi;
    }

    public void setUrlInstalasi(String urlInstalasi) {
        this.urlInstalasi = urlInstalasi;
    }

    public Double getLatInstalasi() {
        return latInstalasi;
    }

    public void setLatInstalasi(Double latInstalasi) {
        this.latInstalasi = latInstalasi;
    }

    public Double getLongInstalasi() {
        return longInstalasi;
    }

    public void setLongInstalasi(Double longInstalasi) {
        this.longInstalasi = longInstalasi;
    }

    public Double getThresholdInstalasi() {
        return thresholdInstalasi;
    }

    public void setThresholdInstalasi(Double thresholdInstalasi) {
        this.thresholdInstalasi = thresholdInstalasi;
    }

    public String getUrlEwsGis() {
        return urlEwsGis;
    }

    public void setUrlEwsGis(String urlEwsGis) {
        this.urlEwsGis = urlEwsGis;
    }

    public SpasDTO getSpas() {
        return spas;
    }

    public void setSpas(SpasDTO spas) {
        this.spas = spas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpasArrInstallDTO)) {
            return false;
        }

        SpasArrInstallDTO spasArrInstallDTO = (SpasArrInstallDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, spasArrInstallDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpasArrInstallDTO{" +
            "id=" + getId() +
            ", namaInstalasi='" + getNamaInstalasi() + "'" +
            ", urlInstalasi='" + getUrlInstalasi() + "'" +
            ", latInstalasi=" + getLatInstalasi() +
            ", longInstalasi=" + getLongInstalasi() +
            ", thresholdInstalasi=" + getThresholdInstalasi() +
            ", urlEwsGis='" + getUrlEwsGis() + "'" +
            ", spas=" + getSpas() +
            "}";
    }
}
