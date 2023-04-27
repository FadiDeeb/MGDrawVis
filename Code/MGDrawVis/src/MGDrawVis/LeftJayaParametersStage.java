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

public class LeftJayaParametersStage extends Stage{
    
    public GridPane layout; 
    public GridPane wrapperPane; 
    public Label populationSizeLabel; 
    public TextField populationSizeText; 
    public Label iterationsLabel; 
    public TextField iterationsText; 
    public ToggleGroup animatedSelection; 
    public RadioButton animatedRadio; 
    public RadioButton notAnimatedRadio; 
    public Button callJaya;  
    public Scene scene; 
    public long populationSize,iterations; 
    
    public LeftJayaParametersStage(boolean enabled)
    {
        wrapperPane = new GridPane(); 
        wrapperPane.setPadding(new Insets(10,10,10,10));
        layout = new GridPane(); 
        layout.setPadding(new Insets(20,20,20,20));
        populationSizeLabel = new Label("Population Size: ");
        populationSizeLabel.setPrefWidth(140);
        populationSizeLabel.setWrapText(true);
        populationSizeText = new TextField("10");
        populationSizeText.setPrefWidth(90);
        iterationsLabel = new Label("Number of Iterations: ");
        iterationsLabel.setPrefWidth(140);
        iterationsLabel.setWrapText(true);
        iterationsText = new TextField("40");
        iterationsText.setPrefWidth(90);
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
        callJaya = new Button("GO"); 
        wrapperPane.add(populationSizeLabel, 0,0); 
        wrapperPane.add(populationSizeText, 1,0); 
        wrapperPane.add(iterationsLabel, 0,1); 
        wrapperPane.add(iterationsText, 1,1); 
        layout.add(wrapperPane, 0, 0);
        layout.add(animatedRadio, 0,1); 
        layout.add(notAnimatedRadio, 0,2); 
        layout.add(callJaya, 0,3);
        scene = new Scene(layout,300,300);
        this.setScene(scene);
        this.show(); 
    }
    
    public boolean handleGoButton()
    {
        populationSize = Long.parseLong(populationSizeText.getText());
        iterations = Long.parseLong(iterationsText.getText());
        if (populationSize > 0 && iterations > 0)
        {
            this.close();
            return true;
        }
        else 
            return false; 
    }
    
}
