package it.polimi.ingsw.view;

import java.util.ArrayList;

public interface ViewInterface {

    void spawn(String msg);

    void showMessage();

    void gameShow(String msg);

    void boardSettingView(ArrayList<String> data, String title);

    void waitingRoomView();

    void loginView();

    void stopView();

    void endTurnShow();

    void reloadShow(String msg);

    void targetShow(String msg);

    void discardWeapon(String msg);

    void preShoot(String msg);

    void optionalShoot(String msg);

    void gameReShow();

    void winnerShow(String msg);

    void interruptPowerUp(String msg);

    void frenzyShootShow();

    void suspend();
}
