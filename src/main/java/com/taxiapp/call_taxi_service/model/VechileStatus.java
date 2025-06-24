package com.taxiapp.call_taxi_service.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trip_status")
public class VechileStatus {

    // Constructors
    public VechileStatus() {
    }

    public VechileStatus(Date startDateTime, String startAddress, String startCoordinates, Date endDateTime,
            String endAddress, String endCoordinates, double totalkm, String duration) {
        this.startDateTime = startDateTime;
        this.startAddress = startAddress;
        this.startCoordinates = startCoordinates;
        this.endDateTime = endDateTime;
        this.endAddress = endAddress;
        this.endCoordinates = endCoordinates;
        this.totalkm = totalkm;
        this.duration = duration;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;

    @Column(nullable = false)
    private String startAddress;

    @Column(nullable = false, length = 500)
    private String startCoordinates;

    @Column(name = "end_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;

    @Column(nullable = false)
    private String endAddress;

    @Column(nullable = false, length = 500)
    private String endCoordinates;

    @Column(name = "total_km", nullable = false)
    private double totalkm;

    @Column(nullable = false, length = 500)
    private String duration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getStartCoordinates() {
        return startCoordinates;
    }

    public void setStartCoordinates(String startCoordinates) {
        this.startCoordinates = startCoordinates;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getEndCoordinates() {
        return endCoordinates;
    }

    public void setEndCoordinates(String endCoordinates) {
        this.endCoordinates = endCoordinates;
    }

    public double getTotalkm() {
        return totalkm;
    }

    public void setTotalkm(double totalkm) {
        this.totalkm = totalkm;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "VechileStatus [id=" + id + ", startDateTime=" + startDateTime + ", startAddress=" + startAddress
                + ", startCoordinates=" + startCoordinates + ", endDateTime=" + endDateTime + ", endAddress="
                + endAddress + ", endCoordinates=" + endCoordinates + ", totalkm=" + totalkm + ", duration=" + duration
                + "]";
    }

}
