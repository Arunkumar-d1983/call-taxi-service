package com.taxiapp.call_taxi_service.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taxiapp.call_taxi_service.model.VehicleExpense;
import com.taxiapp.call_taxi_service.repository.VehicleExpenseRepository;

@Service
public class VehicleExpenseService {

    @Autowired
    private VehicleExpenseRepository vehicleExpenseRepository;

    // Add a new vehicle expense
    public VehicleExpense addExpense(VehicleExpense expense) {
        return vehicleExpenseRepository.save(expense);
    }

    // Get all expenses
    public List<VehicleExpense> getAllExpenses() {
        return vehicleExpenseRepository.findAll();
    }

    // Get expenses by vehicle number
    public List<VehicleExpense> getExpensesByVehicleNo(String vehicleNo) {
        return vehicleExpenseRepository.findByVehicleNo(vehicleNo);
    }

    // Update an expense
    public VehicleExpense updateExpense(Long id, VehicleExpense updatedExpense) {
        Optional<VehicleExpense> existingExpense = vehicleExpenseRepository.findById(id);

        if (existingExpense.isPresent()) {
            VehicleExpense expense = existingExpense.get();
            expense.setVehicleNo(updatedExpense.getVehicleNo());
            expense.setFuelType(updatedExpense.getFuelType());
            expense.setDate(updatedExpense.getDate());
            expense.setAmount(updatedExpense.getAmount());
            return vehicleExpenseRepository.save(expense);
        } else {
            throw new IllegalArgumentException("Expense with ID " + id + " not found.");
        }
    }

    // Delete an expense
    public void deleteExpense(Long id) {
        if (vehicleExpenseRepository.existsById(id)) {
            vehicleExpenseRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Expense with ID " + id + " not found.");
        }
    }

    public List<VehicleExpense> getByDateRangeOrSingleDate(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            // Fetch by date range
            return vehicleExpenseRepository.findByDateBetween(startDate, endDate);
        } else if (startDate != null) {
            // Fetch by single date
            return vehicleExpenseRepository.findByDate(startDate);
        } else {
            // Default case: Fetch all
            return vehicleExpenseRepository.findAll();
        }
    }
}
