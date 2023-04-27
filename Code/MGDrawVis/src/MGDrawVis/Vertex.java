package MGDrawVis;

import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Vertex extends Circle
{
    public Text id; 
    
    public Vertex(int centerX, int centerY, int radius)
    {
        super(centerX, centerY, radius);
        this.setFill(TopBar.palette.getValue());
        id = new Text();
        id.setX((int)(centerX+10));
        id.setY((int)(centerY+10));
        id.setFont(Font.font("Monospaced", FontWeight.BOLD , 12));
        id.setStyle("-fx-color:blue;");
    }
  
    
    public void setPoint(int centerX, int centerY, int radius)
    {
        this.setCenterX(centerX);
        this.setCenterY(centerY);
        this.setRadius(radius);
        id.setX((int)(centerX+10));
        id.setY((int)(centerY+10));
    }
    
    public int getID()
    {
        return Integer.parseInt(id.getText());
    }
    
    public void setID(int id)
    {
        this.id.setText(""+id);
        this.id.setX((int)(this.getCenterX()+10));
        this.id.setY((int)(this.getCenterY()+10));
    }
    
    public boolean equals(Object o)
    {
        if (this == o)
            return true; 
        else if (!(o instanceof Vertex))
            return false; 
        else{
               Vertex v = (Vertex) o; 
               return (this.getCenterX() == v.getCenterX() && this.getCenterY() == v.getCenterY());
             }
    }
}
