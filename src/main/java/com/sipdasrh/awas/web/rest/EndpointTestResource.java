package com.sipdasrh.awas.web.rest;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import com.sipdasrh.awas.service.SpasSensorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/end-point")
public class EndpointTestResource {
    private static final String ENTITY_NAME = "serviceEndpointTest";
    private static final Logger LOG = LoggerFactory.getLogger(EndpointTestResource.class);
    private final SpasSensorService spasSensorService;

    public EndpointTestResource(SpasSensorService spasSensorService) {
        this.spasSensorService = spasSensorService;
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken() throws Exception {
        LOG.info("Get Token API : Start");
        String token = spasSensorService.getAccessTokenFromGis();
        LOG.info("Get Token API : End");
        return ResponseEntity.ok().body(token);
    }

}
