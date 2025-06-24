package com.taxiapp.call_taxi_service.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "vehicle_expenses")
public class VehicleExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_no", nullable = false)
    private String vehicleNo;

    @Column(name = "fuel_type", nullable = false)
    private String fuelType; // Petrol, Diesel, Gas

    @Column(name = "expense_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "fuel_provider", nullable = false)
    private String fuelProvider; // Petrol, Diesel, Gas

    // Constructors
    public VehicleExpense() {
    }

    public VehicleExpense(String vehicleNo, String fuelType, Date date, Double amount, String fuelProvider) {
        this.vehicleNo = vehicleNo;
        this.fuelType = fuelType;
        this.date = date;
        this.amount = amount;
        this.fuelProvider = fuelProvider;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFuelProvider() {
        return fuelProvider;
    }

    public void setFuelProvider(String fuelProvider) {
        this.fuelProvider = fuelProvider;
    }

    @Override
    public String toString() {
        return "VehicleExpense [id=" + id + ", vehicleNo=" + vehicleNo + ", fuelType=" + fuelType + ", date=" + date
                + ", amount=" + amount + ", fuelProvider=" + fuelProvider + "]";
    }

}
