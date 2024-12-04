package com.vash.entel.service.impl;

import com.vash.entel.repository.SurveyRepository;
import com.vash.entel.service.SurveyReportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Workbook;

@Service
@RequiredArgsConstructor
public class SurveyReportServiceImpl implements SurveyReportService {
    // private final SurveyRepository surveyReportRepository;
    private final SurveyRepository surveyRepository;

    @Override
    public List<Map<String, Object>> getSurveyReport() {
        // Llamar a la función desde el repositorio
        List<Object[]> rawData = surveyRepository.getSurveyReport();

        // Convertir el resultado a una lista de mapas clave-valor
        return rawData.stream().map(row -> Map.of(
                "adviserUsername", row[0], // Username del asesor
                "averageValue", row[1], // Promedio de la encuesta
                "quantity", row[2], // Cantidad de encuestas
                "consultDate", row[3] // Fecha de consulta
        )).collect(Collectors.toList());
    }

    @Override
    public byte[] generatePieChart() throws IOException {
        // Crear dataset para el gráfico circular
        DefaultPieDataset dataset = new DefaultPieDataset();
        surveyRepository.getSurveyReport().forEach(row -> {
            String adviserUsername = (String) row[0];
            Long quantity = ((Number) row[2]).longValue(); // Asegurarse de que sea un Long
            dataset.setValue(adviserUsername, quantity);
        });

        // Crear gráfico circular
        JFreeChart chart = ChartFactory.createPieChart(
                "Porcentaje de Atenciones realizadas por Asesor",
                dataset,
                true, // incluir leyenda
                true, // incluir tooltips
                false // URLs
        );

        // Personalizar el renderizador del gráfico para mostrar porcentajes
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {2}", // {0}: clave, {1}: valor, {2}: porcentaje
                new DecimalFormat("0"), // Formato para valores absolutos
                new DecimalFormat("0.00%") // Formato para porcentajes
        ));

        // Cambiar el color de fondo
        chart.setBackgroundPaint(Color.WHITE); // Fondo del gráfico
        plot.setBackgroundPaint(Color.WHITE); // Fondo del área del gráfico (plot area)

        // Convertir a byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(byteArrayOutputStream, chart, 800, 600);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] generateBarChart() throws IOException {
        // Crear dataset para el gráfico de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        surveyRepository.getSurveyReport().forEach(row -> {
            String adviserUsername = (String) row[0];
            Double averageValue = ((Number) row[1]).doubleValue(); // Asegurarse de que sea un Double
            dataset.addValue(averageValue, "Promedio", adviserUsername);
        });

        // Crear gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
                "Promedio de Calificación por Asesor", // Título
                "Asesor", // Etiqueta del eje X
                "Promedio", // Etiqueta del eje Y
                dataset);

        // Obtener el CategoryPlot para personalización
        CategoryPlot plot = chart.getCategoryPlot();

        // Cambiar el color de fondo del área del gráfico
        plot.setBackgroundPaint(Color.WHITE);

        // Cambiar el color de las líneas del grid
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinePaint(Color.DARK_GRAY);

        // Cambiar el color de fondo del gráfico completo
        chart.setBackgroundPaint(Color.WHITE);

        // Convertir a byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(byteArrayOutputStream, chart, 800, 600);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] generateExcelReport() throws IOException {
        // Crear el libro de Excel
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Crear la hoja de datos
        XSSFSheet sheet = workbook.createSheet("Survey Report");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Asesor");
        header.createCell(1).setCellValue("Cantidad");
        header.createCell(2).setCellValue("Promedio");
        header.createCell(3).setCellValue("Fecha");

        // Llenar la tabla de datos
        List<Object[]> surveyData = surveyRepository.getSurveyReport();
        int rowIndex = 1;
        for (Object[] row : surveyData) {
            Row dataRow = sheet.createRow(rowIndex++);
            dataRow.createCell(0).setCellValue((String) row[0]); // Asesor
            dataRow.createCell(1).setCellValue(((Number) row[2]).longValue()); // Cantidad
            dataRow.createCell(2).setCellValue(((Number) row[1]).doubleValue()); // Promedio
            dataRow.createCell(3).setCellValue((String) row[3]); // Fecha
        }

        // Ajustar automáticamente las columnas
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        // Crear la hoja de gráficos
        XSSFSheet chartSheet = workbook.createSheet("Gráficos");

        // Agregar el gráfico circular
        byte[] pieChart = generatePieChart();
        int piePictureIdx = workbook.addPicture(pieChart, Workbook.PICTURE_TYPE_PNG);
        XSSFDrawing pieDrawing = chartSheet.createDrawingPatriarch();
        XSSFClientAnchor pieAnchor = new XSSFClientAnchor();
        pieAnchor.setCol1(0);
        pieAnchor.setRow1(0);
        pieAnchor.setCol2(10);
        pieAnchor.setRow2(20);
        pieDrawing.createPicture(pieAnchor, piePictureIdx);

        // Agregar el gráfico de barras
        byte[] barChart = generateBarChart();
        int barPictureIdx = workbook.addPicture(barChart, Workbook.PICTURE_TYPE_PNG);
        XSSFDrawing barDrawing = chartSheet.createDrawingPatriarch();
        XSSFClientAnchor barAnchor = new XSSFClientAnchor();
        barAnchor.setCol1(0);
        barAnchor.setRow1(20);
        barAnchor.setCol2(10);
        barAnchor.setRow2(40);
        barDrawing.createPicture(barAnchor, barPictureIdx);

        // Convertir el libro a un array de bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

}
