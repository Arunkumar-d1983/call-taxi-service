package com.taxiapp.call_taxi_service.service;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.taxiapp.call_taxi_service.model.DailyCallTaxi;
import com.taxiapp.call_taxi_service.model.VechileStatus;
import com.taxiapp.call_taxi_service.model.VehicleExpense;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportPDFService {

    public File generateVehicleExpenseReportPdf(List<VehicleExpense> expenses) throws FileNotFoundException {
        String filePath = "Vehicle_Expense.pdf";
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        // Add Title
        document.add(new Paragraph("Vehicle Expense Report").setBold().setFontSize(14));
        // Create Table
        Table table = new Table(new float[] { 2, 5, 5, 5, 5, 5 });
        table.addHeaderCell(new Cell().add(new Paragraph("Seq No.").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Vehicle No").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Fuel Type").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Fuel Provider").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Amount").setBold()));
        Map<String, List<VehicleExpense>> expensesByFuelType = new LinkedHashMap<>();
        for (VehicleExpense expense : expenses) {
            expensesByFuelType
                    .computeIfAbsent(expense.getFuelType(), k -> new ArrayList<>())
                    .add(expense);
        }
        double grandTotal = 0;

        for (Map.Entry<String, List<VehicleExpense>> entry : expensesByFuelType.entrySet()) {
            String fuelType = entry.getKey();
            List<VehicleExpense> fuelExpenses = entry.getValue();
            double fuelTotal = 0; // Total for this fuel type
            int seqNo = 1; // Overall sequence number
            for (VehicleExpense expense : fuelExpenses) {
                double amount = expense.getAmount();
                table.addCell(String.valueOf(seqNo)); // Global Seq No
                table.addCell(expense.getDate().toString());
                table.addCell(expense.getVehicleNo());
                table.addCell(fuelType);
                table.addCell(expense.getFuelProvider());
                table.addCell(String.valueOf(amount));
                fuelTotal += expense.getAmount(); // Sum for this fuel type
                seqNo++;
            }
            table.addCell(new Cell(1, 5).add(new Paragraph(entry.getKey() + " Total : ").setBold()));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(fuelTotal)).setBold()));
            grandTotal += fuelTotal; // Add to grand total
        }
        table.addCell(new Cell(1, 5).add(new Paragraph("Grand Total : ").setBold()));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(grandTotal)).setBold()));
        document.add(table);
        document.close();
        return new File(filePath);
    }

    public File generateDailyTaxiDetailReportPdf(List<DailyCallTaxi> dailyTaxis) throws FileNotFoundException {
        String filePath = "Daily_Taxi_Details.pdf";
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        // Add Title
        document.add(new Paragraph("Daily Taxi Details Report").setBold().setFontSize(14));
        // Create Table
        Table table = new Table(new float[] { 2, 5, 5, 5, 5, 5 });
        table.addHeaderCell(new Cell().add(new Paragraph("Seq No.").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Vehicle No").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Driver").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Trip Deatils").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Amount").setBold()));
        int globalSeqNo = 1; // Overall sequence number
        double totalAmount = 0; // Total for this fuel type
        for (DailyCallTaxi dailyTaxi : dailyTaxis) {
            table.addCell(String.valueOf(globalSeqNo));
            table.addCell(dailyTaxi.getDate().toString());
            table.addCell(dailyTaxi.getVehicleNo());
            table.addCell(dailyTaxi.getDriver());
            table.addCell(dailyTaxi.getTripDetails());
            table.addCell(String.valueOf("600.0"));
            globalSeqNo++;
        }
        table.addCell(new Cell(1, 5).add(new Paragraph(" Total : ").setBold()));
        totalAmount = 600 * dailyTaxis.size(); // Sum for this Total Amount
        table.addCell(new Cell().add(new Paragraph(String.valueOf(totalAmount)).setBold()));
        document.add(table);
        document.close();
        return new File(filePath);
    }

    public File generateVechileStatusReportPdf(List<VechileStatus> vechileStatus) throws FileNotFoundException {
        String filePath = "Vechile_Status.pdf";
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        // Document document = new Document(pdf);
        Document document = new Document(pdf, PageSize.LEGAL);
        // Add Title
        document.add(new Paragraph("Vechile Status Report").setBold().setFontSize(14));
        // Create Table
        Table table = new Table(new float[] { 2, 5, 5, 5, 5, 5, 5 });
        table.addHeaderCell(new Cell().add(new Paragraph("Seq No.").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Start DateTime").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Start Address").setBold()));
        // table.addHeaderCell(new Cell().add(new Paragraph("Start
        // Coordinates").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("End DateTime").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("End Address").setBold()));
        // table.addHeaderCell(new Cell().add(new Paragraph("End
        // Coordinates").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Total KM").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Duration").setBold()));
        int globalSeqNo = 1; // Overall sequence number
        for (VechileStatus vechileStat : vechileStatus) {
            table.addCell(String.valueOf(globalSeqNo));
            table.addCell(vechileStat.getStartDateTime().toString());
            table.addCell(vechileStat.getStartAddress());
            // table.addCell(vechileStat.getStartCoordinates());
            table.addCell(vechileStat.getEndDateTime().toString());
            table.addCell(vechileStat.getEndAddress());
            // table.addCell(vechileStat.getEndCoordinates());
            table.addCell(String.valueOf(vechileStat.getTotalkm()));
            table.addCell(vechileStat.getDuration());
            globalSeqNo++;
        }
        document.add(table);
        document.close();
        return new File(filePath);
    }

}
