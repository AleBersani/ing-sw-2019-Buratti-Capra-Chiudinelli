package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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

        //grid column constraint
        gameGUI.columnConstraint(grid, N_COLUMN);

        //grid row constraint
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
}
