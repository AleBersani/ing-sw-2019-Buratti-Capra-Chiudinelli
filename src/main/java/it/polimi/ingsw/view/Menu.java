package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Menu extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage stage = primaryStage;
        Image image = new Image("/images/loginForm.jpg");
        ImageView mv = new ImageView(image);
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane,1200,800);
        Rectangle rectangle = new Rectangle(500,400);
        GridPane grid = new GridPane();
        Button button = new Button("DONE");
        Button button2 = new Button("EXIT");

        //title label
        Label title = new Label();
        title.setTextFill(Color.web("#FFD938", 0.8));
        title.setStyle("-fx-font: 40 Helvetica;");
        title.setEffect(new DropShadow());

        //label info menu
        Label infoMenu = new Label("Board");
        infoMenu.setTextFill(Color.web("#FFD938", 0.8));
        infoMenu.setStyle("-fx-font: 30 Helvetica;");
        infoMenu.setEffect(new DropShadow());
        GridPane.setHalignment(infoMenu, HPos.CENTER);

        //label info menu 2
        Label infoMenu2 = new Label("Skulls");
        infoMenu2.setTextFill(Color.web("#FFD938", 0.8));
        infoMenu2.setStyle("-fx-font: 30 Helvetica;");
        infoMenu2.setEffect(new DropShadow());
        GridPane.setHalignment(infoMenu2, HPos.CENTER);

        //label info menu 3
        Label infoMenu3 = new Label("Frenzy");
        infoMenu3.setTextFill(Color.web("#FFD938", 0.8));
        infoMenu3.setStyle("-fx-font: 30 Helvetica;");
        infoMenu3.setEffect(new DropShadow());
        GridPane.setHalignment(infoMenu3, HPos.CENTER);

        //info text
        Label infoText = new Label();
        infoText.setStyle("-fx-font: 20 Helvetica;");
        infoText.prefWidthProperty().bind(pane.widthProperty().divide(10));
        infoText.prefHeightProperty().bind(pane.heightProperty().divide(10));
        infoText.setEffect(new DropShadow());

        //TODO AUTOMATE GETITEMS

        //choice box 2
        ChoiceBox title2 = new ChoiceBox();
        title2.getItems().addAll("1","2");
        title2.setTooltip(new Tooltip("Select a board"));

        //choice box 3
        ChoiceBox title3 = new ChoiceBox();
        title3.getItems().addAll("5","8");
        title3.setTooltip(new Tooltip("Select the number of skulls"));

        //choice box 4
        ChoiceBox title4 = new ChoiceBox();
        title4.getItems().addAll("Yes","No");
        title4.setTooltip(new Tooltip("Select if you want to enable frenzy"));

        //stage
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("/images/adrenalineLogo.png"));

        //image
        mv.fitWidthProperty().bind(pane.widthProperty());
        mv.fitHeightProperty().bind(pane.heightProperty());

        //grid
        grid.add(title,0,0,5,1);
        grid.addRow(1,new Text(""));
        grid.add(infoMenu,0,2);
        grid.add(infoMenu2,2,2);
        grid.add(infoMenu3,4,2);
        grid.addRow(3,new Text(""));
        grid.add(title2,0,4);
        grid.add(button,1,6);
        grid.add(title3,2,4);
        grid.add(button2,3,6);
        grid.add(title4,4,4);
        grid.addRow(5,new Text("\n\n"));
        grid.add(infoText,0,7,5,1);
        grid.setAlignment(Pos.CENTER);

        //title
        GridPane.setHalignment(title, HPos.CENTER);
        title.setAlignment(Pos.CENTER);
        title.setText("Choose the settings of the game");
        title.prefWidthProperty().bind(pane.widthProperty().divide(2));

        //title2
        GridPane.setHalignment(title2, HPos.CENTER);
        title2.prefWidthProperty().bind(pane.widthProperty().divide(10));
        title2.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //title3
        GridPane.setHalignment(title3, HPos.CENTER);
        title3.prefWidthProperty().bind(pane.widthProperty().divide(10));
        title3.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //title4
        GridPane.setHalignment(title4, HPos.CENTER);
        title4.prefWidthProperty().bind(pane.widthProperty().divide(10));
        title4.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //button
        GridPane.setHalignment(button, HPos.CENTER);
        button.setAlignment(Pos.CENTER);
        button.prefWidthProperty().bind(pane.widthProperty().divide(10));
        button.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //button2
        GridPane.setHalignment(button2, HPos.CENTER);
        button2.setAlignment(Pos.CENTER);
        button2.prefWidthProperty().bind(pane.widthProperty().divide(10));
        button2.prefHeightProperty().bind(pane.heightProperty().divide(20));

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

    public static void main(String[] args){
        Application.launch(args);
    }
}