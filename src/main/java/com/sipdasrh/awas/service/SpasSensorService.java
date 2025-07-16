package com.sipdasrh.awas.service;

public interface SpasSensorService {
    /**
     * Get Data From Listed Sensors
     * @return void
     */
    void getDataFromSensors();
    /**
     * Get Access Token From GIS Service
     * @return String access token
     */
    String getAccessTokenFromGis() throws Exception;

    /**
     * Post to 3rd party GIS application Service
     *
     * @param objectId
     * @param ketinggian
     * @param voltBattery
     * @param thresHold
     * @param curahHujan
     * @return
     */
    Boolean postToServiceGis(int objectId, Double ketinggian, Double voltBattery, Double thresHold, Double curahHujan, String token)
        throws Exception;
}
