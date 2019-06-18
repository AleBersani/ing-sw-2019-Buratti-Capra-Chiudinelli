package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.client.Client;
import it.polimi.ingsw.communication.client.MessageHandler;
import it.polimi.ingsw.view.ViewInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class GUI extends Application implements ViewInterface {

    private Client client;
    private Stage stage;
    private LoginGUI loginGUI;
    private GameGUI gameGUI;
    private MessageHandler messageHandler;
    private boolean sendable;
    private boolean messageToShow;
    private String boardData;
    private String playersData;
    private String youData;
    private String killShotTrackData;
    private String infoString;
    private ArrayList<ArrayList<ArrayList<String>>> boardRepresentation;
    private ArrayList<ArrayList<String>> playersRepresentation;
    private ArrayList<String> youRepresentation;
    private ArrayList<String> killShotRepresentation;
    private ArrayList<String> infoChoiceBox;
    private static final int SECOND_ETIQUETTE= 4;
    private static final int CELL_SEPARATOR= 3;
    private static final int SQUARE_BRACKET= 1;
    private static final int POSSIBLE_SPACE= 1;


    public boolean isMessageToShow() {
        return messageToShow;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getInfoString() {
        return infoString;
    }

    public boolean isSendable() {
        return sendable;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.client = new Client(this);
        messageHandler = new MessageHandler(this,client);
        this.boardRepresentation = new ArrayList<>();
        this.playersRepresentation = new ArrayList<>();
        this.youRepresentation = new ArrayList<>();
        this.killShotRepresentation = new ArrayList<>();
        this.loginGUI = new LoginGUI(this,messageHandler,client);
        this.gameGUI = new GameGUI(this,messageHandler,client);
        client.setMessageHandler(messageHandler);
        client.init();
        client.start();
        this.stage = primaryStage;
        this.sendable = false;

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,Toolkit.getDefaultToolkit().getScreenSize().getHeight()/1.5);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.setScene(scene);

        //stage
        stage.setOnCloseRequest(e -> {
            client.send("quit");
        });
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("/images/login/adrenalineLogo.png"));
        stage.setResizable(true);

        loginGUI.loginImageSetting(stage);
        loginGUI.loginGridSetting(stage);
        stage.show();
    }

    private void clearPane(){
        StackPane stackPane = new StackPane();
        stage.getScene().setRoot(stackPane);
    }

    private void menuGrid(){
        this.clearPane();
        loginGUI.loginImageSetting(stage);
        loginGUI.menuGridSetting(stage,infoChoiceBox,infoString);
    }

    private void waitingRoom(){
        this.clearPane();
        loginGUI.loginImageSetting(stage);
        loginGUI.roomGridSetting(stage);
    }

    private void realShowMessage(){
        this.messageToShow = true;
        this.clearPane();
        switch (messageHandler.getState()){
            case LOGIN: {
                loginGUI.loginImageSetting(stage);
                loginGUI.loginGridSetting(stage);
                break;
            }
            case MENU: {
                loginGUI.loginImageSetting(stage);
                loginGUI.menuGridSetting(stage,infoChoiceBox,infoString);
                break;
            }
            case WAIT: {
                loginGUI.loginImageSetting(stage);
                loginGUI.roomGridSetting(stage);
                break;
            }
            case GAME: {
                this.gameGUI.buildBoard(stage);
                this.gameGUI.buildPlayers(stage);
                this.gameGUI.buildYou(stage);
                this.gameGUI.buildKillShotTrack(stage);
                this.gameGUI.buildButtons(stage);
                break;
            }
        }
        this.messageToShow = false;
    }

    private void spawnView(){
        gameGUI.spawn(stage);
    }

    private void showGameBoard(){
        int i=0;
        int j;
        boardRepresentation.clear();
        for(String room: this.boardData.substring(SQUARE_BRACKET,this.boardData.length()-SQUARE_BRACKET).split(", ")){
            j=0;
            this.boardRepresentation.add(new ArrayList<>());
            room = room.substring(0,room.length()-CELL_SEPARATOR);
            for(String cell: room.split(" - ")){
                boardRepresentation.get(i).add(new ArrayList<>());
                for (String element : cell.split(";")) {
                    boardRepresentation.get(i).get(j).add(element);
                }
                j++;
            }
            i++;
        }
        this.stage.setFullScreen(true);
        this.clearPane();
        this.gameGUI.backGround(stage);
        this.gameGUI.buildBoard(stage);
    }

    private void showPlayers(){
        playersRepresentation.clear();
        int i=0;
        for (String player: this.playersData.substring(SQUARE_BRACKET,playersData.length()-SQUARE_BRACKET).split(",")){
            this.playersRepresentation.add(new ArrayList<>());
            for (String info: player.split(";")){
                if(info.startsWith(" ")){
                    info=info.substring(POSSIBLE_SPACE);
                }
                playersRepresentation.get(i).add(info);
            }
            i++;
        }
        this.gameGUI.buildPlayers(stage);
    }

    private void showYou(){
        youRepresentation.clear();
        for(String info: this.youData.split(";")){
            youRepresentation.add(info);
        }
        this.gameGUI.buildYou(stage);
    }

    private void showKillShot(){
        killShotRepresentation.clear();
        for(String info: this.killShotTrackData.split(";")){
            killShotRepresentation.add(info);
        }
        this.gameGUI.buildKillShotTrack(stage);
        this.gameGUI.buildButtons(stage);
    }

    @Override
    public void spawn(String msg) {
        this.infoString = msg;
        Platform.runLater(this::spawnView);
    }

    @Override
    public void showMessage() {
        Platform.runLater(this::realShowMessage);
    }

    @Override
    public void gameShow(String msg) {
        switch (msg.substring(0,SECOND_ETIQUETTE)){
            case "BRD-":{
                this.boardData = msg.substring(SECOND_ETIQUETTE);
                Platform.runLater(this::showGameBoard);
                break;
            }
            case "PLR-":{
                this.playersData = msg.substring(SECOND_ETIQUETTE);
                Platform.runLater(this::showPlayers);
                break;
            }
            case "YOU-":{
                this.youData = msg.substring(SECOND_ETIQUETTE);
                Platform.runLater(this::showYou);
                break;
            }
            case "KLL-":{
                this.killShotTrackData = msg.substring(SECOND_ETIQUETTE);
                Platform.runLater(this::showKillShot);
                break;
            }
        }
    }

    @Override
    public void boardSettingView(ArrayList<String> data, String title) {
        this.infoChoiceBox = data;
        this.infoString = title;
        Platform.runLater(this::menuGrid);
    }

    @Override
    public void waitingRoomView() {
        Platform.runLater(this::waitingRoom);
    }

    @Override
    public void loginView() {
        this.sendable = true;
    }

    @Override
    public void stopView() {
        Platform.exit();
    }

    public ArrayList<ArrayList<ArrayList<String>>> getBoardRepresentation() {
        return boardRepresentation;
    }

    public ArrayList<ArrayList<String>> getPlayersRepresentation() {
        return playersRepresentation;
    }

    public ArrayList<String> getYouRepresentation() {
        return youRepresentation;
    }

    public ArrayList<String> getKillShotRepresentation() {
        return killShotRepresentation;
    }
}