package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.ArrayList;

class GameGUI {
    private GUI gui;
    private Client client;
    GridPane boardGrid;
    boolean optionalShoot = false;
    boolean endTurn = false;
    String handPosition;
    String typeOfFire;
    String nameWeapon;
    private ButtonsGUI buttonsGUI;
    private ShootingGUI shootingGUI;

    static final String PURPLE = "purple";
    static final String BLUE = "blue";
    static final String GREEN = "green";
    static final String YELLOW = "yellow";
    static final String GREY = "grey";
    static final int SHOOT_ADRENALINE = 6;
    static final int N_INNER_COLUMN = 3;
    static final int N_COLUMN = 7;
    static final int N_INNER_ROW = 3;
    static final int N_ROW = 5;
    static final int CELL_X = 0;
    static final int CELL_Y = 1;
    static final int CELL_COLOR = 2;
    static final int CELL_TYPE = 3;
    static final int CELL_INSIDE = 4;
    static final int CELL_PLAYER_ON_ME = 6;
    static final int CELL_DOORS = 8;
    static final int CELL_WALLS = 10;
    static final int PLAYER_SKULL = 0;
    static final int PLAYER_ROW_SPAN = 1;
    static final int PLAYER_AMMO = 1;
    static final int PLAYER_COL_SPAN = 3;
    static final int PLAYER_DAMAGE = 3;
    static final int PLAYER_XPOS = 4;
    static final int PLAYER_MARK = 5;
    static final int PLAYER_POWER_UP = 6;
    static final int PLAYER_WEAPON = 8;
    static final int PLAYER_COLOR = 9;
    static final int PLAYER_TURNED = 10;
    static final int PLAYER_FRENZY = 11;
    static final int YOU_XPOS = 0;
    static final int YOU_COL_SPAN = 4;
    static final int YOU_YPOS = 4;
    static final int YOU_POINT = 12;
    static final int YOU_WEAPON = 14;
    static final int YOU_POWERUP = 16;
    static final int YOU_FRENZY_ACTION = 17;
    static final int KILL_ROW = 3;
    static final int KILL_COL = 0;
    static final int KILL_TOT_SKULL = 0;
    static final int KILL_ROW_SPAN = 1;
    static final int KILL_COL_SPAN = 3;
    static final int NUMBER_OF_WEAPON = 3;

    GameGUI(GUI gui, Client client) {
        this.gui = gui;
        this.client = client;
    }

    void setShootingGUI(ShootingGUI shootingGUI) {
        this.shootingGUI = shootingGUI;
    }

    void setButtonsGUI(ButtonsGUI buttonsGUI) {
        this.buttonsGUI = buttonsGUI;
    }

    void informationMessage(Stage stage, String msg){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        StackPane pane2 = new StackPane();

        GridPane grid = new GridPane();

        Rectangle rectangle = new Rectangle();
        rectangleStandard(rectangle,pane);

        Label text = new Label(msg);
        labelSetting(text,"#ffffff",0.8,"-fx-font: 50 Helvetica;");
        GridPane.setHalignment(text,HPos. CENTER);
        GridPane.setValignment(text,VPos. CENTER);

        Button ok = new Button("OK");
        GridPane.setHalignment(ok,HPos. CENTER);
        GridPane.setValignment(ok,VPos. CENTER);
        ok.setOnAction(e-> {
            pane.getChildren().remove(pane2);
            gui.persistenShow = false;
        });

        grid.setVgap(50);

        grid.add(text,0,0);
        grid.add(ok,0,1);
        grid.setAlignment(Pos.CENTER);

        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid);
        pane.getChildren().add(pane2);
    }

    void suspended(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        StackPane pane2 = new StackPane();

        GridPane grid = new GridPane();

        Rectangle rectangle = new Rectangle();
        rectangleStandard(rectangle,pane);

        Label text = new Label("YOU ARE SUSPENDED");
        labelSetting(text,"#ffffff",0.8,"-fx-font: 50 Helvetica;");
        GridPane.setHalignment(text,HPos. CENTER);
        GridPane.setValignment(text,VPos. CENTER);

        Button ok = new Button("OK");
        GridPane.setHalignment(ok,HPos. CENTER);
        GridPane.setValignment(ok,VPos. CENTER);
        ok.setOnAction(e-> {
            gui.getMessageHandler().setSuspend(false);
            client.send("SPD-");
            pane.getChildren().remove(pane2);
        });

        grid.setVgap(50);

        grid.add(text,0,0);
        grid.add(ok,0,1);
        grid.setAlignment(Pos.CENTER);

        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid);
        pane.getChildren().add(pane2);
    }

    void buildWinner(Stage stage, String wario){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        GridPane winnerGrid = new GridPane();

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(20);
        winnerGrid.getRowConstraints().add(row);

        ImageView crownIV = new ImageView(new Image("/images/game/crown.png",pane.getWidth()/10,pane.getHeight()/10,false,false));
        winnerGrid.add(crownIV,0,0,2,1);
        GridPane.setHalignment(crownIV,HPos.CENTER);
        GridPane.setValignment(crownIV,VPos.CENTER);

        int i=1;
        for(String winners : wario.split(";")) {

            String[] singleWinner = winners.split("-");

            Label winner = new Label(singleWinner[0]);
            labelSetting(winner, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
            winnerGrid.add(winner, 0, i);
            GridPane.setHalignment(winner, HPos.CENTER);
            GridPane.setValignment(winner, VPos.CENTER);

            Label points = new Label(singleWinner[1] + " points");
            labelSetting(points, "#ffffff", 0.8, "-fx-font: 30 Helvetica;");
            winnerGrid.add(points, 1, i);
            GridPane.setHalignment(points, HPos.CENTER);
            GridPane.setValignment(points, VPos.CENTER);

            RowConstraints row1 = new RowConstraints();
            row.setPercentHeight(5);
            winnerGrid.getRowConstraints().add(row1);

            i++;
        }

        Button exit = new Button("EXIT");
        winnerGrid.add(exit,0,i,2,1
        );
        GridPane.setHalignment(exit,HPos.CENTER);
        GridPane.setValignment(exit,VPos.CENTER);
        exit.setOnAction(e-> client.send("quit"));

        winnerGrid.setVgap(30);
        winnerGrid.setHgap(50);
        winnerGrid.setAlignment(Pos.CENTER);
        pane.getChildren().add(winnerGrid);
    }

    void columnConstraint(GridPane grid, double nColumn){
        for (int j = 0 ; j < nColumn; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            double dimension = 100/nColumn;
            col.setPercentWidth(dimension);
            grid.getColumnConstraints().add(col);
        }
    }

    void rowConstraint(GridPane grid, double nRow){
        for (int i = 0 ; i < nRow; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            double dimension = 100/nRow;
            row.setPercentHeight(dimension);
            grid.getRowConstraints().add(row);
        }
    }

    void rectangleStandard(Rectangle rectangle, Pane pane){
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());
    }

    void labelSetting(Label label, String color, double opacity , String font){
        label.setTextFill(Color.web(color, opacity));
        label.setStyle(font);
        label.setEffect(new DropShadow());
    }

    String powerUpSwitch(String powerups){
        String[] powerupPlusColor = powerups.split(":");
        String realPowerUp = powerupPlusColor[1];
        switch (powerupPlusColor[0]) {
            case "tagback grenade": {
                realPowerUp = realPowerUp.concat("TagbackGrenade");
                break;
            }
            case "newton": {
                realPowerUp = realPowerUp.concat("Newton");
                break;
            }
            case "teleporter": {
                realPowerUp = realPowerUp.concat("Teleporter");
                break;
            }
            case "targeting scope": {
                realPowerUp = realPowerUp.concat("TargetingScope");
                break;
            }
            default:
        }
        return realPowerUp;
    }
}