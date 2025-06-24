package com.taxiapp.call_taxi_service.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taxiapp.call_taxi_service.model.Taxi;

@Repository
public interface TaxiRepository extends JpaRepository<Taxi, Long> {

    Optional<Taxi> findByVehicleNo(String vehicleNo);

}
