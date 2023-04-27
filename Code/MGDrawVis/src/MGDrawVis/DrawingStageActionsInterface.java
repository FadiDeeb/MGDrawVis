package MGDrawVis;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public interface DrawingStageActionsInterface 
{
    void drawNodes();
    void moveNodes();
    void drawEdges();
    void clearScreen();
    void loadGraph() throws FileNotFoundException, IOException, InvalidFormatException;
    void saveGraph() throws FileNotFoundException, IOException, InvalidFormatException;
    void randomizeGraph();
    void generateRandomGraph();
    void runOnMultipleFiles()throws FileNotFoundException, IOException, InterruptedException;
    void Jaya1();
    void JayaGrid();
    void HillClimbing(); 
    void SimulatedAnnealing();
    void updateResultParametersWindow();
    void addToPanel(Graph graph);
    void addToPanelDrawer();
}
