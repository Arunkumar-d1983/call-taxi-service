package com.taxiapp.call_taxi_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taxiapp.call_taxi_service.model.TripDetails;
import com.taxiapp.call_taxi_service.repository.TripDetailsRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TripDetailsService {

    @Autowired
    private TripDetailsRepository tripDetailsRepository;

    // Get all trips
    public List<TripDetails> getAllTrips() {
        return tripDetailsRepository.findAll();
    }

    // Get trip by ID
    public Optional<TripDetails> getTripById(Long id) {
        return tripDetailsRepository.findById(id);
    }

    // Save a trip
    public TripDetails saveTrip(TripDetails tripDetails) {
        return tripDetailsRepository.save(tripDetails);
    }

    // Delete a trip
    public void deleteTrip(Long id) {
        tripDetailsRepository.deleteById(id);
    }

    public List<TripDetails> getTripsByDateRangeOrSingleDate(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            // Fetch by date range
            return tripDetailsRepository.findByTripDateBetween(startDate, endDate);
        } else if (startDate != null) {
            // Fetch by single date
            return tripDetailsRepository.findByTripDate(startDate);
        } else {
            // Default case: Fetch all
            return tripDetailsRepository.findAll();
        }
    }
}
