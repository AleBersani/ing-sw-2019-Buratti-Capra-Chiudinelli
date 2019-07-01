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
    private GUI gui;
    private Client client;
    GridPane boardGrid;
    boolean optionalShoot = false;
    boolean endTurn = false;
    String handPosition;
    String typeOfFire;
    private String powerUpPay;
    String nameWeapon;
    private ButtonsGUI buttonsGUI;

    static final String PURPLE = "purple";
    static final String BLUE = "blue";
    static final String GREEN = "green";
    static final String YELLOW = "yellow";
    static final String GREY = "grey";
    private static final int SHOOT_ADRENALINE = 6;
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

    public void setButtonsGUI(ButtonsGUI buttonsGUI) {
        this.buttonsGUI = buttonsGUI;
    }

    public void spawn(Stage stage, String msg) {
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
                        String[] weapon = cell.get(CELL_INSIDE).split("'");
                        for (int j = 0; j < NUMBER_OF_WEAPON; j++) {
                            if (j < weapon.length) {
                                String weaponName = weapon[j].toLowerCase();
                                weaponName = weaponName.replace(" ", "").concat(".png");
                                grid4.add(new ImageView(new Image("/images/game/weapons/".concat(weaponName), pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), j, 0);
                            } else {
                                grid4.add(new ImageView(new Image("/images/game/weapons/weaponBack.png", pane.getWidth() / N_COLUMN, pane.getHeight() / NUMBER_OF_WEAPON, false, false)), j, 0);
                            }
                        }

                        Button backButton = new Button("BACK");
                        grid4.add(backButton, 1, 1);
                        Rectangle secondRectangle = new Rectangle();
                        rectangleStandard(secondRectangle,pane);
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

    protected void interuptPowerUpUses(Stage stage, String msg){
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
                buildTarget(stage,"Player;"," ");
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
                    label40Helvetica(numberAmmo, "#ffffff", 0.8);
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
                    label40Helvetica(numberAmmo, "#ffffff", 0.8);
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
                    label40Helvetica(numberAmmo, "#ffffff", 0.8);
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
            client.send(toSend[0]);
            pane.getChildren().remove(pane2);
        });
        GridPane.setHalignment(done, HPos.CENTER);
        GridPane.setValignment(done, VPos.CENTER);

        reloadGrid.add(ignore, 0, 3,i+2,1);
        ignore.setOnAction(e -> client.send("RLD-"));
        GridPane.setHalignment(ignore, HPos.CENTER);
        GridPane.setValignment(ignore, VPos.CENTER);

        Label infoText = new Label("You can reload more than one Weapon at time");
        label40Helvetica(infoText, "#ffffff", 0.8);
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
            String fireType = "";
            final String[] targetString = {"TRG-"};
            if(typeOfFire.equals("interupt")){
                targetString[0] = "RPU-".concat(handPosition).concat("'");
                fireType = " ";
            }
            else {
                if (typeOfFire.equals("upu")) {
                    //caso powerup
                    targetString[0] = targetString[0].concat("POU-").concat(handPosition).concat("'");
                    fireType = " ";
                } else {
                    //caso armi
                    targetString[0] = targetString[0].concat("WPN-").concat(handPosition).concat("'").concat(movement).concat("'");
                    fireType = typeOfFire;
                }
            }
            final String[] target = {" ", " ", " ", " "};

            Button clickstop = new Button();
            Button startClick = new Button();

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
                            for (int z = 0; z < target.length; z++) {
                                if(!target[z].equals(" ")){
                                    exist = "true";
                                }
                            }
                            targetString[0] = targetString[0].concat(target[0]).concat(",").concat(target[1]).concat(",").concat(target[2]).concat(",").concat(target[3]).concat(",").concat(finalFireType).concat(",").concat(exist).concat(";");
                            for (int z = 0; z < target.length; z++) {
                                target[z] = " ";
                            }
                            if (j[0] >= targetParameters.size()) {
                                client.send(targetString[0]);
                                gui.reShow();
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
            targetPane.getChildren().add(boardGrid);
            this.buttonsGUI.storeButtons(targetGrid, targetPane, clickstop, startClick);
            targetPane.getChildren().add(targetGrid);

            pane.getChildren().add(targetPane);
        }
        else{
            client.send("TRG-WPN-".concat(handPosition).concat("'").concat(movement).concat("' , , , ,").concat(typeOfFire).concat(",").concat("true").concat(";"));
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

            Button clickStop = new Button();
            Button clickStart = new Button();
            clickStart.setOnAction(e-> movementPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));
            clickStop.setOnAction(e-> movementPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent));

            movementPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
            movementPane.getChildren().add(rectangle);
            movementPane.getChildren().add(boardGrid);
            this.buttonsGUI.storeButtons(movementGrid,movementPane,clickStop,clickStart);
            movementPane.getChildren().add(movementGrid);
            pane.getChildren().add(movementPane);
        } else {
            buildTarget(stage, msg, " ");
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
        label40Helvetica(text, "#ffffff", 0.8);

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
                    unload.setTextFill(Color.web("#ffffff", 0.8));
                    unload.setStyle("-fx-font: 20 Helvetica;");
                    unload.setEffect(new DropShadow());
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

    public void suspended(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        StackPane pane2 = new StackPane();

        GridPane grid = new GridPane();

        Rectangle rectangle = new Rectangle();
        rectangleStandard(rectangle,pane);

        Label text = new Label("YOU ARE SUSPENDED");
        text.setTextFill(Color.web("#ffffff", 0.8));
        text.setStyle("-fx-font: 50 Helvetica;");
        text.setEffect(new DropShadow());
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
        winner.setTextFill(Color.web("#ffffff", 0.8));
        winner.setStyle("-fx-font: 50 Helvetica;");
        winner.setEffect(new DropShadow());
        winnerGrid.add(winner,0,1);
        GridPane.setHalignment(winner,HPos.CENTER);
        GridPane.setValignment(winner,VPos.CENTER);

        Label points = new Label();
        points.setTextFill(Color.web("#ffffff", 0.8));
        points.setStyle("-fx-font: 40 Helvetica;");
        points.setEffect(new DropShadow());
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

    void label40Helvetica(Label label, String color, double opacity){
        label.setTextFill(Color.web(color, opacity));
        label.setStyle("-fx-font: 40 Helvetica;");
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