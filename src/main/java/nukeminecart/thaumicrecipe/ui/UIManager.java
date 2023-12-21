package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.nukeminecart.thaumicrecipe.ui.home.HomeUI;

import java.io.IOException;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.stageHeight;
import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.stageWidth;

/**
 * The entry class from which all other scene updates get called
 */
public class UIManager extends Application {

    //TODO RUN TESTS ON ALL ASPECTS OF THE PROGRAM
    public static Stage stage;

    /**
     * The entry point from which the forge mod calls
     *
     * @param args the arguments passed to this application
     */
    public static void main(String[] args) {
        new ThaumicRecipeConstants().initializeConstants(args);
        launch();

    }

    /**
     * Loads a {@link Parent} onto the main {@link Stage} and displays it
     *
     * @param parent the {@link Parent} container to load
     */
    public static void loadScreen(Parent parent, String name) {
        Scene scene = new Scene(parent, stageWidth, stageHeight);
        ThaumicRecipeConstants.cachedScenes.put(name, scene);
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * Loads a {@link Scene} onto the main {@link Stage} and displays it}
     *
     * @param scene the {@link Scene} to display
     */
    public static void loadScreen(Scene scene) {
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * The entry point at which the ThaumicRecipeTweakerGUI application starts
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set. The primary stage will be embedded in
     *              the browser if the application was launched as an applet.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages and will not be embedded in the browser.
     * @throws IOException if any files that should be gotten are null
     */
    @Override
    public void start(Stage stage) throws IOException {
        UIManager.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(Objects.requireNonNull(UIManager.class.getResourceAsStream("resources/icon.png"))));
        stage.setTitle("Thaumic Recipe Tweaker Home");
        UIManager.loadScreen(new HomeUI().getScene(), "home");
    }


}
