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

/**
 * Class that generate all of the graphics element
 */
public class GUI extends Application implements ViewInterface {
    /**
     * reference to the client
     */
    private Client client;
    /**
     * Stage where all of the graphics elements are placed
     */
    private Stage stage;
    /**
     * reference to LoginGui
     */
    private LoginGUI loginGUI;
    /**
     * reference to GameGUI
     */
    private GameGUI gameGUI;
    /**
     * reference to BackGraphicsGUI
     */
    private BackGraphicsGUI backGraphicsGUI;
    /**
     * reference to ButtonsGUI
     */
    private ButtonsGUI buttonsGUI;
    /**
     * reference to ShootingGUI
     */
    private ShootingGUI shootingGUI;
    /**
     * reference to ChooseGUI
     */
    private ChooseGUI chooseGUI;
    /**
     * reference to MessageHandler
     */
    private MessageHandler messageHandler;
    /**
     * true if the server is listening to the messages from the client in the login state
     */
    private boolean sendable;
    /**
     * true if the messages from the server are to be showed
     */
    private boolean messageToShow;
    /**
     * boolean to stop the update of the background in the game state
     */
    boolean noUpdate = false;
    /**
     * true if the messages that are to be show have to be showed even after an update of the background
     */
    boolean persistentShow;
    /**
     * message for the update of the board
     */
    private String boardData;
    /**
     * message for the update of the enemies data
     */
    private String playersData;
    /**
     * message for the update of the data of the owner
     */
    private String youData;
    /**
     * message for the update of the kill shot track
     */
    private String killShotTrackData;
    /**
     * message that contains data for the game
     */
    private String infoString;
    /**
     * message for show a correct Spawn
     */
    String infoSpawn;
    /**
     * message for show the right requests during the target buildings method
     */
    private String infoTarget;
    /**
     * message that contains data for the optional shoot
     */
    private String opz;
    /**
     * message that contains the weapons to be discarded
     */
    private String discard;
    /**
     * contains the pane after the reShow
     */
    StackPane afterPane;
    /**
     * list of every square of the board
     */
    private ArrayList<ArrayList<ArrayList<String>>> boardRepresentation;
    /**
     * list of the players data
     */
    private ArrayList<ArrayList<String>> playersRepresentation;
    /**
     * list of every data of the owner
     */
    private ArrayList<String> youRepresentation;
    /**
     * list of the skulls and bloods on the kill shot track
     */
    private ArrayList<String> killShotRepresentation;
    /**
     * list of the setting for the menu
     */
    private ArrayList<String> infoChoiceBox;
    /**
     * constant for the length of the second etiquette in a message
     */
    private static final int SECOND_ETIQUETTE= 4;
    /**
     * constant for the length of the separator of the cell message
     */
    private static final int CELL_SEPARATOR= 3;
    /**
     * constant for the length of the square bracket
     */
    private static final int SQUARE_BRACKET= 1;
    /**
     * constant for the length of the space
     */
    private static final int POSSIBLE_SPACE= 1;

    /**
     * Getter method of MessageHandler
     * @return the MessageHandler
     */
    MessageHandler getMessageHandler() {
        return messageHandler;
    }
    /**
     * Getter method of infoTarget
     * @return target message
     */
    String getInfoTarget() {
        return infoTarget;
    }
    /**
     * Getter method of opz
     * @return opz message
     */
    String getOpz() {
        return opz;
    }

    /**
     * Getter method of MessageToShow
     * @return true if the messages from the server are to be showed
     */
    boolean isMessageToShow() {
        return messageToShow;
    }

    /**
     * Setter method of Client
     * @param client the Client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Getter method of Sendable
     * @return Sendable
     */
    boolean isSendable() {
        return sendable;
    }

    /**
     * Starter method of GUI
     * @param primaryStage the stage where all of the graphics elements are placed
     * @throws Exception can throw exceptions from the client initialization
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.initialize();
        client.init();
        client.start();
        this.stage = primaryStage;

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

    /**
     * Initializer method of GUI
     */
    private void initialize(){
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
        this.shootingGUI = new ShootingGUI(this,gameGUI,buttonsGUI);
        this.chooseGUI = new ChooseGUI(this,client,gameGUI,shootingGUI,buttonsGUI);
        buttonsGUI.setShootingGUI(shootingGUI);
        buttonsGUI.setChooseGUI(chooseGUI);
        shootingGUI.setChooseGUI(chooseGUI);
        client.setMessageHandler(messageHandler);
        this.sendable = false;
    }

    /**
     * method that reshow the background in the game state
     */
    void reShow(){
        clearPane();
        this.backGraphicsGUI.backGround(stage);
        this.backGraphicsGUI.buildBoard(stage);
        this.backGraphicsGUI.buildPlayers(stage);
        this.backGraphicsGUI.buildYou(stage);
        this.backGraphicsGUI.buildKillShotTrack(stage);
        this.buttonsGUI.buildButtons(stage);
        this.afterPane = (StackPane)stage.getScene().getRoot();
        if(this.persistentShow){
            gameGUI.informationMessage(stage,messageHandler.getToShow().substring(3));
        }
    }

    /**
     * method to clear the pane of the stage
     */
    private void clearPane(){
        StackPane stackPane = new StackPane();
        stage.getScene().setRoot(stackPane);
    }

    /**
     * method to show the menu
     */
    private void menuGrid(){
        this.clearPane();
        loginGUI.loginImageSetting(stage);
        loginGUI.menuGridSetting(stage,infoChoiceBox,infoString);
    }

    /**
     * method to show the waiting room
     */
    private void waitingRoom(){
        this.clearPane();
        loginGUI.loginImageSetting(stage);
        loginGUI.roomGridSetting(stage);
    }

    /**
     * method that show the messages to show in all of the states
     */
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
                this.persistentShow = true;
                gameGUI.informationMessage(stage,messageHandler.getToShow().substring(3));
                break;
            }
            default:
        }
        this.messageToShow = false;
    }

    /**
     * method that show the respawn screen
     */
    private void spawnView(){
        this.noUpdate = true;
        chooseGUI.spawn(stage,infoSpawn);
    }

    /**
     * method that split and show the data for the building of the board
     */
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

    /**
     * method that split and show the data of the players
     */
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

    /**
     * method that split and show the owner's data
     */
    private void showYou(){
        youRepresentation.clear();
        this.youData = this.youData.concat(" ");
        Collections.addAll(youRepresentation, this.youData.split(";"));
        this.backGraphicsGUI.buildYou(stage);
    }

    /**
     * method that split and show the kill shot track data
     */
    private void showKillShot(){
        killShotRepresentation.clear();
        Collections.addAll(killShotRepresentation, this.killShotTrackData.split(";"));
        this.backGraphicsGUI.buildKillShotTrack(stage);
        this.buttonsGUI.buildButtons(stage);
        if(this.persistentShow){
            gameGUI.informationMessage(stage,messageHandler.getToShow().substring(3));
        }
    }

    /**
     * method that show the reload screen
     */
    private void reload(){
        this.chooseGUI.reload(stage,infoString);
    }

    /**
     * method that show the building target screen
     */
    private void showTarget(){
        this.shootingGUI.buildTarget(stage,infoTarget," ");
    }

    /**
     * method that show the initial phase for the shoot action
     */
    private void preShowShoot(){
        this.shootingGUI.preShootShow(stage,infoTarget);
    }

    /**
     * method that show the discard weapon screen
     */
    private void weaponDiscard(){
        chooseGUI.chooseWeapon(stage, this.discard);
    }

    /**
     * method that show the suspended screen
     */
    private void realSuspended(){
        reShow();
        this.gameGUI.suspended(stage);
    }

    /**
     * method that show the screen for the power ups that can be played in response of a specific action
     */
    private void interruptPower(){
        this.chooseGUI.interruptPowerUpUses(stage,infoSpawn);
    }

    /**
     * method that show the winner screen
     */
    private void showWinner(){
        loginGUI.loginImageSetting(stage);
        gameGUI.buildWinner(stage,this.infoString);
    }

    /**
     * method that show the screen for the shooting action during the frenzy mode
     */
    private void shootFrenzy(){
        buttonsGUI.shootFrenzy(stage);
    }

    /**
     * method that show the respawn screen
     * @param msg data for the spawn
     */
    @Override
    public void spawn(String msg) {
        this.infoSpawn = msg;
        Platform.runLater(this::spawnView);
    }

    /**
     * method that show a message from the server
     */
    @Override
    public void showMessage() {
        Platform.runLater(this::realShowMessage);
    }

    /**
     * method that show the background
     * @param msg data for the background
     */
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

    /**
     * method that show the menu screen
     * @param data data of the settings to show
     * @param title of the data to be show
     */
    @Override
    public void boardSettingView(ArrayList<String> data, String title) {
        this.infoChoiceBox = data;
        this.infoString = title;
        Platform.runLater(this::menuGrid);
    }

    /**
     * method that show the waiting room
     */
    @Override
    public void waitingRoomView() {
        Platform.runLater(this::waitingRoom);
    }

    /**
     * method that show the login
     */
    @Override
    public void loginView() {
        this.sendable = true;
    }

    /**
     * method that close the screen
     */
    @Override
    public void stopView() {
        Platform.exit();
    }

    /**
     * method that change the actions in the game for the end turn phase
     */
    @Override
    public void endTurnShow() {
        this.gameGUI.optionalShoot = false;
        this.gameGUI.endTurn=true;
        Platform.runLater(this::reShow);
    }

    /**
     * method that show the reload
     * @param msg data for the reload
     */
    @Override
    public void reloadShow(String msg) {
        this.infoString = msg;
        Platform.runLater(this::reload);
    }

    /**
     * method that show the building of the target
     * @param msg data for the building of the target
     */
    @Override
    public void targetShow(String msg) {
        this.infoTarget = msg;
        Platform.runLater(this::showTarget);
    }

    /**
     * method that show the discard of the weapons
     * @param msg data for the discard
     */
    @Override
    public void discardWeapon(String msg) {
        this.discard = msg;
        Platform.runLater(this::weaponDiscard);
    }

    /**
     * method that show the initial phase of the shoot
     * @param msg data for the pre shoot
     */
    @Override
    public void preShoot(String msg) {
        this.infoTarget = msg;
        Platform.runLater(this::preShowShoot);
    }

    /**
     * method that change the actions in the game meanwhile there is an optional shoot
     * @param msg data for the optional shoot
     */
    @Override
    public void optionalShoot(String msg) {
        this.opz = msg;
        this.gameGUI.optionalShoot = true;
        Platform.runLater(this::reShow);
    }

    /**
     * method that reshow the background
     */
    @Override
    public void gameReShow() {
        gameGUI.stopMovement = false;
        this.gameGUI.optionalShoot = false;
        Platform.runLater(this::reShow);
    }

    /**
     * method that show the winner
     * @param msg data for the winner
     */
    @Override
    public void winnerShow(String msg) {
        this.infoString = msg;
        Platform.runLater(this::showWinner);
    }

    /**
     * method that show the screen for the power ups that can be played in response of a specific action
     * @param msg data for this type of power ups
     */
    @Override
    public void interruptPowerUp(String msg) {
        this.infoSpawn = msg;
        Platform.runLater(this::interruptPower);
    }

    /**
     * method used meanwhile there is a shoot action in the frenzy turn
     */
    @Override
    public void frenzyShootShow() {
        Platform.runLater(this::shootFrenzy);
    }

    /**
     * method that show the suspended screen
     */
    @Override
    public void suspend() {
        Platform.runLater(this::realSuspended);
    }

    /**
     * getter method of board representation
     * @return a list of strings that contains the data of the cells
     */
    ArrayList<ArrayList<ArrayList<String>>> getBoardRepresentation() {
        return boardRepresentation;
    }

    /**
     * getter method of players representation
     * @return the list of strings where there are the data for the players
     */
    ArrayList<ArrayList<String>> getPlayersRepresentation() {
        return playersRepresentation;
    }

    /**
     * getter method of you representation
     * @return the data of the owner
     */
    ArrayList<String> getYouRepresentation() {
        return youRepresentation;
    }

    /**
     * getter method of kill shot track representation
     * @return the data for the kill shot track
     */
    ArrayList<String> getKillShotRepresentation() {
        return killShotRepresentation;
    }
}