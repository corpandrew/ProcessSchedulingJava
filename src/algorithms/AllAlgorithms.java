package algorithms;

import objects.Process;
import sample.AllAlgorithmsGraph;
import sample.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by corpa on 7/17/17.
 */
public class AllAlgorithms extends Algorithm {

    @Override
    public List<Process> start(List<Process> processList) {
        List<Process> freshList;
        List<Algorithm> allAlgorithms = new ArrayList<>(Arrays.asList(new SJF(), new FCFS(), new Priority(), new RoundRobinFixed(), new RoundRobinVariable(), new SRT()));

        for(Algorithm a : allAlgorithms) {
            freshList = cloneList(processList);
            a.start(freshList);
        }

        //set the data to the new AlgorithmGraph to the tab in UI
        Main.algorithmTimeTab.setContent(new AllAlgorithmsGraph(allAlgorithms).getViewer());

        return processList;
    }

    @Override
    public String toString() {
        return "All Algorithms";
    }

    /**
     * For deep copying an array list
     * @param processList process list to clone
     * @return a copy of the process list
     */
    private List<Process> cloneList(List<Process> processList){
        List<Process> copyProcessList = new ArrayList<>();
        for (Process p : processList) {
            copyProcessList.add(p.clone());
        }
        return copyProcessList;
    }
}
