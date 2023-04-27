package MGDrawVis;

import javafx.application.Application;
import javafx.stage.Stage;

public class MGDrawVis_StarterClass extends Application 
{
    public static DrawingStage drawingStage; 
    
    public void start(Stage stage) throws Exception
    { 
        drawingStage = new DrawingStage();
    }
    
    public static void main(String[] args) 
    {
        launch(args);    // Create the Frame     
    }
}