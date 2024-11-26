package com.vash.entel.api;

import com.vash.entel.service.SurveyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/survey-report")
@RequiredArgsConstructor
public class SurveyReportController {
    private final SurveyReportService surveyReportService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSurveyReport() {
        return ResponseEntity.ok(surveyReportService.getSurveyReport());
    }
}
