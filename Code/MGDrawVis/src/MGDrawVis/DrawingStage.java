package MGDrawVis;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DrawingStage extends Stage
{
    public static int screenWidth = (int) (Screen.getPrimary().getVisualBounds().getMaxX()*2);
    public static int screenHeight = (int) (Screen.getPrimary().getVisualBounds().getMaxY()*2);
    public static int stageWidth = (int) (Screen.getPrimary().getVisualBounds().getMaxX());
    public static int stageHeight = (int) (Screen.getPrimary().getVisualBounds().getMaxY());
    public static Scene scene1; 
    public static Stage drawingStage; 
    public static BorderPane screen;
    public static TopBar topbar; 
    public static LeftBar leftbar; 
    public static RightBar rightbar; 
    public static DrawingStageActions panel;
    public static ZoomableScrollPane scrollBar;
    
    // the starting stage of the application 
    public DrawingStage() throws IOException
    {
        setMaximized(true);
        screen = new BorderPane();
        topbar = new TopBar();
        leftbar = new LeftBar(); 
        rightbar = new RightBar();
        panel = new DrawingStageActions(); // create the draw panel component
        panel.setPrefSize(screenWidth, screenHeight); 
        panel.setStyle("-fx-backgraound-color:white;");
        scrollBar = new ZoomableScrollPane (panel); 
        screen.setTop(topbar);
        screen.setLeft(leftbar);
        screen.setRight(rightbar);
        screen.setCenter(scrollBar);  
        topBarActionEvents();   // add events to all the top bar buttons 
        LeftBarActionEvents();  // add events to all the left bar buttons 
        this.setTitle("MGDrawVis - MetaHeuristics Graph Drawing Visualizer"); 
        this.sizeToScene();
        scene1 = new Scene(screen, stageWidth,stageHeight);
        this.setScene(scene1); 
        this.show();  
    }
    
    public static void topBarActionEvents()
    {
        topbar.drawNode.setOnAction(e -> panel.drawNodes());  // draw nodes 
        TopBar.palette.setOnAction(e -> panel.addToPanel());
        topbar.drawEdge.setOnAction(e -> panel.drawEdges());  // add edges 
        topbar.moveNode.setOnAction(e -> panel.moveNodes());  // move nodes 
        topbar.clearScreen.setOnAction(e -> panel.clearScreen()); // clear screen 
        topbar.loadGraph.setOnAction(e -> {     // load a graph from excel file 
            try {
                panel.loadGraph();
            } catch (IOException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("File Loading Failed");
                a.showAndWait();
            }
        });
        topbar.saveGraph.setOnAction(e -> {          // save a graph to excel file 
            try {
                panel.saveGraph();
            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("File Saving Failed");
                a.showAndWait();
            }
        });  
        topbar.randomizeGraph.setOnAction(e -> panel.randomizeGraph()); // randomize the current graph 
        topbar.generateRandom.setOnAction(e -> panel.generateRandomGraph()); // generate a random graph
        topbar.runMultiple.setOnAction(e -> {
            try {
                panel.runOnMultipleFiles();
            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("File Loading Failed");
                a.showAndWait();
            }
        }); 
    }
    
    public static void LeftBarActionEvents()
    {
        leftbar.hc.setOnAction(e -> panel.HillClimbing());  // run hill climbing 
        leftbar.jaya1.setOnAction(e -> panel.Jaya1());  // run jaya 1
        leftbar.sa.setOnAction(e -> panel.SimulatedAnnealing()); // run simulated annealing
        leftbar.ga.setOnAction(e -> panel.JayaGrid()); // run simulated annealing
    }
    
}
