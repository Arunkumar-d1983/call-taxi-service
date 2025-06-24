package com.taxiapp.call_taxi_service.service;

import com.taxiapp.call_taxi_service.model.DailyCallTaxi;
import com.taxiapp.call_taxi_service.model.VehicleExpense;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    public File generateVehicleExpenseReportExcel(List<VehicleExpense> expenses) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Vehicle Expenses");

        // Create a bold font style for headers and totals
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);

        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        // Create Header Row
        Row header = sheet.createRow(0);
        String[] columns = { "Date", "Vehicle No", "Fuel Type", "Fuel Provider", "Amount" };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(boldStyle); // Apply bold style to header
        }
        /*
         * // Populate Data
         * int rowNum = 1;
         * double totalAmount = 0; // Variable to store total amount
         * for (VehicleExpense expense : expenses) {
         * Row row = sheet.createRow(rowNum++);
         * row.createCell(0).setCellValue(expense.getVehicleNo());
         * row.createCell(1).setCellValue(expense.getFuelType());
         * row.createCell(2).setCellValue(expense.getDate().toString());
         * row.createCell(3).setCellValue(expense.getAmount());
         * totalAmount += expense.getAmount(); // Accumulate the total amount
         * }
         * 
         * // Add a Total Row
         * Row totalRow = sheet.createRow(rowNum);
         * totalRow.createCell(2).setCellValue("Total:");
         * totalRow.createCell(3).setCellValue(totalAmount); // Insert total amount in
         * the Amount column
         */
        // Group expenses by Fuel Type
        Map<String, List<VehicleExpense>> expensesByFuelType = expenses.stream()
                .collect(Collectors.groupingBy(
                        VehicleExpense::getFuelType,
                        LinkedHashMap::new,
                        Collectors.toList()));

        int rowNum = 1;
        double grandTotal = 0; // Overall total amount
        // Iterate over each fuel type and write data
        for (Map.Entry<String, List<VehicleExpense>> entry : expensesByFuelType.entrySet()) {
            String fuelType = entry.getKey();
            List<VehicleExpense> fuelExpenses = entry.getValue();
            double fuelTotal = 0; // Total for this fuel type
            for (VehicleExpense expense : fuelExpenses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(expense.getDate().toString());
                row.createCell(1).setCellValue(expense.getVehicleNo());
                row.createCell(2).setCellValue(expense.getFuelType());
                row.createCell(3).setCellValue(expense.getFuelProvider());
                row.createCell(4).setCellValue(expense.getAmount());
                fuelTotal += expense.getAmount(); // Sum for this fuel type
            }
            // Add subtotal row for this Fuel Type
            Row subtotalRow = sheet.createRow(rowNum++);
            Cell subtotalLabelCell = subtotalRow.createCell(1);
            subtotalLabelCell.setCellValue(fuelType + " Total : ");
            subtotalLabelCell.setCellStyle(boldStyle);
            Cell subtotalAmountCell = subtotalRow.createCell(4);
            subtotalAmountCell.setCellValue(fuelTotal);
            subtotalAmountCell.setCellStyle(boldStyle);

            grandTotal += fuelTotal; // Add to grand total
        }

        // Add final grand total row (Bold)
        Row grandTotalRow = sheet.createRow(rowNum++);
        Cell grandTotalLabelCell = grandTotalRow.createCell(1);
        grandTotalLabelCell.setCellValue("Grand Total : ");
        grandTotalLabelCell.setCellStyle(boldStyle);

        Cell grandTotalAmountCell = grandTotalRow.createCell(4);
        grandTotalAmountCell.setCellValue(grandTotal);
        grandTotalAmountCell.setCellStyle(boldStyle);

        // Save as Excel file
        File file = new File("Vehicle_Expense_Report.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
        workbook.close();
        return file;
    }

    public File generateDailyTaxiDetailReportExcel(List<DailyCallTaxi> dailyTaxis) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Daily Taxi Details");

        // Create a bold font style for headers and totals
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);

        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        // Create Header Row
        Row header = sheet.createRow(0);
        String[] columns = { "Date", "Vehicle No", "Driver", "Trip Deatils" };
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(boldStyle); // Apply bold style to header
        }

        // Populate Data
        int rowNum = 1;
        for (DailyCallTaxi dailyTaxi : dailyTaxis) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dailyTaxi.getDate().toString());
            row.createCell(1).setCellValue(dailyTaxi.getVehicleNo());
            row.createCell(2).setCellValue(dailyTaxi.getDriver());
            row.createCell(3).setCellValue(dailyTaxi.getTripDetails());
        }

        // Save as Excel file
        File file = new File("Daily_Taxi_Deatils_Report.xlsx");
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
        workbook.close();
        return file;
    }

}
