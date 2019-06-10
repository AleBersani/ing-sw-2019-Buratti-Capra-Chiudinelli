package it.polimi.ingsw.view;

import java.util.ArrayList;

public interface ViewInterface {

    void showMessage();

    void dataShow(String msg);

    void boardSettingView(ArrayList<String> data, String title);

    void waitingRoomView();

    void loginView();

    void stopView();
}
