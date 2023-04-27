
package MGDrawVis;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ShowRunFilesGridPane extends GridPane 
{
    public Label processedFileNumLabel; 
    public Label processedFileNum; 
    public Label processedFileNameLabel; 
    public Label processedFileName; 
    public Label statusLabel; 
    public Label status; 
    
    public ShowRunFilesGridPane()
    {
        this.setPadding(new Insets(10,10,10,10));
        processedFileNumLabel = new Label("File#");
        processedFileNumLabel.setPrefWidth(50);
        processedFileNum = new Label("0/0"); 
        processedFileNameLabel = new Label("FileName");
        processedFileNameLabel.setPrefWidth(120);
        processedFileName = new Label(""); 
        statusLabel = new Label("Status:"); 
        status = new Label(""); 
        this.add(processedFileNumLabel, 0, 0);
        this.add(processedFileNum, 0, 1);
        this.add(processedFileNameLabel, 1, 0);
        this.add(processedFileName, 1, 1);
        this.add(statusLabel, 0, 2);
        this.add(status, 1, 2);
    }
    
    public boolean setValues(int fNum, String fName, int maxNum, String statusRun)
    {
        processedFileNum.setText(""+fNum+"/"+maxNum);
        processedFileName.setText(fName);
        status.setText(statusRun);    
        if(fNum == maxNum)
            return false; 
        else return true; 
    }
    
    public void clearValues()
    {
        processedFileNum.setText("0/0");
        processedFileName.setText("");
        status.setText("");
    }
    public void setStatus(String s)
    {
        status.setText(s);
    }
}
