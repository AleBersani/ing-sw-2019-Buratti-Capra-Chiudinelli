package it.polimi.ingsw;

import it.polimi.ingsw.Cards.PowerUp;
import it.polimi.ingsw.Cards.Weapon;
import it.polimi.ingsw.Map.Square;

import java.util.ArrayList;

public class Player {
    private int skull, blueAmmo, RedAmmo, yellowAmmo, points, damegeCounter;
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

}
