package MGDrawVis;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LeftSAParametersStage extends Stage{
    
    public GridPane layout; 
    public GridPane wrapperPane; 
    public Label squareSizeLabel; 
    public TextField squareSizeText; 
    public Label maxIterationLabel; 
    public TextField maxIterationText; 
    public Label iterationPerTempLabel; 
    public TextField iterationPerTempText; 
    public Label initialTempLabel; 
    public TextField initialTempText; 
    public Label coolDownRateLabel; 
    public TextField coolDownRateText; 
    public ToggleGroup animatedSelection; 
    public RadioButton animatedRadio; 
    public RadioButton notAnimatedRadio; 
    public Button callSA;  
    public Scene scene; 
    public int squareSize,maxIteration, iterationPerTemp; 
    public double coolDownRate, initialTemp; 
    
    public LeftSAParametersStage(boolean enabled)
    {
        wrapperPane = new GridPane(); 
        wrapperPane.setPadding(new Insets(10,10,10,10));
        layout = new GridPane(); 
        layout.setPadding(new Insets(20,20,20,20));
        squareSizeLabel = new Label("Square Size: ");
        squareSizeLabel.setPrefWidth(160);
        squareSizeLabel.setWrapText(true);
        squareSizeText = new TextField("512");
        squareSizeText.setPrefWidth(90);
        maxIterationLabel = new Label("Max Iterations: ");
        maxIterationLabel.setPrefWidth(160);
        maxIterationLabel.setWrapText(true);
        maxIterationText = new TextField("50");
        maxIterationText.setPrefWidth(90);
        iterationPerTempLabel = new Label("Iteration Per Temperature: ");
        iterationPerTempLabel.setPrefWidth(160);
        iterationPerTempLabel.setWrapText(true);
        iterationPerTempText = new TextField("15");
        iterationPerTempText.setPrefWidth(90);
        initialTempLabel = new Label("Initial Temperature: ");
        initialTempLabel.setPrefWidth(160);
        initialTempLabel.setWrapText(true);
        initialTempText = new TextField("0.65");
        initialTempText.setPrefWidth(90);
        coolDownRateLabel = new Label("Cool Down Rate: ");
        coolDownRateLabel.setPrefWidth(160);
        coolDownRateLabel.setWrapText(true);
        coolDownRateText = new TextField("0.7");
        coolDownRateText.setPrefWidth(90);
        animatedSelection = new ToggleGroup(); 
        animatedRadio = new RadioButton("Animated"); 
        if(enabled)
            animatedRadio.setDisable(false);
        else
            animatedRadio.setDisable(true);
        animatedRadio.setToggleGroup(animatedSelection);
        animatedRadio.setWrapText(true);
        notAnimatedRadio = new RadioButton("Not Animated");
        notAnimatedRadio.setToggleGroup(animatedSelection);
        notAnimatedRadio.setSelected(true);
        notAnimatedRadio.setWrapText(true);
        callSA = new Button("GO"); 
        wrapperPane.add(squareSizeLabel, 0,0); 
        wrapperPane.add(squareSizeText, 1,0); 
        wrapperPane.add(maxIterationLabel, 0,1); 
        wrapperPane.add(maxIterationText, 1,1); 
        wrapperPane.add(iterationPerTempLabel, 0,2); 
        wrapperPane.add(iterationPerTempText, 1,2);
        wrapperPane.add(initialTempLabel, 0,3); 
        wrapperPane.add(initialTempText, 1,3);
        wrapperPane.add(coolDownRateLabel, 0,4); 
        wrapperPane.add(coolDownRateText, 1,4);
        layout.add(wrapperPane, 0, 0);
        layout.add(animatedRadio, 0,1); 
        layout.add(notAnimatedRadio, 0,2); 
        layout.add(callSA, 0,3);
        scene = new Scene(layout,300,300);
        this.setScene(scene);
        this.show(); 
    }
    
    public boolean handleGoButton()
    {
        squareSize = Integer.parseInt(squareSizeText.getText());
        maxIteration = Integer.parseInt(maxIterationText.getText());
        iterationPerTemp = Integer.parseInt(iterationPerTempText.getText());
        initialTemp = Double.parseDouble(initialTempText.getText());
        coolDownRate = Double.parseDouble(coolDownRateText.getText());
        if (squareSize > 0 && maxIteration > 0 && iterationPerTemp > 0 && initialTemp >= 0 && coolDownRate >= 0 )
        {
            this.close();
            return true;
        }
        else 
            return false; 
    }
}
