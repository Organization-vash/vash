package com.vash.entel.service.impl;

import com.vash.entel.repository.SurveyRepository;
import com.vash.entel.service.SurveyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyReportServiceImpl implements SurveyReportService {
    private final SurveyRepository surveyReportRepository;

    @Override
    public List<Map<String, Object>> getSurveyReport() {
        // Llamar a la función desde el repositorio
        List<Object[]> rawData = surveyReportRepository.getSurveyReport();

        // Convertir el resultado a una lista de mapas clave-valor
        return rawData.stream().map(row -> Map.of(
                "adviserUsername", row[0], // Username del asesor
                "averageValue", row[1],   // Promedio de la encuesta
                "quantity", row[2],       // Cantidad de encuestas
                "consultDate", row[3]     // Fecha de consulta
        )).collect(Collectors.toList());
    }
}
