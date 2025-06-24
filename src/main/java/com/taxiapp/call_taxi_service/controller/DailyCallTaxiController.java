package com.taxiapp.call_taxi_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.taxiapp.call_taxi_service.model.DailyCallTaxi;
import com.taxiapp.call_taxi_service.service.DailyCallTaxiService;

@RestController
@RequestMapping("/api/daily-taxi-rides")
public class DailyCallTaxiController {

    private static final Logger logger = LoggerFactory.getLogger(DailyCallTaxiController.class);

    @Autowired
    private DailyCallTaxiService dailyCallTaxiService;

    @PostMapping("/createentry")
    public ResponseEntity<?> addDailyTaxiDetails(@RequestBody DailyCallTaxi details) {
        logger.info("<<< Invoke addDailyTaxiDetails >>>");
        Map<String, Object> response = new HashMap<>();
        if (details.getVehicleNo() == null || details.getVehicleNo().isEmpty()) {
            throw new IllegalArgumentException("Vehicle number cannot be empty");
        }
        try {
            DailyCallTaxi savedDailyCallTaxi = dailyCallTaxiService.addDailyTaxiDetails(details);
            logger.info("Vehicle added successfully");
            response.put("status", "success");
            response.put("message", "Daily Taxi entry added successfully.");
            response.put("data", savedDailyCallTaxi);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to add daily call taxi details.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/recordinfo")
    public ResponseEntity<?> getAllDailyTaxiDetails() {
        logger.info("<<< Invoke getAllDailyTaxiDetails >>>");
        Map<String, Object> response = new HashMap<>();
        try {
            List<DailyCallTaxi> dailyCallTaxis = dailyCallTaxiService.getAllDailyTaxiDetails();
            logger.info("Successfully retrieved all vehicles");
            response.put("status", "success");
            response.put("message", "Daily Call Taxi details retrieved successfully.");
            response.put("data", dailyCallTaxis);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving vehicles: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to retrieve daily call taxi details.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/updateentry/{seq}")
    public ResponseEntity<?> updateDailyTaxiDetails(@PathVariable Long seq,
            @RequestBody DailyCallTaxi updatedDetails) {
        logger.info("<<< Invoke updateDailyTaxiDetails >>>");
        Map<String, Object> response = new HashMap<>();

        // Validate required fields
        if (updatedDetails.getVehicleNo() == null || updatedDetails.getVehicleNo().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Vehicle number cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            DailyCallTaxi updatedTaxi = dailyCallTaxiService.updateDailyTaxiDetails(seq, updatedDetails);
            logger.info("Daily Vehicle details updated successfully");
            response.put("status", "success");
            response.put("message", "Daily call taxi details updated successfully.");
            response.put("data", updatedTaxi);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Error updating vehicle details: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Failed to update daily call taxi details.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
