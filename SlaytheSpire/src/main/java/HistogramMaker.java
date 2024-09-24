import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.chart.ChartUtils;
import javax.swing.*;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class HistogramMaker extends JFrame {

    //Method used to generate histogram using JFreeChart.
    //Input: List<Double> costs; List of the energy costs found in a deck, String filePath; File path to be saved to.
    public void generateHistogram(List<Double> costs, String filePath) {
        //Creating histogram dataset using costs list.
        HistogramDataset dataset = new HistogramDataset();
        double[] cost = new double[costs.size()];
        for (int i = 0; i < costs.size(); i++){
            cost[i] = costs.get(i);
        }
        //Creating the histogram using JFreeChart method
        dataset.addSeries("Frequency", cost, 10);
        JFreeChart histogram = ChartFactory.createHistogram(
                "Energy Distribution",
                "Energy",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        //Saving the created histogram as a png file to be used in pdf creation later.
        try {
            ChartUtils.saveChartAsPNG(new File(filePath), histogram, 800, 600);
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
        }
    }
}
