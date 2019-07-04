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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

import static it.polimi.ingsw.view.gui.GameGUI.*;

class ShootingGUI {
    private GUI gui;
    private Client client;
    private GameGUI gameGUI;
    private ButtonsGUI buttonsGUI;

    ShootingGUI(GUI gui, Client client, GameGUI gameGUI, ButtonsGUI buttonsGUI) {
        this.gui = gui;
        this.client = client;
        this.gameGUI = gameGUI;
        this.buttonsGUI = buttonsGUI;
    }

    void buildTarget(Stage stage, String msg, String movement) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        StackPane targetPane = new StackPane();
        GridPane targetGrid = new GridPane();
        gameGUI.columnConstraint(targetGrid, N_COLUMN);
        gameGUI.rowConstraint(targetGrid, N_ROW);

        Rectangle rectangle = new Rectangle();
        gameGUI.rectangleStandard(rectangle, pane);

        Label text = new Label("");
        gameGUI.labelSetting(text, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
        targetGrid.add(text, 0, 3, 4, 1);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);

        ArrayList<ArrayList<String>> targetParameters = new ArrayList<>();
        ArrayList<boolean[]> success = new ArrayList<>();
        int i = 0;
        for (String targetParameter : msg.split(";")) {
            targetParameters.add(new ArrayList<>());
            success.add(new boolean[targetParameter.split(",").length]);
            int w=0;
            for (String singleTarget : targetParameter.split(",")) {
                targetParameters.get(i).add(singleTarget);
                success.get(i)[w] = false;
                w++;
            }
            i++;
        }
        if(!targetParameters.get(0).get(0).equals("Area")) {
            targetDescription(targetParameters.get(0).get(0), text);

            ImageView skipIV = new ImageView(new Image("/images/game/skip.png", pane.getWidth() / 17, pane.getHeight() / 10, false, false));
            targetGrid.add(skipIV, 4, 3);
            GridPane.setHalignment(skipIV, HPos.CENTER);
            GridPane.setValignment(skipIV, VPos.CENTER);

            final int[] j = {0};
            final int[] k = {0};
            String fireType;
            final String[] targetString = {"TRG-"};
            if(gameGUI.typeOfFire.equals("interrupt")){
                targetString[0] = "RPU-".concat(gameGUI.handPosition).concat("'");
                fireType = " ";
            }
            else {
                if (gameGUI.typeOfFire.equals("upu")) {
                    //caso powerup
                    targetString[0] = targetString[0].concat("POU-").concat(gameGUI.handPosition).concat("'");
                    fireType = " ";
                } else {
                    //caso armi
                    targetString[0] = targetString[0].concat("WPN-").concat(gameGUI.handPosition).concat("'").concat(movement).concat("'");
                    fireType = gameGUI.typeOfFire;
                }
            }
            final String[] target = {" ", " ", " ", " "};

            Button clickstop = new Button();
            Button startClick = new Button();

            String finalFireType = fireType;
            EventHandler<MouseEvent> clickEvent = event -> {
                if (j[0] >= targetParameters.size()) {
                    event.consume();
                } else {
                    double pixelX = event.getScreenX();
                    double pixelY = event.getScreenY();

                    String cellX = Integer.toString(1 + (int) (pixelX / (pane.getWidth() / N_COLUMN)));
                    String cellY = Integer.toString(1 + (int) (pixelY / (pane.getHeight() / N_ROW)));

                    String[] type = targetParameters.get(j[0]).get(k[0]).split(":");
                    if ((Integer.valueOf(cellX) == 5) && (Integer.valueOf(cellY) == 4)) {
                        success.get(j[0])[k[0]] = true;
                    } else {
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
                            String exist = "false";
                            for (String s : target) {
                                if (!s.equals(" ")) {
                                    exist = "true";
                                }
                            }
                            targetString[0] = targetString[0].concat(target[0]).concat(",").concat(target[1]).concat(",").concat(target[2]).concat(",").concat(target[3]).concat(",").concat(finalFireType).concat(",").concat(exist).concat(";");
                            for (int z = 0; z < target.length; z++) {
                                target[z] = " ";
                            }
                            if (j[0] >= targetParameters.size()) {
                                gui.reShow();
                                if(gameGUI.typeOfFire.equals("interrupt")){
                                    gameGUI.typeOfFire = "";
                                    gameGUI.specialPay(gui.afterPane,targetString[0]);
                                }
                                else {
                                    gameGUI.powerUpPay(gui.afterPane, targetString[0]);
                                }
                            }
                        }
                        if (j[0] < targetParameters.size()) {
                            targetDescription(targetParameters.get(j[0]).get(k[0]), text);
                        }
                    }
                }
            };

            startClick.setOnAction(ev -> pane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));
            clickstop.setOnAction(ev -> targetPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));
            targetPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
            targetPane.getChildren().add(rectangle);
            targetPane.getChildren().add(gameGUI.boardGrid);
            this.buttonsGUI.storeButtons(targetGrid, targetPane, clickstop, startClick);
            targetPane.getChildren().add(targetGrid);

            pane.getChildren().add(targetPane);
        }
        else{
            String messageToSend ="TRG-WPN-".concat(gameGUI.handPosition).concat("'").concat(movement).concat("' , , , ,").concat(gameGUI.typeOfFire).concat(",").concat("true").concat(";") ;
            gameGUI.powerUpPay(pane,messageToSend);
        }
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

    void preShootShow(Stage stage, String msg) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        StackPane preShootPane = new StackPane();
        GridPane preShootGrid = new GridPane();

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(20);
        preShootGrid.getRowConstraints().add(row);

        Rectangle rectangle = new Rectangle();
        gameGUI.rectangleStandard(rectangle, pane);

        Label text = new Label("Choose the type of fire");
        gameGUI.labelSetting(text, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
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
                gameGUI.typeOfFire = fire;
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

        String weaponName = gameGUI.nameWeapon;
        for (String trg : effect) {
            if (trg.startsWith("Optional-")) {
                weaponName = gameGUI.nameWeapon.concat("Info");
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
            gameGUI.rowConstraint(movementGrid, N_ROW);
            gameGUI.columnConstraint(movementGrid, N_COLUMN);

            Rectangle rectangle = new Rectangle();
            gameGUI.rectangleStandard(rectangle, pane);

            Label text = new Label("Where do you want to move?");
            gameGUI.labelSetting(text, "#ffffff", 0.8, "-fx-font: 40 Helvetica;");
            movementGrid.add(text, 0, 3, 4, 1);
            GridPane.setHalignment(text, HPos.CENTER);
            GridPane.setValignment(text, VPos.CENTER);

            EventHandler<MouseEvent> clickEvent = event -> {
                String cellX = Integer.toString(1 + (int) (event.getScreenX() / (pane.getWidth() / N_COLUMN)));
                String cellY = Integer.toString(1 + (int) (event.getScreenY() / (pane.getHeight() / N_ROW)));
                if ((Integer.valueOf(cellX) <= 4) && (Integer.valueOf(cellY) <= 3)) {
                    buildTarget(stage, msg, cellX.concat(":").concat(cellY));
                    pane.getChildren().remove(movementPane);
                }
            };

            Button clickStop = new Button();
            Button clickStart = new Button();
            clickStart.setOnAction(e-> movementPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));
            clickStop.setOnAction(e-> movementPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));

            movementPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
            movementPane.getChildren().add(rectangle);
            movementPane.getChildren().add(gameGUI.boardGrid);
            this.buttonsGUI.storeButtons(movementGrid,movementPane,clickStop,clickStart);
            movementPane.getChildren().add(movementGrid);
            pane.getChildren().add(movementPane);
        } else {
            buildTarget(stage, msg, " ");
        }
    }
}
