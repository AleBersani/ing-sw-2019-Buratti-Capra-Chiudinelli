package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

import static it.polimi.ingsw.view.gui.GameGUI.*;

/**
 * This class contains all the methods to choose weapons,ammos and cards that will beused in the game
 */
class ChooseGUI {
    /**
     * Reference to GUI
     */
    private GUI gui;
    /**
     * Reference to client
     */
    private Client client;
    /**
     * Reference to GameGUI
     */
    private GameGUI gameGUI;
    /**
     * Reference to ShootingGUI
     */
    private ShootingGUI shootingGUI;
    /**
     * Reference to ButtonsGUI
     */
    private ButtonsGUI buttonsGUI;

    /**
     * This is the constructor of ChooseGUI class
     * @param gui Reference to GUI
     * @param client Reference to client
     * @param gameGUI Reference to GameGUI
     * @param shootingGUI Reference to ShootingGUI
     * @param buttonsGUI Reference to ButtonsGUI
     */
    ChooseGUI(GUI gui, Client client, GameGUI gameGUI, ShootingGUI shootingGUI, ButtonsGUI buttonsGUI) {
        this.gui = gui;
        this.client = client;
        this.gameGUI = gameGUI;
        this.shootingGUI = shootingGUI;
        this.buttonsGUI = buttonsGUI;
    }

    /**
     * This method allows the player to spawn to a spawn point choosing a power up to discard
     * @param stage This parameter is the stage where we used to show
     * @param msg This parameter is the message which contains the power ups that can be discarded to spawn
     */
    void spawn(Stage stage, String msg) {
        StackPane pane = (StackPane) stage.getScene().getRoot();

        StackPane pane2 = new StackPane();
        String[] toShow = msg.split(",");
        String[] powerupNumber = toShow[1].split("'");
        int numberPowerup = powerupNumber.length;
        Rectangle rectangle = new Rectangle();

        Label text = new Label(toShow[0]);
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 35 Helvetica;");
        text.setEffect(new DropShadow());
        text.setAlignment(Pos.CENTER);
        gameGUI.rectangleStandard(rectangle, pane);

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
        for (String powerups : powerupNumber) {

            String realPowerUp = gameGUI.powerUpSwitch(powerups);

            ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
            grid2.add(powerUp, i, 1);
            GridPane.setHalignment(powerUp, HPos.CENTER);
            GridPane.setValignment(powerUp, VPos.CENTER);
            int pU = i;
            powerUp.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                gui.noUpdate = false;
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

        for (ArrayList<ArrayList<String>> room : gui.getBoardRepresentation()) {
            for (ArrayList<String> cell : room) {
                if (cell.get(CELL_TYPE).equals("SpawnPoint")) {
                    Button storeButton = new Button();

                    if(cell.get(CELL_COLOR).equals(YELLOW)){
                        storeButton.setText("Yellow");
                        storeButton.setTranslateX(0);
                        storeButton.setTranslateY(pane.getHeight()/5);
                    }
                    else {
                        if(cell.get(CELL_COLOR).equals(BLUE)){
                            storeButton.setText("Blue");
                            storeButton.setTranslateX(pane.getWidth()/5);
                            storeButton.setTranslateY(pane.getHeight()/5);
                        }
                        else {
                            if(cell.get(CELL_COLOR).equals("red")){
                                storeButton.setText("Red");
                                storeButton.setTranslateX(-pane.getWidth()/5);
                                storeButton.setTranslateY(pane.getHeight()/5);
                            }
                        }
                    }

                    storeButton.setOnAction(e -> {
                        GridPane grid4 = new GridPane();
                        Rectangle secondRectangle = new Rectangle();
                        gameGUI.rectangleStandard(secondRectangle,pane);
                        buttonsGUI.storeWeapon(pane, cell, grid4);

                        Button backButton = new Button("BACK");
                        grid4.add(backButton, 1, 1);
                        pane.getChildren().add(secondRectangle);
                        pane.getChildren().add(grid4);
                        grid4.setHgap(40);
                        grid4.setVgap(50);
                        GridPane.setHalignment(backButton, HPos.CENTER);
                        GridPane.setValignment(backButton, VPos.CENTER);
                        grid4.setAlignment(Pos.CENTER);
                        backButton.setOnAction(ev -> {
                            pane.getChildren().remove(grid4);
                            pane.getChildren().remove(secondRectangle);
                        });
                    });

                    pane2.getChildren().add(storeButton);
                }
            }
        }

        pane.getChildren().add(pane2);
    }

    /**
     * This method interrupt the action and let the player to use a targeting scope to add 1 damage to who is shooted or a tagback grenade to add 1 mark to the shooter
     * @param stage This parameter is the stage where we used to show
     * @param msg This parameter is the message which contains all the available power ups that the player can use in that turn
     */
    void interruptPowerUpUses(Stage stage, String msg){
        StackPane pane = (StackPane) stage.getScene().getRoot();

        StackPane pane2 = new StackPane();
        String[] powerupNumber = msg.split(";");
        int numberPowerup = powerupNumber.length;
        Rectangle rectangle = new Rectangle();

        Label text = new Label("Which one do you want to use?");
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 35 Helvetica;");
        text.setEffect(new DropShadow());
        text.setAlignment(Pos.CENTER);
        gameGUI.rectangleStandard(rectangle, pane);

        GridPane grid2 = new GridPane();
        grid2.add(text, 0, 0, numberPowerup, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);

        int i = 0;
        for (String powerups : powerupNumber) {
            String realPowerUp = gameGUI.powerUpSwitch(powerups);

            ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
            grid2.add(powerUp, i, 1);
            GridPane.setHalignment(powerUp, HPos.CENTER);
            GridPane.setValignment(powerUp, VPos.CENTER);
            int pU = i;
            String type;
            String[] powerUpName = powerups.split(":");
            if(powerUpName[0].equals("tagback grenade")){
                type = "tagBack";
            }
            else {
                type = "interrupt";
            }
            powerUp.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                gameGUI.handPosition = String.valueOf(pU);
                gameGUI.typeOfFire = type;
                this.shootingGUI.buildTarget(stage,"Player;"," ");
                pane.getChildren().remove(pane2);
            });
            i++;
        }
        grid2.setAlignment(Pos.CENTER);
        grid2.setHgap(70);
        grid2.setVgap(50);

        Button nope = new Button("IGNORE");
        grid2.add(nope, 0, 2, numberPowerup, 1);
        GridPane.setHalignment(nope, HPos.CENTER);
        GridPane.setValignment(nope, VPos.CENTER);
        nope.setOnAction(e -> {
            client.send("RPU-no");
            pane.getChildren().remove(pane2);
        });

        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(grid2);
        pane.getChildren().add(pane2);
    }

    /**
     * This method allows the player to special pay the targeting scope choosing from power ups or ammo to pay that cost
     * @param pane This parameter is the pane where we add the pane declared on this method
     * @param msg This parameter is the message with the target and we add how to pay the cost
     */
    void specialPay(StackPane pane, String msg){
        StackPane specialPane = new StackPane();
        GridPane specialGrid = new GridPane();
        Rectangle rectangle = new Rectangle();
        gameGUI.rectangleStandard(rectangle, pane);

        RowConstraints row = new RowConstraints();
        row.setVgrow(Priority.ALWAYS);
        double dimension = 100 / 10;
        row.setPercentHeight(dimension);
        specialGrid.getRowConstraints().add(row);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        double dimension2 = 100 / 3;
        row2.setPercentHeight(dimension2);
        specialGrid.getRowConstraints().add(row2);

        String[] allPowerUps = gui.getYouRepresentation().get(YOU_POWERUP).split("'");

        for(int k=0;k<allPowerUps.length;k++){
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            col.setPercentWidth(100 / N_COLUMN);
            specialGrid.getColumnConstraints().add(col);
        }
        ColumnConstraints col = new ColumnConstraints();
        col.setHgrow(Priority.ALWAYS);
        col.setPercentWidth(100 / 25);
        specialGrid.getColumnConstraints().add(col);
        specialGrid.getColumnConstraints().add(col);


        int z=0;
        for(String powerups : allPowerUps){
            String[] single = powerups.split(":");
            if((single[0].equals("targeting scope"))&&(z == Integer.parseInt(gameGUI.handPosition))){
                break;
            }
            z++;
        }

        int j=0;
        for(int i=0; i<allPowerUps.length;i++){
            if(z != i){
                String realPowerUp = gameGUI.powerUpSwitch(allPowerUps[i]);
                ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                specialGrid.add(powerUp, j, 1);
                final int ammo=j;
                powerUp.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> client.send(msg.concat(">").concat(String.valueOf(ammo))));
                j++;
            }

        }

        for (String ammo : gui.getYouRepresentation().get(PLAYER_AMMO).split("'")) {
            String[] ammoQuantity = ammo.split(":");
            switch (ammoQuantity[0]) {
                case "R": {
                    ImageView redAmmoIV = new ImageView(new Image("/images/game/ammo/redAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    specialGrid.add(redAmmoIV, j, 1);
                    GridPane.setHalignment(redAmmoIV, HPos.RIGHT);
                    GridPane.setValignment(redAmmoIV, VPos.TOP);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 30 Helvetica;");
                    specialGrid.add(numberAmmo, j + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.LEFT);
                    GridPane.setValignment(numberAmmo, VPos.TOP);

                    redAmmoIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> client.send(msg.concat(">").concat(ammoQuantity[0])));
                    break;
                }
                case "Y": {
                    ImageView yellowAmmoIV = new ImageView(new Image("/images/game/ammo/yellowAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    specialGrid.add(yellowAmmoIV, j, 1);
                    GridPane.setHalignment(yellowAmmoIV, HPos.RIGHT);
                    GridPane.setValignment(yellowAmmoIV, VPos.CENTER);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 30 Helvetica;");
                    specialGrid.add(numberAmmo, j + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.LEFT);
                    GridPane.setValignment(numberAmmo, VPos.CENTER);

                    yellowAmmoIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> client.send(msg.concat(">").concat(ammoQuantity[0])));
                    break;
                }
                case "B": {
                    ImageView blueAmmoIV = new ImageView(new Image("/images/game/ammo/blueAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    specialGrid.add(blueAmmoIV, j, 1);
                    GridPane.setHalignment(blueAmmoIV, HPos.RIGHT);
                    GridPane.setValignment(blueAmmoIV, VPos.BOTTOM);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 30 Helvetica;");
                    specialGrid.add(numberAmmo, j + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.LEFT);
                    GridPane.setValignment(numberAmmo, VPos.BOTTOM);

                    blueAmmoIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> client.send(msg.concat(">").concat(ammoQuantity[0])));
                    break;
                }
                default:
            }
        }

        Label title = new Label("How do you want to pay?");
        gameGUI.labelSetting(title,"#ffffff", 0.8, "-fx-font: 40 Helvetica;");
        specialGrid.add(title,0,0,specialGrid.getColumnConstraints().size(),1);
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);

        specialGrid.setAlignment(Pos.CENTER);

        specialPane.getChildren().add(rectangle);
        specialPane.getChildren().add(specialGrid);
        pane.getChildren().add(specialPane);
    }

    /**
     * This method allows the player to pay the effect of the weapons with power ups
     * @param pane This parameter is the pane where we add the pane declared on this method
     * @param messageToSend This parameter is the message that need to be send to the server
     */
    void powerUpPay(Pane pane, String messageToSend) {
        if ((gameGUI.typeOfFire != null)&&((gameGUI.typeOfFire.equals("upu"))||(gameGUI.typeOfFire.equals("interrupt"))||(gameGUI.typeOfFire.equals("Base"))||(gameGUI.typeOfFire.equals("tagBack")))) {
            client.send(messageToSend);
            gameGUI.typeOfFire = "";
        }
        else {
            if(gui.getYouRepresentation().get(PLAYER_POWER_UP).equals("PowerUp:0")){
                client.send(messageToSend.concat("> "));
            }
            else {
                StackPane payPane = new StackPane();
                final String[] powerUpPay = new String[1];
                GridPane payGrid = new GridPane();

                Rectangle rectangle = new Rectangle();
                gameGUI.rectangleStandard(rectangle, pane);
                ColumnConstraints col1 = new ColumnConstraints();
                col1.setPercentWidth(20);
                ColumnConstraints col2 = new ColumnConstraints();
                col2.setPercentWidth(20);
                payGrid.getColumnConstraints().addAll(col1, col2);

                Label title = new Label("Do you want to pay with power ups?");
                gameGUI.labelSetting(title, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
                payGrid.add(title, 0, 0,2,1);
                GridPane.setHalignment(title, HPos.CENTER);
                GridPane.setValignment(title, VPos.CENTER);

                Button yes = new Button("YES");
                payGrid.add(yes, 0, 1);
                payGrid.setVgap(30);
                payGrid.setHgap(50);
                GridPane.setHalignment(yes, HPos.CENTER);
                GridPane.setValignment(yes, VPos.CENTER);
                yes.setOnAction(e -> {
                    payPane.getChildren().remove(payGrid);
                    powerUpPay[0] = ">";
                    GridPane gridPowerUp = new GridPane();

                    int j = 0;
                    if (!gui.getYouRepresentation().get(YOU_POWERUP).equals("")) {
                        String[] powerUps = gui.getYouRepresentation().get(YOU_POWERUP).split("'");
                        boolean[] consumed = new boolean[powerUps.length];
                        for (; j < powerUps.length; j++) {
                            consumed[j] = false;
                            String realPowerUp =  gameGUI.powerUpSwitch(powerUps[j]);
                            ImageView powerUpIV = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                            gridPowerUp.add(powerUpIV, j, 0);
                            final int pu = j;
                            powerUpIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, ev -> {
                                if (!consumed[pu]) {
                                    consumed[pu] = true;
                                    powerUpPay[0] = powerUpPay[0].concat(Integer.toString(pu).concat(","));
                                    powerUpIV.setFitWidth(pane.getWidth() / (N_COLUMN * 2));
                                    powerUpIV.setFitHeight(pane.getHeight() / (NUMBER_OF_WEAPON * 2));
                                } else {
                                    ev.consume();
                                }
                            });
                        }
                    }

                    Button done = new Button("DONE");
                    gridPowerUp.add(done, 0, 1, j, 1);
                    GridPane.setHalignment(done, HPos.CENTER);
                    GridPane.setValignment(done, VPos.CENTER);
                    done.setOnAction(ev -> {
                        if(powerUpPay[0].equals(">")){
                            powerUpPay[0] = "> ";
                        }
                        client.send(messageToSend.concat(powerUpPay[0]));
                        gui.reShow();
                    });

                    gridPowerUp.setVgap(30);
                    gridPowerUp.setHgap(50);
                    gridPowerUp.setAlignment(Pos.CENTER);
                    payPane.getChildren().add(gridPowerUp);
                });

                Button no = new Button("NO");
                payGrid.add(no, 1, 1);
                GridPane.setHalignment(no, HPos.CENTER);
                GridPane.setValignment(no, VPos.CENTER);
                no.setOnAction(e -> {
                    client.send(messageToSend.concat("> "));
                    gui.reShow();
                });

                payGrid.setAlignment(Pos.CENTER);
                payPane.getChildren().add(rectangle);
                payPane.getChildren().add(payGrid);
                pane.getChildren().add(payPane);
            }
        }
    }

    /**
     * This method allows the player to reload the weapons that are not loaded
     * @param stage This parameter is the stage where we used to show
     * @param msg This parameter is the message that contains the weapons that can be reloaded
     */
    void reload(Stage stage, String msg) {
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
        gameGUI.rectangleStandard(rectangle, pane);
        Button ignore = new Button("IGNORE");
        Button done = new Button("DONE ");

        int i;
        final String[] toSend = {"RLD-"};
        String[] weapons = msg.split("'");
        boolean[] consumed = new boolean[weapons.length];
        for (i = 0; i < weapons.length; i++) {
            consumed[i] = false;
            String weaponName = weapons[i].toLowerCase();
            weaponName = weaponName.replace(" ", "").concat(".png");
            ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false));
            reloadGrid.add(weaponIV, i, 1);
            GridPane.setHalignment(weaponIV, HPos.CENTER);
            GridPane.setValignment(weaponIV, VPos.CENTER);
            int h=0;
            int w = 0;
            for(String handWeapon: gui.getYouRepresentation().get(YOU_WEAPON).split("'")){
                String[] onlyWeapon = handWeapon.split(":");
                if(onlyWeapon[0].equals(weapons[i])){
                    w = h;
                }
                h++;
            }
            int finalW = w;
            int finalI = i;
            weaponIV.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                if (!consumed[finalI]) {
                    toSend[0] = toSend[0].concat(Integer.toString(finalW)).concat(",");
                    consumed[finalI] = true;
                    weaponIV.setFitWidth(pane.getWidth() / (N_COLUMN * 1.2));
                    weaponIV.setFitHeight(pane.getHeight() / (NUMBER_OF_WEAPON * 1.2));

                    Label reload = new Label("Reloading");
                    reload.setTextFill(Color.web("#ffffff", 0.8));
                    reload.setStyle("-fx-font: 20 Helvetica;");
                    reload.setEffect(new DropShadow());
                    reloadGrid.add(reload, finalI, 1);
                    GridPane.setHalignment(reload, HPos.CENTER);
                    GridPane.setValignment(reload, VPos.TOP);
                }
            });
        }

        for (String ammo : gui.getYouRepresentation().get(PLAYER_AMMO).split("'")) {
            String[] ammoQuantity = ammo.split(":");
            switch (ammoQuantity[0]) {
                case "R": {
                    ImageView redAmmoIV = new ImageView(new Image("/images/game/ammo/redAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    reloadGrid.add(redAmmoIV, i, 1);
                    GridPane.setHalignment(redAmmoIV, HPos.RIGHT);
                    GridPane.setValignment(redAmmoIV, VPos.TOP);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
                    reloadGrid.add(numberAmmo, i + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.LEFT);
                    GridPane.setValignment(numberAmmo, VPos.TOP);
                    break;
                }
                case "Y": {
                    ImageView yellowAmmoIV = new ImageView(new Image("/images/game/ammo/yellowAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    reloadGrid.add(yellowAmmoIV, i, 1);
                    GridPane.setHalignment(yellowAmmoIV, HPos.RIGHT);
                    GridPane.setValignment(yellowAmmoIV, VPos.CENTER);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
                    reloadGrid.add(numberAmmo, i + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.LEFT);
                    GridPane.setValignment(numberAmmo, VPos.CENTER);
                    break;
                }
                case "B": {
                    ImageView blueAmmoIV = new ImageView(new Image("/images/game/ammo/blueAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                    reloadGrid.add(blueAmmoIV, i, 1);
                    GridPane.setHalignment(blueAmmoIV, HPos.RIGHT);
                    GridPane.setValignment(blueAmmoIV, VPos.BOTTOM);
                    Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                    gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
                    reloadGrid.add(numberAmmo, i + 1, 1);
                    GridPane.setHalignment(numberAmmo, HPos.LEFT);
                    GridPane.setValignment(numberAmmo, VPos.BOTTOM);
                    break;
                }
                default:
            }
        }

        for(int j=0;j<weapons.length+2;j++){
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            col.setPercentWidth(100 / N_COLUMN);
            reloadGrid.getColumnConstraints().add(col);
        }

        reloadGrid.add(done, 0, 2,i+2,1);
        done.setOnAction(e -> {
            gameGUI.typeOfFire = " ";
            powerUpPay(pane,toSend[0]);
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(done, HPos.CENTER);
        GridPane.setValignment(done, VPos.CENTER);

        reloadGrid.add(ignore, 0, 3,i+2,1);
        ignore.setOnAction(e -> client.send("RLD-"));
        GridPane.setHalignment(ignore, HPos.CENTER);
        GridPane.setValignment(ignore, VPos.CENTER);

        Label infoText = new Label("You can reload more than one Weapon at time");
        gameGUI.labelSetting(infoText, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
        reloadGrid.add(infoText, 0, 0, i + 2, 1);
        GridPane.setHalignment(infoText, HPos.CENTER);
        GridPane.setValignment(infoText, VPos.CENTER);

        reloadGrid.setHgap(50);
        reloadGrid.setVgap(30);
        reloadGrid.setAlignment(Pos.CENTER);

        pane2.getChildren().add(rectangle);
        pane2.getChildren().add(reloadGrid);
        pane.getChildren().add(pane2);
    }

    /**
     * This method let the player to lay a weapon with another one when he already has 3 weapons and try to grab another one
     * @param stage This parameter is the stage where we used to show
     * @param msg This parameter is the message that will be displayed on the screen
     */
    void chooseWeapon(Stage stage, String msg) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        StackPane pane2 = new StackPane();
        GridPane grid = new GridPane();

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        double dimension1 = 100 / 10;
        row1.setPercentHeight(dimension1);
        grid.getRowConstraints().add(row1);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        double dimension2 = 100 / NUMBER_OF_WEAPON;
        row2.setPercentHeight(dimension2);
        grid.getRowConstraints().add(row2);

        Rectangle rectangle = new Rectangle();
        gameGUI.rectangleStandard(rectangle, pane);

        String[] message = msg.split(":");

        Label text = new Label(message[0]);
        gameGUI.labelSetting(text, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");

        int j = 0;
        for (String weapon : gui.getYouRepresentation().get(YOU_WEAPON).split("'")) {
            if (!weapon.equals("")) {
                String[] playerWeapon = weapon.split(":");
                String weaponName = playerWeapon[0].toLowerCase().replace(" ", "").concat(".png");
                double notLoaded = 1;
                if (playerWeapon[1].equals("false")) {
                    notLoaded = 1.2;
                }
                ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / (N_COLUMN * notLoaded), pane.getHeight() / (NUMBER_OF_WEAPON * notLoaded), false, false));
                grid.add(weaponIV, j, 1);
                GridPane.setHalignment(weaponIV, HPos.CENTER);
                GridPane.setValignment(weaponIV, VPos.CENTER);
                if(notLoaded==1.2){
                    Label unload = new Label("Unload");
                    gameGUI.labelSetting(unload,"#ffffff",0.8,"-fx-font: 20 Helvetica;");
                    grid.add(unload, j, 1);
                    GridPane.setHalignment(unload, HPos.CENTER);
                    GridPane.setValignment(unload, VPos.TOP);
                }
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
}
