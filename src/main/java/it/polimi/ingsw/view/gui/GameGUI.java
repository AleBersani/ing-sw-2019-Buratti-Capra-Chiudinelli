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

/**
 * This class contains all the methods to draw the graphics information of the game on the screen
 */
class GameGUI {
    /**
     * Reference to GUI
     */
    private GUI gui;
    /**
     * Reference to client
     */
    private Client client;
    /**
     * This attribute is the grid of the board
     */
    GridPane boardGrid;
    /**
     * This attribute is to control if the weapon is optional or not
     */
    boolean optionalShoot = false;
    /**
     * This attribute is to control if it's the end of turn
     */
    boolean endTurn = false;
    /**
     * This attribute is the position in hand
     */
    String handPosition;
    /**
     * This attribute is the type of fire of the weapon
     */
    String typeOfFire;
    /**
     * This attribute is the name of the weapon
     */
    String nameWeapon;

    /**
     * This constant is for the purple color
     */
    static final String PURPLE = "purple";
    /**
     * This constant is for the blue color
     */
    static final String BLUE = "blue";
    /**
     * This constant is for the green color
     */
    static final String GREEN = "green";
    /**
     * This constant is for the yellow color
     */
    static final String YELLOW = "yellow";
    /**
     * This constant is for the grey color
     */
    static final String GREY = "grey";
    /**
     * This constant is for control if the damage counter of the player are at least 6
     */
    static final int SHOOT_ADRENALINE = 6;
    /**
     * This constant is the number of inner columns inside the cell
     */
    static final int N_INNER_COLUMN = 3;
    /**
     * This constant is the number of columns
     */
    static final int N_COLUMN = 7;
    /**
     * This constant is the number of inner rows inside the cell
     */
    static final int N_INNER_ROW = 3;
    /**
     * This constant is the number of rows
     */
    static final int N_ROW = 5;
    /**
     * This constant is the position in the board representation of x cell
     */
    static final int CELL_X = 0;
    /**
     * This constant is the position in the board representation of y cell
     */
    static final int CELL_Y = 1;
    /**
     * This constant is the position in the board representation of the cell's color
     */
    static final int CELL_COLOR = 2;
    /**
     * This constant is the position in the board representation of the cell's type
     */
    static final int CELL_TYPE = 3;
    /**
     * This constant is the position in the board representation of what is inside the cell
     */
    static final int CELL_INSIDE = 4;
    /**
     * This constant is the position in the board representation of which players are on the cell
     */
    static final int CELL_PLAYER_ON_ME = 6;
    /**
     * This constant is the position in the board representation of the cell's doors
     */
    static final int CELL_DOORS = 8;
    /**
     * This constant is the position in the board representation of the cell's walls
     */
    static final int CELL_WALLS = 10;
    /**
     * This constant is the position in the player representation of the player's skulls
     */
    static final int PLAYER_SKULL = 0;
    /**
     * This constant is the number of the player's plance row span
     */
    static final int PLAYER_ROW_SPAN = 1;
    /**
     * This constant is the position in the player representation of the player's ammo
     */
    static final int PLAYER_AMMO = 1;
    /**
     * This constant is the number of the player's plance column span
     */
    static final int PLAYER_COL_SPAN = 3;
    /**
     * This constant is the position in the player representation of the player's damage
     */
    static final int PLAYER_DAMAGE = 3;
    /**
     * This constant is number of the player's plance x cell
     */
    static final int PLAYER_XPOS = 4;
    /**
     * This constant is the position in the player representation of the player's mark
     */
    static final int PLAYER_MARK = 5;
    /**
     * This constant is the position in the player representation of the player's power ups
     */
    static final int PLAYER_POWER_UP = 6;
    /**
     * This constant is the position in the player representation of the player's weapons
     */
    static final int PLAYER_WEAPON = 8;
    /**
     * This constant is the position in the player representation of the player's color
     */
    static final int PLAYER_COLOR = 9;
    /**
     * This constant is the position in the player representation of the player's turned plance
     */
    static final int PLAYER_TURNED = 10;
    /**
     * This constant is the position in the player representation of the frenzy turn
     */
    static final int PLAYER_FRENZY = 11;
    /**
     * This constant is the position in the you representation of your plance x cell
     */
    static final int YOU_XPOS = 0;
    /**
     * This constant is the number of your plance column span
     */
    static final int YOU_COL_SPAN = 4;
    /**
     * This constant is the position in the you representation of your plance y cell
     */
    static final int YOU_YPOS = 4;
    /**
     * This constant is the position in the you representation of your points
     */
    static final int YOU_POINT = 12;
    /**
     * This constant is the position in the you representation of your weapons
     */
    static final int YOU_WEAPON = 14;
    /**
     * This constant is the position in the you representation of your power ups
     */
    static final int YOU_POWERUP = 16;
    /**
     * This constant is the position in the you representation of your action counter number of a frenzy turn
     */
    static final int YOU_FRENZY_ACTION = 17;
    /**
     * This constant is number of row where start the killshot track
     */
    static final int KILL_ROW = 3;
    /**
     * This constant is number of column where start the killshot track
     */
    static final int KILL_COL = 0;
    /**
     * This constant is the position in the killshot track representation of the starter number of skulls
     */
    static final int KILL_TOT_SKULL = 0;
    /**
     * This constant is number of rows span of the killshot track
     */
    static final int KILL_ROW_SPAN = 1;
    /**
     * This constant is number of columns span of the killshot track
     */
    static final int KILL_COL_SPAN = 3;
    /**
     * This constant is the limit of the weapon
     */
    static final int NUMBER_OF_WEAPON = 3;

    /**
     * This is the constructor of the GameGUI class
     * @param gui Reference to GUI
     * @param client Reference to client
     */
    GameGUI(GUI gui, Client client) {
        this.gui = gui;
        this.client = client;
    }

    /**
     * This method draw any time a information message is sent from the server
     * @param stage This parameter is the stage where we used to show
     * @param msg This parameter is the message that is going to be displayed
     */
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

        grid.add(text,0,0);
        grid.add(ok,0,1);

        grid.setVgap(50);
        grid.setAlignment(Pos.CENTER);

        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid);
        pane.getChildren().add(pane2);
    }

    /**
     * This method notify that the player is suspended when the turn timer is expired
     * @param stage This parameter is the stage where we used to show
     */
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

    /**
     * This method draw the ended graphics of the players who win the game
     * @param stage This parameter is the stage where we used to show
     * @param msg This parameter is the message of the player and their points that is going to be displayed
     */
    void buildWinner(Stage stage, String msg){
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
        for(String winners : msg.split(";")) {

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

    /**
     * This method add the column constraints to a grid
     * @param grid This parameter is the grid where add the column constraint
     * @param nColumn This parameter is the number of columns
     */
    void columnConstraint(GridPane grid, double nColumn){
        for (int j = 0 ; j < nColumn; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            double dimension = 100/nColumn;
            col.setPercentWidth(dimension);
            grid.getColumnConstraints().add(col);
        }
    }

    /**
     * This method add the row constraints to a grid
     * @param grid This parameter is the grid where add the column constraint
     * @param nRow This parameter is the number of rows
     */
    void rowConstraint(GridPane grid, double nRow){
        for (int i = 0 ; i < nRow; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            double dimension = 100/nRow;
            row.setPercentHeight(dimension);
            grid.getRowConstraints().add(row);
        }
    }

    /**
     * This method add the rectangle to a pane
     * @param rectangle This parameter is the rectangle
     * @param pane This parameter is pane where we add the rectangle
     */
    void rectangleStandard(Rectangle rectangle, Pane pane){
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());
    }

    /**
     * This method set a label
     * @param label This parameter is the label
     * @param color This parameter is the label's color
     * @param opacity This parameter is the label's opacity
     * @param font This parameter is the label's font
     */
    void labelSetting(Label label, String color, double opacity , String font){
        label.setTextFill(Color.web(color, opacity));
        label.setStyle(font);
        label.setEffect(new DropShadow());
    }

    /**
     * This method return the color and the name of a power up
     * @param powerups This parameter is the string of power ups
     * @return The power up's color and the power up's name
     */
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