package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.*;
import it.polimi.ingsw.Model.Cards.Effects.Effect;
import it.polimi.ingsw.Model.Cards.Effects.EffectVsPlayer;
import it.polimi.ingsw.Model.Cards.PowerUp;
import it.polimi.ingsw.Model.Cards.Weapon;
import it.polimi.ingsw.Model.Map.Square;

import java.util.ArrayList;

public class Player {
    private int skull, blueAmmo, redAmmo, yellowAmmo, points, damageCounter;
    private ArrayList<Player> damage = new ArrayList<Player>();
    private ArrayList<Player> mark = new ArrayList<Player>();
    private ArrayList<PowerUp> powerUps= new ArrayList<PowerUp>();
    private ArrayList<Weapon> weapons= new ArrayList<Weapon>();
    private boolean first, lastKill;
    private String color, nickname;
    private Square position, previousPosition;
    private Turn turn;
    int maxRun=3,maxRunFrenzy=4,maxSize=3;

    public Player(boolean first, String color, String nickname) {
        this.first = first;
        this.color = color;
        this.nickname = nickname;
        this.skull = 0;
        this.blueAmmo = 1;
        this.redAmmo = 1;
        this.yellowAmmo = 1;
        this.points = 0;
        this.damageCounter = 0;
    }

    public void run(Square destination) throws InvalidDestinationException {
        if(this.position.calcDist(destination) <= maxRun)
            this.position = destination;
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }

    /* TODO GRAB
    public void grab(Square destination) throws InvalidDestinationException, NothingToGrabException, NoAmmoException {
        if(this.position.calcDist(destination) <= 1+isOnAdrenalineGrab())
            if(destination.require() == false) {
                if (destination.getClass() == AmmoPoint.class){
                    this.redAmmo = this.redAmmo + destination.getAmmo().getRed();
                    if (this.redAmmo > 3)
                        this.redAmmo = 3;
                        this.blueAmmo = this.blueAmmo + destination.getAmmo().getBlue();
                        if (this.blueAmmo > 3)
                            this.blueAmmo = 3;
                        this.yellowAmmo = this.yellowAmmo + destination.getAmmo().getYellow();
                        if (this.yellowAmmo > 3)
                            this.yellowAmmo = 3;
                        if (destination.getAmmo().getPowerUp() == 1)
                            draw();
                    } else
                        if (destination.getWeapons().getCostRed - isRed(destination.getWeapons()) <= this.redAmmo && destination.getWeapons().getCostBlue - isBlue(getWeapons()) <= this.blueAmmo && destination.getWeapons().getCostYellow - isYellow(getWeapons()) <= this.yellowAmmo){
                            this.weapons.add(destination.getClass().getWeapons());
                            if (this.weapons.size() == 4) {
                                this.weapons.remove();
                                destination.getWeapons().add();
                            }
                            this.redAmmo=this.redAmmo - (destination.getWeapons().getCostRed() - isRed(destination.getWeapons());
                            this.blueAmmo=this.blueAmmo - (destination.getWeapons().getCostBlue() - isBlue(destination.getWeapons());
                            this.yellowAmmo=this.yellowAmmo - (destination.getWeapons().getCostYellow() - isYellow(destination.getWeapons());
                        }
                        else
                            throw new NoAmmoException();
                    this.position = destination;
                }
                else
                    throw new NothingToGrabException();
            else
                throw new InvalidDestinationException();
            this.turn.setActionCounter((this.turn.getActionCounter()+1));
            return;
        }
        */
    public void shoot(Weapon weapon,Square destination) throws NotLoadedException, InvalidDestinationException {
        if(isOnAdrenalineShoot()==1)
            if(this.position.calcDist(destination) <= 1)
                this.position = destination;
            else
                throw new InvalidDestinationException();
        if(weapon.isLoad())
            weapon.fire();
        else
            throw new NotLoadedException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }

    public void usePowerUp(PowerUp powerUp){
        for(int i=0;i<this.powerUps.size();i++)
            if(this.powerUps.contains(powerUp)) {
                this.powerUps.get(i).useEffect();
                discard(powerUp);
            }
    }

    public boolean canSee(Player target){
        int i;
        if(this.position.getRoom() == target.position.getRoom())
            return true;
        for(i=0;i<this.position.getDoors().size();i++){
            if(this.position.getDoors().get(i).getRoom() == target.position.getRoom())
                return true;
        }
        return false;
    }

    public void reload(Weapon weapon) throws LoadedException, NoAmmoException {
        if(!weapon.isLoad())
            if((weapon.getCostBlue() <= this.blueAmmo) && (weapon.getCostRed() <= this.redAmmo) && (weapon.getCostYellow() <= this.yellowAmmo))
                weapon.reload();
            else
                throw new NoAmmoException();
        else
            throw new LoadedException();
    }

    public void draw() throws MaxHandSizeException {
        this.powerUps.add(this.position.getRoom().getBoard().nextPowerUp());
        if(this.powerUps.size() < maxSize)
            throw new MaxHandSizeException();
    }

    public void discard(PowerUp powerUp){
        for(int i=0;i<this.powerUps.size();i++)
            if(this.powerUps.contains(powerUp)) {
                this.powerUps.remove(i);
                return;
            }
    }

    public void spawn() {
    /*    TODO SPAWN
        if(this.position == null)
            draw();
        draw();
        discard(powerUp);
        this.position=turn.getMatch().getBoard().findSpawnPoint(powerUp.getColor());
    */
    }

    public void dead(){
        turn.addDead(this);
    }

    public void wound(int damage, Player shooter){
        int i;
        for(i=0;i<damage;i++){
            if(this.damage.size()<12){
                this.damage.add(shooter);
                this.damageCounter++;
            }
        }
        for(i=0;i<this.mark.size();i++){
            if(this.mark.get(i) == shooter)
                if(this.damage.size()<12){
                    this.damage.add(shooter);
                    this.damageCounter++;
                }
            this.mark.remove(i);
        }
        if(this.damageCounter > 10)
            this.dead();
    }

    public void marked(int mark, Player shooter){
        int i,counter;
        for(i=0,counter=0;i<this.mark.size();i++)
            if(this.mark.get(i) == shooter)
                counter++;
        for(i=0;i<mark && counter<3;i++,counter++)
            this.mark.add(this.mark.size(),shooter);
    }

    public void runFrenzy(Square destination) throws InvalidDestinationException {
        if(this.position.calcDist(destination) <= maxRunFrenzy)
            this.position = destination;
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }

    public void shootFrenzy(Weapon weaponShoot,Weapon weaponReload,Square destination) throws NotLoadedException, InvalidDestinationException {
        if (this.position.calcDist(destination) <= 1+onlyFrenzyAction()) {
            this.position = destination;
            weaponReload.reload();
            if (weaponShoot.isLoad())
                weaponShoot.fire();
            else
                throw new NotLoadedException();
            }
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }

    /*  TODO FRENZY GRAB
        public void grabFrenzy(Square destination) throws InvalidDestinationException, NothingToGrabException, NoAmmoException {
            if(this.position.calcDist(destination) <= 2+onlyFrenzyAction())
                if(destination.require() == false) {
                    if (destination.getClass() == AmmoPoint.class) {
                        this.redAmmo = this.redAmmo + destination.getAmmo().getRed();
                        if (this.redAmmo > 3)
                            this.redAmmo = 3;
                        this.blueAmmo = this.blueAmmo + destination.getAmmo().getBlue();
                        if (this.blueAmmo > 3)
                            this.blueAmmo = 3;
                        this.yellowAmmo = this.yellowAmmo + destination.getAmmo().getYellow();
                        if (this.yellowAmmo > 3)
                            this.yellowAmmo = 3;
                        if (destination.getAmmo().getPowerUp() == 1)
                            draw();
                    } else
                    if (destination.getWeapons().getCostRed - isRed(destination.getWeapons()) <= this.redAmmo && destination.getWeapons().getCostBlue - isBlue(getWeapons()) <= this.blueAmmo && destination.getWeapons().getCostYellow - isYellow(getWeapons()) <= this.yellowAmmo){
                        this.weapons.add(destination.getClass().getWeapons());
                        if (this.weapons.size() == 4) {
                            this.weapons.remove();
                            destination.getWeapons().add();
                        }
                        this.redAmmo=this.redAmmo - (destination.getWeapons().getCostRed() - isRed(destination.getWeapons());
                        this.blueAmmo=this.blueAmmo - (destination.getWeapons().getCostBlue() - isBlue(destination.getWeapons());
                        this.yellowAmmo=this.yellowAmmo - (destination.getWeapons().getCostYellow() - isYellow(destination.getWeapons());
                    }
                    else
                        throw new NoAmmoException();
                    this.position = destination;
                }
                else
                    throw new NothingToGrabException();
            else
                throw new InvalidDestinationException();
            this.turn.setActionCounter((this.turn.getActionCounter()+1));
            return;
        }
        */
        /*
        private int isRed(weapons){
            if(weapons.getColor() == "Red")
                return 1;
            return 0;
        }
        private int isBlue(weapons){
            if(weapons.getColor() == "Blue")
                return 1;
            return 0;
        }
        private int isYellow(weapons){
            if(weapons.getColor() == "Yellow")
                return 1;
            return 0;
        }
        */
    private int isOnAdrenalineGrab() {
        if(this.damageCounter>=3)
            return 1;
        return 0;
    }

    private int isOnAdrenalineShoot() {
        if(this.damageCounter>=6)
            return 1;
        return 0;
    }

    private int onlyFrenzyAction() {
        int lastcont=0;
        for(int i=0;i<this.turn.getMatch().getPlayers().size();i++)
            if(this.turn.getMatch().getPlayers().get(i).isLastKill())
                lastcont=i;
        for(int i=0;i<this.turn.getMatch().getPlayers().size();i++)
            if(this.turn.getMatch().getPlayers().contains(this) && i<=lastcont)
                return 1;
        return 0;
    }

    public int getSkull() {
        return skull;
    }

    public void setSkull(int skull) {
        this.skull = skull;
    }

    public int getBlueAmmo() {
        return blueAmmo;
    }

    public void setBlueAmmo(int blueAmmo) {
        this.blueAmmo = blueAmmo;
    }

    public int getRedAmmo() {
        return redAmmo;
    }

    public void setRedAmmo(int redAmmo) {
        this.redAmmo = redAmmo;
    }

    public int getYellowAmmo() {
        return yellowAmmo;
    }

    public void setYellowAmmo(int yellowAmmo) {
        this.yellowAmmo = yellowAmmo;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDamageCounter() {
        return damageCounter;
    }

    public void setDamageCounter(int damageCounter) {
        this.damageCounter = damageCounter;
    }

    public ArrayList<Player> getDamage() {
        return damage;
    }

    public void setDamage(ArrayList<Player> damage) {
        this.damage = damage;
    }

    public ArrayList<Player> getMark() {
        return mark;
    }

    public void setMark(ArrayList<Player> mark) {
        this.mark = mark;
    }

    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(ArrayList<PowerUp> powerUps) {
        this.powerUps = powerUps;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<Weapon> weapons) {
        this.weapons = weapons;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLastKill() {
        return lastKill;
    }

    public void setLastKill(boolean lastKill) {
        this.lastKill = lastKill;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Square getPosition() {
        return position;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public Square getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Square previousPosition) {
        this.previousPosition = previousPosition;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }
}