package MGDrawVis;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class TopBar extends HBox 
{
    public static ColorPicker palette; 
    public Button drawNode; 
    public Button drawEdge;
    public Button moveNode; 
    public Button loadGraph; 
    public Button saveGraph; 
    public Button clearScreen; 
    public Button randomizeGraph;
    public Button generateRandom; 
    public Button runMultiple; 
    public static int prefHeight = 30;
    public static int prefWidth = 120;
    
    public TopBar()
    {
        super.setPrefWidth(DrawingStage.screenWidth);
        super.setPrefHeight(prefHeight);
        super.setAlignment(Pos.CENTER);
        super.setPadding(new Insets(5,5,5,5));
        super.setSpacing(10);
        palette = new ColorPicker();
        palette.setValue(Color.RED);
        palette.setPrefWidth(50);
        palette.setPrefHeight(30);
        drawNode = new Button("Add Nodes");
        drawNode.setPrefWidth(prefWidth);
        drawNode.setPrefHeight(prefHeight);
        drawNode.setTooltip(new Tooltip("Point the cursor to a location and press the mouse left button to draw a node"));
        drawEdge = new Button("Add Edges");
        drawEdge.setPrefWidth(prefWidth);
        drawEdge.setPrefHeight(prefHeight);
        drawEdge.setTooltip(new Tooltip("Click on a node and drag the mouse to connect to another node"));
        moveNode = new Button("Move Nodes"); 
        moveNode.setTooltip(new Tooltip("Drag and move nodes"));
        moveNode.setPrefWidth(prefWidth);
        moveNode.setPrefHeight(prefHeight);
        loadGraph = new Button("Import Graph"); 
        loadGraph.setTooltip(new Tooltip("Import a graph data Excel file"));
        loadGraph.setPrefWidth(prefWidth);
        loadGraph.setPrefHeight(prefHeight);
        saveGraph = new Button("Save Graph"); 
        saveGraph.setTooltip(new Tooltip("Save current graph in Excel file"));
        saveGraph.setPrefWidth(prefWidth);
        saveGraph.setPrefHeight(prefHeight);
        clearScreen = new Button("Clear Screen"); 
        clearScreen.setTooltip(new Tooltip("Clear the screen"));
        clearScreen.setPrefWidth(prefWidth);
        clearScreen.setPrefHeight(prefHeight);
        randomizeGraph = new Button("Randomize"); 
        randomizeGraph.setTooltip(new Tooltip("Randomize the current layout"));
        randomizeGraph.setPrefWidth(prefWidth);
        randomizeGraph.setPrefHeight(prefHeight);
        generateRandom = new Button("Random graph"); 
        generateRandom.setTooltip(new Tooltip("Generate a random graph given number of nodes and edges"));
        generateRandom.setPrefWidth(prefWidth);
        generateRandom.setPrefHeight(prefHeight);
        runMultiple = new Button("Run Experiment"); 
        runMultiple.setTooltip(new Tooltip("Run a selected drawing algorithm on multiple files"));
        runMultiple.setPrefWidth(prefWidth);
        runMultiple.setPrefHeight(prefHeight);
        this.getChildren().add(palette);
        this.getChildren().add(drawNode);
        this.getChildren().add(drawEdge);
        this.getChildren().add(moveNode);
        this.getChildren().add(loadGraph);
        this.getChildren().add(saveGraph);
        this.getChildren().add(clearScreen);
        this.getChildren().add(randomizeGraph);
        this.getChildren().add(generateRandom);
        this.getChildren().add(runMultiple);
    }    
}
