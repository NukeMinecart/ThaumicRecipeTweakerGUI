package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.nukeminecart.thaumicrecipe.ui.home.HomeUI;

public class UIManager extends Application{
    public static Stage stage;
    public static void main(String[] args){
        launch();
    }

    public static void loadScreen(Scene scene) {
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        UIManager.stage = stage;

        Scene scene = new Scene(HomeUI.getScene(), 750, 500);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(UIManager.class.getResourceAsStream("../../../../resources/icon.png")));
        stage.setTitle("Thaumic Recipe Tweaker Home");
        UIManager.loadScreen(scene);
    }



}
