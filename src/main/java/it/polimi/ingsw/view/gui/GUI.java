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
import java.util.Collections;

public class GUI extends Application implements ViewInterface {

    private Client client;
    private Stage stage;
    private LoginGUI loginGUI;
    private GameGUI gameGUI;
    private BackGraphicsGUI backGraphicsGUI;
    private ButtonsGUI buttonsGUI;
    private ShootingGUI shootingGUI;
    private MessageHandler messageHandler;
    private boolean sendable;
    private boolean messageToShow;
    boolean noUpdate = false;
    boolean persistenShow;
    private String boardData;
    private String playersData;
    private String youData;
    private String killShotTrackData;
    private String infoString;
    private String infoSpawn;
    private String infoTarget;
    private String opz;
    private String discard;
    StackPane afterPane;
    private ArrayList<ArrayList<ArrayList<String>>> boardRepresentation;
    private ArrayList<ArrayList<String>> playersRepresentation;
    private ArrayList<String> youRepresentation;
    private ArrayList<String> killShotRepresentation;
    private ArrayList<String> infoChoiceBox;
    private static final int SECOND_ETIQUETTE= 4;
    private static final int CELL_SEPARATOR= 3;
    private static final int SQUARE_BRACKET= 1;
    private static final int POSSIBLE_SPACE= 1;

    MessageHandler getMessageHandler() {
        return messageHandler;
    }

    String getInfoTarget() {
        return infoTarget;
    }

    String getOpz() {
        return opz;
    }

    boolean isMessageToShow() {
        return messageToShow;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    boolean isSendable() {
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
        this.gameGUI = new GameGUI(this,client);
        this.backGraphicsGUI = new BackGraphicsGUI (this,gameGUI);
        this.buttonsGUI = new ButtonsGUI(this,client,gameGUI);
        this.shootingGUI = new ShootingGUI(this,client,gameGUI,buttonsGUI);
        gameGUI.setButtonsGUI(buttonsGUI);
        gameGUI.setShootingGUI(shootingGUI);
        buttonsGUI.setShootingGUI(shootingGUI);
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
        stage.setOnCloseRequest(e -> client.send("quit"));
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("/images/login/adrenalineLogo.png"));
        stage.setResizable(true);

        loginGUI.loginImageSetting(stage);
        loginGUI.loginGridSetting(stage);
        stage.show();
    }

    void reShow(){
        clearPane();
        this.backGraphicsGUI.backGround(stage);
        this.backGraphicsGUI.buildBoard(stage);
        this.backGraphicsGUI.buildPlayers(stage);
        this.backGraphicsGUI.buildYou(stage);
        this.backGraphicsGUI.buildKillShotTrack(stage);
        this.buttonsGUI.buildButtons(stage);
        this.afterPane = (StackPane)stage.getScene().getRoot();
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
        switch (messageHandler.getState()){
            case LOGIN: {
                this.clearPane();
                loginGUI.loginImageSetting(stage);
                loginGUI.loginGridSetting(stage);
                break;
            }
            case MENU: {
                this.clearPane();
                loginGUI.loginImageSetting(stage);
                loginGUI.menuGridSetting(stage,infoChoiceBox,infoString);
                break;
            }
            case WAIT: {
                this.clearPane();
                loginGUI.loginImageSetting(stage);
                loginGUI.roomGridSetting(stage);
                break;
            }
            case GAME: {
                this.persistenShow = true;
                gameGUI.informationMessage(stage,messageHandler.getToShow().substring(3));
                break;
            }
        }
        this.messageToShow = false;
    }

    private void spawnView(){
        this.noUpdate = true;
        gameGUI.spawn(stage,infoSpawn);
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
        this.backGraphicsGUI.backGround(stage);
        this.backGraphicsGUI.buildBoard(stage);
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
        this.backGraphicsGUI.buildPlayers(stage);
    }

    private void showYou(){
        youRepresentation.clear();
        this.youData = this.youData.concat(" ");
        Collections.addAll(youRepresentation, this.youData.split(";"));
        this.backGraphicsGUI.buildYou(stage);
    }

    private void showKillShot(){
        killShotRepresentation.clear();
        Collections.addAll(killShotRepresentation, this.killShotTrackData.split(";"));
        this.backGraphicsGUI.buildKillShotTrack(stage);
        this.buttonsGUI.buildButtons(stage);
    }

    private void reload(){
        this.gameGUI.reload(stage,infoString);
    }

    private void showTarget(){
        this.shootingGUI.buildTarget(stage,infoTarget," ");
    }

    private void preShowShoot(){
        this.shootingGUI.preShootShow(stage,infoTarget);
    }

    private void weaponDiscard(){
        gameGUI.chooseWeapon(stage, this.discard);
    }

    private void insertCommand(){
        reShow();
        if(this.persistenShow){
            gameGUI.informationMessage(stage,messageHandler.getToShow().substring(3));
        }
    }

    private void suspance(){
        reShow();
        this.gameGUI.suspended(stage);
    }

    private void interuptPower(){
        this.gameGUI.interuptPowerUpUses(stage,infoSpawn);
    }

    private void showWinner(){
        loginGUI.loginImageSetting(stage);
        gameGUI.buildWinner(stage,this.infoString);
    }

    private void shootFrenzy(){
        buttonsGUI.shootFrenzy(stage);
    }

    @Override
    public void spawn(String msg) {
        this.infoSpawn = msg;
        Platform.runLater(this::spawnView);
    }

    @Override
    public void showMessage() {
        Platform.runLater(this::realShowMessage);
    }

    @Override
    public void gameShow(String msg) {
        if(!noUpdate) {
            switch (msg.substring(0, SECOND_ETIQUETTE)) {
                case "BRD-": {
                    this.boardData = msg.substring(SECOND_ETIQUETTE);
                    Platform.runLater(this::showGameBoard);
                    break;
                }
                case "PLR-": {
                    this.playersData = msg.substring(SECOND_ETIQUETTE);
                    Platform.runLater(this::showPlayers);
                    break;
                }
                case "YOU-": {
                    this.youData = msg.substring(SECOND_ETIQUETTE);
                    Platform.runLater(this::showYou);
                    break;
                }
                case "KLL-": {
                    this.killShotTrackData = msg.substring(SECOND_ETIQUETTE);
                    Platform.runLater(this::showKillShot);
                    break;
                }
                default:

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

    @Override
    public void endTurnShow() {
        this.gameGUI.optionalShoot = false;
        this.gameGUI.endTurn=true;
        Platform.runLater(this::reShow);
    }

    @Override
    public void reloadShow(String msg) {
        this.infoString = msg;
        Platform.runLater(this::reload);
    }

    @Override
    public void targetShow(String msg) {
        this.infoTarget = msg;
        Platform.runLater(this::showTarget);
    }

    @Override
    public void discardWeapon(String msg) {
        this.discard = msg;
        Platform.runLater(this::weaponDiscard);
    }

    @Override
    public void preShoot(String msg) {
        this.infoTarget = msg;
        Platform.runLater(this::preShowShoot);
    }

    @Override
    public void optionalShoot(String msg) {
        this.opz = msg;
        this.gameGUI.optionalShoot = true;
        Platform.runLater(this::reShow);
    }

    @Override
    public void gameReShow() {
        this.gameGUI.optionalShoot = false;
        Platform.runLater(this::insertCommand);
    }

    @Override
    public void winnerShow(String msg) {
        this.infoString = msg;
        Platform.runLater(this::showWinner);
    }

    @Override
    public void interruptPowerUp(String msg) {
        this.infoSpawn = msg;
        Platform.runLater(this::interuptPower);
    }

    @Override
    public void frenzyShootShow() {
        Platform.runLater(this::shootFrenzy);
    }

    @Override
    public void suspend() {
        Platform.runLater(this::suspance);
    }

    ArrayList<ArrayList<ArrayList<String>>> getBoardRepresentation() {
        return boardRepresentation;
    }

    ArrayList<ArrayList<String>> getPlayersRepresentation() {
        return playersRepresentation;
    }

    ArrayList<String> getYouRepresentation() {
        return youRepresentation;
    }

    ArrayList<String> getKillShotRepresentation() {
        return killShotRepresentation;
    }
}