package com.taxiapp.call_taxi_service.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taxiapp.call_taxi_service.model.TripDetails;

@Repository
public interface TripDetailsRepository extends JpaRepository<TripDetails, Long> {
    // List<TripDetails> findByTripDateBetween(Date startDate, Date endDate);
    // Method to get all records between two dates
    List<TripDetails> findByTripDateBetween(Date startDate, Date endDate);

    // Method to get records for a specific date
    List<TripDetails> findByTripDate(Date tripDate);
}
