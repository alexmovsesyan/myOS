import java.util.Scanner;
import java.io.*;
import javafx.application.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

class UserThread extends Thread{

    private String userName;
    private int currentIndex = 0;
    private int currentDisk;
    private String currFile;
    private int lineCount = 1;
    private int userNum;

    UserThread(String uname){
        super(uname);
        userName = uname;
    }

    void save(String fileName)throws InterruptedException{
        int diskNum = main.RM.request();
        FileInfo FI = new FileInfo();
        FI.diskNumber = diskNum;
        currentDisk = diskNum;
        currentIndex = main.DM.getNextAvailableSector(diskNum);
        FI.startingSector = currentIndex;
        currFile = fileName;
        main.DirM.enter(currFile, FI);
    }

    void write(String line)throws InterruptedException{
        int nextSector = main.DM.allocateSpace(currentDisk);
        main.disks[currentDisk].write(nextSector, new StringBuffer(line));
        StringBuffer data = new StringBuffer();
        main.disks[currentDisk].read(nextSector, data);
        Thread.sleep(main.animationDelay);
        addDiskInfo(currentDisk, nextSector, line);
    }

    void addDiskInfo(int disk, int sector, String data){
        Platform.runLater(
        () -> {
        Text t = new Text("  " +Integer.toString(sector) + ": ["+ data+ "]");
        t.setFill(main.userColors[userNum]);
        t.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 10));
        main.diskGrids[disk].addRow(sector+1, t);
    }
    );
    }

    void end() throws InterruptedException{
        int currSector = main.DM.getNextAvailableSector(currentDisk) -1;
        FileInfo tempF = main.DirM.lookup(currFile);
        tempF.fileLength = currSector - tempF.startingSector + 1;
        main.DirM.enter(currFile, tempF);
        main.RM.release(currentDisk);
	}

	void print(String fileName){
        FileInfo f = main.DirM.lookup(fileName);
        int end = f.startingSector+ f.fileLength;
        PrintJobThread pt = new PrintJobThread(f.startingSector, end, f.diskNumber, main.userColors[userNum]);
        pt.start();
    }


    void printDisk(int disk, int start, int end, String file) throws InterruptedException{
        StringBuffer data = new StringBuffer();
        for (int i=start; i<= end; ++i){
            main.disks[disk].read(i, data);
            data.delete(0, data.length());
        }
        FileInfo f = main.DirM.lookup(file);
    }

	void parseInput(String input)throws InterruptedException{
        String[] parsed = input.split("\\s+");
        String command = parsed[0];
        String fileName;
        switch (command) {
            case ".save":
                fileName = parsed[1];
                save(fileName);
                break;
            case ".end":
                end();
                break;
            case ".print":
                fileName = parsed[1];
                print(fileName);
                break;
            default:
                write(input);
        }
    }

    void getInput(Scanner s) throws InterruptedException{
        String in;
        while (s.hasNextLine()){
            in = s.nextLine();
            if(in.length() >1){
                addLabel(in);
                Thread.sleep(main.animationDelay);
                parseInput(in);
            }
        }
    }

    void addLabel(String input){
        Platform.runLater(
        () -> {
        switch (userName) {
            case "USER1":
                userNum = 0;
                break;
            case "USER2":
                userNum = 1;
                break;
            case "USER3":
                userNum = 2;
                break;
            case "USER4":
                userNum = 3;
        }
        Text t1 = new Text(input);
        t1.setStyle("-fx-font: 10 verdana;");
        t1.setFill(main.userColors[userNum]);
        Text t2 = new Text("    " +input);
        t2.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 10));
        t2.setFill(main.userColors[userNum]);
        main.userGrids[userNum].addRow(lineCount, t2);
        main.userLabels[userNum].setText(input);
        lineCount+=1;
    }
    );
    }

	public void run() {
        File file = new File("../inputs/" + userName);
        try{
            Scanner sc = new Scanner(file);
            getInput(sc);
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        catch (InterruptedException e){
            System.out.println(e);
        }
    }
}
