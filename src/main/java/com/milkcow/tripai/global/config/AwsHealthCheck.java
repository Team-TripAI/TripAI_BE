package com.milkcow.tripai.global.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwsHealthCheck {

    @GetMapping("/aws")
    public ResponseEntity awsEndpoint() {
        return ResponseEntity.ok("HealthCheckOk");
    }

}
