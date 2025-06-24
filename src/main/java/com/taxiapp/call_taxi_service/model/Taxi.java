package com.taxiapp.call_taxi_service.model;

import javax.persistence.*;

@Entity
@Table(name = "vehicles")
public class Taxi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqNo;

    @Column(nullable = false, unique = true)
    private String vehicleNo;

    // Constructors
    public Taxi() {
    }

    public Taxi(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    // Getters & Setters
    public Long getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Long seqNo) {
        this.seqNo = seqNo;
    }

    @Override
    public String toString() {
        return "Taxi [seqNo=" + seqNo + ", vehicleNo=" + vehicleNo + "]";
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
