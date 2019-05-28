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

public class GUI extends Application implements ViewInterface {

    private Client client;
    private Stage stage;
    private LoginGUI loginGUI = new LoginGUI(this);
    private MessageHandler messageHandler;
    private boolean messageToShow;

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
            synchronized (client){
                client.notify();
            }
        });
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("/images/adrenalineLogo.png"));
        stage.setResizable(true);

        //call methods
        /*
        for(int i=0;i<3;i++){
            StackPane stackPane = new StackPane();
            stage.getScene().setRoot(stackPane);
            loginGUI.loginImageSetting(stage);
            if(i==0){
                loginGUI.loginGridSetting(stage,client,messageHandler);
            }
            if(i==1){
                loginGUI.menuGridSetting(stage,client,messageHandler);
            }
            if(i==2){
                loginGUI.roomGridSetting(stage,client,messageHandler);
            }
            stage.show();
        }
        */

        loginGUI.loginImageSetting(stage);
        loginGUI.loginGridSetting(stage,client,messageHandler);

        stage.show();
    }

    private void clearPane(){
        StackPane stackPane = new StackPane();
        stage.getScene().setRoot(stackPane);
    }

    private void menugrid(){
        this.clearPane();
        loginGUI.loginImageSetting(stage);
        loginGUI.menuGridSetting(stage,client,messageHandler);
    }

    private void waitingRoom(){
        this.clearPane();
        loginGUI.loginImageSetting(stage);
        loginGUI.roomGridSetting(stage,client,messageHandler);
    }

    @Override
    public void showMessage() {
        this.messageToShow = true;
        this.stage.show();
        this.messageToShow = false;
    }

    @Override
    public void boardSettingView() {
        Platform.runLater(this::menugrid);
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