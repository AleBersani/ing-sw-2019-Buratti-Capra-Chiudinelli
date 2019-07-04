package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.map.AmmoTile;
import it.polimi.ingsw.model.map.Square;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents one single player
 */
public class Player implements Serializable {
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
     * This attribute is the damage list where there are the damages from other players
     */
    private ArrayList<Player> damage = new ArrayList<>();
    /**
     * This attribute is the list of marks from the other players
     */
    private ArrayList<Player> mark = new ArrayList<>();
    /**
     * This attribute is the list of power ups the player has
     */
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    /**
     * This attribute is the list of weapons the player has
     */
    private ArrayList<Weapon> weapons = new ArrayList<>();
    /**
     * This attribute indicates if the player moves first
     */
    private boolean first;
    /**
     * This attribute indicates if the player killed as last
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
     * This attribute indicates the turn of the player
     */
    private Turn turn;
    /**
     *  This attribute indicates if the plank is turned or not
     */
    private boolean turnedPlank;
    /**
     * This constant represents the maximum movement that can be done to run during a not frenzy turn
     */
    private int maxRun = 3;
    /**
     * This constant represents the maximum movement that can be done to run during a frenzy turn
     */
    private int maxRunFrenzy = 4;
    /**
     * This constant represents the maximum quantity of type of ammo,weapons and power ups
     */
    private int maxSize = 3;

    private boolean wounded;

    /**
     * This constructor instantiates the player
     * @param first This parameter defines if the player is the first to play or not
     * @param color This parameter defines the color of the player
     * @param nickname This parameter defines the name of the player
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
        this.turnedPlank=false;
        this.wounded=false;
    }

    /**
     * This method is the run action that can be done in a not-frenzy turn
     * @param destination This parameter is the final destination where the player wants to move
     * @throws InvalidDestinationException This exception means that the player can't reach the destination
     */
    public void run(Square destination) throws InvalidDestinationException {
        if ((this.position.calcDist(destination) <= (this.turn.isFrenzy()? maxRunFrenzy : maxRun) && !this.position.equals(destination))) {
            this.position.leaves(this);
            this.position = destination;
            destination.arrives(this);
        }
        else {
            throw new InvalidDestinationException();
        }
        this.turn.setActionCounter((this.turn.getActionCounter() + 1));
    }

    /**
     * This method is the grab action that can be done to take ammo
     * @param destination This parameter is the final destination where the player wants to move to grab the ammo or the weapon
     * @throws InvalidDestinationException This exception means that the player can't reach the destination
     * @throws NullAmmoException This exception means that the player didn't grab anything
     * @throws ElementNotFoundException This exception means that there isn't a takeable element
     */
    public void grab(Square destination) throws InvalidDestinationException, NullAmmoException, ElementNotFoundException {
        AmmoTile ammoTile;
        if (this.position.calcDist(destination) <= 1 + isOnAdrenalineGrab() || this.position.calcDist(destination) <= 1 + onFrenzy() + onlyFrenzyAction()) {
            ammoTile = destination.grabAmmo();
            if (ammoTile != null) {
                this.redAmmo = this.redAmmo + ammoTile.getRed();
                if (this.redAmmo > 3)
                    this.redAmmo = 3;
                this.blueAmmo = this.blueAmmo + ammoTile.getBlue();
                if (this.blueAmmo > 3)
                    this.blueAmmo = 3;
                this.yellowAmmo = this.yellowAmmo + ammoTile.getYellow();
                if (this.yellowAmmo > 3)
                    this.yellowAmmo = 3;
                if (ammoTile.getPowerUp() == 1)
                    try {
                        draw();
                    }
                    catch (MaxHandSizeException e){

                    }
            } else
                throw new NullAmmoException();
            this.position.leaves(this);
            this.position = destination;
            destination.arrives(this);
        } else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter() + 1));
    }

    /**
     * This method is the grab action that can be done to take a chosen weapon
     * @param destination This parameter is the final destination where the player wants to move to grab the ammo or the weapon
     * @param position This parameter is the position of the weapon that the player wants to pick
     * @throws ElementNotFoundException This exception means that there isn't a takeable element
     * @throws MaxHandWeaponSizeException This exception means that the player has already reached the maximum number of weapons in a hand
     */
    public void grabWeapon(Square destination, int position) throws ElementNotFoundException, MaxHandWeaponSizeException {
        Weapon weapon;
        weapon=destination.grabWeapon(position);
        this.weapons.add(weapon);
        this.position.leaves(this);
        this.position = destination;
        destination.arrives(this);
        this.turn.setActionCounter((this.turn.getActionCounter() + 1));
        if (this.weapons.size() == 4) {
            throw new MaxHandWeaponSizeException();
        }
    }


    /**
     * This method define the shoot action that the player can do on a not-frenzy turn
     * @param weapon This parameter indicates the weapon that the player has chosen to fire with
     * @param destination This parameter indicates the final destination that the player wants to reach for fire
     * @param target This parameter indicates the target (Room,Squares,Players) of the shoot action
     * @throws NotLoadedException This exception means the weapon is not loaded
     * @throws InvalidDestinationException This exception means that the player can't reach the chosen destination
     * @throws InvalidTargetException This exception means that there are is no valid target chosen
     * @throws NotThisKindOfWeapon This exception means that the type of fire is not permitted by that weapon
     * @throws NoAmmoException This exception means that the player doesn't have enough ammo to buy a weapon
     * @throws NoOwnerException This exception means the weapon is not loaded
     */
    public void shoot(Weapon weapon, Square destination, ArrayList<TargetParameter> target) throws NotLoadedException, InvalidDestinationException, InvalidTargetException, NotThisKindOfWeapon, NoAmmoException, NoOwnerException {
        if(isOnAdrenalineShoot() && !this.turn.isFrenzy()) {
            if (this.position.calcDist(destination) <= 1) {
                this.position.leaves(this);
                this.position = destination;
                destination.arrives(this);
                shootType(weapon, target);
            }
            else {
                throw new InvalidDestinationException();
            }
        }
        else {
            shootType(weapon,target);
        }
    }

    /**
     * This method calls other methods depends of the type of fire the player choose
     * @param weapon This parameter is the weapon which the player wants to shoot with
     * @param target This is the target that the player wants to shoot
     * @throws InvalidTargetException This exception means that there are is no valid target chosen
     * @throws NoOwnerException This exception means that the owner not exists
     * @throws NotThisKindOfWeapon This exception means that the type of fire is not permitted by that weapon
     * @throws NoAmmoException This exception means that the player doesn't have enough ammo to buy a weapon
     * @throws NotLoadedException This exception means the weapon is not loaded
     */
    private void shootType(Weapon weapon,ArrayList<TargetParameter> target) throws InvalidTargetException, NoOwnerException, NotThisKindOfWeapon, NoAmmoException, NotLoadedException {
        int which=0;
        String[] parts = target.get(0).getTypeOfFire().split("-");
        String part1 = parts[0];
        if(!target.get(0).getTypeOfFire().equals("Base") && !target.get(0).getTypeOfFire().equals("Alternative")) {
            String part2 = parts[1];
            which = Integer.parseInt(part2);
        }
        if (weapon.isLoad()) {
            switch(part1) {
                case ("Base"): {
                    weapon.fire(target);
                    break;
                }
                case ("Alternative"): {
                    weapon.fireAlternative(target);
                    break;
                }
                case ("Optional"): {
                    weapon.fireOptional(target, which);
                    break;
                }
            }
        } else
            throw new NotLoadedException();
    }

    /**
     * This method sets the weapon unload and increment the action counter
     * @param weapon This parameter is the weapon that will be unloaded
     */
    public void endShoot(Weapon weapon){
        for(ArrayList<Player> target : weapon.getPreviousTarget()){
            target.clear();
        }
        weapon.setLoad(false);
        this.turn.setActionCounter(this.turn.getActionCounter()+1);
    }

    /**
     * This method is the power up use action
     * @param powerUp This parameter is the selected power up to be used
     * @param target This parameter is the target of the power up effect
     * @throws InvalidTargetException This exception means that there is no valid target chosen
     * @throws OnResponseException This exception is thrown when you try to use a power up that is usable only on response of another action
     */
    public void usePowerUp(PowerUp powerUp, TargetParameter target) throws InvalidTargetException, NoOwnerException, OnResponseException {
        if (this.powerUps.contains(powerUp) && !powerUp.getOnResponse()) {
            powerUp.useEffect(target,null);
            discard(powerUp);
        }
        else throw new OnResponseException();
    }

    /**
     * This method allows to answer if the player can see another target player
     * @param target This parameter is the target player
     * @return true is the player can see the target player, false in the other case.
     */
    boolean canSee(Player target) {
        int i;
        if (this.position.getRoom() == target.position.getRoom())
            return true;
        for (i = 0; i < this.position.getDoors().size(); i++) {
            if (this.position.getDoors().get(i).getRoom() == target.position.getRoom())
                return true;
        }
        return false;
    }

    /**
     * This method allows the player to reload the weapon, if the weapon is not already loaded
     * @param weapon This parameter is the weapon that the player wants to reload
     * @throws LoadedException This exception means that the weapon is already load
     */
    public void reload(Weapon weapon) throws LoadedException{
        if (!weapon.isLoad()){
                weapon.reload();
            }
        else{
            throw new LoadedException();
        }
    }

    /**
     * This method pay the ammo for the weapon or the effect of the shoot
     * @param costBlue This parameter is the cost blue of the effect or the weapon
     * @param costRed This parameter is the cost red of the effect or the weapon
     * @param costYellow This parameter is the cost yellow of the effect or the weapon
     * @param powerUps This parameter is the list of the power ups that the player wants to use as ammo
     * @throws WrongPowerUpException This exception means that the wrong selected power up
     * @throws NoAmmoException This exception means that the player doesn't have the ammo
     */
    public void pay(int costBlue, int costRed, int costYellow, ArrayList<PowerUp> powerUps) throws WrongPowerUpException, NoAmmoException {
        if(powerUps!= null && !powerUps.isEmpty()) {
            for (PowerUp powerUp : powerUps) {
                switch (powerUp.getColor()) {
                    case "blue": {
                        costBlue--;
                        break;
                    }
                    case "red": {
                        costRed--;
                        break;
                    }
                    case "yellow": {
                        costYellow--;
                        break;
                    }
                }
            }
        }
        if(costBlue<0 || costRed<0 || costYellow<0){
            throw new WrongPowerUpException();
        }
        if(this.blueAmmo<costBlue || this.redAmmo<costRed || this.yellowAmmo<costYellow){
            throw new NoAmmoException();
        }
        this.blueAmmo= this.blueAmmo-costBlue;
        this.redAmmo= this.redAmmo-costRed;
        this.yellowAmmo= this.yellowAmmo-costYellow;
        if(powerUps!= null && !powerUps.isEmpty()) {
            for (int i = 0;i<powerUps.size();) {
                this.discard(powerUps.get(i));
                powerUps.remove(i);
            }
        }
    }

    /**
     * This method allows the player to draw a power up from the deck of power ups
     * @throws MaxHandSizeException This exception means that the player has more then three cards in hand
     */
    public void draw() throws MaxHandSizeException {
        if (this.powerUps.size() >= maxSize)
            throw new MaxHandSizeException();
        this.powerUps.add(getTurn().getMatch().getBoard().nextPowerUp());

    }

    /**
     * This method is a draw action but doesn't throw any exception
     */
    public void forceDraw() {
        this.powerUps.add(getTurn().getMatch().getBoard().nextPowerUp());
    }

    /**
     * This method allows the player to discard a selected card from the hand
     * @param powerUp This parameter is the selected power up that the player wants to discard
     */
    public void discard(PowerUp powerUp) {
        if (this.powerUps.contains(powerUp)) {
            this.powerUps.remove(powerUp);
        }
    }

    /**
     * This method allows the player to spawn from a selected power up's color
     * @param powerUp This parameter is the power up that the player wants to discard
     * @throws NotFoundException This exception means that is not found the spawn point
     */
    public void spawn(PowerUp powerUp) throws NotFoundException {
        if(this.position != null) {
            this.position.leaves(this);
        }
        this.position = turn.getMatch().getBoard().findSpawnPoint(powerUp.getColor());
        turn.getMatch().getBoard().findSpawnPoint(powerUp.getColor()).arrives(this);
        discard(powerUp);
    }

    /**
     * This method add the player that is dead in this turn to a list
     */
    public void dead() {
        turn.addDead(this);
    }

    /**
     * This method allows to add the damage counters to this player
     * @param damage  This parameter is the number of damage dealt by the shooter
     * @param shooter This parameter is the player who shoot
     * @param markActivator This parameter is true if the damage should convert the marks in damages, false otherwise
     */
    public void wound(int damage, Player shooter, boolean markActivator) {
        int i;
        for (i = 0; i < damage; i++) {
            if (this.damage.size() < 12) {
                this.damage.add(shooter);
                this.damageCounter++;
            }
        }
        if(markActivator) {
            for (i = 0; i < this.mark.size(); i++) {
                if (this.mark.get(i) == shooter) {
                    if (this.damage.size() < 12) {
                        this.damage.add(shooter);
                        this.damageCounter++;
                    }
                    this.mark.remove(i);
                    i--;
                }
            }
        }
        if (this.damageCounter > 10) {
            this.dead();
        }
        wounded=true;
    }

    /**
     * This method allows to add the marks to this player
     * @param mark This parameter is the number of marks to be added to the player
     * @param shooter This parameter is the player who applies these marks
     */
    public void marked(int mark, Player shooter) {
        int i;
        int counter;
        for (i = 0, counter = 0; i < this.mark.size(); i++)
            if (this.mark.get(i) == shooter)
                counter++;
        for (i = 0; i < mark && counter < 3; i++, counter++)
            this.mark.add(this.mark.size(), shooter);
    }

    /**
     * This method allows the player to do the movement before shooting
     * @param destination This parameter is the final destination square of the player
     * @throws InvalidDestinationException This exception means that the square is invalid
     */
    public void movementShootFrenzy(Square destination) throws InvalidDestinationException{
        if (this.position.calcDist(destination) <= 1 + onlyFrenzyAction()) {
        this.position.leaves(this);
        this.position=destination;
        destination.arrives(this);
        }
        else
            throw new InvalidDestinationException();

    }

    /**
     * This method unlock the first adrenaline improvement
     * @return 1 if the player has 3 or more damage counters, 0 otherwise
     */
    private int isOnAdrenalineGrab() {
        if (this.damageCounter >= 3 && !this.turnedPlank)
            return 1;
        return 0;
    }

    /**
     * This method unlock the second adrenaline improvement
     * @return true if the player has 6 or more damage counters, false otherwise
     */
    private boolean isOnAdrenalineShoot() {
        return this.damageCounter >= 6 && !this.turnedPlank;
    }

    /**
     * This method return an integer for upgrade the movement in a frenzy turn (used for compress grab)
     * @return 1 if it's a frenzy turn, 0 otherwise
     */
    private int onFrenzy() {
        if (this.turn.isFrenzy())
            return 1;
        return 0;
    }

    /**
     * This method return and integer for upgrade the movement in the frenzy action
     * @return 1 if the player is the first player or if his final turn comes later then the first player, 0 otherwise
     */
    public int onlyFrenzyAction() {
        int lastcont = 0;
        int i;
        if (this.turn.isFrenzy()) {
            for (i = 0; i < this.turn.getMatch().getPlayers().size(); i++)
                if (this.turn.getMatch().getPlayers().get(i).isLastKill())
                    lastcont = i;
            for (i = 0; i < this.turn.getMatch().getPlayers().size(); i++)
                if (this.turn.getMatch().getPlayers().get(i)==this && i <= lastcont)
                    return 1;
        }
        return 0;
    }

    /**
     * This method returns the number of skulls of the player
     * @return The number of skulls that the player owns
     */
    int getSkull() { return skull; }

    /**
     * This method sets the number of skulls of the player
     * @param skull This parameter is the number of skulls that the player'll have
     */
    void setSkull(int skull) { this.skull = skull; }

    /**
     * This method returns the number of blue ammo that the player owns
     * @return The number of blue ammo that the player has
     */
    int getBlueAmmo() { return blueAmmo; }

    /**
     * This method sets the number of blue ammo of the player
     * @param blueAmmo This parameter is the number of blue ammo that the player'll have
     */
    public void setBlueAmmo(int blueAmmo) { this.blueAmmo = blueAmmo; }

    /**
     * This method returns the number of red ammo that the player owns
     * @return The number of red ammo that the player has
     */
    int getRedAmmo() { return redAmmo; }

    /**
     * This method sets the number of red ammo of the player
     * @param redAmmo This parameter is the number of red ammo that the player'll have
     */
    public void setRedAmmo(int redAmmo) { this.redAmmo = redAmmo; }

    /**
     * This method returns the number of yellow ammo that the player owns
     * @return The number of yellow ammo that the player has
     */
    int getYellowAmmo() { return yellowAmmo; }

    /**
     * This method sets the number of yellow ammo of the player
     * @param yellowAmmo This parameter is the number of yellow ammo that the player'll have
     */
    public void setYellowAmmo(int yellowAmmo) { this.yellowAmmo = yellowAmmo; }

    /**
     * This method returns the total points the player has done
     * @return The amount of points that the player has earned
     */
    public int getPoints() { return points; }

    /**
     * This method set the points of the player
     * @param points This parameter is the amount of points that the player'll have
     */
    public void setPoints(int points) { this.points = points; }

    /**
     * This method returns if the plank is turned or not
     * @return True if is turned, false if not
     */
    boolean isTurnedPlank() { return turnedPlank; }

    /**
     * This method sets if the plank is turned or not
     * @param turnedPlank This parameter will sets if the plank is turned or not
     */
    void setTurnedPlank(boolean turnedPlank) { this.turnedPlank = turnedPlank; }

    /**
     * This method returns the nickname of the player
     * @return The nickname of the player
     */
    public String getNickname() { return nickname; }

    /**
     * This method returns the damage counters that the player owns
     * @return The amount of damage counters that the player has
     */
    public int getDamageCounter() { return damageCounter; }

    /**
     * This method sets the damage counters of the player
     * @param damageCounter This parameter is te number of damage counters that the player'll own
     */
    public void setDamageCounter(int damageCounter) { this.damageCounter = damageCounter; }

    /**
     * This method returns the damage list of a the player
     * @return The list of damages done by other players
     */
    public ArrayList<Player> getDamage() { return damage; }

    /**
     * This method sets a list of damages of a player
     * @param damage This parameter is the list that the player'll have
     */
    public void setDamage(ArrayList<Player> damage) { this.damage = damage; }

    /**
     * This method returns the mark list of a the player
     * @return The list of marks done by other players
     */
    public ArrayList<Player> getMark() { return mark; }

    /**
     * This method sets a list of marks of a player
     * @param mark This parameter is the list that the player'll have
     */
    public void setMark(ArrayList<Player> mark) { this.mark = mark; }

    /**
     * This method returns the power up list of the player
     * @return The list of power ups that the player has
     */
    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * This method return the weapon list of the player
     * @return The list of wepons that the player has
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * This method sets a list of power ups of a player
     * @param powerUps This parameter is the list that the player'll have
     */
    public void setPowerUps(ArrayList<PowerUp> powerUps) {
        this.powerUps = powerUps;
    }

    /**
     * This method returns if the player is the last killer of a not-frenzy turn
     * @return True if the player is the last killer, false otherwise
     */
    public boolean isLastKill() {
        return lastKill;
    }

    /**
     * This method sets if the player is the last killer or not
     * @param lastKill This parameter is true if the player'll be the last killer, false otherwise
     */
    public void setLastKill(boolean lastKill) {
        this.lastKill = lastKill;
    }

    /**
     * This method returns the color of the player
     * @return The color of the player
     */
    public String getColor() {
        return color;
    }

    /**
     * This method sets the color of the player
     * @param color This parameter is the color that the player'll have
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * This method returns the actual position of the player
     * @return The position of the player
     */
    public Square getPosition() {
        return position;
    }

    /**
     * This method sets the position of the player
     * @param position This parameter is the position that the player'll have
     */
    public void setPosition(Square position) {
        this.position = position;
    }

    /**
     * This method return the turn of the player
     * @return The turn of the player
     */
    public Turn getTurn() { return turn; }

    /**
     * This method sets the turn of the player
     * @param turn This parameter is the turn that the player'll have
     */
    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    /**
     * This method sets the player to first
     * @param first True if is the first, false otherwise
     */
    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isWounded() {
        return wounded;
    }

    public void setWounded(boolean wounded) {
        this.wounded = wounded;
    }

    /**
     * This method provide the description of the other player's character
     * @return The description of the other player's character
     */
    @Override
    public String toString(){
        String player="";
        player=player.concat("S:")
                .concat(Integer.toString(skull)).concat(";")
                .concat("Y:").concat(Integer.toString(yellowAmmo)).concat("'")
                .concat("B:").concat(Integer.toString(blueAmmo)).concat("'")
                .concat("R:").concat(Integer.toString(redAmmo)).concat("'");
        player = player.concat(";damage:;");
        for (Player p : damage) {
            player=player.concat(p.getColor()).concat("'");
        }
        player = player.concat(";mark:;");
        for (Player p : mark) {
            player=player.concat(p.getColor()).concat("'");
        }
        player=player.concat(";PowerUp:").concat(Integer.toString(powerUps.size()));
        player=player.concat(";Weapons:;");
        for (Weapon w : weapons){
            if (w.isLoad()){
                player=player.concat("notVisible").concat("'");
            }
            else {
                player=player.concat(w.getName()).concat("'");
            }
        }
        player=player.concat(";Color:").concat(color);
        player=player.concat(";TurnedPlank:").concat(String.valueOf(turnedPlank));
        player=player.concat(";Frenzy:").concat(String.valueOf(turn.isFrenzy()));
        return player;
    }

    /**
     * This method provide the description of your character
     * @return the description of your character
     */
    public String describe() {
        String descr= toString();
        descr=descr.concat(";Points:").concat(Integer.toString(points));
        descr=descr.concat(";YourWeapons:;");
        for (Weapon w : weapons){
            descr=descr.concat(w.getName()).concat(":").concat(String.valueOf(w.isLoad())).concat("'");
        }
        descr=descr.concat(";YourPowerUps:;");
        for (PowerUp p : powerUps){
            descr=descr.concat(p.getName()).concat(":").concat(p.getColor()).concat("'");
        }
        descr=descr.concat(";");
        descr= descr.concat("ActionsFrenzy:").concat(Integer.toString(onlyFrenzyAction()==1 ? 1 : 2));
        return descr;
    }
}