package com.taxiapp.call_taxi_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taxiapp.call_taxi_service.model.Taxi;
import com.taxiapp.call_taxi_service.repository.TaxiRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaxiService {
    @Autowired
    private TaxiRepository taxiRepository;

    public Taxi addVehicle(Taxi taxi) {
        Optional<Taxi> existingTaxi = taxiRepository.findByVehicleNo(taxi.getVehicleNo());
        if (existingTaxi.isPresent()) {
            throw new IllegalArgumentException("Vehicle with number " + taxi.getVehicleNo() + " already exists.");
        }
        return taxiRepository.save(taxi);
    }

    public List<Taxi> getAllVehicles() {
        return taxiRepository.findAll();
    }

}
