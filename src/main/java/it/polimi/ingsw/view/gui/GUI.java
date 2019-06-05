package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import it.polimi.ingsw.communication.client.MessageHandler;
import it.polimi.ingsw.view.ViewInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends Application implements ViewInterface {

    private Client client;
    private Stage stage;
    private LoginGUI loginGUI;
    private GameGUI gameGUI;
    private MessageHandler messageHandler;
    private boolean messageToShow;
    private State state;
    private String gameData;
    private ArrayList<ArrayList<String>> boardRepresentation;

    private static final int startSecondEtiquette= 0, endSecondEtiquette= 7, squareBracket= 1, cellSeparetore= 3;

    public enum State{
        LOGIN, MENU, WAIT, BOARD;
    }

    public boolean isMessageToShow() {
        return messageToShow;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Client client = new Client(this);
        this.client = client;
        messageHandler = new MessageHandler(this,client);
        this.boardRepresentation = new ArrayList<ArrayList<String>>();
        this.loginGUI = new LoginGUI(this,messageHandler,client);
        this.gameGUI = new GameGUI(this,messageHandler,client);
        client.setMessageHandler(messageHandler);
        client.init();
        client.start();
        this.stage = primaryStage;

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,Toolkit.getDefaultToolkit().getScreenSize().getHeight()/1.5);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setScene(scene);

        //stage
        stage.setOnCloseRequest(e -> {
            messageHandler.setToSend("quit");
            client.setWaiting(false);
        });
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("/images/login/adrenalineLogo.png"));
        stage.setResizable(true);

        //login
        loginGUI.loginImageSetting(stage);
        loginGUI.loginGridSetting(stage);
        stage.show();
        this.state = State.LOGIN;
    }

    private void clearPane(){
        StackPane stackPane = new StackPane();
        stage.getScene().setRoot(stackPane);
    }

    private void menuGrid(){
        this.state = State.MENU;
        this.clearPane();
        loginGUI.loginImageSetting(stage);
        loginGUI.menuGridSetting(stage);
    }

    private void waitingRoom(){
        this.state = State.WAIT;
        this.clearPane();
        loginGUI.loginImageSetting(stage);
        loginGUI.roomGridSetting(stage);
    }

    private void realShowMessage(){
        this.messageToShow = true;
        this.clearPane();
        switch (this.state){
            case LOGIN: {
                loginGUI.loginImageSetting(stage);
                loginGUI.loginGridSetting(stage);
                break;
            }
            case MENU: {
                loginGUI.loginImageSetting(stage);
                loginGUI.menuGridSetting(stage);
                break;
            }
            case WAIT: {
                loginGUI.loginImageSetting(stage);
                loginGUI.roomGridSetting(stage);
                break;
            }
            case BOARD: {

                break;
            }
        }
        this.messageToShow = false;
    }

    private void showGameBoard(){
        this.state = State.BOARD;
        int i=0;
        boardRepresentation.clear();
        for(String room: this.gameData.substring(squareBracket,this.gameData.length()-squareBracket).split(",")){
            this.boardRepresentation.add(new ArrayList<String>());
            for(String cell: room.split(" - ",room.length()-cellSeparetore)){
                boardRepresentation.get(i).add(cell);
            }
            i++;
        }
        this.gameGUI.buildMap(stage);
    }

    @Override
    public void showMessage() {
        Platform.runLater(this::realShowMessage);
    }

    @Override
    public void dataShow(String msg) {
        switch (msg.substring(startSecondEtiquette,endSecondEtiquette)){
            case "Board++":{
                this.gameData = msg.substring(endSecondEtiquette);
                Platform.runLater(this::showGameBoard);
                break;
            }
            case "Players":{
                //TODO
                break;
            }
            case "You++++":{
                //TODO
                break;
            }
        }
    }

    @Override
    public void boardSettingView() {
        Platform.runLater(this::menuGrid);
    }

    @Override
    public void waitingRoomView() {
        Platform.runLater(this::waitingRoom);
    }

    @Override
    public void stopView() {
        Platform.exit();
    }
}