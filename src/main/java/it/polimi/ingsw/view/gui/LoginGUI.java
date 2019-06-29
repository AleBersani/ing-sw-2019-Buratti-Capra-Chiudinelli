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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;

/**
 * class that contains all needed to show the Login section of the game
 */
class LoginGUI {
    /**
     * reference to GUI
     */
    private GUI gui;
    /**
     * reference to MessageHandler
     */
    private MessageHandler messageHandler;
    /**
     * reference to Client
     */
    private Client client;
    /**
     * Red color for text
     */
    private static final String REDCOLOR = "#ff0000";
    /**
     * Yellow color for text
     */
    private static final String YELLOWCOLOR = "#ffd938";

    /**
     * constructor method of LoginGUI
     * @param gui reference to GUI
     * @param messageHandler reference to MessageHandler
     * @param client reference to Client
     */
    LoginGUI(GUI gui, MessageHandler messageHandler, Client client) {
        this.gui = gui;
        this.messageHandler = messageHandler;
        this.client = client;
    }

    /**
     * set the backGround Image
     * @param stage the stage where to show everything
     */
    void loginImageSetting(Stage stage){
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

    /**
     * show the textField where the user are going to write and send his nickname
     * @param stage the stage where to show everything
     */
    void loginGridSetting(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        Button button = new Button();
        Button button2 = new Button();
        TextField username = new TextField();
        GridPane grid = new GridPane();
        Label text = new Label();
        text.setTextFill(Color.web(YELLOWCOLOR, 0.8));
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
        grid.add(infoText,0,5,2,1);
        grid.add(text,0,0,2,1);
        grid.addRow(1, new Text(""));
        grid.add(username,0,2,2,1);
        grid.addRow(3, new Text(""));
        grid.add(button,0,4);
        grid.add(button2,1,4);
        grid.setAlignment(Pos.CENTER);

        //text
        GridPane.setHalignment(text, HPos.CENTER);
        text.setAlignment(Pos.CENTER);
        text.setText("Login");
        text.prefWidthProperty().bind(pane.widthProperty().divide(5));

        //info text
        GridPane.setHalignment(infoText, HPos.CENTER);
        infoText.setAlignment(Pos.CENTER);
        infoText.prefHeightProperty().bind(pane.heightProperty().divide(12));
        if(this.gui.isMessageToShow()){
           infoText.setTextFill(Color.web(REDCOLOR,0.8));
           infoText.setText(messageHandler.getToShow().substring(4));
        }

        //username
        username.setPromptText("Username");
        username.prefWidthProperty().bind(pane.widthProperty().divide(20));
        username.prefHeightProperty().bind(pane.heightProperty().divide(20));
        username.setTooltip(new Tooltip("You can't use a Nickname with - or quit"));

        //button
        buttonSetting(pane,button,15,22, "LOGIN", Pos.CENTER, HPos.CENTER);
        button.setOnAction(e -> loginButtonAction(username,infoText));
        pane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginButtonAction(username,infoText);
                e.consume();
            }
        });

        //exit
        buttonSetting(pane,button2,15,22, "EXIT", Pos.CENTER, HPos.CENTER);
        button2.setOnAction(e -> client.send("quit"));

        pane.getChildren().add(grid);
        fullScreenButton(stage,pane);
    }

    /**
     * behavior of Login button
     * @param username is the textField
     * @param infoText label for error text
     */
    private void loginButtonAction(TextField username, Label infoText){
        if(gui.isSendable()){
            if (username.getText().equals("quit") || username.getText().equals("") || username.getText().contains("-")) {
                infoText.setTextFill(Color.web(REDCOLOR, 0.8));
                infoText.setText("Invalid Nickname");
            } else {
                client.send("LOG-".concat(username.getText()));
            }
        }
        else {
            infoText.setTextFill(Color.web(REDCOLOR, 0.8));
            infoText.setText("Wait a moment, please");
        }
    }

    /**
     * show game settings
     * @param stage the stage where to show everything
     * @param array choosable datas of the game
     * @param msg message to show
     */
    void menuGridSetting(Stage stage, ArrayList<String> array, String msg){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        GridPane grid = new GridPane();
        Button doneButton = new Button();
        Button quitButton = new Button();

        //grid column constraint
        for (int j = 0 ; j < 3; j++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(10);
            grid.getColumnConstraints().add(col);
        }

        //title label
        Label title = new Label("Settings");
        title.setTextFill(Color.web(YELLOWCOLOR, 0.8));
        title.setStyle("-fx-font: 40 Helvetica;");
        title.setEffect(new DropShadow());

        //label info menu
        Label infoMenu = new Label(msg);
        infoMenu.setTextFill(Color.web(YELLOWCOLOR, 0.8));
        infoMenu.setStyle("-fx-font: 30 Helvetica;");
        infoMenu.setEffect(new DropShadow());
        GridPane.setHalignment(infoMenu, HPos.CENTER);

        //choice box
        ChoiceBox<String> choicebox = new ChoiceBox<>();
        for(int i=0 ;i<array.size();i++)
            choicebox.getItems().add(array.get(i));
        choicebox.getSelectionModel().selectFirst();

        //done Button
        buttonSetting(pane,doneButton,10,20, "DONE", Pos.CENTER, HPos.CENTER);
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

        pane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                doneButton.fire();
                e.consume();
            }
        });

        //quit Button
        buttonSetting(pane,quitButton,10,20, "QUIT", Pos.CENTER, HPos.CENTER);
        quitButton.setOnAction(e -> client.send("quit"));

        //grid
        grid.add(title,0,0,3,1);
        grid.add(infoMenu,0,1,3,1);
        grid.add(choicebox,1,2);
        grid.add(doneButton,0,3);
        grid.add(quitButton,2,3);

        grid.setAlignment(Pos.CENTER);
        grid.setVgap(50);
        grid.setHgap(10);

        //title
        GridPane.setHalignment(title, HPos.CENTER);
        title.setAlignment(Pos.CENTER);
        title.prefWidthProperty().bind(pane.widthProperty().divide(2));

        //choicebox
        GridPane.setHalignment(choicebox, HPos.CENTER);
        choicebox.prefWidthProperty().bind(pane.widthProperty().divide(10));
        choicebox.prefHeightProperty().bind(pane.heightProperty().divide(20));

        pane.getChildren().add(grid);
        fullScreenButton(stage,pane);
    }

    /**
     * show the waiting room
     * @param stage the stage where to show everything
     */
    void roomGridSetting(Stage stage){
        StackPane pane = (StackPane)stage.getScene().getRoot();
        stage.getScene().setRoot(pane);
        GridPane grid = new GridPane();


        //title label
        Label title = new Label();
        title.setTextFill(Color.web(YELLOWCOLOR, 0.8));
        title.setStyle("-fx-font: 50 Helvetica;");
        title.setEffect(new DropShadow());

        //exit button
        Button buttonExit = new Button();
        buttonSetting(pane,buttonExit,10,20, "EXIT", Pos.CENTER, HPos.CENTER);
        buttonExit.setOnAction(e -> client.send("quit"));

        //grid
        grid.add(title,0,0);
        grid.addRow(1,new Text (""));

        grid.addRow(7,new Text (""));
        grid.add(buttonExit,0,8);
        grid.setAlignment(Pos.CENTER);

        //List of player
        ArrayList<String> playerList = new ArrayList<>();
        if(messageHandler.getBigReceive()!=null){
            Collections.addAll(playerList, messageHandler.getBigReceive());
        }
        while(playerList.size()<5){
            playerList.add("");
        }

        //title
        GridPane.setHalignment(title, HPos.CENTER);
        title.setAlignment(Pos.CENTER);
        title.setText("Player");
        title.prefWidthProperty().bind(pane.widthProperty().divide(2));


        for(int i=0; i<playerList.size(); i++){
            Label playerNickName = new Label();
            playerNickName.setTextFill(Color.web(YELLOWCOLOR, 0.8));
            playerNickName.setStyle("-fx-font: 20 Helvetica;");
            playerNickName.setEffect(new DropShadow());

            grid.add(playerNickName,0,i+2);

            GridPane.setHalignment(playerNickName, HPos.CENTER);
            playerNickName.setAlignment(Pos.CENTER);
            playerNickName.setText(playerList.get(i));
            playerNickName.prefWidthProperty().bind(pane.widthProperty().divide(10));
            playerNickName.prefHeightProperty().bind(pane.heightProperty().divide(20));
        }

        pane.getChildren().add(grid);
        fullScreenButton(stage,pane);
    }

    /**
     * behavior of fullscreen button
     * @param stage the stage where to show everything
     * @param pane the stackPane of the stage
     */
    private void fullScreenButton(Stage stage, StackPane pane){
        Button fullScreen = new Button("FS");

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

        pane.getChildren().add(fullScreen);
    }

    /**
     * utility setting of button's dimension and alignment
     * @param pane the stackPane of the stage
     * @param button the button to set
     * @param width the percentage width of the button
     * @param height the percentage height of the button
     * @param text the text of the button
     * @param alignment the alignment of the button
     * @param gridHalignment the horizontal alignment of the button
     */
    private void buttonSetting(StackPane pane, Button button, int width, int height, String text, Pos alignment, HPos gridHalignment){
        GridPane.setHalignment(button, gridHalignment);
        button.setAlignment(alignment);
        button.setText(text);
        button.prefWidthProperty().bind(pane.widthProperty().divide(width));
        button.prefHeightProperty().bind(pane.heightProperty().divide(height));
    }
}
