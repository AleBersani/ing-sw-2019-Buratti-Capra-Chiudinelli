package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BackGraphicsGUI {

    private GUI gui;
    private Client client;

    public BackGraphicsGUI(GUI gui, Client client) {
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


}
