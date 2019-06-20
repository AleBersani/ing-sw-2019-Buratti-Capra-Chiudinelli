package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import it.polimi.ingsw.communication.client.MessageHandler;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class GameGUI {
    private Stage stage;
    private GUI gui;
    private MessageHandler messageHandler;
    private Client client;
    private GridPane boardGrid;
    protected boolean endTurn=false;

    private static final String PURPLE="purple";
    private static final String BLUE="blue";
    private static final String GREEN="green";
    private static final String YELLOW="yellow";
    private static final String GREY="grey";
    private static final int N_INNER_COLUMN= 3;
    private static final int N_COLUMN= 7;
    private static final int N_INNER_ROW= 3;
    private static final int N_ROW= 5;
    private static final int CELL_X= 0;
    private static final int CELL_Y= 1;
    private static final int CELL_COLOR= 2;
    private static final int CELL_TYPE= 3;
    private static final int CELL_INSIDE= 4;
    private static final int CELL_PLAYER_ON_ME= 6;
    private static final int CELL_DOORS= 8;
    private static final int CELL_WALLS= 10;
    private static final int PLAYER_ROW_SPAN=1;
    private static final int PLAYER_AMMO=1;
    private static final int PLAYER_COL_SPAN=3;
    private static final int PLAYER_XPOS=4;
    private static final int PLAYER_POWER_UP= 6;
    private static final int PLAYER_WEAPON= 8;
    private static final int PLAYER_COLOR= 9;
    private static final int YOU_XPOS= 0;
    private static final int YOU_COL_SPAN= 4;
    private static final int YOU_YPOS= 4;
    private static final int YOU_POINT= 12;
    private static final int YOU_WEAPON= 14;
    private static final int YOU_POWERUP= 16;
    private static final int KILL_ROW= 3;
    private static final int KILL_COL= 0;
    private static final int KILL_TOT_SKULL= 0;
    private static final int KILL_ROW_SPAN= 1;
    private static final int KILL_COL_SPAN= 3;
    private static final int NUMBER_OF_WEAPON= 3;




    public GameGUI(GUI gui, MessageHandler messageHandler, Client client) {
        this.gui = gui;
        this.messageHandler = messageHandler;
        this.client = client;
    }

    public void backGround(Stage stage){
        //backGround image
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        ImageView screen = new ImageView(new Image("/images/game/metallicScreen.png"));
        screen.fitWidthProperty().bind(pane.widthProperty());
        screen.fitHeightProperty().bind(pane.heightProperty());
        pane.getChildren().add(screen);
    }

    public void buildBoard(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        stage.setResizable(false);
        GridPane grid = new GridPane();

        //grid column constraint
        columnConstraint(grid, N_COLUMN);

        //grid row constraint
        rowConstraint(grid, N_ROW);

        //cells
        for (ArrayList<ArrayList<String>> room: gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if(!cell.isEmpty()) {
                    int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                    int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                    String color = cell.get(CELL_COLOR);
                    if((color.equals(BLUE))||(color.equals(GREEN))||(color.equals(PURPLE))||(color.equals("red"))||(color.equals("white"))||(color.equals(YELLOW))) {
                        grid.add(new ImageView(new Image("/images/game/cell/".concat(color).concat("Cell.png"), pane.getWidth() / N_COLUMN, pane.getHeight() / N_ROW, false, false)), xPos, yPos);
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
                    String wall = "wall".concat(s).concat(".png");
                    if((wall.equals("wallN.png"))||(wall.equals("wallS.png"))||(wall.equals("wallW.png"))||(wall.equals("wallE.png"))){
                        grid.add(new ImageView(new Image("/images/game/cell/wall/".concat(wall),pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false)), xPos, yPos);
                    }
                }
            }
        }

        //doors
        for (ArrayList<ArrayList<String>> room: gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if(!cell.get(CELL_DOORS).isEmpty()){
                    for(String singleDoor: cell.get(CELL_DOORS).split(":")){
                        int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                        int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                        String[] door = singleDoor.split("'");
                        int xDoor = Integer.parseInt(door[CELL_X]);
                        int yDoor = Integer.parseInt(door[CELL_Y]);
                        String doorDirection = "";
                        if(xPos+1 == xDoor){
                            if(yPos+1 > yDoor){
                                doorDirection = "doorN.png";
                            }
                            if(yPos+1 < yDoor){
                                doorDirection = "doorS.png";
                            }
                        }
                        else {
                            if(xPos+1 > xDoor){
                                doorDirection = "doorW.png";
                            }
                            if(xPos+1 < xDoor){
                                doorDirection = "doorE.png";
                            }
                        }
                        grid.add(new ImageView(new Image("/images/game/cell/door/".concat(doorDirection),pane.getWidth()/N_COLUMN,pane.getHeight()/N_ROW,false,false)),xPos,yPos);
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
                    if(!cell.get(CELL_INSIDE).equals("")) {
                        for (String s : cell.get(CELL_INSIDE).split("'")) {
                            String[] difi = s.split(":");
                            if (s.startsWith("Y:")) {
                                if (!difi[1].equals("0")) {
                                    ammoName = ammoName.concat("y").concat(difi[1]);
                                }
                            } else {
                                if (s.startsWith("R:")) {
                                    if (!difi[1].equals("0")) {
                                        ammoName = ammoName.concat("r").concat(difi[1]);
                                    }
                                } else {
                                    if (s.startsWith("B:")) {
                                        if (!difi[1].equals("0")) {
                                            ammoName = ammoName.concat("b").concat(difi[1]);
                                        }
                                    } else {
                                        if (s.startsWith("PU:")) {
                                            if (!difi[1].equals("0")) {
                                                ammoName = ammoName.concat("pu").concat(difi[1]);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        ImageView ammoTile = new ImageView(new Image("/images/game/ammo/tiles/".concat(ammoName).concat(".png"), pane.getWidth() / N_COLUMN / N_INNER_COLUMN, pane.getHeight() / N_ROW / N_INNER_ROW, false, false));
                        grid.add(ammoTile, xPos, yPos);
                        GridPane.setHalignment(ammoTile, HPos.CENTER);
                        GridPane.setValignment(ammoTile, VPos.CENTER);
                    }
                }
            }
        }

        //plyersToken
        for (ArrayList<ArrayList<String>> room: gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                for(String player: cell.get(CELL_PLAYER_ON_ME).split("'")){
                    switch (player){
                        case PURPLE:{
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/purpleToken.png",pane.getWidth()/N_COLUMN/N_INNER_COLUMN,pane.getHeight()/N_ROW/N_INNER_ROW,false,false));
                            grid.add(playerToken,xPos,yPos);
                            GridPane.setHalignment(playerToken,HPos.RIGHT);
                            GridPane.setValignment(playerToken,VPos.CENTER);
                            break;
                        }
                        case BLUE:{
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/blueToken.png",pane.getWidth()/N_COLUMN/N_INNER_COLUMN,pane.getHeight()/N_ROW/N_INNER_ROW,false,false));
                            grid.add(playerToken,xPos,yPos);
                            GridPane.setHalignment(playerToken,HPos.LEFT);
                            GridPane.setValignment(playerToken,VPos.CENTER);
                            break;
                        }
                        case GREEN:{
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/greenToken.png",pane.getWidth()/N_COLUMN/N_INNER_COLUMN,pane.getHeight()/N_ROW/N_INNER_ROW,false,false));
                            grid.add(playerToken,xPos,yPos);
                            GridPane.setHalignment(playerToken,HPos.CENTER);
                            GridPane.setValignment(playerToken,VPos.BOTTOM);
                            break;
                        }
                        case YELLOW:{
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/yellowToken.png",pane.getWidth()/N_COLUMN/N_INNER_COLUMN,pane.getHeight()/N_ROW/N_INNER_ROW,false,false));
                            grid.add(playerToken,xPos,yPos);
                            GridPane.setHalignment(playerToken,HPos.LEFT);
                            GridPane.setValignment(playerToken,VPos.BOTTOM);
                            break;
                        }
                        case GREY:{
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/greyToken.png",pane.getWidth()/N_COLUMN/N_INNER_COLUMN,pane.getHeight()/N_ROW/N_INNER_ROW,false,false));
                            grid.add(playerToken,xPos,yPos);
                            GridPane.setHalignment(playerToken,HPos.RIGHT);
                            GridPane.setValignment(playerToken,VPos.BOTTOM);
                            break;
                        }
                        default:
                    }
                }
            }
        }

        //pane.add
        pane.getChildren().add(grid);
        boardGrid = grid;
    }

    public void buildPlayers(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        GridPane grid = new GridPane();

        //grid column constraint
        columnConstraint(grid, N_COLUMN);

        //grid row constraint
        rowConstraint(grid, N_ROW);

        int i=0;
        for(ArrayList<String> playerColor: gui.getPlayersRepresentation()){
            String[] color = playerColor.get(PLAYER_COLOR).split(":");
            String playerPlance = color[1];
            if((playerPlance.equals(PURPLE))||(playerPlance.equals(BLUE))||(playerPlance.equals(GREEN))||(playerPlance.equals(YELLOW))||(playerPlance.equals(GREY))) {
                grid.add(new ImageView(new Image("/images/game/plance/".concat(playerPlance).concat("Player.png"), pane.getWidth() / N_COLUMN * PLAYER_COL_SPAN, pane.getHeight() / N_ROW, false, false)), PLAYER_XPOS, i, PLAYER_COL_SPAN, PLAYER_ROW_SPAN);
            }
            i++;
        }


        pane.getChildren().add(grid);
    }

    public void buildYou(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        stage.setResizable(false);
        GridPane grid = new GridPane();

        //grid column constraint
        columnConstraint(grid, N_COLUMN);

        //grid row constraint
        rowConstraint(grid, N_ROW);

        String[] color = gui.getYouRepresentation().get(PLAYER_COLOR).split(":");
        String playerPlance = color[1];
        if((playerPlance.equals(PURPLE))||(playerPlance.equals(BLUE))||(playerPlance.equals(GREEN))||(playerPlance.equals(YELLOW))||(playerPlance.equals(GREY))) {
            grid.add(new ImageView(new Image("/images/game/plance/".concat(playerPlance).concat("Player.png"),pane.getWidth()/N_COLUMN*YOU_COL_SPAN,pane.getHeight()/N_ROW,false,false)),YOU_XPOS,YOU_YPOS,YOU_COL_SPAN,PLAYER_ROW_SPAN);
        }

        Label points = new Label(gui.getYouRepresentation().get(YOU_POINT));
        grid.add(points,3,3);
        points.setTextFill(Color.web("#ffffff", 0.8));
        points.setStyle("-fx-font: 30 Helvetica;");
        points.setEffect(new DropShadow());
        GridPane.setHalignment(points, HPos.CENTER);
        //TODO altre plance da fare ancora come immagini



        pane.getChildren().add(grid);
    }

    public void buildKillShotTrack(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        stage.setResizable(false);
        GridPane grid = new GridPane();

        columnConstraint(grid,N_COLUMN);

        rowConstraint(grid,N_ROW);

        grid.add(new ImageView(new Image("/images/game/killshotTrack.png",pane.getWidth()/N_COLUMN*KILL_COL_SPAN,pane.getHeight()/N_COLUMN,false,false)),KILL_COL,KILL_ROW,KILL_COL_SPAN,KILL_ROW_SPAN);
        pane.getChildren().add(grid);

        Pane pane2 = new Pane();
        String[] damage = gui.getKillShotRepresentation().get(1).split("'");
        String[] doubleDamage = gui.getKillShotRepresentation().get(2).split(",");
        int totalSkull = Integer.valueOf(gui.getKillShotRepresentation().get(KILL_TOT_SKULL));
        for(int i = 0;i<totalSkull;i++) {
            String bloodString = "";
            if((i < damage.length)&&(!damage[i].equals(""))) {
                bloodString = damage[i].concat("Blood");
                if (doubleDamage[i].equals("true")) {
                    bloodString = bloodString.concat("X2");
                }
            }
            else {
                bloodString = "redSkull";
            }
            ImageView blood = new ImageView(new Image("/images/game/blood/".concat(bloodString).concat(".png"),pane.getWidth()/30,pane.getHeight()/15,false,false));
            blood.setX(pane.getWidth()/3.02 - ((totalSkull-1-i) * pane.getWidth()/21.5));
            blood.setY(pane.getHeight()/1.5);
            pane2.getChildren().add(blood);
        }


        pane.getChildren().add(pane2);
    }

    public void buildButtons(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        GridPane gridButtons = new GridPane();
        stage.getScene().setRoot(pane);
        columnConstraint(gridButtons, N_COLUMN);
        rowConstraint(gridButtons, N_ROW);

        //store button
        storeButtons(gridButtons, pane);

        //info button enemy
        int i = 0;
        for (ArrayList<String> enemyPlayer : gui.getPlayersRepresentation()) {
            Button infoButton = new Button("INFO");
            gridButtons.add(infoButton, 6, i);
            GridPane.setHalignment(infoButton, HPos.CENTER);
            GridPane.setValignment(infoButton, VPos.BOTTOM);
            infoButton.setOnAction(e -> {
                GridPane gridInfo = new GridPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
                rectangle.setEffect(new BoxBlur());
                rectangle.widthProperty().bind(pane.widthProperty());
                rectangle.heightProperty().bind(pane.heightProperty());

                RowConstraints row = new RowConstraints();
                row.setVgrow(Priority.ALWAYS);
                double dimension = 100 / 3;
                row.setPercentHeight(dimension);
                gridInfo.getRowConstraints().add(row);

                //weapons
                int j = 0;

                for (String weapon : enemyPlayer.get(PLAYER_WEAPON).split("'")) {
                    if (!weapon.equals("")) {
                        if (weapon.equals("notVisible")) {
                            gridInfo.add(new ImageView(new Image("/images/game/weapons/weaponBack.png", pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), j, 0);
                        } else {
                            String weaponName = weapon.toLowerCase().replace(" ", "").concat(".png");
                            gridInfo.add(new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), j, 0);

                        }
                    }
                    j++;
                }

                //power ups
                String[] powerUp = enemyPlayer.get(PLAYER_POWER_UP).split(":");
                Label numberPowerUp = new Label("x".concat(powerUp[1]));
                numberPowerUp.setTextFill(Color.web("#ffffff", 0.8));
                numberPowerUp.setStyle("-fx-font: 40 Helvetica;");
                numberPowerUp.setEffect(new DropShadow());
                gridInfo.add(new ImageView(new Image("/images/game/powerUps/powerUpBack.png", pane.getWidth() / 10, pane.getHeight() / 5, false, false)), j, 0);
                gridInfo.add(numberPowerUp, j, 0);
                GridPane.setHalignment(numberPowerUp, HPos.CENTER);
                GridPane.setValignment(numberPowerUp, VPos.BOTTOM);

                //ammos
                j++;
                for (String ammo : enemyPlayer.get(PLAYER_AMMO).split("'")) {
                    String[] ammoQuantity = ammo.split(":");
                    switch (ammoQuantity[0]) {
                        case "R": {
                            ImageView redAmmoIV = new ImageView(new Image("/images/game/ammo/redAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                            gridInfo.add(redAmmoIV, j, 0);
                            GridPane.setHalignment(redAmmoIV, HPos.LEFT);
                            GridPane.setValignment(redAmmoIV, VPos.TOP);
                            Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                            label40Helvetica(numberAmmo, "#ffffff", 0.8);
                            gridInfo.add(numberAmmo, j + 1, 0);
                            GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                            GridPane.setValignment(numberAmmo, VPos.TOP);
                            break;
                        }
                        case "Y": {
                            ImageView yellowAmmoIV = new ImageView(new Image("/images/game/ammo/yellowAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                            gridInfo.add(yellowAmmoIV, j, 0);
                            GridPane.setHalignment(yellowAmmoIV, HPos.LEFT);
                            GridPane.setValignment(yellowAmmoIV, VPos.CENTER);
                            Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                            label40Helvetica(numberAmmo, "#ffffff", 0.8);
                            gridInfo.add(numberAmmo, j + 1, 0);
                            GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                            GridPane.setValignment(numberAmmo, VPos.CENTER);
                            break;
                        }
                        case "B": {
                            ImageView blueAmmoIV = new ImageView(new Image("/images/game/ammo/blueAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                            gridInfo.add(blueAmmoIV, j, 0);
                            GridPane.setHalignment(blueAmmoIV, HPos.LEFT);
                            GridPane.setValignment(blueAmmoIV, VPos.BOTTOM);
                            Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                            label40Helvetica(numberAmmo, "#ffffff", 0.8);
                            gridInfo.add(numberAmmo, j + 1, 0);
                            GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                            GridPane.setValignment(numberAmmo, VPos.BOTTOM);
                            break;
                        }
                        default:
                    }
                }

                //back button
                Button backButton = new Button("BACK");
                gridInfo.add(backButton, 0, 1,j+2,1);
                GridPane.setHalignment(backButton, HPos.CENTER);
                GridPane.setValignment(backButton, VPos.CENTER);
                backButton.setOnAction(ev -> {
                    pane.getChildren().remove(gridInfo);
                    pane.getChildren().remove(rectangle);
                });

                //grid
                gridInfo.setHgap(40);
                gridInfo.setVgap(50);
                gridInfo.setAlignment(Pos.CENTER);

                pane.getChildren().add(rectangle);
                pane.getChildren().add(gridInfo);
            });
            i++;
        }

        //info button myself
        Button infoButton = new Button("INFO");
        gridButtons.add(infoButton, 3, 4);
        GridPane.setHalignment(infoButton, HPos.CENTER);
        GridPane.setValignment(infoButton, VPos.BOTTOM);
        infoButton.setOnAction(e -> {
            GridPane gridInfo = new GridPane();
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
            rectangle.setEffect(new BoxBlur());
            rectangle.widthProperty().bind(pane.widthProperty());
            rectangle.heightProperty().bind(pane.heightProperty());

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            double dimension = 100 / 3;
            row.setPercentHeight(dimension);
            gridInfo.getRowConstraints().add(row);

            //weapons
            int j = 0;

            for (String weapon : gui.getYouRepresentation().get(YOU_WEAPON).split("'")) {
                if (!weapon.equals("")) {
                    String[] playerWeapon = weapon.split(":");

                    String weaponName = playerWeapon[0].toLowerCase().replace(" ", "").concat(".png");
                    gridInfo.add(new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), j, 0);
                    //TODO carica o scarica
                }
                j++;
            }

            if(!gui.getYouRepresentation().get(YOU_POWERUP).equals("")) {
                for (String powerups : gui.getYouRepresentation().get(YOU_POWERUP).split("'")) {
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
                    ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                    gridInfo.add(powerUp, j, 0);
                    j++;
                }
            }
            //ammos
            j++;
            for (String ammo : gui.getYouRepresentation().get(PLAYER_AMMO).split("'")) {
                String[] ammoQuantity = ammo.split(":");
                switch (ammoQuantity[0]) {
                    case "R": {
                        ImageView redAmmoIV = new ImageView(new Image("/images/game/ammo/redAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                        gridInfo.add(redAmmoIV, j, 0);
                        GridPane.setHalignment(redAmmoIV, HPos.LEFT);
                        GridPane.setValignment(redAmmoIV, VPos.TOP);
                        Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                        label40Helvetica(numberAmmo, "#ffffff", 0.8);
                        gridInfo.add(numberAmmo, j + 1, 0);
                        GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                        GridPane.setValignment(numberAmmo, VPos.TOP);
                        break;
                    }
                    case "Y": {
                        ImageView yellowAmmoIV = new ImageView(new Image("/images/game/ammo/yellowAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                        gridInfo.add(yellowAmmoIV, j, 0);
                        GridPane.setHalignment(yellowAmmoIV, HPos.LEFT);
                        GridPane.setValignment(yellowAmmoIV, VPos.CENTER);
                        Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                        label40Helvetica(numberAmmo, "#ffffff", 0.8);
                        gridInfo.add(numberAmmo, j + 1, 0);
                        GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                        GridPane.setValignment(numberAmmo, VPos.CENTER);
                        break;
                    }
                    case "B": {
                        ImageView blueAmmoIV = new ImageView(new Image("/images/game/ammo/blueAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                        gridInfo.add(blueAmmoIV, j, 0);
                        GridPane.setHalignment(blueAmmoIV, HPos.LEFT);
                        GridPane.setValignment(blueAmmoIV, VPos.BOTTOM);
                        Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                        label40Helvetica(numberAmmo, "#ffffff", 0.8);
                        gridInfo.add(numberAmmo, j + 1, 0);
                        GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                        GridPane.setValignment(numberAmmo, VPos.BOTTOM);
                        break;
                    }
                    default:
                }
            }

            //back button
            Button backButton = new Button("BACK");
            gridInfo.add(backButton, 0, 1,j+2,1);
            GridPane.setHalignment(backButton, HPos.CENTER);
            GridPane.setValignment(backButton, VPos.CENTER);
            backButton.setOnAction(ev -> {
                pane.getChildren().remove(gridInfo);
                pane.getChildren().remove(rectangle);
            });

            //grid
            gridInfo.setHgap(40);
            gridInfo.setVgap(50);
            gridInfo.setAlignment(Pos.CENTER);

            pane.getChildren().add(rectangle);
            pane.getChildren().add(gridInfo);
        });


        //quit button
        Button quit = new Button("Quit");
        gridButtons.add(quit, 6, 4);
        GridPane.setHalignment(quit, HPos.CENTER);
        GridPane.setValignment(quit, VPos.CENTER);
        quit.setOnAction(e -> {
            GridPane quitGrid = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(20);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(20);
            quitGrid.getColumnConstraints().addAll(col1, col2);
            Rectangle rectangle = new Rectangle();
            Label text = new Label("Are you sure to quit?");
            text.setTextFill(Color.web("#ffffff", 0.8));
            text.setStyle("-fx-font: 60 Helvetica;");
            text.setEffect(new DropShadow());
            Button quit1 = new Button("QUIT");
            Button back = new Button("BACK");
            rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
            rectangle.setEffect(new BoxBlur());
            rectangle.widthProperty().bind(pane.widthProperty());
            rectangle.heightProperty().bind(pane.heightProperty());
            quitGrid.add(text, 0, 0, 2, 1);
            quitGrid.add(back, 1, 1);
            quitGrid.add(quit1, 0, 1);
            pane.getChildren().add(rectangle);
            pane.getChildren().add(quitGrid);
            quitGrid.setHgap(70);
            quitGrid.setVgap(50);
            quitGrid.setAlignment(Pos.CENTER);
            GridPane.setHalignment(text, HPos.CENTER);
            GridPane.setValignment(text, VPos.CENTER);
            GridPane.setHalignment(back, HPos.CENTER);
            GridPane.setValignment(back, VPos.CENTER);
            GridPane.setHalignment(quit1, HPos.CENTER);
            GridPane.setValignment(quit1, VPos.CENTER);
            back.setOnAction(ev -> {
                pane.getChildren().remove(quitGrid);
                pane.getChildren().remove(rectangle);
            });
            quit1.setOnAction(ev -> {
                client.send("quit");
            });
        });

        if(!endTurn){
            //Actions
            Button actions = new Button("Actions");
            gridButtons.add(actions, 4, 4);
            GridPane.setHalignment(actions, HPos.CENTER);
            GridPane.setValignment(actions, VPos.CENTER);
            actions.setOnAction(event -> {
                GridPane grid4 = new GridPane();
                Rectangle rectangle = new Rectangle();
                Button shoot = new Button("SHOOT");
                Button run = new Button("RUN");
                Button grab = new Button("GRAB");
                Button back = new Button("BACK");
                rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
                rectangle.setEffect(new BoxBlur());
                rectangle.widthProperty().bind(pane.widthProperty());
                rectangle.heightProperty().bind(pane.heightProperty());
                grid4.add(shoot, 0, 0);
                grid4.add(run, 1, 0);
                grid4.add(grab, 2, 0);
                grid4.add(back, 1, 1);
                pane.getChildren().add(rectangle);
                pane.getChildren().add(grid4);
                grid4.setHgap(70);
                grid4.setVgap(50);
                GridPane.setHalignment(back, HPos.CENTER);
                GridPane.setValignment(back, VPos.CENTER);
                grid4.setAlignment(Pos.CENTER);

                //back
                back.setOnAction(e -> {
                    pane.getChildren().remove(grid4);
                    pane.getChildren().remove(rectangle);
                });

                //run
                run.setOnAction(e -> {
                    pane.getChildren().remove(grid4);
                    pane.getChildren().remove(boardGrid);
                    pane.getChildren().add(boardGrid);

                    GridPane grid5 = new GridPane();
                    columnConstraint(grid5, N_COLUMN);
                    rowConstraint(grid5, N_ROW);
                    storeButtons(grid5, pane);

                    Button backRun = new Button("BACK");
                    grid5.add(backRun, 4, 3);
                    GridPane.setHalignment(backRun, HPos.CENTER);
                    GridPane.setValignment(backRun, VPos.CENTER);

                    EventHandler clickEvent = (EventHandler<MouseEvent>) event1 -> {
                        int cellX = 1 + (int) (event1.getScreenX() / (pane.getWidth() / N_COLUMN));
                        int cellY = 1 + (int) (event1.getScreenY() / (pane.getHeight() / N_ROW));

                        System.out.println(cellX);
                        System.out.println(cellY);

                        if ((cellX < 5) && (cellY < 4)) {
                            client.send("GMC-RUN-".concat(Integer.toString(cellX)).concat(",").concat(Integer.toString(cellY)));
                        }
                    };

                    //Click event
                    pane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);

                    //Back
                    backRun.setOnAction(ev -> {
                        pane.getChildren().remove(grid5);
                        pane.getChildren().remove(rectangle);
                        buildButtons(stage);
                        actions.fire();
                        pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
                    });

                    pane.getChildren().add(grid5);
                });

                //Grab
                grab.setOnAction(e -> {
                    pane.getChildren().remove(grid4);
                    pane.getChildren().remove(boardGrid);
                    pane.getChildren().add(boardGrid);

                    GridPane grid5 = new GridPane();
                    columnConstraint(grid5, N_COLUMN);
                    rowConstraint(grid5, N_ROW);
                    storeButtons(grid5, pane);

                    Button backRun = new Button("BACK");
                    grid5.add(backRun, 4, 3);
                    GridPane.setHalignment(backRun, HPos.CENTER);
                    GridPane.setValignment(backRun, VPos.CENTER);

                    final boolean[] cancelClickEvent = new boolean[1];
                    cancelClickEvent[0] = true;

                    EventHandler clickEvent = (EventHandler<MouseEvent>) event1 -> {
                        int cellX = 1 + (int) (event1.getScreenX() / (pane.getWidth() / N_COLUMN));
                        int cellY = 1 + (int) (event1.getScreenY() / (pane.getHeight() / N_ROW));

                        System.out.println(cellX);
                        System.out.println(cellY);
                        if ((cellX < 5) && (cellY < 4) && (cancelClickEvent[0])) {
                            for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
                                for (ArrayList<String> cell : room) {
                                    if ((cell.get(CELL_X).equals(Integer.toString(cellX))) && (cell.get(CELL_Y).equals(Integer.toString(cellY)))) {
                                        if (cell.get(CELL_TYPE).equals("AmmoPoint")) {
                                            client.send("GMC-GRB-".concat(Integer.toString(cellX)).concat(",").concat(Integer.toString(cellY)));
                                        } else {
                                            cancelClickEvent[0] = false;
                                            GridPane grid = new GridPane();
                                            String[] weapon = cell.get(CELL_INSIDE).split("'");
                                            for (int j = 0; j < NUMBER_OF_WEAPON; j++) {
                                                if (j < weapon.length) {
                                                    String weaponName = weapon[j].toLowerCase();
                                                    weaponName = weaponName.replace(" ", "").concat(".png");
                                                    ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false));
                                                    grid.add(weaponIV, j, 0);
                                                    int w = j;
                                                    weaponIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, ev -> {
                                                        client.send("GMC-GRB-".concat(Integer.toString(cellX)).concat(",").concat(Integer.toString(cellY)
                                                                .concat(",").concat(Integer.toString(w))));
                                                        pane.getChildren().remove(grid);
                                                        pane.getChildren().remove(rectangle);
                                                    });
                                                } else {
                                                    grid.add(new ImageView(new Image("/images/game/weapons/weaponBack.png", pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), j, 0);
                                                }
                                            }

                                            Button backButton = new Button("BACK");
                                            grid.add(backButton, 1, 1);
                                            pane.getChildren().remove(rectangle);
                                            pane.getChildren().add(rectangle);
                                            pane.getChildren().add(grid);
                                            grid.setHgap(40);
                                            grid.setVgap(50);
                                            GridPane.setHalignment(backButton, HPos.CENTER);
                                            GridPane.setValignment(backButton, VPos.CENTER);
                                            grid.setAlignment(Pos.CENTER);
                                            backButton.setOnAction(ev -> {
                                                pane.getChildren().remove(grid);
                                                pane.getChildren().remove(rectangle);
                                                pane.getChildren().remove(grid5);
                                                pane.getChildren().remove(boardGrid);
                                                pane.getChildren().add(boardGrid);
                                                pane.getChildren().remove(gridButtons);
                                                pane.getChildren().add(gridButtons);
                                                actions.fire();
                                            });
                                        }
                                    }
                                }
                            }
                        }

                    };

                    //Click event
                    pane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);

                    //Back
                    backRun.setOnAction(ev -> {
                        pane.getChildren().remove(grid5);
                        pane.getChildren().remove(rectangle);
                        buildButtons(stage);
                        actions.fire();
                        pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
                    });

                    pane.getChildren().add(grid5);
                });
            });
        }
        else{
            //end turn
            Button endTurnButton = new Button("END TURN");
            gridButtons.add(endTurnButton, 4, 4);
            GridPane.setHalignment(endTurnButton, HPos.CENTER);
            GridPane.setValignment(endTurnButton, VPos.CENTER);
            endTurnButton.setOnAction(event -> {
                this.endTurn=false;
                client.send("END-");
            });
        }

        //Use power Up
        Button powerUps = new Button("Use PowerUps");
        gridButtons.add(powerUps,5,4);
        GridPane.setHalignment(powerUps,HPos.CENTER);
        GridPane.setValignment(powerUps,VPos.CENTER);
        powerUps.setOnAction(e->{
            GridPane powerUpGrid = new GridPane();
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
            rectangle.setEffect(new BoxBlur());
            rectangle.widthProperty().bind(pane.widthProperty());
            rectangle.heightProperty().bind(pane.heightProperty());

            int j=0;
            if(!gui.getYouRepresentation().get(YOU_POWERUP).equals("")) {
                for (String powerups : gui.getYouRepresentation().get(YOU_POWERUP).split("'")) {
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
                    ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                    powerUpGrid.add(powerUp, j, 0);
                    powerUp.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, ev -> {
                        GridPane targetGrid = new GridPane();
                        columnConstraint(targetGrid, N_COLUMN);
                        rowConstraint(targetGrid, N_ROW);

                        Button back = new Button("BACK");
                        targetGrid.add(back, 4, 3);
                        GridPane.setHalignment(back, HPos.CENTER);
                        GridPane.setValignment(back, VPos.CENTER);
                        back.setOnAction(eve -> {
                            pane.getChildren().remove(rectangle);
                            pane.getChildren().remove(targetGrid);
                            pane.getChildren().remove(gridButtons);
                            pane.getChildren().add(gridButtons);
                            powerUps.fire();
                        });


                        pane.getChildren().remove(powerUpGrid);
                        pane.getChildren().remove(boardGrid);
                        pane.getChildren().add(boardGrid);
                        storeButtons(targetGrid, pane);
                        pane.getChildren().add(targetGrid);
                    });
                    j++;
                }
            }
            Button backButton = new Button("BACK");
            if(j==0) {
                j = 1;
            }
            powerUpGrid.add(backButton,0,1,j,1);
            pane.getChildren().add(rectangle);
            pane.getChildren().add(powerUpGrid);
            powerUpGrid.setHgap(40);
            powerUpGrid.setVgap(50);
            GridPane.setHalignment(backButton,HPos.CENTER);
            GridPane.setValignment(backButton,VPos.CENTER);
            powerUpGrid.setAlignment(Pos.CENTER);
            backButton.setOnAction(ev -> {
                pane.getChildren().remove(powerUpGrid);
                pane.getChildren().remove(rectangle);
            });
        });

        pane.getChildren().add(gridButtons);
    }

    public void spawn(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();

        StackPane pane2 = new StackPane();

        String[] powerupNumber = gui.getYouRepresentation().get(PLAYER_POWER_UP).split(":");
        int numberPowerup = Integer.parseInt(powerupNumber[1]);
        Rectangle rectangle = new Rectangle();
        String[] toShow = gui.getInfoString().split(":");
        Label text = new Label(toShow[0]);
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 35 Helvetica;");
        text.setEffect(new DropShadow());
        text.setAlignment(Pos.CENTER);
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());

        GridPane grid2 = new GridPane();
        grid2.add(text,0,0, numberPowerup, 1);
        GridPane.setHalignment(text,HPos.CENTER);
        GridPane.setValignment(text,VPos.CENTER);

        int i=0;
        for(String powerups: gui.getYouRepresentation().get(YOU_POWERUP).split("'")) {
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
            ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
            grid2.add(powerUp, i, 1);
            int pU = i;
            powerUp.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                client.send("SPW-".concat(Integer.toString(pU)));
                pane.getChildren().remove(pane2);
            });
            i++;
        }
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(70);
        grid2.setVgap(50);
        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid2);
        pane.getChildren().add(pane2);

    }

    protected void reload(Stage stage,String msg){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        StackPane pane2 = new StackPane();

        GridPane reloadGrid = new GridPane();
        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        double dimension1 = 100 / 10;
        row1.setPercentHeight(dimension1);
        reloadGrid.getRowConstraints().add(row1);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        double dimension2 = 100 / NUMBER_OF_WEAPON;
        row2.setPercentHeight(dimension2);
        reloadGrid.getRowConstraints().add(row2);

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());
        Button ignore = new Button("IGNORE");
        Button done = new Button("DONE ");

        int i;
        String toSend = "RLD-";
        String[] weapons = msg.split("'");
        boolean[] consumed = new boolean[weapons.length];
        for(i=0; i< weapons.length; i++){
            consumed[i]=false;
            String weaponName = weapons[i].toLowerCase();
            weaponName = weaponName.replace(" ","").concat(".png");
            ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName),pane.getWidth()/N_COLUMN,pane.getHeight()/NUMBER_OF_WEAPON,false,false));
            reloadGrid.add(weaponIV,i,1);
            int w = i;
            weaponIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e->{
                if(!consumed[w]){
                    toSend.concat(Integer.toString(w)).concat(",");
                    consumed[w]=true;
                    weaponIV.setFitWidth(pane.getWidth()/(N_COLUMN*2));
                    weaponIV.setFitHeight(pane.getHeight()/(NUMBER_OF_WEAPON*2));
                }
            });
        }

        for (String ammo : gui.getYouRepresentation().get(PLAYER_AMMO).split("'")) {
            String[] ammoQuantity = ammo.split(":");
            switch (ammoQuantity[0]) {
                case "R": {
                    ImageView redAmmoIV = new ImageView(new Image("/images/game/ammo/redAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    reloadGrid.add(redAmmoIV, i, 1);
                    GridPane.setHalignment(redAmmoIV, HPos.LEFT);
                    GridPane.setValignment(redAmmoIV, VPos.TOP);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    label40Helvetica(numberAmmo, "#ffffff", 0.8);
                    reloadGrid.add(numberAmmo, i + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                    GridPane.setValignment(numberAmmo, VPos.TOP);
                    break;
                }
                case "Y": {
                    ImageView yellowAmmoIV = new ImageView(new Image("/images/game/ammo/yellowAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    reloadGrid.add(yellowAmmoIV, i, 1);
                    GridPane.setHalignment(yellowAmmoIV, HPos.LEFT);
                    GridPane.setValignment(yellowAmmoIV, VPos.CENTER);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    label40Helvetica(numberAmmo, "#ffffff", 0.8);
                    reloadGrid.add(numberAmmo, i + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                    GridPane.setValignment(numberAmmo, VPos.CENTER);
                    break;
                }
                case "B": {
                    ImageView blueAmmoIV = new ImageView(new Image("/images/game/ammo/blueAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    reloadGrid.add(blueAmmoIV, i, 1);
                    GridPane.setHalignment(blueAmmoIV, HPos.LEFT);
                    GridPane.setValignment(blueAmmoIV, VPos.BOTTOM);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    label40Helvetica(numberAmmo, "#ffffff", 0.8);
                    reloadGrid.add(numberAmmo, i + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                    GridPane.setValignment(numberAmmo, VPos.BOTTOM);
                    break;
                }
                default:
            }
        }

        reloadGrid.add(done,0,2);
        done.setOnAction(e->{
            client.send(toSend);
        });
        GridPane.setHalignment(done,HPos.CENTER);
        GridPane.setValignment(done,VPos.CENTER);

        reloadGrid.add(ignore,1,2);
        ignore.setOnAction(e->{
            client.send("RLD-ignore");
        });
        GridPane.setHalignment(ignore,HPos.CENTER);
        GridPane.setValignment(ignore,VPos.CENTER);

        Label infoText = new Label("You can reload more than one Weapon at time");
        label40Helvetica(infoText, "#ffffff", 0.8);
        reloadGrid.add(infoText, 0, 0,weapons.length+2,1);
        GridPane.setHalignment(infoText,HPos.CENTER);
        GridPane.setValignment(infoText,VPos.CENTER);

        reloadGrid.setHgap(50);
        reloadGrid.setVgap(30);
        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(reloadGrid);
        pane.getChildren().add(pane2);
    }

    private void storeButtons(GridPane gridButtons, StackPane pane){
        for (ArrayList<ArrayList<String>> room: gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if(cell.get(CELL_TYPE).equals("SpawnPoint")){
                    int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                    int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                    Button storeButton = new Button("Store");
                    gridButtons.add(storeButton,xPos,yPos);
                    GridPane.setHalignment(storeButton,HPos.CENTER);
                    GridPane.setValignment(storeButton,VPos.CENTER);
                    storeButton.setOnAction(e ->{
                        GridPane grid4 = new GridPane();
                        Rectangle rectangle = new Rectangle();
                        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
                        rectangle.setEffect(new BoxBlur());
                        rectangle.widthProperty().bind(pane.widthProperty());
                        rectangle.heightProperty().bind(pane.heightProperty());
                        String[] weapon = cell.get(CELL_INSIDE).split("'");
                        for(int i=0; i<NUMBER_OF_WEAPON; i++){
                            if(i<weapon.length){
                                String weaponName = weapon[i].toLowerCase();
                                weaponName = weaponName.replace(" ","").concat(".png");
                                grid4.add(new ImageView(new Image("/images/game/weapons/".concat(weaponName),pane.getWidth()/N_COLUMN,pane.getHeight()/NUMBER_OF_WEAPON,false,false)),i,0);
                            }
                            else{
                                grid4.add(new ImageView(new Image("/images/game/weapons/weaponBack.png",pane.getWidth()/N_COLUMN,pane.getHeight()/NUMBER_OF_WEAPON,false,false)),i,0);
                            }
                        }

                        Button backButton = new Button("BACK");
                        grid4.add(backButton,1,1);
                        pane.getChildren().add(rectangle);
                        pane.getChildren().add(grid4);
                        grid4.setHgap(40);
                        grid4.setVgap(50);
                        GridPane.setHalignment(backButton,HPos.CENTER);
                        GridPane.setValignment(backButton,VPos.CENTER);
                        grid4.setAlignment(Pos.CENTER);
                        backButton.setOnAction(ev -> {
                            pane.getChildren().remove(grid4);
                            pane.getChildren().remove(rectangle);
                        });
                    });
                }
            }
        }
    }

    private void columnConstraint(GridPane grid, double nColumn){
        for (int j = 0 ; j < nColumn; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            double dimension = 100/nColumn;
            col.setPercentWidth(dimension);
            grid.getColumnConstraints().add(col);
        }
    }

    private void rowConstraint(GridPane grid, double nRow){
        for (int i = 0 ; i < nRow; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            double dimension = 100/nRow;
            row.setPercentHeight(dimension);
            grid.getRowConstraints().add(row);
        }
    }

    private void label40Helvetica(Label label, String color, double opacity){
        label.setTextFill(Color.web(color, opacity));
        label.setStyle("-fx-font: 40 Helvetica;");
        label.setEffect(new DropShadow());
    }


    /*


        //TODO IF THERE ARE ANY THROWED EXCEPTION
        //informationMessage(pane);

        //TODO IF THE PLAYER WANTS TO RELOAD WEAPONS
        //reload(pane);



    public void drawYellowPlayer(Pane pane){
        drawBloodOnYellow(pane);
        drawMarkOnYellow(pane);
        drawSkullOnYellow(pane);
    }

    public void drawSkullOnYellow(Pane pane){
        //skull
        Image skull = new Image("/images/game/blood/redSkull.png",pane.getWidth()/35,pane.getHeight()/20,false,false);

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
        Image skull = new Image("/images/game/blood/redSkull.png",pane.getWidth()/35,pane.getHeight()/20,false,false);

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
    */
}
