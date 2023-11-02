package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape.RecipeShapeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.Objects;

/**
 * The class that contains all the controller elements and logic for the RecipeEditorUI parent
 */

public class RecipeEditorUI extends ThaumicRecipeUI {
    private static Recipe recipe;
    @FXML
    private Label title;

    /**
     * Gets the {@link Parent} container containing all the RecipeEditorUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeEditorUI.fxml if not found
     */
    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeEditorUI.class.getResource("RecipeEditorUI.fxml")));

    }

    /**
     * Loads the {@link RecipeEditorUI} scene
     *
     * @param recipe the recipe to load the editor with
     * @throws IOException if RecipeShapeUI.fxml is not found
     */
    public static void launchEditor(Recipe recipe) throws IOException {
        RecipeEditorUI.recipe = recipe;
        UIManager.loadScreen(getScene());
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        title.setText("Recipe Editor: " + recipe.getName());
    }

    /**
     * Test method for certain not fully implemented features TODO -> REMOVE
     */
    @FXML
    private void test() {
        try {
            RecipeShapeUI.launchShapeEditor(recipe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
