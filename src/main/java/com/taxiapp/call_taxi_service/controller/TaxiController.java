package com.taxiapp.call_taxi_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taxiapp.call_taxi_service.model.Taxi;
import com.taxiapp.call_taxi_service.service.TaxiService;

@RestController
@RequestMapping("/api/calltaxi")
public class TaxiController {

    private static final Logger logger = LoggerFactory.getLogger(TaxiController.class);
    @Autowired
    private TaxiService taxiService;

    @PostMapping("/addVehicle") // Custom endpoint
    public ResponseEntity<?> addVehicle(@RequestBody Taxi taxi) {
        logger.info("<<< Invoke addVehicle >>>" + taxi.toString());
        Map<String, Object> response = new HashMap<>();
        if (taxi.getVehicleNo() == null || taxi.getVehicleNo().isEmpty()) {
            response.put("error", "Vehicle number cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            Taxi savedTaxi = taxiService.addVehicle(taxi);
            logger.info("Vehicle added successfully");
            response.put("status", "success");
            response.put("message", "Vehicle added successfully");
            response.put("data", savedTaxi);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (DataIntegrityViolationException e) { // Catches constraint violations
            response.put("status", "error");
            response.put("message", "Vehicle with this number already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to add vehicle");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getAllVehicles")
    public ResponseEntity<?> getAllVehicles() {
        logger.info("<<< Invoke getAllVehicles >>>");
        Map<String, Object> response = new HashMap<>();
        try {
            List<Taxi> vehicles = taxiService.getAllVehicles();
            logger.info("Successfully retrieved all vehicles");
            response.put("status", "success");
            response.put("message", "Vehicles retrieved successfully");
            response.put("data", vehicles);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving vehicles: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to retrieve vehicles");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
