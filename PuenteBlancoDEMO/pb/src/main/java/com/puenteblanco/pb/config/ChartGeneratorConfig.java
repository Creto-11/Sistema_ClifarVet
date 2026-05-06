package com.puenteblanco.pb.config;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.image.BufferedImage;
import java.util.Map;

public class ChartGeneratorConfig {

    public static BufferedImage generarGraficoBarras(Map<String, Long> datos, String titulo, String ejeX, String ejeY) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        datos.forEach((categoria, valor) -> dataset.addValue(valor, "", categoria));

        JFreeChart chart = ChartFactory.createBarChart(
                titulo,
                ejeX,
                ejeY,
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        return chart.createBufferedImage(500, 300);
    }

    public static BufferedImage generarGraficoLineas(Map<String, Long> datos, String titulo, String ejeX, String ejeY) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        datos.forEach((clave, valor) -> dataset.addValue(valor, "Citas", clave));

        JFreeChart chart = ChartFactory.createLineChart(
                titulo,
                ejeX,
                ejeY,
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        return chart.createBufferedImage(500, 300);
    }
}
