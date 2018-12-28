import java.io.*;
import javafx.scene.paint.Color; 
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.*;

class Printer{

    static void printLine(int disk, int printerNum, String data, Color color) throws IOException, InterruptedException{
        String fileName = "PRINTER" + Integer.toString(printerNum+1);
        File f = new File("../outputs/" + fileName);
        Thread.sleep(2750 + main.animationDelay);
        BufferedWriter writer = new BufferedWriter (new FileWriter(f, true));
        writer.write(data.toString());
        writer.write("\n");
        addLabel(printerNum, data, color);
        writer.close();
    }

    static void addLabel(int printerNum, String input, Color userColor){
        Platform.runLater( () -> {
        Text t = new Text("    "+input);
        t.setFill(userColor);
        t.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 10));
        main.printGrids[printerNum].addColumn(1, t);
    }
    );
    }

}
