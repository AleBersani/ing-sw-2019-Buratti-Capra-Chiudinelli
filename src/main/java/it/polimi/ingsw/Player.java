package it.polimi.ingsw;

import it.polimi.ingsw.Cards.PowerUp;
import it.polimi.ingsw.Cards.Weapon;
import it.polimi.ingsw.Map.Square;

import java.util.ArrayList;

public class Player {
    private int skull, blueAmmo, RedAmmo, yellowAmmo, points, damageCounter;
    private ArrayList<Player> damage = new ArrayList<Player>();
    private ArrayList<Player> mark = new ArrayList<Player>();
    private ArrayList<PowerUp> powerUps= new ArrayList<PowerUp>();
    private ArrayList<Weapon> weapons= new ArrayList<Weapon>();
    private boolean first, lastKill;
    private String color, nickname;
    private Square position, previousPosition;

    public void run(Square destination){

        return;
    }
    public void grab(Square destination){

        return;
    }
    public void shoot(Weapon weapon){

        return;
    }
    public void usePowerUp(PowerUp powerUp){

        return;
    }
    public boolean canSee(Player target){

        return false;
    }
    public void reload(Weapon weapon){

        return;
    }
    public void draw(){

        return;
    }
    public void discard(PowerUp powerUp){

        return;
    }
    public void spawn(){

        return;
    }
    public void dead(){

        return;
    }
    public void wound(int damage, Player shooter){

        return;
    }
    public void marked(int mark, Player shooter){

        return;
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
        return RedAmmo;
    }

    public void setRedAmmo(int redAmmo) {
        RedAmmo = redAmmo;
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
}
