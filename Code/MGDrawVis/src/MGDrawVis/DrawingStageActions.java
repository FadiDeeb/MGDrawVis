package MGDrawVis; 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

 public class DrawingStageActions extends AnchorPane implements DrawingStageActionsInterface
 {
    public Graph graph;
    public Drawer drawer;
    public Drawer initialDrawer;
    public boolean disableUpdateWindowforDrawer; 
    private int nodeSize = 10; 
    
    public DrawingStageActions() 
    {
       graph = new Graph();    
       initialDrawer = new Drawer(0, graph);
       drawer = new Drawer(0, graph);
       disableUpdateWindowforDrawer = true; 
    }

    /***************** stops all the events of top bar *************************/ 
    private void stopAllEvents()
    {
       this.setOnMousePressed(null);
       for(int i=0; i<graph.numVertices; i++)
          graph.points.get(i).setOnMouseDragged(null);
       this.setOnMouseDragged(null);
       this.setOnMouseReleased(null);
       disableUpdateWindowforDrawer = true; 
    }
    
    /*********************** add (paint) all nodes and edges to the panel *****************/ 
    public void addToPanel()
    {
        this.addToPanel(graph);
    }
    public void addToPanel(Graph graph)
    {     
       this.getChildren().clear();
       for(int i=0; i<graph.numVertices; i++)
       {
          graph.getVertex(i).setFill(TopBar.palette.getValue());
          this.getChildren().add(graph.getVertex(i));
          this.getChildren().add(graph.getVertex(i).id);
       }
       for(int j=0; j<graph.lines.size(); j++)
       {
           Line l = graph.lines.get(j);
           this.getChildren().add(l);
       }
    }
    
    public void addToPanelDrawer()
    {
        this.addToPanelDrawer(graph);
    }
    
    public void addToPanelDrawer(Graph graph)
    {     
       this.getChildren().clear();
       for(int i=0; i<graph.numVertices; i++)
       {
          graph.getVertex(i).setFill(TopBar.palette.getValue());
          this.getChildren().add(graph.getVertex(i));
          this.getChildren().add(graph.getVertex(i).id);
       }
       for(int j=0; j<graph.undirectedLines.size(); j++)
       {
           Pair<Integer, Integer> p = graph.undirectedLines.get(j);
           int s = p.getKey();
           int e = p.getValue();
           Vertex vt1 = graph.points.get(s);
           Vertex vt2 = graph.points.get(e); 
           Line line = new Line(vt1.getCenterX(), vt1.getCenterY(), vt2.getCenterX(), vt2.getCenterY());
           graph.lines.set(j,line);
           this.getChildren().add(line);
       }
    }
 
 /*********************************Draw Nodes*****************************/
    public void drawNodes()
    {
       stopAllEvents(); 
       this.setOnMousePressed(e -> handleDrawNodes(e));
    }

    private void handleDrawNodes(MouseEvent e)
    {
       Vertex r = new Vertex((int)e.getX(), (int)e.getY(), nodeSize);  // get the point from the mouse and create a small rectangle
       graph.addVertex(r);  // add the small rectangle to the list  
       updateResultParametersWindow();
       this.getChildren().add(r);
       this.getChildren().add(r.id);
    }
    
    /*******************************Move Nodes*****************************/ 
    public void moveNodes()
    {
       stopAllEvents();
       for(int i=0; i<graph.numVertices; i++)
          graph.points.get(i).setOnMouseDragged(e -> handleMoveNodes(e));
    }
    
    private void handleMoveNodes(MouseEvent e)
    {
       Vertex c = (Vertex) e.getSource();
       Vertex newV = new Vertex((int)e.getX(),(int)e.getY(),nodeSize);
       int node = graph.indexIs(c);
       graph.updateLine(node, c, newV);
       c.setCenterX(e.getX());
       c.setCenterY(e.getY());
       c.id.setX((int)(c.getCenterX()+10));
       c.id.setY((int)(c.getCenterY()+10));
       updateResultParametersWindow();
    }
 
    /**********************************Draw Edges****************************/ 
    private Vertex e1,e2;
    private Line line;
    private int drawEdge;
 
    public void drawEdges()
    {
       stopAllEvents();
       this.setOnMouseDragged(e -> handleStartPoint(e));
       this.setOnMouseReleased(e -> handleEndPoint(e)); 
    }
 
    private void handleStartPoint(MouseEvent e)
    {
       if((drawEdge+1) % 2 == 1)
       {
          e1=null;   
          for (int i=0; i<graph.numVertices; i++)
          {
             if (graph.points.get(i).contains(e.getX(), e.getY())) 
             {
                drawEdge++; 
                e1 = graph.points.get(i);
                line = new Line(e1.getCenterX(), e1.getCenterY(), e.getX(), e.getY());
                this.getChildren().add(line);
             }
          }
       }
       else
       {
           if(line != null)
           {
              line.setEndX(e.getX()); 
              line.setEndY(e.getY()); 
           }
       }
    }
 
    private void handleEndPoint(MouseEvent e)
    {
       boolean found = false; 
       if((drawEdge+1) % 2 == 0)
       {
          for (int i=0; i<graph.numVertices; i++)
          {
             if (graph.points.get(i).contains(e.getX(), e.getY()))
             {
                 e2 = graph.points.get(i); 
                 if (!graph.hasEdge(e1, e2) && !e1.equals(e2))
                 {
                    drawEdge++;
                    line.setEndX(e2.getCenterX());
                    line.setEndY(e2.getCenterY());
                    graph.addEdge(e1, e2, line);
                    updateResultParametersWindow();
                    found = true; 
                 }
             }
          } 
          if (!found)
          {
             drawEdge=0;
             line.setEndX(e1.getCenterX());
             line.setEndY(e1.getCenterY());
          }
       }
       updateResultParametersWindow();
    }
 
    /****************************Clear Screen************************************/
    public void clearScreen()
    {
       stopAllEvents(); 
       this.getChildren().clear();
       graph.clearGraphRandom();
       updateResultParametersWindow();
       drawer.clearResults();
    }
 
    /*****************************Load Graph**********************************/
    public void loadGraph() throws FileNotFoundException, IOException
    {
       stopAllEvents();
       graph.clearGraph();
       this.getChildren().clear(); 
       graph.loadGraph(this, false, new File(""));  
       updateResultParametersWindow();
    }
 
    /****************************Save Graph******************************/
    public void saveGraph() throws FileNotFoundException, IOException, InvalidFormatException
    {
       stopAllEvents();
       graph.saveGraph();
       updateResultParametersWindow();
    }
 
    /************************Randomize Current Graph************************/
    public void randomizeGraph()
    {
       stopAllEvents();
       graph.randomize();
       updateResultParametersWindow();
    }
 
    /***********************Generate Random Graph**************************/
    public void generateRandomGraph()
    {
       stopAllEvents();
       graph.clearGraph();
       this.getChildren().clear();
       TopRandomGeneratorStage randomWindow = new TopRandomGeneratorStage(); 
       randomWindow.goButton.setOnAction(e -> goGenerate(randomWindow));
       randomWindow.batchButton.setOnAction(e -> {
           try {
               goGenerateBatch(randomWindow);
           } catch (IOException ex) {
               Logger.getLogger(DrawingStageActions.class.getName()).log(Level.SEVERE, null, ex);
           }
       });
    }
 
    private void goGenerate(TopRandomGeneratorStage randomWindow)
    {
       graph.clearGraph();
       this.getChildren().clear();
       boolean accepted = randomWindow.handleGoButton();
       if (accepted)
       {
          graph.generateRandom(randomWindow.nodes,randomWindow.edges);
          addToPanel(graph);
          updateResultParametersWindow();
       }
    }
    
    // this is to generate multiple random graphs in the generate random option
    private void goGenerateBatch(TopRandomGeneratorStage randomWindow) throws IOException
    {
       boolean accepted = randomWindow.handleBatchButton();
       if (accepted)
       {
           File directory = new File("MyGraphs" + File.separator + "N"+randomWindow.nodes+"E"+randomWindow.edges + "_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date()));
           directory.mkdir();
           for (int i=0; i < randomWindow.numGraphs; i++)
           {
               graph.clearGraph();
               this.getChildren().clear();
               graph.generateRandom(randomWindow.nodes,randomWindow.edges);
               addToPanel(graph);
               updateResultParametersWindow();
               graph.saveGraph("." + File.separator + directory + File.separator + "N"+randomWindow.nodes+"E"+randomWindow.edges+"_"+(i+1)+".xlsx");
               this.clearScreen();
           }
       }
    }
 
 /**************************************************************************/
 public void runOnMultipleFiles() throws FileNotFoundException, IOException, InterruptedException
 {
     stopAllEvents();
     graph.clearGraph();
     this.getChildren().clear();
     TopRunBatchStage runBatchWindow = new TopRunBatchStage(this);
 }
 
 public void Jaya1()
 {
     stopAllEvents();
     disableUpdateWindowforDrawer = false; 
     drawer = new Jaya(this, graph, 0, true); // create a drawing algorithm object. Old drawer is sent as parameter to maintain current objective function values 
     updateResultParametersWindow(); 
 }
  
 public void JayaGrid()
 {
     stopAllEvents();
     disableUpdateWindowforDrawer = false; 
     drawer = new JayaGrid(this, graph, 0, true); // create a drawing algorithm object. Old drawer is sent as parameter to maintain current objective function values 
     updateResultParametersWindow();
 }
 
 /********************** Hill Climbing Option on the left bar *****************/ 
 public void HillClimbing()
 {
     stopAllEvents();
     disableUpdateWindowforDrawer = false; 
     drawer = new HC(this, graph, 0, true); // create a drawing algorithm object. Old drawer is sent as parameter to maintain current objective function values 
     updateResultParametersWindow(); 
 }
 
 public void SimulatedAnnealing()
 {
     stopAllEvents();
     disableUpdateWindowforDrawer = false; 
     drawer = new SA(this, graph, 0, true); // create a drawing algorithm object. Old drawer is sent as parameter to maintain current objective function values 
     updateResultParametersWindow();
 }
 
 public void updateResultParametersWindow()
 {
     MGDrawVis_StarterClass.drawingStage.rightbar.cont1NodesValue.setText("" + graph.numVertices);
     MGDrawVis_StarterClass.drawingStage.rightbar.cont1EdgesValue.setText("" + graph.numEdges);
     if (disableUpdateWindowforDrawer)
        drawer.updateResults(graph);
     else 
        drawer.clearPartialResults();
 }
}