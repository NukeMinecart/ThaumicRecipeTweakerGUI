package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape.RecipeShapeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.Objects;

public class RecipeEditorUI {
    private static Recipe recipe;
    private double x,y;
    @FXML
    private Label title;
    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeEditorUI.class.getResource("RecipeEditorUI.fxml")));

    }

    public static void launchEditor(Recipe recipe) throws IOException {
        RecipeEditorUI.recipe = recipe;
        UIManager.loadScreen(getScene());
    }

    @FXML
    public void initialize(){
        title.setText("Recipe Editor: "+recipe.getName());
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

    @FXML private void test(){
        try {
            RecipeShapeUI.launchEditor(recipe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
