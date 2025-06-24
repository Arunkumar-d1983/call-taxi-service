package com.taxiapp.call_taxi_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxiapp.call_taxi_service.model.DailyCallTaxi;
import com.taxiapp.call_taxi_service.model.EmployeeTripDetails;
import com.taxiapp.call_taxi_service.model.TripDetails;
import com.taxiapp.call_taxi_service.model.VechileStatus;
import com.taxiapp.call_taxi_service.model.VehicleExpense;

import com.taxiapp.call_taxi_service.service.DailyCallTaxiService;
import com.taxiapp.call_taxi_service.service.EmailService;
import com.taxiapp.call_taxi_service.service.EmployeeTripDetailsService;
import com.taxiapp.call_taxi_service.service.PdfService;
import com.taxiapp.call_taxi_service.service.ReportPDFService;
import com.taxiapp.call_taxi_service.service.ReportService;
import com.taxiapp.call_taxi_service.service.TripDetailsService;
import com.taxiapp.call_taxi_service.service.VehicleExpenseService;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportPDFService reportPDFService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TripDetailsService tripDetailsService;

    @Autowired
    private EmployeeTripDetailsService employeeTripDetailsService;

    @Autowired
    private VehicleExpenseService vehicleExpenseService;

    @Autowired
    private DailyCallTaxiService dailyCallTaxiService;

    @GetMapping("/sendVehicleExpenseReport")
    public ResponseEntity<?> sendReport(@RequestBody String jsonInput) {
        logger.info("Invoke sendReport >>>");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonInput);

            String startDateStr = jsonNode.has("startDate") ? jsonNode.get("startDate").asText() : null;
            String endDateStr = jsonNode.has("endDate") ? jsonNode.get("endDate").asText() : null;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date startDate = startDateStr != null ? inputFormat.parse(startDateStr) : null;
            Date endDate = endDateStr != null ? inputFormat.parse(endDateStr) : null;
            // List<VehicleExpense> expenses = expenseRepository.findAll();
            List<VehicleExpense> expenses = vehicleExpenseService.getByDateRangeOrSingleDate(startDate, endDate);
            logger.info("sendReport expenses >>>" + expenses);
            File report = reportService.generateVehicleExpenseReportExcel(expenses);
            logger.info("report >>>" + report);
            // List<DailyCallTaxi> dailyTaxiDetails = dailyCallTaxiRepository.findAll();
            List<DailyCallTaxi> dailyTaxiDetails = dailyCallTaxiService.getByDateRangeOrSingleDate(startDate,
                    endDate);
            logger.info("sendReport dailyTaxiDetails >>>" + dailyTaxiDetails);
            File reportDaily = reportService.generateDailyTaxiDetailReportExcel(dailyTaxiDetails);
            logger.info("reportDaily >>>" + reportDaily);

            File expensePdf = reportPDFService.generateVehicleExpenseReportPdf(expenses);
            logger.info("expensePdf >>>" + expensePdf);
            File dailyTaxiPdf = reportPDFService.generateDailyTaxiDetailReportPdf(dailyTaxiDetails);
            logger.info("dailyTaxiPdf >>>" + dailyTaxiPdf);

            List<VechileStatus> vechileStatusDetails = dailyCallTaxiService.getByDateRangeOrSingleDateWithVechileStatus(
                    startDate,
                    endDate);
            logger.info("sendReport vechileStatusDetails >>>" + vechileStatusDetails);
            File vechileStatusPdf = reportPDFService.generateVechileStatusReportPdf(vechileStatusDetails);
            logger.info("vechileStatusPdf >>>" + vechileStatusPdf);
            // emailService.sendEmailWithAttachmentExcel(
            // new String[] { "arunkumarmca@gmail.com", "arunkumarmca.d@gmail.com",
            // "vickymedha@gmail.com",
            // "sum3udhaya@gmail.com" },
            // "Daily Taxi Deatils & Vehicle Expense Report",
            // "Please find the attached Vehicle Expense Report.",
            // new File[] { report, reportDaily, expensePdf, dailyTaxiPdf });

            emailService.sendEmailWithAttachmentExcel(
                    new String[] { "arunkumarmca@gmail.com", "arunkumarmca.d@gmail.com" },
                    "Daily Taxi Deatils & Vehicle Expense Report",
                    "Please find the attached Vehicle Expense Report.",
                    new File[] { report, reportDaily, expensePdf, dailyTaxiPdf, vechileStatusPdf });

            return new ResponseEntity<>("Report Sent Successfully", HttpStatus.OK);
        } catch (IOException | MessagingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/generate")
    public ResponseEntity<?> generatePdf(@RequestBody String jsonInput) throws ParseException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonInput);

            String startDateStr = jsonNode.has("startDate") ? jsonNode.get("startDate").asText() : null;
            String endDateStr = jsonNode.has("endDate") ? jsonNode.get("endDate").asText() : null;
            String employeeName = jsonNode.has("employeeName") ? jsonNode.get("employeeName").asText() : null;

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date startDate = startDateStr != null ? inputFormat.parse(startDateStr) : null;
            Date endDate = endDateStr != null ? inputFormat.parse(endDateStr) : null;

            /** Day Wise Summary Start **/
            List<TripDetails> tripDetails = tripDetailsService.getTripsByDateRangeOrSingleDate(startDate, endDate);
            logger.info("sendReport tripDetails >>>" + tripDetails);
            // if (tripDetails.isEmpty()) {
            // return new ResponseEntity<>("No records found.", HttpStatus.NOT_FOUND);
            // }
            File pdfDayWiseSummary = pdfService.generateDayWiseSummary(tripDetails,
                    startDateStr != null ? outputFormat.format(startDate) : null,
                    endDateStr != null ? outputFormat.format(endDate) : null);
            logger.info("pdfDayWiseSummary >>>" + pdfDayWiseSummary);
            /** Day Wise Summary End **/

            /** Employee Summary Start **/
            List<EmployeeTripDetails> employeeTripSummaryDetails = employeeTripDetailsService
                    .getTripsByDateRangeOrSingleDate(
                            startDate,
                            endDate, employeeName);
            logger.info("sendReport employeeTripSummaryDetails >>>" + employeeTripSummaryDetails);
            // if (employeeTripSummaryDetails.isEmpty()) {
            // return new ResponseEntity<>("No records found.", HttpStatus.NOT_FOUND);
            // }
            File pdfEmpTripSummary = pdfService.generateEmployeeSummary(employeeTripSummaryDetails,
                    startDateStr != null ? outputFormat.format(startDate) : null,
                    endDateStr != null ? outputFormat.format(endDate) : null, employeeName);
            logger.info("pdfEmpTripSummary >>>" + pdfEmpTripSummary);
            /** Employee Summary End **/

            /** Day Wise Detailed Start **/
            // List<EmployeeTripDetails> employeeTripDayWiseDetails =
            // employeeTripDetailsRepository.findByTripDate(
            // startDate);
            // logger.info("sendReport employeeTripDayWiseDetails >>>" +
            // employeeTripDayWiseDetails);
            File pdfEmpTripDetailed = pdfService.generateDayWiseDetailed(employeeTripSummaryDetails,
                    startDateStr != null ? outputFormat.format(startDate) : null,
                    endDateStr != null ? outputFormat.format(endDate) : null);
            logger.info("pdfEmpTripDetailed >>>" + pdfEmpTripDetailed);
            /** Day Wise Detailed End **/

            // File ss = pdfService.generateMonthlyBudgetReport();
            emailService.sendEmailWithAttachmentExcel(
                    new String[] { "arunkumarmca@gmail.com", "arunkumarmca.d@gmail.com" },
                    "Monthly Report",
                    "Please find the attached Monthly Report.",
                    new File[] { pdfDayWiseSummary, pdfEmpTripSummary, pdfEmpTripDetailed });
            // emailService.sendEmailWithAttachmentExcel(
            // new String[] { "arunkumarmca@gmail.com", "arunkumarmca.d@gmail.com" },
            // "Monthly Report",
            // "Please find the attached Vehicle Expense Report.",
            // new File[] { pdfEmpTripGenerator });

            return new ResponseEntity<>("Report Sent Successfully", HttpStatus.OK);
        } catch (IOException | MessagingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
