import java.io.*;
import java.util.*;
import java.util.Scanner;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color; 
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.BackgroundFill;
import javafx.geometry.HPos;
import javafx.event.EventHandler;

public class main extends Application{
	static final int NUMBER_OF_USERS=4;
	static final int NUMBER_OF_DISKS=2;
	static final int NUMBER_OF_PRINTERS=3;

	static UserThread users[] = new UserThread[NUMBER_OF_USERS];
	public static Disk disks[] = new Disk[NUMBER_OF_DISKS];	

	public static ResourceManager RM= new ResourceManager(NUMBER_OF_DISKS);
	public static DiskManager DM = new DiskManager();
	public static DirectoryManager DirM = new DirectoryManager();
    public static ResourceManager PM = new ResourceManager(NUMBER_OF_PRINTERS);

    public static GridPane userGrids[] = new GridPane[NUMBER_OF_USERS];
    public static Label userLabels[] = new Label[NUMBER_OF_USERS];
    public static GridPane diskGrids[] = new GridPane[NUMBER_OF_DISKS];
    public static GridPane printGrids[] = new GridPane[NUMBER_OF_PRINTERS];
    public static Label printLabels[] = new Label[NUMBER_OF_PRINTERS];
    public static GridPane printLoadGrids[] = new GridPane[NUMBER_OF_PRINTERS];
    public static Color[] userColors = {Color.web("#3a85aa"), Color.web("#6caaa0"), Color.web("#dc5f46"), Color.web("#8b3642")};
    public static GridPane grid2 = new GridPane();
    public static int animationDelay = 2000;

    Stage window;
    Scene scene1, scene2;

    public static void main(String[] args){
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        addWelcomePage(primaryStage);
        
    }

    void addWelcomePage(Stage primaryStage){
        window = primaryStage;
        window.setTitle("Alex's Operating System");

        GridPane startPage = new GridPane();

        HBox vb = new HBox();
        vb.setPadding(new Insets(10, 100, 50, 100));
        vb.setSpacing(100);

        StackPane startTitle = new StackPane();
        Label title = new Label("Welcome to Alex's Operating System!");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 18));
        title.setStyle("-fx-padding: 7");
        CornerRadii corn = new CornerRadii(7,7,7,7,false);
        startTitle.setBackground(new Background(new BackgroundFill(Color.web("#3a85aa"), corn,Insets.EMPTY)));
        startTitle.getChildren().add(title);
        StackPane.setAlignment(title, Pos.CENTER);
        startTitle.setPrefWidth(100);
        startTitle.setMaxHeight(10);
        startTitle.setPadding(new Insets(30, 50, 50, 50));
        startPage.addRow(0,startTitle);


        Button start = new Button("start");
        start.setStyle("-fx-font: 15 verdana;" + 
                        "-fx-font-weight: bold;"+
                        "-fx-background-radius: 7 7 7 7;"+
                        "-fx-text-fill: #dc5f46;");
        start.setPrefSize(100, 20);
        start.setOnAction(e -> goToOS());
        vb.getChildren().add(start);
        
        Button quit = new Button("quit");
        quit.setStyle("-fx-font: 15 verdana;" + 
                         "-fx-font-weight: bold;"+
                        "-fx-background-radius: 7 7 7 7;"+
                        "-fx-text-fill: #6caaa0;");
        quit.setPrefSize(100, 20);
        quit.setOnAction(e -> Platform.exit());
        vb.getChildren().add(quit);

        startPage.addRow(1,vb);

        startPage.setStyle("-fx-background-color: #d5e5ed; -fx-padding: 10; -fx-hgap: 2; -fx-vgap: 2;");

        scene1 = new Scene(startPage, 540, 200);

        
      window.setScene(scene1);
      window.show();
    }

    void goToOS(){
        addOSScene();

        for(int i=0; i<NUMBER_OF_DISKS; ++i)
            disks[i] = new Disk();

        String user;
        for(int i=0; i<NUMBER_OF_USERS; ++i){
            user = "USER" + Integer.toString(i+1);
            users[i] = new UserThread(user);
            users[i].start();
        }
        try{
            for(int i=0; i<NUMBER_OF_PRINTERS; ++i){
                String fileName = "PRINTER" + Integer.toString(i+1);
                File f = new File("../outputs/" + fileName);
                BufferedWriter writer = new BufferedWriter (new FileWriter(f));
            }
        }
        catch(IOException e){
            System.out.println(e);
        }

        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });     

    }

    void addUserGrids(GridPane grid){
        
        for(int i =0; i< NUMBER_OF_USERS; ++i){
            GridPane user = new GridPane();
            StackPane userLabel = new StackPane();
            Label title = new Label("USER " + Integer.toString(i+1));
            title.setTextFill(Color.WHITE);
            title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            title.setStyle("-fx-padding: 7");
            CornerRadii corn = new CornerRadii(7,7,0,0,false);
            userLabel.setBackground(new Background(new BackgroundFill(userColors[i], corn,Insets.EMPTY)));
            Label info = new Label("No Input");
            info.setTextFill(Color.WHITE);
            info.setStyle("-fx-font: 11 verdana;" +
                            "-fx-padding: 7");
            title.setUnderline(true);

            user.setStyle("-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 7 7 7 7;");
            user.setPrefHeight(580);
              
            userLabel.getChildren().addAll(title,info);
            StackPane.setAlignment(title, Pos.TOP_CENTER);
            StackPane.setAlignment(info, Pos.BOTTOM_CENTER);
            userLabel.setPrefWidth(130);
            userLabel.setPrefHeight(50);

            grid.add(userLabel, i, 1);
            
            userGrids[i] = user;
            userLabels[i] = info;
            int col =i;
            
            Button expand = new Button("v");
            expand.setStyle("-fx-font: 13 verdana;" + 
                            "-fx-background-radius: 0 0 7 7;"+
                            "-fx-background-color: white;");
            switch (i) {
                case 0:
                    expand.setStyle("-fx-text-fill: #3a85aa;");
                    break;
                case 1:
                    expand.setStyle("-fx-text-fill: #6caaa0;");
                    break;
                case 2:
                    expand.setStyle("-fx-text-fill: #dc5f46;");
                    break;
                case 3:
                    expand.setStyle("-fx-text-fill: #8b3642;");
            }

            
            expand.setPrefSize(130, 20);
            grid.add(expand,i,2);
            expand.setOnAction(e -> expandUser(grid, user,col,3, 1, 3, expand));
        }
    }


    void expandUser(GridPane grid, GridPane user,int col, int row, int colSpan, int rowSpan, Button expand){
        grid.getChildren().remove(expand);
        grid.add(user,col,row,colSpan,rowSpan);
        Button collapse = new Button("^");
        collapse.setStyle("-fx-font: 13 verdana;" + 
                            "-fx-background-radius: 0 0 7 7;"+
                            "-fx-background-color: white;");
        switch (col) {
                case 0:
                    collapse.setStyle("-fx-text-fill: #3a85aa;");
                    break;
                case 1:
                    collapse.setStyle("-fx-text-fill: #6caaa0;");
                    break;
                case 2:
                    collapse.setStyle("-fx-text-fill: #dc5f46;");
                    break;
                case 3:
                    collapse.setStyle("-fx-text-fill: #8b3642;");
            }
        collapse.setPrefSize(130, 20);
        grid.add(collapse,col,2);
        collapse.setOnAction(e -> collapseUser(grid,user,collapse,expand, col));
    }

    void collapseUser(GridPane grid, GridPane user, Button collapse, Button expand, int col){
        grid.getChildren().remove(user);
        grid.getChildren().remove(collapse);
        grid.add(expand,col,2);
    }

  

    void addDiskGrids(GridPane grid){
        for(int i =0; i< NUMBER_OF_DISKS; ++i){
            StackPane diskTitle = new StackPane();
            Label title = new Label("DISK " + Integer.toString(i+1));
            title.setTextFill(Color.WHITE);
            title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            title.setStyle("-fx-padding: 7");
            CornerRadii corn = new CornerRadii(7,7,0,0,false);
            diskTitle.setBackground(new Background(new BackgroundFill(Color.web("#6d6e71"), corn,Insets.EMPTY)));
            diskTitle.getChildren().add(title);
            StackPane.setAlignment(title, Pos.CENTER);
            diskTitle.setPrefWidth(130);
            diskTitle.setPrefHeight(50);
            grid.add(diskTitle, i+5, 1);

            GridPane disk = new GridPane();
            disk.setStyle("-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 7 7 7 7;");
            disk.setPrefHeight(607);
            grid.add(disk,i+5,2, 1, 3);
            diskGrids[i] = disk;
        }

    }

    void addPrintGrids(GridPane grid){
        for(int i =0; i< NUMBER_OF_PRINTERS; ++i){
            GridPane printer = new GridPane();

            StackPane printTitle = new StackPane();
            Label title = new Label("PRINTER " + Integer.toString(i+1));
            title.setTextFill(Color.WHITE);
            title.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
            title.setStyle("-fx-padding: 7");
            CornerRadii corn = new CornerRadii(7,7,0,0,false);
            printTitle.setBackground(new Background(new BackgroundFill(Color.web("#6d6e71"), corn,Insets.EMPTY)));
            printTitle.getChildren().add(title);
            StackPane.setAlignment(title, Pos.CENTER);
            printTitle.setPrefWidth(130);
            printTitle.setPrefHeight(50);
            grid.add(printTitle, i+7, 1);

            printer.setStyle("-fx-background-color: #ffffff;" +
            "-fx-background-radius: 7 7 7 7;");
            printer.setPrefHeight(580);
            addPrinterLoads(grid,i+7,2, i, Color.web("#59b2e0"));
            grid.add(printer,i+7,3);
            printGrids[i] = printer;
        }
    }

    void addPrinterLoads(GridPane grid, int col, int row, int printNum, Color color){
        GridPane printLoad = new GridPane();
        Label title = new Label("NO PRINT JOBS");
        title.setTextFill(color);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        title.setStyle("-fx-padding: 7");
        CornerRadii corn = new CornerRadii(7,7,7,7,false);
        printLoad.setBackground(new Background(new BackgroundFill(Color.web("#d2d4d8"), corn,Insets.EMPTY)));
        printLoad.addRow(0,title);
        StackPane.setAlignment(title, Pos.CENTER);
        printLoad.setPrefWidth(130);
        printLoad.setMinHeight(50);
        printLabels[printNum] = title;
        printLoadGrids[printNum] = printLoad;
        
       
        grid2.add(printLoad,col,row);

    }

    void quit(){
        System.exit(0);
    }


    void addOSScene(){
        GridPane grid = new GridPane();
        addUserGrids(grid);
        addDiskGrids(grid2);
        addPrintGrids(grid2);

        GridPane layout = new GridPane();
        layout.addColumn(0, grid);
        layout.addColumn(1,grid2);

        grid.setStyle("-fx-background-color: #d5e5ed; -fx-padding: 10; -fx-hgap: 2; -fx-vgap: 2;");
        grid2.setStyle("-fx-background-color: #d5e5ed; -fx-padding: 10; -fx-hgap: 2; -fx-vgap: 2;");
        layout.setStyle("-fx-background-color: #d5e5ed;");
        scene2 = new Scene(layout, 1400, 700);


        StackPane optionsLabel = new StackPane();
        Label title = new Label("OPTIONS");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
        title.setStyle("-fx-padding: 7");
        CornerRadii corn = new CornerRadii(7,7,0,0,false);
        optionsLabel.setBackground(new Background(new BackgroundFill(Color.web("#6d6e71"), corn,Insets.EMPTY)));
        optionsLabel.getChildren().add(title);
        StackPane.setAlignment(title, Pos.CENTER);
        optionsLabel.setPrefWidth(130);
        optionsLabel.setPrefHeight(50);
        grid2.add(optionsLabel, 11, 2);

        VBox vb = new VBox();
        vb.setStyle("-fx-background-color: #bec0c4; -fx-padding: 10; -fx-hgap: 2; -fx-vgap: 2;");
        vb.setPadding(new Insets(10, 50, 50, 50));
        vb.setSpacing(10);

        Label speed = new Label("speed:");
        speed.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        speed.setStyle("-fx-padding: 2; -fx-text-fill: #3a85aa;");
        vb.getChildren().add(speed);

        Button slow = new Button("slow");
        slow.setStyle("-fx-font: 13 verdana;" + 
                        "-fx-background-radius: 7 7 7 7;"+
                        "-fx-text-fill: #3a85aa;");
        slow.setPrefSize(100, 20);
        slow.setOnAction(e -> changeSpeed("slow"));
        vb.getChildren().add(slow);

        Button medium = new Button("medium");
        medium.setStyle("-fx-font: 13 verdana;" + 
                        "-fx-background-radius: 7 7 7 7;"+
                        "-fx-text-fill: #3a85aa;");
        medium.setPrefSize(100, 20);
        medium.setOnAction(e -> changeSpeed("medium"));
        vb.getChildren().add(medium);

        Button fast = new Button("fast");
        fast.setStyle("-fx-font: 13 verdana;" + 
                        "-fx-background-radius: 7 7 7 7;"+
                        "-fx-text-fill: #3a85aa;");
        fast.setPrefSize(100, 20);
        fast.setOnAction(e -> changeSpeed("fast"));
        vb.getChildren().add(fast);

        Label leave = new Label("quit program:");
        leave.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        leave.setStyle(" -fx-vgap: 6; -fx-text-fill: #dc5f46;");
        vb.getChildren().add(leave);

        Button bQuit = new Button("exit");
        bQuit.setStyle("-fx-font: 13 verdana;" + 
                        "-fx-background-radius: 7 7 7 7;"+
                        "-fx-text-fill: #dc5f46;");
        bQuit.setPrefSize(150, 20);
        bQuit.setOnAction(e -> quit());
        vb.getChildren().add(bQuit);


        grid2.add(vb,11, 3);
        window.setScene(scene2);
    }

    void changeSpeed(String speed){
        switch (speed) {
            case "fast":
                animationDelay = 0;
                break;
            case "medium":
                animationDelay = 600;
                break;
            case "slow":
                animationDelay = 2000;
        }
    }

	
}
