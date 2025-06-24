package com.taxiapp.call_taxi_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taxiapp.call_taxi_service.model.EmployeeTripDetails;
import com.taxiapp.call_taxi_service.model.TripDetails;
import com.taxiapp.call_taxi_service.repository.EmployeeTripDetailsRepository;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeTripDetailsService {

    @Autowired
    private EmployeeTripDetailsRepository repository;

    public List<EmployeeTripDetails> getAllTrips() {
        return repository.findAll();
    }

    @SuppressWarnings("unused")
    public List<EmployeeTripDetails> getTripsByDateRangeOrSingleDate(Date startDate, Date endDate,
            String employeeName) {
        if (startDate != null && endDate != null) {
            // Fetch by date range
            return repository.findByTripDateBetween(startDate, endDate);
        } else if (startDate != null) {
            // Fetch by single date
            return repository.findByTripDate(startDate);
        } else if (startDate != null && employeeName != null) {
            // Fetch by specific date and employee name
            return repository.findByTripDateAndEmployeeName(startDate, employeeName);
        } else {
            // Default case: Fetch all
            return repository.findAll();
        }
    }

    public EmployeeTripDetails saveTrip(EmployeeTripDetails tripDetails) {
        return repository.save(tripDetails);
    }
}
