package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.communication.server.ClientHandler;
import it.polimi.ingsw.communication.server.MultiServer;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.cards.WeaponAlternative;
import it.polimi.ingsw.model.cards.WeaponOptional;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.map.SpawnPoint;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is the controller that handle the game
 */
public class Controller {

    /**
     * This attribute is the minimum number of player before the game ends for lack of players
     */
    private int minimumPlayer;

    /**
     * This attribute is a list of ClientInfo mapped with the nickname
     */
    private Map<String,ClientInfo> nicknameList = new ConcurrentHashMap<>();

    /**
     * This attribute is a list of the nickname of the disconnected players
     */
    private ArrayList<String> disconnected = new ArrayList<>();

    /**
     * This attribute is the server that handle the connection
     */
    private MultiServer server;

    /**
     * This attribute is the number of skulls that are used in the game
     */
    private int skulls;

    /**
     * This attribute is true if the frenzy rules are enabled
     */
    private boolean frenzyEn;

    /**
     * This attribute is the path of the file that contains the board
     */
    private String board;

    /**
     * This attribute is the duration of the timer before the match starts in seconds
     */
    private int timer;

    /**
     * This attribute describe the mode of the game
     */
    private String mode;

    /**
     * This attribute is the list of available boards
     */
    private ArrayList<Integer> availableBoards;

    /**
     * This attribute is the list of available values of skulls
     */
    private ArrayList<Integer> availableSkulls;

    /**
     * This attribute is the list of the available colors for the players
     */
    private ArrayList<String> availableColors;

    /**
     * This attribute is turned to true when the match starts
     */
    private boolean gameStarted;

    /**
     * This attribute is the match
     */
    private Match match;

    /**
     * This attributed is used for synchronizing access to nicknameList
     */
    private final Object nicknameListLock;

    /**
     * This constant is the length of the etiquette in the messages
     */
    private static final int ETIQUETTE = 4;

    /**
     * This constant is the maximum number of players
     */
    private static final int MAX_PLAYERS_NUMBER = 5;

    /**
     * This parameter is true when someone is respawning
     */
    private boolean respawning;

    /**
     * This parameter is the duration of the timer of a single turn in seconds
     */
    private int timerTurn;

    /**
     * This parameter is the timer that, when expires, suspend a player from play
     */
    private Timer suspending;

    /**
     * This constructor initialize the controller using the data in the configuration file
     * @param args This parameters contains the path of the configuration file at position 1
     */
    public Controller(String [] args){
        Gson gSon= new Gson();
        Configuration configuration;
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            configuration = gSon.fromJson(br, Configuration.class);
        }
        catch (Exception e){
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Configuration.json")));
            configuration = gSon.fromJson(br, Configuration.class);
        }
        this.board="/Board/Board" +Integer.toString(configuration.board)+ ".json";
        this.skulls=configuration.skulls;
        this.frenzyEn=configuration.frenzy;
        this.timer=configuration.timer;
        this.mode=configuration.mode;
        this.availableBoards=configuration.availableBoards;
        this.availableSkulls=configuration.availableSkulls;
        this.availableColors=configuration.availableColors;
        this.timerTurn=configuration.timerTurn;
        this.minimumPlayer=configuration.minimumPlayer;
        this.gameStarted=false;
        this.nicknameListLock = new Object();
        this.respawning=false;
    }

    /**
     * This method send a message to a client
     * @param msg This parameter is the message that will be sent
     * @param clientHandler This parameter is the clientHandler of the client that will recive the message
     */
    private void sendString(String msg, ClientHandler clientHandler) {
        clientHandler.print(msg);
    }

    /**
     * This method is used for initializing a server for handling the connection
     * @param port This parameter is the port where the connection is opened
     * @throws IOException This exception is thrown when there are problems on the connection
     */
    public void launchServer(int port) throws IOException {
        this.server = new MultiServer(port,this);
        server.init(timer);
        server.lifeCycle();
    }

    /**
     * This method is called every time the controller receives a message
     * @param msg This parameter is the message received
     * @param clientHandler This parameter is the clientHander of the client who sent the message
     */
    public void understandMessage(String msg, ClientHandler clientHandler){
        if(msg.startsWith("LOG-")){
            this.login(msg.substring(ETIQUETTE),clientHandler);
        }
        else {
            switch (getNicknameList().get(clientHandler.getName()).state) {
                case LOGIN: {
                    if (msg.startsWith("SET-")) {
                        understandSettingMessages(msg.substring(ETIQUETTE), clientHandler);
                    } else {
                        sendString("Wrong Etiquette, this is LOGIN", clientHandler);
                    }
                    break;
                }
                case GAME: {

                    if(msg.startsWith("GMC-")){
                        understandGameCommand(clientHandler, msg.substring(ETIQUETTE));
                    }
                    else {
                        sendString("Wrong Etiquette, this is GAME", clientHandler);
                    }
                    break;
                }
                case SPAWN: {
                    if (msg.startsWith("SPW-")) {
                        try {
                            spawn(clientHandler, playerFromNickname(clientHandler.getName(), this.match), Integer.parseInt(msg.substring(ETIQUETTE)));
                        } catch (NotFoundException e) {
                            sendString("error", clientHandler);
                        }

                    }
                    else {
                        sendString("Wrong Etiquette, this is SPAWN", clientHandler);
                    }
                    break;
                }
                case LAY_WEAPON: {
                    if (msg.startsWith("WPN-")){
                        layWeapon(Integer.parseInt(msg.substring(ETIQUETTE)), clientHandler);
                        updateBackground(this.match);
                        lifeCycle(clientHandler);
                    }
                    else {
                        sendString("Wrong Etiquette, this is LAY WEAPON", clientHandler);
                    }
                    break;
                }
                case END: {
                    if(msg.startsWith("GMC-UPU-")){
                        targetRequestPU(clientHandler,msg.substring(ETIQUETTE));
                    }
                    else {
                        if (msg.startsWith("END-")) {
                            reload(clientHandler);
                        }
                        else if(msg.startsWith("RLD-")) {
                            try {
                                effectiveReload(msg, clientHandler, this.match);
                            } catch (NoAmmoException e) {
                                reload(clientHandler);
                                sendString(">>>You don't have enough ammo", clientHandler);
                                return;
                            } catch (WrongPowerUpException e) {
                                reload(clientHandler);
                                sendString(">>>Wrong PowerUps", clientHandler);
                                return;
                            }
                            clientHandler.setYourTurn(false);
                            respawn();
                            this.match.getTurn().endTurn();
                            if(this.match.isEndgame()){
                                this.endGame();
                            }
                            suspending.cancel();
                            if(!respawning){
                                nextTurn();
                            }
                            else {
                                sendString(">>>Wait for the respawn", clientHandler);
                            }
                        }
                        else {
                            sendString("Wrong Etiquette, this is END", clientHandler);
                        }
                    }
                    break;
                }
                case TARGETING:{
                    if(msg.startsWith("TRG-")){
                        switch (msg.substring(ETIQUETTE).substring(0,ETIQUETTE)){
                            case "WPN-":{
                                shootingAction(clientHandler, msg.substring(ETIQUETTE*2));
                                break;
                            }
                            case "POU-":{
                                powerUpAction(clientHandler, msg.substring(ETIQUETTE*2));
                                break;
                            }
                            default:
                                sendString("Wrong Etiquette, this is TARGETING", clientHandler);
                        }
                    }
                    break;
                }
                case OPTIONAL_WEAPON_SHOOTING:{
                    if (msg.startsWith("TRG-WPN-")){
                        shootingAction(clientHandler,msg.substring(ETIQUETTE*2));
                    }

                    else if (msg.equals("ESH-")){
                        try {
                            offensivePowerUpResponse(clientInfoFromClientHandler(clientHandler),
                                    clientInfoFromClientHandler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(clientInfoFromClientHandler(clientHandler).optionalWeaponShooting));
                        } catch (NotFoundException e) {
                            sendString("error", clientHandler);
                        } catch (NoResponseException e) {
                            endOptionalShooting(clientHandler);
                        }
                    }
                    else {
                        sendString("Wrong Etiquette, this is OPTIONAL_WEAPON_SHOOTING", clientHandler);
                    }
                    break;
                }
                case RESPONSE_OFFENSIVE_PU:{
                    if(msg.startsWith("RPU-")){
                        if(msg.substring(ETIQUETTE).equals("no")){
                            try {
                                if (clientInfoFromClientHandler(clientHandler).weapon.isOptional()) {
                                    endOptionalShooting(clientHandler);
                                } else {
                                    endShooting(clientHandler, clientInfoFromClientHandler(clientHandler).weapon);
                                }
                            } catch (NotFoundException e) {
                                sendString("error", clientHandler);
                            }
                        }
                        else {
                            ArrayList<PowerUp> usable = new ArrayList<>();
                            try {
                                for (PowerUp powerUp : playerFromNickname(clientHandler.getName(), clientInfoFromClientHandler(clientHandler).simulation).getPowerUps()) {
                                    if (powerUp.getOnResponse() && powerUp.isOffensive()) {
                                        usable.add(powerUp);
                                    }
                                }
                                Player player = playerFromNickname(clientHandler.getName(), clientInfoFromClientHandler(clientHandler).simulation);
                                switch (msg.split(">")[1]){
                                    case "B":{
                                        player.pay(1,0,0,null);
                                        break;
                                    }
                                    case "R":{
                                        player.pay(0,1,0,null);
                                        break;
                                    }
                                    case "Y":{
                                        player.pay(0,0,1,null);
                                        break;
                                    }
                                    default:{
                                        PowerUp payment = player.getPowerUps().get(Integer.parseInt(msg.split(">")[1]));
                                        ArrayList<PowerUp> paymentWrapper= new ArrayList<>();
                                        paymentWrapper.add(payment);
                                        switch (payment.getColor()){
                                            case "blue":{
                                                player.pay(1,0,0,paymentWrapper);
                                                break;
                                            }
                                            case "red":{
                                                player.pay(0,1,0,paymentWrapper);
                                                break;
                                            }
                                            case "yellow":{
                                                player.pay(0,0,1,paymentWrapper);
                                                break;
                                            }
                                        }
                                    }

                                }
                                usable.get(Integer.parseInt(msg.substring(ETIQUETTE).split("'")[0])).useEffect(generateTarget(msg.split(">")[0].split("'")[1].substring(0, msg.split(">")[0].split("'")[1].length() - 1), clientHandler, clientInfoFromClientHandler(clientHandler).simulation),
                                        clientInfoFromClientHandler(clientHandler).weapon.getPreviousTarget());
                                playerFromNickname(clientHandler.getName(), clientInfoFromClientHandler(clientHandler).simulation).discard(usable.get(Integer.parseInt(msg.substring(ETIQUETTE).split("'")[0])));
                                if (clientInfoFromClientHandler(clientHandler).weapon.isOptional()) {
                                    endOptionalShooting(clientHandler);
                                } else {
                                    endShooting(clientHandler, clientInfoFromClientHandler(clientHandler).weapon);
                                }
                            } catch (NotFoundException | NoOwnerException e) {
                                sendString("error", clientHandler);
                            } catch (InvalidTargetException e) {
                                invalidTarget(clientHandler);
                            } catch (WrongPowerUpException e) {
                                try {
                                    cleanSimulation(clientInfoFromClientHandler(clientHandler));
                                } catch (NotFoundException e1) {
                                    sendString("error", clientHandler);
                                }
                                sendString(">>>Wrong power up", clientHandler);
                            } catch (NoAmmoException e) {
                                try {
                                    cleanSimulation(clientInfoFromClientHandler(clientHandler));
                                } catch (NotFoundException e1) {
                                    sendString("error", clientHandler);
                                }
                                sendString(">>>You don't have enogh ammo", clientHandler);
                            }
                        }
                    }
                    else {
                        sendString("Wrong Etiquette, this is OPTIONAL_WEAPON_SHOOTING", clientHandler);
                    }
                    break;
                }
                case RESPONSE_DEFENSIVE_POWERUP:{
                    if(msg.startsWith("RPU-")){
                        ArrayList<PowerUp> usable = new ArrayList<>();
                        try {
                            for (PowerUp powerUp : playerFromNickname(clientHandler.getName(), this.match).getPowerUps()) {
                                if (powerUp.getOnResponse() && !powerUp.isOffensive()) {
                                    usable.add(powerUp);
                                }
                            }
                            ArrayList<ArrayList<Player>> shooterWrapper= new ArrayList<>();
                            ArrayList<Player> shooter = new ArrayList<>();
                            shooter.add(this.match.getTurn().getCurrent());
                            shooterWrapper.add(new ArrayList<>());
                            shooterWrapper.add(new ArrayList<>());
                            shooterWrapper.add(shooter);
                            usable.get(Integer.parseInt(msg.substring(ETIQUETTE).split("'")[0])).useEffect(generateTarget(msg.split("'")[1].substring(0, msg.split("'")[1].length() - 1),clientHandler, this.match),shooterWrapper);
                            playerFromNickname(clientHandler.getName(), this.match).discard(usable.get(Integer.parseInt(msg.substring(ETIQUETTE).split("'")[0])));
                            clientHandler.setYourTurn(false);
                            clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.GAME);
                            for (ClientInfo clientInfo : getNicknameList().values()){
                                if(clientInfo.clientHandler.isYourTurn()){
                                    return;
                                }
                            }
                            getNicknameList().get(this.match.getTurn().getCurrent().getNickname()).clientHandler.setYourTurn(true);
                            updateBackground(this.match);
                            lifeCycle(getNicknameList().get(this.match.getTurn().getCurrent().getNickname()).clientHandler);
                        }
                        catch (NotFoundException | NoOwnerException e){
                            sendString("error", clientHandler);
                        } catch (InvalidTargetException e) {
                            invalidTarget(clientHandler);
                        }

                    }
                    break;
                }
                case SHOOTING_FRENZY:{
                    if(msg.startsWith("RLD-")) {
                        try {
                            try {
                                effectiveReload(msg, clientHandler, clientInfoFromClientHandler(clientHandler).simulation);
                            }  catch (NoAmmoException e) {
                                reload(clientHandler);
                                sendString(">>>You don't have enough ammo", clientHandler);
                                return;
                            } catch (WrongPowerUpException e) {
                                reload(clientHandler);
                                sendString(">>>Wrong PowerUps", clientHandler);
                                return;
                            }
                            updateBackground(clientInfoFromClientHandler(clientHandler).simulation);
                            sendString("FNZ-", clientHandler);
                        } catch (NotFoundException e) {
                            sendString("error", clientHandler);
                        }
                    }
                    else if(msg.startsWith("GMC-SHT-")){
                        targetRequestWeapon(clientHandler, msg.substring(ETIQUETTE));
                    }
                    break;
                }
            }
        }
    }

    /**
     * This method reload the weapons required
     * @param msg This parameter contains the position in the hand of the weapons to reload followed by the power up used for pay
     * @param clientHandler This parameter is the clientHandler of the client that wants to reload weapons
     * @param match This parameter is the match where the weapon need to be reloaded
     */
    private void effectiveReload(String msg, ClientHandler clientHandler, Match match) throws WrongPowerUpException, NoAmmoException {
        if (!msg.substring(ETIQUETTE).equals("")) {
            try {
                int totalCostBlue=0;
                int totalCostRed=0;
                int totalCostYellow=0;
                for (String weapon : msg.substring(ETIQUETTE).split(">")[0].split(",")) {
                    Weapon realWeapon = playerFromNickname(clientHandler.getName(),match).getWeapons().get(Integer.parseInt(weapon));
                    totalCostBlue= totalCostBlue+realWeapon.getCostBlue();
                    totalCostRed=totalCostRed+realWeapon.getCostRed();
                    totalCostYellow=totalCostYellow+realWeapon.getCostYellow();
                    playerFromNickname(clientHandler.getName(), match).pay(totalCostBlue,totalCostRed,totalCostYellow,generatePowerUpPayment(msg,clientHandler, match));
                    playerFromNickname(clientHandler.getName(), match).reload(realWeapon);
                }
            } catch (NotFoundException e) {
                sendString("error", clientHandler);
            } catch (LoadedException e) {
                sendString(">>>This weapon is already load", clientHandler);
            }
            updateBackground(match);
        }
    }

    /**
     * This method is called every time an InvalidTargetException is thrown
     * @param clientHandler This parameter is the clientHandler of the client that obtained the exception
     */
    private void invalidTarget(ClientHandler clientHandler){
        updateBackground(this.match);
        sendString(">>>Invalid target", clientHandler);
        try {
            cleanSimulation(clientInfoFromClientHandler(clientHandler));
        } catch (NotFoundException e1) {
            sendString("error", clientHandler);
        }
    }

    /**
     * This method is used for asking a client if he wants to use an offensive power up
     * @param clientInfo This parameter is the ClientInfo of the client that can use an offensive power up
     * @param weapon This parameter is the weapon that is used right before the use of the offensive power up
     * @throws NoResponseException This exception is thrown if the player doesn't have an offensive power up
     */
    private void offensivePowerUpResponse(ClientInfo clientInfo, Weapon weapon) throws NoResponseException {
        clientInfo.weapon= weapon;
        String responseRequest="RPU-";
        try {
            for(PowerUp p : playerFromNickname(clientInfo.clientHandler.getName(), this.match).getPowerUps()){
                if(p.getOnResponse() && p.isOffensive()){
                    responseRequest=responseRequest.concat(p.getName()).concat(":").concat(p.getColor()).concat(";");
                }
            }
            if(responseRequest.equals("RPU-")){
                throw new NoResponseException();
            }
            clientInfo.setState(ClientInfo.State.RESPONSE_OFFENSIVE_PU);
            sendString(responseRequest,clientInfo.clientHandler);

        } catch (NotFoundException e) {
            sendString("error", clientInfo.clientHandler);
        }
    }

    /**
     * This method is called when a new turn starts
     */
    private void nextTurn() {
        for (ClientInfo clientInfo : nicknameList.values()) {
            if (clientInfo.clientHandler.getName().equals(match.getTurn().getCurrent().getNickname())) {
                clientInfo.clientHandler.setYourTurn(true);
                if(clientInfo.state == ClientInfo.State.END_GAME){
                    return;
                }
                if(clientInfo.suspended){
                    clientInfo.setState(ClientInfo.State.END);
                    understandMessage("RLD-",clientInfo.clientHandler);
                }
                else {
                    timerTurn(clientInfo);
                    if (clientInfo.clientHandler.isFirstSpawn()) {
                        clientInfo.setState(ClientInfo.State.SPAWN);
                        startingSpawn(clientInfo.clientHandler, match.getTurn().getCurrent());
                    } else {
                        clientInfo.setState(ClientInfo.State.GAME);
                        updateBackground(this.match);
                        sendString(">>>Now is your turn", clientInfo.clientHandler);
                        lifeCycle(clientInfo.clientHandler);
                    }
                }
            }
        }
    }

    /**
     * This method is called when a player finish his shooting with a WeaponOptional
     * @param clientHandler This parameter is the clientHandler of the client that ends shooting
     */
    private void endOptionalShooting(ClientHandler clientHandler) {
        boolean ok=false;
        try {
            ClientInfo clientInfo = clientInfoFromClientHandler(clientHandler);
            for (String order :((WeaponOptional)playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(clientInfoFromClientHandler(clientHandler).optionalWeaponShooting)).getOrder()){
                String actualOrder=clientInfo.shootingOptionals.substring(0,clientInfo.shootingOptionals.length()-1);
                if(order.equals(actualOrder)){
                    ok=true;
                }
            }

            if(ok){
                this.match=clientInfoFromClientHandler(clientHandler).simulation;
                playerFromNickname(clientHandler.getName(), this.match).endShoot(playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(clientInfoFromClientHandler(clientHandler).optionalWeaponShooting));
                clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.GAME);
                clientInfoFromClientHandler(clientHandler).optionalWeaponShooting= null;
                clientInfoFromClientHandler(clientHandler).shootingOptionals="";
                clientInfoFromClientHandler(clientHandler).simulation=null;
                updateBackground(this.match);
                defensivePowerUpResponse(clientHandler);
                lifeCycle(clientHandler);
            }
            if(!ok){
                clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.GAME);
                clientInfoFromClientHandler(clientHandler).optionalWeaponShooting= null;
                clientInfoFromClientHandler(clientHandler).shootingOptionals="";
                clientInfoFromClientHandler(clientHandler).simulation=null;
                updateBackground(this.match);
                sendString(">>>Not correct order of effects",clientHandler);
                lifeCycle(clientHandler);
            }
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    /**
     * This method is used for asking to one or more clients if they want to use a defensive power up
     * @param clientHandler This parameter is the clientHandler of the client that will be the target of the defensive power up
     */
    private void defensivePowerUpResponse(ClientHandler clientHandler) {
        boolean toUse=false;
        for(Player opponent : this.match.getPlayers()) {
            if (opponent.isWounded()) {
                String request = "RPU-";
                ClientInfo clientInfo = null;
                for (PowerUp powerUp : opponent.getPowerUps()) {
                    if ( powerUp.getOnResponse() && !powerUp.isOffensive()) {
                        toUse = true;
                        request = request.concat(powerUp.getName()).concat(":").concat(powerUp.getColor()).concat(";");
                        clientInfo = getNicknameList().get(opponent.getNickname());
                        clientInfo.clientHandler.setYourTurn(true);
                        clientInfo.setState(ClientInfo.State.RESPONSE_DEFENSIVE_POWERUP);
                    }
                }
                if(request.equals("RPU-")){
                    return;
                }
                sendString(request, clientInfo.clientHandler);
            }
        }
        if (toUse) {
            clientHandler.setYourTurn(false);
            sendString(">>>Wait a response from the opponent", clientHandler);
        }
    }

    /**
     * This method sends to the client a list of his unloaded weapons
     * @param clientHandler This parameter is the clientHandler of the client that needs to reaload weapons
     */
    private void reload(ClientHandler clientHandler) {
        try {
            String toLoad="";
            for (Weapon weapon : playerFromNickname(clientHandler.getName(), this.match).getWeapons()){
                if(!weapon.isLoad()){
                    toLoad=toLoad.concat(weapon.getName()).concat("'");
                }
            }
            sendString("RLD-"+ toLoad, clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    /**
     * This method is used for laying a weapon if the player grab the fourth weapon
     * @param weaponPos This parameter is the position in hand of the weapon to lay
     * @param clientHandler This parameter is the clientHandler of the player that needs to lay a weapon
     */
    private void layWeapon(int weaponPos, ClientHandler clientHandler) {
        try {
            if(weaponPos<playerFromNickname(clientHandler.getName(), this.match).getWeapons().size()-1) {
                Player player = playerFromNickname(clientHandler.getName(), this.match);
                Weapon weapon = player.getWeapons().get(weaponPos);
                player.getWeapons().remove(weapon);
                weapon.reload();
                ((SpawnPoint) player.getPosition()).getWeapons().add(weapon);
                clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.GAME);
            }
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    /**
     * This method is called every time a client asks for a game action
     * @param clientHandler This parameter is the clientHandler that wants to do the game action
     * @param msg This parameter is the description of the action that the client wants to do
     */
    private void understandGameCommand(ClientHandler clientHandler, String msg) {
        switch (msg.substring(0,ETIQUETTE)){
            case "SHT-": {
                if (this.match.getTurn().isFrenzy()){
                    startShootingFrenzy(clientHandler,msg);
                }
                else {
                    targetRequestWeapon(clientHandler, msg);
                }
                break;
            }
            case "RUN-": {
                runningAction(clientHandler, msg);
                break;
            }
            case "GRB-": {
                grabbingAction(clientHandler, msg);
                break;
            }
            case "UPU-": {
                targetRequestPU(clientHandler, msg);
                break;
            }
            default: {
                sendString("Invalid command", clientHandler);
            }
        }
    }

    /**
     * This method starts the shooting when the player is in a frenzy turn
     * @param clientHandler This parameter is the clientHandler of the client that is shooting
     * @param msg This parameter is a description of the Square where the player wants to move
     */
    private void startShootingFrenzy(ClientHandler clientHandler, String msg) {
        try {
            int x= Integer.parseInt(msg.substring(ETIQUETTE).split(":")[0]);
            int y= Integer.parseInt(msg.substring(ETIQUETTE).split(":")[1]);
            ClientInfo clientInfo= clientInfoFromClientHandler(clientHandler);
            clientInfo.setState(ClientInfo.State.SHOOTING_FRENZY);
            clientInfo.simulation=deepClone(this.match);
            playerFromNickname(clientHandler.getName(), clientInfo.simulation).movementShootFrenzy(clientInfo.simulation.getBoard().find(x,y));
            updateBackground(clientInfo.simulation);
            reload(clientHandler);
        }
        catch (InvalidDestinationException e) {
            try {
                cleanSimulation(clientInfoFromClientHandler(clientHandler));
            } catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
            sendString(">>>Invalid destination", clientHandler);
        }
        catch (NotFoundException | IOException | ClassNotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    /**
     * This method send to the client the types of the targets required by a specific weapon
     * @param clientHandler This parameter is the clientHandler of the client that is shooting
     * @param msg This parameter is the description of the weapon
     */
    private void targetRequestWeapon(ClientHandler clientHandler, String msg) {
        String targetRequest="TRW-";
        try {
            clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.TARGETING);
            targetRequest=targetRequest.concat("Base'");
            for(Effect effect : playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE))).getEffect()){
                targetRequest=targetRequest.concat(effect.getDescription()).concat(";");
            }
            if (playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE))).isOptional()){
                int i=0;
                WeaponOptional weaponOptional=(WeaponOptional)playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE)));
                for (ArrayList<Effect> arrayList : weaponOptional.getOptionalEffect()){
                    targetRequest=targetRequest.concat("'Optional-"+ i +"'");
                    for (Effect effect : arrayList){
                        targetRequest=targetRequest.concat(effect.getDescription()).concat(";");
                    }
                    i++;
                }
            }
            if (playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE))).isAlternative()){
                WeaponAlternative weaponAlternative=(WeaponAlternative) playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE)));
                targetRequest=targetRequest.concat("'Alternative'");
                for (Effect effect : weaponAlternative.getAlternativeEffect()) {
                    targetRequest=targetRequest.concat(effect.getDescription()).concat(";");
                }
            }
            sendString(targetRequest, clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    /**
     * This method send to the client the type of the target required by a specific power up
     * @param clientHandler This parameter is the clientHandler of the client that is using the power up
     * @param msg This parameter is the description of the power up
     */
    private void targetRequestPU(ClientHandler clientHandler, String msg) {
        try {
            clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.TARGETING);
            sendString("TRG-"+playerFromNickname(clientHandler.getName(), this.match).getPowerUps().get(Integer.parseInt(msg.substring(ETIQUETTE))).getEffect().getDescription(), clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    /**
     * This method handle the main phase of a shooting action
     * @param clientHandler This parameter is the clientHandler of the client that is shooting
     * @param msg This parameter is the description of the targets
     */
    private void shootingAction(ClientHandler clientHandler, String msg){
        ArrayList<TargetParameter> targetParameters = new ArrayList<>();
        String[] data = msg.split(">")[0].split("'");
        try {
            if(clientInfoFromClientHandler(clientHandler).simulation == null) {
                clientInfoFromClientHandler(clientHandler).simulation = deepClone(match);
            }
            for (String target : data[2].split(">")[0].split(";")) {
                TargetParameter temp =generateTarget(target, clientHandler, clientInfoFromClientHandler(clientHandler).simulation);
                if(temp != null) {
                    targetParameters.add(temp);
                }
            }
            if(!targetParameters.isEmpty()){
                Weapon weapon=clientInfoFromClientHandler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0]));
                if(targetParameters.get(0).getTypeOfFire().equals("Alternative")){
                    playerFromNickname(clientHandler.getName(), clientInfoFromClientHandler(clientHandler).simulation).pay(
                            ((WeaponAlternative)weapon).getAlternativeEffect().get(0).getCostBlue(),
                            ((WeaponAlternative)weapon).getAlternativeEffect().get(0).getCostRed(),
                            ((WeaponAlternative)weapon).getAlternativeEffect().get(0).getCostYellow(),
                            generatePowerUpPayment("ADP-"+msg,clientHandler, clientInfoFromClientHandler(clientHandler).simulation)
                            );
                }
                else if(targetParameters.get(0).getTypeOfFire().startsWith("Optional-")){
                    String typeOfFire=targetParameters.get(0).getTypeOfFire();
                    playerFromNickname(clientHandler.getName(), clientInfoFromClientHandler(clientHandler).simulation).pay(
                            ((WeaponOptional)weapon).getOptionalEffect().get(Integer.parseInt(typeOfFire.split("-")[1])).get(0).getCostBlue(),
                            ((WeaponOptional)weapon).getOptionalEffect().get(Integer.parseInt(typeOfFire.split("-")[1])).get(0).getCostRed(),
                            ((WeaponOptional)weapon).getOptionalEffect().get(Integer.parseInt(typeOfFire.split("-")[1])).get(0).getCostYellow(),
                            generatePowerUpPayment("ADP-"+msg,clientHandler, clientInfoFromClientHandler(clientHandler).simulation)
                    );
                }
                clientInfoFromClientHandler(clientHandler).simulation.getTurn().getCurrent().shoot(weapon,
                        data[1].equals(" ") ? playerFromNickname(clientHandler.getName(), this.match).getPosition() : clientInfoFromClientHandler(clientHandler).simulation.getBoard().find(Integer.parseInt(data[1].split(":")[0]), Integer.parseInt(data[1].split(":")[1])),
                        targetParameters);
                if(!clientInfoFromClientHandler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])).isOptional()) {
                    try {
                        offensivePowerUpResponse(clientInfoFromClientHandler(clientHandler),clientInfoFromClientHandler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])));
                    }
                    catch (NoResponseException e) {
                        endShooting(clientHandler, clientInfoFromClientHandler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])));
                    }
                }
                else {
                    clientInfoFromClientHandler(clientHandler).shootingOptionals =
                            clientInfoFromClientHandler(clientHandler).shootingOptionals.concat(
                                    targetParameters.get(0).getTypeOfFire().contains("-") ?
                                            Integer.toString(Integer.parseInt(targetParameters.get(0).getTypeOfFire().split("-")[1]) + 1) :
                                            "0").concat("-");
                    clientInfoFromClientHandler(clientHandler).optionalWeaponShooting = Integer.parseInt(data[0]);
                    clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.OPTIONAL_WEAPON_SHOOTING);
                    updateBackground(clientInfoFromClientHandler(clientHandler).simulation);
                 String possibleOrder = "OWS-";
                    boolean ok = false;
                    for (String order : ((WeaponOptional) playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(clientInfoFromClientHandler(clientHandler).optionalWeaponShooting)).getOrder()) {
                        if (order.startsWith(clientInfoFromClientHandler(clientHandler).shootingOptionals)) {
                            possibleOrder = possibleOrder.concat(order.substring(clientInfoFromClientHandler(clientHandler).shootingOptionals.length())).concat(";");
                         ok = true;
                        }
                    }
                    if (!ok) {
                        try {
                            offensivePowerUpResponse(clientInfoFromClientHandler(clientHandler),playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(clientInfoFromClientHandler(clientHandler).optionalWeaponShooting));
                        }
                        catch (NoResponseException e) {
                            endOptionalShooting(clientHandler);
                        }
                    } else {
                        sendString(possibleOrder, clientHandler);
                    }
                }
            }
            else {
                throw new InvalidTargetException();
            }
        } catch (InvalidTargetException e) {
            invalidTarget(clientHandler);

        } catch (NoOwnerException | IOException | ClassNotFoundException | NotThisKindOfWeapon | NotFoundException e) {
            sendString("error", clientHandler);
        } catch (NotLoadedException e) {
            updateBackground(this.match);
            sendString(">>>This weapon is unloaded", clientHandler);
            try {
                cleanSimulation(clientInfoFromClientHandler(clientHandler));
            }
            catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }

        } catch (InvalidDestinationException e) {
            updateBackground(this.match);
            sendString(">>>Invalid destination", clientHandler);
            try {
                cleanSimulation(clientInfoFromClientHandler(clientHandler));
            }
            catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
        } catch (NoAmmoException e) {
            updateBackground(this.match);
            sendString(">>>You don't have enough ammo", clientHandler);
            try {
                cleanSimulation(clientInfoFromClientHandler(clientHandler));
            }
            catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
        } catch (WrongPowerUpException e) {
            updateBackground(this.match);
            sendString(">>>Wrong PowerUps", clientHandler);
            try {
                cleanSimulation(clientInfoFromClientHandler(clientHandler));
            }
            catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
        }

    }

    /**
     * This method handle the final phase of a shooting action with a weapon that is not a WeaponOptional
     * @param clientHandler This parameter is the clientHandler of the client that was shooting
     * @param weapon This parameter is the weapon that was shooting
     */
    private void endShooting(ClientHandler clientHandler, Weapon weapon){
        try {
            clientInfoFromClientHandler(clientHandler).simulation.getTurn().getCurrent().endShoot(weapon);
            match = clientInfoFromClientHandler(clientHandler).simulation;
            updateBackground(this.match);
            cleanSimulation(clientInfoFromClientHandler(clientHandler));
            defensivePowerUpResponse(clientHandler);
        }
        catch (NotFoundException e){
            sendString("error", clientHandler);
        }
    }

    /**
     * This method reset the simulation on a ClientInfo
     * @param clientInfo This parameter is the clientInfo where the simulation will be cleaned
     */
    private void cleanSimulation(ClientInfo clientInfo){
        clientInfo.simulation=null;
        clientInfo.shootingOptionals= "";
        clientInfo.setState(ClientInfo.State.GAME);
        lifeCycle(clientInfo.clientHandler);
    }

    /**
     * This method handle a running action
     * @param clientHandler This parameter is the clientHandler of the client that is running
     * @param msg This parameter is the description of the square where the clients wants to go
     */
    private void runningAction(ClientHandler clientHandler, String msg){
        try {
            playerFromNickname(clientHandler.getName(), this.match).run(match.getBoard().find(Integer.parseInt(msg.substring(ETIQUETTE).split(",")[0]),
                    Integer.parseInt(msg.substring(ETIQUETTE).split(",")[1])));
            updateBackground(this.match);
        } catch (InvalidDestinationException e) {
            sendString(">>>Square not valid", clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
            updateBackground(this.match);
        }
        lifeCycle(clientHandler);
    }

    /**
     * This method return an ArrayList that contains the powerUps used for a payment
     * @param msg This parameter is a description of the powerUps used for a payment
     * @param clientHandler This parameter is the clientHandler of the client that is paying
     * @param match This parameter is the match where the power ups are taken from
     * @return An ArrayList that contains the powerUps used for a payment
     */
    private ArrayList<PowerUp> generatePowerUpPayment (String msg ,ClientHandler clientHandler, Match match) {
        ArrayList<PowerUp> payment = null;
        if (!(msg.substring(ETIQUETTE)).split(">")[1].equals(" ")) {
            payment = new ArrayList<>();
            for (String powerUp : msg.split(">")[1].split(",")) {
                try {
                    payment.add(playerFromNickname(clientHandler.getName(), match).getPowerUps().get(Integer.parseInt(powerUp)));
                } catch (NotFoundException e) {
                    sendString("error", clientHandler);
                }
            }
        }
        return payment;
    }

    /**
     * This method handle a grabbing action
     * @param clientHandler This parameter is the clientHandler of the client that wants to grab something
     * @param msg This parameter is the description of what the client wants to grab
     */
    private void grabbingAction(ClientHandler clientHandler, String msg){
        try {
            String[] stringo =msg.substring(ETIQUETTE).split(">")[0].split(",");
            playerFromNickname(clientHandler.getName(), this.match).grab(match.getBoard().find(Integer.parseInt(stringo[0]),
                    Integer.parseInt(stringo[1])));
            updateBackground(this.match);
            lifeCycle(clientHandler);
        } catch (InvalidDestinationException e) {
            sendString(">>>Square not valid", clientHandler);
        }catch (NotFoundException e) {
            sendString("error", clientHandler);
        } catch (NullAmmoException e) {
            sendString(">>>Nothing to grab", clientHandler);
        }
        catch (ElementNotFoundException e) {
            try {

                Weapon weapon = ((SpawnPoint) match.getBoard().find(Integer.parseInt(msg.substring(ETIQUETTE).split(">")[0].split(",")[0]), Integer.parseInt(msg.substring(ETIQUETTE).split(">")[0].split(",")[1]))).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE).split(">")[0].split(",")[2]));
                playerFromNickname(clientHandler.getName(), this.match).pay(
                        weapon.getColor().equals("blue") ? weapon.getCostBlue() - 1 : weapon.getCostBlue(),
                        weapon.getColor().equals("red") ? weapon.getCostRed() - 1 : weapon.getCostRed(),
                        weapon.getColor().equals("yellow") ? weapon.getCostYellow() - 1 : weapon.getCostYellow(),
                        generatePowerUpPayment(msg.substring(ETIQUETTE), clientHandler, this.match));
                playerFromNickname(clientHandler.getName(), this.match).grabWeapon(match.getBoard().find(Integer.parseInt(msg.substring(ETIQUETTE).split(",")[0]),
                        Integer.parseInt(msg.substring(ETIQUETTE).split(">")[0].split(",")[1])), Integer.parseInt(msg.substring(ETIQUETTE).split(">")[0].split(",")[2]));
                updateBackground(this.match);
                lifeCycle(clientHandler);

            } catch (ElementNotFoundException e1) {
                sendString(">>>Nothing to grab", clientHandler);
            }catch (NoAmmoException e1) {
                sendString(">>>You don't have enough ammo", clientHandler);
            } catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
            catch (WrongPowerUpException e1) {
                sendString(">>>Wrong powerUps", clientHandler);
            }
            catch (MaxHandWeaponSizeException e1) {
                try {
                    clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.LAY_WEAPON);
                    String toLay="WPN-Select the weapon to drop:";
                    for (int i=0; i<playerFromNickname(clientHandler.getName(), this.match).getWeapons().size()-1;i++){
                        toLay=toLay.concat(playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(i).getName()).concat(";");
                    }
                    sendString(toLay, clientHandler);
                } catch (NotFoundException e2) {
                    sendString("error", clientHandler);
                }
            }
        }
    }


    /**
     * This method handle the use of a power up
     * @param clientHandler This parameter is the clientHandler of the client that wants touse a power up
     * @param msg This parameter is the description of the target
     */
    private void powerUpAction(ClientHandler clientHandler, String msg){
        try {
            TargetParameter target = generateTarget(msg.split("'")[1].replace(";",""), clientHandler,this.match);
            if(target!=null) {
                playerFromNickname(clientHandler.getName(), this.match).usePowerUp(playerFromNickname(clientHandler.getName(), this.match).getPowerUps().get(Integer.parseInt(msg.split("'")[0])), target);
            }
            updateBackground(this.match);
        } catch (InvalidTargetException e) {
            invalidTarget(clientHandler);
        } catch (NoOwnerException | NotFoundException e) {
            sendString("error", clientHandler);
        } catch (OnResponseException e) {
            updateBackground(this.match);
            sendString(">>>This power up can be used only on response of another action", clientHandler);
            try {
                clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.GAME);
            } catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
            lifeCycle(clientHandler);
        }

        try {
            clientInfoFromClientHandler(clientHandler).setState(ClientInfo.State.GAME);
            lifeCycle(clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }

    }

    /**
     * This method generate a targetParameter from its description
     * @param target This parameter is the description of a target
     * @param clientHandler This parameter is the clientHandler of the owner of the weapon or powerUp
     * @param match This parameter is the match where the target are taken
     * @return A targetParameter based on its description
     */
    private TargetParameter generateTarget(String target,ClientHandler clientHandler, Match match) {
        String[] parameters = target.split(",");
        TargetParameter targetParameter= null;
        try {
            if(parameters[5].equals("true")) {
                targetParameter = new TargetParameter(
                        parameters[0].equals(" ") ? null : match.getBoard().find(Integer.parseInt(parameters[0].split(":")[0]), Integer.parseInt(parameters[0].split(":")[1])),
                        playerFromNickname(clientHandler.getName(), match),
                        parameters[1].equals(" ") ? null : playerFromColor(parameters[1], match),
                        parameters[2].equals(" ") ? null : match.getBoard().find(Integer.parseInt(parameters[2].split(":")[0]), Integer.parseInt(parameters[2].split(":")[1])).getRoom(),
                        parameters[3].equals(" ") ? null : match.getBoard().find(Integer.parseInt(parameters[3].split(":")[0]), Integer.parseInt(parameters[3].split(":")[1])),
                        parameters[4].equals(" ") ? null : parameters[4]
                );
            }
            else {
                return null;
            }
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }

        return targetParameter;
    }


    /**
     * This method ask to one or more clients to respawn
     */
    private void respawn() {
        for (Player player: match.getTurn().getDeads()){
            for (ClientInfo clientInfo: getNicknameList().values()){
                if(clientInfo.clientHandler.getName().equals(player.getNickname())){
                    clientInfo.clientHandler.setYourTurn(true);
                    clientInfo.setState(ClientInfo.State.SPAWN);
                    try {
                        player.draw();
                    } catch (MaxHandSizeException e) {
                        player.forceDraw();
                    }
                    sendString("SPW-Select a power up for spawning,"+ powerUps(player),clientInfo.clientHandler);
                    respawning=true;
                }
            }
        }
    }

    /**
     * This method handle the login
     * @param command This parameter is the nickname of the player that is logging
     * @param clientHandler This parameter is the clientHandler of the client that is logging
     */
    private void login(String command, ClientHandler clientHandler) {
        if (gameStarted) {
            if (this.disconnected.contains(command)) {
                clientHandler.setName(command);
                this.disconnected.remove(command);
                for (ClientInfo clientInfo : getNicknameList().values()){
                    if(clientInfo.clientHandler.getName().equals(command)){
                        clientInfo.clientHandler = clientHandler;
                        clientInfo.clientHandler.setYourTurn(false);
                        clientInfo.suspended = false;
                    }
                }
                sendString("Match started", clientHandler);
                updateBackground(this.match);
            }
            else {
                sendString(">>> Game already started",clientHandler);
            }
        }
        else {
            if (getNicknameList().size() < MAX_PLAYERS_NUMBER) {
                boolean first = false;

                if (getNicknameList().isEmpty()) {
                    first = true;
                }

                if (!clientHandler.isLogged()) {
                    if (!getNicknameList().containsKey(command)) {
                        clientHandler.setName(command);
                        getNicknameList().put(command, new ClientInfo(clientHandler));
                        System.out.println("<<< " + clientHandler.getSocket().getRemoteSocketAddress() + " is logged as: " + command);
                        sendString("logged as: " + command, clientHandler);
                        clientHandler.setLogged(true);
                        if (first) {
                            setGameRules(clientHandler);
                        }
                        else{
                            sendString("Now you are in the waiting room", clientHandler);
                            getNicknameList().get(command).setState(ClientInfo.State.WAIT);
                            for(ClientInfo clientInfo: getNicknameList().values()){
                                if(clientInfo.state.equals(ClientInfo.State.WAIT)){
                                    this.waitingRoom(clientInfo.clientHandler);
                                }
                            }
                        }
                    }
                    else {
                        sendString(">>> This nickname is already use", clientHandler);
                    }
                }
                else {
                    sendString(">>> You are already logged", clientHandler);
                }
            }
            else {
                sendString(">>> Game full", clientHandler);
            }
        }

    }

    /**
     * This method is used for asking a client to set the rules of the game
     * @param clientHandler This parameter is the clientHandler of the client that set the rules
     */
    private void setGameRules( ClientHandler clientHandler) {
        sendString("setting",clientHandler);
        sendString("Select a board-".concat(availableBoards.toString()),clientHandler); //Select a board[1,2,3,4]
    }

    /**
     * This method is used by the first client that log in to set the rules of the game
     * @param msg This parameter is the description of the rules that ara be setting
     * @param clientHandler This parameter is the clientHandler of the client that set the rules
     */
    private void understandSettingMessages(String msg, ClientHandler clientHandler){
        switch (msg.substring(0,ETIQUETTE)){
            case "BRD-": //Board
                this.selectBoard(msg.substring(ETIQUETTE),clientHandler);
                break;
            case "SKL-": //Skull
                this.setSkulls(msg.substring(ETIQUETTE),clientHandler);
                break;
            case "FRZ-": //Frenzy
                this.setFrenzy(msg.substring(ETIQUETTE),clientHandler);
                break;
            default:
                sendString("ERROR",clientHandler);
        }
    }

    /**
     * This method is used for setting the board
     * @param msg This parameter is the number of the board
     * @param clientHandler This parameter is the clientHandler of the client that set the rules
     */
    private void selectBoard(String msg, ClientHandler clientHandler){
        if(availableBoards.contains(Integer.parseInt(msg))) {
            board = "/Board/Board" + msg + ".json";
            sendString("You selected the board number " + msg, clientHandler);
            sendString("Select the number of skulls-".concat(availableSkulls.toString()), clientHandler);
        }
        else{
            sendString(">>>This board doesn't exist, please select another one", clientHandler);
        }
    }

    /**
     * This method is used for setting the number of skulls
     * @param msg This parameter is the number of skulls
     * @param clientHandler This parameter is the clientHandler of the client that set the rules
     */
    private void setSkulls(String msg, ClientHandler clientHandler){
        if(availableSkulls.contains(Integer.parseInt(msg))) {
            skulls = Integer.parseInt(msg);
            sendString("You selected " + msg + " skulls", clientHandler);
            sendString("Do you like to play with frenzy? Y/N", clientHandler);
        }
        else{
            sendString(">>>Value not valid", clientHandler);
        }
    }

    /**
     * This method is used for setting the frenzy
     * @param msg This parameter Y if the client wants to enable the frenzy, N otherwise
     * @param clientHandler This parameter is the clientHandler of the client that set the rules
     */
    private void setFrenzy(String msg, ClientHandler clientHandler){
        switch (msg){
            case "Y": {
                frenzyEn = true;
                sendString("You enabled frenzy",clientHandler);
                getNicknameList().get(clientHandler.getName()).setState(ClientInfo.State.WAIT);
                sendString("Now you are in the waiting room", clientHandler);
                this.waitingRoom(clientHandler);
                break;
            }
            case "N": {
                frenzyEn = false;
                sendString("You disabled frenzy", clientHandler);
                getNicknameList().get(clientHandler.getName()).setState(ClientInfo.State.WAIT);
                sendString("Now you are in the waiting room", clientHandler);
                this.waitingRoom(clientHandler);
                break;
            }
            default:{
                sendString(">>>Please respond Y or N", clientHandler);
            }
        }
    }

    /**
     * This method send to a client the list of players in the waiting room
     * @param clientHandler This parameter is the clientHandler of the client that will receive the list
     */
    private void waitingRoom(ClientHandler clientHandler){
        String playersNames = ":::";
        String[] allNames = getNicknameList().keySet().toArray(new String[0]);
        for(String name: allNames){
            playersNames = playersNames.concat("-" + name);
        }
        sendString(playersNames,clientHandler);

    }

    /**
     * This method is called when a client is disconnected
     * @param clientHandler This parameter is the clientHandler of the client that is disconnected
     */
    public void quit(ClientHandler clientHandler){
        clientHandler.setDisconnect(true);
        if(gameStarted) {
            disconnected.add(clientHandler.getName());
            try {
                suspend(clientHandler);
            } catch (NotFoundException e) {
                sendString("error", clientHandler);
            }
        }
        else {
            getNicknameList().remove(clientHandler.getName());
            for (ClientInfo clientInfo : getNicknameList().values()) {
                sendString(">>>" + clientHandler.getName() + " disconnected", clientInfo.clientHandler);
            }
        }
        for (ClientInfo clientInfo : getNicknameList().values()) {
            if (clientInfo.state.equals(ClientInfo.State.WAIT)) {
                this.waitingRoom(clientInfo.clientHandler);
            }
        }

        server.print(clientHandler.getName() + " disconnected");
    }

    /**
     * This method is called to suspend a player for inactivity
     * @param clientHandler This parameter is the clientHandler of the client that is suspende
     * @throws NotFoundException This exception is thrown if the client doesn't exist in the nicknameList
     */
    private void suspend(ClientHandler clientHandler) throws NotFoundException {
        ClientInfo clientInfo = clientInfoFromClientHandler(clientHandler);
        clientInfo.suspend();
        if(!clientHandler.isDisconnect()) {
            for (ClientInfo all : getNicknameList().values()) {
                sendString(">>>" + clientHandler.getName() + " suspended", all.clientHandler);
            }
        }
        if(clientHandler.isYourTurn()) {
            if (clientInfo.state == ClientInfo.State.LAY_WEAPON) {
                understandMessage("WPN-0", clientHandler);
            }
            if (clientInfo.state == ClientInfo.State.SPAWN) {
                understandMessage("SPW-0", clientHandler);
            }
            clientInfo.setState(ClientInfo.State.END);
            understandMessage("RLD-", clientHandler);
        }
    }

    /**
     * This method generate a Player for each ClientHandler and start the game
     */
    public void startGame() {
        Random random=new Random();
        int n;
        n=random.nextInt(getNicknameList().size());
        ArrayList<Player> players;
        players=new ArrayList<>();
        for(Map.Entry e : getNicknameList().entrySet()){
            players.add(new Player(false,"",(String) e.getKey()));
        }
        players.get(n).setFirst(true);
        for (Player p : players){
            p.setColor(availableColors.get(0));
            availableColors.remove(availableColors.get(0));
        }
         System.out.println("Match started");
        gameStarted=true;
        match = new Match(players, getNicknameList().size(), skulls, frenzyEn, mode, board);
        match.start();
        for (Player player: match.getPlayers()){
            player.setTurn(match.getTurn());
        }
        for(ClientInfo clientInfo: getNicknameList().values()){
            sendString("Match started",clientInfo.clientHandler);
            clientInfo.setState(ClientInfo.State.GAME);
            clientInfo.clientHandler.setYourTurn(false);

        }
        updateBackground(this.match);
        for(ClientInfo clientInfo: getNicknameList().values()) {
            if (match.getTurn().getCurrent().getNickname().equals(clientInfo.clientHandler.getName())) {
                clientInfo.clientHandler.setYourTurn(true);
                clientInfo.setState(ClientInfo.State.SPAWN);
                startingSpawn(clientInfo.clientHandler, match.getTurn().getCurrent());
                timerTurn(clientInfo);
            }
        }

    }

    /**
     * This method start a timer for a single turn
     * @param clientInfo This parameter is the clientInfo of the client that has the turn
     */
    private void timerTurn(ClientInfo clientInfo){
        suspending=new Timer();
        suspending.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    suspend(clientInfo.clientHandler);
                } catch (NotFoundException e) {
                    sendString("error", clientInfo.clientHandler);
                }
                clientInfo.setState(ClientInfo.State.END);
                understandMessage("RLD-", clientInfo.clientHandler);
            }
        }, timerTurn*1000);
    }

    /**
     * This method is called when a player need to spawn for the first time
     * @param actual This parameter is the clientHandler of the client that needs to spawn
     * @param player This parameter is the player that needs to spawn
     */
    private void startingSpawn(ClientHandler actual, Player player){
        try {
            player.draw();
        } catch (MaxHandSizeException e) {
            player.forceDraw();
        }
        try {
            player.draw();
        } catch (MaxHandSizeException e) {
            player.forceDraw();
        }
        updateBackground(this.match);
        sendString("SPW-Discard a power up for spawning," +powerUps(player), actual);
    }

    /**
     * This method sends to a client what kind of actions he can do
     * @param actual his parameter is the clientHandler of the client that needs to do actions
     */
    private void lifeCycle(ClientHandler actual) {
        try {
            if(match.getTurn().getActionCounter()<(playerFromNickname(actual.getName(), this.match).onlyFrenzyAction()==1?1:2)) {
                sendString("INS-", actual);
            }
            else {
                sendString("END-Use a powerUp or end turn", actual);
                try {
                    clientInfoFromClientHandler(actual).setState(ClientInfo.State.END);
                } catch (NotFoundException e) {
                    sendString("error", actual);
                }

            }
        } catch (NotFoundException e) {
            sendString("error", actual);
        }

    }

    /**
     * This methods generate a String that contains a description of all the powerUps of a player
     * @param player This parameter is the player
     * @return A String that contains a description of all the powerUps of a player
     */
    private String powerUps(Player player){
        String powerUps="";
        for(int i=0; i<player.getPowerUps().size();i++) {
            powerUps=powerUps.concat(player.getPowerUps().get(i).getName())
                    .concat(":").concat(player.getPowerUps().get(i).getColor())
                    .concat("'");
        }
        return powerUps;
    }

    /**
     * This method send to all the clients all the information of the match
     * @param match This parameter is the match
     */
    private void updateBackground(Match match){
        for (ClientInfo clientInfo: getNicknameList().values()){
            if(!clientInfo.suspended) {
                sendString(boardDescriptor(match), clientInfo.clientHandler);
                sendString(playersDescriptor(clientInfo.clientHandler, match), (clientInfo.clientHandler));
                sendString(youDescriptor(clientInfo.clientHandler, match), clientInfo.clientHandler);
                sendString(killshotTrackDescriptor(match), clientInfo.clientHandler);
            }
            else {
                sendString("SPD-", clientInfo.clientHandler);
            }
        }
    }

    /**
     * This method is called when a player needs to spawn
     * @param actual This parameter is the clientHandler of the client that need to spawn
     * @param player This parameter is the player that needs to spawn
     * @param powerUpPosition This parameter is the position in the hand of the powerUp used for spawning
     */
    public void spawn(ClientHandler actual, Player player,int powerUpPosition) {
        if(powerUpPosition<player.getPowerUps().size() && powerUpPosition>=0) {
            PowerUp powerUp = player.getPowerUps().get(powerUpPosition);

            try {
                player.spawn(powerUp);
                getNicknameList().get(actual.getName()).setState(ClientInfo.State.GAME);
            } catch (NotFoundException e) {
                sendString("SpawnPoint not found", actual);
            }
            updateBackground(this.match);

            if(!actual.isFirstSpawn()) {
                actual.setYourTurn(false);
            }
            else {
                actual.setFirstSpawn(false);
            }

            if(actual.isYourTurn()){
                lifeCycle(actual);
            }
            for (ClientInfo clientInfo : getNicknameList().values()){
                if(clientInfo.clientHandler.isYourTurn()){
                    return;
                }
            }
            if(respawning) {
                nextTurn();
                respawning= false;
            }

        }
        else {
            sendString(">>>Invalid powerUp", actual);
            sendString("SPW-Discard a power up for spawning," +powerUps(player), actual);
        }

    }

    /**
     * This method produce a description of the killshotTrack of a match
     * @param match This parameter is the match
     * @return A description of the killshotTrack of a match
     */
    private String killshotTrackDescriptor(Match match) {
        String killshotTrackDescriptor= "BGD-KLL-".concat(Integer.toString(skulls)).concat(";");
        for (Player p : match.getKillShotTrack()) {
            killshotTrackDescriptor = killshotTrackDescriptor.concat(p.getColor()).concat("'");
        }
        killshotTrackDescriptor=killshotTrackDescriptor.concat(";").concat(match.getDoubleOnKillShotTrack().toString());
        return killshotTrackDescriptor;
    }

    /**
     * This method produce a description of the board of a match
     * @param match This parameter is the match
     * @return A description of the board of a match
     */
    private String boardDescriptor(Match match) {
        String boardDescriptor="BGD-BRD-";
        boardDescriptor=boardDescriptor.concat(match.getBoard().getRooms().toString());
        return boardDescriptor;
    }

    /**
     * This method produce a description of the opponents of a player in a match
     * @param current This parameter is the player
     * @param match This parameter is the match
     * @return A description of the opponents of a player in a match
     */
    private String playersDescriptor(ClientHandler current, Match match){
        String you= current.getName();
        ArrayList<Player> enemies= (ArrayList<Player>) match.getPlayers().clone();
        Player i=null;
        for (Player p : enemies){
            if(p.getNickname().equals(you)){
                i=p;
            }
        }
        enemies.remove(i);
        String playersDescriptor="BGD-PLR-";
        playersDescriptor=playersDescriptor.concat(enemies.toString());
        return playersDescriptor;
    }

    /**
     * This method produce a description of a player in a match
     * @param current This parameter is the player
     * @param match This parameter is the match
     * @return A description of a player in a match
     */
    private String youDescriptor(ClientHandler current, Match match){
        String you= current.getName();
        Player y=null;
        for (Player p: match.getPlayers()){
            if(p.getNickname().equals(you)){
                y=p;
            }
        }
        String youDescriptor="BGD-YOU-";
        youDescriptor=youDescriptor.concat(y.describe());
        return youDescriptor;
    }

    /**
     * This method find a player from his nickname
     * @param nickname This parameter is the nickname
     * @param match This parameter is the match where the player is taken
     * @return A player from his nickname
     * @throws NotFoundException This exception is thrown when doesn't exist a player with that nickname
     */
    private Player playerFromNickname(String nickname, Match match) throws NotFoundException {
        for (Player player : match.getPlayers()){
            if(nickname.equals(player.getNickname())){
                return player;
            }
        }
        throw (new NotFoundException());
    }

    /**
     * This method find a player from his color
     * @param color This parameter is the color
     * @param match This parameter is the match where the player is taken
     * @return A player from his color
     * @throws NotFoundException This exception is thrown when doesn't exist a player with that color
     */
    private Player playerFromColor(String color, Match match) throws NotFoundException {
        for (Player player : match.getPlayers()){
            if(color.equals(player.getColor())){
                return player;
            }
        }
        throw (new NotFoundException());
    }

    /**
     * This method find a ClientInfo from his ClientHandler
     * @param clientHandler This parameter is the clientHandler
     * @return A ClientInfo from his ClientHandler
     * @throws NotFoundException This exception is thrown when doesn't exist a clientInfo with that ClientHandler
     */
    private ClientInfo clientInfoFromClientHandler(ClientHandler clientHandler) throws NotFoundException {
        for (ClientInfo clientInfo : getNicknameList().values()){
            if(clientInfo.clientHandler.equals(clientHandler)){
                return clientInfo;
            }
        }
        throw new NotFoundException();
    }

    /**
     * This method clone a match
     * @param object This parameter is the match to clone
     * @return A copy of a match
     * @throws IOException This Exception is thrown when there are problems with the serialization
     * @throws ClassNotFoundException This Exception is thrown if the class is not found
     */
    static Match deepClone(Match object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
        return (Match) objectInputStream.readObject();
    }

    /**
     * This method is called when a suspended or disconnected player rejoin the game
     * @param clientHandler This parameter is the clientHandler of the client
     */
    public void revertSuspension(ClientHandler clientHandler) {
        try {
            ClientInfo clientInfo= clientInfoFromClientHandler(clientHandler);
            clientInfo.suspended=false;
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    /**
     * This method controls if there are enough player to continue the game
     */
    void numberCheck() {
        int activePlayers=getNicknameList().size();
        for (ClientInfo clientInfo : getNicknameList().values()){
            if(clientInfo.suspended){
                activePlayers--;
            }
        }
        if(activePlayers<this.minimumPlayer){
            this.match.endGame();
            this.endGame();
        }
    }

    /**
     * This method end the game and send the winner to all the players
     */
    private void endGame() {
        String endGame= "ENG-";
        for(Player winner : this.match.getWinner()){
            endGame =endGame.concat(winner.getNickname()).concat("-").concat(Integer.toString(winner.getPoints())).concat(";");
        }
        for (ClientInfo clientInfo : getNicknameList().values()){
            clientInfo.setState(ClientInfo.State.END_GAME);
            sendString(endGame, clientInfo.clientHandler);
        }
    }

    /**
     * This class is used for deserialize the configuration data
     */
    private class Configuration{
        private int board;
        private int skulls;
        private boolean frenzy;
        private int timer;
        private ArrayList<Integer> availableBoards;
        private ArrayList<Integer> availableSkulls;
        private String mode;
        private ArrayList<String> availableColors;
        private int timerTurn;
        private int minimumPlayer;
    }

    /**
     * This method is used for synchronized access to nicknameList
     * @return The nicknameList
     */
    public Map<String, ClientInfo> getNicknameList() {
        synchronized (nicknameListLock){
            return nicknameList;
        }
    }
}