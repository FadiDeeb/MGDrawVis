package MGDrawVis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Graph implements GraphInterface   
{
    public int numVertices;
    public int numEdges; 
    public ArrayList<Vertex> points;  
    public ArrayList<ArrayList<Integer>> adjacency;  
    public ArrayList<Line> lines; 
    public ArrayList<Pair<Integer,Integer>> undirectedLines; 
    public ArrayList<ArrayList<Double>> distances;
    public List<File> inputFiles; 
    public Double cost; 
    public int nodeSize = 10; 
    
    public Graph()
    {
       numVertices = 0;
       numEdges = 0; 
       points = new ArrayList<Vertex>();
       adjacency = new ArrayList<ArrayList<Integer>>(); 
       lines = new ArrayList<Line>(); 
       undirectedLines = new ArrayList<Pair<Integer,Integer>>(); 
       distances = new ArrayList<ArrayList<Double>>();   
   }
    
    /********************** Check if Empty ***********************************/ 
    public boolean isEmpty()
    {
        return numVertices == 0;
    }

    /******************* add a node to a graph ******************************/ 
    public void addVertex(Vertex vertex)
    {
       vertex.id.setText(""+(numVertices+1));
       points.add(vertex); 
       adjacency.add(new ArrayList<Integer>()); 
       distances.add(new ArrayList<Double>());
       for (int i=0; i<distances.size()-1; i++)
           distances.get(i).add(0.0);
       for (int i=0; i<distances.size(); i++)
           distances.get(numVertices).add(0.0);
       numVertices++;
    }
  
    public void updateVertex(int v, Vertex vertex)
    {
        Vertex original = new Vertex((int)this.points.get(v).getCenterX(), (int)this.points.get(v).getCenterY(),nodeSize);
        this.points.get(v).setCenterX(vertex.getCenterX());
        this.points.get(v).setCenterY(vertex.getCenterY());
        this.points.get(v).id.setX((int)(this.points.get(v).getCenterX()+10));
        this.points.get(v).id.setY((int)(this.points.get(v).getCenterY()+10));
        //updateLine(v, original, this.points.get(v));
        this.computeDistances(v);
    }
    
    public void copyPointsBackToGraph(ArrayList<Vertex> vertices)
    {
        for(int i=0; i<points.size(); i++)
            this.updateVertex(i, vertices.get(i));       
    }
    /******************* get a node given its index *************************/ 
    public Vertex getVertex(int index)
    {
        return points.get(index); 
    }
    
    /****************** add an edge to a graph ****************************/ 
    public void addEdge(Vertex fromVertex, Vertex toVertex, Line line)
    {
      int v1;
      int v2;
      v1 = indexIs(fromVertex);
      v2 = indexIs(toVertex);
      adjacency.get(v1).add(v2);
      adjacency.get(v2).add(v1);
      lines.add(line);
      Pair <Integer, Integer> p = new Pair(v1,v2);
      undirectedLines.add(p);  
      numEdges++;
    }
    
    /******************** check if there is edge between two nodes ***********/ 
    public boolean hasEdge(Vertex vertex1, Vertex vertex2)
    {
        return (adjacency.get(indexIs(vertex1)).contains(indexIs(vertex2)));
    }
    
    /******************** check if there is edge between two nodes given their indices *****/ 
    public boolean hasEdge(int v1, int v2)
    {
        return adjacency.get(v1).contains(v2);
    }
    
    /******************* return an index of a give node ********************/ 
    public int indexIs(Vertex vertex)
    {
       int index = 0;
       while (!vertex.equals(points.get(index)))
          index++;
       return index;
    }

    /************************ clear all graph information *****************/ 
    public void clearGraphRandom()
    {
       numVertices = 0;
       numEdges = 0; 
       points = new ArrayList<Vertex>();
       adjacency = new ArrayList<ArrayList<Integer>>(); 
       lines = new ArrayList<Line>(); 
       undirectedLines = new ArrayList<Pair<Integer, Integer>>(); 
       distances = new ArrayList<ArrayList<Double>>();
    }
    
    /************************ clear all graph information *****************/ 
    public void clearGraph()
    {
       numVertices = 0;
       numEdges = 0; 
       points = new ArrayList<Vertex>();
       //adjacency = new ArrayList<ArrayList<Integer>>(); 
       lines = new ArrayList<Line>(); 
       //undirectedLines = new ArrayList<Pair<Integer, Integer>>(); 
       distances = new ArrayList<ArrayList<Double>>();
    }
   
    /********************** load a graph from Excel file ********************/ 
    public void loadGraph(DrawingStageActions layout, boolean multiple, File filename) throws FileNotFoundException, IOException
    {
        File directory = new File("MyGraphs");
        if (! directory.exists())
        {
           directory.mkdir();
        }
        FileInputStream fis=null;
        if (multiple)
            fis = new FileInputStream(filename);
        else
        {
           FileChooser inF = new FileChooser();
           inF.setInitialDirectory(directory);
           File inputFile = inF.showOpenDialog(null);
           try{
           fis = new FileInputStream(inputFile);
           }catch(Exception e)
           {
               System.out.println("You did not select a file");
           }
        }
        if (fis != null)
        {
           XSSFWorkbook excelFile = new XSSFWorkbook(fis); 
           XSSFSheet sheet1 = excelFile.getSheetAt(0);
           Iterator<Row> rowIt = sheet1.iterator();
           boolean hasCoordinates = rowIt.next().getCell(0).getStringCellValue().equalsIgnoreCase("Has Coordinates");
           int nodes = (int)(rowIt.next().getCell(0).getNumericCellValue());
           for (int i=0; i<nodes; i++)
              addDisplayNodes(layout,rowIt,hasCoordinates); 
           while(rowIt.hasNext())
           {
              Row row = rowIt.next(); 
              Iterator<Cell> cellIt = row.cellIterator();
              while(cellIt.hasNext())
              {
                 int v1 = (int)(cellIt.next().getNumericCellValue());
                 int v2 = (int)(cellIt.next().getNumericCellValue());
                 addDisplayEdge(v1,v2,layout); 
              }
           }
       }
    }
    
    private void addDisplayNodes(DrawingStageActions layout, Iterator<Row> rowIt, boolean hasCoordinates)
    {
       Vertex r=null; 
       if (hasCoordinates)
       {
           Row row = rowIt.next(); 
           Iterator<Cell> cellIt = row.cellIterator();
           while(cellIt.hasNext())
           {
              double x = (double)(cellIt.next().getNumericCellValue());
              double y = (double)(cellIt.next().getNumericCellValue());
              r = new Vertex((int)(x), (int)(y), nodeSize); 
           }
       }
       else
          r = new Vertex((int)(Math.random()*DrawingStage.screenWidth), (int)(Math.random()*DrawingStage.screenHeight), nodeSize); 
       this.addVertex(r); 
       layout.getChildren().add(r);
       layout.getChildren().add(r.id);
    }
   
    private void addDisplayEdge(int v1, int v2, DrawingStageActions layout)
    {
       Vertex vt1 = this.getVertex(v1);
       Vertex vt2 = this.getVertex(v2);
       Line line = new Line(vt1.getCenterX(), vt1.getCenterY(), vt2.getCenterX(), vt2.getCenterY());
       this.addEdge(vt1, vt2, line);
       layout.getChildren().add(line);
    }
    
    /******************* Load Multiple Graphs ************************/ 
    public void loadMultipleGraphs(DrawingStageActions layout) throws FileNotFoundException, IOException
    {
        File directory = new File("MyGraphs");
        if (! directory.exists())
        {
           directory.mkdir();
        }
        FileChooser inF = new FileChooser();
        inF.setInitialDirectory(directory);
        inputFiles = inF.showOpenMultipleDialog(null); 
    }
    /******************* save a graph to an excel file ************************/ 
    
    // save to a file given as a parameter 
    public void saveGraph(String fileName) throws FileNotFoundException, IOException
    {
        File directory = new File("MyGraphs");
        if (! directory.exists())
        {
           directory.mkdir();
        }       
        File outputFile = new File(fileName);
        XSSFWorkbook excelFile = new XSSFWorkbook();
        XSSFSheet sheet = excelFile.createSheet("Graph Data");
        Map<Integer, Object[]> graphData = createRecords(); 
        writeDatatoSheet(graphData, sheet, excelFile, outputFile);
    }
    
    // save in a file chosen by the user in a user define directory (for interactive testing) 
    public void saveGraph() throws FileNotFoundException, IOException
    {
        File directory = new File("MyGraphs");
        if (! directory.exists())
        {
           directory.mkdir();
        }
        FileChooser outF = new FileChooser();
        outF.setInitialDirectory(directory);
        File outputFile = outF.showSaveDialog(null);
        XSSFWorkbook excelFile = new XSSFWorkbook();
        XSSFSheet sheet = excelFile.createSheet("Graph Data");
        Map<Integer, Object[]> graphData = createRecords(); 
        writeDatatoSheet(graphData, sheet, excelFile, outputFile);
    }
    
    private Map<Integer, Object[]> createRecords()
    {
        Map<Integer, Object[]> graphData = new TreeMap<Integer, Object[]>();
        graphData.put(1, new Object[]{"Has Coordinates"});
        graphData.put(2, new Object[]{(Integer)(this.numVertices)});
        int rowCount = 3; 
        for(int i=0; i<this.numVertices; i++)
            graphData.put(rowCount++, new Object[]{(Double)(this.points.get(i).getCenterX()),(Double)(this.points.get(i).getCenterY())});
        for(int i=0; i<this.numEdges; i++)
            graphData.put(rowCount++, new Object[]{(Integer)(this.undirectedLines.get(i).getKey()),(Integer)(this.undirectedLines.get(i).getValue())});
        return graphData; 
    }
    
    private void writeDatatoSheet(Map<Integer, Object[]> graphData, XSSFSheet sheet, XSSFWorkbook excelFile, File outputFile) throws FileNotFoundException, IOException
    {
        Set<Integer> keyid = graphData.keySet();
        XSSFRow row; 
        int rowid = 0;
        for (Integer key : keyid) 
        {
           row = sheet.createRow(rowid++);
           Object[] objectArr = graphData.get(key);
           int cellid = 0;
           for (Object obj : objectArr) 
           {
              Cell cell = row.createCell(cellid++);
              if (obj instanceof Integer)
                 cell.setCellValue((Integer)obj);
              else if(obj instanceof Double)
                  cell.setCellValue((Double)obj);
              else cell.setCellValue((String)obj);
           }
        }  
        FileOutputStream fos = new FileOutputStream(outputFile); 
        excelFile.write(fos);
        fos.close();
    }
    
    /******************** randomize the current graph layout ***************/ 
    public void randomize()
    {
        for (int i=0; i<points.size(); i++)
        {
            Vertex vertexBeforeUpdate = new Vertex((int)points.get(i).getCenterX(),(int)points.get(i).getCenterY(),nodeSize);
            int x = (int)(Math.random()*DrawingStage.screenWidth);
            int y = (int)(Math.random()*DrawingStage.screenHeight);  
            points.get(i).setPoint(x, y, nodeSize);
            updateLine(i, vertexBeforeUpdate, points.get(i));
        }
    }
    
    private boolean isStartingPoint(Vertex c,Line L)
    {
       if(c.contains(L.getStartX(),L.getStartY()))
           return true; 
       return false; 
    }
    
    private boolean isEndingPoint(Vertex c,Line L)
    {
       if(c.contains(L.getEndX(),L.getEndY()))
           return true; 
       return false; 
    }
    
    /********************* generate a random graph given number of nodes and edges *************/ 
    public void generateRandom(int nodes, int edges)
    {
        do{
           clearGraphRandom();
           addRandomNodes(nodes);
           addRandomEdges(edges);
        }
        while(!isConnected());
    }
    
    private void addRandomNodes(int nodes)
    {
        for(int i=0; i<nodes; i++)
        {
           int x = (int)(Math.random()*DrawingStage.screenWidth);
           int y = (int)(Math.random()*DrawingStage.screenHeight);  
           Vertex r = new Vertex(x, y, nodeSize);  // get the point from the mouse and create a small rectangle
           while(points.contains(r))
           {
               x = (int)(Math.random()*DrawingStage.screenWidth);
               y = (int)(Math.random()*DrawingStage.screenHeight);  
               r = new Vertex(x, y, nodeSize);  // get the point from the mouse and create a small rectangle
           }
           addVertex(r);  // add the small rectangle to the list
        }
    }
    
    private void addRandomEdges(int edges)
    {
        for(int i=0; i<edges; i++)
        {
            int p1 = (int)(Math.random()*points.size());
            int p2 = (int)(Math.random()*points.size());
            while(p2 == p1 || adjacency.get(p1).contains((Integer)p2))
            {
                p1 = (int)(Math.random()*points.size());
                p2 = (int)(Math.random()*points.size());
            }
            Line line = new Line(getVertex(p1).getCenterX(),getVertex(p1).getCenterY(),getVertex(p2).getCenterX(),getVertex(p2).getCenterY());
            addEdge(getVertex(p1),getVertex(p2),line);
        }
    }
    
    public void generateRandomAllTypes(int nodes, int edges)
    {
           clearGraphRandom();
           addRandomNodes(nodes);
    }
    
    /**************** updates the end points of a line (edge) *****************/ 
    public void updateLine(int v, Vertex oldV, Vertex newV)
    {
        for(int j=0; j< this.lines.size(); j++)
        {
            if(isStartingPoint(oldV,lines.get(j)))
            {
                lines.get(j).setStartX(newV.getCenterX());
                lines.get(j).setStartY(newV.getCenterY());
            }
            else if(isEndingPoint(oldV,lines.get(j))){
                  lines.get(j).setEndX(newV.getCenterX());
                  lines.get(j).setEndY(newV.getCenterY());
                }
        }
    }
    
    /******************** check if a graph is connected using BFS *************/
    public boolean isConnected()
    {
        int r; 
        ArrayList<Integer> L = new ArrayList<Integer>();  //List of visited nodes 
        ArrayList<Integer> K = new ArrayList<Integer>();  //List of nodes to be explored
        L.add(0); // visit 0 
        K.add(0); // 0 is the first to explore
        while (K.size() > 0) // k is not empty 
        {
           r = K.get(0); // find and remove some vertex y in K
           K.remove(0); 
           for(int i=0; i<this.adjacency.get(r).size(); i++) // for each edge (y,z)
           {
              if (!L.contains(adjacency.get(r).get(i))) // if z is not in L 
              {
                 L.add(adjacency.get(r).get(i)); // add z to L 
                 K.add(adjacency.get(r).get(i)); // add z to K 
              }
           }
        }
        if (L.size() < this.numVertices) // if L has fewer items than number of nodes 
           return false;  // disconnected 
        else 
           return true; // connected 
    }
    
    /***************** compute distances between every two nodes ******************/ 
    public void computeDistances() 
    {
       double dx, dy; 
       double temp;
       for(int i=0; i<points.size(); i++)
       { 
          for(int j=0; j<points.size(); j++)
          {
             dx = points.get(i).getCenterX() - points.get(j).getCenterX();
             dy = points.get(i).getCenterY() - points.get(j).getCenterY();
             temp = Math.sqrt(dx*dx+dy*dy);    // compute distance
                distances.get(i).set(j, temp);
           }
      }
    }
    
    /***************** compute distances from a given point to all other points *******/
    public void computeDistances(int p)
    {
       double dx, dy; 
       double temp;
       int v = p;
       for(int i=0; i<points.size(); i++)   // to go through all the points 
       {
          dx = points.get(v).getCenterX() - points.get(i).getCenterX();
          dy = points.get(v).getCenterY() - points.get(i).getCenterY();
          temp = Math.sqrt(dx*dx+dy*dy);    // compute distance
          distances.get(v).set(i, temp);    // update the distance from node p to node i 
          distances.get(i).set(v, temp);    // update the distance from node i to node p 
       }
    }
    
    /****************** copy the content of a graph to another graph **************/
   public void copyGraph(Graph graph)
   {
       this.clearGraph(); 
       this.numVertices = graph.numVertices;
       this.numEdges = graph.numEdges; 
       for (int i=0; i<graph.numVertices; i++)
       {
               Vertex v = graph.points.get(i); 
               Vertex p = new Vertex((int)v.getCenterX(), (int)v.getCenterY(), nodeSize);
               p.id.setText(""+(i+1));
               this.points.add(p);
       }
       this.adjacency = graph.adjacency;
       this.undirectedLines = graph.undirectedLines;
       this.lines = graph.lines; 
       
       for(int i=0; i<graph.distances.size(); i++)
       {
           ArrayList<Double> temp = graph.distances.get(i); 
           this.distances.add(temp);
       }
       this.cost = graph.cost;
       
       /*
       for(int i=0; i<graph.lines.size(); i++)
       {
            Line y = graph.lines.get(i);
            Vertex vt1 = new Vertex((int)y.getStartX(), (int)y.getStartY(), nodeSize);
            Vertex vt2 = new Vertex((int)y.getEndX(), (int)y.getEndY(), nodeSize);
            Line line = new Line(vt1.getCenterX(), vt1.getCenterY(), vt2.getCenterX(), vt2.getCenterY());
            this.lines.add(line);
        }*/
             
       /*this.clearGraph();
       for(int i=0; i < graph.points.size(); i++)
       {
           Vertex v = new Vertex((int)(graph.points.get(i).getCenterX()),(int)(graph.points.get(i).getCenterY()), (int)(graph.points.get(i).getRadius()));
           this.addVertex(v);
       }
       for(Pair<Integer,Integer> k: graph.undirectedLines)
       {
           Vertex v1 = new Vertex((int)graph.points.get(k.getKey()).getCenterX(), (int)graph.points.get(k.getKey()).getCenterY(), (int)graph.points.get(k.getKey()).getRadius());
           Vertex v2 = new Vertex((int)graph.points.get(k.getValue()).getCenterX(), (int)graph.points.get(k.getValue()).getCenterY(), (int)graph.points.get(k.getValue()).getRadius());
           Line line = new Line(v1.getCenterX(), v1.getCenterY(), v2.getCenterX(), v2.getCenterY());
           this.addEdge(v1, v2, line);
       }
       this.computeDistances();*/
   }
   
   public String toString()
   {
       String s = "";
       s += numVertices + "\n";
       s += numEdges + "\n";
       s += points.toString() + "\n";
       s += adjacency.toString() + "\n";
       s += lines.toString() + "\n";
       s += undirectedLines.toString() + "\n";
       s += distances.toString() + "\n";
       return s; 
   }
}
