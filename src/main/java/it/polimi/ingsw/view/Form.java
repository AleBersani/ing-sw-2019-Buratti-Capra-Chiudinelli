package it.polimi.ingsw.view;

import it.polimi.ingsw.communication.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Form extends Application {

    private Client client;
    private Stage stage;

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Client client = new Client(this);
        client.init();
        client.start();
        this.stage = primaryStage;
        Image image = new Image("/images/loginForm.jpg");
        ImageView mv = new ImageView(image);
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane,1500,1000);
        Button button = new Button();
        Button button2 = new Button();
        TextField username = new TextField();
        GridPane grid = new GridPane();
        GridPane.setHalignment(button, HPos.CENTER);
        Rectangle rectangle = new Rectangle(500,400);
        Label text = new Label();
        text.setTextFill(Color.web("#FFD938", 0.8));
        text.setStyle("-fx-font: 70 Helvetica;");
        text.setEffect(new DropShadow());
        Label infoText = new Label();
        infoText.setStyle("-fx-font: 15 Helvetica;");
        infoText.setEffect(new DropShadow());

        //stage
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("/images/adrenalineLogo.png"));

        //image
        mv.fitWidthProperty().bind(pane.widthProperty());
        mv.fitHeightProperty().bind(pane.heightProperty());

        //grid
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(10);
        grid.getColumnConstraints().addAll(column1);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(10);
        grid.getColumnConstraints().addAll(column2);
        grid.add(text,0,0,2,1);
        grid.addRow(1, new Text(""));
        grid.add(username,0,2,2,1);
        grid.addRow(3, new Text(""));
        grid.add(button,0,4);
        grid.add(button2,1,4);
        grid.add(infoText,0,5,2,1);
        grid.setAlignment(Pos.CENTER);

        //text
        GridPane.setHalignment(text, HPos.CENTER);
        text.setAlignment(Pos.CENTER);
        text.setText("Login");
        text.prefWidthProperty().bind(pane.widthProperty().divide(5));

        //info text
        GridPane.setHalignment(infoText, HPos.CENTER);
        infoText.setAlignment(Pos.CENTER);
        infoText.prefWidthProperty().bind(pane.widthProperty().divide(7));
        infoText.prefHeightProperty().bind(pane.heightProperty().divide(7));

        //username
        username.setPromptText("Username");
        username.prefWidthProperty().bind(pane.widthProperty().divide(20));
        username.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //button
        GridPane.setHalignment(button, HPos.CENTER);
        button.setAlignment(Pos.CENTER);
        button.setText("LOGIN");
        button.prefWidthProperty().bind(pane.widthProperty().divide(15));
        button.prefHeightProperty().bind(pane.heightProperty().divide(22));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.send("login " + username.getText());
                if(username.getText().equals("prova")) {
                    infoText.setTextFill(Color.web("#66ff66", 0.8));
                    infoText.setText("Successfully logged");
                }
                else {
                    infoText.setTextFill(Color.web("#ff0000", 0.8));
                    infoText.setText("Username already in use");
                }
            }
        });

        //button2
        GridPane.setHalignment(button2, HPos.CENTER);
        button2.setAlignment(Pos.CENTER);
        button2.setText("EXIT");
        button2.prefWidthProperty().bind(pane.widthProperty().divide(15));
        button2.prefHeightProperty().bind(pane.heightProperty().divide(22));


        //rectangle
        rectangle.setFill(Color.rgb(0, 0, 0, 0.5));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty().divide(2));

        pane.getChildren().add(mv);
        pane.getChildren().add(rectangle);
        pane.getChildren().add(grid);

        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public void stopView(){
        Platform.exit();
    }
}
