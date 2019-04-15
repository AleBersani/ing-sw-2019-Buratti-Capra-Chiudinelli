package it.polimi.ingsw.Model.Map;

public class AmmoPoint extends Square {

    private AmmoTile ammo;

    public AmmoPoint(int x, int y, String color, Room room) {
        super(x, y, color, room);
        ammo=null;
    }

    public AmmoTile getAmmo() {
        return ammo;
    }

    @Override
    public void generate(){
        this.ammo=super.getRoom().getBoard().nextAmmo();
        return;
    }

    @Override
    public boolean require() {
        return this.ammo==null;
    }

    public void setAmmo(AmmoTile ammo) {
        this.ammo = ammo;
    }
}
