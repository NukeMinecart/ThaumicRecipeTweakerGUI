package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.RecipeListUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.search.RecipeSearchUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape.RecipeShapeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;
import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.stage;

/**
 * The class that contains all the controller elements and logic for the RecipeEditorUI parent
 */

public class RecipeEditorUI extends ThaumicRecipeUI {
    @FXML
    private TextField nameField, visField, inputField, outputField;
    @FXML
    private ListView<String> ingredientsListview, aspectListview;
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
        instanceRecipeEditorUI = this;
        if (!cachedScenes.containsKey("editor-" + editorRecipe.getName())) {
            UIManager.loadScreen(getScene(), "editor-" + editorRecipe.getName());
        } else {
            UIManager.loadScreen(cachedScenes.get("editor-" + editorRecipe.getName()));
        }
    }

    /**
     * Load the {@link RecipeEditorUI} from the cachedScenes and with a {@link String} value of the item
     */
    public void launchEditorFromSearch(String item, String type) {
        if (type.equalsIgnoreCase("input")) editorRecipe.setInput(item);
        else editorRecipe.setOutput(item);
        try {
            UIManager.loadScreen(getScene(), "editor-" + editorRecipe.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML initialize event
     */
    @FXML
    private void initialize() {
        title.setText("Recipe Editor: " + editorRecipe.getName());
        inputField.setText(editorRecipe.getInput());
        visField.setText(String.valueOf(editorRecipe.getVis()));
        outputField.setText(editorRecipe.getOutput());
        nameField.setText(editorRecipe.getName());
        ingredientsListview.setItems(FXCollections.observableArrayList(editorRecipe.getIngredients()));
        aspectListview.setItems(FXCollections.observableArrayList(editorRecipe.getAspects()));

    }

    /**
     * FXML event to open the {@link RecipeShapeUI}
     */
    @FXML
    private void openShapeEditor() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeShapeUI().launchShapeEditor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML event to open the {@link RecipeSearchUI} with the input parameter
     */
    @FXML
    private void openInputItemSearch() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeSearchUI().launchItemSearch("input");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML event to open the {@link RecipeSearchUI} with the output parameter
     */
    @FXML
    private void openOutputItemSearch() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeSearchUI().launchItemSearch("output");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test method for certain not fully implemented features TODO -> REMOVE
     */
    @FXML
    private void test() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeListUI().launchListEditor("ingredients");
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
            System.out.println(Arrays.toString(originalRecipe.getShape()));
            ThaumicRecipeConstants.instanceRecipeManagerUI.loadManager();
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
            editorRecipe.setName(nameField.getText());
            RecipeManagerUI.recipeEditorMap.put(editorRecipe.getName(), editorRecipe);
            System.out.println(Arrays.toString(editorRecipe.getShape()));
            ThaumicRecipeConstants.instanceRecipeManagerUI.loadManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
