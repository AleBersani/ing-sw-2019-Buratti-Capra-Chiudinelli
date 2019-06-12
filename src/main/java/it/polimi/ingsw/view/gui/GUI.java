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
    private boolean sendable;
    private boolean messageToShow;
    private String gameData;
    private String infoString;
    private ArrayList<ArrayList<ArrayList<String>>> boardRepresentation;
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

    public boolean isSendable() {
        return sendable;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.client = new Client(this);
        messageHandler = new MessageHandler(this,client);
        this.boardRepresentation = new ArrayList<>();
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

                break;
            }
        }
        this.messageToShow = false;
    }

    private void showGameBoard(){
        int i=0;
        int j;
        boardRepresentation.clear();
        for(String room: this.gameData.substring(SQUARE_BRACKET,this.gameData.length()-SQUARE_BRACKET).split(", ")){
            j=0;
            this.boardRepresentation.add(new ArrayList<>());
            room = room.substring(0,room.length()-CELL_SEPARATOR);
            for(String cell: room.split(" - ")){
                    boardRepresentation.get(i).add(new ArrayList<>());
                    for (String element : cell.split(";")) {
                        boardRepresentation.get(i).get(j).add(element);
                        System.out.println(boardRepresentation.get(i).get(j));
                    }
                    j++;
            }
            i++;
        }
        this.stage.setFullScreen(true);
        this.clearPane();
        this.gameGUI.buildBoard(stage);
    }

    @Override
    public void showMessage() {
        Platform.runLater(this::realShowMessage);
    }

    @Override
    public void gameShow(String msg) {
        switch (msg.substring(0,SECOND_ETIQUETTE)){
            case "BRD-":{
                this.gameData = msg.substring(SECOND_ETIQUETTE);
                Platform.runLater(this::showGameBoard);
                break;
            }
            case "PLR-":{
                //TODO
                break;
            }
            case "YOU-":{
                //TODO
                break;
            }
            case "KLL-":{
                //TODO
                break;
            }
        }
    }

    public ArrayList<ArrayList<ArrayList<String>>> getBoardRepresentation() {
        return boardRepresentation;
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

}