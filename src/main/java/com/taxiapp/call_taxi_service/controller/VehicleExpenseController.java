package com.taxiapp.call_taxi_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taxiapp.call_taxi_service.model.VehicleExpense;
import com.taxiapp.call_taxi_service.service.VehicleExpenseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle-expense")
public class VehicleExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleExpenseController.class);

    @Autowired
    private VehicleExpenseService vehicleExpenseService;

    // Add Vehicle Expense
    @PostMapping("/add-expense")
    public ResponseEntity<?> addVehicleExpense(@RequestBody VehicleExpense expense) {
        logger.info("<<< Invoke addVehicleExpense >>> " + expense.toString());
        Map<String, Object> response = new HashMap<>();

        // Validate required fields
        if (expense.getVehicleNo() == null || expense.getVehicleNo().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Vehicle number cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (expense.getFuelType() == null || expense.getFuelType().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Fuel type (Petrol/Diesel/Gas) cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (expense.getAmount() == null || expense.getAmount() <= 0) {
            response.put("status", "error");
            response.put("message", "Expense amount must be greater than zero");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            VehicleExpense savedExpense = vehicleExpenseService.addExpense(expense);
            response.put("status", "success");
            response.put("message", "Vehicle expense added successfully");
            response.put("data", savedExpense);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            response.put("status", "error");
            response.put("message", "Duplicate vehicle expense entry detected.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            logger.error("Error adding vehicle expense: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Failed to add vehicle expense");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get all vehicle expenses
    @GetMapping("/list-expenses")
    public ResponseEntity<?> getAllVehicleExpenses() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<VehicleExpense> dailyCallTaxis = vehicleExpenseService.getAllExpenses();
            logger.info("Successfully retrieved all vehicles");
            response.put("status", "success");
            response.put("message", "Vechile expense details retrieved successfully.");
            response.put("data", dailyCallTaxis);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving vehicles: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to retrieve vechile expense details.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get expenses by vehicle number
    @GetMapping("/expense/{vehicleNo}")
    public ResponseEntity<?> getExpensesByVehicleNo(@PathVariable String vehicleNo) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<VehicleExpense> dailyCallTaxis = vehicleExpenseService.getExpensesByVehicleNo(vehicleNo);
            logger.info("Successfully retrieved all vehicles");
            response.put("status", "success");
            response.put("message", "Vechile expense details retrieved successfully.");
            response.put("data", dailyCallTaxis);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving vehicles: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to retrieve vechile expense details.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Update Vehicle Expense
    @PutMapping("/modify-expense/{id}")
    public ResponseEntity<?> updateVehicleExpense(@PathVariable Long id, @RequestBody VehicleExpense updatedExpense) {
        Map<String, Object> response = new HashMap<>();
        try {
            VehicleExpense updated = vehicleExpenseService.updateExpense(id, updatedExpense);
            logger.info("Daily Vehicle details updated successfully");
            response.put("status", "success");
            response.put("message", "Vechile expense details updated successfully.");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Error updating vehicle details: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Failed to update vechile expense details.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
