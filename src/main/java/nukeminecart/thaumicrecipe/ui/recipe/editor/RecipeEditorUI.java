package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape.RecipeShapeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.IOException;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.editorRecipe;
import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.originalRecipe;

/**
 * The class that contains all the controller elements and logic for the RecipeEditorUI parent
 */

public class RecipeEditorUI extends ThaumicRecipeUI {
    @FXML
    private Label title;

    /**
     * Gets the {@link Parent} container containing all the RecipeEditorUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeEditorUI.fxml if not found
     */
    public Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeEditorUI.class.getResource("RecipeEditorUI.fxml")));

    }

    /**
     * Loads the {@link RecipeEditorUI} scene
     *
     * @throws IOException if RecipeShapeUI.fxml is not found
     */
    public void launchEditor() throws IOException {
        UIManager.loadScreen(getScene());
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        title.setText("Recipe Editor: " + editorRecipe.getName());
    }

    /**
     * Test method for certain not fully implemented features TODO -> REMOVE
     */
    @FXML
    private void test() {
        try {
            new RecipeShapeUI().launchShapeEditor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns to the {@link RecipeManagerUI} scene without saving the recipe
     */
    @FXML
    private void returnToManager() {
        try {
            RecipeManagerUI.recipeEditorMap.put(originalRecipe.getName(), originalRecipe);
            new RecipeManagerUI().loadManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns to the {@link RecipeManagerUI} scene while saving the recipe in the editor
     */
    @FXML
    private void saveRecipeAndReturn() {
        try {
            RecipeManagerUI.recipeEditorMap.put(editorRecipe.getName(), editorRecipe);
            new RecipeManagerUI().loadManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
