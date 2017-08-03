package sample;

import algorithms.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import objects.Process;
import org.jfree.chart.fx.ChartViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User Interface Class
 */
public class Main extends Application {

    private ObservableList<Algorithm> algorithmTypes = FXCollections.observableArrayList(new SJF(), new FCFS(), new Priority(), new RoundRobinFixed(), new RoundRobinVariable(), new SRT(), new AllAlgorithms());
    private int processInt;
    private Stage primaryStage;
    public static TextArea outputArea;
    private Button addProcessButton = null;
    public static Spinner quantumSpinner;

    /**
     * List of Process's in the table
     */
    public static ObservableList<Process> processObservableList = FXCollections.observableArrayList();

    private TableView<Process> processTableView = new TableView<>(processObservableList);

    public static Tab algorithmTimeTab;

    @Override
    public void start(Stage primaryStage) {
        newWelcomeStage();
    }

    private Scene loadTableStage() {
        processInt = 1;

        TabPane tabPane = new TabPane();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab processTab = new Tab("Process");
        Tab ganttChartTab = new Tab("Gantt Chart");
        algorithmTimeTab = new Tab("Algorithm Comparison");

        GridPane gridPane = new GridPane();
        processTab.setContent(gridPane);

        ChartViewer ganttChart = new GanttBarChart().getViewer();

        Scene viewer = new Scene(ganttChart);

        GridPane pane = new GridPane();
        pane.add(ganttChart, 0, 0);

        ganttChartTab.setContent(viewer.getRoot());

        tabPane.getTabs().addAll(processTab, ganttChartTab, algorithmTimeTab);

        Scene scene = new Scene(tabPane);

        primaryStage.setTitle("Process Scheduling Simulator");

        Label textLabel = new Label("Process Scheduling Simulator");
        textLabel.setFont(new Font(25));
        textLabel.setAlignment(Pos.CENTER);

        processTableView.setEditable(true);

        TableColumn processIDColumn = new TableColumn("Process ID");
        processIDColumn.setMinWidth(100);
        processIDColumn.setCellValueFactory(
                new PropertyValueFactory<Process, String>("pid"));
        processIDColumn.setEditable(false);

        TableColumn arrivalTimeColumn = new TableColumn("Arrival Time");
        arrivalTimeColumn.setMinWidth(100);
        arrivalTimeColumn.setCellValueFactory(
                new PropertyValueFactory<Process, String>("arrivalTime"));

        TableColumn burstTimeColumn = new TableColumn("Burst Time");
        burstTimeColumn.setMinWidth(100);
        burstTimeColumn.setCellValueFactory(
                new PropertyValueFactory<Process, String>("burstTime"));

        TableColumn priorityColumn = new TableColumn("Priority");
        priorityColumn.setMinWidth(100);
        priorityColumn.setCellValueFactory(
                new PropertyValueFactory<Process, String>("priority"));

        processTableView.setItems(processObservableList);

        processTableView.getColumns().addAll(processIDColumn, arrivalTimeColumn, burstTimeColumn, priorityColumn);

        final VBox gridVBox = new VBox();
        gridVBox.setSpacing(5);
        gridVBox.setPadding(new Insets(10, 12, 15, 12));
        gridVBox.getChildren().addAll(textLabel, processTableView);

        processTableView.setColumnResizePolicy((param) -> true);

        //for pressing delete and back space in the table to delete a row
        processTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
                    Process selectedProcess = processTableView.getSelectionModel().getSelectedItem();
                    processObservableList.remove(selectedProcess);
                }
            }
        });

        HBox hBox = new HBox();
        hBox.setSpacing(10);

        NumberTextField arrivalTimeTextField = new NumberTextField();
        arrivalTimeTextField.setPromptText("Arrival Time");

        NumberTextField burstTimeTextField = new NumberTextField();
        burstTimeTextField.setPromptText("Burst Time");

        NumberTextField priorityTextField = new NumberTextField();
        priorityTextField.setPromptText("Priority");
        //if you press enter on the priority text field it will add the process to the table
        priorityTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    addProcessButton.fire();
                } else if (keyEvent.getCode() == KeyCode.TAB) {
                    keyEvent.consume();
                    arrivalTimeTextField.requestFocus();
                }
            }
        });

        //adding the process with error checking (if one value is empty or one priority is already in the table) alert the user
        addProcessButton = new Button("Add Process");
        addProcessButton.setOnAction(event -> {

            int priority = getValue(priorityTextField.getText());

            for (Process proc : processObservableList) {
                if (proc.getPriority() == priority) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Can't add process to table");
                    error.setHeaderText("Couldn't add process to the table");
                    error.setContentText("You can't have 2 process's with the same priority!");
                    error.showAndWait();
                    return;
                }
            }

            try {
                Process p = new Process(processInt++, getValue(arrivalTimeTextField.getText()), getValue(burstTimeTextField.getText()), getValue(priorityTextField.getText()));

                processObservableList.add(p);

                arrivalTimeTextField.clear();
                burstTimeTextField.clear();
                priorityTextField.clear();
            } catch (NumberFormatException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error Adding to Table");
                error.setHeaderText("Couldn't add process to the table");
                error.setContentText("One of the values was entered incorrectly. Try Again!");
                error.showAndWait();
                processInt--;
            }
        });


        hBox.getChildren().addAll(arrivalTimeTextField, burstTimeTextField, priorityTextField, addProcessButton);
        gridVBox.getChildren().add(hBox);

        HBox buttonHBox = new HBox();
        buttonHBox.setSpacing(10);

        //generating random process
        Button generateRandomData = new Button("Generate Random Process");
        generateRandomData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Random r = new Random(System.currentTimeMillis());// seed
                int arrivalTime, burstTime, priority;

                arrivalTime = r.nextInt(25);
                burstTime = 3 + r.nextInt(25);
                priority = r.nextInt(100);

                Process p = new Process(processInt++, arrivalTime, burstTime, priority);

                processObservableList.add(p);

                outputArea.appendText("\nGenerated a process to the table");

                GanttBarChart a = new GanttBarChart(processObservableList);

                ganttChartTab.setContent(a.getViewer());
            }
        });

        //clear table
        Button clearTableButton = new Button("Clear Table");
        clearTableButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processInt = 1;
                processObservableList.clear();
            }
        });

        buttonHBox.getChildren().addAll(generateRandomData, clearTableButton);
        gridVBox.getChildren().add(buttonHBox);

        HBox mainHBox = new HBox();
        mainHBox.setPadding(new Insets(10, 12, 10, 12));
        mainHBox.setSpacing(10);

        VBox leftVBox = new VBox();
        leftVBox.setPadding(new Insets(10, 12, 10, 12));
        leftVBox.setSpacing(10);

        HBox optionHbox = new HBox();
        optionHbox.setPadding(new Insets(10, 12, 10, 0));
        optionHbox.setSpacing(10);

        Label quantumLabel = new Label("Quantum : ");

        quantumSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 10);
        quantumSpinner.setValueFactory(valueFactory);
        quantumSpinner.setEditable(true);
        quantumSpinner.setTooltip(new Tooltip("Quantum Value"));

        ComboBox<Algorithm> algorithmComboBox = new ComboBox<>(algorithmTypes);
        algorithmComboBox.getSelectionModel().selectFirst(); //select the first element

        quantumSpinner.setDisable(true);

        algorithmComboBox.setOnAction(event -> {
            switch (algorithmComboBox.getValue().toString()) {
                case "All Algorithms":
                    quantumSpinner.setDisable(false);
                    break;
                case "Priority":
                    quantumSpinner.setDisable(true);
                    break;
                case "SRT":
                    quantumSpinner.setDisable(true);
                    break;
                case "RR Fixed":
                    quantumSpinner.setDisable(false);
                    break;
                case "RR Variable":
                    quantumSpinner.setDisable(false);
                    break;
                default:
                    quantumSpinner.setDisable(true);
            }
        });

        Button runAlgorithmButton = new Button("Start Algorithm");

        //to run the algorithm, with error checking (no processes in the table)
        runAlgorithmButton.setOnAction(event -> {

            if (processObservableList.isEmpty()) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Add Data to Table");
                error.setHeaderText("You must add data to the table first!");
                error.setContentText("You can add it manually or click the Generate Random button.");
                error.showAndWait();
            } else {
                Algorithm selectedAlgorithm = algorithmComboBox.getValue();

                //clone the list for no modification issues
                List<Process> copyProcessList = new ArrayList<>();
                for (Process p : processObservableList) {
                    copyProcessList.add(p.clone());
                }
                List<Process> processes = selectedAlgorithm.start(copyProcessList);

                GanttBarChart a = new GanttBarChart(processes);
                ganttChartTab.setContent(a.getViewer());
            }
        });

        runAlgorithmButton.setAlignment(Pos.CENTER);

        outputArea = new TextArea("Algorithm Output");
        outputArea.setEditable(false);
        outputArea.setMaxWidth(400);

        Button clearOutputButton = new Button("Clear Output");
        clearOutputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                outputArea.clear();
            }
        });

        HBox runAlgClearHBox = new HBox(runAlgorithmButton, clearOutputButton);

        optionHbox.getChildren().addAll(algorithmComboBox, quantumLabel, quantumSpinner);
        leftVBox.getChildren().addAll(optionHbox, runAlgClearHBox, outputArea);

        mainHBox.getChildren().addAll(leftVBox, gridVBox);

        gridPane.add(mainHBox, 0, 0);
        outputArea.setMinHeight(primaryStage.getHeight() - 2);

        primaryStage.setResizable(false);

        return scene;

    }

    private int getValue(String valueString) {
        return Integer.parseInt(valueString);
    }

    private Scene newWelcomeStage() {

        primaryStage = new Stage();

        Group welcomeGroup = new Group();
        Scene welcomeScene = new Scene(welcomeGroup);
        primaryStage.setScene(welcomeScene);

        VBox welcomeVBox = new VBox();
        welcomeVBox.setPadding(new Insets(15, 12, 15, 12));
        welcomeVBox.setSpacing(10);
        welcomeGroup.getChildren().add(welcomeVBox);

        primaryStage.setTitle("Welcome to Process Scheduling!");

        welcomeVBox.setPadding(new Insets(15, 12, 15, 12));
        welcomeVBox.setSpacing(10);
        welcomeVBox.getChildren().add(new Label(""));

        String username = System.getProperty("user.name");
        Label textLabel = new Label("Welcome " + username.toUpperCase().charAt(0) + username.substring(1)/*username.replace(username.charAt(0), (char) (username.charAt(0) + 32))*/ + "!");
        welcomeVBox.getChildren().add(textLabel);
        textLabel.setFont(new Font(30));
        textLabel.alignmentProperty().setValue(Pos.CENTER);

        TextArea instructionTextArea = new TextArea("How to use: \n1.\tAdd processes to table or pick random amount to add.\n2.\tPick algorithm to run from Dropdown.\n3.\tTake a gander at the output or head on over to the Gantt chart tab!");
        instructionTextArea.setEditable(false);
        welcomeVBox.getChildren().add(instructionTextArea);

        Button nextScene = new Button("Continue");
        nextScene.setOnAction(event -> {
            primaryStage.setScene(loadTableStage());
        });

        welcomeVBox.getChildren().add(nextScene);

        primaryStage.show();
        return welcomeScene;

    }

    public static void main(String[] args) {
        launch(args);
    }

}
