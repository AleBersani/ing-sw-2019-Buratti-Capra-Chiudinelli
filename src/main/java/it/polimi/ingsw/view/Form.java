package it.polimi.ingsw.view;

import it.polimi.ingsw.communication.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Form extends Application {

    private Client client;
    private Stage stage;
    private LoginGUI loginGUI = new LoginGUI();

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Client client = new Client(this);
        client.init();
        client.start();
        this.stage = primaryStage;


        StackPane pane = new StackPane();
        Scene scene = new Scene(pane,1500,1000);
        stage.setScene(scene);

        //stage
        stage.setTitle("Adrenaline");
        stage.getIcons().add(new Image("/images/adrenalineLogo.png"));
        stage.setResizable(true);

        //call methods
        /*
        for(int i=0;i<3;i++){
            StackPane stackPane = new StackPane();
            stage.getScene().setRoot(stackPane);
            loginGUI.loginImageSetting(stage);
            if(i==0){
                loginGUI.loginGridSetting(stage,client);
            }
            if(i==1){
                loginGUI.menuGridSetting(stage);
            }
            if(i==2){
                loginGUI.roomGridSetting(stage);
            }
            stage.show();
        }
        */
        loginGUI.loginImageSetting(stage);
        loginGUI.loginGridSetting(stage,client);

        stage.show();
    }

    public void stopView(){
        Platform.exit();
    }
}