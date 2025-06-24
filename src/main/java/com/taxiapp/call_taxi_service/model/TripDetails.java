package com.taxiapp.call_taxi_service.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tripdetails")
public class TripDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tripDate;

    @Column(name = "trip_day", nullable = false, length = 20)
    private String tripDay;

    @Column(name = "trip_pickups", nullable = false)
    private int tripPickups;

    @Column(name = "trip_drops", nullable = false)
    private int tripDrops;

    // Constructors
    public TripDetails() {
    }

    public TripDetails(Date tripDate, String tripDay, int tripPickups, int tripDrops) {
        this.tripDate = tripDate;
        this.tripDay = tripDay;
        this.tripPickups = tripPickups;
        this.tripDrops = tripDrops;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripDay() {
        return tripDay;
    }

    public void setTripDay(String tripDay) {
        this.tripDay = tripDay;
    }

    public int getTripPickups() {
        return tripPickups;
    }

    public void setTripPickups(int tripPickups) {
        this.tripPickups = tripPickups;
    }

    public int getTripDrops() {
        return tripDrops;
    }

    public void setTripDrops(int tripDrops) {
        this.tripDrops = tripDrops;
    }

    @Override
    public String toString() {
        return "TripDetails [id=" + id + ", tripDate=" + tripDate + ", tripDay=" + tripDay + ", tripPickups="
                + tripPickups + ", tripDrops=" + tripDrops + "]";
    }
}
