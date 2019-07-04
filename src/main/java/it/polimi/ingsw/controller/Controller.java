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

public class Controller {

    private static final int MAX_ACTIONS = 2;
    private int minimumPlayer;
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
    private boolean respawning;
    private int timerTurn;
    private Timer suspending;

    public Controller(String[] args){
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
                            effectiveReload(msg, clientHandler, this.match);
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
                            offensivePowerUpResponse(clientInfoFromClientHandeler(clientHandler),
                                    clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting));
                        } catch (NotFoundException e) {
                            sendString("error", clientHandler);
                        } catch (NoResponeException e) {
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
                                if (clientInfoFromClientHandeler(clientHandler).weapon.isOptional()) {
                                    endOptionalShooting(clientHandler);
                                } else {
                                    endShooting(clientHandler, clientInfoFromClientHandeler(clientHandler).weapon);
                                }
                            } catch (NotFoundException e) {
                                sendString("error", clientHandler);
                            }
                        }
                        else {
                            ArrayList<PowerUp> usable = new ArrayList<>();
                            try {
                                for (PowerUp powerUp : playerFromNickname(clientHandler.getName(), clientInfoFromClientHandeler(clientHandler).simulation).getPowerUps()) {
                                    if (powerUp.getOnResponse() && powerUp.isOffensive()) {
                                        usable.add(powerUp);
                                    }
                                }
                                Player player = playerFromNickname(clientHandler.getName(), clientInfoFromClientHandeler(clientHandler).simulation);
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
                                usable.get(Integer.parseInt(msg.substring(ETIQUETTE).split("'")[0])).useEffect(generateTarget(msg.split(">")[0].split("'")[1].substring(0, msg.split(">")[0].split("'")[1].length() - 1), clientHandler, clientInfoFromClientHandeler(clientHandler).simulation),
                                        clientInfoFromClientHandeler(clientHandler).weapon.getPreviousTarget());
                                playerFromNickname(clientHandler.getName(), clientInfoFromClientHandeler(clientHandler).simulation).discard(usable.get(Integer.parseInt(msg.substring(ETIQUETTE).split("'")[0])));
                                if (clientInfoFromClientHandeler(clientHandler).weapon.isOptional()) {
                                    endOptionalShooting(clientHandler);
                                } else {
                                    endShooting(clientHandler, clientInfoFromClientHandeler(clientHandler).weapon);
                                }
                            } catch (NotFoundException e) {
                                sendString("error", clientHandler);
                            } catch (InvalidTargetException e) {
                                invalidTarget(clientHandler);
                            } catch (NoOwnerException e) {
                                sendString("error", clientHandler);
                            } catch (WrongPowerUpException e) {
                                try {
                                    cleanSimulation(clientInfoFromClientHandeler(clientHandler));
                                } catch (NotFoundException e1) {
                                    sendString("error", clientHandler);
                                }
                                sendString(">>>Wrong power up", clientHandler);
                            } catch (NoAmmoException e) {
                                try {
                                    cleanSimulation(clientInfoFromClientHandeler(clientHandler));
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
                            clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.GAME);
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
                            effectiveReload(msg, clientHandler, clientInfoFromClientHandeler(clientHandler).simulation);
                            updateBackground(clientInfoFromClientHandeler(clientHandler).simulation);
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

    private void effectiveReload(String msg, ClientHandler clientHandler, Match match) {
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
            } catch (NoAmmoException e) {
                reload(clientHandler);
                sendString(">>>You don't have enough ammo", clientHandler);
                return;
            } catch (WrongPowerUpException e) {
                reload(clientHandler);
                sendString(">>>Wrong PowerUps", clientHandler);
                return;
            }
            updateBackground(match);
        }
    }

    private void invalidTarget(ClientHandler clientHandler){
        updateBackground(this.match);
        sendString(">>>Invalid target", clientHandler);
        try {
            cleanSimulation(clientInfoFromClientHandeler(clientHandler));
        } catch (NotFoundException e1) {
            sendString("error", clientHandler);
        }
    }

    private void offensivePowerUpResponse(ClientInfo clientInfo, Weapon weapon) throws NoResponeException {
        clientInfo.weapon= weapon;
        ArrayList<PowerUp> powerUps= new ArrayList<>();
        String responseRequest="RPU-";
        try {
            for(PowerUp p : playerFromNickname(clientInfo.clientHandler.getName(), this.match).getPowerUps()){
                if(p.getOnResponse() && p.isOffensive()){
                    powerUps.add(p);
                    responseRequest=responseRequest.concat(p.getName()).concat(":").concat(p.getColor()).concat(";");
                }
            }
            if(responseRequest.equals("RPU-")){
                throw new NoResponeException();
            }
            clientInfo.setState(ClientInfo.State.RESPONSE_OFFENSIVE_PU);
            sendString(responseRequest,clientInfo.clientHandler);

        } catch (NotFoundException e) {
            sendString("error", clientInfo.clientHandler);
        }
    }

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

    private void endOptionalShooting(ClientHandler clientHandler) {
        boolean ok=false;
        try {
            ClientInfo clientInfo = clientInfoFromClientHandeler(clientHandler);
            for (String order :((WeaponOptional)playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting)).getOrder()){
                String actualOrder=clientInfo.shootingOptionals.substring(0,clientInfo.shootingOptionals.length()-1);
                if(order.equals(actualOrder)){
                    ok=true;
                }
            }

            if(ok){
                this.match=clientInfoFromClientHandeler(clientHandler).simulation;
                playerFromNickname(clientHandler.getName(), this.match).endShoot(playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting));
                clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.GAME);
                clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting= null;
                clientInfoFromClientHandeler(clientHandler).shootingOptionals="";
                clientInfoFromClientHandeler(clientHandler).simulation=null;
                updateBackground(this.match);
                defensivePowerUpResponse(clientHandler);
                lifeCycle(clientHandler);
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

    private void defensivePowerUpResponse(ClientHandler clientHandler) {
        Boolean toUse=false;
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

    private void layWeapon(int weaponPos, ClientHandler clientHandler) {
        try {
            if(weaponPos<playerFromNickname(clientHandler.getName(), this.match).getWeapons().size()-1) {
                Player player = playerFromNickname(clientHandler.getName(), this.match);
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

    private void startShootingFrenzy(ClientHandler clientHandler, String msg) {
        try {
            int x= Integer.parseInt(msg.substring(ETIQUETTE).split(":")[0]);
            int y= Integer.parseInt(msg.substring(ETIQUETTE).split(":")[1]);
            ClientInfo clientInfo= clientInfoFromClientHandeler(clientHandler);
            clientInfo.setState(ClientInfo.State.SHOOTING_FRENZY);
            clientInfo.simulation=deepClone(this.match);
            playerFromNickname(clientHandler.getName(), clientInfo.simulation).movementShootFrenzy(clientInfo.simulation.getBoard().find(x,y));
            updateBackground(clientInfo.simulation);
            reload(clientHandler);
        }
        catch (InvalidDestinationException e) {
            try {
                cleanSimulation(clientInfoFromClientHandeler(clientHandler));
            } catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
            sendString(">>>Invalid destination", clientHandler);
        }
        catch (NotFoundException | IOException | ClassNotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    private void targetRequestWeapon(ClientHandler clientHandler, String msg) {
        String targetRequest="TRW-";
        try {
            clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.TARGETING);
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

    private void targetRequestPU(ClientHandler clientHandler, String msg) {
        try {
            clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.TARGETING);
            sendString("TRG-"+playerFromNickname(clientHandler.getName(), this.match).getPowerUps().get(Integer.parseInt(msg.substring(ETIQUETTE))).getEffect().getDescription(), clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

    private void shootingAction(ClientHandler clientHandler, String msg){
        ArrayList<TargetParameter> targetParameters = new ArrayList<>();
        String[] data = msg.split(">")[0].split("'");
        try {
            if(clientInfoFromClientHandeler(clientHandler).simulation == null) {
                clientInfoFromClientHandeler(clientHandler).simulation = deepClone(match);
            }
            for (String target : data[2].split(">")[0].split(";")) {
                TargetParameter temp =generateTarget(target, clientHandler, clientInfoFromClientHandeler(clientHandler).simulation);
                if(temp != null) {
                    targetParameters.add(temp);
                }
            }
            if(!targetParameters.isEmpty()){
                Weapon weapon=clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0]));
                if(targetParameters.get(0).getTypeOfFire().equals("Alternative")){
                    playerFromNickname(clientHandler.getName(), clientInfoFromClientHandeler(clientHandler).simulation).pay(
                            ((WeaponAlternative)weapon).getAlternativeEffect().get(0).getCostBlue(),
                            ((WeaponAlternative)weapon).getAlternativeEffect().get(0).getCostRed(),
                            ((WeaponAlternative)weapon).getAlternativeEffect().get(0).getCostYellow(),
                            generatePowerUpPayment("ADP-"+msg,clientHandler, clientInfoFromClientHandeler(clientHandler).simulation)
                            );
                }
                else if(targetParameters.get(0).getTypeOfFire().startsWith("Optional-")){
                    String typeOfFire=targetParameters.get(0).getTypeOfFire();
                    playerFromNickname(clientHandler.getName(), clientInfoFromClientHandeler(clientHandler).simulation).pay(
                            ((WeaponOptional)weapon).getOptionalEffect().get(Integer.parseInt(typeOfFire.split("-")[1])).get(0).getCostBlue(),
                            ((WeaponOptional)weapon).getOptionalEffect().get(Integer.parseInt(typeOfFire.split("-")[1])).get(0).getCostRed(),
                            ((WeaponOptional)weapon).getOptionalEffect().get(Integer.parseInt(typeOfFire.split("-")[1])).get(0).getCostYellow(),
                            generatePowerUpPayment("ADP-"+msg,clientHandler, clientInfoFromClientHandeler(clientHandler).simulation)
                    );
                }
                clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().shoot(weapon,
                        data[1].equals(" ") ? playerFromNickname(clientHandler.getName(), this.match).getPosition() : clientInfoFromClientHandeler(clientHandler).simulation.getBoard().find(Integer.parseInt(data[1].split(":")[0]), Integer.parseInt(data[1].split(":")[1])),
                        targetParameters);
                if(!clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])).isOptional()) {
                    try {
                        offensivePowerUpResponse(clientInfoFromClientHandeler(clientHandler),clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])));
                    }
                    catch (NoResponeException e) {
                        endShooting(clientHandler, clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().getWeapons().get(Integer.parseInt(data[0])));
                    }
                }
                else {
                    clientInfoFromClientHandeler(clientHandler).shootingOptionals =
                            clientInfoFromClientHandeler(clientHandler).shootingOptionals.concat(
                                    targetParameters.get(0).getTypeOfFire().contains("-") ?
                                            Integer.toString(Integer.parseInt(targetParameters.get(0).getTypeOfFire().split("-")[1]) + 1) :
                                            "0").concat("-");
                    clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting = Integer.parseInt(data[0]);
                    clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.OPTIONAL_WEAPON_SHOOTING);
                    updateBackground(clientInfoFromClientHandeler(clientHandler).simulation);
                 String possibleOrder = "OWS-";
                    boolean ok = false;
                    for (String order : ((WeaponOptional) playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting)).getOrder()) {
                        if (order.startsWith(clientInfoFromClientHandeler(clientHandler).shootingOptionals)) {
                            possibleOrder = possibleOrder.concat(order.substring(clientInfoFromClientHandeler(clientHandler).shootingOptionals.length())).concat(";");
                         ok = true;
                        }
                    }
                    if (!ok) {
                        try {
                            offensivePowerUpResponse(clientInfoFromClientHandeler(clientHandler),playerFromNickname(clientHandler.getName(), this.match).getWeapons().get(clientInfoFromClientHandeler(clientHandler).optionalWeaponShooting));
                        }
                        catch (NoResponeException e) {
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

        } catch (NoOwnerException e) {
            sendString("error", clientHandler);
        } catch (IOException e) {
            sendString("error", clientHandler);
        } catch (ClassNotFoundException e) {
            sendString("error", clientHandler);
        } catch (NotLoadedException e) {
            updateBackground(this.match);
            sendString(">>>This weapon is unloaded", clientHandler);
            try {
                cleanSimulation(clientInfoFromClientHandeler(clientHandler));
            }
            catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }

        } catch (InvalidDestinationException e) {
            updateBackground(this.match);
            sendString(">>>Invalid destination", clientHandler);
            try {
                cleanSimulation(clientInfoFromClientHandeler(clientHandler));
            }
            catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            sendString("error", clientHandler);
        } catch (NoAmmoException e) {
            updateBackground(this.match);
            sendString(">>>You don't have enough ammo", clientHandler);
            try {
                cleanSimulation(clientInfoFromClientHandeler(clientHandler));
            }
            catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        } catch (WrongPowerUpException e) {
            updateBackground(this.match);
            sendString(">>>Wrong PowerUps", clientHandler);
            try {
                cleanSimulation(clientInfoFromClientHandeler(clientHandler));
            }
            catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
        }

    }
    private void endShooting(ClientHandler clientHandler, Weapon weapon){
        try {
            clientInfoFromClientHandeler(clientHandler).simulation.getTurn().getCurrent().endShoot(weapon);
            match = clientInfoFromClientHandeler(clientHandler).simulation;
            updateBackground(this.match);
            cleanSimulation(clientInfoFromClientHandeler(clientHandler));
            defensivePowerUpResponse(clientHandler);
        }
        catch (NotFoundException e){
            sendString("error", clientHandler);
        }
    }

    private void cleanSimulation(ClientInfo clientInfo){
        clientInfo.simulation=null;
        clientInfo.shootingOptionals= "";
        clientInfo.setState(ClientInfo.State.GAME);
        lifeCycle(clientInfo.clientHandler);
    }

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
                    clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.LAY_WEAPON);
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


    private void powerUpAction(ClientHandler clientHandler, String msg){
        try {
            TargetParameter target = generateTarget(msg.split("'")[1].replace(";",""), clientHandler,this.match);
            if(target!=null) {
                playerFromNickname(clientHandler.getName(), this.match).usePowerUp(playerFromNickname(clientHandler.getName(), this.match).getPowerUps().get(Integer.parseInt(msg.split("'")[0])), target);
            }
            updateBackground(this.match);
        } catch (InvalidTargetException e) {
            invalidTarget(clientHandler);
        } catch (NoOwnerException e) {
            sendString("error", clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        } catch (OnResponseException e) {
            updateBackground(this.match);
            sendString(">>>This power up can be used only on response of another action", clientHandler);
            try {
                clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.GAME);
            } catch (NotFoundException e1) {
                sendString("error", clientHandler);
            }
            lifeCycle(clientHandler);
        }

        try {
            clientInfoFromClientHandeler(clientHandler).setState(ClientInfo.State.GAME);
            lifeCycle(clientHandler);
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }

    }

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

    private void waitingRoom(ClientHandler clientHandler){
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
            try {
                suspend(clientHandler);
            } catch (NotFoundException e) {
                sendString("error", clientHandler);
            }
        }
        else {
            getNicknameList().remove(clientHandler.getName());
        }
        for (ClientInfo clientInfo : getNicknameList().values()) {
            sendString(">>>" + clientHandler.getName() + " disconnected", clientInfo.clientHandler);
            if (clientInfo.state.equals(ClientInfo.State.WAIT)) {
                this.waitingRoom(clientInfo.clientHandler);
            }
        }

        server.print(clientHandler.getName() + " disconnected");
    }

    private void suspend(ClientHandler clientHandler) throws NotFoundException {
        ClientInfo clientInfo = clientInfoFromClientHandeler(clientHandler);
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

    private void lifeCycle(ClientHandler actual) {
        try {
            if(match.getTurn().getActionCounter()<(playerFromNickname(actual.getName(), this.match).onlyFrenzyAction()==1?1:2)) {
                sendString("INS-", actual);
            }
            else {
                sendString("END-Use a powerUp or end turn", actual);
                try {
                    clientInfoFromClientHandeler(actual).setState(ClientInfo.State.END);
                } catch (NotFoundException e) {
                    sendString("error", actual);
                }

            }
        } catch (NotFoundException e) {
            sendString("error", actual);
        }

    }

    private String powerUps(Player player){
        String powerUps="";
        for(int i=0; i<player.getPowerUps().size();i++) {
            powerUps=powerUps.concat(player.getPowerUps().get(i).getName())
                    .concat(":").concat(player.getPowerUps().get(i).getColor())
                    .concat("'");
        }
        return powerUps;
    }

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

    private String killshotTrackDescriptor(Match match) {
        String killshotTrackDescriptor= "BGD-KLL-".concat(Integer.toString(skulls)).concat(";");
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

    private Player playerFromNickname(String nickname, Match match) throws NotFoundException {
        for (Player player : match.getPlayers()){
            if(nickname.equals(player.getNickname())){
                return player;
            }
        }
        throw (new NotFoundException());
    }

    private Player playerFromColor(String color, Match match) throws NotFoundException {
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

    static Match deepClone(Match object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
        return (Match) objectInputStream.readObject();
    }

    public void revertSuspension(ClientHandler clientHandler) {
        try {
            ClientInfo clientInfo= clientInfoFromClientHandeler(clientHandler);
            clientInfo.suspended=false;
        } catch (NotFoundException e) {
            sendString("error", clientHandler);
        }
    }

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

    public Map<String, ClientInfo> getNicknameList() {
        synchronized (nicknameListLock){
            return nicknameList;
        }
    }
}