package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
                gameGUI.drawSkullOnPlayer(pane);
            }
            if(!player.get(PLAYER_DAMAGE).equals("")){
                gameGUI.drawBloodOnPlayer(pane);
            }
            if(!player.get(PLAYER_MARK).equals("")){
                gameGUI.drawMarkOnPlayer(pane);
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
            gameGUI.drawSkullOnYou(pane);
        }
        if(!gui.getYouRepresentation().get(PLAYER_DAMAGE).equals("")){
            gameGUI.drawBloodOnYou(pane);
        }
        if(!gui.getYouRepresentation().get(PLAYER_MARK).equals("")){
            gameGUI.drawMarkOnYou(pane);
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
