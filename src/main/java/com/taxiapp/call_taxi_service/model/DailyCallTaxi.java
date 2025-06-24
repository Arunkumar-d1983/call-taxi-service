package com.taxiapp.call_taxi_service.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "daily_taxi_calls")
public class DailyCallTaxi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String vehicleNo;

    @Column(name = "trip_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private String driver;

    @Column(nullable = false, length = 500)
    private String tripDetails;

    // Constructors
    public DailyCallTaxi() {
    }

    public DailyCallTaxi(String vehicleNo, Date date, String driver, String tripDetails) {
        this.vehicleNo = vehicleNo;
        this.date = date;
        this.driver = driver;
        this.tripDetails = tripDetails;
    }

    // Getters & Setters
    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(String tripDetails) {
        this.tripDetails = tripDetails;
    }

    @Override
    public String toString() {
        return "DailyCallTaxi [seq=" + seq + ", vehicleNo=" + vehicleNo + ", date=" + date + ", driver=" + driver
                + ", tripDetails=" + tripDetails + "]";
    }
}
