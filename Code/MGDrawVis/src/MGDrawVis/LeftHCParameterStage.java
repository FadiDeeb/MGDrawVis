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

public class LeftHCParameterStage extends Stage{
    
    public GridPane layout; 
    public GridPane wrapperPane; 
    public Label squareSizeLabel; 
    public TextField squareSizeText; 
    public Label squareReductionLabel; 
    public TextField squareReductionText; 
    public ToggleGroup animatedSelection; 
    public RadioButton animatedRadio; 
    public RadioButton notAnimatedRadio; 
    public Button callHC;  
    public Scene scene; 
    public int squareSize,squareReduction; 
    
    public LeftHCParameterStage(boolean enabled)
    {
        wrapperPane = new GridPane(); 
        wrapperPane.setPadding(new Insets(10,10,10,10));
        layout = new GridPane(); 
        layout.setPadding(new Insets(20,20,20,20));
        squareSizeLabel = new Label("Square Size: ");
        squareSizeLabel.setPrefWidth(140);
        squareSizeLabel.setWrapText(true);
        squareSizeText = new TextField("1024");
        squareSizeText.setPrefWidth(90);
        squareReductionLabel = new Label("Square Reduction: ");
        squareReductionLabel.setPrefWidth(140);
        squareReductionLabel.setWrapText(true);
        squareReductionText = new TextField("6");
        squareReductionText.setPrefWidth(90);
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
        callHC = new Button("GO"); 
        wrapperPane.add(squareSizeLabel, 0,0); 
        wrapperPane.add(squareSizeText, 1,0); 
        wrapperPane.add(squareReductionLabel, 0,1); 
        wrapperPane.add(squareReductionText, 1,1); 
        layout.add(wrapperPane, 0, 0);
        layout.add(animatedRadio, 0,1); 
        layout.add(notAnimatedRadio, 0,2); 
        layout.add(callHC, 0,3);
        scene = new Scene(layout,300,300);
        this.setScene(scene);
        this.show(); 
    }
    
    public boolean handleGoButton()
    {
        squareSize = Integer.parseInt(squareSizeText.getText());
        squareReduction = Integer.parseInt(squareReductionText.getText());
        if (squareSize > 0 && squareReduction > 0)
        {
            this.close();
            return true;
        }
        else 
            return false; 
    }
    
}
