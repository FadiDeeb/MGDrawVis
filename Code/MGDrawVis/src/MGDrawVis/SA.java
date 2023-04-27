package MGDrawVis;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.util.Duration;

public class SA extends Drawer
{
    // drawer required fields (mainly parameters) 
    LeftSAParametersStage saParameterWindow; 
    int squareSize; 
    int maxIterations;
    int iterationsPerTemp; 
    double initialTemp; 
    double coolDownRate; 
    double TempThreshold; 
    
    // drawer constructor to initialize parameters to their values from the parameters stage 
    public SA(DrawingStageActions draw, Graph graph, int phase, boolean animatedEnabled)
    {
        super(graph, draw.initialDrawer);
        phaseNumber = phase; 
        name = "SA"; 
        saParameterWindow = new LeftSAParametersStage(animatedEnabled);
        saParameterWindow.callSA.setOnAction(e -> getParameters(draw, graph));
    }
    
    // call this method with the drawing algorithm button is clicked 
    public void getParameters(DrawingStageActions draw, Graph graph) 
    {
        running = true;
        boolean accepted = saParameterWindow.handleGoButton();
        if (accepted)
        {
           squareSize = saParameterWindow.squareSize;
           maxIterations = saParameterWindow.maxIteration;
           iterationsPerTemp = saParameterWindow.iterationPerTemp;
           initialTemp = saParameterWindow.initialTemp; 
           coolDownRate = saParameterWindow.coolDownRate; 
           if (phaseNumber == 0)
           {
              if (((RadioButton)saParameterWindow.animatedSelection.getSelectedToggle()).getText().equals("Not Animated"))
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
        int temp; 
        long startTime; 
        long endTime;
        public AnimatedRun(DrawingStageActions draw, PauseTransition pause, Graph graph, double oldCost)
        {
            temp = iterationsPerTemp; 
            this.draw = draw; 
            this.pause = pause; 
            this.graph = graph; 
            this.oldCost = oldCost;
            startTime = System.nanoTime(); 
            endTime = 0;  
        }
        public void handle(ActionEvent e)
        {
            // SA algorithm is here (recursive call) 
            if(maxIterations > 0)
            {
                this.oldCost = search(graph, this.oldCost, initialTemp, squareSize); // draw according to all measures 
                draw.addToPanelDrawer();
                iterationsPerTemp--; 
                if(iterationsPerTemp <= 0)
                {
                   maxIterations--;
                   iterationsPerTemp = temp;
                }
                endTime = System.nanoTime();   // to calculate time while executing 
                executionTime = endTime - startTime;
                pause.play();
            }
            else {   // stopping case (base case) when algorithm task is completed z
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
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost
        double Temp = initialTemp;
        int squaresize = squareSize;
        long startTime = System.nanoTime();  // starting time of the drawer
        for (int i=0; i<maxIterations; i++)   // # of iterations of the whole drawing process. it executes according to the value of stopcriterion in the small window 
        {
           this.objFunCost.cost = oldCost; 
           for(int j=0; j<iterationsPerTemp; j++)  // number of iterations for searching in the neighbourhood of the current solution with temperature Temp 
           {
               oldCost = search(graph, oldCost, Temp, squaresize); // draw according to all measures 
           }
           Temp = Temp * coolDownRate; // this cools down the temperature. Searching in the neighbourhood and accepting solutions would become harder  
           squaresize = squaresize - (squareSize/maxIterations);    
        }
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer 
        //numEvaluatedSolutions -= (0.8*numEvaluatedSolutions); 
        updateResults(graph);
        draw.addToPanelDrawer(graph);
        running = false;
        updateResults(graph); 
        //numEvaluatedSolutions--;
    }
    
    /****************** run SA algorithm for fixed number of evaluated solutions ***************************/ 
    public void run1 (DrawingStageActions draw, Graph graph)
    {
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost
        double Temp = initialTemp;
        int squaresize = squareSize;
        long startTime = System.nanoTime();  // starting time of the drawer
        int fixedEvalSolutions = Integer.parseInt(TopRunBatchStage.evalSolText.getText());
        for (int i=0; i<maxIterations; i++)   // # of iterations of the whole drawing process. it executes according to the value of stopcriterion in the small window 
        {
           this.objFunCost.cost = oldCost;  
           for(int j=0; j<iterationsPerTemp; j++)  // number of iterations for searching in the neighbourhood of the current solution with temperature Temp 
           {
               if (this.numEvaluatedSolutions < fixedEvalSolutions)
               {
                  oldCost = search(graph, oldCost, Temp, squaresize); // draw according to all measures 
               }
           }
           Temp = Temp * coolDownRate; // this cools down the temperature. Searching in the neighbourhood and accepting solutions would become harder  
           squaresize = squaresize - (squareSize/maxIterations);    
        }
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer 
       // numEvaluatedSolutions -= (0.9*numEvaluatedSolutions); 
        updateResults(graph);
        draw.addToPanelDrawer(graph);
        running = false;
        updateResults(graph); 
        //numEvaluatedSolutions--;
    }
    
    /****************** run SA algorithm for fixed Objective function value ***************************/ 
    public void run2 (DrawingStageActions draw, Graph graph)
    {
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost
        double Temp = initialTemp;
        int squaresize = squareSize;
        long startTime = System.nanoTime();  // starting time of the drawer
        double fixedObjFunValue = Double.parseDouble(TopRunBatchStage.objFunText.getText());
        for (int i=0; i<maxIterations; i++)   // # of iterations of the whole drawing process. it executes according to the value of stopcriterion in the small window 
        {
           this.objFunCost.cost = oldCost; 
           for(int j=0; j<iterationsPerTemp; j++)  // number of iterations for searching in the neighbourhood of the current solution with temperature Temp 
           {
               if (this.objFunCost.cost > fixedObjFunValue)
               {
                  oldCost = search(graph, oldCost, Temp, squaresize); // draw according to all measures 
               }
           }
           Temp = Temp * coolDownRate; // this cools down the temperature. Searching in the neighbourhood and accepting solutions would become harder  
           squaresize = squaresize - (squareSize/maxIterations);    
        }
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer 
        //numEvaluatedSolutions -= (0.8*numEvaluatedSolutions); 
        updateResults(graph);
        draw.addToPanelDrawer(graph);
        running = false;
        updateResults(graph); 
        //numEvaluatedSolutions--;
    }
    
    /****************** run SA algorithm for fixed execution time ***************************/ 
    public void run3 (DrawingStageActions draw, Graph graph)
    {
        double oldCost = Double.MAX_VALUE;
        updateResults(graph); // this calculates solutionVector and Computes cost
        double Temp = initialTemp;
        int squaresize = squareSize;
        long startTime = System.nanoTime();  // starting time of the drawer
        double fixedExecutionTime = Double.parseDouble(TopRunBatchStage.timeText.getText());
        for (int i=0; i<maxIterations; i++)   // # of iterations of the whole drawing process. it executes according to the value of stopcriterion in the small window 
        {
           this.objFunCost.cost = oldCost;  
           for(int j=0; j<iterationsPerTemp; j++)  // number of iterations for searching in the neighbourhood of the current solution with temperature Temp 
           {
               if (System.nanoTime()-startTime < fixedExecutionTime*1000000000)
               {
                  oldCost = search(graph, oldCost, Temp, squaresize); // draw according to all measures 
               }
           }
           Temp = Temp * coolDownRate; // this cools down the temperature. Searching in the neighbourhood and accepting solutions would become harder  
           squaresize = squaresize - (squareSize/maxIterations);    
        }
        long endTime = System.nanoTime(); // ending time of the drawer 
        executionTime = endTime - startTime; // compute execution time of the drawer 
        //numEvaluatedSolutions -= (0.8*numEvaluatedSolutions); 
        updateResults(graph);
        draw.addToPanelDrawer(graph);
        running = false;
        updateResults(graph); 
        //numEvaluatedSolutions--;
    }
    
    /***************** neighbourhood search technique for HC **************/ 
    public double search(Graph graph, double oldCost, double Temp, int squaresize)
    {
        double oldFit, newFit;   // to store old fitness and new fitness of the moved node 
        double costDiff;  // stores the value of the difference between the new cost and the old cost
        double simAcceptance; // stores the value of e^-costDiff/Temp .. This will determine whether to accept a solution based on a random generated number simRand
        double simRand; // stores a random number between 0 and less than 1 to be compared with simAcceptance value 
        Vertex temp_point, original_point;   // temporary points
        original_point = new Vertex(0,0,graph.nodeSize); // initialize the temporary point
        temp_point = new Vertex(0,0,graph.nodeSize); // initialize the temporary point
        oldCost = objFunCost.cost;  // store the new cost into the old  
        //int randNeighbour = 1 + (int)(Math.random() * ((8 - 1) + 1)); // generate a random number to choose a random neighbourhood location on the 8 positions of the square 
        // for each node create a rectangle of "square size" and test the points on up, down, left, right and the corners 
        // whether they can be potential new locations and test the value of the cost function.
        for(int i=0; i<graph.points.size(); i++)   
        {
            int randNeighbour = 1 + (int)(Math.random() * ((8 - 1) + 1)); // generate a random number to choose a random neighbourhood location on the 8 positions of the square 
            original_point.setCenterX(graph.points.get(i).getCenterX()); // save the coordinates of the original point
            original_point.setCenterY(graph.points.get(i).getCenterY()); 
            temp_point.setCenterX(graph.points.get(i).getCenterX()); // save the coordinates of the original point
            temp_point.setCenterY(graph.points.get(i).getCenterY()); 
            oldFit = objFunCost.cost;
            
            // right location
            if (randNeighbour == 1)
            {
               if(original_point.getCenterX() + squareSize > 0 && original_point.getCenterX() + squareSize < DrawingStage.screenWidth)
               {
                   Vertex t = new Vertex((int)original_point.getCenterX() + squareSize, (int)original_point.getCenterY(), graph.nodeSize);
                   graph.updateVertex(i, t);
                   updateResults(graph,i);  // recompute distances of the moved node with the other nodes 
                   //numEvaluatedSolutions--;
                   newFit = objFunCost.cost; // compute current cost 
                   costDiff = newFit - oldFit; 
                   simAcceptance = Math.exp(-1*costDiff/Temp); // compute the value of e^-costDiff/Temp 
                   simRand = Math.random(); // generate a random number 
                   if ( (costDiff < 0) || (simAcceptance <= 1 && simRand < simAcceptance) )  // compare fitness of the node in its new and old positions
                   {   
                      temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                      temp_point.setCenterY(graph.points.get(i).getCenterY());   
                   }
                   else
                   {
                      graph.updateVertex(i, temp_point); 
                      updateResults(graph, i);  // recompute the distances of the node in its previous position 
                      //numEvaluatedSolutions--;
                   }
               }
            }
            //lower-right corner 
            else if (randNeighbour == 2)
            {
               if(original_point.getCenterX() + squareSize > 0 && original_point.getCenterX() + squareSize < DrawingStage.screenWidth
                    && original_point.getCenterY() + squareSize > 0 && original_point.getCenterY() + squareSize < DrawingStage.screenHeight)
               {
                   Vertex t = new Vertex((int)original_point.getCenterX() + squareSize, (int)original_point.getCenterY() + squareSize, graph.nodeSize);
                   graph.updateVertex(i, t);
                   updateResults(graph, i);  // recompute distances of the moved node with the other nodes 
                   //numEvaluatedSolutions--;
                   newFit = objFunCost.cost; // compute current cost 
                   costDiff = newFit - oldFit; 
                   simAcceptance = Math.exp(-1*costDiff/Temp); // compute the value of e^-costDiff/Temp 
                   simRand = Math.random(); // generate a random number 
                   if ( (costDiff < 0) || (simAcceptance <= 1 && simRand < simAcceptance) )  // compare fitness of the node in its new and old positions
                   {  
                      temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                      temp_point.setCenterY(graph.points.get(i).getCenterY());   
                   }
                   else
                   {
                      graph.updateVertex(i, temp_point); 
                      updateResults(graph, i);  // recompute the distances of the node in its previous position 
                      //numEvaluatedSolutions--;
                   }
               }
            }
            // bottom location 
            else if (randNeighbour == 3)
            {
               if(original_point.getCenterY() + squareSize > 0 && original_point.getCenterY() + squareSize < DrawingStage.screenHeight)
               {
                   Vertex t = new Vertex((int)original_point.getCenterX(), (int)original_point.getCenterY() + squareSize, graph.nodeSize);
                   graph.updateVertex(i, t);
                   updateResults(graph, i);  // recompute distances of the moved node with the other nodes  
                   //numEvaluatedSolutions--;
                   newFit = objFunCost.cost; // compute current cost 
                   costDiff = newFit - oldFit; 
                   simAcceptance = Math.exp(-1*costDiff/Temp); // compute the value of e^-costDiff/Temp 
                   simRand = Math.random(); // generate a random number 
                   if ( (costDiff < 0) || (simAcceptance <= 1 && simRand < simAcceptance) )  // compare fitness of the node in its new and old positions
                   {  
                      temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                      temp_point.setCenterY(graph.points.get(i).getCenterY());   
                   }
                   else
                   { 
                      graph.updateVertex(i, temp_point); 
                      updateResults(graph, i);  // recompute the distances of the node in its previous position 
                      //numEvaluatedSolutions--;
                   }
                }
            }
            // lower-left location 
            else if (randNeighbour == 4)
            {
               if(original_point.getCenterX() - squareSize > 0 && original_point.getCenterX() - squareSize < DrawingStage.screenWidth
                    && original_point.getCenterY() + squareSize > 0 && original_point.getCenterY() + squareSize < DrawingStage.screenHeight)
               {
                   Vertex t = new Vertex((int)original_point.getCenterX() - squareSize, (int)original_point.getCenterY() + squareSize, graph.nodeSize);
                   graph.updateVertex(i, t);
                   updateResults(graph, i);  // recompute distances of the moved node with the other nodes 
                   //numEvaluatedSolutions--;
                   newFit = objFunCost.cost; // compute current cost  
                   costDiff = newFit - oldFit; 
                   simAcceptance = Math.exp(-1*costDiff/Temp); // compute the value of e^-costDiff/Temp 
                   simRand = Math.random(); // generate a random number 
                   if ( (costDiff < 0) || (simAcceptance <= 1 && simRand < simAcceptance) )  // compare fitness of the node in its new and old positions
                   {   
                      temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                      temp_point.setCenterY(graph.points.get(i).getCenterY());   
                   }
                   else
                   {
                      graph.updateVertex(i, temp_point); 
                      updateResults(graph, i);  // recompute the distances of the node in its previous position 
                      //numEvaluatedSolutions--;
                   }
                }
            }
             // left location 
            else if (randNeighbour == 5)
            {
                if(original_point.getCenterX() - squareSize > 0 && original_point.getCenterX() - squareSize < DrawingStage.screenWidth)
                {
                   Vertex t = new Vertex((int)original_point.getCenterX() - squareSize, (int)original_point.getCenterY(), graph.nodeSize);
                   graph.updateVertex(i, t);
                   updateResults(graph, i);  // recompute distances of the moved node with the other nodes 
                   //numEvaluatedSolutions--;
                   newFit = objFunCost.cost; // compute current cost 
                   costDiff = newFit - oldFit; 
                   simAcceptance = Math.exp(-1*costDiff/Temp); // compute the value of e^-costDiff/Temp 
                   simRand = Math.random(); // generate a random number 
                   if ( (costDiff < 0) || (simAcceptance <= 1 && simRand < simAcceptance) )  // compare fitness of the node in its new and old positions
                   {   
                      temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                      temp_point.setCenterY(graph.points.get(i).getCenterY());   
                   }
                   else
                   {
                      graph.updateVertex(i, temp_point); 
                      updateResults(graph, i);  // recompute the distances of the node in its previous position 
                      //numEvaluatedSolutions--;
                   }
                }
            }
             // upper-left location 
             else if (randNeighbour == 6)
             {
                if(original_point.getCenterX() - squareSize > 0 && original_point.getCenterX() - squareSize < DrawingStage.screenWidth
                     && original_point.getCenterY() - squareSize > 0 && original_point.getCenterY() - squareSize < DrawingStage.screenHeight)
                {
                   Vertex t = new Vertex((int)original_point.getCenterX() - squareSize, (int)original_point.getCenterY() - squareSize, graph.nodeSize);
                   graph.updateVertex(i, t);
                   updateResults(graph, i);  // recompute distances of the moved node with the other nodes 
                   //numEvaluatedSolutions--;
                   newFit = objFunCost.cost; // compute current cost 
                   costDiff = newFit - oldFit; 
                   simAcceptance = Math.exp(-1*costDiff/Temp); // compute the value of e^-costDiff/Temp 
                   simRand = Math.random(); // generate a random number 
                   if ( (costDiff < 0) || (simAcceptance <= 1 && simRand < simAcceptance) )  // compare fitness of the node in its new and old positions
                   {   
                      temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                      temp_point.setCenterY(graph.points.get(i).getCenterY());   
                   }
                   else
                   {
                      graph.updateVertex(i, temp_point); 
                      updateResults(graph, i);  // recompute the distances of the node in its previous position 
                      //numEvaluatedSolutions--;
                   }
                }
             }
             // up location 
            else if (randNeighbour == 7)
            {
                if(original_point.getCenterY() - squareSize > 0 && original_point.getCenterY() - squareSize < DrawingStage.screenHeight)
                {
                   Vertex t = new Vertex((int)original_point.getCenterX(), (int)original_point.getCenterY() - squareSize, graph.nodeSize);
                   graph.updateVertex(i, t);
                   updateResults(graph, i);  // recompute distances of the moved node with the other nodes 
                   //numEvaluatedSolutions--;
                   newFit = objFunCost.cost; // compute current cost 
                   costDiff = newFit - oldFit; 
                   simAcceptance = Math.exp(-1*costDiff/Temp); // compute the value of e^-costDiff/Temp 
                   simRand = Math.random(); // generate a random number 
                   if ( (costDiff < 0) || (simAcceptance <= 1 && simRand < simAcceptance) )  // compare fitness of the node in its new and old positions
                   {   
                      temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                      temp_point.setCenterY(graph.points.get(i).getCenterY());   
                   }
                   else
                   {
                      graph.updateVertex(i, temp_point); 
                      updateResults(graph, i);  // recompute the distances of the node in its previous position 
                      //numEvaluatedSolutions--;
                   }
                }
            }
             //upper-right location 
            else if (randNeighbour == 8)
             {
                if(original_point.getCenterX() + squareSize > 0 && original_point.getCenterX() + squareSize < DrawingStage.screenWidth
                     && original_point.getCenterY() - squareSize > 0 && original_point.getCenterY() - squareSize < DrawingStage.screenHeight)
                {
                   Vertex t = new Vertex((int)original_point.getCenterX() + squareSize, (int)original_point.getCenterY() - squareSize, graph.nodeSize);
                   graph.updateVertex(i, t);
                   updateResults(graph, i);  // recompute distances of the moved node with the other nodes 
                   //numEvaluatedSolutions--;
                   newFit = objFunCost.cost; // compute current cost 
                   costDiff = newFit - oldFit; 
                   simAcceptance = Math.exp(-1*costDiff/Temp); // compute the value of e^-costDiff/Temp 
                   simRand = Math.random(); // generate a random number 
                   if ( (costDiff < 0) || (simAcceptance <= 1 && simRand < simAcceptance) )  // compare fitness of the node in its new and old positions
                   { 
                      temp_point.setCenterX(graph.points.get(i).getCenterX()); 
                      temp_point.setCenterY(graph.points.get(i).getCenterY());   
                   }
                   else
                   {
                      graph.updateVertex(i, temp_point); 
                      updateResults(graph, i);  // recompute the distances of the node in its previous position 
                      //numEvaluatedSolutions--;
                   }
                }
             }
            
      }
      updateResults(graph);
      //numEvaluatedSolutions ++; 
      return oldCost; 
    }
 }
   
