package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

import static it.polimi.ingsw.view.gui.GameGUI.*;

class ButtonsGUI {
    private GUI gui;
    private Client client;
    private GameGUI gameGUI;
    private ShootingGUI shootingGUI;

    ButtonsGUI(GUI gui, Client client, GameGUI gameGUI) {
        this.gui = gui;
        this.client = client;
        this.gameGUI = gameGUI;
    }

    public void setShootingGUI(ShootingGUI shootingGUI) {
        this.shootingGUI = shootingGUI;
    }

    void buildButtons(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        GridPane gridButtons = new GridPane();
        stage.getScene().setRoot(pane);
        gameGUI.columnConstraint(gridButtons, N_COLUMN);
        gameGUI.rowConstraint(gridButtons, N_ROW);

        Button stopClick = new Button();
        Button startClick = new Button();

        //store button
        storeButtons(gridButtons, pane, stopClick, startClick);

        //info button enemy
        int i = 0;
        for (ArrayList<String> enemyPlayer : gui.getPlayersRepresentation()) {
            infoEnemy(gridButtons,pane,enemyPlayer,i);
            i++;
        }

        //info button myself
        infoMyself(gridButtons,pane);

        //quit button
        quitButton(gridButtons,pane);

        if(!gameGUI.optionalShoot) {
            if (!gameGUI.endTurn) {
                //Actions
                actionButton(stage,gridButtons,pane);
            } else {
                //end turn
                Button endTurnButton = new Button("END TURN");
                gridButtons.add(endTurnButton, 4, 4);
                GridPane.setHalignment(endTurnButton, HPos.CENTER);
                GridPane.setValignment(endTurnButton, VPos.CENTER);
                endTurnButton.setOnAction(event -> {
                    gameGUI.endTurn = false;
                    client.send("END-");
                });
            }

            //Use power Up
            usePowerUp(gridButtons,pane);
        }
        else{
            //continue shooting optional
            keepShooting(gridButtons,pane,stage);

            //stop shooting
            Button stopButton = new Button("STOP");
            gridButtons.add(stopButton, 5, 4);
            GridPane.setHalignment(stopButton, HPos.CENTER);
            GridPane.setValignment(stopButton, VPos.CENTER);
            stopButton.setOnAction(e -> {
                client.send("ESH-");
                gameGUI.optionalShoot = false;
            });
        }

        pane.getChildren().add(gridButtons);
    }

    private void actionButton(Stage stage, GridPane gridButtons, StackPane pane){
        Button actions = new Button("Actions");
        gridButtons.add(actions, 4, 4);
        GridPane.setHalignment(actions, HPos.CENTER);
        GridPane.setValignment(actions, VPos.CENTER);
        actions.setOnAction(event -> {
            GridPane grid4 = new GridPane();
            Rectangle rectangle = new Rectangle();

            Button back = new Button("BACK");
            gameGUI.rectangleStandard(rectangle, pane);

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
            if(gui.getYouRepresentation().get(PLAYER_FRENZY).equals("Frenzy:true")){
                String[] numberAction = gui.getYouRepresentation().get(YOU_FRENZY_ACTION).replace(" ","").split(":");
                if(numberAction[1].equals("2")){
                    //run
                    runAction(stage, grid4, pane, rectangle, actions, "RUN",1,false);
                }
                //shoot
                runAction(stage, grid4, pane, rectangle, actions, "SHOOT",0,true);
            }
            else {
                //run
                runAction(stage, grid4, pane, rectangle, actions, "RUN",1,false);

                //shoot
                shootAction(grid4, pane, rectangle, actions);
            }
            //Grab
            grabAction(stage, gridButtons, grid4, pane, rectangle, actions);
        });
    }

    private void shootAction(GridPane grid4, StackPane pane, Rectangle rectangle, Button actions){
        Button shoot = new Button("SHOOT");
        grid4.add(shoot, 0, 0);
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
                            gameGUI.nameWeapon = weaponName;
                            gameGUI.handPosition = Integer.toString(wpn);
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
    }

    private void grabAction(Stage stage, GridPane gridButtons, GridPane grid4, StackPane pane, Rectangle rectangle, Button actions){
        Button stopClick = new Button();
        Button startClick = new Button();
        Button grab = new Button("GRAB");
        grid4.add(grab, 2, 0);
        grab.setOnAction(e -> {
            pane.getChildren().remove(grid4);
            pane.getChildren().remove(gameGUI.boardGrid);
            pane.getChildren().add(gameGUI.boardGrid);

            GridPane grid5 = new GridPane();
            gameGUI.columnConstraint(grid5, N_COLUMN);
            gameGUI.rowConstraint(grid5, N_ROW);
            storeButtons(grid5, pane, stopClick, startClick);

            Button backRun = new Button("BACK");
            grid5.add(backRun, 4, 3);
            GridPane.setHalignment(backRun, HPos.CENTER);
            GridPane.setValignment(backRun, VPos.CENTER);

            Label text = new Label("Choose a square where you want to move and grab");
            gameGUI.labelSetting(text,"#ffffff",0.8,"-fx-font: 40 Helvetica;");
            grid5.add(text, 0, 3, 4, 1);
            GridPane.setHalignment(text, HPos.CENTER);
            GridPane.setValignment(text, VPos.CENTER);

            final boolean[] cancelClickEvent = new boolean[1];
            cancelClickEvent[0] = true;

            EventHandler<MouseEvent> clickEvent = event1 -> {
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
                                                String messageToSend = "GMC-GRB-".concat(Integer.toString(cellX)).concat(",").concat(Integer.toString(cellY).concat(",").concat(Integer.toString(w)));
                                                gameGUI.typeOfFire = " ";
                                                gameGUI.powerUpPay(pane,messageToSend);
                                                pane.getChildren().remove(grid);
                                            });
                                        } else {
                                            grid.add(new ImageView(new Image("/images/game/weapons/weaponBack.png", pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), j, 0);
                                        }
                                    }


                                    pane.getChildren().remove(rectangle);
                                    pane.getChildren().add(rectangle);
                                    pane.getChildren().add(grid);
                                    grid.setHgap(40);
                                    grid.setVgap(50);
                                    Button backButton = new Button("BACK");
                                    grid.add(backButton, 1, 1);
                                    GridPane.setHalignment(backButton, HPos.CENTER);
                                    GridPane.setValignment(backButton, VPos.CENTER);
                                    grid.setAlignment(Pos.CENTER);
                                    backButton.setOnAction(ev -> {
                                        pane.getChildren().remove(grid);
                                        pane.getChildren().remove(rectangle);
                                        pane.getChildren().remove(grid5);
                                        pane.getChildren().remove(gameGUI.boardGrid);
                                        pane.getChildren().add(gameGUI.boardGrid);
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
            stopClick.setOnAction(ev -> pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));
            startClick.setOnAction(ev -> pane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));
            startClick.fire();

            //Back
            runGrabBackButton(stage, pane, rectangle, actions, grid5, backRun, clickEvent);

            pane.getChildren().add(grid5);
        });
    }

    private void runAction(Stage stage, GridPane grid4, StackPane pane, Rectangle rectangle, Button actions, String title, int columnIndex, boolean shoot){
        Button stopClick = new Button();
        Button startClick = new Button();
        Button run = new Button(title);
        grid4.add(run, columnIndex, 0);
        run.setOnAction(e -> {
            pane.getChildren().remove(grid4);
            pane.getChildren().remove(gameGUI.boardGrid);
            pane.getChildren().add(gameGUI.boardGrid);

            GridPane grid5 = new GridPane();
            gameGUI.columnConstraint(grid5, N_COLUMN);
            gameGUI.rowConstraint(grid5, N_ROW);
            storeButtons(grid5, pane, stopClick, startClick);

            Label text = new Label("Choose a square where you want to move");
            gameGUI.labelSetting(text,"#ffffff",0.8,"-fx-font: 40 Helvetica;");
            grid5.add(text, 0, 3, 4, 1);
            GridPane.setHalignment(text, HPos.CENTER);
            GridPane.setValignment(text, VPos.CENTER);

            EventHandler<MouseEvent> clickEvent = event1 -> {
                int cellX = 1 + (int) (event1.getScreenX() / (pane.getWidth() / N_COLUMN));
                int cellY = 1 + (int) (event1.getScreenY() / (pane.getHeight() / N_ROW));

                if ((cellX < 5) && (cellY < 4)) {
                    if(shoot){
                        client.send("GMC-SHT-".concat(Integer.toString(cellX)).concat(":").concat(Integer.toString(cellY)));
                    }
                    else{
                        client.send("GMC-RUN-".concat(Integer.toString(cellX)).concat(",").concat(Integer.toString(cellY)));

                    }
                }
            };

            //Click event
            stopClick.setOnAction(ev -> pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));
            startClick.setOnAction(ev -> pane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));
            startClick.fire();

            //Back
            Button backRun = new Button("BACK");
            grid5.add(backRun, 4, 3);
            GridPane.setHalignment(backRun, HPos.CENTER);
            GridPane.setValignment(backRun, VPos.CENTER);
            runGrabBackButton(stage, pane, rectangle, actions, grid5, backRun, clickEvent);

            pane.getChildren().add(grid5);
        });
    }


    private void runGrabBackButton(Stage stage, StackPane pane, Rectangle rectangle, Button actions, GridPane grid5, Button backRun, EventHandler<MouseEvent> clickEvent) {
        backRun.setOnAction(ev -> {
            pane.getChildren().remove(grid5);
            pane.getChildren().remove(rectangle);
            buildButtons(stage);
            actions.fire();
            pane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
        });
    }

    private void keepShooting(GridPane gridButtons, StackPane pane, Stage stage){
        Button keepOptionalButton = new Button("KEEP SHOOTING");
        if(!gui.getOpz().equals("")) {
            gridButtons.add(keepOptionalButton, 4, 4);
        }
        GridPane.setHalignment(keepOptionalButton, HPos.CENTER);
        GridPane.setValignment(keepOptionalButton, VPos.CENTER);
        keepOptionalButton.setOnAction(event -> {

            StackPane opzShootPane = new StackPane();
            GridPane opzShootGrid = new GridPane();
            opzShootGrid.setAlignment(Pos.CENTER);

            Rectangle rectangle = new Rectangle();
            gameGUI.rectangleStandard(rectangle,opzShootPane);
            opzShootPane.getChildren().add(rectangle);

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20);
            opzShootGrid.getRowConstraints().add(row);

            Label text = new Label("Choose the type of fire");
            gameGUI.labelSetting(text, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
            opzShootGrid.add(text, 0, 0, 2, 1);
            GridPane.setHalignment(text, HPos.CENTER);
            GridPane.setValignment(text, VPos.CENTER);

            ArrayList<String> effects = new ArrayList<>();
            for(String option: gui.getOpz().split(";")){
                option = option.substring(0,1);
                if(!effects.isEmpty()){
                    if(!effects.contains(option)){
                        effects.add(option);
                    }
                }
                else {
                    effects.add(option);
                }
            }

            int j=1;
            String[] requestedTarget = gui.getInfoTarget().split("'");
            for(String singleEffect: effects){
                Button effectButton = new Button();
                GridPane.setHalignment(effectButton, HPos.CENTER);
                GridPane.setValignment(effectButton, VPos.CENTER);
                opzShootGrid.add(effectButton, 1, j);
                String type;
                if(singleEffect.equals("0")){
                    type = "Base";
                    effectButton.setText("BASE");
                }
                else{
                    type = "Optional-".concat(Integer.toString((Integer.parseInt(singleEffect))-1));
                    effectButton.setText("OPT-".concat(singleEffect));
                }
                final String fire = type;
                final int w = ((Integer.parseInt(singleEffect))*2)+1;
                effectButton.setOnAction(e -> {
                    gameGUI.typeOfFire = fire;
                    shootingGUI.buildTarget(stage,requestedTarget[w]," ");
                    pane.getChildren().remove(opzShootPane);
                });

                j++;
            }

            for (int k = 0; k < (j - 1); k++) {
                RowConstraints buttonRow = new RowConstraints();
                buttonRow.setPrefHeight(pane.getHeight() / (NUMBER_OF_WEAPON * (j - 1)));
                opzShootGrid.getRowConstraints().add(buttonRow);
            }

            String weaponName = gameGUI.nameWeapon.concat("Info");
            ImageView weaponIV = new ImageView(new Image("/images/game/weapons/".concat(weaponName).concat(".png"), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false));
            opzShootGrid.add(weaponIV, 0, 1, 1, j - 1);

            opzShootPane.getChildren().add(opzShootGrid);
            pane.getChildren().add(opzShootPane);
        });
    }

    private void usePowerUp(GridPane gridButtons, StackPane pane){
        Button powerUps = new Button("Use PowerUps");
        gridButtons.add(powerUps, 5, 4);
        GridPane.setHalignment(powerUps, HPos.CENTER);
        GridPane.setValignment(powerUps, VPos.CENTER);
        Button backButton = new Button("BACK");
        powerUps.setOnAction(e -> {
            GridPane powerUpGrid = new GridPane();
            Rectangle rectangle = new Rectangle();
            gameGUI.rectangleStandard(rectangle, pane);

            int j = 0;
            if (!gui.getYouRepresentation().get(YOU_POWERUP).equals("")) {
                for (String powerups : gui.getYouRepresentation().get(YOU_POWERUP).split("'")) {
                    String realPowerUp = gameGUI.powerUpSwitch(powerups);
                    ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                    powerUpGrid.add(powerUp, j, 0);
                    final int pu = j;
                    powerUp.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, ev -> {
                        pane.getChildren().remove(powerUpGrid);
                        pane.getChildren().remove(rectangle);
                        pane.getChildren().remove(backButton);
                        client.send(("GMC-UPU-") + (pu));
                        gameGUI.typeOfFire = "upu";
                        gameGUI.handPosition = Integer.toString(pu);
                    });
                    j++;
                }
            }
            if (j == 0) {
                j = 1;
            }
            powerUpGrid.add(backButton, 0, 1, j, 1);

            backButton.setOnAction(ev -> {
                pane.getChildren().remove(powerUpGrid);
                pane.getChildren().remove(rectangle);
            });

            pane.getChildren().add(rectangle);
            pane.getChildren().add(powerUpGrid);
            powerUpGrid.setHgap(40);
            powerUpGrid.setVgap(50);
            GridPane.setHalignment(backButton, HPos.CENTER);
            GridPane.setValignment(backButton, VPos.CENTER);
            powerUpGrid.setAlignment(Pos.CENTER);
        });
    }

    private void quitButton(GridPane gridButtons, StackPane pane){
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
            gameGUI.labelSetting(text,"#ffffff",0.8,"-fx-font: 60 Helvetica;");
            Button quit1 = new Button("QUIT");
            Button back = new Button("BACK");
            gameGUI.rectangleStandard(rectangle, pane);
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
            quit1.setOnAction(ev -> client.send("quit"));
        });
    }

    private void infoEnemy(GridPane gridButtons, StackPane pane, ArrayList<String> enemyPlayer, int rowIndex){
        Button infoButton = new Button("INFO");
        gridButtons.add(infoButton, 6, rowIndex);
        GridPane.setHalignment(infoButton, HPos.CENTER);
        GridPane.setValignment(infoButton, VPos.BOTTOM);
        infoButton.setOnAction(e -> {
            GridPane gridInfo = new GridPane();
            Rectangle rectangle = new Rectangle();
            gameGUI.rectangleStandard(rectangle, pane);

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
            gameGUI.labelSetting(numberPowerUp,"#ffffff",0.8,"-fx-font: 40 Helvetica;");
            gridInfo.add(new ImageView(new Image("/images/game/powerUps/powerUpBack.png", pane.getWidth() / 10, pane.getHeight() / 5, false, false)), j, 0);
            gridInfo.add(numberPowerUp, j, 0);
            GridPane.setHalignment(numberPowerUp, HPos.CENTER);
            GridPane.setValignment(numberPowerUp, VPos.BOTTOM);

            //ammos
            j++;
            for (String ammo : enemyPlayer.get(PLAYER_AMMO).split("'")) {
                ammoShow(pane, gridInfo, j, ammo);
            }

            //back button
            infoBackButton(pane, gridInfo, rectangle, j);

            //grid
            gridInfo.setHgap(40);
            gridInfo.setVgap(50);
            gridInfo.setAlignment(Pos.CENTER);

            pane.getChildren().add(rectangle);
            pane.getChildren().add(gridInfo);
        });
    }

    private void infoMyself(GridPane gridButtons, StackPane pane){
        Button infoButton = new Button("INFO");
        gridButtons.add(infoButton, 3, 4);
        GridPane.setHalignment(infoButton, HPos.CENTER);
        GridPane.setValignment(infoButton, VPos.BOTTOM);

        infoButton.setOnAction(e -> {
            GridPane gridInfo = new GridPane();
            Rectangle rectangle = new Rectangle();
            gameGUI.rectangleStandard(rectangle, pane);

            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            double dimension = 100 / 3;
            row.setPercentHeight(dimension);
            gridInfo.getRowConstraints().add(row);

            //weapons
            int j = 0;
            for (String weapon : gui.getYouRepresentation().get(YOU_WEAPON).split("'")) {
                double notLoaded = 1;
                if (!weapon.equals("")) {
                    String[] playerWeapon = weapon.split(":");
                    String weaponName = playerWeapon[0].toLowerCase().replace(" ", "").concat(".png");

                    if (playerWeapon[1].equals("false")) {
                        notLoaded = 1.2;
                    }
                    gridInfo.add(new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / (N_COLUMN * notLoaded), pane.getHeight() / (NUMBER_OF_WEAPON * notLoaded), false, false)), j, 0);

                    if(notLoaded==1.2) {
                        Label unload = new Label("Unload");
                        gameGUI.labelSetting(unload,"#ffffff",0.8,"-fx-font: 20 Helvetica;");
                        gridInfo.add(unload, j, 0);
                        GridPane.setHalignment(unload,HPos.CENTER);
                        GridPane.setValignment(unload,VPos.TOP);
                    }
                }
                j++;
            }

            //power ups
            if (!gui.getYouRepresentation().get(YOU_POWERUP).equals("")) {
                for (String powerups : gui.getYouRepresentation().get(YOU_POWERUP).split("'")) {
                    String realPowerUp = gameGUI.powerUpSwitch(powerups);
                    ImageView powerUp = new ImageView(new Image("images/game/powerUps/".concat(realPowerUp).concat(".png"), pane.getWidth() / 10, pane.getHeight() / 5, false, false));
                    gridInfo.add(powerUp, j, 0);
                    j++;
                }
            }
            //ammos
            for (String ammo : gui.getYouRepresentation().get(PLAYER_AMMO).split("'")) {
                ammoShow(pane, gridInfo, j, ammo);
            }

            infoBackButton(pane, gridInfo, rectangle, j);

            //grid
            gridInfo.setHgap(40);
            gridInfo.setVgap(50);
            gridInfo.setAlignment(Pos.CENTER);

            pane.getChildren().add(rectangle);
            pane.getChildren().add(gridInfo);
        });
    }

    private void infoBackButton(StackPane pane, GridPane gridInfo, Rectangle rectangle, int j) {
        Button backButton = new Button("BACK");
        gridInfo.add(backButton, 0, 1, j + 2, 1);
        GridPane.setHalignment(backButton, HPos.CENTER);
        GridPane.setValignment(backButton, VPos.CENTER);
        backButton.setOnAction(ev -> {
            pane.getChildren().remove(gridInfo);
            pane.getChildren().remove(rectangle);
        });
    }

    private void ammoShow(StackPane pane, GridPane gridInfo, int j, String ammo) {
        String[] ammoQuantity = ammo.split(":");
        switch (ammoQuantity[0]) {
            case "R": {
                ImageView redAmmoIV = new ImageView(new Image("/images/game/ammo/redAmmo.png", pane.getWidth() / 7 / 3, pane.getHeight() / 5 / 3, false, false));
                gridInfo.add(redAmmoIV, j, 0);
                GridPane.setHalignment(redAmmoIV, HPos.LEFT);
                GridPane.setValignment(redAmmoIV, VPos.TOP);
                Label numberAmmo = new Label("x".concat(ammoQuantity[1]));
                gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
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
                gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
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
                gameGUI.labelSetting(numberAmmo, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
                gridInfo.add(numberAmmo, j + 1, 0);
                GridPane.setHalignment(numberAmmo, HPos.RIGHT);
                GridPane.setValignment(numberAmmo, VPos.BOTTOM);
                break;
            }
            default:
        }
    }

    void storeButtons(GridPane gridButtons, StackPane pane, Button stopClick, Button startClick) {
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
                        stopClick.fire();
                        GridPane grid4 = new GridPane();
                        Rectangle rectangle = new Rectangle();
                        gameGUI.rectangleStandard(rectangle, pane);
                        storeWeapon(pane, cell, grid4);

                        grid4.setHgap(40);
                        grid4.setVgap(50);
                        Button backButton = new Button("BACK");
                        grid4.add(backButton, 1, 1);
                        GridPane.setHalignment(backButton, HPos.CENTER);
                        GridPane.setValignment(backButton, VPos.CENTER);
                        grid4.setAlignment(Pos.CENTER);
                        backButton.setOnAction(ev -> {
                            startClick.fire();
                            pane.getChildren().remove(grid4);
                            pane.getChildren().remove(rectangle);
                        });
                        pane.getChildren().add(rectangle);
                        pane.getChildren().add(grid4);
                    });
                }
            }
        }
    }

    void storeWeapon(StackPane pane, ArrayList<String> cell, GridPane grid4) {
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
    }

    void shootFrenzy(Stage stage){
        StackPane pane = (StackPane) stage.getScene().getRoot();
        StackPane frenzyPane = new StackPane();
        GridPane gridWeapons = new GridPane();
        Rectangle rectangle = new Rectangle();
        gameGUI.rectangleStandard(rectangle,pane);


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
                        pane.getChildren().remove(frenzyPane);
                        gameGUI.nameWeapon = weaponName;
                        gameGUI.handPosition = Integer.toString(wpn);
                    });
                }
            }
            j++;
        }

        gridWeapons.setHgap(40);
        gridWeapons.setVgap(50);
        gridWeapons.setAlignment(Pos.CENTER);
        frenzyPane.getChildren().add(rectangle);
        frenzyPane.getChildren().add(gridWeapons);
        pane.getChildren().add(frenzyPane);
    }
}
