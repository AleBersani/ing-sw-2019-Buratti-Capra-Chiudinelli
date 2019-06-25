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
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Controller {

    private static final int MAX_ACTIONS = 2;
    private Map<String,ClientInfo> nicknameList = new ConcurrentHashMap<>();
    private ArrayList<String> disconnected = new ArrayList<>();
    private MultiServer server;
    private int skulls;
    private boolean frenzyEn;
    private String board;
    private int timer;
    private String mode;
    private ArrayList<Integer> availableBoards;
    private ArrayList<Integer> availableSkulls;
    private ArrayList<String> availableColors;
    private boolean gameStarted;
    private Match match;
    private final Object nicknameListLock;
    private static final int ETIQUETTE = 4;
    private static final int MAX_PLAYERS_NUMBER = 5;

    public Controller(){
        Gson gSon= new Gson();
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Configuration.json")));
        Configuration configuration = gSon.fromJson(br, Configuration.class);
        this.board="/Board/Board" +Integer.toString(configuration.board)+ ".json";
        this.skulls=configuration.skulls;
        this.frenzyEn=configuration.frenzy;
        this.timer=configuration.timer;
        this.mode=configuration.mode;
        this.availableBoards=configuration.availableBoards;
        this.availableSkulls=configuration.availableSkulls;
        this.availableColors=configuration.availableColors;
        this.gameStarted=false;
        this.nicknameListLock = new Object();
    }

    private void sendString(String msg, ClientHandler clientHandler) {
        clientHandler.print(msg);
    }

    public void launchServer(int port) throws IOException {
        this.server = new MultiServer(port,this);
        server.init(timer);
        server.lifeCycle();
    }

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
                            spawn(clientHandler, playerFromNickname(clientHandler.getName()), Integer.parseInt(msg.substring(ETIQUETTE)));
                            getNicknameList().get(clientHandler.getName()).setState(ClientInfo.State.GAME);
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
                            if (!msg.substring(ETIQUETTE).equals("ignore")) {
                                try {
                                    for (String weapon : msg.substring(ETIQUETTE).split(",")) {
                                        playerFromNickname(clientHandler.getName()).getWeapons().get(Integer.parseInt(weapon)).reload();
                                    }
                                } catch (NotFoundException e) {
                                    sendString("error", clientHandler);
                                }
                                updateBackground(this.match);
                            }
                            respawn();
                            match.getTurn().endTurn();
                            clientHandler.setYourTurn(false);
                            for (ClientInfo clientInfo : nicknameList.values()) {
                                if(clientInfo.clientHandler.getName().equals(match.getTurn().getCurrent().getNickname())){
                                    clientInfo.clientHandler.setYourTurn(true);
                                    if(clientInfo.clientHandler.isFirstSpawn()){
                                        clientInfo.setState(ClientInfo.State.SPAWN);
                                        startingSpawn(clientInfo.clientHandler,match.getTurn().getCurrent());
                                    }
                                    else {
                                        clientInfo.setState(ClientInfo.State.GAME);
                                        updateBackground(this.match);
                                        sendString(">>>Now is your turn", clientInfo.clientHandler);
                                        lifeCycle(clientInfo.clientHandler);
                                    }
                                }
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
                        try {
                            shootingAction(clientHandler,clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting + "'" + msg.substring(ETIQUETTE*2).split("'")[1]);
                        } catch (NotFoundException e) {
                            sendString("error", clientHandler);
                        }
                    }

                    else if (msg.equals("ESH-")){
                        endOptionalShooting(clientHandler);
                    }
                    else {
                        sendString("Wrong Etiquette, this is OPTIONAL_WEAPON_SHOOTING", clientHandler);
                    }
                    break;
                }
            }
        }
    }

    private void endOptionalShooting(ClientHandler clientHandler) {
        boolean ok=false;
        try {
            for (String order :((WeaponOptional)playerFromNickname(clientHandler.getName()).getWeapons().get(clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting)).getOrder()){
                if(order.equals(clientInfoFromClientHandeler(clientHandler).shootingOptionals.substring(0,order.length()-1))){
                    playerFromNickname(clientHandler.getName()).endShoot(playerFromNickname(clientHandler.getName()).getWeapons().get(clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting));
                    clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.GAME);
                    clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting= null;
                    clientInfoFromClientHandeler(clientHandler).shootingOptionals="";
                    this.match=clientInfoFromClientHandeler(clientHandler).simulation;
                    clientInfoFromClientHandeler(clientHandler).simulation=null;
                    updateBackground(this.match);
                    lifeCycle(clientHandler);
                    ok=true;
                }
            }
            if(!ok){
                clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.GAME);
                clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting= null;
                clientInfoFromClientHandeler(clientHandler).shootingOptionals="";
                clientInfoFromClientHandeler(clientHandler).simulation=null;
                updateBackground(this.match);
                sendString(">>>Not correct order of effects",clientHandler);
                lifeCycle(clientHandler);
            }
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    private void reload(ClientHandler clientHandler) {
        try {
            String toLoad="";
            for (Weapon weapon : playerFromNickname(clientHandler.getName()).getWeapons()){
                if(!weapon.isLoad()){
                    toLoad=toLoad.concat(weapon.getName()).concat("'");
                }
            }
            sendString("RLD-"+ toLoad, clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    private void layWeapon(int weaponPos, ClientHandler clientHandler) {
        try {
            if(weaponPos<playerFromNickname(clientHandler.getName()).getWeapons().size()-1) {
                Player player = playerFromNickname(clientHandler.getName());
                Weapon weapon = player.getWeapons().get(weaponPos);
                player.getWeapons().remove(weapon);
                weapon.reload();
                ((SpawnPoint) player.getPosition()).getWeapons().add(weapon);
                clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.GAME);
            }
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    private void understandGameCommand(ClientHandler clientHandler, String msg) {
        switch (msg.substring(0,ETIQUETTE)){
            case "SHT-":
                targetRequestWeapon(clientHandler, msg);
                break;
            case "RUN-":
                runningAction(clientHandler, msg);
                break;
            case "GRB-":
                grabbingAction(clientHandler, msg);
                break;
            case "UPU-":
                targetRequestPU(clientHandler, msg);
                break;
            default:
                sendString("Invalid command", clientHandler);

        }
    }

    private void targetRequestWeapon(ClientHandler clientHandler, String msg) {
        String targetRequest="TRW-";
        try {
            clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.TARGETING);
            targetRequest=targetRequest.concat("Base'");
            for(Effect effect : playerFromNickname(clientHandler.getName()).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE))).getEffect()){
                targetRequest=targetRequest.concat(effect.getDescription()).concat(";");
            }
            if (playerFromNickname(clientHandler.getName()).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE))).isOptional()){
                int i=0;
                WeaponOptional weaponOptional=(WeaponOptional)playerFromNickname(clientHandler.getName()).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE)));
                for (ArrayList<Effect> arrayList : weaponOptional.getOptionalEffect()){
                    targetRequest=targetRequest.concat("'Optional-"+ i +"'");
                    for (Effect effect : arrayList){
                        targetRequest=targetRequest.concat(effect.getDescription()).concat(";");
                    }
                    i++;
                }
            }
            if (playerFromNickname(clientHandler.getName()).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE))).isAlternative()){
                WeaponAlternative weaponAlternative=(WeaponAlternative) playerFromNickname(clientHandler.getName()).getWeapons().get(Integer.parseInt(msg.substring(ETIQUETTE)));
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

    private void targetRequestPU(ClientHandler clientHandler, String msg) {
        try {
            clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.TARGETING);
            sendString("TRG-"+playerFromNickname(clientHandler.getName()).getPowerUps().get(Integer.parseInt(msg.substring(ETIQUETTE))).getEffect().getDescription(), clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    private void shootingAction(ClientHandler clientHandler, String msg){
        ArrayList<TargetParameter> targetParameters = new ArrayList<>();
        String[] data = msg.split("'");
        try {
            clientInfoFromClientHandeler(clientHandler).simulation = deepClone(match);
            for (String target : data[2].split(";")) {
                targetParameters.add(generateTarget(target, clientHandler));
            }
            clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().shoot(clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])),
                    data[1].equals("") ? null : clientInfoFromClientHandeler(clientHandler).simulation.getBoard().find(Integer.parseInt(data[1].split(":")[0]),Integer.parseInt(data[1].split(":")[1])), //TODO potrebbero esserci casini con questa null
                    targetParameters);
            if(!clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])).isOptional()) {
                clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().endShoot(clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])));
                match = clientInfoFromClientHandeler(clientHandler).simulation;
                updateBackground(this.match);
            }
            else{
                clientInfoFromClientHandeler(clientHandler).shootingOptionals.concat(
                        targetParameters.get(0).getTypeOfFire().contains("-")?
                                Integer.toString(Integer.parseInt(targetParameters.get(0).getTypeOfFire().split("-")[1]+1)) :
                                "0").concat("-");
                clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting = Integer.parseInt(data[0]);
                clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.OPTIONAL_WEAPON_SHOOTING);
                updateBackground(clientInfoFromClientHandeler(clientHandler).simulation);
                sendString("OWS-",clientHandler);
            }
        } catch (InvalidTargetException e) {
            sendString(">>>Invalid target", clientHandler);
        } catch (NoOwnerException e) {
            sendString("error", clientHandler);
        } catch (IOException e) {
            sendString("error", clientHandler);
        } catch (ClassNotFoundException e) {
            sendString("error", clientHandler);
        } catch (NotLoadedException e) {
            sendString(">>>This weapon is unload", clientHandler);
        } catch (InvalidDestinationException e) {
            sendString(">>>Invalid destination", clientHandler);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            sendString("error", clientHandler);
        } catch (NoAmmoException e) {
            sendString(">>>You don't have enough ammo", clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }

    }

    private void runningAction(ClientHandler clientHandler, String msg){
        try {
            playerFromNickname(clientHandler.getName()).run(match.getBoard().find(Integer.parseInt(msg.substring(ETIQUETTE).split(",")[0]),
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

    private void grabbingAction(ClientHandler clientHandler, String msg){
        try {
            String[] stringo =msg.substring(ETIQUETTE).split(",");
            playerFromNickname(clientHandler.getName()).grab(match.getBoard().find(Integer.parseInt(stringo[0]),
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
                playerFromNickname(clientHandler.getName()).grabWeapon(match.getBoard().find(Integer.parseInt(msg.substring(ETIQUETTE).split(",")[0]),
                        Integer.parseInt(msg.substring(ETIQUETTE).split(",")[1])),Integer.parseInt(msg.substring(ETIQUETTE).split(",")[2]));
                updateBackground(this.match);
                lifeCycle(clientHandler);
                //TODO richiamare grabWeapon con powerUp
            } catch (ElementNotFoundException e1) {
                sendString(">>>Nothing to grab", clientHandler);
            }catch (NoAmmoException e1) {
                sendString(">>>You don't have enough ammo", clientHandler);
            } catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
            catch (MaxHandWeaponSizeException e1) {
                try {
                    clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.LAY_WEAPON);
                    String toLay="WPN-Select the weapon to drop:";
                    for (int i=0; i<playerFromNickname(clientHandler.getName()).getWeapons().size()-1;i++){
                        toLay=toLay.concat(playerFromNickname(clientHandler.getName()).getWeapons().get(i).getName()).concat(";");
                    }
                    sendString(toLay, clientHandler);
                } catch (NotFoundException e2) {
                    sendString("error", clientHandler);
                }
            }
        }
    }


    private void powerUpAction(ClientHandler clientHandler, String msg){
        try {
            playerFromNickname(clientHandler.getName()).usePowerUp(playerFromNickname(clientHandler.getName()).getPowerUps().get(Integer.parseInt(msg.split("'")[0])),
                    generateTarget(msg, clientHandler));
            updateBackground(this.match);
        } catch (InvalidTargetException e) {
            sendString(">>>Invalid Target", clientHandler);
        } catch (NoOwnerException e) {
            sendString("error", clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        } catch (OnResponseException e) {
            sendString(">>>This power up can be used only on response of another action", clientHandler);
        }

        try {
            clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.GAME);
            lifeCycle(clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }

    }

    private TargetParameter generateTarget(String target,ClientHandler clientHandler) {
        String[] data =target.split("'");
        String[] parameters = data[1].split(",");
        TargetParameter targetParameter= null;
        try {
            targetParameter = new TargetParameter(
                    parameters[0].equals(" ")? null : match.getBoard().find(Integer.parseInt(parameters[0].split(":")[0]),Integer.parseInt(parameters[0].split(":")[1])),
                    playerFromNickname(clientHandler.getName()),
                    parameters[1].equals(" ")? null : playerFromColor(parameters[1]),
                    parameters[2].equals(" ")? null : match.getBoard().find(Integer.parseInt(parameters[2].split(":")[0]),Integer.parseInt(parameters[2].split(":")[1])).getRoom(),
                    parameters[3].equals(" ")? null :match.getBoard().find(Integer.parseInt(parameters[3].split(":")[0]),Integer.parseInt(parameters[3].split(":")[1])),
                    parameters[4].equals(" ")? null : parameters[4]
            );
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }

        return targetParameter;
    }


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
                    sendString("SPW-Select a power up for spawning:"+ powerUps(player),clientInfo.clientHandler);
                }
            }
        }
    }

    public void login(String command, ClientHandler clientHandler) {
        if (gameStarted) {
            if (this.disconnected.contains(command)) {
                this.disconnected.remove(command);
                this.nicknameList.put(command, new ClientInfo(clientHandler, ClientInfo.State.GAME));
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

    private void setGameRules( ClientHandler clientHandler) {
        sendString("setting",clientHandler);
        sendString("Select a board-".concat(availableBoards.toString()),clientHandler); //Select a board[1,2,3,4]
    }

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

    public void selectBoard(String msg, ClientHandler clientHandler){
        if(availableBoards.contains(Integer.parseInt(msg))) {
            board = "/Board/Board" + msg + ".json";
            sendString("You selected the board number " + msg, clientHandler);
            sendString("Select the number of skulls-".concat(availableSkulls.toString()), clientHandler);
        }
        else{
            sendString(">>>This board doesn't exist, please select another one", clientHandler);
        }
    }

    public void setSkulls(String msg, ClientHandler clientHandler){
        if(availableSkulls.contains(Integer.parseInt(msg))) {
            skulls = Integer.parseInt(msg);
            sendString("You selected " + msg + " skulls", clientHandler);
            sendString("Do you like to play with frenzy? Y/N", clientHandler);
        }
        else{
            sendString(">>>Value not valid", clientHandler);
        }
    }

    public void setFrenzy(String msg, ClientHandler clientHandler){
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

    public void waitingRoom(ClientHandler clientHandler){
        String playersNames = ":::";
        String[] allNames = getNicknameList().keySet().toArray(new String[0]);
        for(String name: allNames){
            playersNames = playersNames.concat("-" + name);
        }
        sendString(playersNames,clientHandler);

    }

    public void quit(ClientHandler clientHandler){
        clientHandler.setDisconnect(true);
        if(gameStarted) {
            disconnected.add(clientHandler.getName());
        }
        getNicknameList().remove(clientHandler.getName());
        for (ClientInfo clientInfo : getNicknameList().values()){
            sendString(">>>" + clientHandler.getName() + " disconnected", clientInfo.clientHandler);
            if(clientInfo.state.equals(ClientInfo.State.WAIT)){
                this.waitingRoom(clientInfo.clientHandler);
            }
        }
        server.print(clientHandler.getName() + " disconnected");
    }

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
            }
        }

    }

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
        sendString("SPW-Discard a power up for spawning:" +powerUps(player), actual);
    }

    private void lifeCycle(ClientHandler actual) {
        if(match.getTurn().getActionCounter()<MAX_ACTIONS) {
            sendString("Insert a command", actual);
        }
        else {
            sendString("END-Use a powerUp or end turn", actual);
            try {
                clientInfoFromClientHandeler(actual).setState(ClientInfo.State.END);
            } catch (NotFoundException e) {
                sendString("error", actual);
            }

        }

    }

    private String powerUps(Player player){
        String powerUps="";
        for(int i=0; i<player.getPowerUps().size();i++) {
            powerUps=powerUps.concat(player.getPowerUps().get(i).getName())
                    .concat(",").concat(player.getPowerUps().get(i).getColor())
                    .concat(" ");
        }
        return powerUps;
    }

    private void updateBackground(Match match){
        for (ClientInfo clientInfo: getNicknameList().values()){
            sendString(boardDescriptor(match),clientInfo.clientHandler);
            sendString(playersDescriptor(clientInfo.clientHandler, match),(clientInfo.clientHandler));
            sendString(youDescriptor(clientInfo.clientHandler, match),clientInfo.clientHandler);
            sendString(killshotTrackDescriptor(match),clientInfo.clientHandler);
        }
    }

    public void spawn(ClientHandler actual, Player player,int powerUpPosition) {
        if(!actual.isFirstSpawn()) {
            actual.setYourTurn(false);
        }
        else {
            actual.setFirstSpawn(false);
        }
        if(powerUpPosition<player.getPowerUps().size() && powerUpPosition>=0) {
            PowerUp powerUp = player.getPowerUps().get(powerUpPosition);

            try {
                player.spawn(powerUp);
            } catch (NotFoundException e) {
                sendString("SpawnPoint not found", actual);
            }
            updateBackground(this.match);
            if(actual.isYourTurn()){
                lifeCycle(actual);
            }
        }
        else {
            sendString(">>>Invalid powerUp", actual);
            sendString("SPW-Discard a power up for spawning" +powerUps(player), actual);
        }
    }

    private String killshotTrackDescriptor(Match match) {
        String killshotTrackDescriptor= "BGD-KLL-".concat(Integer.toString(match.getSkulls())).concat(";");
        for (Player p : match.getKillShotTrack()) {
            killshotTrackDescriptor = killshotTrackDescriptor.concat(p.getColor()).concat("'");
        }
        killshotTrackDescriptor=killshotTrackDescriptor.concat(";").concat(match.getDoubleOnKillShotTrack().toString());
        return killshotTrackDescriptor;
    }

    private String boardDescriptor(Match match) {
        String boardDescriptor="BGD-BRD-";
        boardDescriptor=boardDescriptor.concat(match.getBoard().getRooms().toString());
        return boardDescriptor;
    }

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

    public Player playerFromNickname(String nickname) throws NotFoundException {
        for (Player player : match.getPlayers()){
            if(nickname.equals(player.getNickname())){
                return player;
            }
        }
        throw (new NotFoundException());
    }

    private Player playerFromColor(String color) throws NotFoundException {
        for (Player player : match.getPlayers()){
            if(color.equals(player.getColor())){
                return player;
            }
        }
        throw (new NotFoundException());
    }

    private ClientInfo clientInfoFromClientHandeler(ClientHandler clientHandler) throws NotFoundException {
        for (ClientInfo clientInfo : getNicknameList().values()){
            if(clientInfo.clientHandler.equals(clientHandler)){
                return clientInfo;
            }
        }
        throw new NotFoundException();
    }

    public static Match deepClone(Match object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
        return (Match) objectInputStream.readObject();
    }

    private class Configuration{
        private int board;
        private int skulls;
        private boolean frenzy;
        private int timer;
        private ArrayList<Integer> availableBoards;
        private ArrayList<Integer> availableSkulls;
        private String mode;
        private ArrayList<String> availableColors;
    }

    public Map<String, ClientInfo> getNicknameList() {
        synchronized (nicknameListLock){
            return nicknameList;
        }
    }
}