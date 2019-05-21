package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Form extends Application {

    public void start(Stage stage){
        Image image = new Image("file:src/main/resources/images/loginForm.jpg");
        ImageView mv = new ImageView(image);
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane,1200,800);
        Button button = new Button();
        TextField username = new TextField();
        GridPane grid = new GridPane();
        GridPane.setHalignment(button, HPos.CENTER);

        //stage
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("file:src/main/resources/images/adrenalineLogo.png"));

        //image
        mv.fitWidthProperty().bind(pane.widthProperty());
        mv.fitHeightProperty().bind(pane.heightProperty());

        //text field
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(15);
        grid.getColumnConstraints().addAll(column1);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(15);
        grid.getRowConstraints().addAll(row1);
        grid.add(username,0,0);
        username.setPromptText("Username");
        grid.add(button,0,2);
        grid.setAlignment(Pos.CENTER);

        //button
        button.setText("Login");

        pane.getChildren().add(mv);
        pane.getChildren().add(grid);

        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    public static void main(String [] args){
        launch(args);
    }
}
