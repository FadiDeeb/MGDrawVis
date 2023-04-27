package MGDrawVis;

import javafx.geometry.Insets; 
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class RightBar extends VBox 
{
    public GridPane container1; 
    public GridPane container2; 
    public GridPane container3; 
    public GridPane container4; 
    public Label cont1NodesLabel; 
    public Label cont1NodesValue; 
    public Label cont1EdgesLabel; 
    public Label cont1EdgesValue; 
    public Label cont2NodeOcclusion; 
    public TextField cont2NodeOcclusionWeight; 
    public Label cont2EdgeLength; 
    public TextField cont2EdgeLengthWeight; 
    public Label cont2EdgeCrossings; 
    public TextField cont2EdgeCrossingsWeight;
    public Label cont2NodeEdgeOcclusion; 
    public TextField cont2NodeEdgeOcclusionWeight; 
    public Label cont2AngularResolution; 
    public TextField cont2AngularResolutionWeight;
    public Label cont2ScreenCenter; 
    public TextField cont2ScreenCenterWeight; 
    public Label cont3DesiredNodeOcclusion; 
    public TextField cont3DesiredNodeOcclusionValue; 
    public Label cont3DesiredEdgeLength; 
    public TextField cont3DesiredEdgeLengthValue; 
    public Label cont3DesiredNodeEdgeOcclusion; 
    public TextField cont3DesiredNodeEdgeOcclusionValue;
    public Label cont3DesiredAngularResolution; 
    public TextField cont3DesiredAngularResolutionValue;
    public Label cont4NodeOcclusion; 
    public Label cont4NodeOcclusionResult; 
    public Label cont4EdgeLength; 
    public Label cont4EdgeLengthResult; 
    public Label cont4EdgeCrossings; 
    public Label cont4EdgeCrossingsResult;
    public Label cont4NodeEdgeOcclusion; 
    public Label cont4NodeEdgeOcclusionResult; 
    public Label cont4AngularResolution; 
    public Label cont4AngularResolutionResult;
    public Label cont4ScreenCenter; 
    public Label cont4ScreenCenterResult;
    public Label cont4ObjectiveFunction; 
    public Label cont4ObjectiveFunctionResult; 
    public Label cont4EvaluatedSolutions; 
    public Label cont4EvaluatedSolutionsResult; 
    public Label cont4ExecutionTime; 
    public Label cont4ExecutionTimeResult; 
    public Label cont4DrawerStatusLabel; 
    public Label cont4DrawerStatus; 
    
    public static int prefHeight = 30;
    public static int prefWidth = 200;
    public static int textFieldPreWidth = 50;
    
    public RightBar()
    {
        super.setPrefHeight(DrawingStage.screenHeight);
        super.setPrefWidth(prefWidth);
        super.setAlignment(Pos.TOP_LEFT);
        super.setSpacing(30);
        super.setPadding(new Insets(5,5,5,5));
        container1Method(); 
        container2Method(); 
        container3Method(); 
        container4Method();
        Label weightsLabel = new Label("Metrics Weights");
        weightsLabel.setFont(Font.font("Verdana",14));
        weightsLabel.setAlignment(Pos.CENTER);
        Label desiredLabel = new Label("Desired Distances");
        desiredLabel.setFont(Font.font("Verdana",14));
        desiredLabel.setAlignment(Pos.CENTER);
        Label resultsLabel = new Label("Results");
        resultsLabel.setFont(Font.font("Verdana",14));
        resultsLabel.setAlignment(Pos.CENTER);
        this.getChildren().add(container1);
        this.getChildren().add(weightsLabel);
        this.getChildren().add(container2);
        this.getChildren().add(desiredLabel);
        this.getChildren().add(container3);
        this.getChildren().add(resultsLabel);
        this.getChildren().add(container4);
    }    
    
    public void container1Method()
    {
        container1 = new GridPane(); 
        container1.setPrefWidth(prefWidth);
        container1.setAlignment(Pos.TOP_LEFT);
        container1.setPadding(new Insets(3,3,3,3));
        cont1NodesLabel = new Label("Nodes: ");
        cont1NodesValue = new Label("0");
        cont1EdgesLabel = new Label("Edges: ");
        cont1EdgesValue = new Label("0");
        container1.add(cont1NodesLabel,0,0);
        container1.add(cont1NodesValue,1,0); 
        container1.add(cont1EdgesLabel,0,1);
        container1.add(cont1EdgesValue,1,1); 
    }
    
    public void container2Method()
    {
        container2 = new GridPane(); 
        container2.setPrefWidth(prefWidth);
        container2.setAlignment(Pos.TOP_LEFT);
        container2.setPadding(new Insets(3,3,3,3));
        cont2NodeOcclusion = new Label("Nodes Occlusion: ");
        cont2NodeOcclusionWeight = new TextField("1"); 
        cont2NodeOcclusionWeight.setPrefWidth(textFieldPreWidth);
        cont2EdgeLength = new Label("Edge Length: ");
        cont2EdgeLengthWeight = new TextField("1"); 
        cont2EdgeLengthWeight.setPrefWidth(textFieldPreWidth);
        cont2EdgeCrossings = new Label("Edge Crossings: "); 
        cont2EdgeCrossingsWeight = new TextField("1");
        cont2EdgeCrossingsWeight.setPrefWidth(textFieldPreWidth);
        cont2NodeEdgeOcclusion = new Label("Node Edge Occlusion: "); 
        cont2NodeEdgeOcclusionWeight = new TextField("0");
        cont2NodeEdgeOcclusionWeight.setPrefWidth(textFieldPreWidth);
        cont2AngularResolution = new Label("Angular Resolution: ");
        cont2AngularResolutionWeight = new TextField("1");
        cont2AngularResolutionWeight.setPrefWidth(textFieldPreWidth);
        cont2ScreenCenter = new Label("Screen Center: ");
        cont2ScreenCenterWeight = new TextField("1");
        cont2ScreenCenterWeight.setPrefWidth(textFieldPreWidth);
        container2.add(cont2NodeOcclusion,0,0);
        container2.add(cont2NodeOcclusionWeight,1,0);
        container2.add(cont2EdgeLength,0,1);
        container2.add(cont2EdgeLengthWeight,1,1);
        container2.add(cont2EdgeCrossings,0,2);
        container2.add(cont2EdgeCrossingsWeight,1,2);
        container2.add(cont2NodeEdgeOcclusion,0,3);
        container2.add(cont2NodeEdgeOcclusionWeight,1,3);
        container2.add(cont2AngularResolution,0,4);
        container2.add(cont2AngularResolutionWeight,1,4);
        container2.add(cont2ScreenCenter,0,5);
        container2.add(cont2ScreenCenterWeight,1,5);
    }
    
    public void container3Method()
    {
        container3 = new GridPane(); 
        container3.setPrefWidth(prefWidth);
        container3.setAlignment(Pos.TOP_LEFT);
        container3.setPadding(new Insets(3,3,3,3));
        cont3DesiredNodeOcclusion = new Label("Nodes Occlusion: ");
        cont3DesiredNodeOcclusionValue = new TextField("100"); 
        cont3DesiredNodeOcclusionValue.setPrefWidth(textFieldPreWidth);
        cont3DesiredEdgeLength = new Label("Edge Length: "); 
        cont3DesiredEdgeLengthValue = new TextField("100");
        cont3DesiredEdgeLengthValue.setPrefWidth(textFieldPreWidth);
        cont3DesiredNodeEdgeOcclusion = new Label("Node Edge Occlusion: ");
        cont3DesiredNodeEdgeOcclusionValue = new TextField("30");
        cont3DesiredNodeEdgeOcclusionValue.setPrefWidth(textFieldPreWidth);
        cont3DesiredAngularResolution = new Label("Angular Resolution: ");
        cont3DesiredAngularResolutionValue = new TextField("25");
        cont3DesiredAngularResolutionValue.setPrefWidth(textFieldPreWidth);
        container3.add(cont3DesiredNodeOcclusion,0,0);
        container3.add(cont3DesiredNodeOcclusionValue,1,0);
        container3.add(cont3DesiredEdgeLength,0,1);
        container3.add(cont3DesiredEdgeLengthValue,1,1);
        container3.add(cont3DesiredNodeEdgeOcclusion,0,2);
        container3.add(cont3DesiredNodeEdgeOcclusionValue,1,2);
        container3.add(cont3DesiredAngularResolution,0,3);
        container3.add(cont3DesiredAngularResolutionValue,1,3);
    }
    
    public void container4Method()
    {
        container4 = new GridPane(); 
        container4.setPrefWidth(prefWidth);
        container4.setAlignment(Pos.TOP_LEFT);
        container4.setPadding(new Insets(3,3,3,3));
        cont4NodeOcclusion = new Label("Nodes Occlusion: "); 
        cont4NodeOcclusionResult = new Label("0"); 
        cont4EdgeLength = new Label("Edge Length: "); 
        cont4EdgeLengthResult = new Label("0"); 
        cont4EdgeCrossings = new Label("Edge Crossings: ");
        cont4EdgeCrossingsResult = new Label("0");
        cont4NodeEdgeOcclusion = new Label("Node Edge Occlusion: "); 
        cont4NodeEdgeOcclusionResult = new Label("0");
        cont4AngularResolution = new Label("Angular Resolution: ");
        cont4AngularResolutionResult = new Label("0");
        cont4ScreenCenter = new Label("Screen Center: ");
        cont4ScreenCenterResult = new Label("0");
        cont4ObjectiveFunction = new Label("Objective Function: "); 
        cont4ObjectiveFunction.setWrapText(true);
        cont4ObjectiveFunctionResult = new Label("0.0"); 
        cont4ObjectiveFunction.setTextFill(Color.RED);
        cont4ObjectiveFunctionResult.setTextFill(Color.RED);
        cont4EvaluatedSolutions = new Label("Evaluated Solutions: ");
        cont4EvaluatedSolutions.setWrapText(true);
        cont4EvaluatedSolutionsResult = new Label("0"); 
        cont4EvaluatedSolutions.setTextFill(Color.RED);
        cont4EvaluatedSolutionsResult.setTextFill(Color.RED);
        cont4ExecutionTime = new Label("Time (seconds): "); 
        cont4ExecutionTime.setWrapText(true);
        cont4ExecutionTimeResult = new Label("0"); 
        cont4ExecutionTime.setTextFill(Color.RED);
        cont4ExecutionTimeResult.setTextFill(Color.RED);
        cont4DrawerStatusLabel = new Label("Draw Status: "); 
        cont4DrawerStatusLabel.setWrapText(true);
        cont4DrawerStatus = new Label("Idle"); 
        cont4DrawerStatusLabel.setTextFill(Color.BLUE);
        cont4DrawerStatus.setTextFill(Color.BLUE);
        container4.add(cont4NodeOcclusion,0,0);
        container4.add(cont4NodeOcclusionResult,1,0);
        container4.add(cont4EdgeLength,0,1);
        container4.add(cont4EdgeLengthResult,1,1);
        container4.add(cont4EdgeCrossings,0,2);
        container4.add(cont4EdgeCrossingsResult,1,2);
        container4.add(cont4NodeEdgeOcclusion,0,3);
        container4.add(cont4NodeEdgeOcclusionResult,1,3);
        container4.add(cont4AngularResolution,0,4);
        container4.add(cont4AngularResolutionResult,1,4);
        container4.add(cont4ScreenCenter,0,5);
        container4.add(cont4ScreenCenterResult,1,5);
        container4.add(cont4ObjectiveFunction,0,6);
        container4.add(cont4ObjectiveFunctionResult,1,6);
        container4.add(cont4EvaluatedSolutions,0,7);
        container4.add(cont4EvaluatedSolutionsResult,1,7);
        container4.add(cont4ExecutionTime,0,8);
        container4.add(cont4ExecutionTimeResult,1,8);
        container4.add(cont4DrawerStatusLabel,0,9);
        container4.add(cont4DrawerStatus,1,9);
    }
}
