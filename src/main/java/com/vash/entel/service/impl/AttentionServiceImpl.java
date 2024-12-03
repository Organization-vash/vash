package com.vash.entel.service.impl;

import com.vash.entel.exception.ResourceNotFoundException;
import com.vash.entel.exception.BadRequestException;
import com.vash.entel.model.entity.Attention;
import com.vash.entel.model.entity.Service;
import com.vash.entel.model.enums.AttentionStatus;
import com.vash.entel.model.enums.SuccessStatus;
import com.vash.entel.repository.AttentionRepository;
import com.vash.entel.repository.ServiceRepository;
import com.vash.entel.service.AttentionService;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@org.springframework.stereotype.Service 

public class AttentionServiceImpl implements AttentionService {

    private final AttentionRepository attentionRepository;
    private final ServiceRepository serviceRepository;

    public AttentionServiceImpl(AttentionRepository attentionRepository, ServiceRepository serviceRepository) {
        this.attentionRepository = attentionRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void addServiceToAttention(Integer attentionId, Integer serviceId) {
        Attention attention = attentionRepository.findById(attentionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attention no encontrada"));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service no encontrado"));

        attention.getServices().add(service);
        attentionRepository.save(attention);
    }

    @Override
    public void removeServiceFromAttention(Integer attentionId, Integer serviceId) {
        Attention attention = attentionRepository.findById(attentionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attention no encontrada"));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service no encontrado"));

        if (attention.getServices().size() <= 1) {
            throw new BadRequestException("Debe haber al menos un servicio seleccionado");
        }

        attention.getServices().remove(service);
        attentionRepository.save(attention);
    }

    @Override
    public ResponseEntity<Map<String, String>> finalizeAttention(Integer attentionId) {
        Optional<Attention> attentionOpt = attentionRepository.findById(attentionId);

        if (attentionOpt.isPresent()) {
            Attention attention = attentionOpt.get();

            if ((attention.getSuccessStatus() == SuccessStatus.SUCCESSFUL || attention.getSuccessStatus() == SuccessStatus.NOT_SUCCESSFUl)
                    && (attention.getAttentionStatus() == AttentionStatus.ATTEND || attention.getAttentionStatus() == AttentionStatus.NOT_ATTENDING)) {
                attention.setUpdated_at(LocalDateTime.now());
                attentionRepository.save(attention);

                return ResponseEntity.ok(Map.of("message", "Atención finalizada con éxito"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "No se cumplen las condiciones para finalizar la atención"));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Atención no encontrada"));
        }
    }

    @Override
    public byte[] generateExcelReport() throws IOException {
        // Lógica para obtener los datos y construir el Excel
        List<Object[]> reportData = attentionRepository.getAttentionReport();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte de Atenciones");

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "DocNumber", "Fullname", "DocumentType", "Code", "Service",
                    "AttentionStatus", "SuccessStatus", "Adviser", "SurveyValue"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            // Llenar datos en el Excel
            int rowNum = 1;
            for (Object[] rowData : reportData) {
                Row row = sheet.createRow(rowNum++);
                for (int col = 0; col < rowData.length; col++) {
                    Cell cell = row.createCell(col);
                    cell.setCellValue(rowData[col] == null ? "" : rowData[col].toString());
                }
            }

            // Ajustar tamaños de las columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir el workbook a un array de bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    @Override
    public List<Map<String, Object>> getReportData() {
        List<Object[]> rawData = attentionRepository.getAttentionReport();

        // Convertir los resultados en una lista de mapas
        return rawData.stream().map(row -> Map.of(
                "DocNumber", row[0],
                "Fullname", row[1],
                "DocumentType", row[2],
                "Code", row[3],
                "Service", row[4],
                "AttentionStatus", row[5],
                "SuccessStatus", row[6],
                "Adviser", row[7],
                "SurveyValue", row[8]
        )).collect(Collectors.toList());
    }

}

