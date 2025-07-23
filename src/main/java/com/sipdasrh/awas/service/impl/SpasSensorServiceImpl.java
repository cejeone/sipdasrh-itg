package com.sipdasrh.awas.service.impl;

import com.sipdasrh.awas.config.ApplicationProperties;
import com.sipdasrh.awas.domain.SpasArrInstall;
import com.sipdasrh.awas.domain.SpasArrLog;
import com.sipdasrh.awas.repository.SpasArrInstallRepository;
import com.sipdasrh.awas.repository.SpasArrLogRepository;
import com.sipdasrh.awas.service.SpasSensorService;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.net.ssl.*;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SpasSensorServiceImpl implements SpasSensorService {

    @Autowired
    RestTemplate restTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(SpasSensorServiceImpl.class);
    private final SpasArrInstallRepository spasArrInstallRepository;
    private final SpasArrLogRepository spasArrLogRepository;
    private final ApplicationProperties applicationProperties;

    public SpasSensorServiceImpl(
        SpasArrInstallRepository spasArrInstallRepository,
        SpasArrLogRepository spasArrLogRepository,
        ApplicationProperties applicationProperties
    ) {
        this.spasArrInstallRepository = spasArrInstallRepository;
        this.spasArrLogRepository = spasArrLogRepository;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void getDataFromSensors() {
        try {
            LOG.info("Get Data From Sensors API : Start at {}", LocalDate.now());
            List<SpasArrInstall> spasArrLogList = spasArrInstallRepository.findAll();

            String arcgisToken = getAccessTokenFromGis();
            spasArrLogList.forEach(spasArrLog -> {
                LOG.info("Get Data From Sensors API : Sensor {}", spasArrLog.getNamaInstalasi());
                String res = restTemplate.getForObject(spasArrLog.getUrlInstalasi(), String.class);
                JsonParser springParser = JsonParserFactory.getJsonParser();
                Map<String, Object> mapResult = springParser.parseMap(res);
                if (mapResult.get("statusCode").equals(200)) {
                    LOG.info("Extract Data From Sensors API : {}", spasArrLog.getNamaInstalasi());
                    Map<String, Object> sensorData = (Map<String, Object>) mapResult.get("data");
                    String lastSending = sensorData.get("last_sending").toString();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
                    ZonedDateTime localizedDate = ZonedDateTime.parse(lastSending, formatter);
                    // Start Parsing JSON from IoT
                    Map<String, Object> sensorDetail = (Map<String, Object>) sensorData.get("sensor");
                    Map<String, Object> waterLevel = (Map<String, Object>) sensorDetail.get("Water Level");
                    Integer waterLevelInteger = (int) waterLevel.get("value_actual");
                    //                        String waterLevelUnit = waterLevel.get("unit").toString();

                    Map<String, Object> batteryDetail = (Map<String, Object>) sensorDetail.get("Battery");
                    Double batteryLevelInteger = (Double) batteryDetail.get("value_actual");
                    //                        String batteryLevelUnit = waterLevel.get("unit").toString();

                    Map<String, Object> rainDetail = (Map<String, Object>) sensorDetail.get("Rainfall");
                    Integer rainLevelInteger = (int) rainDetail.get("value_actual");
                    //                        String rainLevelUnit = waterLevel.get("unit").toString();
                    // End Parsing
                    SpasArrLog newData = new SpasArrLog()
                        .logValue(sensorDetail.toString())
                        .timeLog(localizedDate)
                        .timeRetrieve(ZonedDateTime.now())
                        .batteryLevel(batteryLevelInteger)
                        .rainLevel((double) rainLevelInteger)
                        .waterLevel((double) waterLevelInteger)
                        .spasArrInstall(spasArrLog);
                    spasArrLogRepository.saveAndFlush(newData);

                    // Sync to Gis Service
                    try {
                        Boolean isSynced = postToServiceGis(
                            spasArrLog.getUrlEwsGis(),
                            (double) waterLevelInteger,
                            batteryLevelInteger,
                            spasArrLog.getThresholdInstalasi(),
                            (double) rainLevelInteger,
                            arcgisToken
                        );
                        if (isSynced) LOG.info(" Synced to GIS Service : ObjectID={}", spasArrLog.getUrlEwsGis());
                    } catch (Exception e) {
                        LOG.error("Error : {}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            });
            LOG.info("Get Data From Sensors API : End at {}", LocalDate.now());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getAccessTokenFromGis() throws Exception {
        LOG.info("Get Token From GIS Service : {}", applicationProperties.getExternalGis().getUrlToken());
        //Set Headers and other configurations if needed
        String baseGisUrlToken = applicationProperties.getExternalGis().getUrlToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        // Set header for APPLICATION_FORM
        body.add("username", applicationProperties.getExternalGis().getUserName());
        body.add("password", applicationProperties.getExternalGis().getPassword());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        // TODO: remove it in prod -> configureTrustAllSSL()
        configureTrustAllSSL();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(baseGisUrlToken, request, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            LOG.info("GIS Token Retrieved Successfully: {}", responseEntity.getBody());
            JsonParser springParser = JsonParserFactory.getJsonParser();
            Map<String, Object> mapResult = springParser.parseMap(responseEntity.getBody());
            return mapResult.get("token").toString();
        }
        return "";
    }

    @Override
    public Boolean postToServiceGis(int objectId, Double ketinggian, Double voltBattery, Double thresHold, Double curahHujan, String token)
        throws Exception {
        LOG.info("Starting Post to GIS : {}", applicationProperties.getExternalGis().getUrlService());
        String baseGisUrlService = applicationProperties.getExternalGis().getUrlService();
        //Set Header Authorization and Form Payload
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String postBody = formatPostRequest(objectId, ketinggian, voltBattery, thresHold, curahHujan);
        body.add("features", postBody);
        body.add("f", "pjson");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // TODO: remove it in prod -> configureTrusAllSSL()
        configureTrustAllSSL();
        ResponseEntity<String> res = restTemplate.postForEntity(baseGisUrlService, request, String.class);
        if (res.getStatusCode().is2xxSuccessful()) {
            LOG.info("Succesfully POST to GIS : {}", res.getBody());
            return true;
        }
        return false;
    }

    /**
     * Configures SSL to trust all certificates and bypass hostname verification.
     * This allows the application to connect to servers with untrusted or self-signed certificates.
     * WARNING: This approach is insecure for production use.
     */
    private static void configureTrustAllSSL() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing - trust all clients
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing - trust all servers
                }
            },
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    /**
     * Private function to generate format of payload that accepted by arcgis service
     * @param objectId
     * @param ketinggian
     * @param voltBattery
     * @param thresHold
     * @param curahHujan
     * @return
     */
    private static String formatPostRequest(int objectId, Double ketinggian, Double voltBattery, Double thresHold, Double curahHujan) {
        //[{"attributes": {"objectid": 2,"status" : "normal"},}]
        String currentStatus = determineCurrentStatus(ketinggian, thresHold);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("[{\"attributes\":{\"objectid\":")
            .append(objectId)
            .append(",\"status\":")
            .append(currentStatus)
            .append(",\"ketinggian\":")
            .append(ketinggian)
            .append(",\"battery\":")
            .append(voltBattery)
            .append(",\"curah_huja\":")
            .append(curahHujan)
            .append("},}]");
        return stringBuilder.toString();
    }

    /**
     * Function to determine 'Status' Of Each Observable DAS area
     * @param ketinggian
     * @param threshold
     * @return
     */
    private static String determineCurrentStatus(Double ketinggian, Double threshold) {
        if (threshold - ketinggian > 10) return "aman";
        if ((threshold - ketinggian <= 10) && (threshold - ketinggian >= 0)) return "hati-hati";
        if (threshold - ketinggian < 0) return "siaga";

        return "tidak terdeteksi";
    }
}
