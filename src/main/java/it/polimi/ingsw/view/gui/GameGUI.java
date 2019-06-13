package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import it.polimi.ingsw.communication.client.MessageHandler;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.ArrayList;

public class GameGUI {
    private Stage stage;
    private GUI gui;
    private MessageHandler messageHandler;
    private Client client;
    private static final int N_COLUMN= 7;
    private static final int N_ROW= 5;
    private static final int CELL_X= 0;
    private static final int CELL_Y= 1;
    private static final int CELL_COLOR= 2;
    private static final int CELL_TYPE= 3;
    private static final int CELL_INSIDE= 4;
    private static final int CELL_DOORS= 8;
    private static final int CELL_WALLS= 10;


    public GameGUI(GUI gui, MessageHandler messageHandler, Client client) {
        this.gui = gui;
        this.messageHandler = messageHandler;
        this.client = client;
    }

    public void buildBoard(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        stage.setResizable(true);
        GridPane grid = new GridPane();

        //backGround image
        Image screenImage = new Image("/images/game/metallicScreen.png");
        ImageView screen = new ImageView(screenImage);
        screen.fitWidthProperty().bind(pane.widthProperty());
        screen.fitHeightProperty().bind(pane.heightProperty());

        //cell
        Image blue = new Image("/images/game/cell/blueCell.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image red = new Image("/images/game/cell/redCell.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image yellow = new Image("/images/game/cell/yellowCell.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image white = new Image("/images/game/cell/whiteCell.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image purple = new Image("/images/game/cell/purpleCell.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image green = new Image("/images/game/cell/greenCell.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);

        //wall
        Image wallW = new Image("/images/game/cell/wall/wallW.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image wallN = new Image("/images/game/cell/wall/wallN.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image wallS = new Image("/images/game/cell/wall/wallS.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image wallE = new Image("/images/game/cell/wall/wallE.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);

        //door
        Image doorW = new Image("/images/game/cell/door/doorW.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image doorE = new Image("/images/game/cell/door/doorE.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image doorN = new Image("/images/game/cell/door/doorN.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);
        Image doorS = new Image("/images/game/cell/door/doorS.png",pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false);

        //ammo
        Image b2pu1 = new Image("/images/game/ammo/tiles/b2pu1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image r1b1pu1 = new Image("/images/game/ammo/tiles/r1b1pu1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image r1b2 = new Image("/images/game/ammo/tiles/r1b2.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image r2b1 = new Image("/images/game/ammo/tiles/r2b1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image r2pu1 = new Image("/images/game/ammo/tiles/r2pu1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image y1b1pu1 = new Image("/images/game/ammo/tiles/y1b1pu1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image y1b2 = new Image("/images/game/ammo/tiles/y1b2.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image y1r1pu1 = new Image("/images/game/ammo/tiles/y1r1pu1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image y1r2 = new Image("/images/game/ammo/tiles/y1r2.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image y2b1 = new Image("/images/game/ammo/tiles/y2b1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image y2pu1 = new Image("/images/game/ammo/tiles/y2pu1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);
        Image y2r1 = new Image("/images/game/ammo/tiles/y2r1.png",pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false);

        //TODO costruire le ImageView con dentro new Image con stringa del percorso costruita precisa

        //grid column constraint
        for (int j = 0 ; j < N_COLUMN; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            col.setPercentWidth(100/N_COLUMN);
            grid.getColumnConstraints().add(col);
        }

        //grid row constraint
        for (int i = 0 ; i < N_ROW; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            row.setPercentHeight(100/N_ROW);
            grid.getRowConstraints().add(row);
        }

        //cells
        for (ArrayList<ArrayList<String>> room: gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if(!cell.isEmpty()) {
                    int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                    int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                    switch (cell.get(CELL_COLOR)) {
                        case "blue": {
                            grid.add(new ImageView(blue), xPos, yPos);
                            break;
                        }
                        case "red": {
                            grid.add(new ImageView(red), xPos, yPos);
                            break;
                        }
                        case "yellow": {
                            grid.add(new ImageView(yellow), xPos, yPos);
                            break;
                        }
                        case "white": {
                            grid.add(new ImageView(white), xPos, yPos);
                            break;
                        }
                        case "purple": {
                            grid.add(new ImageView(purple), xPos, yPos);
                            break;
                        }
                        case "green": {
                            grid.add(new ImageView(green), xPos, yPos);
                            break;
                        }
                        default: {

                        }
                    }
                }
            }
        }

        //doors
        for (ArrayList<ArrayList<String>> room: gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if(!cell.get(CELL_DOORS).isEmpty()){
                    for(String singleDoor: cell.get(CELL_DOORS).split("°")){
                        int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                        int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                        ArrayList<Integer> doorCoordinate = new ArrayList<>();
                        for(String s: singleDoor.split("'")){
                            doorCoordinate.add(Integer.valueOf(s));
                        }
                        if(xPos+1 == doorCoordinate.get(CELL_X)){
                            if(yPos+1 > doorCoordinate.get(CELL_Y)){
                                grid.add(new ImageView(doorN),xPos,yPos);
                            }
                            if(yPos+1 < doorCoordinate.get(CELL_Y)){
                                grid.add(new ImageView(doorS),xPos,yPos);
                            }
                        }
                        else {
                            if(xPos+1 > doorCoordinate.get(CELL_X)){
                                grid.add(new ImageView(doorW),xPos,yPos);
                            }
                            if(xPos+1 < doorCoordinate.get(CELL_X)){
                                grid.add(new ImageView(doorE),xPos,yPos);
                            }
                        }
                    }
                }
            }
        }

        //walls
        for (ArrayList<ArrayList<String>> room: gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                for(String s: cell.get(CELL_WALLS).split("'")){
                    switch (s){
                        case "N":{
                            grid.add(new ImageView(wallN), xPos, yPos);
                            break;
                        }
                        case "S":{
                            grid.add(new ImageView(wallS), xPos, yPos);
                            break;
                        }
                        case "W":{
                            grid.add(new ImageView(wallW), xPos, yPos);
                            break;
                        }
                        case "E":{
                            grid.add(new ImageView(wallE), xPos, yPos);
                            break;
                        }
                        default:

                    }
                }
            }
        }

        //Ammopoint
        for (ArrayList<ArrayList<String>> room: gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                if(cell.get(CELL_TYPE).equals("AmmoPoint")){
                    String ammoName = "";
                    for(String s: cell.get(CELL_INSIDE).split("'")){
                        String[] difi = s.split(":");
                        if(s.startsWith("Y:")){
                            if(!difi[1].equals("0")){
                                ammoName = ammoName.concat("y").concat(difi[1]);
                            }
                        }
                        else {
                            if(s.startsWith("R:")){
                                if(!difi[1].equals("0")){
                                    ammoName = ammoName.concat("r").concat(difi[1]);
                                }
                            }
                            else {
                                if (s.startsWith("B:")) {
                                    if (!difi[1].equals("0")) {
                                        ammoName = ammoName.concat("b").concat(difi[1]);
                                    }
                                }
                                else {
                                    if (s.startsWith("PU:")) {
                                        if (!difi[1].equals("0")) {
                                            ammoName = ammoName.concat("pu").concat(difi[1]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //TODO caso in cui non ci sia l'ammoTile
                    ImageView ammoTile = new ImageView(new Image("/images/game/ammo/tiles/".concat(ammoName).concat(".png"),pane.getWidth()/N_COLUMN/3,pane.getHeight()/N_ROW/3,false,false));
                    grid.add(ammoTile, xPos, yPos);
                    GridPane.setHalignment(ammoTile,HPos.CENTER);
                    GridPane.setValignment(ammoTile,VPos.CENTER);
                    System.out.println(ammoName);
                }
                else{

                }
            }
        }

        //TODO aggiungere sopra i giocatori






        //pane.add
        pane.getChildren().add(screen);
        pane.getChildren().add(grid);
    }

    /*
    public void buildMap(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        stage.setResizable(true);
        GridPane grid = new GridPane();

        //cell
        Image screenImage = new Image("/images/game/metallicScreen.png");
        ImageView screen = new ImageView(screenImage);
        Image blue = new Image("/images/game/cell/blueCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image red = new Image("/images/game/cell/redCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image yellow = new Image("/images/game/cell/yellowCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image white = new Image("/images/game/cell/whiteCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);

        //wall
        Image wallW = new Image("/images/game/cell/wall/wallW.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image wallN = new Image("/images/game/cell/wall/wallN.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image wallS = new Image("/images/game/cell/wall/wallS.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image wallE = new Image("/images/game/cell/wall/wallE.png",pane.getWidth()/7,pane.getHeight()/5,false,false);

        //door
        Image doorW = new Image("/images/game/cell/door/doorW.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image doorE = new Image("/images/game/cell/door/doorE.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image doorN = new Image("/images/game/cell/door/doorN.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image doorS = new Image("/images/game/cell/door/doorS.png",pane.getWidth()/7,pane.getHeight()/5,false,false);

        //plance
        Image bluePlayer = new Image("/images/game/plance/bluePlayer.png",pane.getWidth()/7*4,pane.getHeight()/5,false,false);
        Image yellowPlayer = new Image("/images/game/plance/yellowPlayer.png",pane.getWidth()/7*3,pane.getHeight()/5,false,false);
        Image greenPlayer = new Image("/images/game/plance/greenPlayer.png",pane.getWidth()/7*3,pane.getHeight()/5,false,false);
        Image greyPlayer = new Image("/images/game/plance/greyPlayer.png",pane.getWidth()/7*3,pane.getHeight()/5,false,false);
        Image purplePlayer = new Image("/images/game/plance/purplePlayer.png",pane.getWidth()/7*3,pane.getHeight()/5,false,false);

        //ammo
        Image ammoBack = new Image("/images/game/ammo/tiles/ammoBack.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
        Image blueAmmo = new Image("/images/game/ammo/blueAmmo.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
        ImageView blueammoIV = new ImageView(blueAmmo);
        Image yellowAmmo = new Image("/images/game/ammo/yellowAmmo.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
        ImageView yellowAmmoIV = new ImageView(yellowAmmo);
        Image redAmmo = new Image("/images/game/ammo/redAmmo.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
        ImageView redAmmoIV = new ImageView(redAmmo);

        //killshot track
        Image killshotTrack = new Image("/images/game/killshotTrack.png",pane.getWidth()/7*3,pane.getHeight()/7,false,false);

        //points
        Label points = new Label("YOUR POINTS:"+"\n"+"0");
        points.setTextFill(Color.web("#ffffff", 0.8));
        points.setStyle("-fx-font: 30 Helvetica;");
        points.setEffect(new DropShadow());

        //weapon
        Image weaponBack = new Image("/images/game/weapons/weaponBack.png",pane.getWidth()/7,pane.getHeight()/3,false,false);

        //powerUp
        Image powerUpBack = new Image("/images/game/powerUps/powerUpBack.png",pane.getWidth()/10,pane.getHeight()/5,false,false);

        //image
        screen.fitWidthProperty().bind(pane.widthProperty());
        screen.fitHeightProperty().bind(pane.heightProperty());

        //grid column constraint
        for (int j = 0 ; j < 7; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(col);
        }

        //grid row constraint
        for (int i = 0 ; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }

        //grid
        grid.add(new ImageView(blue), 0, 0);
        grid.add(new ImageView(wallW),0,0);
        grid.add(new ImageView(wallN),0,0);
        grid.add(new ImageView(doorS),0,0);

        grid.add(new ImageView(blue),1,0);
        grid.add(new ImageView(wallN),1,0);
        grid.add(new ImageView(wallS),1,0);

        grid.add(new ImageView(blue),2,0);
        grid.add(new ImageView(wallN),2,0);
        grid.add(new ImageView(wallE),2,0);
        grid.add(new ImageView(doorS),2,0);

        grid.add(new ImageView(red),0,1);
        grid.add(new ImageView(wallW),0,1);
        grid.add(new ImageView(wallS),0,1);
        grid.add(new ImageView(doorN),0,1);

        grid.add(new ImageView(red),1,1);
        grid.add(new ImageView(wallN),1,1);
        grid.add(new ImageView(doorS),1,1);

        grid.add(new ImageView(red),2,1);
        grid.add(new ImageView(wallS),2,1);
        grid.add(new ImageView(doorE),2,1);
        grid.add(new ImageView(doorN),2,1);

        grid.add(new ImageView(white),1,2);
        grid.add(new ImageView(wallW),1,2);
        grid.add(new ImageView(wallS),1,2);
        grid.add(new ImageView(doorN),1,2);

        grid.add(new ImageView(white),2,2);
        grid.add(new ImageView(wallN),2,2);
        grid.add(new ImageView(wallS),2,2);
        grid.add(new ImageView(doorE),2,2);

        grid.add(new ImageView(yellow),3,1);
        grid.add(new ImageView(wallN),3,1);
        grid.add(new ImageView(wallE),3,1);
        grid.add(new ImageView(doorW),3,1);

        grid.add(new ImageView(yellow),3,2);
        grid.add(new ImageView(wallS),3,2);
        grid.add(new ImageView(wallE),3,2);
        grid.add(new ImageView(doorW),3,2);

        //plance
        grid.add(new ImageView(yellowPlayer),4,0,3,1);
        grid.add(new ImageView(greenPlayer),4,1,3,1);
        grid.add(new ImageView(greyPlayer),4,2,3,1);
        grid.add(new ImageView(purplePlayer),4,3,3,1);
        grid.add(new ImageView(bluePlayer),0,4,4,1);

        //killshot track and points
        grid.add(new ImageView(killshotTrack),0,3,3,1);
        grid.add(points,3,3);
        GridPane.setHalignment(points,HPos. CENTER);
        GridPane.setValignment(points,VPos. CENTER);

        //set token position
        setTokenPosition(grid,pane,"blue",0,0);
        setTokenPosition(grid,pane,"green",0,0);
        setTokenPosition(grid,pane,"yellow",0,0);
        setTokenPosition(grid,pane,"grey",0,0);
        setTokenPosition(grid,pane,"purple",0,0);

        //info button,ammo and button
        GridPane grid2 = new GridPane();
        for (int i = 0 ; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20);
            grid2.getRowConstraints().add(row);
        }
        for (int j = 0 ; j < 7; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(14.2857);
            grid2.getColumnConstraints().add(col);
        }
        for(int i=0;i<5;i++)
            for(int j=0;j<7;j++){
                if((j==6 && i!=4)||(j==3 && i==4)){
                    Button button = new Button("INFO");
                    grid2.add(button, j, i);
                    button.setOnAction(e ->{
                        GridPane grid3 = new GridPane();
                        Rectangle rectangle = new Rectangle();
                        Button button2 = new Button("BACK");
                        Label text = new Label("x2");
                        text.setTextFill(Color.web("#ffffff", 0.8));
                        text.setStyle("-fx-font: 40 Helvetica;");
                        text.setEffect(new DropShadow());
                        Label text1 = new Label("x1");
                        text1.setTextFill(Color.web("#ffffff", 0.8));
                        text1.setStyle("-fx-font: 40 Helvetica;");
                        text1.setEffect(new DropShadow());
                        Label text2 = new Label("x0");
                        text2.setTextFill(Color.web("#ffffff", 0.8));
                        text2.setStyle("-fx-font: 40 Helvetica;");
                        text2.setEffect(new DropShadow());
                        Label text3 = new Label("x3");
                        text3.setTextFill(Color.web("#ffffff", 0.8));
                        text3.setStyle("-fx-font: 40 Helvetica;");
                        text3.setEffect(new DropShadow());
                        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
                        rectangle.setEffect(new BoxBlur());
                        rectangle.widthProperty().bind(pane.widthProperty());
                        rectangle.heightProperty().bind(pane.heightProperty());
                        grid3.add(new ImageView(weaponBack),0,0);
                        grid3.add(new ImageView(weaponBack),1,0);
                        grid3.add(new ImageView(weaponBack),2,0);
                        grid3.add(new Text("\t"),3,0);
                        grid3.add(new ImageView(powerUpBack),4,0);
                        grid3.add(text,4,0);
                        grid3.add(new Text("\t"),5,0);
                        grid3.add(blueammoIV,6,0);
                        grid3.add(text1,7,0);
                        grid3.add(yellowAmmoIV,6,0);
                        grid3.add(text2,7,0);
                        grid3.add(redAmmoIV,6,0);
                        grid3.add(text3,7,0);
                        grid3.add(button2,2,1);
                        pane.getChildren().add(rectangle);
                        pane.getChildren().add(grid3);
                        grid3.setHgap(30);
                        grid3.setVgap(20);
                        GridPane.setHalignment(button2,HPos.CENTER);
                        GridPane.setValignment(button2,VPos.CENTER);
                        GridPane.setHalignment(text,HPos.CENTER);
                        GridPane.setValignment(text,VPos.BOTTOM);
                        GridPane.setHalignment(blueammoIV,HPos.CENTER);
                        GridPane.setValignment(blueammoIV,VPos.TOP);
                        GridPane.setHalignment(text1,HPos.RIGHT);
                        GridPane.setValignment(text1,VPos.TOP);
                        GridPane.setHalignment(yellowAmmoIV,HPos.CENTER);
                        GridPane.setValignment(yellowAmmoIV,VPos.CENTER);
                        GridPane.setHalignment(text2,HPos.RIGHT);
                        GridPane.setValignment(text2,VPos.CENTER);
                        GridPane.setHalignment(redAmmoIV,HPos.CENTER);
                        GridPane.setValignment(redAmmoIV,VPos.BOTTOM);
                        GridPane.setHalignment(text3,HPos.RIGHT);
                        GridPane.setValignment(text3,VPos.BOTTOM);
                        grid3.setAlignment(Pos.CENTER);
                        button2.setOnAction(ev -> {
                            pane.getChildren().remove(grid3);
                            pane.getChildren().remove(rectangle);
                        });
                    });
                    GridPane.setHalignment(button, HPos.CENTER);
                    GridPane.setValignment(button, VPos.BOTTOM);
                }
                else{
                    if((i==0 && j==0) ||(i==1 && j==0)||(i==1 && j==1)||(i==1 && j==2)||(i==3 && j==1)||(i==2 && j==1)||(i==2 && j==2)){
                        ImageView ammoBackIV = new ImageView(ammoBack);
                        grid2.add(ammoBackIV, i, j);
                        GridPane.setHalignment(ammoBackIV, HPos.CENTER);
                        GridPane.setValignment(ammoBackIV, VPos.CENTER);

                    }
                    else{
                        if((i==2 && j==0)||(i==0 && j==1)||(i==3 && j==2)){
                            Button button = new Button("Store");
                            grid2.add(button,i,j);
                            GridPane.setHalignment(button,HPos.CENTER);
                            GridPane.setValignment(button,VPos.CENTER);
                            button.setOnAction(e ->{
                                GridPane grid4 = new GridPane();
                                Rectangle rectangle = new Rectangle();
                                Button button2 = new Button("BACK");
                                rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
                                rectangle.setEffect(new BoxBlur());
                                rectangle.widthProperty().bind(pane.widthProperty());
                                rectangle.heightProperty().bind(pane.heightProperty());
                                grid4.add(new ImageView(weaponBack),0,0);
                                grid4.add(new ImageView(weaponBack),1,0);
                                grid4.add(new ImageView(weaponBack),2,0);
                                grid4.add(button2,1,1);
                                pane.getChildren().add(rectangle);
                                pane.getChildren().add(grid4);
                                grid4.setHgap(30);
                                grid4.setVgap(20);
                                GridPane.setHalignment(button2,HPos.CENTER);
                                GridPane.setValignment(button2,VPos.CENTER);
                                grid4.setAlignment(Pos.CENTER);
                                button2.setOnAction(ev -> {
                                    pane.getChildren().remove(grid4);
                                    pane.getChildren().remove(rectangle);
                                });
                            });
                        }
                        else
                        if(i==4 && j==4){
                            Button actions = new Button("Actions");
                            grid2.add(actions,j,i);
                            GridPane.setHalignment(actions,HPos.CENTER);
                            GridPane.setValignment(actions,VPos.CENTER);
                            actions.setOnAction(e ->{
                                GridPane grid4 = new GridPane();
                                Rectangle rectangle = new Rectangle();
                                Button shoot = new Button("SHOOT");
                                Button run = new Button("RUN");
                                Button grab = new Button("GRAB");
                                Button button2 = new Button("BACK");
                                rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
                                rectangle.setEffect(new BoxBlur());
                                rectangle.widthProperty().bind(pane.widthProperty());
                                rectangle.heightProperty().bind(pane.heightProperty());
                                grid4.add(shoot,0,0);
                                grid4.add(run,1,0);
                                grid4.add(grab,2,0);
                                grid4.add(button2,1,1);
                                pane.getChildren().add(rectangle);
                                pane.getChildren().add(grid4);
                                grid4.setHgap(70);
                                grid4.setVgap(50);
                                GridPane.setHalignment(button2,HPos.CENTER);
                                GridPane.setValignment(button2,VPos.CENTER);
                                grid4.setAlignment(Pos.CENTER);
                                button2.setOnAction(ev -> {
                                    pane.getChildren().remove(grid4);
                                    pane.getChildren().remove(rectangle);
                                });
                            });
                        }
                        else
                        if(i==4 && j==5){
                            Button powerUps = new Button("Use PowerUps");
                            grid2.add(powerUps,j,i);
                            GridPane.setHalignment(powerUps,HPos.CENTER);
                            GridPane.setValignment(powerUps,VPos.CENTER);
                            powerUps.setOnAction(e ->{
                                spawn(pane);
                            });
                        }
                        else
                        if(i==4 && j==6){
                            Button quit = new Button("Quit");
                            grid2.add(quit,j,i);
                            GridPane.setHalignment(quit,HPos.CENTER);
                            GridPane.setValignment(quit,VPos.CENTER);
                            quit.setOnAction(e ->{
                                GridPane grid4 = new GridPane();
                                ColumnConstraints col1 = new ColumnConstraints();
                                col1.setPercentWidth(20);
                                ColumnConstraints col2 = new ColumnConstraints();
                                col2.setPercentWidth(20);
                                grid4.getColumnConstraints().addAll(col1,col2);
                                Rectangle rectangle = new Rectangle();
                                Label text = new Label("Are you sure to quit?");
                                text.setTextFill(Color.web("#ffffff", 0.8));
                                text.setStyle("-fx-font: 60 Helvetica;");
                                text.setEffect(new DropShadow());
                                Button quit1 = new Button("QUIT");
                                Button button2 = new Button("BACK");
                                rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
                                rectangle.setEffect(new BoxBlur());
                                rectangle.widthProperty().bind(pane.widthProperty());
                                rectangle.heightProperty().bind(pane.heightProperty());
                                grid4.add(text,0,0,2,1);
                                grid4.add(button2,1,1);
                                grid4.add(quit1,0,1);
                                pane.getChildren().add(rectangle);
                                pane.getChildren().add(grid4);
                                grid4.setHgap(70);
                                grid4.setVgap(50);
                                grid4.setAlignment(Pos.CENTER);
                                GridPane.setHalignment(text,HPos.CENTER);
                                GridPane.setValignment(text,VPos.CENTER);
                                GridPane.setHalignment(button2,HPos.CENTER);
                                GridPane.setValignment(button2,VPos.CENTER);
                                GridPane.setHalignment(quit1,HPos.CENTER);
                                GridPane.setValignment(quit1,VPos.CENTER);
                                button2.setOnAction(ev -> {
                                    pane.getChildren().remove(grid4);
                                    pane.getChildren().remove(rectangle);
                                });
                                quit1.setOnAction(ev -> {
                                    stage.close();
                                });
                            });
                        }
                    }
                }
            }

        //pane
        pane.getChildren().add(screen);
        pane.getChildren().add(grid);

        //killshot track
        drawKillshotTrack(pane,5,5);

        //blood
        drawBluePlayer(pane);
        drawYellowPlayer(pane);

        pane.getChildren().add(grid2);

        //TODO IF THERE ARE ANY THROWED EXCEPTION
        //informationMessage(pane);

        //TODO IF THE PLAYER NEEDS TO SPAWN
        //spawn(stage);

        //TODO IF THE PLAYER WANTS TO RELOAD WEAPONS
        //reload(pane);

        stage.show();
    }
*/

    public void drawYellowPlayer(Pane pane){
        drawBloodOnYellow(pane);
        drawMarkOnYellow(pane);
        drawSkullOnYellow(pane);
    }

    public void drawSkullOnYellow(Pane pane){
        //skull
        Image skull = new Image("/images/game/redSkull.png",pane.getWidth()/35,pane.getHeight()/20,false,false);

        //pane
        Pane pane2 = new Pane();

        for(int i=0;i<6;i++){
            ImageView skullIV = new ImageView(skull);
            skullIV.setX(pane.getWidth()/1.5238 + (i * pane.getWidth()/42.6666));
            skullIV.setY(pane.getHeight()/6.75);
            pane2.getChildren().add(skullIV);
        }
        pane.getChildren().add(pane2);
    }

    public void drawMarkOnYellow(Pane pane){
        //blood
        Image greyMark = new Image("/images/game/blood/greyBlood.png",pane.getWidth()/40,pane.getHeight()/25,false,false);

        //pane
        Pane pane2 = new Pane();

        for(int i=0;i<12;i++){
            ImageView greyMarkIV = new ImageView(greyMark);
            greyMarkIV.setX(pane.getWidth()/1.1428 - (i * pane.getWidth()/106.6666));
            greyMarkIV.setY(pane.getHeight()/360);
            pane2.getChildren().add(greyMarkIV);
        }
        pane.getChildren().add(pane2);
    }

    public void drawBloodOnYellow(Pane pane){
        //blood
        Image greenBlood = new Image("/images/game/blood/greenBlood.png",pane.getWidth()/25,pane.getHeight()/15,false,false);

        Pane pane2 = new Pane();

        for(int i=0;i<12;i++){
            ImageView greenBloodIV = new ImageView(greenBlood);
            greenBloodIV.setX(pane.getWidth()/1.6666 + (i * pane.getWidth()/40.8510));
            greenBloodIV.setY(pane.getHeight()/14.4);
            pane2.getChildren().add(greenBloodIV);
        }
        pane.getChildren().add(pane2);
    }

    public void drawBluePlayer(Pane pane){
        drawBloodOnBlue(pane);
        drawMarkOnBlue(pane);
        drawSkullOnBlue(pane);
    }

    public void drawSkullOnBlue(Pane pane){
        //skull
        Image skull = new Image("/images/game/redSkull.png",pane.getWidth()/35,pane.getHeight()/20,false,false);

        //pane
        Pane pane2 = new Pane();

        for(int i=0;i<6;i++){
            ImageView skullIV = new ImageView(skull);
            skullIV.setX(pane.getWidth()/8.3478 + (i * pane.getWidth()/33.6842));
            skullIV.setY(pane.getHeight()/1.0588);
            pane2.getChildren().add(skullIV);
        }
        pane.getChildren().add(pane2);
    }

    public void drawMarkOnBlue(Pane pane){
        //blood
        Image yellowMark = new Image("/images/game/blood/yellowBlood.png",pane.getWidth()/40,pane.getHeight()/25,false,false);

        //pane
        Pane pane2 = new Pane();

        for(int i=0;i<12;i++){
            ImageView yellowMarkIV = new ImageView(yellowMark);
            yellowMarkIV.setX(pane.getWidth()/2.4303 - (i * pane.getWidth()/76.8));
            yellowMarkIV.setY(pane.getHeight()/1.2485);
            pane2.getChildren().add(yellowMarkIV);
        }
        pane.getChildren().add(pane2);
    }

    public void drawBloodOnBlue(Pane pane){
        //blood
        Image purpleBlood = new Image("/images/game/blood/purpleBlood.png",pane.getWidth()/25,pane.getHeight()/15,false,false);

        Pane pane2 = new Pane();

        for(int i=0;i<12;i++){
            ImageView purpleBloodIV = new ImageView(purpleBlood);
            purpleBloodIV.setX(pane.getWidth()/21.3333 + (i * pane.getWidth()/31.4754));
            purpleBloodIV.setY(pane.getHeight()/1.1489);
            pane2.getChildren().add(purpleBloodIV);
        }
        pane.getChildren().add(pane2);
    }

    public void drawKillshotTrack(Pane pane,int tot, int num){
        //skull
        Image skull = new Image("/images/game/redSkull.png",pane.getWidth()/30,pane.getHeight()/15,false,false);


        //blood
        Image blueBlood = new Image("/images/game/blood/blueBlood.png",pane.getWidth()/25,pane.getHeight()/15,false,false);

        //pane2
        Pane pane2 = new Pane();

        for(int i = 0;i<tot;i++) {
            if(i<num) {
                ImageView skullIV = new ImageView(skull);
                skullIV.setX(pane.getWidth()/3.02 - (i * pane.getWidth()/21.5));
                skullIV.setY(pane.getHeight()/1.5);
                pane2.getChildren().add(skullIV);
            }
            else {
                ImageView blueBloodIV = new ImageView(blueBlood);
                blueBloodIV.setX(pane.getWidth()/3.02 - (i * pane.getWidth()/21.5));
                blueBloodIV.setY(pane.getHeight()/1.5);
                pane2.getChildren().add(blueBloodIV);
            }
        }
        pane.getChildren().add(pane2);
    }

    public void setTokenPosition(GridPane grid,StackPane pane, String color, int x, int y){
        //token
        Image blueToken = new Image("/images/game/tokens/blueToken.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
        Image yellowToken = new Image("/images/game/tokens/yellowToken.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
        Image greenToken = new Image("/images/game/tokens/greenToken.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
        Image greyToken = new Image("/images/game/tokens/greyToken.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
        Image purpleToken = new Image("/images/game/tokens/purpleToken.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);

        //ImageView token
        ImageView blueTokenIV = new ImageView(blueToken);
        ImageView yellowTokenIV = new ImageView(yellowToken);
        ImageView greenTokenIV = new ImageView(greenToken);
        ImageView greyTokenIV = new ImageView(greyToken);
        ImageView purpleTokenIV = new ImageView(purpleToken);

        switch(color) {
            case("blue"):{
                grid.getChildren().remove(blueTokenIV);
                grid.add(blueTokenIV,x,y);
                GridPane.setHalignment(blueTokenIV,HPos.LEFT);
                GridPane.setValignment(blueTokenIV,VPos.CENTER);
                break;
            }
            case("yellow"):{
                grid.getChildren().remove(yellowTokenIV);
                grid.add(yellowTokenIV,x,y);
                GridPane.setHalignment(yellowTokenIV,HPos.LEFT);
                GridPane.setValignment(yellowTokenIV,VPos.BOTTOM);
                break;
            }
            case("green"):{
                grid.getChildren().remove(greenTokenIV);
                grid.add(greenTokenIV,x,y);
                GridPane.setHalignment(greenTokenIV,HPos.CENTER);
                GridPane.setValignment(greenTokenIV,VPos.BOTTOM);
                break;
            }
            case("grey"):{
                grid.getChildren().remove(greyTokenIV);
                grid.add(greyTokenIV,x,y);
                GridPane.setHalignment(greyTokenIV,HPos.RIGHT);
                GridPane.setValignment(greyTokenIV,VPos.BOTTOM);
                break;
            }
            case("purple"):{
                grid.getChildren().remove(purpleTokenIV);
                grid.add(purpleTokenIV,x,y);
                GridPane.setHalignment(purpleTokenIV,HPos.RIGHT);
                GridPane.setValignment(purpleTokenIV,VPos.CENTER);
                break;
            }
            default:{
                System.out.println("ERROR, Invalid DATA");
            }
        }
    }

    public void informationMessage(Pane pane){
        StackPane pane2 = new StackPane();
        GridPane grid2 = new GridPane();
        Rectangle rectangle = new Rectangle();
        Label text = new Label("Problems...");
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 60 Helvetica;");
        text.setEffect(new DropShadow());
        Button ok = new Button("OK");
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());
        grid2.add(text,0,0);
        grid2.add(ok,0,1);
        ok.setOnAction(e->{
            pane.getChildren().remove(pane2);
        });
        grid2.setAlignment(Pos.CENTER);
        GridPane.setHalignment(text,HPos. CENTER);
        GridPane.setValignment(text,VPos. CENTER);
        GridPane.setHalignment(ok,HPos. CENTER);
        GridPane.setValignment(ok,VPos. CENTER);
        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid2);
        pane.getChildren().add(pane2);

    }

    public void spawn(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
    }

    /*
        public void spawn(Stage stage){
        //powerUps
        Image blueNewton = new Image("/images/game/powerUps/blueNewton.png",pane.getWidth()/10,pane.getHeight()/5,false,false);
        ImageView blueNewtonIV = new ImageView(blueNewton);
        Image redTagBackGrenade = new Image("/images/game/powerUps/redTagBackGrenade.png",pane.getWidth()/10,pane.getHeight()/5,false,false);
        ImageView redTagBackGrenadeIV = new ImageView(redTagBackGrenade);
        Image yellowTargetingScope = new Image("images/game/powerUps/yellowTargetingScope.png",pane.getWidth()/10,pane.getHeight()/5,false,false);
        ImageView yellowTargetingScopeIV = new ImageView(yellowTargetingScope);

        StackPane pane2 = new StackPane();
        GridPane grid2 = new GridPane();
        //grid column constraint
        for (int j = 0 ; j < 3; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            col.setPercentWidth(20);
            grid2.getColumnConstraints().add(col);
        }

        Rectangle rectangle = new Rectangle();
        Label text = new Label("Choose a powerUp");
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 60 Helvetica;");
        text.setEffect(new DropShadow());
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());
        grid2.add(text,0,0,3,1);
        grid2.setAlignment(Pos.CENTER);
        GridPane.setHalignment(text,HPos. CENTER);
        GridPane.setValignment(text,VPos. CENTER);
        grid2.add(blueNewtonIV,0,1);
        blueNewtonIV. addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("blue");
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(blueNewtonIV,HPos. CENTER);
        GridPane.setValignment(blueNewtonIV,VPos. CENTER);
        grid2.add(redTagBackGrenadeIV,1,1);
        redTagBackGrenadeIV. addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("red");
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(redTagBackGrenadeIV,HPos. CENTER);
        GridPane.setValignment(redTagBackGrenadeIV,VPos. CENTER);
        grid2.add(yellowTargetingScopeIV,2,1);
        yellowTargetingScopeIV. addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("yellow");
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(yellowTargetingScopeIV,HPos. CENTER);
        GridPane.setValignment(yellowTargetingScopeIV,VPos. CENTER);
        grid2.setHgap(70);
        grid2.setVgap(50);
        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid2);
        pane.getChildren().add(pane2);
    }

     */

    public void reload(Pane pane){
        //weapon
        Image lockRifle = new Image("images/game/weapons/lockRifle.png",pane.getWidth()/7,pane.getHeight()/3,false,false);
        ImageView lockRifleIV = new ImageView(lockRifle);
        Image furnace = new Image("images/game/weapons/furnace.png",pane.getWidth()/7,pane.getHeight()/3,false,false);
        ImageView furnaceIV = new ImageView(furnace);
        Image zx2 = new Image("images/game/weapons/zx2.png",pane.getWidth()/7,pane.getHeight()/3,false,false);
        ImageView zx2IV = new ImageView(zx2);

        StackPane pane2 = new StackPane();
        GridPane grid2 = new GridPane();
        //grid column constraint
        for (int j = 0 ; j < 3; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            col.setPercentWidth(20);
            grid2.getColumnConstraints().add(col);
        }

        Rectangle rectangle = new Rectangle();
        Label text = new Label("Choose a weapon to reload");
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 60 Helvetica;");
        text.setEffect(new DropShadow());
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());
        grid2.add(text,0,0,3,1);
        grid2.setAlignment(Pos.CENTER);
        GridPane.setHalignment(text,HPos. CENTER);
        GridPane.setValignment(text,VPos. CENTER);
        grid2.add(lockRifleIV,0,1);
        lockRifleIV. addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("reloaded blue");
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(lockRifleIV,HPos. CENTER);
        GridPane.setValignment(lockRifleIV,VPos. CENTER);
        grid2.add(furnaceIV,1,1);
        furnaceIV. addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("reloaded red");
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(furnaceIV,HPos. CENTER);
        GridPane.setValignment(furnaceIV,VPos. CENTER);
        grid2.add(zx2IV,2,1);
        zx2IV. addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("reloaded yellow");
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(zx2IV,HPos. CENTER);
        GridPane.setValignment(zx2IV,VPos. CENTER);
        grid2.setHgap(70);
        grid2.setVgap(50);
        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid2);
        pane.getChildren().add(pane2);
    }
}
