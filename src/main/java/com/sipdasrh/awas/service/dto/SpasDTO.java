package com.sipdasrh.awas.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sipdasrh.awas.domain.Spas} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpasDTO implements Serializable {

    private Long id;

    private String namaSpas;

    private String namaManufaktur;

    private Integer tipeSpas;

    public String getNamaBpdas() {
        return namaBpdas;
    }

    public void setNamaBpdas(String namaBpdas) {
        this.namaBpdas = namaBpdas;
    }

    private String namaBpdas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaSpas() {
        return namaSpas;
    }

    public void setNamaSpas(String namaSpas) {
        this.namaSpas = namaSpas;
    }

    public String getNamaManufaktur() {
        return namaManufaktur;
    }

    public void setNamaManufaktur(String namaManufaktur) {
        this.namaManufaktur = namaManufaktur;
    }

    public Integer getTipeSpas() {
        return tipeSpas;
    }

    public void setTipeSpas(Integer tipeSpas) {
        this.tipeSpas = tipeSpas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpasDTO)) {
            return false;
        }

        SpasDTO spasDTO = (SpasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, spasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpasDTO{" +
            "id=" + getId() +
            ", namaSpas='" + getNamaSpas() + "'" +
            ", namaManufaktur='" + getNamaManufaktur() + "'" +
            ", tipeSpas=" + getTipeSpas() +
            "}";
    }
}
