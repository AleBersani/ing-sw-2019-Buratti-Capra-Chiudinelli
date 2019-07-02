package it.polimi.ingsw.view.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

import static it.polimi.ingsw.view.gui.GameGUI.*;

class BackGraphicsGUI {

    private GUI gui;
    private GameGUI gameGUI;

    BackGraphicsGUI(GUI gui, GameGUI gameGUI) {
        this.gui = gui;
        this.gameGUI = gameGUI;
    }

    void backGround(Stage stage) {
        //backGround image
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        ImageView screen = new ImageView(new Image("/images/game/metallicScreen.png"));
        screen.fitWidthProperty().bind(pane.widthProperty());
        screen.fitHeightProperty().bind(pane.heightProperty());
        pane.getChildren().add(screen);
    }

    void buildBoard(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        stage.setResizable(false);
        GridPane grid = new GridPane();

        gameGUI.columnConstraint(grid, N_COLUMN);
        gameGUI.rowConstraint(grid, N_ROW);

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
                            if (!difi[1].equals("0")) {
                                if (s.startsWith("Y:")) {
                                    ammoName = ammoName.concat("y").concat(difi[1]);
                                }
                                else {
                                    if (s.startsWith("R:")) {
                                        ammoName = ammoName.concat("r").concat(difi[1]);
                                    }
                                    else {
                                        if (s.startsWith("B:")) {
                                            ammoName = ammoName.concat("b").concat(difi[1]);
                                        }
                                        else {
                                            if (s.startsWith("PU:")) {
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

        //playersToken
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
        gameGUI.boardGrid = grid;
    }

    void buildPlayers(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        GridPane grid = new GridPane();

        gameGUI.columnConstraint(grid, N_COLUMN);
        gameGUI.rowConstraint(grid, N_ROW);

        int i = 0;
        for (ArrayList<String> player : gui.getPlayersRepresentation()) {
            String[] color = player.get(PLAYER_COLOR).split(":");
            String playerPlance = color[1];
            String[] turned = player.get(PLAYER_TURNED).split(":");
            String playerTurned = turned[1];
            String[] frenzy = player.get(PLAYER_FRENZY).split(":");
            String turnFrenzy = frenzy[1];

            plance(pane,grid,playerPlance,turnFrenzy,playerTurned,PLAYER_COL_SPAN,PLAYER_XPOS,i);

            i++;
        }
        pane.getChildren().add(grid);

        for (ArrayList<String> player : gui.getPlayersRepresentation()){
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
    }

    void buildYou(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        stage.setResizable(false);
        GridPane grid = new GridPane();

        gameGUI.columnConstraint(grid, N_COLUMN);
        gameGUI.rowConstraint(grid, N_ROW);

        String[] color = gui.getYouRepresentation().get(PLAYER_COLOR).split(":");
        String playerPlance = color[1];
        String[] turned = gui.getYouRepresentation().get(PLAYER_TURNED).split(":");
        String playerTurned = turned[1];
        String[] frenzy = gui.getYouRepresentation().get(PLAYER_FRENZY).split(":");
        String turnFrenzy = frenzy[1];

        plance(pane,grid,playerPlance,turnFrenzy,playerTurned,YOU_COL_SPAN,YOU_XPOS,YOU_YPOS);

        Label points = new Label(gui.getYouRepresentation().get(YOU_POINT));
        grid.add(points, 3, 3);
        points.setTextFill(Color.web("#ffffff", 0.8));
        points.setStyle("-fx-font: 30 Helvetica;");
        points.setEffect(new DropShadow());
        GridPane.setHalignment(points, HPos.CENTER);

        pane.getChildren().add(grid);

        if(!gui.getYouRepresentation().get(PLAYER_SKULL).equals("")){
            drawSkullOnYou(pane);
        }
        if(!gui.getYouRepresentation().get(PLAYER_DAMAGE).equals("")){
            drawBloodOnYou(pane);
        }
        if(!gui.getYouRepresentation().get(PLAYER_MARK).equals("")){
            drawMarkOnYou(pane);
        }
    }

    void buildKillShotTrack(Stage stage) {
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        stage.setResizable(false);
        GridPane grid = new GridPane();

        gameGUI.columnConstraint(grid, N_COLUMN);

        gameGUI.rowConstraint(grid, N_ROW);

        grid.add(new ImageView(new Image("/images/game/killshotTrack.png", pane.getWidth() / N_COLUMN * KILL_COL_SPAN, pane.getHeight() / N_COLUMN, false, false)), KILL_COL, KILL_ROW, KILL_COL_SPAN, KILL_ROW_SPAN);
        pane.getChildren().add(grid);

        Pane pane2 = new Pane();
        String[] damage = gui.getKillShotRepresentation().get(1).split("'");
        String[] doubleDamage = gui.getKillShotRepresentation().get(2).substring(1,gui.getKillShotRepresentation().get(2).length()-1).split(",");
        int totalSkull = Integer.parseInt(gui.getKillShotRepresentation().get(KILL_TOT_SKULL));
        int i;
        for (i = 0; i < totalSkull; i++) {
            String bloodString;
            if ((i < damage.length) && (!damage[i].equals(""))) {
                bloodString = damage[i].concat("Blood");
                doubleDamage[i] = doubleDamage[i].replace(" ","");
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
        for(;i<damage.length;i++){
            String bloodString;
            bloodString = damage[i].concat("Blood");
            doubleDamage[i] = doubleDamage[i].replace(" ","");
            if (doubleDamage[i].equals("true")) {
                bloodString = bloodString.concat("X2");
            }
            ImageView blood = new ImageView(new Image("/images/game/blood/".concat(bloodString).concat(".png"), pane.getWidth() / 30, pane.getHeight() / 15, false, false));
            blood.setX(pane.getWidth() / 2.9 + (i * pane.getWidth() / 60));
            blood.setY(pane.getHeight() / 1.5);
            pane2.getChildren().add(blood);
        }
        pane.getChildren().add(pane2);
    }

    private void drawBloodOnYou(StackPane pane) {
        String[] blood = gui.getYouRepresentation().get(PLAYER_DAMAGE).split("'");

        for(int i=0;i<blood.length;i++){
            if(!blood[i].equals("")) {
                ImageView bloodIV = new ImageView(new Image("/images/game/blood/".concat(blood[i]).concat("Blood.png"), pane.getWidth() / 25, pane.getHeight() / 15, false, false));
                String [] turned = gui.getYouRepresentation().get(PLAYER_TURNED).split(":");
                if(turned[1].equals("false")){
                    bloodIV.setTranslateX(-pane.getWidth()/2 + pane.getWidth() / 14.8837 + (i * pane.getWidth() / (2.8318*11)));
                    bloodIV.setTranslateY(-pane.getHeight()/2 + pane.getHeight() / 1.1106);
                }
                else{
                    bloodIV.setTranslateX(-pane.getWidth()/2 + pane.getWidth() / 13.9130 + (i * pane.getWidth() / (2.8893*11)));
                    bloodIV.setTranslateY(-pane.getHeight()/2 + pane.getHeight() / 1.1106);
                }
                pane.getChildren().add(bloodIV);
            }
        }
    }

    private void drawSkullOnYou(StackPane pane) {
        String[] skull = gui.getYouRepresentation().get(PLAYER_SKULL).split(":");

        for (int i = 0; i < Integer.valueOf(skull[1]); i++) {
            ImageView skullIV = new ImageView(new Image("/images/game/blood/redSkull.png", pane.getWidth() / 35, pane.getHeight() / 20, false, false));
            String [] turned = gui.getYouRepresentation().get(PLAYER_TURNED).split(":");
            if(turned[1].equals("false")) {
                skullIV.setTranslateX(-pane.getWidth() / 2 + pane.getWidth() / 7.4418 + (i * pane.getWidth() / (6.5641 * 5)));
                skullIV.setTranslateY(-pane.getHeight() / 2 + pane.getHeight() / 1.0312);
            }
            else{
                skullIV.setTranslateX(-pane.getWidth() / 2 + pane.getWidth() / 5.8986 + (i * pane.getWidth() / (10.9401 * 3)));
                skullIV.setTranslateY(-pane.getHeight() / 2 + pane.getHeight() / 1.0301);
            }
            pane.getChildren().add(skullIV);
        }
    }

    private void drawMarkOnYou(StackPane pane){
        String[] mark = gui.getYouRepresentation().get(PLAYER_MARK).split("'");

        for(int i=0;i<mark.length;i++){
            if(!mark[i].equals("")) {
                ImageView markIV = new ImageView(new Image("/images/game/blood/".concat(mark[i]).concat("Blood.png"), pane.getWidth() / 40, pane.getHeight() / 25, false, false));
                markIV.setTranslateX(-pane.getWidth()/2 + pane.getWidth() / 2.3572 - (i * pane.getWidth() / (6.7724*11)));
                markIV.setTranslateY(-pane.getHeight()/2 + pane.getHeight() / 1.2161);
                pane.getChildren().add(markIV);
            }
        }
    }

    private void drawBloodOnPlayer(StackPane pane){
        int j=0;
        for(ArrayList<String> player : gui.getPlayersRepresentation()) {
            String[] blood = player.get(PLAYER_DAMAGE).split("'");

            for(int i=0;i<blood.length;i++){
                if(!blood[i].equals("")) {
                    ImageView bloodIV = new ImageView(new Image("/images/game/blood/".concat(blood[i]).concat("Blood.png"), pane.getWidth() / 25, pane.getHeight() / 15, false, false));
                    String [] turned = player.get(PLAYER_TURNED).split(":");
                    if(turned[1].equals("false")) {
                        bloodIV.setTranslateX(-pane.getWidth() / 2 + pane.getWidth() / 1.6120 + (i * pane.getWidth() / (3.7647 * 11)));
                        bloodIV.setTranslateY(-pane.getHeight() / 2 + pane.getHeight() / 9.8461 + (j * pane.getHeight() / N_ROW));
                    }
                    else{
                        bloodIV.setTranslateX(-pane.getWidth() / 2 + pane.getWidth() / 1.6 + (i * pane.getWidth() / (3.8323 * 11)));
                        bloodIV.setTranslateY(-pane.getHeight() / 2 + pane.getHeight() / 9.8461 + (j * pane.getHeight() / N_ROW));
                    }
                    pane.getChildren().add(bloodIV);
                }
            }
            j++;
        }
    }

    private void drawSkullOnPlayer(StackPane pane){
        int j=0;
        for(ArrayList<String> player : gui.getPlayersRepresentation()){
            String[] skull = player.get(PLAYER_SKULL).split(":");

            for (int i = 0; i < Integer.valueOf(skull[1]); i++) {
                ImageView skullIV = new ImageView(new Image("/images/game/blood/redSkull.png", pane.getWidth() / 35, pane.getHeight() / 20, false, false));
                String [] turned = player.get(PLAYER_TURNED).split(":");
                if(turned[1].equals("false")) {
                    skullIV.setTranslateX(-pane.getWidth()/2 + pane.getWidth() / 1.4901 + (i * pane.getWidth() / (8.7671*5)));
                    skullIV.setTranslateY(-pane.getHeight()/2 + pane.getHeight() / 5.9534 + (j*pane.getHeight()/N_ROW));
                }
                else{
                    skullIV.setTranslateX(-pane.getWidth()/2 + pane.getWidth() / 1.4317 + (i * pane.getWidth() / (14.3820*3)));
                    skullIV.setTranslateY(-pane.getHeight()/2 + pane.getHeight() / 5.9190 + (j*pane.getHeight()/N_ROW));
                }
                pane.getChildren().add(skullIV);
            }
            j++;
        }
    }

    private void drawMarkOnPlayer(StackPane pane){
        int j=0;
        for(ArrayList<String> player : gui.getPlayersRepresentation()) {
            String[] mark = player.get(PLAYER_MARK).split("'");

            for(int i=0;i<mark.length;i++){
                if(!mark[i].equals("")) {
                    ImageView markIV = new ImageView(new Image("/images/game/blood/".concat(mark[i]).concat("Blood.png"), pane.getWidth() / 40, pane.getHeight() / 25, false, false));
                    markIV.setTranslateX(-pane.getWidth()/2 + pane.getWidth() / 1.1188 - (i * pane.getWidth() / (8.5906*11)));
                    markIV.setTranslateY(-pane.getHeight()/2 + pane.getHeight() / 42.6666 + (j * pane.getHeight() / N_ROW));
                    pane.getChildren().add(markIV);
                }
            }
            j++;
        }
    }

    private void plance(StackPane pane, GridPane grid,String playerPlance, String turnFrenzy, String playerTurned, int colSpan, int xpos, int ypos){
        String declaration = "/images/game/plance/";

        if ((playerPlance.equals(PURPLE)) || (playerPlance.equals(BLUE)) || (playerPlance.equals(GREEN)) || (playerPlance.equals(YELLOW)) || (playerPlance.equals(GREY))) {
            if (turnFrenzy.equals("false")) {
                grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("Player.png"), pane.getWidth() / N_COLUMN * colSpan, pane.getHeight() / N_ROW, false, false)), xpos, ypos, colSpan, PLAYER_ROW_SPAN);
            } else {
                if (playerTurned.equals("false")) {
                    grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("TurnedActionPlayer.png"), pane.getWidth() / N_COLUMN * colSpan, pane.getHeight() / N_ROW, false, false)), xpos, ypos, colSpan, PLAYER_ROW_SPAN);
                } else {
                    grid.add(new ImageView(new Image(declaration.concat(playerPlance).concat("TurnedPlayer.png"), pane.getWidth() / N_COLUMN * colSpan, pane.getHeight() / N_ROW, false, false)), xpos, ypos, colSpan, PLAYER_ROW_SPAN);
                }
            }
        }
    }
}
