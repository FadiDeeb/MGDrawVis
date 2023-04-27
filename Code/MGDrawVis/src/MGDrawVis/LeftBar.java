package MGDrawVis;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LeftBar extends VBox 
{
    public Label label; 
    public Button jaya1; 
    public Button ga;  
    public Button hc; 
    public Button sa; 
    public static int prefHeight = 30;
    public static int prefWidth = 150;
    
    public LeftBar()
    {
        super.setPrefHeight(DrawingStage.screenHeight);
        super.setPrefWidth(prefWidth);
        super.setAlignment(Pos.TOP_LEFT);
        super.setSpacing(30);
        super.setPadding(new Insets(5,5,5,5));
        label = new Label("Drawing Algorithms");
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Verdana",14));
        label.setPrefWidth(prefWidth);
        label.setPrefHeight(prefHeight);
        jaya1 = new Button("Jaya I");
        jaya1.setPrefWidth(prefWidth);
        jaya1.setPrefHeight(prefHeight);
        ga = new Button("Jaya+Grid");
        ga.setPrefWidth(prefWidth);
        ga.setPrefHeight(prefHeight);
        hc = new Button("Hill Climbing");
        hc.setPrefWidth(prefWidth);
        hc.setPrefHeight(prefHeight);
        sa = new Button("Simulated Annealing");
        sa.setPrefWidth(prefWidth);
        sa.setPrefHeight(prefHeight);
        this.getChildren().add(label);
        this.getChildren().add(jaya1);
        this.getChildren().add(ga);
        this.getChildren().add(hc);
        this.getChildren().add(sa);
    }    
}
