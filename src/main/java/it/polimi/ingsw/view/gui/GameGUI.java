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

public class GameGUI {
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

    public void setShootingGUI(ShootingGUI shootingGUI) {
        this.shootingGUI = shootingGUI;
    }

    void setButtonsGUI(ButtonsGUI buttonsGUI) {
        this.buttonsGUI = buttonsGUI;
    }

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
        for (String powerups : powerupNumber) {

            String realPowerUp = powerUpSwitch(powerups);

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
                        rectangleStandard(secondRectangle,pane);
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

    void interuptPowerUpUses(Stage stage, String msg){
        StackPane pane = (StackPane) stage.getScene().getRoot();

        StackPane pane2 = new StackPane();
        String[] powerupNumber = msg.split(";");
        int numberPowerup = powerupNumber.length;
        Rectangle rectangle = new Rectangle();

        Label text = new Label("Which one do you want tu use?");
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 35 Helvetica;");
        text.setEffect(new DropShadow());
        text.setAlignment(Pos.CENTER);
        rectangleStandard(rectangle, pane);

        GridPane grid2 = new GridPane();
        grid2.add(text, 0, 0, numberPowerup, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);

        int i = 0;
        for (String powerups : powerupNumber) {
            String realPowerUp = powerUpSwitch(powerups);

            ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
            grid2.add(powerUp, i, 1);
            GridPane.setHalignment(powerUp, HPos.CENTER);
            GridPane.setValignment(powerUp, VPos.CENTER);
            int pU = i;
            powerUp.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                this.handPosition = String.valueOf(pU);
                this.typeOfFire = "interupt";
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
        rectangleStandard(rectangle, pane);
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
                    labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
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
                    labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
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
                    labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
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
            powerUpPay(pane,toSend[0]);
            //client.send(toSend[0]); //TODO controllare
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(done, HPos.CENTER);
        GridPane.setValignment(done, VPos.CENTER);

        reloadGrid.add(ignore, 0, 3,i+2,1);
        ignore.setOnAction(e -> client.send("RLD-"));
        GridPane.setHalignment(ignore, HPos.CENTER);
        GridPane.setValignment(ignore, VPos.CENTER);

        Label infoText = new Label("You can reload more than one Weapon at time");
        labelSetting(infoText, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
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

    void powerUpPay(Pane pane, String messageToSend) {//TODO CONTROLLARE QUANDO VIENE CHIAMATA
        if ((typeOfFire != null)&&(typeOfFire.equals("upu"))) {
            client.send(messageToSend);
            typeOfFire = "";
        }
        else {
            StackPane payPane = new StackPane();
            final String[] powerUpPay = new String[1];
            GridPane payGrid = new GridPane();

            Label title = new Label("Do you want to pay with power ups?");
            labelSetting(title, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
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
                powerUpPay[0] = "-";
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
                gridPowerUp.add(done, 0, 1);
                GridPane.setHalignment(done, HPos.CENTER);
                GridPane.setValignment(done, VPos.CENTER);
                done.setOnAction(ev -> {
                    client.send(messageToSend.concat(powerUpPay[0]));
                    pane.getChildren().remove(payPane);
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
                client.send(messageToSend.concat("- "));
                pane.getChildren().remove(payPane);
            });

            Rectangle rectangle = new Rectangle();
            rectangleStandard(rectangle, pane);

            payGrid.setAlignment(Pos.CENTER);
            payPane.getChildren().add(rectangle);
            payPane.getChildren().add(payGrid);
            pane.getChildren().add(payPane);
        }
    }

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
        rectangleStandard(rectangle, pane);

        String[] message = msg.split(":");

        Label text = new Label(message[0]);
        labelSetting(text, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");

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
                    labelSetting(unload,"#ffffff",0.8,"-fx-font: 20 Helvetica;");
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

    public void buildWinner(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        //TODO MESSAGE HANDLER NEED TO CALL TO THE BACKGROUND IMAGE OF THE LOGIN GUI
        //TODO WE NEED TO KNOW ONLY THE NAME OF THE WINNER AND THE POINTS THAT HE MAKES
        GridPane winnerGrid = new GridPane();

        winnerGrid.add(new ImageView(new Image("/images/game/crown.png",pane.getWidth()/40,pane.getHeight()/25,false,false)),0,0);

        Label winner = new Label();
        labelSetting(winner,"#ffffff",0.8,"-fx-font: 50 Helvetica;");
        winnerGrid.add(winner,0,1);
        GridPane.setHalignment(winner,HPos.CENTER);
        GridPane.setValignment(winner,VPos.CENTER);

        Label points = new Label();
        labelSetting(points,"#ffffff",0.8,"-fx-font: 40 Helvetica;");
        winnerGrid.add(points,0,2);
        GridPane.setHalignment(points,HPos.CENTER);
        GridPane.setValignment(points,VPos.CENTER);

        Button exit = new Button("EXIT");
        winnerGrid.add(exit,0,2);
        GridPane.setHalignment(exit,HPos.CENTER);
        GridPane.setValignment(exit,VPos.CENTER);
        exit.setOnAction(e-> client.send("quit"));

        winnerGrid.setVgap(50);

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