package com.taxiapp.call_taxi_service.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.taxiapp.call_taxi_service.model.VehicleExpense;

@Repository
public interface VehicleExpenseRepository extends JpaRepository<VehicleExpense, Long> {

    // Find expenses by vehicle number
    List<VehicleExpense> findByVehicleNo(String vehicleNo);

    // Method to get all records between two dates
    List<VehicleExpense> findByDateBetween(Date startDate, Date endDate);

    // Method to get records for a specific date
    List<VehicleExpense> findByDate(Date tripDate);
}
