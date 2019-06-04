package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import it.polimi.ingsw.communication.client.MessageHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LoginGUI {
    private GUI gui;

    public LoginGUI(GUI gui) {
        this.gui = gui;
    }

    public void loginImageSetting(Stage stage){
        Image image = new Image("/images/login/loginForm.jpg");
        ImageView mv = new ImageView(image);
        Rectangle rectangle = new Rectangle(500,400);
        StackPane pane = (StackPane) stage.getScene().getRoot();
        stage.getScene().setRoot(pane);

        //image
        mv.fitWidthProperty().bind(pane.widthProperty());
        mv.fitHeightProperty().bind(pane.heightProperty());

        //rectangle
        rectangle.setFill(Color.rgb(0, 0, 0, 0.8));
        rectangle.setEffect(new BoxBlur());
        rectangle.widthProperty().bind(pane.widthProperty());
        rectangle.heightProperty().bind(pane.heightProperty().divide(2));

        //pane
        pane.getChildren().add(mv);
        pane.getChildren().add(rectangle);

        //stage
        stage.setScene(stage.getScene());
    }

    public void loginGridSetting(Stage stage, Client client, MessageHandler messageHandler){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        Button button = new Button();
        Button button2 = new Button();
        Button fullScreen = new Button("FS");
        TextField username = new TextField();
        GridPane grid = new GridPane();
        Label text = new Label();
        text.setTextFill(Color.web("#FFD938", 0.8));
        text.setStyle("-fx-font: 70 Helvetica;");
        text.setEffect(new DropShadow());
        Label infoText = new Label();
        infoText.setStyle("-fx-font: 25 Helvetica;");
        infoText.setEffect(new DropShadow());

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
        infoText.prefHeightProperty().bind(pane.heightProperty().divide(7));
        if(this.gui.isMessageToShow()){
           infoText.setTextFill(Color.web("#ff0000",0.8));
           infoText.setText(messageHandler.getToShow().substring(4));
        }


        //username
        username.setPromptText("Username");
        username.prefWidthProperty().bind(pane.widthProperty().divide(20));
        username.prefHeightProperty().bind(pane.heightProperty().divide(20));
        username.setTooltip(new Tooltip("You can't use a Nickname with - or quit"));

        //button
        GridPane.setHalignment(button, HPos.CENTER);
        button.setAlignment(Pos.CENTER);
        button.setText("LOGIN");
        button.prefWidthProperty().bind(pane.widthProperty().divide(15));
        button.prefHeightProperty().bind(pane.heightProperty().divide(22));
        button.setOnAction(e -> {
            if(username.getText().equals("quit")||username.getText().equals("")||username.getText().contains("-")){
                infoText.setTextFill(Color.web("#ff0000",0.8));
                infoText.setText("Invalid Nickname");
            }
            else{
                messageHandler.setToSend(username.getText());
                client.setWaiting(false);
            }
        });

        //exit
        GridPane.setHalignment(button2, HPos.CENTER);
        button2.setAlignment(Pos.CENTER);
        button2.setText("EXIT");
        button2.prefWidthProperty().bind(pane.widthProperty().divide(15));
        button2.prefHeightProperty().bind(pane.heightProperty().divide(22));
        button2.setOnAction(e -> {
            messageHandler.setToSend("quit");
            client.setWaiting(false);
        });

        //button full screen
        StackPane.setAlignment(fullScreen, Pos.TOP_RIGHT);
        fullScreen.setAlignment(Pos.CENTER);
        fullScreen.prefWidthProperty().bind(pane.widthProperty().divide(25));
        fullScreen.prefHeightProperty().bind(pane.heightProperty().divide(25));
        fullScreen.setOnAction(e -> {
            if(!stage.isFullScreen()){
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            }
            else
                stage.setFullScreen(false);
        });

        pane.getChildren().add(grid);
        pane.getChildren().add(fullScreen);
    }

    public void menuGridSetting(Stage stage, Client client, MessageHandler messageHandler){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        GridPane grid = new GridPane();
        Button doneButton = new Button("DONE");
        Button defaultButton = new Button("DEFAULT");
        Button fullScreen = new Button("FS");

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

        //TODO automatizzare le opzioni di getItem

        //choice box 2
        ChoiceBox<String> title2 = new ChoiceBox<>();
        title2.getItems().addAll("1","2","3","4");
        title2.getSelectionModel().selectFirst();
        title2.setTooltip(new Tooltip("Select a board"));

        //choice box 3
        ChoiceBox<String> title3 = new ChoiceBox<>();
        title3.getItems().addAll("8","5");
        title3.getSelectionModel().selectFirst();
        title3.setTooltip(new Tooltip("Select the number of skulls"));

        //choice box 4
        ChoiceBox<String> title4 = new ChoiceBox<>();
        title4.getItems().addAll("Yes","No");
        title4.getSelectionModel().selectFirst();
        title4.setTooltip(new Tooltip("Select if you want to enable frenzy"));

        //done Button
        doneButton.setOnAction(e -> {
            messageHandler.slowSendAdd(title2.getValue());
            messageHandler.slowSendAdd(title3.getValue());
            if(title4.getValue().equals("Yes")){
                messageHandler.slowSendAdd("Y");
            }
            else{
                messageHandler.slowSendAdd("N");
            }
            client.setWaiting(false);
            client.setGo(true);
        });
        doneButton.setTooltip(new Tooltip("Press if you want to play with this settings"));

        //default Button
        defaultButton.setOnAction(e -> {
            title2.getSelectionModel().selectFirst();
            title3.getSelectionModel().selectFirst();
            title4.getSelectionModel().selectFirst();
        });
        defaultButton.setTooltip(new Tooltip("Press if you want to return to default settings"));

        //grid
        grid.add(title,0,0,5,1);
        grid.addRow(1,new Text(""));
        grid.add(infoMenu,0,2);
        grid.add(infoMenu2,2,2);
        grid.add(infoMenu3,4,2);
        grid.addRow(3,new Text(""));
        grid.add(title2,0,4);
        grid.add(doneButton,1,6);
        grid.add(title3,2,4);
        grid.add(defaultButton,3,6);
        grid.add(title4,4,4);
        grid.addRow(5,new Text("\n\n"));
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

        //doneButton
        GridPane.setHalignment(doneButton, HPos.CENTER);
        doneButton.setAlignment(Pos.CENTER);
        doneButton.prefWidthProperty().bind(pane.widthProperty().divide(10));
        doneButton.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //exitButton
        GridPane.setHalignment(defaultButton, HPos.CENTER);
        defaultButton.setAlignment(Pos.CENTER);
        defaultButton.prefWidthProperty().bind(pane.widthProperty().divide(10));
        defaultButton.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //button full screen
        StackPane.setAlignment(fullScreen, Pos.TOP_RIGHT);
        fullScreen.setAlignment(Pos.CENTER);
        fullScreen.prefWidthProperty().bind(pane.widthProperty().divide(25));
        fullScreen.prefHeightProperty().bind(pane.heightProperty().divide(25));
        fullScreen.setOnAction(e -> {
            if(!stage.isFullScreen()){
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            }
            else
                stage.setFullScreen(false);
        });

        pane.getChildren().add(grid);
        pane.getChildren().add(fullScreen);
    }

    public void roomGridSetting(Stage stage, Client client, MessageHandler messageHandler){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        GridPane grid = new GridPane();
        Button fullScreen = new Button("FS");

        //title label
        Label title = new Label();
        title.setTextFill(Color.web("#FFD938", 0.8));
        title.setStyle("-fx-font: 50 Helvetica;");
        title.setEffect(new DropShadow());

        //title nickname player 1
        Label title1 = new Label();
        title1.setTextFill(Color.web("#FFD938", 0.8));
        title1.setStyle("-fx-font: 20 Helvetica;");
        title1.setEffect(new DropShadow());

        //title nickname player 2
        Label title2 = new Label();
        title2.setTextFill(Color.web("#FFD938", 0.8));
        title2.setStyle("-fx-font: 20 Helvetica;");
        title2.setEffect(new DropShadow());

        //title nickname player 3
        Label title3 = new Label();
        title3.setTextFill(Color.web("#FFD938", 0.8));
        title3.setStyle("-fx-font: 20 Helvetica;");
        title3.setEffect(new DropShadow());

        //title nickname player 4
        Label title4 = new Label();
        title4.setTextFill(Color.web("#FFD938", 0.8));
        title4.setStyle("-fx-font: 20 Helvetica;");
        title4.setEffect(new DropShadow());

        //title nickname player 5
        Label title5 = new Label();
        title5.setTextFill(Color.web("#FFD938", 0.8));
        title5.setStyle("-fx-font: 20 Helvetica;");
        title5.setEffect(new DropShadow());

        //exit button
        Button buttonExit = new Button("EXIT");
        buttonExit.setOnAction(e -> {
            messageHandler.setToSend("quit");
            client.setWaiting(false);
        });

        //grid
        grid.add(title,0,0);
        grid.addRow(1,new Text (""));
        grid.add(title1,0,2);
        grid.add(title2,0,3);
        grid.add(title3,0,4);
        grid.add(title4,0,5);
        grid.add(title5,0,6);
        grid.addRow(7,new Text (""));
        grid.add(buttonExit,0,8);
        grid.setAlignment(Pos.CENTER);

        //List of player
        ArrayList<String> playerList = new ArrayList<>();
        if(messageHandler.getBigReceive()!=null){
            for(String s: messageHandler.getBigReceive()){
                playerList.add(s);
            }
        }
        while(playerList.size()<5){
            playerList.add("");
        }

        //title
        GridPane.setHalignment(title, HPos.CENTER);
        title.setAlignment(Pos.CENTER);
        title.setText("Player");
        title.prefWidthProperty().bind(pane.widthProperty().divide(2));

        //title1
        GridPane.setHalignment(title1, HPos.CENTER);
        title1.setAlignment(Pos.CENTER);
        title1.setText(playerList.get(0));
        title1.prefWidthProperty().bind(pane.widthProperty().divide(10));
        title1.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //title2
        GridPane.setHalignment(title2, HPos.CENTER);
        title2.setAlignment(Pos.CENTER);
        title2.setText(playerList.get(1));
        title2.prefWidthProperty().bind(pane.widthProperty().divide(10));
        title2.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //title3
        GridPane.setHalignment(title3, HPos.CENTER);
        title3.setAlignment(Pos.CENTER);
        title3.setText(playerList.get(2));
        title3.prefWidthProperty().bind(pane.widthProperty().divide(10));
        title3.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //title4
        GridPane.setHalignment(title4, HPos.CENTER);
        title4.setAlignment(Pos.CENTER);
        title4.setText(playerList.get(3));
        title4.prefWidthProperty().bind(pane.widthProperty().divide(10));
        title4.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //title5
        GridPane.setHalignment(title5, HPos.CENTER);
        title5.setAlignment(Pos.CENTER);
        title5.setText(playerList.get(4));
        title5.prefWidthProperty().bind(pane.widthProperty().divide(10));
        title5.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //button
        GridPane.setHalignment(buttonExit, HPos.CENTER);
        buttonExit.setAlignment(Pos.CENTER);
        buttonExit.prefWidthProperty().bind(pane.widthProperty().divide(10));
        buttonExit.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //button full screen
        StackPane.setAlignment(fullScreen, Pos.TOP_RIGHT);
        fullScreen.setAlignment(Pos.CENTER);
        fullScreen.prefWidthProperty().bind(pane.widthProperty().divide(25));
        fullScreen.prefHeightProperty().bind(pane.heightProperty().divide(25));
        fullScreen.setOnAction(e -> {
            if(!stage.isFullScreen()){
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            }
            else
                stage.setFullScreen(false);
        });

        pane.getChildren().add(grid);
        pane.getChildren().add(fullScreen);
    }
}
