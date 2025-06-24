package com.taxiapp.call_taxi_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.taxiapp.call_taxi_service.model.TripDetails;
import com.taxiapp.call_taxi_service.service.TripDetailsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripDetailsController {

    @Autowired
    private TripDetailsService tripDetailsService;

    // Get all trips
    @GetMapping
    public List<TripDetails> getAllTrips() {
        return tripDetailsService.getAllTrips();
    }

    // Get trip by ID
    @GetMapping("/{id}")
    public Optional<TripDetails> getTripById(@PathVariable Long id) {
        return tripDetailsService.getTripById(id);
    }

    // Add a new trip
    @PostMapping
    public TripDetails createTrip(@RequestBody TripDetails tripDetails) {
        return tripDetailsService.saveTrip(tripDetails);
    }

    // Delete a trip by ID
    @DeleteMapping("/{id}")
    public String deleteTrip(@PathVariable Long id) {
        tripDetailsService.deleteTrip(id);
        return "Trip with ID " + id + " deleted successfully.";
    }
}
