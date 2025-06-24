package com.taxiapp.call_taxi_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taxiapp.call_taxi_service.model.DailyCallTaxi;
import com.taxiapp.call_taxi_service.model.VechileStatus;
import com.taxiapp.call_taxi_service.repository.DailyCallTaxiRepository;
import com.taxiapp.call_taxi_service.repository.VechileStatusRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DailyCallTaxiService {
    @Autowired
    private DailyCallTaxiRepository dailyCallTaxiRepository;

    @Autowired
    private VechileStatusRepository vechileStatusRepository;

    public DailyCallTaxi addDailyTaxiDetails(DailyCallTaxi details) {
        return dailyCallTaxiRepository.save(details);
    }

    public List<DailyCallTaxi> getAllDailyTaxiDetails() {
        return dailyCallTaxiRepository.findAll();
    }

    public DailyCallTaxi updateDailyTaxiDetails(Long seq, DailyCallTaxi updatedDetails) {
        return dailyCallTaxiRepository.findById(seq)
                .map(existing -> {
                    existing.setVehicleNo(updatedDetails.getVehicleNo());
                    existing.setDate(updatedDetails.getDate());
                    existing.setDriver(updatedDetails.getDriver());
                    existing.setTripDetails(updatedDetails.getTripDetails());
                    return dailyCallTaxiRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Taxi record not found"));
    }

    public List<DailyCallTaxi> getByDateRangeOrSingleDate(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            // Fetch by date range
            return dailyCallTaxiRepository.findByDateBetween(startDate, endDate);
        } else if (startDate != null) {
            // Fetch by single date
            return dailyCallTaxiRepository.findByDate(startDate);
        } else {
            // Default case: Fetch all
            return dailyCallTaxiRepository.findAll();
        }
    }

    public List<VechileStatus> getByDateRangeOrSingleDateWithVechileStatus(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            Date startOfDay = atStartOfDay(startDate);
            Date endOfDay = atEndOfDay(endDate);
            return vechileStatusRepository.findByStartDateTimeBetween(startOfDay, endOfDay);
        } else if (startDate != null) {
            Date startOfDay = atStartOfDay(startDate);
            Date endOfDay = atEndOfDay(startDate);
            return vechileStatusRepository.findByStartDateTimeBetween(startOfDay, endOfDay);
        } else {
            return vechileStatusRepository.findAll();
        }
    }

    private Date atStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date atEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

}
