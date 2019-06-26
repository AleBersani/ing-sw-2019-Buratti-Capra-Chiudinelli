package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
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
    private Client client;
    private GridPane boardGrid;
    protected boolean endTurn = false;
    private String handPosition;
    private String typeOfFire;
    private String powerUpPay;
    private String nameWeapon;

    private static final String PURPLE = "purple";
    private static final String BLUE = "blue";
    private static final String GREEN = "green";
    private static final String YELLOW = "yellow";
    private static final String GREY = "grey";
    private static final int SHOOT_ADRENALINE = 6;
    private static final int N_INNER_COLUMN = 3;
    private static final int N_COLUMN = 7;
    private static final int N_INNER_ROW = 3;
    private static final int N_ROW = 5;
    private static final int CELL_X = 0;
    private static final int CELL_Y = 1;
    private static final int CELL_COLOR = 2;
    private static final int CELL_TYPE = 3;
    private static final int CELL_INSIDE = 4;
    private static final int CELL_PLAYER_ON_ME = 6;
    private static final int CELL_DOORS = 8;
    private static final int CELL_WALLS = 10;
    private static final int PLAYER_SKULL = 0;
    private static final int PLAYER_ROW_SPAN = 1;
    private static final int PLAYER_AMMO = 1;
    private static final int PLAYER_COL_SPAN = 3;
    private static final int PLAYER_DAMAGE = 3;
    private static final int PLAYER_XPOS = 4;
    private static final int PLAYER_MARK = 5;
    private static final int PLAYER_POWER_UP = 6;
    private static final int PLAYER_WEAPON = 8;
    private static final int PLAYER_COLOR = 9;
    private static final int PLAYER_TURNED = 10;
    private static final int PLAYER_FRENZY = 11;
    private static final int YOU_XPOS = 0;
    private static final int YOU_COL_SPAN = 4;
    private static final int YOU_YPOS = 4;
    private static final int YOU_POINT = 12;
    private static final int YOU_WEAPON = 14;
    private static final int YOU_POWERUP = 16;
    private static final int KILL_ROW = 3;
    private static final int KILL_COL = 0;
    private static final int KILL_TOT_SKULL = 0;
    private static final int KILL_ROW_SPAN = 1;
    private static final int KILL_COL_SPAN = 3;
    private static final int NUMBER_OF_WEAPON = 3;

    public GameGUI(GUI gui, Client client) {
        this.gui = gui;
        this.client = client;
    }

    public void backGround(Stage stage) {
        //backGround image
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        ImageView screen = new ImageView(new Image("/images/game/metallicScreen.png"));
        screen.fitWidthProperty().bind(pane.widthProperty());
        screen.fitHeightProperty().bind(pane.heightProperty());
        pane.getChildren().add(screen);
    }

    public void buildBoard(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        stage.setResizable(false);
        GridPane grid = new GridPane();

        //grid column constraint
        columnConstraint(grid, N_COLUMN);

        //grid row constraint
        rowConstraint(grid, N_ROW);

        //cells
        for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if (!cell.isEmpty()) {
                    int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                    int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                    String color = cell.get(CELL_COLOR);
                    if ((color.equals(BLUE)) || (color.equals(GREEN)) || (color.equals(PURPLE)) || (color.equals("red")) || (color.equals("white")) || (color.equals(YELLOW))) {
                        grid.add(new ImageView(new Image("/images/game/cell/".concat(color).concat("Cell.png"), pane.getWidth() / N_COLUMN, pane.getHeight() / N_ROW, false, false)), xPos, yPos);
                    }
                }
            }
        }

        //walls
        for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                for (String s : cell.get(CELL_WALLS).split("'")) {
                    String wall = "wall".concat(s).concat(".png");
                    if ((wall.equals("wallN.png")) || (wall.equals("wallS.png")) || (wall.equals("wallW.png")) || (wall.equals("wallE.png"))) {
                        grid.add(new ImageView(new Image("/images/game/cell/wall/".concat(wall), pane.getWidth() / N_COLUMN, pane.getHeight() / N_ROW, false, false)), xPos, yPos);
                    }
                }
            }
        }

        //doors
        for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if (!cell.get(CELL_DOORS).isEmpty()) {
                    for (String singleDoor : cell.get(CELL_DOORS).split(":")) {
                        int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                        int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                        String[] door = singleDoor.split("'");
                        int xDoor = Integer.parseInt(door[CELL_X]);
                        int yDoor = Integer.parseInt(door[CELL_Y]);
                        String doorDirection = "";
                        if (xPos + 1 == xDoor) {
                            if (yPos + 1 > yDoor) {
                                doorDirection = "doorN.png";
                            }
                            if (yPos + 1 < yDoor) {
                                doorDirection = "doorS.png";
                            }
                        } else {
                            if (xPos + 1 > xDoor) {
                                doorDirection = "doorW.png";
                            }
                            if (xPos + 1 < xDoor) {
                                doorDirection = "doorE.png";
                            }
                        }
                        grid.add(new ImageView(new Image("/images/game/cell/door/".concat(doorDirection), pane.getWidth() / N_COLUMN, pane.getHeight() / N_ROW, false, false)), xPos, yPos);
                    }
                }
            }
        }

        //Ammopoint
        for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                if (cell.get(CELL_TYPE).equals("AmmoPoint")) {
                    String ammoName = "";
                    if (!cell.get(CELL_INSIDE).equals("")) {
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
        for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                for (String player : cell.get(CELL_PLAYER_ON_ME).split("'")) {
                    switch (player) {
                        case PURPLE: {
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/purpleToken.png", pane.getWidth() / N_COLUMN / N_INNER_COLUMN, pane.getHeight() / N_ROW / N_INNER_ROW, false, false));
                            grid.add(playerToken, xPos, yPos);
                            GridPane.setHalignment(playerToken, HPos.RIGHT);
                            GridPane.setValignment(playerToken, VPos.CENTER);
                            break;
                        }
                        case BLUE: {
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/blueToken.png", pane.getWidth() / N_COLUMN / N_INNER_COLUMN, pane.getHeight() / N_ROW / N_INNER_ROW, false, false));
                            grid.add(playerToken, xPos, yPos);
                            GridPane.setHalignment(playerToken, HPos.LEFT);
                            GridPane.setValignment(playerToken, VPos.CENTER);
                            break;
                        }
                        case GREEN: {
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/greenToken.png", pane.getWidth() / N_COLUMN / N_INNER_COLUMN, pane.getHeight() / N_ROW / N_INNER_ROW, false, false));
                            grid.add(playerToken, xPos, yPos);
                            GridPane.setHalignment(playerToken, HPos.CENTER);
                            GridPane.setValignment(playerToken, VPos.BOTTOM);
                            break;
                        }
                        case YELLOW: {
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/yellowToken.png", pane.getWidth() / N_COLUMN / N_INNER_COLUMN, pane.getHeight() / N_ROW / N_INNER_ROW, false, false));
                            grid.add(playerToken, xPos, yPos);
                            GridPane.setHalignment(playerToken, HPos.LEFT);
                            GridPane.setValignment(playerToken, VPos.BOTTOM);
                            break;
                        }
                        case GREY: {
                            ImageView playerToken = new ImageView(new Image("/images/game/tokens/greyToken.png", pane.getWidth() / N_COLUMN / N_INNER_COLUMN, pane.getHeight() / N_ROW / N_INNER_ROW, false, false));
                            grid.add(playerToken, xPos, yPos);
                            GridPane.setHalignment(playerToken, HPos.RIGHT);
                            GridPane.setValignment(playerToken, VPos.BOTTOM);
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

    public void buildPlayers(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        GridPane grid = new GridPane();

        //grid column constraint
        columnConstraint(grid, N_COLUMN);

        //grid row constraint
        rowConstraint(grid, N_ROW);

        int i = 0;
        for (ArrayList<String> player : gui.getPlayersRepresentation()) {
            String[] color = player.get(PLAYER_COLOR).split(":");
            String playerPlance = color[1];
            String[] turned = player.get(PLAYER_TURNED).split(":");
            String playerTurned = turned[1];
            String[] frenzy = player.get(PLAYER_FRENZY).split(":");
            String turnFrenzy = frenzy[1];
            String declaration = "/images/game/plance/";

            if ((playerPlance.equals(PURPLE)) || (playerPlance.equals(BLUE)) || (playerPlance.equals(GREEN)) || (playerPlance.equals(YELLOW)) || (playerPlance.equals(GREY))) {
                if (turnFrenzy.equals("false")) { //frenzy false
                    grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("Player.png"), pane.getWidth() / N_COLUMN * PLAYER_COL_SPAN, pane.getHeight() / N_ROW, false, false)), PLAYER_XPOS, i, PLAYER_COL_SPAN, PLAYER_ROW_SPAN);
                } else {
                    if (playerTurned.equals("false")) {   //turned false
                        grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("TurnedActionPlayer.png"), pane.getWidth() / N_COLUMN * PLAYER_COL_SPAN, pane.getHeight() / N_ROW, false, false)), PLAYER_XPOS, i, PLAYER_COL_SPAN, PLAYER_ROW_SPAN);
                    } else {   //frenzy true and turned true
                        grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("TurnedPlayer.png"), pane.getWidth() / N_COLUMN * PLAYER_COL_SPAN, pane.getHeight() / N_ROW, false, false)), PLAYER_XPOS, i, PLAYER_COL_SPAN, PLAYER_ROW_SPAN);
                    }
                }
            }
            i++;

            if(!player.get(PLAYER_SKULL).equals("")){
                drawSkullOnPlayer(pane);
            }
            if(!player.get(PLAYER_DAMAGE).equals("")){
                drawBloodOnPlayer(pane);
            }
            if(!player.get(PLAYER_MARK).equals("")){
                drawMarkOnPlayer(pane);
            }
        }
        pane.getChildren().add(grid);
    }

    public void buildYou(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
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
        String[] turned = gui.getYouRepresentation().get(PLAYER_TURNED).split(":");
        String playerTurned = turned[1];
        String[] frenzy = gui.getYouRepresentation().get(PLAYER_FRENZY).split(":");
        String turnFrenzy = frenzy[1];
        String declaration = "/images/game/plance/";

        if ((playerPlance.equals(PURPLE)) || (playerPlance.equals(BLUE)) || (playerPlance.equals(GREEN)) || (playerPlance.equals(YELLOW)) || (playerPlance.equals(GREY))) {
            if (turnFrenzy.equals("false")) {
                grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("Player.png"), pane.getWidth() / N_COLUMN * YOU_COL_SPAN, pane.getHeight() / N_ROW, false, false)), YOU_XPOS, YOU_YPOS, YOU_COL_SPAN, PLAYER_ROW_SPAN);
            } else {
                if (playerTurned.equals("false")) {
                    grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("TurnedActionPlayer.png"), pane.getWidth() / N_COLUMN * YOU_COL_SPAN, pane.getHeight() / N_ROW, false, false)), YOU_XPOS, YOU_YPOS, YOU_COL_SPAN, PLAYER_ROW_SPAN);
                } else {
                    grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("TurnedPlayer.png"), pane.getWidth() / N_COLUMN * YOU_COL_SPAN, pane.getHeight() / N_ROW, false, false)), YOU_XPOS, YOU_YPOS, YOU_COL_SPAN, PLAYER_ROW_SPAN);
                }
            }
        }
        Label points = new Label(gui.getYouRepresentation().get(YOU_POINT));
        grid.add(points, 3, 3);
        points.setTextFill(Color.web("#ffffff", 0.8));
        points.setStyle("-fx-font: 30 Helvetica;");
        points.setEffect(new DropShadow());
        GridPane.setHalignment(points, HPos.CENTER);

        if(!gui.getYouRepresentation().get(PLAYER_SKULL).equals("")){
            drawSkullOnYou(pane);
        }
        if(!gui.getYouRepresentation().get(PLAYER_DAMAGE).equals("")){
            drawBloodOnYou(pane);
        }
        if(!gui.getYouRepresentation().get(PLAYER_MARK).equals("")){
            drawMarkOnYou(pane);
        }
        pane.getChildren().add(grid);
    }

    public void buildKillShotTrack(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        this.stage = stage;
        stage.setResizable(false);
        GridPane grid = new GridPane();

        columnConstraint(grid, N_COLUMN);

        rowConstraint(grid, N_ROW);

        grid.add(new ImageView(new Image("/images/game/killshotTrack.png", pane.getWidth() / N_COLUMN * KILL_COL_SPAN, pane.getHeight() / N_COLUMN, false, false)), KILL_COL, KILL_ROW, KILL_COL_SPAN, KILL_ROW_SPAN);
        pane.getChildren().add(grid);

        Pane pane2 = new Pane();
        String[] damage = gui.getKillShotRepresentation().get(1).split("'");
        String[] doubleDamage = gui.getKillShotRepresentation().get(2).split(",");
        int totalSkull = Integer.valueOf(gui.getKillShotRepresentation().get(KILL_TOT_SKULL));
        for (int i = 0; i < totalSkull; i++) {
            String bloodString = "";
            if ((i < damage.length) && (!damage[i].equals(""))) {
                bloodString = damage[i].concat("Blood");
                if (doubleDamage[i].equals("true")) {
                    bloodString = bloodString.concat("X2");
                }
            } else {
                bloodString = "redSkull";
            }
            ImageView blood = new ImageView(new Image("/images/game/blood/".concat(bloodString).concat(".png"), pane.getWidth() / 30, pane.getHeight() / 15, false, false));
            blood.setX(pane.getWidth() / 3.02 - ((totalSkull - 1 - i) * pane.getWidth() / 21.5));
            blood.setY(pane.getHeight() / 1.5);
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
                rectangleStandard(rectangle, pane);

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
                gridInfo.add(backButton, 0, 1, j + 2, 1);
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
            rectangleStandard(rectangle, pane);

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            double dimension = 100 / 3;
            row.setPercentHeight(dimension);
            gridInfo.getRowConstraints().add(row);

            //weapons
            int j = 0;
            int notLoaded = 1;
            for (String weapon : gui.getYouRepresentation().get(YOU_WEAPON).split("'")) {
                if (!weapon.equals("")) {
                    String[] playerWeapon = weapon.split(":");
                    String weaponName = playerWeapon[0].toLowerCase().replace(" ", "").concat(".png");

                    if (playerWeapon[1].equals("false")) {
                        notLoaded = 2;
                    }
                    gridInfo.add(new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / (N_COLUMN * notLoaded), pane.getHeight() / (NUMBER_OF_WEAPON * notLoaded), false, false)), j, 0);
                }
                j++;
            }

            //power ups
            if (!gui.getYouRepresentation().get(YOU_POWERUP).equals("")) {
                for (String powerups : gui.getYouRepresentation().get(YOU_POWERUP).split("'")) {
                    String realPowerUp = powerUpSwitch(powerups);
                    ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                    gridInfo.add(powerUp, j, 0);
                    j++;
                }
            }
            //ammos
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
            gridInfo.add(backButton, 0, 1, j + 2, 1);
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
            rectangleStandard(rectangle, pane);
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

        if (!endTurn) {
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
                rectangleStandard(rectangle, pane);
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

                    Label text = new Label("Choose a square where you want to move");
                    text.setTextFill(Color.web("#ffffff", 0.8));
                    text.setStyle("-fx-font: 40 Helvetica;");
                    text.setEffect(new DropShadow());
                    grid5.add(text, 0, 3, 4, 1);
                    GridPane.setHalignment(text, HPos.CENTER);
                    GridPane.setValignment(text, VPos.CENTER);

                    EventHandler clickEvent = (EventHandler<MouseEvent>) event1 -> {
                        int cellX = 1 + (int) (event1.getScreenX() / (pane.getWidth() / N_COLUMN));
                        int cellY = 1 + (int) (event1.getScreenY() / (pane.getHeight() / N_ROW));

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

                    Label text = new Label("Choose a square where you want to move and grab");
                    text.setTextFill(Color.web("#ffffff", 0.8));
                    text.setStyle("-fx-font: 40 Helvetica;");
                    text.setEffect(new DropShadow());
                    grid5.add(text, 0, 3, 4, 1);
                    GridPane.setHalignment(text, HPos.CENTER);
                    GridPane.setValignment(text, VPos.CENTER);

                    final boolean[] cancelClickEvent = new boolean[1];
                    cancelClickEvent[0] = true;

                    EventHandler clickEvent = (EventHandler<MouseEvent>) event1 -> {
                        int cellX = 1 + (int) (event1.getScreenX() / (pane.getWidth() / N_COLUMN));
                        int cellY = 1 + (int) (event1.getScreenY() / (pane.getHeight() / N_ROW));

                        if ((cellX < 5) && (cellY < 4) && (cancelClickEvent[0])) {
                            for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
                                for (ArrayList<String> cell : room) {
                                    if ((cell.get(CELL_X).equals(Integer.toString(cellX))) && (cell.get(CELL_Y).equals(Integer.toString(cellY)))) {
                                        if (cell.get(CELL_TYPE).equals("AmmoPoint")) {
                                            client.send("GMC-GRB-".concat(Integer.toString(cellX)).concat(",").concat(Integer.toString(cellY)));
                                        } else {
                                            text.setText("");
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

                //shoot
                shoot.setOnAction(e -> {
                    GridPane gridWeapons = new GridPane();
                    Button backShoot = new Button("BACK");

                    int j = 0;
                    for (String weapon : gui.getYouRepresentation().get(YOU_WEAPON).split("'")) {
                        if (!weapon.equals("")) {
                            String[] playerWeapon = weapon.split(":");
                            if (playerWeapon[1].equals("true")) {
                                String weaponName = playerWeapon[0].toLowerCase().replace(" ", "");
                                ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName).concat(".png"), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false));
                                gridWeapons.add(weaponIV, j, 0);
                                final int wpn = j;
                                weaponIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, ev -> {
                                    client.send("GMC-SHT-".concat(Integer.toString(wpn)));
                                    pane.getChildren().remove(gridWeapons);
                                    pane.getChildren().remove(rectangle);
                                    this.nameWeapon = weaponName;
                                    this.handPosition = Integer.toString(wpn);
                                    //TODO AGGIUNGERE PAGAMENTO POWER UP E POSIZIONE ADRENALINA
                                });
                            }
                        }
                        j++;
                    }

                    if (j == 0) {
                        j = 1;
                    }
                    gridWeapons.add(backShoot, 0, 1, j, 1);
                    GridPane.setHalignment(backShoot, HPos.CENTER);
                    GridPane.setValignment(backShoot, VPos.CENTER);

                    backShoot.setOnAction(ev -> {
                        pane.getChildren().remove(gridWeapons);
                        pane.getChildren().remove(rectangle);
                        actions.fire();
                    });

                    gridWeapons.setHgap(40);
                    gridWeapons.setVgap(50);
                    gridWeapons.setAlignment(Pos.CENTER);
                    pane.getChildren().remove(grid4);
                    pane.getChildren().add(gridWeapons);
                });

            });
        } else {
            //end turn
            Button endTurnButton = new Button("END TURN");
            gridButtons.add(endTurnButton, 4, 4);
            GridPane.setHalignment(endTurnButton, HPos.CENTER);
            GridPane.setValignment(endTurnButton, VPos.CENTER);
            endTurnButton.setOnAction(event -> {
                this.endTurn = false;
                client.send("END-");
            });
        }

        //Use power Up
        Button powerUps = new Button("Use PowerUps");
        gridButtons.add(powerUps, 5, 4);
        GridPane.setHalignment(powerUps, HPos.CENTER);
        GridPane.setValignment(powerUps, VPos.CENTER);
        Button backButton = new Button("BACK");
        powerUps.setOnAction(e -> {
            GridPane powerUpGrid = new GridPane();
            Rectangle rectangle = new Rectangle();
            rectangleStandard(rectangle, pane);

            int j = 0;
            if (!gui.getYouRepresentation().get(YOU_POWERUP).equals("")) {
                for (String powerups : gui.getYouRepresentation().get(YOU_POWERUP).split("'")) {
                    String realPowerUp = powerUpSwitch(powerups);
                    ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                    powerUpGrid.add(powerUp, j, 0);
                    final int pu = j;
                    powerUp.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, ev -> {
                        pane.getChildren().remove(powerUpGrid);
                        pane.getChildren().remove(rectangle);
                        pane.getChildren().remove(backButton);
                        client.send(("GMC-UPU-") + (pu));
                        typeOfFire = "upu";
                        handPosition = Integer.toString(pu);
                    });
                    j++;
                }
            }
            if (j == 0) {
                j = 1;
            }
            powerUpGrid.add(backButton, 0, 1, j, 1);
            pane.getChildren().add(rectangle);
            pane.getChildren().add(powerUpGrid);
            powerUpGrid.setHgap(40);
            powerUpGrid.setVgap(50);
            GridPane.setHalignment(backButton, HPos.CENTER);
            GridPane.setValignment(backButton, VPos.CENTER);
            powerUpGrid.setAlignment(Pos.CENTER);
            backButton.setOnAction(ev -> {
                pane.getChildren().remove(powerUpGrid);
                pane.getChildren().remove(rectangle);
            });
        });
        pane.getChildren().add(gridButtons);
    }

    public void spawn(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();

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
        rectangleStandard(rectangle, pane);

        GridPane grid2 = new GridPane();
        grid2.add(text, 0, 0, numberPowerup, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);

        for (int i = 0; i < numberPowerup; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            col.setPercentWidth(20);
            grid2.getColumnConstraints().add(col);
        }
        int i = 0;
        for (String powerups : gui.getYouRepresentation().get(YOU_POWERUP).split("'")) {

            String realPowerUp = powerUpSwitch(powerups);

            ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
            grid2.add(powerUp, i, 1);
            GridPane.setHalignment(powerUp, HPos.CENTER);
            GridPane.setValignment(powerUp, VPos.CENTER);
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

    protected void reload(Stage stage, String msg) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
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
        rectangleStandard(rectangle, pane);
        Button ignore = new Button("IGNORE");
        Button done = new Button("DONE ");

        int i;
        String toSend = "RLD-";
        String[] weapons = msg.split("'");
        boolean[] consumed = new boolean[weapons.length];
        for (i = 0; i < weapons.length; i++) {
            consumed[i] = false;
            String weaponName = weapons[i].toLowerCase();
            weaponName = weaponName.replace(" ", "").concat(".png");
            ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false));
            reloadGrid.add(weaponIV, i, 1);
            int w = i;
            weaponIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                if (!consumed[w]) {
                    toSend.concat(Integer.toString(w)).concat(",");
                    consumed[w] = true;
                    weaponIV.setFitWidth(pane.getWidth() / (N_COLUMN * 2));
                    weaponIV.setFitHeight(pane.getHeight() / (NUMBER_OF_WEAPON * 2));
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

        reloadGrid.add(done, 0, 2);
        done.setOnAction(e -> client.send(toSend));
        GridPane.setHalignment(done, HPos.CENTER);
        GridPane.setValignment(done, VPos.CENTER);

        reloadGrid.add(ignore, 1, 2);
        ignore.setOnAction(e -> client.send("RLD-ignore"));
        GridPane.setHalignment(ignore, HPos.CENTER);
        GridPane.setValignment(ignore, VPos.CENTER);

        Label infoText = new Label("You can reload more than one Weapon at time");
        label40Helvetica(infoText, "#ffffff", 0.8);
        reloadGrid.add(infoText, 0, 0, weapons.length + 2, 1);
        GridPane.setHalignment(infoText, HPos.CENTER);
        GridPane.setValignment(infoText, VPos.CENTER);

        reloadGrid.setHgap(50);
        reloadGrid.setVgap(30);
        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(reloadGrid);
        pane.getChildren().add(pane2);
    }

    protected void buildTarget(Stage stage, String msg, String movement) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        StackPane targetPane = new StackPane();
        GridPane targetGrid = new GridPane();
        columnConstraint(targetGrid, N_COLUMN);
        rowConstraint(targetGrid, N_ROW);

        Rectangle rectangle = new Rectangle();
        rectangleStandard(rectangle, pane);

        Label text = new Label("");
        label40Helvetica(text, "#ffffff", 0.8);
        targetGrid.add(text, 0, 3, 4, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);

        ArrayList<ArrayList<String>> targetParameters = new ArrayList<>();
        ArrayList<boolean[]> success = new ArrayList<>();
        int i = 0;
        for (String targetParameter : msg.split(";")) {
            targetParameters.add(new ArrayList<>());
            success.add(new boolean[targetParameter.split("'").length]);
            int w=0;
            for (String singleTarget : targetParameter.split(",")) {
                targetParameters.get(i).add(singleTarget);
                success.get(i)[w] = false;
                w++;
            }
            i++;
        }

        targetDescription(targetParameters.get(0).get(0), text);

        ImageView skullIV = new ImageView(new Image("/images/game/blood/redSkull.png",pane.getWidth()/35,pane.getHeight()/20,false,false));
        targetGrid.add(skullIV,4,3);

        final int[] j = {0};
        final int[] k = {0};
        String fireType = "";
        final String[] targetString = {"TRG-"};
        if (typeOfFire.equals("upu")) {
            //caso powerup
            targetString[0] = targetString[0].concat("POU-").concat(handPosition).concat("'");
            fireType = " ";
        } else {
            //caso armi
            targetString[0] = targetString[0].concat("WPN-").concat(handPosition).concat("'").concat(movement).concat("'");
            fireType = typeOfFire;
        }
        final String[] target = {" ", " ", " ", " "};

        String finalFireType = fireType;
        EventHandler clickEvent = (EventHandler<MouseEvent>) event -> {
            if (j[0] >= targetParameters.size()) {
                event.consume();
            } else {
                double pixelX = event.getScreenX();
                double pixelY = event.getScreenY();

                String cellX = Integer.toString(1 + (int) (pixelX / (pane.getWidth() / N_COLUMN)));
                String cellY = Integer.toString(1 + (int) (pixelY / (pane.getHeight() / N_ROW)));

                String[] type = targetParameters.get(j[0]).get(k[0]).split(":");
                if((Integer.valueOf(cellX) == 5) && (Integer.valueOf(cellY) == 4)){
                    success.get(j[0])[k[0]] = true;
                }
                else {
                    if ((Integer.valueOf(cellX) <= 4) && (Integer.valueOf(cellY) <= 3)) {
                        switch (type[0]) {
                            case "Movement": {
                                target[0] = cellX.concat(":").concat(cellY);
                                success.get(j[0])[k[0]] = true;
                                break;
                            }
                            case "Player": {
                                String innerCellX = Integer.toString((int) (pixelX / (pane.getWidth() / (N_COLUMN * N_INNER_COLUMN))) % N_INNER_COLUMN);
                                String innerCellY = Integer.toString((int) (pixelY / (pane.getHeight() / (N_ROW * N_INNER_ROW))) % N_INNER_ROW);
                                String pos = innerCellX.concat(innerCellY);
                                String color = "";
                                switch (pos) {
                                    case "01": {
                                        color = BLUE;
                                        break;
                                    }
                                    case "02": {
                                        color = YELLOW;
                                        break;
                                    }
                                    case "12": {
                                        color = GREEN;
                                        break;
                                    }
                                    case "22": {
                                        color = GREY;
                                        break;
                                    }
                                    case "21": {
                                        color = PURPLE;
                                        break;
                                    }
                                    default: {
                                    }
                                }
                                if (!color.equals("")) {
                                    for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
                                        for (ArrayList<String> cell : room) {
                                            if ((cell.get(CELL_X).equals(cellX)) && (cell.get(CELL_Y).equals(cellY))) {
                                                for (String player : cell.get(CELL_PLAYER_ON_ME).split("'")) {
                                                    if (player.equals(color)) {
                                                        target[1] = color;
                                                        success.get(j[0])[k[0]] = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                            case "Room": {
                                target[2] = cellX.concat(":").concat(cellY);
                                success.get(j[0])[k[0]] = true;
                                break;
                            }
                            case "Square": {
                                target[3] = cellX.concat(":").concat(cellY);
                                success.get(j[0])[k[0]] = true;
                                break;
                            }
                            default: {
                            }
                        }
                    }
                }
                if (success.get(j[0])[k[0]]) {
                    success.get(j[0])[k[0]] = false;
                    k[0]++;
                    if (k[0] >= targetParameters.get(j[0]).size()) {
                        k[0] = 0;
                        j[0]++;
                        targetString[0] = targetString[0].concat(target[0]).concat(",").concat(target[1]).concat(",").concat(target[2]).concat(",").concat(target[3]).concat(",").concat(finalFireType).concat(";");
                        for (int z = 0; z < target.length; z++) {
                            target[z] = " ";
                        }
                        if (j[0] >= targetParameters.size()) {
                            client.send(targetString[0]);
                        }
                    }
                    if (j[0] < targetParameters.size()) {
                        targetDescription(targetParameters.get(j[0]).get(k[0]), text);
                    }
                }
            }
        };

        targetPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
        targetPane.getChildren().add(rectangle);
        targetPane.getChildren().add(boardGrid);
        storeButtons(targetGrid, targetPane);
        targetPane.getChildren().add(targetGrid);

        pane.getChildren().add(targetPane);
    }

    private void targetDescription(String msg, Label infoText) {
        String info = "";
        switch (msg) {
            case "Movement:enemy": {
                info = "Choose a square where you want to move the enemy player";
                break;
            }
            case "Movement:self": {
                info = "Choose a square where you want to move";
                break;
            }
            case "Player": {
                info = "Choose an enemy player";
                break;
            }
            case "Room": {
                info = "Choose a square to select the room";
                break;
            }
            case "Square": {
                info = "Choose a square";
                break;
            }
            default: {
            }
        }
        infoText.setText(info);
    }

    public void preShootShow(Stage stage, String msg) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        StackPane preShootPane = new StackPane();
        GridPane preShootGrid = new GridPane();

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(20);
        preShootGrid.getRowConstraints().add(row);

        Rectangle rectangle = new Rectangle();
        rectangleStandard(rectangle, pane);

        Label text = new Label("Choose the type of fire");
        label40Helvetica(text, "#ffffff", 0.8);
        preShootGrid.add(text, 0, 0, 2, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);

        String[] effect = msg.split("'");

        int j = 1;
        ArrayList<String> target = new ArrayList<>();
        for (String trg : effect) {
            Button effectButton = new Button();
            String[] meaning = trg.split("-");
            final String fire = trg;
            final int w = j - 1;
            effectButton.setOnAction(e -> {
                this.typeOfFire = fire;
                shootMovement(stage, target.get(w));
                pane.getChildren().remove(preShootPane);
            });
            switch (meaning[0]) {
                case "Base": {
                    effectButton.setText("BASE");
                    preShootGrid.add(effectButton, 1, j);
                    j++;
                    break;
                }
                case "Optional": {
                    effectButton.setText("OPT. ".concat(Integer.toString(Integer.valueOf(meaning[1]) + 1)));
                    preShootGrid.add(effectButton, 1, j);
                    j++;
                    break;
                }
                case "Alternative": {
                    effectButton.setText("ALT.");
                    preShootGrid.add(effectButton, 1, j);
                    j++;
                    break;
                }
                default: {
                    target.add(trg);
                }
            }
            GridPane.setHalignment(effectButton, HPos.CENTER);
            GridPane.setValignment(effectButton, VPos.CENTER);
        }

        String weaponName = this.nameWeapon;
        for (String trg : effect) {
            if (trg.startsWith("Optional-")) {
                weaponName = this.nameWeapon.concat("Info");
                break;
            }
        }
        ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName).concat(".png"), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false));
        preShootGrid.add(weaponIV, 0, 1, 1, j - 1);

        for (int i = 0; i < (j - 1); i++) {
            RowConstraints buttonRow = new RowConstraints();
            buttonRow.setPrefHeight(pane.getHeight() / (NUMBER_OF_WEAPON * (j - 1)));
            preShootGrid.getRowConstraints().add(buttonRow);
        }

        preShootGrid.setHgap(50);
        preShootGrid.setAlignment(Pos.CENTER);

        preShootPane.getChildren().add(rectangle);
        preShootPane.getChildren().add(preShootGrid);
        pane.getChildren().add(preShootPane);
    }

    private void shootMovement(Stage stage, String msg) {
        if (gui.getYouRepresentation().get(PLAYER_DAMAGE).split("'").length >= SHOOT_ADRENALINE) {
            StackPane pane = (StackPane) stage.getScene().getRoot();
            StackPane movementPane = new StackPane();
            GridPane movementGrid = new GridPane();
            rowConstraint(movementGrid, N_ROW);
            columnConstraint(movementGrid, N_COLUMN);

            Rectangle rectangle = new Rectangle();
            rectangleStandard(rectangle, pane);

            Label text = new Label("Where do you want to move?");
            label40Helvetica(text, "#ffffff", 0.8);
            movementGrid.add(text, 0, 3, 4, 1);
            GridPane.setHalignment(text, HPos.CENTER);
            GridPane.setValignment(text, VPos.CENTER);

            EventHandler clickEvent = (EventHandler<MouseEvent>) event -> {
                String cellX = Integer.toString(1 + (int) (event.getScreenX() / (pane.getWidth() / N_COLUMN)));
                String cellY = Integer.toString(1 + (int) (event.getScreenY() / (pane.getHeight() / N_ROW)));
                if ((Integer.valueOf(cellX) <= 4) && (Integer.valueOf(cellY) <= 3)) {
                    buildTarget(stage, msg, cellX.concat(":").concat(cellY));
                    pane.getChildren().remove(movementPane);
                }
            };

            movementPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
            movementPane.getChildren().add(rectangle);
            movementPane.getChildren().add(boardGrid);
            movementPane.getChildren().add(movementGrid);
            pane.getChildren().add(movementPane);
        } else {
            buildTarget(stage, msg, "");
        }
    }

    private void storeButtons(GridPane gridButtons, StackPane pane) {
        for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if (cell.get(CELL_TYPE).equals("SpawnPoint")) {
                    int xPos = Integer.valueOf(cell.get(CELL_X)) - 1;
                    int yPos = Integer.valueOf(cell.get(CELL_Y)) - 1;
                    Button storeButton = new Button("Store");
                    gridButtons.add(storeButton, xPos, yPos);
                    GridPane.setHalignment(storeButton, HPos.CENTER);
                    GridPane.setValignment(storeButton, VPos.CENTER);
                    storeButton.setOnAction(e -> {
                        GridPane grid4 = new GridPane();
                        Rectangle rectangle = new Rectangle();
                        rectangleStandard(rectangle, pane);
                        String[] weapon = cell.get(CELL_INSIDE).split("'");
                        for (int i = 0; i < NUMBER_OF_WEAPON; i++) {
                            if (i < weapon.length) {
                                String weaponName = weapon[i].toLowerCase();
                                weaponName = weaponName.replace(" ", "").concat(".png");
                                grid4.add(new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), i, 0);
                            } else {
                                grid4.add(new ImageView(new Image("/images/game/weapons/weaponBack.png", pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), i, 0);
                            }
                        }

                        Button backButton = new Button("BACK");
                        grid4.add(backButton, 1, 1);
                        pane.getChildren().add(rectangle);
                        pane.getChildren().add(grid4);
                        grid4.setHgap(40);
                        grid4.setVgap(50);
                        GridPane.setHalignment(backButton, HPos.CENTER);
                        GridPane.setValignment(backButton, VPos.CENTER);
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


    private void powerUpPay(Pane pane) {//TODO CONTROLLARE QUANDO VIENE CHIAMATA
        Pane payPane = new Pane();

        GridPane payGrid = new GridPane();

        Label title = new Label("Do you want to pay with power ups?");
        label40Helvetica(title, "#ffffff", 0.8);
        payGrid.add(title, 0, 0);
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);

        Button yes = new Button("YES");
        payGrid.add(yes, 0, 1);
        GridPane.setHalignment(yes, HPos.CENTER);
        GridPane.setValignment(yes, VPos.CENTER);
        yes.setOnAction(e -> {
            payPane.getChildren().remove(payGrid);

            GridPane gridPowerUp = new GridPane();

            if (!gui.getYouRepresentation().get(YOU_POWERUP).equals("")) {
                String[] powerUps = gui.getYouRepresentation().get(YOU_POWERUP).split("'");
                boolean[] consumed = new boolean[powerUps.length];
                for (int j = 0; j < powerUps.length; j++) {
                    consumed[j] = false;
                    String realPowerUp = powerUpSwitch(powerUps[j]);
                    ImageView powerUpIV = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                    gridPowerUp.add(powerUpIV, j, 0);
                    final int pu = j;
                    powerUpIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, ev -> {
                        if (!consumed[pu]) {
                            consumed[pu] = true;
                            this.powerUpPay = this.powerUpPay.concat(Integer.toString(pu).concat(","));
                            powerUpIV.setFitWidth(pane.getWidth() / (N_COLUMN * 2));
                            powerUpIV.setFitHeight(pane.getHeight() / (NUMBER_OF_WEAPON * 2));
                        } else {
                            ev.consume();
                        }
                    });
                }
            }

            Button done = new Button("DONE");
            gridPowerUp.add(done, 0, 1);
            GridPane.setHalignment(done, HPos.CENTER);
            GridPane.setValignment(done, VPos.CENTER);
            done.setOnAction(ev -> {
                //TODO cosa bisogna mandare qui così
                pane.getChildren().remove(payPane);
            });

            payPane.getChildren().add(gridPowerUp);
        });

        Button no = new Button("NO");
        payGrid.add(no, 1, 1);
        GridPane.setHalignment(no, HPos.CENTER);
        GridPane.setValignment(no, VPos.CENTER);
        no.setOnAction(e -> {
            this.powerUpPay = " ";
            pane.getChildren().remove(payPane);
        });

        Rectangle rectangle = new Rectangle();
        rectangleStandard(rectangle, pane);

        payGrid.setAlignment(Pos.CENTER);
        payPane.getChildren().add(rectangle);
        payPane.getChildren().add(payGrid);
        pane.getChildren().add(payPane);
    }

    public void chooseWeapon(Stage stage, String msg) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        StackPane pane2 = new StackPane();
        GridPane grid = new GridPane();

        Rectangle rectangle = new Rectangle();
        rectangleStandard(rectangle, pane);

        String[] message = msg.split(":");

        Label text = new Label(message[0]);
        label40Helvetica(text, "#ffffff", 0.8);

        int j = 0;
        int notLoaded = 1;
        for (String weapon : gui.getYouRepresentation().get(YOU_WEAPON).split("'")) {
            if (!weapon.equals("")) {
                String[] playerWeapon = weapon.split(":");
                String weaponName = playerWeapon[0].toLowerCase().replace(" ", "").concat(".png");

                if (playerWeapon[1].equals("false")) {
                    notLoaded = 2;
                }
                ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / (N_COLUMN * notLoaded), pane.getHeight() / (NUMBER_OF_WEAPON * notLoaded), false, false));
                grid.add(weaponIV, j, 1);
                final int wpn = j;
                weaponIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                    client.send("WPN-".concat(Integer.toString(wpn)));
                    pane.getChildren().remove(pane2);
                });
            }
            j++;
        }

        grid.add(text, 0, 0, j, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);

        grid.setAlignment(Pos.CENTER);
        grid.setVgap(30);
        grid.setHgap(50);

        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid);
        pane.getChildren().add(pane2);
    }

    public void drawBloodOnYou(Pane pane) {
        String[] blood = gui.getYouRepresentation().get(PLAYER_DAMAGE).split("'");

        for(int i=0;i<blood.length;i++){
            ImageView bloodIV = new ImageView(new Image("/images/game/blood/".concat(blood[i]).concat("Blood.png"),pane.getWidth()/25,pane.getHeight()/15,false,false));
            bloodIV.setX(pane.getWidth()/21.3333 + (i * pane.getWidth()/31.4754));
            bloodIV.setY(pane.getHeight()/1.1489);
            pane.getChildren().add(bloodIV);
        }
    }

    public void drawSkullOnYou(Pane pane) {
        String[] skull = gui.getYouRepresentation().get(PLAYER_SKULL).split(":");

        for (int i = 0; i < Integer.valueOf(skull[1]); i++) {
            ImageView skullIV = new ImageView(new Image("/images/game/blood/redSkull.png",pane.getWidth()/35,pane.getHeight()/20,false,false));
            skullIV.setX(pane.getWidth() / 8.3478 + (i * pane.getWidth() / 33.6842));
            skullIV.setY(pane.getHeight() / 1.0588);
            pane.getChildren().add(skullIV);
        }
    }

    public void drawMarkOnYou(Pane pane){
        String[] mark = gui.getYouRepresentation().get(PLAYER_MARK).split("'");

        for(int i=0;i<mark.length;i++){
            ImageView markIV = new ImageView(new Image("/images/game/blood/".concat(mark[i]).concat("Blood.png"),pane.getWidth()/40,pane.getHeight()/25,false,false));
            markIV.setX(pane.getWidth()/2.4303 - (i * pane.getWidth()/76.8));
            markIV.setY(pane.getHeight()/1.2485);
            pane.getChildren().add(markIV);
        }
    }

    public void drawBloodOnPlayer(Pane pane){
        int j=0;
        for(ArrayList<String> player : gui.getPlayersRepresentation()) {
            String[] blood = player.get(PLAYER_SKULL).split("'");

            for(int i=0;i<blood.length;i++){
                ImageView bloodIV = new ImageView(new Image("/images/game/blood/".concat(blood[i]).concat("Blood.png"),pane.getWidth()/25,pane.getHeight()/15,false,false));
                bloodIV.setX(pane.getWidth()/1.6666 + (i * pane.getWidth()/40.8510));
                bloodIV.setY(pane.getHeight()/14.4 + (j*pane.getHeight()/N_ROW));
                pane.getChildren().add(bloodIV);
            }
            j++;
        }
    }

    public void drawSkullOnPlayer(Pane pane){
        int j=0;
        for(ArrayList<String> player : gui.getPlayersRepresentation()){
            String[] skull = player.get(PLAYER_SKULL).split(":");

            for (int i = 0; i < Integer.valueOf(skull[1]); i++) {
                ImageView skullIV = new ImageView(new Image("/images/game/blood/redSkull.png", pane.getWidth() / 35, pane.getHeight() / 20, false, false));
                skullIV.setX(pane.getWidth() / 1.5238 + (i * pane.getWidth() / 42.6666));
                skullIV.setY(pane.getHeight() / 6.75 + (j*pane.getHeight()/N_ROW));
                pane.getChildren().add(skullIV);
            }
            j++;
        }
    }

    public void drawMarkOnPlayer(Pane pane){
        int j=0;
        for(ArrayList<String> player : gui.getPlayersRepresentation()) {
            String[] mark = player.get(PLAYER_MARK).split("'");

            for(int i=0;i<mark.length;i++){
                ImageView markIV = new ImageView(new Image("/images/game/blood/".concat(mark[i]).concat("Blood.png"),pane.getWidth()/40,pane.getHeight()/25,false,false));
                markIV.setX(pane.getWidth()/1.1428 - (i * pane.getWidth()/106.6666));
                markIV.setY(pane.getHeight()/360 + (j*pane.getHeight()/N_ROW));
                pane.getChildren().add(markIV);
            }
            j++;
        }
    }

    public void informationMessage(Stage stage,String msg){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        StackPane pane2 = new StackPane();

        GridPane grid = new GridPane();

        Rectangle rectangle = new Rectangle();
        rectangleStandard(rectangle,pane);

        Label text = new Label(msg);
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 50 Helvetica;");
        text.setEffect(new DropShadow());
        GridPane.setHalignment(text,HPos. CENTER);
        GridPane.setValignment(text,VPos. CENTER);

        Button ok = new Button("OK");
        GridPane.setHalignment(ok,HPos. CENTER);
        GridPane.setValignment(ok,VPos. CENTER);
        ok.setOnAction(e-> pane.getChildren().remove(pane2));

        grid.setVgap(50);

        grid.add(text,0,0);
        grid.add(ok,0,1);
        grid.setAlignment(Pos.CENTER);

        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid);
        pane.getChildren().add(pane2);
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

    private void rectangleStandard(Rectangle rectangle, Pane pane){
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty());
    }

    private void label40Helvetica(Label label, String color, double opacity){
        label.setTextFill(Color.web(color, opacity));
        label.setStyle("-fx-font: 40 Helvetica;");
        label.setEffect(new DropShadow());
    }

    private String powerUpSwitch(String powerups){
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