package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.nukeminecart.thaumicrecipe.ui.home.HomeUI;

import java.util.Objects;

public class UIManager extends Application{

    public static String separator, recipeDir, loadedRecipe;

    public static void main(String[] args){
        separator = System.getProperty("file.separator");
        recipeDir = args.length > 0 ? args[0]+separator+"thaumicrecipe"+separator : null;
        loadedRecipe = args.length > 1 ? args[1] : null;
        launch();

    }
    public static Stage stage;

    public static void loadScreen(Parent parent) {
        Scene scene = new Scene(parent, 750, 500);
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        UIManager.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(Objects.requireNonNull(UIManager.class.getResourceAsStream("resources/icon.png"))));
        stage.setTitle("Thaumic Recipe Tweaker Home");
        UIManager.loadScreen(HomeUI.getScene());
    }



}
