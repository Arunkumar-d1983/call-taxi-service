package com.taxiapp.call_taxi_service.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taxiapp.call_taxi_service.model.VechileStatus;

@Repository
public interface VechileStatusRepository extends JpaRepository<VechileStatus, Long> {

    // Method to get all records between two dates
    List<VechileStatus> findByStartDateTimeBetween(Date startDate, Date endDate);

    // Method to get records for a specific date
    List<VechileStatus> findByStartDateTime(Date tripDate);
}
