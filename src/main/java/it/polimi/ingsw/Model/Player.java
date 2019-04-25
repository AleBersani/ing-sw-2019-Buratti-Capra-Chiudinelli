package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.*;
import it.polimi.ingsw.Model.Cards.PowerUp;
import it.polimi.ingsw.Model.Cards.Weapon;
import it.polimi.ingsw.Model.Map.Square;

import java.util.ArrayList;

/**
 * This class represent one single player
 */
public class Player {
    /**
     * This attribute is the number of skulls the player owns
     */
    private int skull;
    /**
     * This attribute is the number of blue ammo the player owns
     */
    private int blueAmmo;
    /**
     * This attribute is the number of red ammo the player owns
     */
    private int redAmmo;
    /**
     * This attribute is the number of yellow ammo the player owns
     */
    private int yellowAmmo;
    /**
     * This attribute is the number of points the player has
     */
    private int points;
    /**
     * This attribute is the number of damage the player has
     */
    private int damageCounter;
    /**
     * This attribute is the list of damage from the other players
     */
    private ArrayList<Player> damage = new ArrayList<Player>();
    /**
     * This attribute is the list of mark from the other players
     */
    private ArrayList<Player> mark = new ArrayList<Player>();
    /**
     * This attribute is the list of power up the player has
     */
    private ArrayList<PowerUp> powerUps= new ArrayList<PowerUp>();
    /**
     * This attribute is the list of weapon the player has
     */
    private ArrayList<Weapon> weapons= new ArrayList<Weapon>();
    /**
     * This attribute indicates if it is the first player of all others
     */
    private boolean first;
    /**
     * This attribute indicates if the player made the last kill
     */
    private boolean lastKill;
    /**
     * This attribute indicates the color of the player
     */
    private String color;
    /**
     * This attribute indicates the nickname of the player
     */
    private String nickname;
    /**
     * This attribute indicates the current position of the player
     */
    private Square position;
    /**
     * This attribute indicates the previous position of the player
     */
    private Square previousPosition;
    /**
     * This attribute indicates the turn of the player
     */
    private Turn turn;
    /**
     * This constant is for the maximum movement that can be done to run during a not frenzy turn
     */
    int maxRun=3;
    /**
     * This constant is for the maximum movement that can be done to run during a frenzy turn
     */
    int maxRunFrenzy=4;
    /**
     * This constant is for the maximum quantity of type of ammo,weapons and power ups
     */
    int maxSize=3;

    /**
     * This constructor instantiates the player
     * @param first This parameter is for define if the player is the first to play or not
     * @param color This parameter is for define the color of the player
     * @param nickname This parameter is for define the name of the player
     */
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

    /**
     * This method is the run action that can be done in the not frenzy turn
     * @param destination This parameter is the final destination where the player wanna move
     * @throws InvalidDestinationException This is exception means the player can't reach tje destination
     */
    public void run(Square destination) throws InvalidDestinationException {
        if(this.position.calcDist(destination) <= maxRun)
            this.position = destination;
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }

    public void grab(Square destination) throws MaxHandSizeException, NoAmmoException, MaxHandWeaponSizeException, InvalidDestinationException {
        int i=0,j=0; //TODO CONTROL CHOOSE
        if(this.position.calcDist(destination) <= 1+isOnAdrenalineGrab()) {
            try {
                destination.grabAmmo();
                this.redAmmo = this.redAmmo + destination.grabAmmo().getRed();
                if (this.redAmmo > 3)
                    this.redAmmo = 3;
                this.blueAmmo = this.blueAmmo + destination.grabAmmo().getBlue();
                if (this.blueAmmo > 3)
                    this.blueAmmo = 3;
                this.yellowAmmo = this.yellowAmmo + destination.grabAmmo().getYellow();
                if (this.yellowAmmo > 3)
                    this.yellowAmmo = 3;
                if (destination.grabAmmo().getPowerUp() == 1)
                    draw();
            } catch (ElementNotFoundException e) {
                try{
                    destination.grabWeapon(i);
                    if (destination.grabWeapon(i).getCostRed() - isRed(destination.grabWeapon(i)) <= this.redAmmo && destination.grabWeapon(i).getCostBlue() - isBlue(destination.grabWeapon(i)) <= this.blueAmmo && destination.grabWeapon(i).getCostYellow() - isYellow(destination.grabWeapon(i)) <= this.yellowAmmo){
                        this.redAmmo=this.redAmmo - (destination.grabWeapon(i).getCostRed() - isRed(destination.grabWeapon(i)));
                        this.blueAmmo=this.blueAmmo - (destination.grabWeapon(i).getCostBlue() - isBlue(destination.grabWeapon(i)));
                        this.yellowAmmo=this.yellowAmmo - (destination.grabWeapon(i).getCostYellow() - isYellow(destination.grabWeapon(i)));
                        this.weapons.add(destination.grabWeapon(i));
                        if (this.weapons.size() == 4)
                            throw new MaxHandWeaponSizeException();
                    }
                    else
                        throw new NoAmmoException();
                }
                catch (ElementNotFoundException ex) {
                }
            }
            this.position = destination;
        }
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }

    public void shoot(Weapon weapon,Square destination,TargetParameter target) throws NotLoadedException, InvalidDestinationException, InvalidTargetExcepion {
        if(isOnAdrenalineShoot()==1)
            if(this.position.calcDist(destination) <= 1)
                this.position = destination;
            else
                throw new InvalidDestinationException();
        if(weapon.isLoad())
            weapon.fire(target);
        else
            throw new NotLoadedException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }

    public void usePowerUp(PowerUp powerUp, TargetParameter target) throws InvalidTargetExcepion {
        for(int i=0;i<this.powerUps.size();i++)
            if(this.powerUps.contains(powerUp)) {
                this.powerUps.get(i).useEffect(target);
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
            if((weapon.getCostBlue() <= this.blueAmmo) && (weapon.getCostRed() <= this.redAmmo) && (weapon.getCostYellow() <= this.yellowAmmo)) {
                this.blueAmmo=this.blueAmmo-weapon.getCostBlue();
                this.redAmmo=this.redAmmo-weapon.getCostRed();
                this.yellowAmmo=this.yellowAmmo-weapon.getCostYellow();
                weapon.reload();
            }
            else
                throw new NoAmmoException();
        else
            throw new LoadedException();
    }

    public void draw() throws MaxHandSizeException {
        this.powerUps.add(getTurn().getMatch().getBoard().nextPowerUp());
        if(this.powerUps.size() > maxSize)
            throw new MaxHandSizeException();
    }

    public void discard(PowerUp powerUp){
        for(int i=0;i<this.powerUps.size();i++)
            if(this.powerUps.contains(powerUp)) {
                this.powerUps.remove(i);
                return;
            }
    }

    public void spawn(){
        /*
        if(this.position == null)
            draw();
        draw();
        this.position=turn.getMatch().getBoard().findSpawnPoint(powerUp.getColor());
        discard(powerUp);
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

    public void shootFrenzy(Weapon weaponShoot,Weapon weaponReload,Square destination,TargetParameter target) throws NotLoadedException, InvalidDestinationException, InvalidTargetExcepion {
        if (this.position.calcDist(destination) <= 1+onlyFrenzyAction()) {
            this.position = destination;
            try {
                reload(weaponReload);
            } catch (LoadedException e) {
                e.printStackTrace();
            } catch (NoAmmoException e) {
                e.printStackTrace();
            }
            if (weaponShoot.isLoad())
                weaponShoot.fire(target);
            else
                throw new NotLoadedException();
            }
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }

    public void grabFrenzy(Square destination) throws MaxHandSizeException, NoAmmoException, MaxHandWeaponSizeException, InvalidDestinationException {
        int i=0,j=0; //TODO CONTROL CHOOSE
        if(this.position.calcDist(destination) <= 2+onlyFrenzyAction()) {
            try {
                destination.grabAmmo();
                this.redAmmo = this.redAmmo + destination.grabAmmo().getRed();
                if (this.redAmmo > 3)
                    this.redAmmo = 3;
                this.blueAmmo = this.blueAmmo + destination.grabAmmo().getBlue();
                if (this.blueAmmo > 3)
                    this.blueAmmo = 3;
                this.yellowAmmo = this.yellowAmmo + destination.grabAmmo().getYellow();
                if (this.yellowAmmo > 3)
                    this.yellowAmmo = 3;
                if (destination.grabAmmo().getPowerUp() == 1)
                    draw();
            } catch (ElementNotFoundException e) {
                try{
                    destination.grabWeapon(i);
                    if (destination.grabWeapon(i).getCostRed() - isRed(destination.grabWeapon(i)) <= this.redAmmo && destination.grabWeapon(i).getCostBlue() - isBlue(destination.grabWeapon(i)) <= this.blueAmmo && destination.grabWeapon(i).getCostYellow() - isYellow(destination.grabWeapon(i)) <= this.yellowAmmo){
                        this.redAmmo=this.redAmmo - (destination.grabWeapon(i).getCostRed() - isRed(destination.grabWeapon(i)));
                        this.blueAmmo=this.blueAmmo - (destination.grabWeapon(i).getCostBlue() - isBlue(destination.grabWeapon(i)));
                        this.yellowAmmo=this.yellowAmmo - (destination.grabWeapon(i).getCostYellow() - isYellow(destination.grabWeapon(i)));
                        this.weapons.add(destination.grabWeapon(i));
                        if (this.weapons.size() == 4)
                            throw new MaxHandWeaponSizeException();
                    }
                    else
                        throw new NoAmmoException();
                }
                catch (ElementNotFoundException ex) {
                }
            }
            this.position = destination;
        }
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter()+1));
    }
    private int isRed(Weapon weapon){
        if(weapon.getColor() == "Red")
            return 1;
        return 0;
    }

    private int isBlue(Weapon weapon){
        if(weapon.getColor() == "Blue")
            return 1;
        return 0;
    }

    private int isYellow(Weapon weapon){
        if(weapon.getColor() == "Yellow")
            return 1;
        return 0;
    }

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

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }
}