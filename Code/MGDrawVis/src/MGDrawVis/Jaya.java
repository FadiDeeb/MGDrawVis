
package MGDrawVis;

import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.RadioButton;
import javafx.util.Duration;

public class Jaya extends Drawer 
{
    // drawer required fields (mainly parameters) 
    LeftJayaParametersStage jayaParameterWindow;
    Graph jayaBestCand;
    Graph jayaWorstCand;
    int bestIndex, worstIndex; 
    ArrayList<Graph> jayaCandidates; 
    long populationSize; 
    long iterations;
    
    // drawer constructor to initialize parameters to their values from the parameters stage 
    public Jaya(DrawingStageActions draw, Graph graph, int phase, boolean animatedEnabled)
    {
        super(graph, draw.initialDrawer);
        phaseNumber = phase; 
        name = "Jaya"; 
        jayaParameterWindow = new LeftJayaParametersStage(animatedEnabled);
        jayaParameterWindow.callJaya.setOnAction(e -> getParameters(draw, graph));
    }
        
    // call this method with the drawing algorithm button is clicked 
    public void getParameters(DrawingStageActions draw, Graph graph) 
    {
        running = true;
        boolean accepted = jayaParameterWindow.handleGoButton();
        if (accepted)
        {
           populationSize = jayaParameterWindow.populationSize;
           iterations = jayaParameterWindow.iterations;
           if (phaseNumber == 0)
           {
              if (((RadioButton)jayaParameterWindow.animatedSelection.getSelectedToggle()).getText().equals("Not Animated")) 
                 run0(draw, graph); // not animated 
              else
                 runAnimated(draw, graph);  // animated
           }
        }
    }
    
    /************** this inner class is used for animation purposes *********/
    private class AnimatedRun implements EventHandler<ActionEvent>
    {
        DrawingStageActions draw; 
        PauseTransition pause;
        Graph graph; 
        long startTime; 
        long endTime;
        public AnimatedRun(DrawingStageActions draw, PauseTransition pause, Graph graph)
        {
            this.draw = draw; 
            this.pause = pause; 
            this.graph = graph; 
            startTime = System.nanoTime();
            endTime = 0;  
        }
        public void handle(ActionEvent e)
        {
            if(iterations > 0)
            {
               generateNewCandidates();
               findBestWorst();
               graph.copyGraph(jayaBestCand);
               draw.addToPanelDrawer(graph);
               updateResults(this.graph);  
               //numEvaluatedSolutions--;
               iterations--;                
               endTime = System.nanoTime(); // ending time of the drawer 
               executionTime = endTime - startTime; // compute execution time of the drawer
               this.pause.play();
            }
            else{  
                running = false;
                draw.addToPanelDrawer(graph);
                updateResults(this.graph); 
                //numEvaluatedSolutions--;
            }
        }
    }
    
    /*************** this method is called for animated layout (it uses a reference variable from the above inner class ***/
    public void runAnimated (DrawingStageActions draw, Graph graph)
    {
        jayaBestCand = new Graph(); 
        jayaBestCand.copyGraph(graph);
        jayaWorstCand = new Graph(); 
        jayaWorstCand.copyGraph(graph);
        updateResults(graph); // this calculates solutionVector and Computes cost 
        //numEvaluatedSolutions--;
        generateJayaCandidates(graph); 
        findBestWorst();
        PauseTransition pause = new PauseTransition (Duration.millis(200));  
        AnimatedRun animated = new AnimatedRun(draw, pause, graph);  // creating object from the above inner class 
        pause.setOnFinished(animated);
        pause.play(); // first call to handle method in the inner class above 
    }
    
    /****************** run Jaya algorithm without animation ***************************/ 
    public void run0 (DrawingStageActions draw, Graph graph)
    {
        updateResults(graph);
        //numEvaluatedSolutions--;
        jayaBestCand = new Graph(); 
        jayaBestCand.copyGraph(graph);
        jayaWorstCand = new Graph(); 
        jayaWorstCand.copyGraph(graph);
        long startTime = System.nanoTime();  // starting time of the drawer
        generateJayaCandidates(graph); 
        findBestWorst();
        for (int i=0; i<iterations; i++)
        { 
            generateNewCandidates();
            findBestWorst();
        } 
        graph.copyGraph(jayaBestCand);
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer
        updateResults(graph);
        //numEvaluatedSolutions--;
        draw.addToPanelDrawer(graph); // display execution time   
        running = false; 
        updateResults(graph);  
        //numEvaluatedSolutions--;
    } 
    
    /****************** run Jaya algorithm for fixed number of evaluated solutions ***************************/ 
    public void run1 (DrawingStageActions draw, Graph graph)
    {
        updateResults(graph);
        //numEvaluatedSolutions--;
        jayaBestCand = new Graph(); 
        jayaBestCand.copyGraph(graph);
        jayaWorstCand = new Graph(); 
        jayaWorstCand.copyGraph(graph);
        long startTime = System.nanoTime();  // starting time of the drawer
        generateJayaCandidates(graph); 
        findBestWorst();
        int fixedEvalSolutions = Integer.parseInt(TopRunBatchStage.evalSolText.getText());
        for (int i=0; i<iterations; i++)
        { 
            if(this.numEvaluatedSolutions < fixedEvalSolutions)
            {
               generateNewCandidates();
               findBestWorst();
            }
        }
        graph.copyGraph(jayaBestCand);
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer
        updateResults(graph);
        //numEvaluatedSolutions--;
        draw.addToPanelDrawer(graph); // display execution time   
        running = false; 
        updateResults(graph); 
        //numEvaluatedSolutions--;
    }
    
    /****************** run Jaya algorithm for fixed Objective function value ***************************/ 
    public void run2 (DrawingStageActions draw, Graph graph)
    {
        updateResults(graph);
        //numEvaluatedSolutions--;
        jayaBestCand = new Graph(); 
        jayaBestCand.copyGraph(graph);
        jayaWorstCand = new Graph(); 
        jayaWorstCand.copyGraph(graph);
        long startTime = System.nanoTime();  // starting time of the drawer
        generateJayaCandidates(graph); 
        findBestWorst();
        double fixedObjFunValue = Double.parseDouble(TopRunBatchStage.objFunText.getText());
        for (int i=0; i<iterations; i++)
        { 
            if(this.objFunCost.cost > fixedObjFunValue)
            {
               generateNewCandidates();
               findBestWorst();
            }
        }
        graph.copyGraph(jayaBestCand);
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer
        updateResults(graph);
        //numEvaluatedSolutions--;
        draw.addToPanelDrawer(graph); // display execution time   
        running = false; 
        updateResults(graph); 
        //numEvaluatedSolutions--;
    }
    
    /****************** run Jaya algorithm for fixed execution time ***************************/ 
    public void run3 (DrawingStageActions draw, Graph graph)
    {
        updateResults(graph);
        //numEvaluatedSolutions--;
        jayaBestCand = new Graph(); 
        jayaBestCand.copyGraph(graph);
        jayaWorstCand = new Graph(); 
        jayaWorstCand.copyGraph(graph);
        long startTime = System.nanoTime();  // starting time of the drawer
        generateJayaCandidates(graph); 
        findBestWorst();
        double fixedExecutionTime = Double.parseDouble(TopRunBatchStage.timeText.getText());
        for (int i=0; i<iterations; i++)
        { 
            if(System.nanoTime()-startTime < fixedExecutionTime*1000000000)
            {
               generateNewCandidates();
               findBestWorst();
            }
        }
        graph.copyGraph(jayaBestCand); 
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer
        updateResults(graph);
        //numEvaluatedSolutions--;
        draw.addToPanelDrawer(graph); // display execution time   
        running = false; 
        updateResults(graph); 
        //numEvaluatedSolutions--;
    }
    
    private void findBestWorst()
    {
        double bestCost = Double.MAX_VALUE;
        double worstCost = Double.MIN_VALUE;
        bestIndex = -1; 
        worstIndex = -1; 
        for(int i=0; i<populationSize; i++)
        {
            if (jayaCandidates.get(i).cost < bestCost)
            {
                bestCost = jayaCandidates.get(i).cost;
                bestIndex = i; 
            }
            if (jayaCandidates.get(i).cost > worstCost)
            {
                worstCost = jayaCandidates.get(i).cost;
                worstIndex = i;  
            }
        }
        if (bestIndex != -1)
        {
            jayaBestCand.copyGraph(jayaCandidates.get(bestIndex));
            jayaBestCand.cost = bestCost; 
        }
        if (worstIndex != -1)
        {
            jayaWorstCand.copyGraph(jayaCandidates.get(worstIndex));
            jayaWorstCand.cost = worstCost; 
        }
    }
    
    private void generateJayaCandidates(Graph graph)
    {
        Bounds bounds = DrawingStage.screen.getBoundsInLocal();
        Bounds screenBounds = DrawingStage.screen.localToScene(bounds);
        int sx = (int) DrawingStage.screen.getTop().getLayoutX();
        int sy = (int) DrawingStage.screen.getLeft().getLayoutX();
        int width = (int) DrawingStage.screenWidth; // screenBounds.getWidth();
        int height = (int) DrawingStage.screenHeight;//screenBounds.getHeight();
        jayaCandidates = new ArrayList<>();
        for (int i=0; i<populationSize; i++) // added 1 for the current layout 
           jayaCandidates.add(new Graph());
        for(int i=0; i<populationSize; i++)
           jayaCandidates.get(i).copyGraph(graph); 
        for (int k = 0; k < populationSize; k++)
        {
           for (int i=0; i<graph.points.size(); i++)
           {
              int x = (int) ((Math.random() * (sx+width)));      
              int y = (int) ((Math.random() * (sy+height)));
              Vertex v = new Vertex(x,y,graph.nodeSize);
              jayaCandidates.get(k).updateVertex(i, v);
            }
            updateResults(jayaCandidates.get(k));
            jayaCandidates.get(k).cost = this.objFunCost.cost;
        }   
    }
    
    private void generateNewCandidates()
    {    
        double cost; 
        double newCost; 
        Graph g = new Graph();
        for(int i=0; i<populationSize; i++)
        { 
            if(i != bestIndex)
            {
               g = new Graph();
               g.copyGraph(jayaCandidates.get(i));
               cost = jayaCandidates.get(i).cost;
         //      if (i == worstIndex)
                  g = updateCandidateJaya(g); 
         //      else if (Math.random() < 0.7)
         //         g = updateCandidateJaya(g); 
         //         else     
         //            g = updateCandidateQI(g);
                updateResults(g);
                newCost = this.objFunCost.cost;
                if (newCost < cost)
                {
                    jayaCandidates.get(i).copyGraph(g);
                    jayaCandidates.get(i).cost = newCost;
                }
            }
        }
    }
        
    public Graph updateCandidateJaya(Graph g)
    {
       Bounds bounds = DrawingStage.screen.getBoundsInLocal();
       Bounds screenBounds = DrawingStage.screen.localToScene(bounds);
       int sx = (int) DrawingStage.screen.getTop().getLayoutX();
       int sy = (int) DrawingStage.screen.getLeft().getLayoutX();
       int width = (int) DrawingStage.screenWidth; // screenBounds.getWidth();
       int height = (int) DrawingStage.screenHeight;//screenBounds.getHeight();
       for (int i = 0; i < g.points.size(); i++)
       {    
           int x = (int)(g.points.get(i).getCenterX() + (int)((Math.random() * (jayaBestCand.points.get(i).getCenterX() - Math.abs(g.points.get(i).getCenterX())))) - (int)((Math.random() * (jayaWorstCand.points.get(i).getCenterX() - Math.abs(g.points.get(i).getCenterX())))));
           int y = (int)(g.points.get(i).getCenterY() + (int)((Math.random() * (jayaBestCand.points.get(i).getCenterY() - Math.abs(g.points.get(i).getCenterY())))) - (int)((Math.random() * (jayaWorstCand.points.get(i).getCenterY() - Math.abs(g.points.get(i).getCenterY())))));
           this.numEvaluatedSolutions++;
           /*  if (x < 0)
              x = sx; 
           else if (x > sx+width)
               x = sx + width; 
           if (y < 0)
               y = sy; 
           else if (y > sy+height)
               y = sy+height;*/
           Vertex v = new Vertex(x,y,g.nodeSize);
           g.updateVertex(i, v);
       }
       return g; 
    }
    
    public Graph updateCandidateQI(Graph g)
    {
       Bounds bounds = DrawingStage.screen.getBoundsInLocal();
       Bounds screenBounds = DrawingStage.screen.localToScene(bounds);
       int sx = (int) DrawingStage.screen.getTop().getLayoutX();
       int sy = (int) DrawingStage.screen.getLeft().getLayoutX();
       int width = (int) DrawingStage.screenWidth; // screenBounds.getWidth();
       int height = (int) DrawingStage.screenHeight;//screenBounds.getHeight();
       Graph currentSQ = new Graph(); 
       Graph bestSQ = new Graph(); 
       Graph worstSQ = new Graph(); 
       bestSQ.copyGraph(jayaBestCand);
       worstSQ.copyGraph(jayaWorstCand);
       currentSQ.copyGraph(g);
       for (int i = 0; i<g.points.size(); i++)
       {
           bestSQ.points.set(i, new Vertex((int)Math.pow(bestSQ.points.get(i).getCenterX(),2),(int)Math.pow(bestSQ.points.get(i).getCenterY(),2), g.nodeSize));
           worstSQ.points.set(i, new Vertex((int)Math.pow(worstSQ.points.get(i).getCenterX(),2),(int)Math.pow(worstSQ.points.get(i).getCenterY(),2), g.nodeSize));
           currentSQ.points.set(i, new Vertex((int)Math.pow(currentSQ.points.get(i).getCenterX(),2),(int)Math.pow(currentSQ.points.get(i).getCenterY(),2), g.nodeSize));
       }
       
       for (int i = 0; i < g.points.size(); i++)
       {   
           double s1x1 = (((bestSQ.points.get(i).getCenterX() - worstSQ.points.get(i).getCenterX()) * g.cost) + ((worstSQ.points.get(i).getCenterX() - currentSQ.points.get(i).getCenterX()) * jayaBestCand.cost) + ((currentSQ.points.get(i).getCenterX() - bestSQ.points.get(i).getCenterX()) * jayaWorstCand.cost));
           double s1y1 = (((bestSQ.points.get(i).getCenterY() - worstSQ.points.get(i).getCenterY()) * g.cost) + ((worstSQ.points.get(i).getCenterY() - currentSQ.points.get(i).getCenterY()) * jayaBestCand.cost) + ((currentSQ.points.get(i).getCenterY() - bestSQ.points.get(i).getCenterY()) * jayaWorstCand.cost));
           double s2x1 = (((jayaBestCand.points.get(i).getCenterX() - jayaWorstCand.points.get(i).getCenterX()) * g.cost) + ((jayaWorstCand.points.get(i).getCenterX() - g.points.get(i).getCenterX()) * jayaBestCand.cost) + ((g.points.get(i).getCenterX() - jayaBestCand.points.get(i).getCenterX()) * jayaWorstCand.cost));
           double s2y1 = (((jayaBestCand.points.get(i).getCenterY() - jayaWorstCand.points.get(i).getCenterY()) * g.cost) + ((jayaWorstCand.points.get(i).getCenterY() - g.points.get(i).getCenterY()) * jayaBestCand.cost) + ((g.points.get(i).getCenterY() - jayaBestCand.points.get(i).getCenterY()) * jayaWorstCand.cost));
           if (s2x1 == 0)
               s2x1 = 0.0001; 
           if (s2y1 == 0)
               s2y1 = 0.0001; 
           int x = (int)((0.5)*(s1x1/s2x1));
           int y = (int)((0.5)*(s1y1/s2y1));
           if (x < 0)
              x = sx; 
           else if (x > sx+width)
               x = sx + width; 
           if (y < 0)
               y = sy; 
           else if (y > sy+height)
               y = sy+height; 
           this.numEvaluatedSolutions++;
           Vertex v = new Vertex(x,y,g.nodeSize);
           g.updateVertex(i, v);
       }
       return g; 
    }
 }
    
  