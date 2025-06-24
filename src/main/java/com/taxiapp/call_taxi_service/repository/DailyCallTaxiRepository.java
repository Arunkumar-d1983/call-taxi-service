package com.taxiapp.call_taxi_service.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taxiapp.call_taxi_service.model.DailyCallTaxi;

@Repository
public interface DailyCallTaxiRepository extends JpaRepository<DailyCallTaxi, Long> {

    // Method to get all records between two dates
    List<DailyCallTaxi> findByDateBetween(Date startDate, Date endDate);

    // Method to get records for a specific date
    List<DailyCallTaxi> findByDate(Date tripDate);
}
