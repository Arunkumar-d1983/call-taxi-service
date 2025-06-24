package com.taxiapp.call_taxi_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.taxiapp.call_taxi_service.model.EmployeeTripDetails;

import java.util.Date;
import java.util.List;

@Repository
public interface EmployeeTripDetailsRepository extends JpaRepository<EmployeeTripDetails, Long> {

    // Method to get all records between two dates
    List<EmployeeTripDetails> findByTripDateBetween(Date startDate, Date endDate);

    // Method to get records for a specific date
    List<EmployeeTripDetails> findByTripDate(Date tripDate);

    List<EmployeeTripDetails> findByTripDateAndEmployeeName(Date tripDate, String employeeName);
}
