package it.polimi.ingsw.view.gui;

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

public class GameGUI extends Application {

    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane pane = new StackPane();
        this.stage = primaryStage;
        Scene scene = new Scene(pane, Toolkit.getDefaultToolkit().getScreenSize().getWidth(),Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setScene(scene);

        GridPane grid = new GridPane();

        Image screenImage = new Image("/images/game/metallicScreen.png");
        ImageView screen = new ImageView(screenImage);
        Image blue = new Image("/images/game/cell/blueCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image red = new Image("/images/game/cell/redCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image yellow = new Image("/images/game/cell/yellowCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image white = new Image("/images/game/cell/whiteCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image black = new Image("/images/game/cell/blackCell.png",pane.getWidth()/7,pane.getHeight()/5,false,false);

        Image wallW = new Image("/images/game/cell/wall/wallW.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image wallN = new Image("/images/game/cell/wall/wallN.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image wallS = new Image("/images/game/cell/wall/wallS.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
        Image wallE = new Image("/images/game/cell/wall/wallE.png",pane.getWidth()/7,pane.getHeight()/5,false,false);
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
        Image ammoBack = new Image("/images/game/ammo/ammoBack.png",pane.getWidth()/7/3,pane.getHeight()/5/3,false,false);
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
/*
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
*/
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
        setTokenPosition(stage,grid,pane,"blue",0,0);
        setTokenPosition(stage,grid,pane,"green",0,0);
        setTokenPosition(stage,grid,pane,"yellow",0,0);
        setTokenPosition(stage,grid,pane,"grey",0,0);
        setTokenPosition(stage,grid,pane,"purple",0,0);
        //setTokenPosition(stage,grid,pane,"blue",1,0); TODO REMOVE THE PREVIOUS TOKEN

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
                                                grid4.add(button2,0,1);
                                                grid4.add(quit1,1,1);
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
                                                //quit.setOnAction(ev -> {
                                                // messageHandler.setToSend("quit");
                                                //            synchronized (client){
                                                //                client.notify();
                                                //            });
                                            });
                                        }
                        }
                    }
        }

        //pane
        pane.getChildren().add(screen);
        pane.getChildren().add(grid);
        pane.getChildren().add(grid2);

        //stage
        stage.setResizable(true);
        stage.show();
    }

    public void buildMap(){

    }

    public void setTokenPosition(Stage stage,GridPane grid,StackPane pane, String color, int x, int y){
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
}
