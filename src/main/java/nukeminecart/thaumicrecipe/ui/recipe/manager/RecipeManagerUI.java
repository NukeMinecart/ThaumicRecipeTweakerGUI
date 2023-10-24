package main.java.nukeminecart.thaumicrecipe.ui.recipe.manager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.versions.FileParserV1;

import java.io.IOException;
import java.util.Objects;

public class RecipeManagerUI {

    private static Recipe[] recipes;
    private double x,y;

    public static Parent getScene() throws IOException {

        return FXMLLoader.load(Objects.requireNonNull(RecipeManagerUI.class.getResource("RecipeManagerUI.fxml")));
    }

    public static void loadManager(String[] contents) throws IOException {
        if(contents != null){
            recipes = new FileParserV1().getRecipesFromString(contents);
        }
        Scene scene = new Scene(getScene(), 750, 500);
        UIManager.loadScreen(scene);
    }

    @FXML
    private void closeScreen() {
        Platform.exit();
    }
    @FXML private void panePressed(MouseEvent me){
        x = UIManager.stage.getX() - me.getScreenX();
        y = UIManager.stage.getY() - me.getScreenY();
    }

    @FXML private void paneDragged(MouseEvent me){
        UIManager.stage.setX(x + me.getScreenX());
        UIManager.stage.setY(y + me.getScreenY());
    }


}
