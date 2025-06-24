package com.taxiapp.call_taxi_service.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "employee_trip_details")
public class EmployeeTripDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date tripDate;

    @Column(name = "route_name", nullable = false)
    private String routeName;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "trip_type", nullable = false)
    private String tripType;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "drop_location", nullable = false)
    private String dropLocation;

    @Column(name = "pickup_time", nullable = false)
    private String pickupTime;

    @Column(name = "cab_no", nullable = false)
    private String cabNo;

    @Column(name = "driver_name", nullable = false)
    private String driverName;

    @Column(name = "amount", nullable = false)
    private Double amount;

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

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getCabNo() {
        return cabNo;
    }

    public void setCabNo(String cabNo) {
        this.cabNo = cabNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "EmployeeTripDetails [id=" + id + ", tripDate=" + tripDate + ", routeName=" + routeName
                + ", employeeName=" + employeeName + ", tripType=" + tripType + ", pickupLocation=" + pickupLocation
                + ", dropLocation=" + dropLocation + ", pickupTime=" + pickupTime + ", cabNo=" + cabNo + ", driverName="
                + driverName + ", amount=" + amount + "]";
    }

}
