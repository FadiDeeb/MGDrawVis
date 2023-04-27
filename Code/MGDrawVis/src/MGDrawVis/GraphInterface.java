package MGDrawVis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.shape.Line;

public interface GraphInterface
{
    boolean isEmpty();
    void addVertex(Vertex vertex);
    void updateVertex(int v, Vertex vertex);
    void copyPointsBackToGraph(ArrayList<Vertex> vertices);
    Vertex getVertex(int index);
    int indexIs(Vertex v);
    void addEdge(Vertex fromVertex, Vertex toVertex, Line line);
    boolean hasEdge(Vertex fromVertex, Vertex toVertex);
    boolean hasEdge(int v1, int v2);
    void clearGraph(); 
    void loadGraph(DrawingStageActions layout, boolean multiple, File filename) throws FileNotFoundException, IOException;
    void loadMultipleGraphs(DrawingStageActions layout) throws FileNotFoundException, IOException;
    void saveGraph() throws FileNotFoundException, IOException;
    void saveGraph(String fileName) throws FileNotFoundException, IOException;
    void randomize(); 
    void generateRandom(int nodes, int edges);
    void generateRandomAllTypes(int nodes, int edges);
    void updateLine(int v, Vertex oldV, Vertex newV); // update the end points of a line 
    boolean isConnected();
    void computeDistances();
    void computeDistances(int p);
    void copyGraph(Graph graph);
}
