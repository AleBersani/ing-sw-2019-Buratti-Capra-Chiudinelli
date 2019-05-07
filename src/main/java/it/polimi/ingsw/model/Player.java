package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.map.AmmoTile;
import it.polimi.ingsw.model.map.Square;

import java.util.ArrayList;

/**
 * This class represents one single player
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
     * This attribute indicates the previous position of the player
     */
    private Square previousPosition;
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
    int maxRun = 3;
    /**
     * This constant represents the maximum movement that can be done to run during a frenzy turn
     */
    int maxRunFrenzy = 4;
    /**
     * This constant represents the maximum quantity of type of ammo,weapons and power ups
     */
    int maxSize = 3;

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
    }

    /**
     * This method is the run action that can be done in a not-frenzy turn
     * @param destination This parameter is the final destination where the player wants to move
     * @throws InvalidDestinationException This exception means that the player can't reach the destination
     */
    public void run(Square destination) throws InvalidDestinationException {
        if (this.position.calcDist(destination) <= maxRun)
            this.position = destination;
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter() + 1));
    }

    /**
     * This method is the grab action that can be done to take ammo
     * @param destination This parameter is the final destination where the player wants to move to grab the ammo or the weapon
     * @throws MaxHandSizeException This exception means that the player has already reached the maximum number of cards in a hand
     * @throws InvalidDestinationException This exception means that the player can't reach the destination
     * @throws NullAmmoException This exception means that the player didn't grab anything
     * @throws ElementNotFoundException This exception means that there isn't a takeable element
     */
    public void grab(Square destination) throws MaxHandSizeException, InvalidDestinationException, NullAmmoException, ElementNotFoundException {
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
                    draw();
            } else
                throw new NullAmmoException();
            this.position = destination;
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
     * @throws NoAmmoException This exception means that the player doesn't have enough ammo to buy a weapon
     */
    public void grabWeapon(Square destination,int position) throws ElementNotFoundException, MaxHandWeaponSizeException, NoAmmoException {
        Weapon weapon;
        weapon=destination.grabWeapon(position);
        if (weapon.getCostRed() - isRed(weapon) <= this.redAmmo && weapon.getCostBlue() - isBlue(weapon) <= this.blueAmmo && weapon.getCostYellow() - isYellow(weapon) <= this.yellowAmmo){
            this.redAmmo=this.redAmmo - (weapon.getCostRed() - isRed(weapon));
            this.blueAmmo=this.blueAmmo - (weapon.getCostBlue() - isBlue(weapon));
            this.yellowAmmo=this.yellowAmmo - (weapon.getCostYellow() - isYellow(weapon));
            this.weapons.add(weapon);
            this.position = destination;
            this.turn.setActionCounter((this.turn.getActionCounter() + 1));
            if (this.weapons.size() == 4)
                throw new MaxHandWeaponSizeException();
        }
        else
            throw new NoAmmoException();
    }

    /**
     * This method define the shoot action that the player can do on a not-frenzy turn
     * @param weapon This parameter indicates the weapon that the player has chosen to fire with
     * @param destination This parameter indicates the final destination that the player wants to reach for fire
     * @param target This parameter indicates the target (Room,Squares,Players) of the shoot action
     * @throws NotLoadedException This exception means the weapon is not loaded
     * @throws InvalidDestinationException This exception means that the player can't reach the chosen destination
     * @throws InvalidTargetException This exception means that there are is no valid target chosen
     */
    public void shoot(Weapon weapon, Square destination, TargetParameter target) throws NotLoadedException, InvalidDestinationException, InvalidTargetException {
        if (isOnAdrenalineShoot() == 1)
            if (this.position.calcDist(destination) <= 1)
                this.position = destination;
            else
                throw new InvalidDestinationException();
        if (weapon.isLoad()) {
            weapon.fire(target);
            weapon.getPreviousTarget().clear();
        } else
            throw new NotLoadedException();
        this.turn.setActionCounter((this.turn.getActionCounter() + 1));
    }

    /**
     * This method is the power up use action
     * @param powerUp This parameter is the selected power up to be used
     * @param target This parameter is the target of the power up effect
     * @throws InvalidTargetException This exception means that there is no valid target chosen
     */
    public void usePowerUp(PowerUp powerUp, TargetParameter target) throws InvalidTargetException {
        for (int i = 0; i < this.powerUps.size(); i++)
            if (this.powerUps.contains(powerUp)) {
                this.powerUps.get(i).useEffect(target);
                discard(powerUp);
                return;
            }
    }

    /**
     * This method allows to answer if the player can see another target player
     * @param target This parameter is the target player
     * @return true is the player can see the target player, false in the other case.
     */
    public boolean canSee(Player target) {
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
     * This method allows the player to reload the weapon, if he has the minimum ammo to do
     * @param weapon This parameter is the weapon that the player wants to reload
     * @throws LoadedException This exception means that the weapon is already load
     * @throws NoAmmoException This exception means that the player doesn't have the ammo to reload
     */
    public void reload(Weapon weapon) throws LoadedException, NoAmmoException {
        if (!weapon.isLoad())
            if ((weapon.getCostBlue() <= this.blueAmmo) && (weapon.getCostRed() <= this.redAmmo) && (weapon.getCostYellow() <= this.yellowAmmo)) {
                this.blueAmmo = this.blueAmmo - weapon.getCostBlue();
                this.redAmmo = this.redAmmo - weapon.getCostRed();
                this.yellowAmmo = this.yellowAmmo - weapon.getCostYellow();
                weapon.reload();
            } else
                throw new NoAmmoException();
        else
            throw new LoadedException();
    }

    /**
     * This method allows the player to draw a power up from the deck of power ups
     * @throws MaxHandSizeException This exception means that the player has more then three cards in hand
     */
    public void draw() throws MaxHandSizeException {
        this.powerUps.add(getTurn().getMatch().getBoard().nextPowerUp());
        if (this.powerUps.size() > maxSize)
            throw new MaxHandSizeException();
    }

    /**
     * This method allows the player to discard a selected card from the hand
     * @param powerUp This parameter is the selected power up that the player wants to discard
     */
    public void discard(PowerUp powerUp) {
        for (int i = 0; i < this.powerUps.size(); i++)
            if (this.powerUps.contains(powerUp)) {
                this.powerUps.remove(i);
                return;
            }
    }

    /**
     * This method allows the player to spawn from a selected power up's color
     * @param powerUp This parameter is the power up that the player wants to discard
     * @throws NotFoundException This exception means that is not found the spawn point
     */
    public void spawn(PowerUp powerUp) throws NotFoundException {
        this.position = turn.getMatch().getBoard().findSpawnPoint(powerUp.getColor());
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
     */
    public void wound(int damage, Player shooter) {
        int i;
        for (i = 0; i < damage; i++) {
            if (this.damage.size() < 12) {
                this.damage.add(shooter);
                this.damageCounter++;
            }
        }
        for (i = 0; i < this.mark.size(); i++) {
            if (this.mark.get(i) == shooter && this.damage.size() < 12) {
                this.damage.add(shooter);
                this.damageCounter++;
            }
            this.mark.remove(i);
        }
        if (this.damageCounter > 10)
            this.dead();
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
     * This method is the run action that can be done in a frenzy turn
     * @param destination This parameter is the final destination where the player wants to move
     * @throws InvalidDestinationException This exception means that the player can't reach the destination
     */
    public void runFrenzy(Square destination) throws InvalidDestinationException {
        if (this.position.calcDist(destination) <= maxRunFrenzy)
            this.position = destination;
        else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter() + 1));
    }

    /**
     * This method is the shoot action that can be done in a frenzy turn
     * @param weaponShoot  This parameter is the weapon that the player wants to fire with
     * @param weaponReload This parameter is the weapon that the player wants to reload
     * @param destination  This parameter is the final destination where the player wants to move
     * @param target       This parameter is the target of the shoot action
     * @throws NotLoadedException This exception means that the weapon is not load
     * @throws InvalidDestinationException This exception means that the player can't reach the destination
     * @throws InvalidTargetException This exception means that there is no valid target chosen
     */
    public void shootFrenzy(Weapon weaponShoot, Weapon weaponReload, Square destination, TargetParameter target) throws NotLoadedException, InvalidDestinationException, InvalidTargetException {
        if (this.position.calcDist(destination) <= 1 + onlyFrenzyAction()) {
            this.position = destination;
            try {
                reload(weaponReload);
            } catch (LoadedException | NoAmmoException e) {
                e.printStackTrace();
            }
            if (weaponShoot.isLoad())
                weaponShoot.fire(target);
            else
                throw new NotLoadedException();
        } else
            throw new InvalidDestinationException();
        this.turn.setActionCounter((this.turn.getActionCounter() + 1));
    }

    /**
     * This method is the grab action that can be done in a frenzy turn
     * @param destination This parameter is the final destination where the player wants to move
     * @throws MaxHandSizeException This exception means that the player has more then three cards in hand
     * @throws InvalidDestinationException This exception means that the player can't reach the destination
     * @throws NullAmmoException This exception means that the player doesn't grab anything
     * @throws ElementNotFoundException This exception means that there isn't a takeable element
     */
    public void grabFrenzy(Square destination) throws InvalidDestinationException, MaxHandSizeException, NullAmmoException, ElementNotFoundException {
        grab(destination);
    }

    /**
     * This method answer if a weapon is red or not
     * @param weapon This parameter is the weapon chosen by the player
     * @return 1 if the weapons is red, 0 otherwise
     */
    private int isRed(Weapon weapon) {
        if (weapon.getColor().equals("Red"))
            return 1;
        return 0;
    }

    /**
     * This method answer if a weapon is blue or not
     * @param weapon This parameter is the weapon chosen by the player
     * @return 1 if the weapons is blue, 0 otherwise
     */
    private int isBlue(Weapon weapon) {
        if (weapon.getColor().equals("Blue"))
            return 1;
        return 0;
    }

    /**
     * This method answer if a weapon is yellow or not
     * @param weapon This parameter is the weapon chosen by the player
     * @return 1 if the weapons is yellow, 0 otherwise
     */
    private int isYellow(Weapon weapon) {
        if (weapon.getColor().equals("Yellow"))
            return 1;
        return 0;
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
     * @return 1 if the player has 6 or more damage counters, 0 otherwise
     */
    private int isOnAdrenalineShoot() {
        if (this.damageCounter >= 6 && !this.turnedPlank)
            return 1;
        return 0;
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
    public int getSkull() { return skull; }

    /**
     * This method sets the number of skulls of the player
     * @param skull This parameter is the number of skulls that the player'll have
     */
    public void setSkull(int skull) { this.skull = skull; }

    /**
     * This method returns the number of blue ammo that the player owns
     * @return The number of blue ammo that the player has
     */
    public int getBlueAmmo() { return blueAmmo; }

    /**
     * This method sets the number of blue ammo of the player
     * @param blueAmmo This parameter is the number of blue ammo that the player'll have
     */
    public void setBlueAmmo(int blueAmmo) { this.blueAmmo = blueAmmo; }

    /**
     * This method returns the number of red ammo that the player owns
     * @return The number of red ammo that the player has
     */
    public int getRedAmmo() { return redAmmo; }

    /**
     * This method sets the number of red ammo of the player
     * @param redAmmo This parameter is the number of red ammo that the player'll have
     */
    public void setRedAmmo(int redAmmo) { this.redAmmo = redAmmo; }

    /**
     * This method returns the number of yellow ammo that the player owns
     * @return The number of yellow ammo that the player has
     */
    public int getYellowAmmo() { return yellowAmmo; }

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
    public boolean isTurnedPlank() { return turnedPlank; }

    /**
     * This method sets if the plank is turned or not
     * @param turnedPlank This parameter will sets if the plank is turned or not
     */
    public void setTurnedPlank(boolean turnedPlank) { this.turnedPlank = turnedPlank; }

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
     * This method sets the previous position of the player
     * @param previousPosition This parameter is the previous position that the player'll have
     */
    public void setPreviousPosition(Square previousPosition) {
        this.previousPosition = previousPosition;
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
}