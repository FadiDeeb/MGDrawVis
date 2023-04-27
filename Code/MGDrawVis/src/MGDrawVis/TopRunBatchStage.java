
package MGDrawVis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TopRunBatchStage extends Stage
{
    public GridPane layout1; 
    public GridPane layout;
    
    public GridPane firstRow;
    public Label row1Label1;
    public ComboBox chooseMethod; 
    public ObservableList methodsList; 
    
    public GridPane secondRow;
    public Label runModeLabel;
    public Label evalSolLabel;
    public Label objFunLabel; 
    public Label timeLabel;
    public static TextField evalSolText;
    public static TextField objFunText;
    public static TextField timeText;
    public ToggleGroup phaseToggle; 
    public RadioButton phase1Radio; 
    public RadioButton phase2Radio;
    public RadioButton phase3Radio;
    public RadioButton phase4Radio;
    
    public GridPane thirdRow;
    public Label numRunsEachCaseLabel; 
    public TextField numRunsEachCaseText; 
    
    public GridPane fourthRow;
    public Button selectFilesButton; 
    public Button selectParameters; 
    public Button runGoButton; 
    
    public ShowRunFilesGridPane showRunFile; 
    
    public boolean selectFilesFlag, selectParametersFlag; 
    public int numRuns; 
    
    public Scene scene; 
    
    ArrayList<Double> batchObjFun;
    ArrayList<Double> batchEvalSol;
    ArrayList<Double> batchTime;
    ArrayList<Double> batchNodesOcc;
    ArrayList<Double> batchEdgeLen;
    ArrayList<Double> batchCrossing;
    ArrayList<Double> batchNodeEdgeOcc;
    ArrayList<Double> batchAngular;
    
    ArrayList<Double> allObjFunMax;
    ArrayList<Double> allObjFunMin;
    ArrayList<Double> allObjFunAvg;
    ArrayList<Double> allObjFunMedian;
    ArrayList<Double> allObjFunSD;
    ArrayList<Double> allEvalSolMax;
    ArrayList<Double> allEvalSolMin;
    ArrayList<Double> allEvalSolAvg;
    ArrayList<Double> allEvalSolMedian;
    ArrayList<Double> allEvalSolSD;
    ArrayList<Double> allTimeMax;
    ArrayList<Double> allTimeMin;
    ArrayList<Double> allTimeAvg;
    ArrayList<Double> allTimeMedian;
    ArrayList<Double> allTimeSD;
    ArrayList<Double> allNodesOccMax;
    ArrayList<Double> allNodesOccMin;
    ArrayList<Double> allNodesOccAvg;
    ArrayList<Double> allNodesOccMedian;
    ArrayList<Double> allNodesOccSD;
    ArrayList<Double> allEdgeLenMax;
    ArrayList<Double> allEdgeLenMin;
    ArrayList<Double> allEdgeLenAvg;
    ArrayList<Double> allEdgeLenMedian;
    ArrayList<Double> allEdgeLenSD;
    ArrayList<Double> allCrossingMax;
    ArrayList<Double> allCrossingMin;
    ArrayList<Double> allCrossingAvg;
    ArrayList<Double> allCrossingMedian;
    ArrayList<Double> allCrossingSD;
    ArrayList<Double> allNodeEdgeOccMax;
    ArrayList<Double> allNodeEdgeOccMin;
    ArrayList<Double> allNodeEdgeOccAvg;
    ArrayList<Double> allNodeEdgeOccMedian;
    ArrayList<Double> allNodeEdgeOccSD;
    ArrayList<Double> allAngularMax;
    ArrayList<Double> allAngularMin;
    ArrayList<Double> allAngularAvg;
    ArrayList<Double> allAngularMedian;
    ArrayList<Double> allAngularSD;
       
    public TopRunBatchStage(DrawingStageActions panel) throws FileNotFoundException, IOException, InterruptedException
    {
        selectFilesFlag = false; 
        selectParametersFlag = false; 
        firstRow = new GridPane(); 
        firstRow.setPadding(new Insets(10,10,10,10));
        row1Label1 = new Label("Choose a method");
        row1Label1.setPrefWidth(140);
        String[] methods = {"Jaya I", "Jaya+Grid", "Hill Climbing", "Simulated Annealing"};
        methodsList = FXCollections.observableArrayList(methods);
        chooseMethod = new ComboBox(methodsList);
        chooseMethod.setPromptText("Choose a method");
        firstRow.add(row1Label1, 0, 0);
        firstRow.add(chooseMethod, 1, 0);
        
        secondRow = new GridPane(); 
        secondRow.setPadding(new Insets(10,10,10,10)); 
        secondRow.setVgap(5);
        runModeLabel = new Label("Choose Run Mode");
        runModeLabel.setPrefWidth(180);
        runModeLabel.setUnderline(true);
        phaseToggle = new ToggleGroup();
        phase1Radio = new RadioButton ("No Limit (Phase 1)");
        phase1Radio.setSelected(true); 
        phase1Radio.setToggleGroup(phaseToggle);
        phase2Radio = new RadioButton ("Fixed Number of Solutions (Phase 2)");
        phase2Radio.setPrefWidth(250);
        phase2Radio.setToggleGroup(phaseToggle);
        phase3Radio = new RadioButton ("Fixed Objective Function (Phase 3)");
        phase3Radio.setToggleGroup(phaseToggle);
        phase4Radio = new RadioButton ("Fixed Execution Time (Phase 4)");
        phase4Radio.setToggleGroup(phaseToggle);
        evalSolLabel = new Label("   Number of Solutions");
        evalSolLabel.setPrefWidth(180);
        evalSolText = new TextField();
        evalSolText.setPrefWidth(40);
        objFunLabel = new Label("   Objective Function Value");
        objFunLabel.setPrefWidth(180);
        objFunText = new TextField();
        objFunText.setPrefWidth(40);
        timeLabel = new Label("   Execution Time (sec)");
        timeLabel.setPrefWidth(180);
        timeText = new TextField();
        timeText.setPrefWidth(40);
        secondRow.add(runModeLabel, 0, 0);
        secondRow.add(phase1Radio, 0, 1);
        secondRow.add(phase2Radio, 0, 2);
        secondRow.add(evalSolLabel, 1, 2);
        secondRow.add(evalSolText, 2, 2);
        secondRow.add(phase3Radio, 0, 3);
        secondRow.add(objFunLabel, 1, 3);
        secondRow.add(objFunText, 2, 3);
        secondRow.add(phase4Radio, 0, 4);
        secondRow.add(timeLabel, 1, 4);
        secondRow.add(timeText, 2, 4);        
    
        thirdRow = new GridPane(); 
        thirdRow.setPadding(new Insets(10,10,10,10)); 
        numRunsEachCaseLabel = new Label("Number of Runs on Each Test Case "); 
        numRunsEachCaseLabel.setPrefWidth(200);
        numRunsEachCaseText = new TextField("1");
        numRunsEachCaseText.setPrefWidth(40);
        thirdRow.add(numRunsEachCaseLabel, 0, 0);
        thirdRow.add(numRunsEachCaseText, 1, 0);
        
        fourthRow = new GridPane(); 
        fourthRow.setPadding(new Insets(10,10,10,10)); 
        fourthRow.setHgap(20);
        selectFilesButton = new Button("Select Files");
        selectFilesButton.setPrefWidth(150);
        selectParameters = new Button("Select Parameters");
        selectParameters.setPrefWidth(150);
        runGoButton = new Button("Go");
        runGoButton.setPrefWidth(150);
        fourthRow.add(selectFilesButton, 0, 0);
        fourthRow.add(selectParameters, 1, 0);
        fourthRow.add(runGoButton, 2, 0);
        showRunFile = new ShowRunFilesGridPane(); 
        
        selectFilesButton.setOnAction(e -> {
            try {
                handleSelectFiles(panel);
            } catch (Exception ex) {
                System.out.println("File Not Found");
            }
        });
        
        selectParameters.setOnAction(e -> {
            try {
                handleSelectParameters(panel);
            } catch (Exception ex) {
                System.out.println("File Not Found");
            }
        });
        
        runGoButton.setOnAction(e -> {
            try {
                handleGoButton(panel);
            } catch (Exception ex) {
                System.out.println("File Not Found");
            }
        });
       
        layout1 = new GridPane(); 
        layout1.setPadding(new Insets(20,20,20,20));
        layout1.add(firstRow, 0, 0);
        layout1.add(secondRow, 0, 1);
        layout1.add(thirdRow, 0, 2);
        layout1.add(fourthRow, 0, 3);
        layout = new GridPane(); 
        layout.setPadding(new Insets(20,20,20,20));
        layout.add(layout1, 0, 0);
        layout.add(showRunFile, 0, 1);   
        scene = new Scene(layout,600,500);
        this.setScene(scene);
        this.show(); 
    }
    
    public void handleSelectFiles(DrawingStageActions panel) throws  FileNotFoundException, IOException, InterruptedException
    {
        selectFilesFlag = true; 
        showRunFile.clearValues();
        panel.graph.loadMultipleGraphs(panel);  
    }
    
    public void handleSelectParameters(DrawingStageActions panel)
    {
        if(selectFilesFlag == true)
        {
           selectParametersFlag = true; 
           String phase = ((RadioButton)phaseToggle.getSelectedToggle()).getText();
           int n = 0; 
           if(phase.equals(phase1Radio.getText()))   // no limit
               n = 1; 
           else if(phase.equals(phase2Radio.getText()))  // fixed eval solutions
               n = 2;
           else if(phase.equals(phase3Radio.getText()))  // fixed obj fun
               n = 3;
           else if(phase.equals(phase4Radio.getText()))  // fixed time 
               n = 4;
           if (chooseMethod.getValue().toString().equals("Jaya I"))
               panel.drawer = new Jaya(panel, panel.graph, n, false);
           else if (chooseMethod.getValue().toString().equals("Jaya+Grid"))
               panel.drawer = new JayaGrid(panel, panel.graph, n, false);
           else if (chooseMethod.getValue().toString().equals("Hill Climbing"))
               panel.drawer = new HC(panel, panel.graph, n, false);
           else if (chooseMethod.getValue().toString().equals("Simulated Annealing"))
               panel.drawer = new SA(panel, panel.graph, n, false); 
        }
        else {
               Alert alert = new Alert(AlertType.WARNING);
               alert.setContentText("Select Files First");
               alert.show();
             }
    }
    public boolean handleGoButton(DrawingStageActions panel) // throws  FileNotFoundException, IOException, InterruptedException
    {
        if (selectParametersFlag == true)
        {           
           selectFilesFlag = false; 
           selectParametersFlag = false; 
           initialzeAllResults();
           boolean finalFileOpenFlag = false; 
           saveTotalStatBatch(panel, null, 0, finalFileOpenFlag);
           numRuns = Integer.parseInt(numRunsEachCaseText.getText());
           runShowProgress(panel); // run drawer on batch of files and update run file on stage using pausetransition and class showprogress
           return false;
        }
        else
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("Select Parameters First");
            alert.show();
            return true; 
        }
    }    
    
    class ShowRunProgress implements EventHandler<ActionEvent>
    {
        DrawingStageActions panel; 
        PauseTransition pause;
        int files; 
       
        public ShowRunProgress(DrawingStageActions panel, PauseTransition pause)
        { 
            this.panel = panel; 
            this.pause = pause; 
            this.files = 0; 
        }
        public void handle(ActionEvent e)
        {
            if(files < panel.graph.inputFiles.size())
            {
                try {
                   // runprogress run on all files (recursively)
                   runProgress(panel, panel.graph.inputFiles.get(files), files);
                   files++; 
                   panel.clearScreen();
                  } catch (Exception ex) {
                      System.out.println("Error occurred");
                  } 
                  pause.play();
            }
            else {   try {
                // stopping case (base case) when algorithm task is completed
                // saveTotalStatBatch() Method before add to excel
                writeFinalResultstoSheet2();
                } catch (IOException ex) {
                    System.out.println("Problem");
                }
                 panel.clearScreen();
                 showRunFile.setStatus("Done"); 
                
            }
        }
    }
    
    public void runShowProgress (DrawingStageActions draw)
    {
        PauseTransition pause = new PauseTransition (Duration.millis(100));  
        TopRunBatchStage.ShowRunProgress progress = new TopRunBatchStage.ShowRunProgress(draw, pause);  // creating object from the above inner class 
        pause.setOnFinished(progress);
        pause.play(); // first call to handle method in the inner class above 
    }
    
    public void runProgress(DrawingStageActions panel, File file, int i) throws IOException
    {
        initialzeBatchResults();
        Boolean fileOpenFlag = false; 
        writeBatchResults(panel, file, 0, fileOpenFlag);
        for(int run=0; run < numRuns; run++)
        { 
            fileOpenFlag = true; 
            panel.graph.clearGraphRandom();
            panel.getChildren().clear();
            panel.drawer.clearDrawerData(panel.graph, panel.drawer.phaseNumber, panel.initialDrawer);
            panel.graph.loadGraph(panel, true, file);
            panel.drawer.running = true; 
            if (panel.drawer.phaseNumber == 1)
               panel.drawer.run0(panel, panel.graph);
            else if (panel.drawer.phaseNumber == 2)
               panel.drawer.run1(panel, panel.graph);
            else if (panel.drawer.phaseNumber == 3)
               panel.drawer.run2(panel, panel.graph);
            else if (panel.drawer.phaseNumber == 4)
               panel.drawer.run3(panel, panel.graph);
            saveBatchResults(panel, file, run, fileOpenFlag);
            panel.updateResultParametersWindow();  
            saveAsPng(panel, file, run);
            showRunFile.setValues(i+1, file.getName(), panel.graph.inputFiles.size(), "Processing"); 
        }
        writeBatchResultstoSheet1();
        saveTotalStatBatch(panel, panel.graph.inputFiles.get(i), i, true);
    } 
    
    private void initialzeAllResults()
    {
        allObjFunMax = new ArrayList();
        allObjFunMin = new ArrayList();
        allObjFunAvg = new ArrayList();
        allObjFunMedian = new ArrayList();
        allObjFunSD = new ArrayList();
        allEvalSolMax = new ArrayList();
        allEvalSolMin = new ArrayList();
        allEvalSolAvg = new ArrayList();
        allEvalSolMedian = new ArrayList();
        allEvalSolSD = new ArrayList();
        allTimeMax = new ArrayList();
        allTimeMin = new ArrayList();
        allTimeAvg = new ArrayList();
        allTimeMedian = new ArrayList();
        allTimeSD = new ArrayList();
        allNodesOccMax = new ArrayList();
        allNodesOccMin = new ArrayList();
        allNodesOccAvg = new ArrayList();
        allNodesOccMedian = new ArrayList();
        allNodesOccSD = new ArrayList();
        allEdgeLenMax = new ArrayList();
        allEdgeLenMin = new ArrayList();
        allEdgeLenAvg = new ArrayList();
        allEdgeLenMedian = new ArrayList();
        allEdgeLenSD = new ArrayList();
        allCrossingMax = new ArrayList();
        allCrossingMin = new ArrayList();
        allCrossingAvg = new ArrayList();
        allCrossingMedian = new ArrayList();
        allCrossingSD = new ArrayList();
        allNodeEdgeOccMax = new ArrayList();
        allNodeEdgeOccMin = new ArrayList();
        allNodeEdgeOccAvg = new ArrayList();
        allNodeEdgeOccMedian = new ArrayList();
        allNodeEdgeOccSD = new ArrayList();
        allAngularMax = new ArrayList();
        allAngularMin = new ArrayList();
        allAngularAvg = new ArrayList();
        allAngularMedian = new ArrayList();
        allAngularSD = new ArrayList();
    }
    
    private void initialzeBatchResults()
    {
        batchObjFun = new ArrayList(); 
        batchEvalSol = new ArrayList();
        batchTime = new ArrayList();
        batchNodesOcc = new ArrayList();
        batchEdgeLen = new ArrayList();
        batchCrossing = new ArrayList();
        batchNodeEdgeOcc = new ArrayList();
        batchAngular = new ArrayList();
    }
    
    private void saveBatchResults(DrawingStageActions panel, File f, int run, boolean flag)
    {
        batchObjFun.add(panel.drawer.objFunCost.cost);
        batchEvalSol.add((double)(panel.drawer.numEvaluatedSolutions)); 
        batchTime.add(panel.drawer.executionTime / 1000000000.0); 
        batchNodesOcc.add(panel.drawer.objFunCost.normalizedVector.get(0));
        batchEdgeLen.add(panel.drawer.objFunCost.normalizedVector.get(1));
        batchCrossing.add(panel.drawer.objFunCost.normalizedVector.get(2));
        batchNodeEdgeOcc.add(panel.drawer.objFunCost.normalizedVector.get(3));
        batchAngular.add(panel.drawer.objFunCost.normalizedVector.get(4));
        writeBatchResults(panel, f, run, flag);
    }
    
    public File directory; 
    public File outputFile;
    public XSSFWorkbook excelFile;
    public XSSFSheet sheet;
    public Map<Integer, Object[]> batchResults; 
    
    private void writeBatchResults(DrawingStageActions panel, File f, int run, boolean flag)
    { 
        if(flag == false)
        {
           directory = new File(directoryFinal + File.separator + f.getName().substring(0,f.getName().length()-5));
           if (! directory.exists())
           {
              directory.mkdir();
           }
           outputFile = new File("." + File.separator + directory + File.separator + f.getName().substring(0,f.getName().length()-5) + "_Experiment" + ".xlsx");
           excelFile = new XSSFWorkbook();
           sheet = excelFile.createSheet("Experiment");
           batchResults = null;
        }
        else
           batchResults = createRecords(panel, run); 
    }
    
    private Map<Integer, Object[]> createRecords(DrawingStageActions panel, int run)
    {
        if(batchResults == null)
        {
            batchResults = new TreeMap<Integer, Object[]>();
            batchResults.put(1, new Object[]{"run", "nodes", "edges", "objective function", "evaluated solutions", "time", "m1 nodes occlusion", "m2 edge length", "m3 edge crossing", "m4 node edge occlusion", "m5 angular resolution"});
            batchResults.put(2, new Object[]{(Integer)run, (Integer)panel.graph.numVertices, (Integer)panel.graph.numEdges, (Double)panel.drawer.objFunCost.cost, (Integer)panel.drawer.numEvaluatedSolutions, (Double)(panel.drawer.executionTime/1000000000.0),(Double)panel.drawer.objFunCost.normalizedVector.get(0), (Double)panel.drawer.objFunCost.normalizedVector.get(1), (Double)panel.drawer.objFunCost.normalizedVector.get(2), (Double)panel.drawer.objFunCost.normalizedVector.get(3), (Double)panel.drawer.objFunCost.normalizedVector.get(4)});
        }
        batchResults.put(run+2, new Object[]{(Integer)run, (Integer)panel.graph.numVertices, (Integer)panel.graph.numEdges, (Double)panel.drawer.objFunCost.cost, (Integer)panel.drawer.numEvaluatedSolutions, (Double)(panel.drawer.executionTime/1000000000.0),(Double)panel.drawer.objFunCost.normalizedVector.get(0), (Double)panel.drawer.objFunCost.normalizedVector.get(1), (Double)panel.drawer.objFunCost.normalizedVector.get(2), (Double)panel.drawer.objFunCost.normalizedVector.get(3), (Double)panel.drawer.objFunCost.normalizedVector.get(4)});
        return batchResults;
    }
    
    public void writeBatchResultstoSheet1() throws FileNotFoundException, IOException
    {
        Set<Integer> keyid = batchResults.keySet();
        XSSFRow row; 
        int rowid = 0;
        for (Integer key : keyid) 
        {
           row = sheet.createRow(rowid++);
           Object[] objectArr = batchResults.get(key);
           int cellid = 0;
           for (Object obj : objectArr) 
           {
              Cell cell = row.createCell(cellid++);
              if (obj instanceof Integer)
                 cell.setCellValue((Integer)obj);
              else if(obj instanceof Double)
                  cell.setCellValue((Double)obj);
              else cell.setCellValue((String)obj);
           }
        }  
        FileOutputStream fos = new FileOutputStream(outputFile); 
        excelFile.write(fos);
        fos.close();
    } 
    
    public File directoryFinal; 
    public File outputFileFinal;
    public XSSFWorkbook excelFileFinal;
    public XSSFSheet sheetFinal;
    public Map<Integer, Object[]> finalResults; 
    
    public void saveTotalStatBatch(DrawingStageActions panel, File f, int testCase, boolean finalFileOpenFlag)
    {
        if(finalFileOpenFlag == false)
        {
           directoryFinal = new File("Experiments" + File.separator + new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date()) + "_" + panel.drawer.name + "_Phase" + panel.drawer.phaseNumber);
           if (! directoryFinal.exists())
           {
              directoryFinal.mkdir();
           }
           outputFileFinal = new File("." + File.separator + directoryFinal + File.separator + "FinalResultsExperiment" + ".xlsx");
           excelFileFinal = new XSSFWorkbook();
           sheetFinal = excelFileFinal.createSheet("Final Results Experiment");
           finalResults = null;
        }
        else
           finalResults = createFinalRecords(panel, f, testCase);
    }
    
    private Map<Integer, Object[]> createFinalRecords(DrawingStageActions panel, File f, int testCase)
    {        
        Double max, min, avg1, median1, sd1; 
        max = Collections.max(batchObjFun); 
        min = Collections.min(batchObjFun);
        avg1 = avg(batchObjFun);
        median1 = median(batchObjFun);
        sd1 = sd(batchObjFun);
        allObjFunMax.add(max);
        allObjFunMin.add(min);
        allObjFunAvg.add(avg1);
        allObjFunMedian.add(median1);
        allObjFunSD.add(sd1);
        
        max = Collections.max(batchEvalSol); 
        min = Collections.min(batchEvalSol);
        avg1 = avg(batchEvalSol);
        median1 = median(batchEvalSol);
        sd1 = sd(batchEvalSol);
        allEvalSolMax.add(max);
        allEvalSolMin.add(min);
        allEvalSolAvg.add(avg1);
        allEvalSolMedian.add(median1);
        allEvalSolSD.add(sd1);
        
        max = Collections.max(batchTime); 
        min = Collections.min(batchTime);
        avg1 = avg(batchTime);
        median1 = median(batchTime);
        sd1 = sd(batchTime);
        allTimeMax.add(max);
        allTimeMin.add(min);
        allTimeAvg.add(avg1);
        allTimeMedian.add(median1);
        allTimeSD.add(sd1);
        
        max = Collections.max(batchNodesOcc); 
        min = Collections.min(batchNodesOcc);
        avg1 = avg(batchNodesOcc);
        median1 = median(batchNodesOcc);
        sd1 = sd(batchNodesOcc);
        allNodesOccMax.add(max);
        allNodesOccMin.add(min);
        allNodesOccAvg.add(avg1);
        allNodesOccMedian.add(median1);
        allNodesOccSD.add(sd1);
        
        max = Collections.max(batchEdgeLen); 
        min = Collections.min(batchEdgeLen);
        avg1 = avg(batchEdgeLen);
        median1 = median(batchEdgeLen);
        sd1 = sd(batchEdgeLen);
        allEdgeLenMax.add(max);
        allEdgeLenMin.add(min);
        allEdgeLenAvg.add(avg1);
        allEdgeLenMedian.add(median1);
        allEdgeLenSD.add(sd1);
        
        max = Collections.max(batchCrossing); 
        min = Collections.min(batchCrossing);
        avg1 = avg(batchCrossing);
        median1 = median(batchCrossing);
        sd1 = sd(batchCrossing);
        allCrossingMax.add(max);
        allCrossingMin.add(min);
        allCrossingAvg.add(avg1);
        allCrossingMedian.add(median1);
        allCrossingSD.add(sd1);
        
        max = Collections.max(batchNodeEdgeOcc); 
        min = Collections.min(batchNodeEdgeOcc);
        avg1 = avg(batchNodeEdgeOcc);
        median1 = median(batchNodeEdgeOcc);
        sd1 = sd(batchNodeEdgeOcc);
        allNodeEdgeOccMax.add(max);
        allNodeEdgeOccMin.add(min);
        allNodeEdgeOccAvg.add(avg1);
        allNodeEdgeOccMedian.add(median1);
        allNodeEdgeOccSD.add(sd1);
        
        max = Collections.max(batchAngular); 
        min = Collections.min(batchAngular);
        avg1 = avg(batchAngular);
        median1 = median(batchAngular);
        sd1 = sd(batchAngular);
        allAngularMax.add(max);
        allAngularMin.add(min);
        allAngularAvg.add(avg1);
        allAngularMedian.add(median1);
        allAngularSD.add(sd1);
        
        if(finalResults == null)
        {
            finalResults = new TreeMap<Integer, Object[]>();
            finalResults.put(1, new Object[]{"file name", "runs", "nodes", "edges", "objective function (max)", "objective function (min)", "objective function (avg)", "objective function (median)", "objective function (SD)",  "evaluated solutions (max)", "evaluated solutions (min)", "evaluated solutions (avg)", "evaluated solutions (median)", "evaluated solutions (SD)", "time (max)", "time (min)", "time (avg)", "time (median)", "time (SD)", "m1 nodes occlusion (max)", "m1 nodes occlusion (min)", "m1 nodes occlusion (avg)", "m1 nodes occlusion (median)", "m1 nodes occlusion (SD)",  "m2 edge length (max)", "m2 edge length (min)", "m2 edge length (avg)", "m2 edge length (median)", "m2 edge length (SD)", "m3 edge crossing (max)", "m3 edge crossing (min)", "m3 edge crossing (avg)", "m3 edge crossing (median)", "m3 edge crossing (SD)", "m4 node edge occlusion (max)", "m4 node edge occlusion (min)", "m4 node edge occlusion (avg)", "m4 node edge occlusion (median)", "m4 node edge occlusion (SD)", "m5 angular resolution (max)", "m5 angular resolution (min)", "m5 angular resolution (avg)", "m5 angular resolution (median)", "m5 angular resolution (SD)"}); 
        }
        finalResults.put(testCase+2, new Object[]{f.getName(), (Integer)numRuns, (Integer)panel.graph.numVertices, (Integer)panel.graph.numEdges, (Double)allObjFunMax.get(testCase), (Double)allObjFunMin.get(testCase), (Double)allObjFunAvg.get(testCase), (Double)allObjFunMedian.get(testCase), (Double)allObjFunSD.get(testCase), (Double)allEvalSolMax.get(testCase), (Double)allEvalSolMin.get(testCase), (Double)allEvalSolAvg.get(testCase), (Double)allEvalSolMedian.get(testCase), (Double)allEvalSolSD.get(testCase), (Double)allTimeMax.get(testCase),(Double)allTimeMin.get(testCase), (Double)allTimeAvg.get(testCase), (Double)allTimeMedian.get(testCase), (Double)allTimeSD.get(testCase), (Double)allNodesOccMax.get(testCase), (Double)allNodesOccMin.get(testCase), (Double)allNodesOccAvg.get(testCase), (Double)allNodesOccMedian.get(testCase), (Double)allNodesOccSD.get(testCase), (Double)allEdgeLenMax.get(testCase), (Double)allEdgeLenMin.get(testCase), (Double)allEdgeLenAvg.get(testCase), (Double)allEdgeLenMedian.get(testCase), (Double)allEdgeLenSD.get(testCase), (Double)allCrossingMax.get(testCase), (Double)allCrossingMin.get(testCase), (Double)allCrossingAvg.get(testCase), (Double)allCrossingMedian.get(testCase), (Double)allCrossingSD.get(testCase), (Double)allNodeEdgeOccMax.get(testCase), (Double)allNodeEdgeOccMin.get(testCase), (Double)allNodeEdgeOccAvg.get(testCase), (Double)allNodeEdgeOccMedian.get(testCase), (Double)allNodeEdgeOccSD.get(testCase), (Double)allAngularMax.get(testCase), (Double)allAngularMin.get(testCase), (Double)allAngularAvg.get(testCase), (Double)allAngularMedian.get(testCase), (Double)allAngularSD.get(testCase)});
        return finalResults;
    }
    
    public void writeFinalResultstoSheet2() throws FileNotFoundException, IOException
    {
        Set<Integer> keyid = finalResults.keySet();
        XSSFRow row; 
        int rowid = 0;
        for (Integer key : keyid) 
        {
           row = sheetFinal.createRow(rowid++);
           Object[] objectArr = finalResults.get(key);
           int cellid = 0;
           for (Object obj : objectArr) 
           {
              Cell cell = row.createCell(cellid++);
              if (obj instanceof Integer)
                 cell.setCellValue((Integer)obj);
              else if(obj instanceof Double)
                  cell.setCellValue((Double)obj);
              else cell.setCellValue((String)obj);
           }
        }  
        FileOutputStream fos = new FileOutputStream(outputFileFinal); 
        excelFileFinal.write(fos);
        fos.close();
    }
    
    private void writeFinalStat()
    {
        System.out.println(Collections.max(allObjFunMax));
        System.out.println(Collections.min(allObjFunMin));
        System.out.println(avg(allObjFunAvg));
        System.out.println(median(allObjFunMedian));
        System.out.println(sd(allObjFunSD));
        
        System.out.println(Collections.max(allEvalSolMax));
        System.out.println(Collections.min(allEvalSolMin));
        System.out.println(avg(allEvalSolAvg));
        System.out.println(median(allEvalSolMedian));
        System.out.println(sd(allEvalSolSD));
        
        System.out.println(Collections.max(allTimeMax));
        System.out.println(Collections.min(allTimeMin));
        System.out.println(avg(allTimeAvg));
        System.out.println(median(allTimeMedian));
        System.out.println(sd(allTimeSD));
        
        System.out.println(Collections.max(allNodesOccMax));
        System.out.println(Collections.min(allNodesOccMin));
        System.out.println(avg(allNodesOccAvg));
        System.out.println(median(allNodesOccMedian));
        System.out.println(sd(allNodesOccSD));
        
        System.out.println(Collections.max(allEdgeLenMax));
        System.out.println(Collections.min(allEdgeLenMin));
        System.out.println(avg(allEdgeLenAvg));
        System.out.println(median(allEdgeLenMedian));
        System.out.println(sd(allEdgeLenSD));
        
        System.out.println(Collections.max(allCrossingMax));
        System.out.println(Collections.min(allCrossingMin));
        System.out.println(avg(allCrossingAvg));
        System.out.println(median(allCrossingMedian));
        System.out.println(sd(allCrossingSD));
        
        System.out.println(Collections.max(allNodeEdgeOccMax));
        System.out.println(Collections.min(allNodeEdgeOccMin));
        System.out.println(avg(allNodeEdgeOccAvg));
        System.out.println(median(allNodeEdgeOccMedian));
        System.out.println(sd(allNodeEdgeOccSD));
        
        System.out.println(Collections.max(allAngularMax));
        System.out.println(Collections.min(allAngularMin));
        System.out.println(avg(allAngularAvg));
        System.out.println(median(allAngularMedian));
        System.out.println(sd(allAngularSD));       
    }
    
    private Double avg(ArrayList<Double> A)
    {
        double sum = 0.0; 
        for(int i=0; i<A.size(); i++)
            sum += A.get(i);
        return sum / A.size();
    }
    
    private Double median(ArrayList<Double> A)
    {
        Collections.sort(A);
        double median;
        if (A.size() % 2 == 0)
           median = ((A.get((A.size()/2)) + A.get((A.size()/2 - 1)))/2);
        else
           median = A.get(A.size()/2); 
        return median;
    }
    
    private Double sd(ArrayList<Double> A)
    {
        double avg = avg(A);
        double total = 0.0; 
        for(int i=0; i<A.size(); i++)
        {
            double dist = Math.pow((A.get(i) - avg),2);
            total += dist; 
        }
        double standardDev = Math.sqrt(total / A.size());
        return standardDev;
    }
    
    public void saveAsPng(DrawingStageActions panel, File f, int i) {
        WritableImage image = panel.snapshot(new SnapshotParameters(), null);
	File file = new File("." + File.separator + directory + File.separator + f.getName().substring(0,f.getName().length()-5) + "_" + i + ".png");
    try {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    } catch (IOException e) {
    }
}
}
