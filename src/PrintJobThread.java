import javafx.scene.paint.Color; 
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.application.*;
import java.io.*;

class PrintJobThread extends Thread{

    int startSector;
    int endSector;
    int disk;
    Color userColor;

PrintJobThread(int start, int end, int diskNum, Color color){
        startSector = start;
        endSector = end;
        disk = diskNum;
        userColor = color;
    }

    public void run() {
        try{
            int printerNum = main.PM.request();
            StringBuffer data = new StringBuffer();
            HBox loading = new HBox();
            loading.setPadding(new Insets(0, 20, 10, 20));
            loading.setSpacing(10);
            Platform.runLater( () -> {
                main.printLoadGrids[printerNum].addRow(1,loading);
            });
            for(int i=startSector; i<endSector; ++i){
                if(i==startSector+5){
                    Platform.runLater( () -> {
                        loading.getChildren().clear();
                    });
                    
                }
                main.disks[disk].read(i, data);
                Printer.printLine(disk, printerNum, data.toString(), userColor);
                data.delete(0, data.length());
                Platform.runLater(
                    () -> {
                    main.printLabels[printerNum].setText("   PRINTING...");
                    main.printLabels[printerNum].setTextFill(userColor);
            
                    Rectangle r = new Rectangle(12,12);
                    r.setFill(userColor);
                    loading.getChildren().add(r);

                }
                );
                if(i == endSector-1){
                    Platform.runLater( () -> {
                        main.printLabels[printerNum].setText("      DONE!");
                    });
                    
                }
            }
            Platform.runLater( () -> {
                main.printLoadGrids[printerNum].getChildren().remove(loading);
            });
            main.PM.release(printerNum);
        }
        catch(IOException e){
            System.out.println(e);
        }
        catch(InterruptedException e){
            System.out.println(e);
        }
    }


}
