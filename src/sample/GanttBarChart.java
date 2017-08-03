package sample;

import objects.Process;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.text.NumberFormat;
import java.util.List;

/**
 * Gantt Chart to visually show the output to the user.
 */
public class GanttBarChart {

    private List<Process> processList;

    public GanttBarChart() {

    }

    /**
     * Create a GanttChart with
     * @param processList List of finished processes to create the chart with
     */
    public GanttBarChart(List<Process> processList) {
        this.processList = processList;
    }

    /**
     * Create a dataset with each of the processes
     * @return the dataset
     */
    public DefaultCategoryDataset createDataset() {

        DefaultCategoryDataset result = new DefaultCategoryDataset();

        if(processList == null){//avoid NPE
            return result;
        }

        for (Process p : processList) {
            result.addValue(p.getBurstTime(), "P" + p.getPid(), "");
        }

        return result;
    }

    /**
     * Create the JFreeChart
     * @param dataset dataset
     * @return the chart
     */
    private JFreeChart createChart(DefaultCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createStackedBarChart(
                "Gantt ", // chart title
                "PID", // domain axis label
                "Time (ms)", // range axis label
                dataset
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        plot.setAnchorValue(0);

        //custom tooltips
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultToolTipGenerator(new IntervalCategoryToolTipGenerator("({0}{1},{2})", NumberFormat.getInstance()));

        return chart;
    }

    //convert this to JFX able chart
    public ChartViewer getViewer() {
        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        return new ChartViewer(chart);
    }

}
