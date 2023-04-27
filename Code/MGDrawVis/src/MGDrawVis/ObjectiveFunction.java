
package MGDrawVis;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ObjectiveFunction 
{
    final static int numOfMeasures = 6; 
    double cost; 
    ArrayList<ArrayList<Double>> m; 
    ArrayList<Integer> weights;  
    ArrayList<Double> oldAvgMeasure; 
    ArrayList<Double> newAvgMeasure; 
    ArrayList<Double> oldSDMeasure; 
    ArrayList<Double> newSDMeasure; 
    ArrayList<Double> maxMeasureValues; 
    ArrayList<Double> minMeasureValues;
    ArrayList<Double> solutionVector;
    ArrayList<Double> normalizedVector;
    
    public ObjectiveFunction(Graph graph)
    {
        m = new ArrayList<ArrayList<Double>>(); 
        for (int i=0; i<numOfMeasures; i++)
            m.add(new ArrayList<Double>());
        weights = new ArrayList<Integer>(); 
        oldAvgMeasure = new ArrayList<Double>(); 
        newAvgMeasure = new ArrayList<Double>(); 
        oldSDMeasure = new ArrayList<Double>(); 
        newSDMeasure = new ArrayList<Double>(); 
        maxMeasureValues = new ArrayList<Double>(); 
        minMeasureValues = new ArrayList<Double>(); 
        solutionVector = new ArrayList<Double>();
        normalizedVector = new ArrayList<Double>(); 
        for(int i=0; i<numOfMeasures; i++)
        {
            oldAvgMeasure.add(0.0);
            newAvgMeasure.add(0.0); 
            oldSDMeasure.add(0.0);
            newSDMeasure.add(0.0); 
            maxMeasureValues.add(Double.MIN_VALUE);
            minMeasureValues.add(Double.MAX_VALUE); 
            weights.add(1);
        }
        cost = 0;
    }
    
    public void copyHistory(ObjectiveFunction obj)
    {
        for(int i=0; i<numOfMeasures; i++)
        {
            for(int j=0; j < obj.m.get(i).size(); j++)
                m.get(i).add(obj.m.get(i).get(j));
        }
        for(int i=0; i<numOfMeasures; i++)
        {
           oldAvgMeasure.set(i,obj.oldAvgMeasure.get(i));
           newAvgMeasure.set(i,obj.newAvgMeasure.get(i));
           oldSDMeasure.set(i,obj.oldSDMeasure.get(i));
           newSDMeasure.set(i,obj.newSDMeasure.get(i));
           maxMeasureValues.set(i,obj.maxMeasureValues.get(i));
           minMeasureValues.set(i,obj.minMeasureValues.get(i));
        }
        for(int i=0; i<obj.solutionVector.size(); i++)
        {
            solutionVector.add(obj.solutionVector.get(i));
            normalizedVector.add(obj.normalizedVector.get(i));
        }
    }
    
    public void Clear_Measures()
    {
       // clearing each measure 
       for (int i=0; i<m.size(); i++)
           m.get(i).clear();
       // create new list for each measure 
       for (int i=0; i<m.size(); i++)
           m.set(i, new ArrayList<Double>());
       solutionVector = new ArrayList<Double>();
       normalizedVector = new ArrayList<Double>(); 
       for(int i=0; i<numOfMeasures; i++)
       {
          oldAvgMeasure.set(i, 0.0);
          newAvgMeasure.set(i, 0.0); 
          oldSDMeasure.set(i, 0.0);
          newSDMeasure.set(i, 0.0); 
          maxMeasureValues.set(i, Double.MIN_VALUE);
          minMeasureValues.set(i, Double.MAX_VALUE);
       }
       cost = 0; 
    }
    
    public ArrayList<Double> calculateSolutionVector(Graph graph)
    {
       graph.computeDistances();
       ArrayList<Double> c = new ArrayList<Double>(); 
       for(int i=0; i<numOfMeasures; i++)
           c.add(0.0);
       solutionVector = new ArrayList<Double>(); // temp arraylist 
       normalizedVector = new ArrayList<Double>(); // store the normalized measures into vector 
       weights.set(0, Integer.parseInt(DrawingStage.rightbar.cont2NodeOcclusionWeight.getText()));  
       weights.set(1, Integer.parseInt(DrawingStage.rightbar.cont2EdgeLengthWeight.getText())); 
       weights.set(2, Integer.parseInt(DrawingStage.rightbar.cont2EdgeCrossingsWeight.getText())); 
       weights.set(3, Integer.parseInt(DrawingStage.rightbar.cont2NodeEdgeOcclusionWeight.getText())); 
       weights.set(4, Integer.parseInt(DrawingStage.rightbar.cont2AngularResolutionWeight.getText()));
       weights.set(5, Integer.parseInt(DrawingStage.rightbar.cont2ScreenCenterWeight.getText())); 
       // compute the current cost function according to all measures
       for(int i=0; i<graph.points.size(); i++)
       {
          if (weights.get(0) > 0) 
            c.set(0, c.get(0)+ measure1(graph, i));   
          if (weights.get(1) > 0) 
            c.set(1, c.get(1)+ measure2(graph, i));  
          if (weights.get(2) > 0) 
            c.set(2, c.get(2)+ measure3(graph, i));  
          if (weights.get(3) > 0) 
            c.set(3, c.get(3)+ measure4(graph, i));  
          if (weights.get(4) > 0) 
            c.set(4, c.get(4)+ measure5(graph, i)); 
          if (weights.get(5) > 0) 
            c.set(5, c.get(5)+ measure6(graph, i)); 
       }
       // values are divided by specific values because during computing these values some computations have been repreated 
       // add measures to temp arraylist to be sent to equalizescales 
       solutionVector.add(c.get(0)/2);
       solutionVector.add(c.get(1)/2);
       solutionVector.add(c.get(2)/8);
       solutionVector.add(c.get(3)/2);
       solutionVector.add(c.get(4)/3);
       solutionVector.add(c.get(5)/1);
       // equalize scales of the measures and saves them back in norm vector 
       normalizedVector.addAll(EqualizeScales(solutionVector)); 
       return solutionVector;    // return normalized vector  
    }
    
    public ArrayList<Double> calculateSolutionVector(Graph graph, int p) // This function computes the fitness (cost) for a specific node p  
    {
       graph.computeDistances(p);
       ArrayList<Double> c = new ArrayList<Double>(); 
       for(int i=0; i<numOfMeasures; i++)
           c.add(0.0);
       solutionVector = new ArrayList<Double>(); // temp arraylist 
       normalizedVector = new ArrayList<Double>(); // store the normalized measures into vector 
       weights.set(0, Integer.parseInt(DrawingStage.rightbar.cont2NodeOcclusionWeight.getText()));  
       weights.set(1, Integer.parseInt(DrawingStage.rightbar.cont2EdgeLengthWeight.getText())); 
       weights.set(2, Integer.parseInt(DrawingStage.rightbar.cont2EdgeCrossingsWeight.getText())); 
       weights.set(3, Integer.parseInt(DrawingStage.rightbar.cont2NodeEdgeOcclusionWeight.getText())); 
       weights.set(4, Integer.parseInt(DrawingStage.rightbar.cont2AngularResolutionWeight.getText()));
       weights.set(5, Integer.parseInt(DrawingStage.rightbar.cont2ScreenCenterWeight.getText()));
       // compute the current cost function according to all measures for the node p 
       if (weights.get(0) > 0) 
          c.set(0, c.get(0)+ measure1(graph, p));   
       if (weights.get(1) > 0) 
          c.set(1, c.get(1)+ measure2(graph, p));  
       if (weights.get(2) > 0) 
          c.set(2, c.get(2)+ measure3(graph, p));  
       if (weights.get(3) > 0) 
          c.set(3, c.get(3)+ measure4(graph, p));  
       if (weights.get(4) > 0) 
          c.set(4, c.get(4)+ measure5(graph, p)); 
       if (weights.get(5) > 0) 
          c.set(5, c.get(5)+ measure6(graph, p)); 
     
       // values are divided by specific values because during computing these values some computations have been repreated 
       // add measures to temp arraylist to be sent to equalizescales 
       solutionVector.add(c.get(0)/2);
       solutionVector.add(c.get(1)/2);
       solutionVector.add(c.get(2)/8);
       solutionVector.add(c.get(3)/2);
       solutionVector.add(c.get(4)/3);
       solutionVector.add(c.get(5)/1);
       // equalize scales of the measures and saves them back in norm vector 
       normalizedVector.addAll(EqualizeScales(solutionVector)); 
       return solutionVector;    // return normalized vector    
    }
    
     // This method takes two vectors of current (better) and previous (worse) measures at specific points and updates the solution vector by subtracting the previous and adding the current 
    public void UpdateSolutionVector(ArrayList<Double> oldV, ArrayList<Double> newV)
    {
        ArrayList<Double> temp = new ArrayList<Double> (); // temp array to clone the solution vector and then will be saved back in the solution vector
        for (int i=0; i<solutionVector.size(); i++)
        {
           if(solutionVector.get(i).doubleValue() > 0)
               temp.add(solutionVector.get(i).doubleValue() - oldV.get(i).doubleValue() + newV.get(i).doubleValue()); // update the vector 
           else
               temp.add(0.0);
        }
        // update solution vector 
        solutionVector.clear();
        solutionVector.addAll(temp);
        normalizedVector.clear();
        normalizedVector.addAll(EqualizeScales(solutionVector));
    }
    
    // This method takes an arraylist of solution vector and finds the weighted sum of the measures in the vector by multiplying them by their user defined weights 
    public double ComputeCost()
    {
       double sum = 0; 
       // add the weights to the array 
       weights.set(0, Integer.parseInt(DrawingStage.rightbar.cont2NodeOcclusionWeight.getText()));  
       weights.set(1, Integer.parseInt(DrawingStage.rightbar.cont2EdgeLengthWeight.getText())); 
       weights.set(2, Integer.parseInt(DrawingStage.rightbar.cont2EdgeCrossingsWeight.getText())); 
       weights.set(3, Integer.parseInt(DrawingStage.rightbar.cont2NodeEdgeOcclusionWeight.getText())); 
       weights.set(4, Integer.parseInt(DrawingStage.rightbar.cont2AngularResolutionWeight.getText()));
       weights.set(5, Integer.parseInt(DrawingStage.rightbar.cont2ScreenCenterWeight.getText()));
       // compute the weighted sum 
       
       // this part is for harmonic mean  
       double totalWeights = 0; 
       double totalWeightOverMetric = 0; 
       for (int i=0; i<normalizedVector.size(); i++)
       {
           if (weights.get(i).doubleValue() > 0)
           {
              totalWeights += weights.get(i).doubleValue(); 
              if (normalizedVector.get(i).doubleValue() > 0)
                 totalWeightOverMetric += (weights.get(i).doubleValue() / normalizedVector.get(i).doubleValue());
           }
       }
       /*
       if (totalWeightOverMetric > 0)
          sum = totalWeights / totalWeightOverMetric;
       else sum = 0; 
       double harmonicMean = sum; 
       cost = harmonicMean; 
       return harmonicMean;
       // end of harmonic mean 
       */
       // this part is for normal mean (old implementation)
       
       
       for(int i=0; i<normalizedVector.size(); i++)
          sum += (weights.get(i).doubleValue() * normalizedVector.get(i).doubleValue()); 
       
       cost = sum; 
       return sum; 
       
       
    }
    
    public ArrayList<Double> EqualizeScales(ArrayList<Double> a)
    {
       ArrayList<Double> temp = new ArrayList<Double> (); 
       ArrayList<Double> c = new ArrayList<Double>(); 
       for(int i=0; i<numOfMeasures; i++)
           c.add(0.0);
       // Normalize each measure with its normalization method and save each into a variable 
       c.set(0, Norm_M1(a.get(0).doubleValue()));
       c.set(1, Norm_M2(a.get(1).doubleValue()));
       c.set(2, Norm_M3(a.get(2).doubleValue()));
       c.set(3, Norm_M4(a.get(3).doubleValue()));
       c.set(4, Norm_M5(a.get(4).doubleValue()));
       c.set(5, Norm_M6(a.get(5).doubleValue()));
       // store the normalized measures into a vector 
       for(int i=0; i<c.size(); i++)
           temp.add(c.get(i));     
       // return the normalized vector of measures 
       return temp; 
    }
    
    /*******************************************************************************/
    
    // Compute the mean (average) from previous value of the average given the new added value, the previous average, and number of elements in the list (array).  
    private double Avg_measures(double measure, double oldAvg, int num)
    {
       double newAvg; 
       if (num == 0)
         newAvg = 0.0; 
       else 
          if (num == 1)
             newAvg = measure; 
          else 
             newAvg = oldAvg + ((measure-oldAvg)/num); 
       return newAvg; 
    }
 
    // Compute the standard deviation of array using old value of standard deviation given the new added value, the value of previous standard deviation, 
    // the value of previous average, the value of the current measure, and the number of elements in the array (list) 
    private double SD_measures (double measure, double oldSD, double oldAvg, double newAvg, int num)
    {
       double SD; 
       if (num == 0 || num == 1)
         SD = 0.0; 
       else
         SD = Math.sqrt((oldSD * oldSD) + ((measure-oldAvg)*(measure-newAvg)));
       return SD; 
    }
 
    // compute and save current max and min for measure 1 values (old max and min are compared to parameter m) 
    private void maxmin_m1 (double m1)
    {
       if(m.get(0).size() == 0 || m.get(0).size() == 1 || m.get(0).size() == 2) // if no or one value only in the array of measure 1 
       {
           maxMeasureValues.set(0, m1);  
           minMeasureValues.set(0, m1);   
       }
       else
       {
          if(m1 > maxMeasureValues.get(0))
             maxMeasureValues.set(0, m1);
          if(m1 < minMeasureValues.get(0))
             minMeasureValues.set(0, m1);
       }
    }
    
    // normalize the value of m by subtracting the current average (mean) of all the current values and divide them by their standard deviation 
    // then the normalized value subtracts the minimum value and divided by the difference between max and min values to get a value between 0 and 1 
    private double Norm_M1(double m1)
    {
       double max, min; 
       double mNorm;
       m.get(0).add(m1);
       if (m.get(0).size() == 1 || m.get(0).size() == 2) 
       {
           oldAvgMeasure.set(0, m1);
           newAvgMeasure.set(0, m1);
           oldSDMeasure.set(0, 0.0);
           newSDMeasure.set(0, 0.0);
       }
       else
       {
           newAvgMeasure.set(0, Avg_measures(m1, oldAvgMeasure.get(0), m.get(0).size()-1));
           newSDMeasure.set(0, SD_measures(m1, oldSDMeasure.get(0), oldAvgMeasure.get(0), newAvgMeasure.get(0), m.get(0).size()-1)); 
           oldAvgMeasure.set(0, newAvgMeasure.get(0));
           oldSDMeasure.set(0, newSDMeasure.get(0)); 
       }
       maxmin_m1(m1);
       if (newSDMeasure.get(0) == 0) 
         mNorm = m1;  
       else
       {
          mNorm = (m1 - newAvgMeasure.get(0)) / newSDMeasure.get(0); // value 
          min = (minMeasureValues.get(0) - newAvgMeasure.get(0)) / newSDMeasure.get(0); // min 
          max = (maxMeasureValues.get(0) - newAvgMeasure.get(0)) / newSDMeasure.get(0); // max 
          mNorm = (mNorm - min) / (max - min); // normalized 
       }
       return mNorm;    
    }
 
    // compute and save current max and min for measure 2 values (old max and min are compared to parameter m) 
    private void maxmin_m2 (double m2)
    {
       if(m.get(1).size() == 0 || m.get(1).size() == 1 || m.get(1).size() == 2) // if no or one value only in the array of measure 2 
       {
         maxMeasureValues.set(1, m2); 
         minMeasureValues.set(1, m2); 
       }
       else
       {
         if(m2 > maxMeasureValues.get(1))
             maxMeasureValues.set(1, m2); 
         if(m2 < minMeasureValues.get(1))
             minMeasureValues.set(1, m2); 
       }
    }
 
    // normalize the value of m by subtracting the current average (mean) of all the current values and divide them by their standard deviation 
    // then the normalized value subtracts the minimum value and divided by the difference between max and min values to get a value between 0 and 1 
    private double Norm_M2(double m2)
    {
       double max, min; 
       double mNorm;  
       m.get(1).add(m2); 
       if (m.get(1).size() == 1 || m.get(1).size() == 2) 
       {
           oldAvgMeasure.set(1, m2);
           newAvgMeasure.set(1, m2);
           oldSDMeasure.set(1, 0.0);
           newSDMeasure.set(1, 0.0);
       }
       else
       {
           newAvgMeasure.set(1, Avg_measures(m2, oldAvgMeasure.get(1), m.get(1).size()-1));
           newSDMeasure.set(1, SD_measures(m2, oldSDMeasure.get(1), oldAvgMeasure.get(1), newAvgMeasure.get(1), m.get(1).size()-1)); 
           oldAvgMeasure.set(1, newAvgMeasure.get(1));
           oldSDMeasure.set(1, newSDMeasure.get(1)); 
     }
     maxmin_m2(m2);
     if (newSDMeasure.get(1) == 0) 
         mNorm = m2;  
     else
     {
        mNorm = (m2 - newAvgMeasure.get(1)) / newSDMeasure.get(1); // value 
        min = (minMeasureValues.get(1) - newAvgMeasure.get(1)) / newSDMeasure.get(1); // min 
        max = (maxMeasureValues.get(1) - newAvgMeasure.get(1)) / newSDMeasure.get(1); // max 
        mNorm = (mNorm - min) / (max - min); // normalized 
     }
     return mNorm;    
   }
  
   // normalize the value of m by dividing m by the maximum possible number of edges ((numOfEdges() * numOfEdges()-1) / 2);
   private double Norm_M3(double m3)
   {
      double mNorm; 
      double e; 
      e = Double.parseDouble(DrawingStage.rightbar.cont1EdgesValue.getText());
      if(e > 1)
         mNorm = m3 / ((e * (e-1)) / 2);
      else 
         mNorm = 0; 
      return mNorm;
   }
 
   // compute and save current max and min for measure 4 values (old max and min are compared to parameter m) 
   private void maxmin_m4 (double m4)
   {
       if(m.get(3).size() == 0 || m.get(3).size() == 1 || m.get(3).size() == 2) // if no or one value only in the array of measure 2 
       {
         maxMeasureValues.set(3, m4); 
         minMeasureValues.set(3, m4); 
       }
       else
       {
         if(m4 > maxMeasureValues.get(3))
             maxMeasureValues.set(3, m4); 
         if(m4 < minMeasureValues.get(3))
             minMeasureValues.set(3, m4); 
       }
    }
 
    // normalize the value of m by subtracting the current average (mean) of all the current values and divide them by their standard deviation 
    // then the normalized value subtracts the minimum value and divided by the difference between max and min values to get a value between 0 and 1 
    private double Norm_M4(double m4)
    {
       double max, min; 
       double mNorm;  
       m.get(3).add(m4); 
       if (m.get(3).size() == 1 || m.get(3).size() == 2) 
       {
           oldAvgMeasure.set(3, m4);
           newAvgMeasure.set(3, m4);
           oldSDMeasure.set(3, 0.0);
           newSDMeasure.set(3, 0.0);
       }
       else
       {
           newAvgMeasure.set(3, Avg_measures(m4, oldAvgMeasure.get(3), m.get(3).size()-1));
           newSDMeasure.set(3, SD_measures(m4, oldSDMeasure.get(3), oldAvgMeasure.get(3), newAvgMeasure.get(3), m.get(3).size()-1)); 
           oldAvgMeasure.set(3, newAvgMeasure.get(3));
           oldSDMeasure.set(3, newSDMeasure.get(3)); 
       }
       maxmin_m4(m4);
       if (newSDMeasure.get(3) == 0) 
          mNorm = m4;  
       else
       {
          mNorm = (m4 - newAvgMeasure.get(3)) / newSDMeasure.get(3); // value 
          min = (minMeasureValues.get(3) - newAvgMeasure.get(3)) / newSDMeasure.get(3); // min 
          max = (maxMeasureValues.get(3) - newAvgMeasure.get(3)) / newSDMeasure.get(3); // max 
          mNorm = (mNorm - min) / (max - min); // normalized 
       }
       return mNorm;  
    }
 
    // compute and save current max and min for measure 5 values (old max and min are compared to parameter m) 
    private void maxmin_m5 (double m5)
    {
       if(m.get(4).size() == 0 || m.get(4).size() == 1 || m.get(4).size() == 2) // if no or one value only in the array of measure 2 
       {
         maxMeasureValues.set(4, m5); 
         minMeasureValues.set(4, m5); 
       }
       else
       {
         if(m5 > maxMeasureValues.get(4))
             maxMeasureValues.set(4, m5); 
         if(m5 < minMeasureValues.get(4))
             minMeasureValues.set(4, m5); 
       }
    }
 
    // normalize the value of m by subtracting the current average (mean) of all the current values and divide them by their standard deviation 
    // then the normalized value subtracts the minimum value and divided by the difference between max and min values to get a value between 0 and 1 
    private double Norm_M5(double m5)
    {
       double max, min; 
       double mNorm;  
       m.get(4).add(m5); 
       if (m.get(4).size() == 1 || m.get(4).size() == 2) 
       {
           oldAvgMeasure.set(4, m5);
           newAvgMeasure.set(4, m5);
           oldSDMeasure.set(4, 0.0);
           newSDMeasure.set(4, 0.0);
       }
       else
       {
           newAvgMeasure.set(4, Avg_measures(m5, oldAvgMeasure.get(4), m.get(4).size()-1));
           newSDMeasure.set(4, SD_measures(m5, oldSDMeasure.get(4), oldAvgMeasure.get(4), newAvgMeasure.get(4), m.get(4).size()-1)); 
           oldAvgMeasure.set(4, newAvgMeasure.get(4));
           oldSDMeasure.set(4, newSDMeasure.get(4)); 
       }
       maxmin_m5(m5);
       if (newSDMeasure.get(4) == 0) 
          mNorm = m5;  
       else
       {
          mNorm = (m5 - newAvgMeasure.get(4)) / newSDMeasure.get(4); // value 
          min = (minMeasureValues.get(4) - newAvgMeasure.get(4)) / newSDMeasure.get(4); // min 
          max = (maxMeasureValues.get(4) - newAvgMeasure.get(4)) / newSDMeasure.get(4); // max 
          mNorm = (mNorm - min) / (max - min); // normalized 
       }
       return mNorm;      
    }
    
    // compute and save current max and min for measure 6 values (old max and min are compared to parameter m) 
    private void maxmin_m6 (double m6)
    {
       if(m.get(5).size() == 0 || m.get(5).size() == 1 || m.get(5).size() == 2) // if no or one value only in the array of measure 1 
       {
           maxMeasureValues.set(5, m6);  
           minMeasureValues.set(5, m6);   
       }
       else
       {
          if(m6 > maxMeasureValues.get(5))
             maxMeasureValues.set(5, m6);
          if(m6 < minMeasureValues.get(5))
             minMeasureValues.set(5, m6);
       }
    }
    
    // normalize the value of m by subtracting the current average (mean) of all the current values and divide them by their standard deviation 
    // then the normalized value subtracts the minimum value and divided by the difference between max and min values to get a value between 0 and 1 
    private double Norm_M6(double m6)
    {
       double max, min; 
       double mNorm;
       m.get(5).add(m6);
       if (m.get(5).size() == 1 || m.get(5).size() == 2) 
       {
           oldAvgMeasure.set(5, m6);
           newAvgMeasure.set(5, m6);
           oldSDMeasure.set(5, 0.0);
           newSDMeasure.set(5, 0.0);
       }
       else
       {
           newAvgMeasure.set(5, Avg_measures(m6, oldAvgMeasure.get(5), m.get(5).size()-1));
           newSDMeasure.set(5, SD_measures(m6, oldSDMeasure.get(5), oldAvgMeasure.get(5), newAvgMeasure.get(5), m.get(5).size()-1)); 
           oldAvgMeasure.set(5, newAvgMeasure.get(5));
           oldSDMeasure.set(5, newSDMeasure.get(5)); 
       }
       maxmin_m6(m6);
       if (newSDMeasure.get(5) == 0) 
         mNorm = m6;  
       else
       {
          mNorm = (m6 - newAvgMeasure.get(5)) / newSDMeasure.get(5); // value 
          min = (minMeasureValues.get(5) - newAvgMeasure.get(5)) / newSDMeasure.get(5); // min 
          max = (maxMeasureValues.get(5) - newAvgMeasure.get(5)) / newSDMeasure.get(5); // max 
          mNorm = (mNorm - min) / (max - min); // normalized 
       }
       return mNorm;    
    }
    
    private double measure1(Graph graph, int p)  // nodes distribution criterion for specific node p 
    {
       // Compute the cost function according to nodes distribution criterion (sum of inverse propotional to distance squared)
       int node_threshold = Integer.parseInt(DrawingStage.rightbar.cont3DesiredNodeOcclusionValue.getText()); // to test if the distance between nodes is less than this value or not. 
       double c = 0;  // to compute cost of this criterion then will be added to the overall cost
       double d; // distance 
       for(int i=0; i<graph.points.size(); i++)
       { 
           if(i != p) // do not count the distance from the node to itself
           {
              d = graph.distances.get(p).get(i);
              c += (((d-node_threshold) * (d-node_threshold)));
              //if (d < node_threshold && d != 0)     // to check the distances between nodes which are less than the threshold 
              //   c += 1.0 / (d*d);              // compute the cost function
           }
      }
      return (c); // we don't divide by 2 here because we are just computing the distances from node p to every other node 
    }
 
    private double measure2(Graph graph, int p)  // edges length criterion (all edges incident to node p only)
    {
       // Compute the cost function according to edges length criterion (sum of (ei-required_distance)^2 divided by 2 since every edges is counted twice)
       double required_length = Double.parseDouble(DrawingStage.rightbar.cont3DesiredEdgeLengthValue.getText());; // required edge length  
       double e;// for edge length
       double c = 0;  // to compute cost of this criterion then will be added to the overall cost
       for(int i=0; i<graph.adjacency.get(p).size(); i++) // for all nodes adjacent to node p 
       {
           e = graph.distances.get(p).get(graph.adjacency.get(p).get(i));     //get length of the edge from the array distances 
           c += (((e-required_length) * (e-required_length)));      // Sum((edge length - required distance)^2) 
       }
       return (c); // we do not divide by 2 here becuase we only compute the length of every edge incident to node p 
    }
 
    private int measure3(Graph graph, int p) // lines intersections criterion (just checks the lines that node p is one of their end points) 
    {
       // This method computes number of intesections between lines 
       // It goes through the adjacency list and creates line for each edge 
       int intersections = 0; 
       for(int i=0; i<graph.adjacency.get(p).size(); i++) // creates lines for edges whose end point is point p
       {
           Line line1 = new Line(graph.points.get(p).getCenterX(), graph.points.get(p).getCenterY() , graph.points.get(graph.adjacency.get(p).get(i)).getCenterX(), graph.points.get(graph.adjacency.get(p).get(i)).getCenterY()); // create line 1
           for(int m=0; m<graph.points.size(); m++)
           {
              for(int n=0; n<graph.adjacency.get(m).size(); n++) // creates lines for edges whose end point is m (but not p)
              {
                  if(m!=p && m!=graph.adjacency.get(p).get(i) && graph.adjacency.get(m).get(n)!=p && graph.adjacency.get(m).get(n)!= graph.adjacency.get(p).get(i)) // if there is no common point between line 1 and the points of line 2
                  {
                        Line line2 = new Line(graph.points.get(m).getCenterX(), graph.points.get(m).getCenterY() , graph.points.get(graph.adjacency.get(m).get(n)).getCenterX(), graph.points.get(graph.adjacency.get(m).get(n)).getCenterY()); // create line 2 
                        if(doIntersect(line1, line2)) // if two lines intersect
                            intersections++;  // count intersections
                  }
              }
           }
        }
        return intersections; 
    }
    // Given three colinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    private boolean onSegment(Circle p, Circle q, Circle r)
    {
       if (q.getCenterX() <= Math.max(p.getCenterX(), r.getCenterX()) && q.getCenterX() >= Math.min(p.getCenterX(), r.getCenterX()) &&
        q.getCenterY() <= Math.max(p.getCenterY(), r.getCenterY()) && q.getCenterY() >= Math.min(p.getCenterY(), r.getCenterY()))
          return true;
       return false;
    }
 
    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    private int orientation(Circle p, Circle q, Circle r)
    {
       int val = (int)((q.getCenterY() - p.getCenterY()) * (r.getCenterX() - q.getCenterX()) - (q.getCenterX() - p.getCenterX()) * (r.getCenterY() - q.getCenterY()));
       if (val == 0) 
           return 0;  // colinear
       return (val > 0)? 1: 2; // clock or counterclock wise
    }
    
   // The main function that returns true if line segment 'p1q1'
   // and 'p2q2' intersect.
   private boolean doIntersect(Line line1, Line line2)
   {
       Circle p1 = new Circle(line1.getStartX(),line1.getStartY(), 10);
       Circle q1 = new Circle(line1.getEndX(),line1.getEndY(), 10);
       Circle p2 = new Circle(line2.getStartX(),line2.getStartY(), 10);
       Circle q2 = new Circle(line2.getEndX(),line2.getEndY(), 10);
       // Find the four orientations needed for general and
       // special cases
       int o1 = orientation(p1, q1, p2);
       int o2 = orientation(p1, q1, q2);
       int o3 = orientation(p2, q2, p1);
       int o4 = orientation(p2, q2, q1);
       // General case
       if (o1 != o2 && o3 != o4)
           return true;
       // Special Cases
       // p1, q1 and p2 are colinear and p2 lies on segment p1q1
       if (o1 == 0 && onSegment(p1, p2, q1)) return true;
       // p1, q1 and q2 are colinear and q2 lies on segment p1q1
       if (o2 == 0 && onSegment(p1, q2, q1)) return true;
       // p2, q2 and p1 are colinear and p1 lies on segment p2q2
       if (o3 == 0 && onSegment(p2, p1, q2)) return true;
       // p2, q2 and q1 are colinear and q1 lies on segment p2q2
       if (o4 == 0 && onSegment(p2, q1, q2)) return true;
       return false; // Doesn't fall in any of the above cases
    } 
   
    private double measure4(Graph graph, int p) // nodes-edges occlusion (checks only the edges which are affected by node p)
    {
        int x1, x2, y1, y2; // needed for computing the angles
        double degree1, degree2; // needed for computing the angles
        // This method computes nodes-edges occlusion criterion (for node p) 
        int occlusion_threshold = Integer.parseInt(DrawingStage.rightbar.cont3DesiredNodeEdgeOcclusionValue.getText()); // to test if the distance between a node and an edge is less than this value or not 
        double c1 = 0; // to compute the cost of distances from other nodes to each edge whose one of its end points is p 
        double c2 = 0; // to compute the cost of distances from the node p to all other edges 
        double c = 0;  // sum of c1 and c2 
        double d;  // to computer the distance between the node and the edge 
        // to compute the cost of distances from other nodes to each edge whose one of its end points is p 
        for(int i=0; i<graph.adjacency.get(p).size(); i++) // creates lines for edges whose one of its end points is node p
        {
           Line2D.Double line1 = new Line2D.Double(graph.points.get(p).getCenterX(), graph.points.get(p).getCenterY() , graph.points.get(graph.adjacency.get(p).get(i)).getCenterX(), graph.points.get(graph.adjacency.get(p).get(i)).getCenterY()); // create line 1
           for(int m=0; m<graph.points.size(); m++) // to go through all the nodes 
           {
              if ((m != p) && (m != graph.adjacency.get(p).get(i))) // if the node m is not any of the end points of the line 
              {
                  // if the point points.get(m) is within the plane of the line "angles between the point m and the end points of the line are less than or equal to 90" then we compute the distance
                  x1 = (int)(graph.points.get(m).getCenterX() - graph.points.get(p).getCenterX()); 
                  y1 = (int)(graph.points.get(m).getCenterY() - graph.points.get(p).getCenterY());
                  x2 = (int)(graph.points.get(graph.adjacency.get(p).get(i)).getCenterX() - graph.points.get(p).getCenterX()); 
                  y2 = (int)(graph.points.get(graph.adjacency.get(p).get(i)).getCenterY() - graph.points.get(p).getCenterY());
                  degree1 = Math.acos(((x1 * x2) + (y1 * y2))/(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2))); // compute first angle in radian
                  degree1 = Math.toDegrees(degree1); // first angle in degrees 
                  x1 = (int)(graph.points.get(m).getCenterX() - graph.points.get(graph.adjacency.get(p).get(i)).getCenterX()); 
                  y1 = (int)(graph.points.get(m).getCenterY() - graph.points.get(graph.adjacency.get(p).get(i)).getCenterY());
                  x2 = (int)(graph.points.get(p).getCenterX() - graph.points.get(graph.adjacency.get(p).get(i)).getCenterX()); 
                  y2 = (int)(graph.points.get(p).getCenterY() - graph.points.get(graph.adjacency.get(p).get(i)).getCenterY());
                  degree2 = Math.acos(((x1 * x2) + (y1 * y2))/(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2))); // compute second angle in radian
                  degree2 = Math.toDegrees(degree2); // second angle in degrees 
                  if(degree1 <= 90 && degree2 <= 90) // if the node is within the plane of the line 
                  {
                      d = line1.ptLineDist(graph.points.get(m).getCenterX(), graph.points.get(m).getCenterY()); // compute the distance from point m to the line
                      if (d == 0)  // in case the point is on the line, and to avoid dividing by zero, we will make the distance 1  
                          d = 1;
                      c1 += (((d-occlusion_threshold) * (d-occlusion_threshold)));
                      //if (d < occlusion_threshold )
                      //   c1 += 1.0 / (d*d); // add the inverse proportional of the square of the distance to the cost function 
                  }
               }  
           }
        }
        // to compute the cost of distances from the node p to all other edges
        for(int i=0; i<graph.points.size(); i++)  // go through all the points 
        {
           if (i != p) // check distances from node p to all othet edges whose non of their endpoints is node p
           for(int j=0; j<graph.adjacency.get(i).size(); j++) // creates lines for edges whose end point is point i
           {
              Line2D.Double line1 = new Line2D.Double(graph.points.get(i).getCenterX(), graph.points.get(i).getCenterY() , graph.points.get(graph.adjacency.get(i).get(j)).getCenterX(), graph.points.get(graph.adjacency.get(i).get(j)).getCenterY()); // create line 
              if ( (p != graph.adjacency.get(i).get(j))) // if the node p is not any of the end points of the line 
              {
                   // if the point points.get(p) is within the plane of the line "angles between the point m and the end points of the line are less than or equal to 90" then we compute the distance
                   x1 = (int)(graph.points.get(p).getCenterX() - graph.points.get(i).getCenterX()); 
                   y1 = (int)(graph.points.get(p).getCenterY() - graph.points.get(i).getCenterY());
                   x2 = (int)(graph.points.get(graph.adjacency.get(i).get(j)).getCenterX() - graph.points.get(i).getCenterX()); 
                   y2 = (int)(graph.points.get(graph.adjacency.get(i).get(j)).getCenterY() - graph.points.get(i).getCenterY());
                   degree1 = Math.acos(((x1 * x2) + (y1 * y2))/(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2))); // compute first angle in radian
                   degree1 = Math.toDegrees(degree1); // first angle in degrees 
                   x1 = (int)(graph.points.get(p).getCenterX() - graph.points.get(graph.adjacency.get(i).get(j)).getCenterX()); 
                   y1 = (int)(graph.points.get(p).getCenterY() - graph.points.get(graph.adjacency.get(i).get(j)).getCenterY());
                   x2 = (int)(graph.points.get(i).getCenterX() - graph.points.get(graph.adjacency.get(i).get(j)).getCenterX()); 
                   y2 = (int)(graph.points.get(i).getCenterY() - graph.points.get(graph.adjacency.get(i).get(j)).getCenterY());
                   degree2 = Math.acos(((x1 * x2) + (y1 * y2))/(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2))); // compute second angle in radian
                   degree2 = Math.toDegrees(degree2); // second angle in degrees 
                   if(degree1 <= 90 && degree2 <= 90) // if the node is within the plane of the line 
                   {
                       d = line1.ptLineDist(graph.points.get(p).getCenterX(), graph.points.get(p).getCenterY()); // compute the distance from point p to the line
                       if (d == 0)  // in case the point is on the line, and to avoid dividing by zero, we will make the distance 1  
                           d = 1;
                       c2 += (((d-occlusion_threshold) * (d-occlusion_threshold)));
                       //if (d < occlusion_threshold )
                      //    c2 += 1.0 / (d*d); // add the inverse proportional of the square of the distance to the cost function 
                   }
                }  
           }
        }
        c2 = c2 / 2; // this cost is divided by 2 because distance from node p to edges has been computed twice "with edges [i,j] and [j,i] 
        c = c1 + c2;  // add both costs 
        return (c);
    }

    private double measure5(Graph graph, int p) // Angular Resolution for all angles which are affected by node p
    {
       // This method computes angular resolution criterion 
       int angle_threshold = Integer.parseInt(DrawingStage.rightbar.cont3DesiredAngularResolutionValue.getText()); // Threshold value to test the angles which are below this value only 
       double c = 0; // to compute the cost of this criterion
       int x1, x2, y1, y2; // used for computing the slopes of the lines 
       double degree, radian; // stores the degree between two lines 
       for(int i=0; i<graph.adjacency.get(p).size(); i++)  // for the first line (p,i)
       {
           // the following checks the angles whose common node is p 
           for(int j=i+1; j<graph.adjacency.get(p).size(); j++)  // for the second line sharing the same point p as for the first line (p,j)
           {
              x1 = (int)(graph.points.get(graph.adjacency.get(p).get(i)).getCenterX() - graph.points.get(p).getCenterX()); // x coordinate difference in line 1 
              y1 = (int)(graph.points.get(graph.adjacency.get(p).get(i)).getCenterY() - graph.points.get(p).getCenterY()); // y coordinate difference in line 1
              x2 = (int)(graph.points.get(graph.adjacency.get(p).get(j)).getCenterX() - graph.points.get(p).getCenterX()); // x coordinate difference in line 2
              y2 = (int)(graph.points.get(graph.adjacency.get(p).get(j)).getCenterY() - graph.points.get(p).getCenterY()); // y coordinate difference in line 2
              degree = Math.acos(((x1 * x2) + (y1 * y2))/(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2))); // compute the angle in radian 
              degree = Math.toDegrees(degree); // compute the angle in degrees 
              if (degree < angle_threshold)  // check only the angles which are less than the given threshold 
              {
                 radian = Math.toRadians(degree); // convert the degree to radian 
                 c += Math.abs(((2*Math.PI)/graph.adjacency.get(p).size())-radian); // add the following to the total cost: (((2*PI)/degree of the point p) - degree in radian between line1 and line2)
              }
           }
           // the following checks the angles whose common node is the node adjacent to p (not p itself)
           for(int j=0; j<graph.adjacency.get(graph.adjacency.get(p).get(i)).size(); j++)
           {
                if(graph.adjacency.get(graph.adjacency.get(p).get(i)).get(j) != p) 
                {
                    x1 = (int)(graph.points.get(p).getCenterX() - graph.points.get(graph.adjacency.get(p).get(i)).getCenterX()); // x coordinate difference in line 1 
                    y1 = (int)(graph.points.get(p).getCenterY() - graph.points.get(graph.adjacency.get(p).get(i)).getCenterY()); // y coordinate difference in line 1
                    x2 = (int)(graph.points.get(graph.adjacency.get(graph.adjacency.get(p).get(i)).get(j)).getCenterX() - graph.points.get(graph.adjacency.get(p).get(i)).getCenterX()); // x coordinate difference in line 2
                    y2 = (int)(graph.points.get(graph.adjacency.get(graph.adjacency.get(p).get(i)).get(j)).getCenterY() - graph.points.get(graph.adjacency.get(p).get(i)).getCenterY()); // y coordinate difference in line 2
                    degree = Math.acos(((x1 * x2) + (y1 * y2))/(Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2))); // compute the angle in radian 
                    degree = Math.toDegrees(degree); // compute the angle in degrees 
                    if (degree < angle_threshold)  // check only the angles which are less than the given threshold 
                    {
                       radian = Math.toRadians(degree); // convert the degree to radian 
                       c += Math.abs(((2*Math.PI)/graph.adjacency.get(graph.adjacency.get(p).get(i)).size())-radian); // add the following to the total cost: (((2*PI)/degree of the point i adjacent to p) - degree in radian between line1 and line2)
                    }
                }
            }
        }
        return c; 
    }
    
    private double measure6(Graph graph, int p)  // nodes distribution criterion for specific node p 
    {
        Bounds bounds = DrawingStage.screen.getBoundsInLocal();
        Bounds screenBounds = DrawingStage.screen.localToScene(bounds);
        int x = (int) screenBounds.getMinX();
        int y = (int) screenBounds.getMinY();
        int width = (int) screenBounds.getWidth()/2;
        int height = (int) screenBounds.getHeight()/2;
        double c = 0;  // to compute cost of this criterion then will be added to the overall cost
        double d; // distance 
        for(int i=0; i<graph.points.size(); i++)
        {  
            d = distanceToCenter(graph.points.get(i), x+width, y+height);
            if (d != 0)     
              c += (d*d);              // compute the cost function
        }
        return (c); // we don't divide by 2 here because we are just computing the distances from node p to every other node 
    }

    private double distanceToCenter(Vertex p, int x, int y)
    {
       double dx, dy; 
       double temp;
       dx = p.getCenterX() - x;  
       dy = p.getCenterY() - y; 
       temp = Math.sqrt(dx*dx+dy*dy);    // compute distance
       return temp; 
    }
}

