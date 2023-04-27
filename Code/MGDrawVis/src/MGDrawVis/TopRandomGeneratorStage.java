package MGDrawVis;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TopRandomGeneratorStage extends Stage
{
    public GridPane layout; 
    public GridPane wrapperPane; 
    public Label nodesLabel; 
    public TextField nodesText; 
    public Label edgesLabel; 
    public TextField edgesText; 
    public Button goButton;
    public TextField numGraphsText; 
    public Label numGraphsLabel; 
    public Button batchButton; 
    public GridPane wrapperPane2; 
    public Label message; 
    public Scene scene; 
    public int nodes,edges; 
    public int numGraphs; 
    
    public TopRandomGeneratorStage()
    {
        wrapperPane = new GridPane(); 
        wrapperPane.setPadding(new Insets(10,10,10,10));
        layout = new GridPane(); 
        layout.setPadding(new Insets(10,10,10,10));
        nodesLabel = new Label("Nodes: ");
        nodesLabel.setPrefWidth(50);
        nodesText = new TextField("0");
        nodesText.setPrefWidth(90);
        edgesLabel = new Label("Edges: ");
        edgesLabel.setPrefWidth(50);
        edgesText = new TextField("0");
        edgesText.setPrefWidth(90);
        goButton = new Button("Generate a Graph"); 
        wrapperPane2 = new GridPane(); 
        wrapperPane2.setPadding(new Insets(10,10,10,10));
        numGraphsLabel = new Label("How many graphs ");
        numGraphsLabel.setPrefWidth(120);
        numGraphsText = new TextField("1");
        numGraphsText.setPrefWidth(50);
        batchButton = new Button ("Generate Graphs");
        message = new Label(); 
        message.setWrapText(true);
        wrapperPane.add(nodesLabel, 0,0); 
        wrapperPane.add(nodesText, 1,0); 
        wrapperPane.add(edgesLabel, 0,1); 
        wrapperPane.add(edgesText, 1,1); 
        layout.add(wrapperPane, 0, 0);
        layout.add(goButton, 0,1);
        layout.add(message, 0, 2);
        wrapperPane2.add(numGraphsLabel, 0,3);
        wrapperPane2.add(numGraphsText, 1,3);
        wrapperPane2.add(batchButton, 0, 4);
        layout.add(wrapperPane2, 0, 3);
        scene = new Scene(layout,200,200);
        this.setScene(scene);
        this.show(); 
    }
    
    public boolean handleGoButton()
    {
        nodes = Integer.parseInt(nodesText.getText());
        edges = Integer.parseInt(edgesText.getText());
        if (nodes >= 1)
        {
            if(edges >= nodes-1 && edges <= (nodes*(nodes-1)/2))
            {   
                this.close();
                return true;
            }
            else 
            {
                message.setTextFill(Color.RED);
                message.setText("Number of edges must be at least " + (nodes-1) + " and at most " + (nodes*(nodes-1)/2));
                return false; 
            }
        }
        else {
          message.setTextFill(Color.RED);
          message.setText("Number of nodes must be at least 1");
          return false; 
        }
    }
    
    public boolean handleBatchButton()
    {
        handleGoButton();
        numGraphs = Integer.parseInt(numGraphsText.getText());
        if (numGraphs > 0)
            return true; 
        else return false; 
    }
    
}
