package sample;

import algorithms.Algorithm;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.text.NumberFormat;
import java.util.List;

/**
 * Chart to visually show the run times of the algorithms
 * Same as Gantt Bar Chart practically
 */
public class AllAlgorithmsGraph {

    private List<Algorithm> algorithmList;

    public AllAlgorithmsGraph() {

    }

    public AllAlgorithmsGraph(List<Algorithm> algorithmList) {
        this.algorithmList = algorithmList;
    }

    public DefaultCategoryDataset createDataset() {

        DefaultCategoryDataset result = new DefaultCategoryDataset();

        if (algorithmList == null) {
            return result;
        }

        for (Algorithm a : algorithmList) {
            //use the variable finishTime to plot the runTime
            result.addValue(a.finishTime, a.toString(), a.toString());
        }

        return result;
    }


    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createStackedBarChart(
                "Time Completed ", // chart title
                "Algorithm", // domain axis label
                "Time (ms)", // range axis label
                dataset
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setAnchorValue(0);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultToolTipGenerator(new IntervalCategoryToolTipGenerator("({0}, RunTime = {2})", NumberFormat.getInstance()));

        return chart;
    }

    public ChartViewer getViewer() {
        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        return new ChartViewer(chart);
    }

}
