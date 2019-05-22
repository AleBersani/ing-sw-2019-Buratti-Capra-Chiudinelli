package it.polimi.ingsw.view;

import it.polimi.ingsw.communication.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Form extends Application {

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Client client = new Client(this);
        client.init();
        client.start();
        Stage stage = primaryStage;
        Image image = new Image("/images/loginForm.jpg");
        ImageView mv = new ImageView(image);
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane,1200,800);
        Button button = new Button();
        TextField username = new TextField();
        GridPane grid = new GridPane();
        GridPane.setHalignment(button, HPos.CENTER);
        Rectangle rectangle = new Rectangle(500,400);
        Label text = new Label();
        text.setTextFill(Color.web("#FFD938", 0.8));
        text.setStyle("-fx-font: 70 helvetica;");

        //stage
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("/images/adrenalineLogo.png"));

        //image
        mv.fitWidthProperty().bind(pane.widthProperty());
        mv.fitHeightProperty().bind(pane.heightProperty());

        //grid
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(15);
        grid.getColumnConstraints().addAll(column1);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(15);
        grid.getRowConstraints().addAll(row1);
        grid.add(text,0,0);
        grid.add(username,0,1);
        grid.add(button,0,2);
        grid.widthProperty().divide(2.4);
        grid.heightProperty().divide(2);
        grid.setAlignment(Pos.CENTER);

        //text
        text.setText("Login");
        text.prefWidthProperty().bind(pane.widthProperty().divide(7));
        text.prefHeightProperty().bind(pane.heightProperty().divide(7));
        text.setAlignment(Pos.CENTER);

        //username
        username.setPromptText("Username");
        username.prefWidthProperty().bind(pane.widthProperty().divide(20));
        username.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //button
        button.setText("Login");
        button.prefWidthProperty().bind(pane.widthProperty().divide(20));
        button.prefHeightProperty().bind(pane.heightProperty().divide(20));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.send("login " + username.getText());
            }
        });

        //rectangle
        rectangle.setFill(Color.rgb(0, 0, 0, 0.5));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty().divide(4));
        rectangle.heightProperty().bind(pane.heightProperty().divide(3));

        pane.getChildren().add(mv);
        pane.getChildren().add(rectangle);
        pane.getChildren().add(grid);

        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
