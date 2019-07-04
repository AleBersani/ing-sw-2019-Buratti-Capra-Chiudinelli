package it.polimi.ingsw.view;

import java.util.ArrayList;

public interface ViewInterface {

    /**
     * method that show the respawn screen
     * @param msg data for the spawn
     */
    void spawn(String msg);

    /**
     * method that show a message from the server
     */
    void showMessage();

    /**
     * method that show the background
     * @param msg data for the background
     */
    void gameShow(String msg);

    /**
     * method that show the menu screen
     * @param data data of the settings to show
     * @param title of the data to be show
     */
    void boardSettingView(ArrayList<String> data, String title);

    /**
     * method that show the waiting room
     */
    void waitingRoomView();

    /**
     * method that show the login
     */
    void loginView();

    /**
     * method that close the screen
     */
    void stopView();

    /**
     * method that change the actions in the game for the end turn phase
     */
    void endTurnShow();

    /**
     * method that show the reload
     * @param msg data for the reload
     */
    void reloadShow(String msg);

    /**
     * method that show the building of the target
     * @param msg data for the building of the target
     */
    void targetShow(String msg);

    /**
     * method that show the discard of the weapons
     * @param msg data for the discard
     */
    void discardWeapon(String msg);

    /**
     * method that show the initial phase of the shoot
     * @param msg data for the pre shoot
     */
    void preShoot(String msg);

    /**
     * method that change the actions in the game meanwhile there is an optional shoot
     * @param msg data for the optional shoot
     */
    void optionalShoot(String msg);

    /**
     * method that reshow the background
     */
    void gameReShow();

    /**
     * method that show the winner
     * @param msg data for the winner
     */
    void winnerShow(String msg);

    /**
     * method that show the screen for the power ups that can be played in response of a specific action
     * @param msg data for this type of power ups
     */
    void interruptPowerUp(String msg);

    /**
     * method used meanwhile there is a shoot action in the frenzy turn
     */
    void frenzyShootShow();

    /**
     * method that show the suspended screen
     */
    void suspend();
}
