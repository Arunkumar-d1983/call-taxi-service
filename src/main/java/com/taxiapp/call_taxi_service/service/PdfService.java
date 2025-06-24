package com.taxiapp.call_taxi_service.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.taxiapp.call_taxi_service.model.EmployeeTripDetails;
import com.taxiapp.call_taxi_service.model.TripDetails;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class PdfService {

        public File generateDayWiseSummary(List<TripDetails> tripDetails, String formattedStartDate,
                        String formattedEndDate)
                        throws FileNotFoundException, MalformedURLException {
                String filePath = "Day_Wise_Summary_Report.pdf";
                String logoPath = new File("Image/citylogo.png").getAbsolutePath();
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf, PageSize.A3);
                document.setMargins(20, 20, 20, 20);

                // Header (Logo Left, Address Right)
                Table headerTable = new Table(UnitValue.createPercentArray(new float[] { 3, 4 }))
                                .useAllAvailableWidth();
                ImageData imageData = ImageDataFactory.create(logoPath);
                Image logo = new Image(imageData);
                logo.scaleToFit(100, 50); // Resize logo (Width x Height)
                Cell logoCell = new Cell().add(logo).setBorder(Border.NO_BORDER);
                headerTable.addCell(logoCell);
                Paragraph address = new Paragraph()
                                .add(new Paragraph("CITY EXPRESS CABS").setBold().setFontSize(12)) // Bold Title
                                .add("\n") // Line break
                                .add(new Text("Address: ").setBold())
                                .add(" No.250, F Block, Kakkan Colony,\n")
                                .add("Besant Nagar, Chennai-600090\n")
                                .add("Phone: 918015161100  Email : cityexpresscalltaxi@gmail.com")
                                .setTextAlignment(TextAlignment.LEFT) // Align Right
                                .setMultipliedLeading(1.2f) // Adjust line spacing
                                .setPaddingRight(1) // Add padding to push it further right
                                .setPaddingLeft(80); // Increase spacing from the logo
                headerTable.addCell(new Cell().add(address).setBorder(Border.NO_BORDER)); // Right Side Address
                document.add(headerTable);

                DeviceRgb bgColor = new DeviceRgb(128, 128, 128);
                Paragraph title = new Paragraph("Day Wise Summary Report")
                                .setBold()
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER)
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY) // **Background color for box**
                                .setBorder(new SolidBorder(ColorConstants.BLACK, 1)) // **Border Color**
                                .setPadding(10);
                document.add(title);
                document.add(new Paragraph("\n"));
                // Customer Details
                Table customerTable = new Table(UnitValue.createPercentArray(new float[] { 5, 2 }))
                                .useAllAvailableWidth();
                customerTable.addHeaderCell(getHeaderCell("Customer Address"));
                customerTable.addHeaderCell(getHeaderCell("Date"));

                Paragraph customerAddress = new Paragraph()
                                .add(new Text("SUPEROPS TECHNOLOGIES PRIVATE LIMITED,").setBold().setFontSize(12))
                                .add("\n") // Line break
                                .add(new Text("Address: ").setBold())
                                .add("6th Floor, Hardy Tower, Ramanujan IT City,\n")
                                .add("Rajiv Gandhi Salai (OMR), Taramani,\n")
                                .add("CHENNAI-6000113\n")
                                .add(new Text("GST No : 33AAICC2625E2Z3.").setBold())
                                .setTextAlignment(TextAlignment.LEFT);
                customerTable.addHeaderCell(new Cell().add(customerAddress).setTextAlignment(TextAlignment.CENTER));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(new Date());
                customerTable.addHeaderCell(getDataCell(formattedDate).setTextAlignment(TextAlignment.CENTER));
                document.add(customerTable);

                // Date Range
                if (formattedStartDate != null && formattedEndDate != null) {
                        document.add(new Paragraph("\nDate Range : " + formattedStartDate + " to " + formattedEndDate)
                                        .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
                } else if (formattedStartDate != null) {
                        document.add(new Paragraph("\nDate : " + formattedStartDate)
                                        .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
                }
                // Table Header
                float[] columnWidths = { 2, 3, 3, 3, 3, 3 };
                Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
                table.addHeaderCell(getHeaderCell("Sl.No").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Date").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Day").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Pick Up").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Drop").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Total").setBackgroundColor(ColorConstants.LIGHT_GRAY));

                // Data Rows
                int totalPickups = 0;
                int totalDrops = 0;
                int totalRides = 0;
                int seqNo = 1;
                for (TripDetails tripDetail : tripDetails) {
                        table.addCell(getDataCell(String.valueOf(seqNo)));
                        String tripDate = dateFormat.format(tripDetail.getTripDate());
                        table.addCell(getDataCell(tripDate));
                        table.addCell(getDataCell(tripDetail.getTripDay()));
                        table.addCell(
                                        getDataCell(String.valueOf(tripDetail.getTripPickups()))
                                                        .setTextAlignment(TextAlignment.RIGHT));
                        table.addCell(getDataCell(String.valueOf(tripDetail.getTripDrops()))
                                        .setTextAlignment(TextAlignment.RIGHT));
                        int rides = tripDetail.getTripPickups() + tripDetail.getTripDrops();
                        table.addCell(getDataCell(String.valueOf(rides)).setTextAlignment(TextAlignment.RIGHT));
                        totalPickups += tripDetail.getTripPickups();
                        totalDrops += tripDetail.getTripDrops();
                        totalRides += rides;
                        seqNo++;
                }

                table.addCell(new Cell(1, 3).add(new Paragraph("Total  ").setBold())
                                .setTextAlignment(TextAlignment.CENTER));
                table.addCell(getDataCell(String.valueOf(totalPickups)).setTextAlignment(TextAlignment.RIGHT))
                                .setBold();
                table.addCell(getDataCell(String.valueOf(totalDrops)).setTextAlignment(TextAlignment.RIGHT)).setBold();
                table.addCell(getDataCell(String.valueOf(totalRides)).setTextAlignment(TextAlignment.RIGHT)).setBold();
                document.add(table);

                document.add(new Paragraph("\n"));

                Table summaryTable = new Table(3);
                summaryTable.setWidth(UnitValue.createPercentValue(100));
                String pickup = "Pick Ups \n" + String.valueOf(totalPickups);
                String drop = "Drops \n" + String.valueOf(totalDrops);
                String total = "Total \n" + String.valueOf(totalRides);
                summaryTable.addCell(new Cell().add(new Paragraph(pickup).setBold())
                                .setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                summaryTable.addCell(new Cell().add(new Paragraph(drop).setBold())
                                .setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                summaryTable.addCell(new Cell().add(new Paragraph(total).setBold())
                                .setTextAlignment(TextAlignment.CENTER).setBackgroundColor(ColorConstants.LIGHT_GRAY));

                document.add(summaryTable);

                document.close();

                return new File(filePath);
        }

        // Utility Methods
        private static Cell getTextCell(String text, TextAlignment alignment) {
                return new Cell().add(new Paragraph(text)).setTextAlignment(alignment).setBorder(null);
        }

        private static Cell getHeaderCell(String text) {
                return new Cell().add(new Paragraph(text).setBold()).setTextAlignment(TextAlignment.CENTER);
        }

        private static Cell getDataCell(String text) {
                return new Cell().add(new Paragraph(text)).setTextAlignment(TextAlignment.CENTER);
        }

        public File generateInvoicenew() throws JRException {
                try {
                        // Load the JRXML template from resources
                        InputStream reportStream = new ClassPathResource("report.jrxml").getInputStream();

                        // Compile the JRXML file
                        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

                        // Parameters to pass into the report
                        Map<String, Object> parameters = new HashMap<>();
                        parameters.put("ReportTitle", "Tax Invoice");

                        // Fill the report with data (using an empty data source for now)
                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
                                        new JREmptyDataSource());

                        // Generate the PDF file
                        File pdfFile = File.createTempFile("invoice_", ".pdf");
                        JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdfFile));

                        return pdfFile;
                } catch (Exception e) {
                        throw new JRException("Error generating PDF", e);
                }
        }

        public File generateMonthlyBudgetReport() throws IOException {
                String filePath = "Invoice.pdf";
                String logoPath = new File("Image/Logo.jfif").getAbsolutePath();

                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc, PageSize.A4);

                // Add background image
                ImageData imageData = ImageDataFactory.create(logoPath);
                Image img = new Image(imageData);
                img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                img.setFixedPosition(0, 0);
                document.add(img);

                // Overlay text for invoice details
                document.add(new Paragraph("INVOICE").setFontSize(20).setBold().setFontColor(ColorConstants.RED));
                document.add(new Paragraph("Invoice #: 52148"));
                document.add(new Paragraph("Date: 01/02/2020"));

                document.add(new Paragraph("Item Description       Price       Qty       Total"));
                document.add(new Paragraph("Lorem Ipsum Dolor      $50.00      1         $50.00"));
                document.add(new Paragraph("Pellentesque id neque  $20.00      3         $60.00"));
                document.add(new Paragraph("Interdum et malesuada  $10.00      2         $20.00"));
                document.add(new Paragraph("Vivamus volutpat       $90.00      1         $90.00"));
                document.add(new Paragraph("------------------------------------------------"));
                document.add(new Paragraph("Sub Total: $220.00"));
                document.add(new Paragraph("Total: $220.00").setBold());

                document.add(new Paragraph("Payment Info:"));
                document.add(new Paragraph("Account #: 1234 5678 9012"));
                document.add(new Paragraph("A/C Name: Lorem Ipsum"));

                document.add(new Paragraph("Thank you for your business!").setBold());

                document.close();

                return new File(filePath);

        }

        public File generateEmployeeSummary(List<EmployeeTripDetails> empTripDetails, String formattedStartDate,
                        String formattedEndDate, String inputEmployeeName)
                        throws FileNotFoundException, MalformedURLException {
                String filePath = null;
                if (inputEmployeeName == null) {
                        filePath = "Employee_Summary_Report.pdf";
                } else {
                        filePath = "Employee_Wise_Report.pdf";
                }
                String logoPath = new File("Image/citylogo.png").getAbsolutePath();
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf, PageSize.A3);
                document.setMargins(20, 20, 20, 20);

                // Header (Logo Left, Address Right)
                Table headerTable = new Table(UnitValue.createPercentArray(new float[] { 3, 4 }))
                                .useAllAvailableWidth();
                ImageData imageData = ImageDataFactory.create(logoPath);
                Image logo = new Image(imageData);
                logo.scaleToFit(100, 50); // Resize logo (Width x Height)
                Cell logoCell = new Cell().add(logo).setBorder(Border.NO_BORDER);
                headerTable.addCell(logoCell);
                Paragraph address = new Paragraph()
                                .add(new Paragraph("CITY EXPRESS CABS").setBold().setFontSize(12)) // Bold Title
                                .add("\n") // Line break
                                .add(new Text("Address: ").setBold())
                                .add(" No.250, F Block, Kakkan Colony,\n")
                                .add("Besant Nagar, Chennai-600090\n")
                                .add("Phone: 918015161100  Email : cityexpresscalltaxi@gmail.com")
                                .setTextAlignment(TextAlignment.LEFT) // Align Right
                                .setMultipliedLeading(1.2f) // Adjust line spacing
                                .setPaddingRight(1) // Add padding to push it further right
                                .setPaddingLeft(80); // Increase spacing from the logo
                headerTable.addCell(new Cell().add(address).setBorder(Border.NO_BORDER)); // Right Side Address
                document.add(headerTable);

                DeviceRgb bgColor = new DeviceRgb(128, 128, 128);
                String titleName = null;
                if (inputEmployeeName == null) {
                        titleName = "Employee Summary Report";
                } else {
                        titleName = "Employee Wise Report";
                }
                Paragraph title = new Paragraph(titleName)
                                .setBold()
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER)
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY) // **Background color for box**
                                .setBorder(new SolidBorder(ColorConstants.BLACK, 1)) // **Border Color**
                                .setPadding(10);
                document.add(title);
                document.add(new Paragraph("\n"));
                // Customer Details
                Table customerTable = new Table(UnitValue.createPercentArray(new float[] { 5, 2 }))
                                .useAllAvailableWidth();
                customerTable.addHeaderCell(getHeaderCell("Customer Address"));
                customerTable.addHeaderCell(getHeaderCell("Date"));

                Paragraph customerAddress = new Paragraph()
                                .add(new Text("SUPEROPS TECHNOLOGIES PRIVATE LIMITED,").setBold().setFontSize(12)) // Bold
                                                                                                                   // //
                                                                                                                   // Title
                                .add("\n") // Line break
                                .add(new Text("Address: ").setBold())
                                .add("6th Floor, Hardy Tower, Ramanujan IT City,\n")
                                .add("Rajiv Gandhi Salai (OMR), Taramani,\n")
                                .add("CHENNAI-6000113\n")
                                .add(new Text("GST No : 33AAICC2625E2Z3.").setBold())
                                .setTextAlignment(TextAlignment.LEFT);
                customerTable.addHeaderCell(new Cell().add(customerAddress).setTextAlignment(TextAlignment.CENTER));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(new Date());
                customerTable.addHeaderCell(getDataCell(formattedDate).setTextAlignment(TextAlignment.CENTER));
                document.add(customerTable);

                // Date Range
                if (formattedStartDate != null && formattedEndDate != null) {
                        document.add(new Paragraph("\nDate Range : " + formattedStartDate + " to " + formattedEndDate)
                                        .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
                } else if (formattedStartDate != null) {
                        document.add(new Paragraph("\nDate : " + formattedStartDate)
                                        .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
                }
                // Table Header
                float[] columnWidths = { 2, 4, 4, 3, 2, 2 };
                Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
                table.addHeaderCell(getHeaderCell("Sl.No").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Emp.Name").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Route Name").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Trip Rates").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Total Rides").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Total Amount").setBackgroundColor(ColorConstants.LIGHT_GRAY));

                Map<String, Map<String, Object>> employeeDataMap = new LinkedHashMap<>();

                for (EmployeeTripDetails emp : empTripDetails) {
                        // Filter by employee name if provided
                        if (inputEmployeeName != null && !emp.getEmployeeName().equalsIgnoreCase(inputEmployeeName)) {
                                continue;
                        }
                        // Key format: "EmployeeName|RouteName" to uniquely group by employee and route
                        String key = emp.getEmployeeName() + "|" + emp.getRouteName();

                        employeeDataMap.computeIfAbsent(key, k -> {
                                Map<String, Object> data = new HashMap<>();
                                data.put("totalAmount", 0.0); // Total amount for this employee-route combination
                                data.put("totalTrips", 0); // Total trips for this employee-route combination
                                return data;
                        });

                        Map<String, Object> data = employeeDataMap.get(key);
                        // Sum total amount per route
                        data.put("totalAmount", (double) emp.getAmount());
                        // Sum total trips per employee & route
                        data.put("totalTrips", (int) data.get("totalTrips") + 1);
                }

                int seqNo = 1;
                int totalRidesSum = 0;
                double totalAmountSum = 0;

                for (Map.Entry<String, Map<String, Object>> entry : employeeDataMap.entrySet()) {
                        String[] keyParts = entry.getKey().split("\\|");
                        String employeeName = keyParts[0];
                        String routeName = keyParts[1];

                        Map<String, Object> data = entry.getValue();
                        double totalAmount = (double) data.get("totalAmount");
                        int totalTrips = (int) data.get("totalTrips");

                        table.addCell(getDataCell(String.valueOf(seqNo)));
                        table.addCell(getDataCell(employeeName));
                        table.addCell(getDataCell(routeName));
                        table.addCell(getDataCell(String.valueOf(totalAmount)).setTextAlignment(TextAlignment.RIGHT));
                        table.addCell(getDataCell(String.valueOf(totalTrips)).setTextAlignment(TextAlignment.RIGHT));
                        table.addCell(getDataCell(String.valueOf(totalTrips * totalAmount))
                                        .setTextAlignment(TextAlignment.RIGHT));

                        seqNo++;
                        totalAmountSum += totalTrips * totalAmount;
                        totalRidesSum += totalTrips;
                }

                table.addCell(new Cell(1, 4).add(new Paragraph("Total  ").setBold())
                                .setTextAlignment(TextAlignment.CENTER));
                table.addCell(getDataCell(String.valueOf(totalRidesSum)).setTextAlignment(TextAlignment.RIGHT))
                                .setBold();
                table.addCell(getDataCell(String.valueOf(totalAmountSum)).setTextAlignment(TextAlignment.RIGHT))
                                .setBold();
                document.add(table);

                document.add(new Paragraph("\n"));

                document.close();

                return new File(filePath);
        }

        public File generateDayWiseDetailed(List<EmployeeTripDetails> empTripDetails, String formattedStartDate,
                        String formattedEndDate)
                        throws FileNotFoundException, MalformedURLException {
                String filePath = "Day_Wise_Detailed_Report.pdf";
                String logoPath = new File("Image/citylogo.png").getAbsolutePath();
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf, PageSize.A3);
                document.setMargins(20, 20, 20, 20);

                // Header (Logo Left, Address Right)
                Table headerTable = new Table(UnitValue.createPercentArray(new float[] { 3, 4 }))
                                .useAllAvailableWidth();
                ImageData imageData = ImageDataFactory.create(logoPath);
                Image logo = new Image(imageData);
                logo.scaleToFit(100, 50); // Resize logo (Width x Height)
                Cell logoCell = new Cell().add(logo).setBorder(Border.NO_BORDER);
                headerTable.addCell(logoCell);
                Paragraph address = new Paragraph()
                                .add(new Paragraph("CITY EXPRESS CABS").setBold().setFontSize(12)) // Bold Title
                                .add("\n") // Line break
                                .add(new Text("Address: ").setBold())
                                .add(" No.250, F Block, Kakkan Colony,\n")
                                .add("Besant Nagar, Chennai-600090\n")
                                .add("Phone: 918015161100  Email : cityexpresscalltaxi@gmail.com")
                                .setTextAlignment(TextAlignment.LEFT) // Align Right
                                .setMultipliedLeading(1.2f) // Adjust line spacing
                                .setPaddingRight(1) // Add padding to push it further right
                                .setPaddingLeft(80); // Increase spacing from the logo
                headerTable.addCell(new Cell().add(address).setBorder(Border.NO_BORDER)); // Right Side Address
                document.add(headerTable);

                DeviceRgb bgColor = new DeviceRgb(128, 128, 128);
                Paragraph title = new Paragraph("Day Wise Detailed Report")
                                .setBold()
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER)
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY) // **Background color for box**
                                .setBorder(new SolidBorder(ColorConstants.BLACK, 1)) // **Border Color**
                                .setPadding(10);
                document.add(title);
                document.add(new Paragraph("\n"));
                // Customer Details
                Table customerTable = new Table(UnitValue.createPercentArray(new float[] { 5, 2 }))
                                .useAllAvailableWidth();
                customerTable.addHeaderCell(getHeaderCell("Customer Address"));
                customerTable.addHeaderCell(getHeaderCell("Date"));

                Paragraph customerAddress = new Paragraph()
                                .add(new Text("SUPEROPS TECHNOLOGIES PRIVATE LIMITED,").setBold().setFontSize(12))
                                .add("\n") // Line break
                                .add(new Text("Address: ").setBold())
                                .add("6th Floor, Hardy Tower, Ramanujan IT City,\n")
                                .add("Rajiv Gandhi Salai (OMR), Taramani,\n")
                                .add("CHENNAI-6000113\n")
                                .add(new Text("GST No : 33AAICC2625E2Z3.").setBold())
                                .setTextAlignment(TextAlignment.LEFT);
                customerTable.addHeaderCell(new Cell().add(customerAddress).setTextAlignment(TextAlignment.CENTER));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(new Date());
                customerTable.addHeaderCell(getDataCell(formattedDate).setTextAlignment(TextAlignment.CENTER));
                document.add(customerTable);

                // Date Range
                if (formattedStartDate != null && formattedEndDate != null) {
                        document.add(new Paragraph("\nDate Range : " + formattedStartDate + " to " + formattedEndDate)
                                        .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
                } else if (formattedStartDate != null) {
                        document.add(new Paragraph("\nDate : " + formattedStartDate)
                                        .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT));
                }
                // Table Header
                float[] columnWidths = { 2, 3, 3, 3, 4, 4, 4 };
                Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
                table.setWidth(UnitValue.createPercentValue(100));
                table.addHeaderCell(
                                getHeaderCell("Sl.No").setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(getHeaderCell("Emp.Name").setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setTextAlignment(TextAlignment.LEFT));
                table.addHeaderCell(getHeaderCell("Route Name").setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setTextAlignment(TextAlignment.LEFT));
                table.addHeaderCell(getHeaderCell("Trip Type").setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setTextAlignment(TextAlignment.LEFT));
                table.addHeaderCell(getHeaderCell("Pickup Location").setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setTextAlignment(TextAlignment.LEFT));
                table.addHeaderCell(getHeaderCell("Drop Location").setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setTextAlignment(TextAlignment.LEFT));
                table.addHeaderCell(getHeaderCell("Driver Name").setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setTextAlignment(TextAlignment.LEFT));

                // Data Rows
                int seqNo = 1;
                for (EmployeeTripDetails empTripDetail : empTripDetails) {
                        table.addCell(getDataCell(String.valueOf(seqNo)));
                        table.addCell(getDataCell(empTripDetail.getEmployeeName()));
                        table.addCell(getDataCell(empTripDetail.getRouteName()));
                        table.addCell(getDataCell(empTripDetail.getTripType()));
                        table.addCell(getDataCell(empTripDetail.getPickupLocation()).setKeepTogether(true));
                        table.addCell(getDataCell(empTripDetail.getDropLocation()).setKeepTogether(true));
                        table.addCell(getDataCell(empTripDetail.getDriverName()).setKeepTogether(true));
                        seqNo++;
                }

                document.add(table);

                document.add(new Paragraph("\n"));
                document.close();

                return new File(filePath);
        }

}
