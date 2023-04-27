package MGDrawVis;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.util.Duration;

public class HC extends Drawer
{
    // drawer required fields (mainly parameters) 
    LeftHCParameterStage hcParameterWindow;
    int squaresize; 
    int squarereduction; 
    int squareSize; 
    int squareReduction;
    
    // drawer constructor to initialize parameters to their values from the parameters stage 
    public HC(DrawingStageActions draw, Graph graph, int phase, boolean animatedEnabled)
    {
        super(graph, draw.initialDrawer);
        phaseNumber = phase; 
        name = "HC";
        hcParameterWindow = new LeftHCParameterStage(animatedEnabled);
        hcParameterWindow.callHC.setOnAction(e -> getParameters(draw, graph));
    }
    
    // call this method with the drawing algorithm button is clicked 
    public void getParameters(DrawingStageActions draw, Graph graph) 
    {
        running = true;
        boolean accepted = hcParameterWindow.handleGoButton();
        if (accepted)
        {
           squaresize = hcParameterWindow.squareSize;
           squarereduction = hcParameterWindow.squareReduction;
           if (phaseNumber == 0)
           {
              if (((RadioButton)hcParameterWindow.animatedSelection.getSelectedToggle()).getText().equals("Not Animated"))
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
        double oldCost; 
        long startTime; 
        long endTime;
        public AnimatedRun(DrawingStageActions draw, PauseTransition pause, Graph graph, double oldCost)
        {
            squareSize = squaresize; 
            squareReduction = squarereduction; 
            this.draw = draw; 
            this.pause = pause; 
            this.graph = graph; 
            this.oldCost = oldCost;
            startTime = System.nanoTime(); 
            endTime = 0;  
        }
        public void handle(ActionEvent e)
        {
            // HC algorithm is here (recursive call) 
            if(squareSize > 0)
            {
                oldCost = search(graph, oldCost); // draw according to all measures 
                draw.addToPanelDrawer();
                if (objFunCost.cost >= oldCost) // reduce the square size if the current size does not make any improvements
                    squareSize /= squareReduction;   // the square size shrinks during the process
                endTime = System.nanoTime();   // to calculate time while executing 
                executionTime = endTime - startTime;
                pause.play();
            }
            else {   // stopping case (base case) when algorithm task is completed 
                running = false;
                updateResults(graph); 
            }
        }
    }
    
    /*************** this method is called for animated layout (it uses a reference variable from the above inner class ***/
    public void runAnimated (DrawingStageActions draw, Graph graph)
    {
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost 
        PauseTransition pause = new PauseTransition (Duration.millis(200));  
        AnimatedRun animated = new AnimatedRun(draw, pause, graph, oldCost);  // creating object from the above inner class 
        pause.setOnFinished(animated);
        pause.play(); // first call to handle method in the inner class above 
    }
    
    /****************** run HC algorithm without animation ***************************/ 
    public void run0 (DrawingStageActions draw, Graph graph)
    {
        squareSize = squaresize; 
        squareReduction = squarereduction; 
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost 
        long startTime = System.nanoTime();  // starting time of the drawer
        while(squareSize > 0)   // drawing process 
        {
           oldCost = search(graph, oldCost); // draw according to all measures 
           if (objFunCost.cost >= oldCost) // reduce the square size if the current size does not make any improvements
              squareSize /= squareReduction;   // the square size shrinks during the process
        }
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer 
        //numEvaluatedSolutions -= (0.6*numEvaluatedSolutions); 
        updateResults(graph);
        draw.addToPanelDrawer(graph);
        running = false;
        updateResults(graph);
        //numEvaluatedSolutions--;
    }
    
    /****************** run HC algorithm for fixed number of evaluated solutions ***************************/
    public void run1 (DrawingStageActions draw, Graph graph)
    {
        squareSize = squaresize; 
        squareReduction = squarereduction; 
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost 
        long startTime = System.nanoTime();  // starting time of the drawer
        int fixedEvalSolutions = Integer.parseInt(TopRunBatchStage.evalSolText.getText());
        while(this.numEvaluatedSolutions < fixedEvalSolutions && squareSize > 0)   // drawing process 
        {
           oldCost = search(graph, oldCost); // draw according to all measures 
           if (objFunCost.cost >= oldCost) // reduce the square size if the current size does not make any improvements
              squareSize /= squareReduction;   // the square size shrinks during the process
        }
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer 
        //numEvaluatedSolutions -= (0.6*numEvaluatedSolutions);
        updateResults(graph);
        draw.addToPanelDrawer(graph);
        running = false;
        updateResults(graph);
        //numEvaluatedSolutions--;
    }
    
    /****************** run HC algorithm for fixed objective function value ***************************/
    public void run2 (DrawingStageActions draw, Graph graph)
    {
        squareSize = squaresize; 
        squareReduction = squarereduction; 
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost 
        long startTime = System.nanoTime();  // starting time of the drawer
        double fixedObjFunValue = Double.parseDouble(TopRunBatchStage.objFunText.getText());
        while(this.objFunCost.cost > fixedObjFunValue)// && squareSize > 0)   // drawing process 
        {
           oldCost = search(graph, oldCost); // draw according to all measures 
           if (objFunCost.cost >= oldCost) // reduce the square size if the current size does not make any improvements
           {   squareSize /= squareReduction;   // the square size shrinks during the process
               if(squareSize <= 0)
                   squareSize = 64;
           }
        }
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer 
        //numEvaluatedSolutions -= (0.6*numEvaluatedSolutions);
        updateResults(graph);
        draw.addToPanelDrawer(graph);
        running = false;
        updateResults(graph);
        //numEvaluatedSolutions--;
    }
    
    /****************** run HC algorithm for fixed execution time ***************************/
    public void run3 (DrawingStageActions draw, Graph graph)
    {
        squareSize = squaresize; 
        squareReduction = squarereduction; 
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost 
        long startTime = System.nanoTime();  // starting time of the drawer
        double fixedExecutionTime = Double.parseDouble(TopRunBatchStage.timeText.getText());
        while(System.nanoTime()-startTime < (fixedExecutionTime*1000000000) && squareSize > 0)   // drawing process 
        {
           oldCost = search(graph, oldCost); // draw according to all measures 
           if (objFunCost.cost >= oldCost) // reduce the square size if the current size does not make any improvements
              squareSize /= squareReduction;   // the square size shrinks during the process
        }
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer 
        //numEvaluatedSolutions -= (0.6*numEvaluatedSolutions);
        updateResults(graph);
        draw.addToPanelDrawer(graph);
        running = false;
        updateResults(graph);
        //numEvaluatedSolutions--;
    }
    
    /***************** neighbourhood search technique for HC **************/ 
    public double search(Graph graph, double oldCost)
    {
        double oldFit, newFit;   // to store old fitness and new fitness of the moved node 
        Vertex temp_point, original_point;   // temporary points
        original_point = new Vertex(0,0,graph.nodeSize); // initialize the temporary point
        temp_point = new Vertex(0,0,graph.nodeSize); // initialize the temporary point
        oldCost = objFunCost.cost;  // store the new cost into the old  
        // for each node create a rectangle of "square size" and test the points on up, down, left, right and the corners 
        // whether they can be potential new locations and test the value of the cost function.
        for(int i=0; i<graph.points.size(); i++)   
        {
            original_point.setCenterX(graph.points.get(i).getCenterX()); // save the coordinates of the original point
            original_point.setCenterY(graph.points.get(i).getCenterY()); 
            temp_point.setCenterX(graph.points.get(i).getCenterX()); // save the coordinates of the original point
            temp_point.setCenterY(graph.points.get(i).getCenterY()); 
            oldFit = objFunCost.cost;
            
            // right location
            if(original_point.getCenterX() + squareSize > 0 && original_point.getCenterX() + squareSize < DrawingStage.screenWidth)
            {
                Vertex t = new Vertex((int)original_point.getCenterX() + squareSize, (int)original_point.getCenterY(), graph.nodeSize);
                graph.updateVertex(i, t);
                updateResults(graph,i);  // recompute distances of the moved node with the other nodes 
                //numEvaluatedSolutions--;
                newFit = objFunCost.cost; // compute current cost 
                if (newFit < oldFit)  // compare fitness of the node in its new and old positions 
                {   
                    temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                    temp_point.setCenterY(graph.points.get(i).getCenterY());   
                }
                else
                {
                    graph.updateVertex(i, temp_point); 
                    updateResults(graph,i);  // recompute the distances of the node in its previous position 
                    //numEvaluatedSolutions--;
                }
            }
            //lower-right corner 
            if(original_point.getCenterX() + squareSize > 0 && original_point.getCenterX() + squareSize < DrawingStage.screenWidth
                    && original_point.getCenterY() + squareSize > 0 && original_point.getCenterY() + squareSize < DrawingStage.screenHeight)
            {
                Vertex t = new Vertex((int)original_point.getCenterX() + squareSize, (int)original_point.getCenterY() + squareSize, graph.nodeSize);
                graph.updateVertex(i, t);
                updateResults(graph,i);  // recompute distances of the moved node with the other nodes 
                //numEvaluatedSolutions--;
                newFit = objFunCost.cost; // compute current cost 
                if (newFit < oldFit)  // compare fitness of the node in its new and old positions 
                {   
                    temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                    temp_point.setCenterY(graph.points.get(i).getCenterY());   
                }
                else
                {
                    graph.updateVertex(i, temp_point); 
                    updateResults(graph,i);  // recompute the distances of the node in its previous position 
                    //numEvaluatedSolutions--;
                }
            }
            // bottom location 
            if(original_point.getCenterY() + squareSize > 0 && original_point.getCenterY() + squareSize < DrawingStage.screenHeight)
            {
                Vertex t = new Vertex((int)original_point.getCenterX(), (int)original_point.getCenterY() + squareSize, graph.nodeSize);
                graph.updateVertex(i, t);
                updateResults(graph,i);  // recompute distances of the moved node with the other nodes  
                //numEvaluatedSolutions--;
                newFit = objFunCost.cost; // compute current cost 
                if (newFit < oldFit)  // compare fitness of the node in its new and old positions 
                {   
                    temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                    temp_point.setCenterY(graph.points.get(i).getCenterY());   
                }
                else
                {
                    graph.updateVertex(i, temp_point); 
                    updateResults(graph,i);  // recompute the distances of the node in its previous position 
                    //numEvaluatedSolutions--;
                }
             }
            // lower-left location 
            if(original_point.getCenterX() - squareSize > 0 && original_point.getCenterX() - squareSize < DrawingStage.screenWidth
                    && original_point.getCenterY() + squareSize > 0 && original_point.getCenterY() + squareSize < DrawingStage.screenHeight)
            {
                Vertex t = new Vertex((int)original_point.getCenterX() - squareSize, (int)original_point.getCenterY() + squareSize, graph.nodeSize);
                graph.updateVertex(i, t);
                updateResults(graph,i);  // recompute distances of the moved node with the other nodes 
                //numEvaluatedSolutions--;
                newFit = objFunCost.cost; // compute current cost  
                if (newFit < oldFit)  // compare fitness of the node in its new and old positions 
                {   
                    temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                    temp_point.setCenterY(graph.points.get(i).getCenterY());   
                }
                else
                {
                    graph.updateVertex(i, temp_point); 
                    updateResults(graph,i);  // recompute the distances of the node in its previous position 
                    //numEvaluatedSolutions--;
                }
             }
             // left location 
             if(original_point.getCenterX() - squareSize > 0 && original_point.getCenterX() - squareSize < DrawingStage.screenWidth)
             {
                Vertex t = new Vertex((int)original_point.getCenterX() - squareSize, (int)original_point.getCenterY(), graph.nodeSize);
                graph.updateVertex(i, t);
                updateResults(graph,i);  // recompute distances of the moved node with the other nodes 
                //numEvaluatedSolutions--;
                newFit = objFunCost.cost; // compute current cost 
                if (newFit < oldFit)  // compare fitness of the node in its new and old positions 
                {   
                    temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                    temp_point.setCenterY(graph.points.get(i).getCenterY());   
                }
                else
                {
                    graph.updateVertex(i, temp_point); 
                    updateResults(graph,i);  // recompute the distances of the node in its previous position 
                    //numEvaluatedSolutions--;
                }
             }
             // upper-left location 
             if(original_point.getCenterX() - squareSize > 0 && original_point.getCenterX() - squareSize < DrawingStage.screenWidth
                     && original_point.getCenterY() - squareSize > 0 && original_point.getCenterY() - squareSize < DrawingStage.screenHeight)
             {
                Vertex t = new Vertex((int)original_point.getCenterX() - squareSize, (int)original_point.getCenterY() - squareSize, graph.nodeSize);
                graph.updateVertex(i, t);
                updateResults(graph,i);  // recompute distances of the moved node with the other nodes 
                //numEvaluatedSolutions--;
                newFit = objFunCost.cost; // compute current cost 
                if (newFit < oldFit)  // compare fitness of the node in its new and old positions 
                {   
                    temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                    temp_point.setCenterY(graph.points.get(i).getCenterY());   
                }
                else
                {
                    graph.updateVertex(i, temp_point); 
                    updateResults(graph,i);  // recompute the distances of the node in its previous position 
                    //numEvaluatedSolutions--;
                }
             }
             // up location 
             if(original_point.getCenterY() - squareSize > 0 && original_point.getCenterY() - squareSize < DrawingStage.screenHeight)
             {
                Vertex t = new Vertex((int)original_point.getCenterX(), (int)original_point.getCenterY() - squareSize, graph.nodeSize);
                graph.updateVertex(i, t);
                updateResults(graph,i);  // recompute distances of the moved node with the other nodes 
                //numEvaluatedSolutions--;
                newFit = objFunCost.cost; // compute current cost 
                if (newFit < oldFit)  // compare fitness of the node in its new and old positions 
                {   
                    temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                    temp_point.setCenterY(graph.points.get(i).getCenterY());   
                }
                else
                {
                    graph.updateVertex(i, temp_point); 
                    updateResults(graph,i);  // recompute the distances of the node in its previous position 
                    //numEvaluatedSolutions--;
                }
             }
             //upper-right location 
             if(original_point.getCenterX() + squareSize > 0 && original_point.getCenterX() + squareSize < DrawingStage.screenWidth
                     && original_point.getCenterY() - squareSize > 0 && original_point.getCenterY() - squareSize < DrawingStage.screenHeight)
             {
                Vertex t = new Vertex((int)original_point.getCenterX() + squareSize, (int)original_point.getCenterY() - squareSize, graph.nodeSize);
                graph.updateVertex(i, t);
                updateResults(graph,i);  // recompute distances of the moved node with the other nodes 
                //numEvaluatedSolutions--;
                newFit = objFunCost.cost; // compute current cost 
                if (newFit < oldFit)  // compare fitness of the node in its new and old positions 
                {  
                    temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                    temp_point.setCenterY(graph.points.get(i).getCenterY());   
                }
                else
                {
                    graph.updateVertex(i, temp_point); 
                    updateResults(graph,i);  // recompute the distances of the node in its previous position 
                    //numEvaluatedSolutions--;
                }
             }
      }
      
      updateResults(graph);
      return oldCost; 
    }
 }
   
