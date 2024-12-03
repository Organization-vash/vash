package com.vash.entel.api;

import com.vash.entel.service.SurveyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.io.IOException;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/survey-report")
@RequiredArgsConstructor
public class SurveyReportController {
    private final SurveyReportService surveyReportService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSurveyReport() {
        return ResponseEntity.ok(surveyReportService.getSurveyReport());
    }

    @GetMapping("/pie-chart")
    public ResponseEntity<byte[]> getPieChart() throws IOException {
        byte[] chart = surveyReportService.generatePieChart();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/png");
        headers.set("Content-Disposition", "inline; filename=pie-chart.png");
        return new ResponseEntity<>(chart, headers, HttpStatus.OK);
    }

    @GetMapping("/bar-chart")
    public ResponseEntity<byte[]> getBarChart() throws IOException {
        byte[] chart = surveyReportService.generateBarChart();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/png");
        headers.set("Content-Disposition", "inline; filename=bar-chart.png");
        return new ResponseEntity<>(chart, headers, HttpStatus.OK);
    }

    @GetMapping("/download-excel")
    public ResponseEntity<byte[]> downloadExcel() throws IOException {
        byte[] excelFile = surveyReportService.generateExcelReport();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        headers.set("Content-Disposition", "attachment; filename=SurveyReport.xlsx");
        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }
}
