package com.sipdasrh.awas.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.sipdasrh.awas.domain.SpasArrLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpasArrLogDTO implements Serializable {

    private Long id;

    private ZonedDateTime timeLog;

    private ZonedDateTime timeRetrieve;

    private String logValue;

    private Double waterLevel;

    private Double batteryLevel;

    private Double rainLevel;

    private SpasArrInstallDTO spasArrInstall;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeLog() {
        return timeLog;
    }

    public void setTimeLog(ZonedDateTime timeLog) {
        this.timeLog = timeLog;
    }

    public ZonedDateTime getTimeRetrieve() {
        return timeRetrieve;
    }

    public void setTimeRetrieve(ZonedDateTime timeRetrieve) {
        this.timeRetrieve = timeRetrieve;
    }

    public String getLogValue() {
        return logValue;
    }

    public void setLogValue(String logValue) {
        this.logValue = logValue;
    }

    public Double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(Double waterLevel) {
        this.waterLevel = waterLevel;
    }

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Double getRainLevel() {
        return rainLevel;
    }

    public void setRainLevel(Double rainLevel) {
        this.rainLevel = rainLevel;
    }

    public SpasArrInstallDTO getSpasArrInstall() {
        return spasArrInstall;
    }

    public void setSpasArrInstall(SpasArrInstallDTO spasArrInstall) {
        this.spasArrInstall = spasArrInstall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpasArrLogDTO)) {
            return false;
        }

        SpasArrLogDTO spasArrLogDTO = (SpasArrLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, spasArrLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpasArrLogDTO{" +
            "id=" + getId() +
            ", timeLog='" + getTimeLog() + "'" +
            ", timeRetrieve='" + getTimeRetrieve() + "'" +
            ", logValue='" + getLogValue() + "'" +
            ", waterLevel=" + getWaterLevel() +
            ", batteryLevel=" + getBatteryLevel() +
            ", rainLevel=" + getRainLevel() +
            ", spasArrInstall=" + getSpasArrInstall() +
            "}";
    }
}
