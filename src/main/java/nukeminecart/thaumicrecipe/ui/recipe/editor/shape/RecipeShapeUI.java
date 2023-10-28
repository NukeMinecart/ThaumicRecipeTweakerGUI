package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.Objects;

public class RecipeShapeUI {
    public static Recipe recipe;
    private double x,y;

    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeShapeUI.class.getResource("RecipeShapeUI.fxml")));
    }
    public static void launchEditor(Recipe recipe) throws IOException {
        RecipeShapeUI.recipe = recipe;
        UIManager.loadScreen(getScene());
    }

    @FXML
    public void initialize(){
    }

    @FXML private void closeScreen() {
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
