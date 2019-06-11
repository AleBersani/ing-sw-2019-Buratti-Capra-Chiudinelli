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
import java.util.concurrent.CountDownLatch;

public class LoginGUI {
    private GUI gui;
    private MessageHandler messageHandler;
    private Client client;


    public LoginGUI(GUI gui, MessageHandler messageHandler, Client client) {
        this.gui = gui;
        this.messageHandler = messageHandler;
        this.client = client;
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

    public void loginGridSetting(Stage stage){
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
            if(gui.isSendable()){
                if (username.getText().equals("quit") || username.getText().equals("") || username.getText().contains("-")) {
                    infoText.setTextFill(Color.web("#ff0000", 0.8));
                    infoText.setText("Invalid Nickname");
                } else {
                    client.send("LOG-".concat(username.getText()));
                }
            }
            else {
                infoText.setTextFill(Color.web("#ff0000", 0.8));
                infoText.setText("Wait a moment, please");
            }
        });

        //exit
        GridPane.setHalignment(button2, HPos.CENTER);
        button2.setAlignment(Pos.CENTER);
        button2.setText("EXIT");
        button2.prefWidthProperty().bind(pane.widthProperty().divide(15));
        button2.prefHeightProperty().bind(pane.heightProperty().divide(22));
        button2.setOnAction(e -> {
            client.send("quit");
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

    public void menuGridSetting(Stage stage,ArrayList<String> array,String msg){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        GridPane grid = new GridPane();
        Button doneButton = new Button("DONE");
        Button quitButton = new Button("QUIT");
        Button fullScreen = new Button("FS");

        //title label
        Label title = new Label("Settings");
        title.setTextFill(Color.web("#FFD938", 0.8));
        title.setStyle("-fx-font: 40 Helvetica;");
        title.setEffect(new DropShadow());

        //label info menu
        Label infoMenu = new Label(msg);
        infoMenu.setTextFill(Color.web("#FFD938", 0.8));
        infoMenu.setStyle("-fx-font: 30 Helvetica;");
        infoMenu.setEffect(new DropShadow());
        GridPane.setHalignment(infoMenu, HPos.CENTER);

        //choice box
        ChoiceBox<String> choicebox = new ChoiceBox<>();
        for(int i=0 ;i<array.size();i++)
            choicebox.getItems().add(array.get(i));
        choicebox.getSelectionModel().selectFirst();

        //done Button
        doneButton.setOnAction(e ->{
            switch (msg){
                case "Board":{
                    client.send("SET-BRD-".concat(choicebox.getValue()));
                    break;
                }
                case "Skull":{
                    client.send("SET-SKL-".concat(choicebox.getValue()));
                    break;
                }
                case "Frenzy":{
                    client.send("SET-FRZ-".concat(choicebox.getValue()));
                    break;
                }
                default:
            }

        });

        //quit Button
        quitButton.setOnAction(e -> client.send("quit"));

        //grid
        grid.add(title,0,0,2,1);
        grid.add(infoMenu,0,1,2,1);
        grid.add(choicebox,1,2);
        grid.add(doneButton,0,3);
        grid.add(quitButton,1,3);

        grid.setAlignment(Pos.CENTER);
        //grid.setVGap(); //TODO SET A GAP
        //grid.setHGap();

        //title
        GridPane.setHalignment(title, HPos.CENTER);
        title.setAlignment(Pos.CENTER);
        title.prefWidthProperty().bind(pane.widthProperty().divide(2));

        //choicebox
        GridPane.setHalignment(choicebox, HPos.CENTER);
        choicebox.prefWidthProperty().bind(pane.widthProperty().divide(10));
        choicebox.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //doneButton
        GridPane.setHalignment(doneButton, HPos.CENTER);
        doneButton.setAlignment(Pos.CENTER);
        doneButton.prefWidthProperty().bind(pane.widthProperty().divide(10));
        doneButton.prefHeightProperty().bind(pane.heightProperty().divide(20));

        //quitButton
        GridPane.setHalignment(quitButton, HPos.CENTER);
        quitButton.setAlignment(Pos.CENTER);
        quitButton.prefWidthProperty().bind(pane.widthProperty().divide(10));
        quitButton.prefHeightProperty().bind(pane.heightProperty().divide(20));

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

    public void roomGridSetting(Stage stage){
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
            client.send("quit");
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
