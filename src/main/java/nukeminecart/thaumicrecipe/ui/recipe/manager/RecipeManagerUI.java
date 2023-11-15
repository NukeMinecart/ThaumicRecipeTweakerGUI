package main.java.nukeminecart.thaumicrecipe.ui.recipe.manager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.cell.RecipeCellFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * The class that contains all the controller elements and logic for the RecipeManagerUI parent
 */

public class RecipeManagerUI extends ThaumicRecipeUI {


    public static final ObservableList<Recipe> recipes = FXCollections.observableArrayList();
    public static final HashMap<String, Recipe> recipeEditorMap = new HashMap<>();
    private static String stringTitle;
    @FXML
    private ListView<Recipe> recipeList;
    @FXML
    private TextField nameField;
    @FXML
    private Label title, recipeListLabel, nameLabel;

    /**
     * Loads the {@link RecipeEditorUI} and passes it the recipe to be displayed
     *
     * @param recipeName the recipe name to load the recipe editor with
     */
    public static void openEditor(String recipeName) {
        try {
            ThaumicRecipeConstants.changeEditorRecipe(recipeEditorMap.get(recipeName));
            recipeEditorMap.remove(recipeName);
            new RecipeEditorUI().launchEditor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the recipeEditorMap {@link HashMap} with the key as the name of the recipe and the values as the {@link Recipe} from the recipes {@link ObservableList}
     */
    private void refreshHashMap() {
        for (Recipe recipe : recipes) {
            recipeEditorMap.put(recipe.getName(), recipe);
        }
    }

    /**
     * Updates the recipes {@link ObservableList} with the values from the recipeEditorMap {@link HashMap}
     */
    private void refreshRecipes() {
        recipes.setAll(recipeEditorMap.values());
    }

    /**
     * Gets the {@link Parent} container containing all the RecipeManagerUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeManagerUI.fxml if not found
     */
    public Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeManagerUI.class.getResource("RecipeManagerUI.fxml")));
    }

    /**
     * Gets the cached {@link Scene} from {@link ThaumicRecipeConstants}
     *
     * @return the cached {@link Scene}
     */
    public Scene getCachedScene() {
        return cachedScenes.get("manager");
    }

    /**
     * Loads the RecipeManager scene and adds all the recipes to the listView
     *
     * @param name         the name of the file
     * @param fileContents the contents of the file
     * @throws IOException if getScene returns an invalid {@link Parent}
     */
    public void loadManager(String name, List<String> fileContents) throws IOException {
        instanceRecipeManagerUI = this;
        if (fileContents != null && fileContents.size() > 1) {
            recipes.addAll(FileParser.getRecipesFromString(fileContents));
            refreshHashMap();
        }
        stringTitle = name.endsWith(".rcp") ? name : name + ".rcp";
        UIManager.loadScreen(getScene(), "manager");
    }

    /**
     * Load the {@link RecipeManagerUI} scene and refresh the recipes from the {@link HashMap}
     */
    public void loadManager() throws IOException {
        instanceRecipeManagerUI = this;
        UIManager.loadScreen(getCachedScene());
        Platform.runLater(this::refreshRecipes);
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        recipeList.setCellFactory(new RecipeCellFactory());
        recipeList.setItems(recipes);
        title.setText("Recipe Manager: " + stringTitle);
        recipes.addListener((ListChangeListener<Recipe>) c -> refreshHashMap());
        recipeListLabel.setText("Recipes in " + stringTitle);
        nameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (nameField.getText().isEmpty()) return;

                changeEditorRecipe(new Recipe(nameField.getText()));
                editorRecipeExisted = false;
                nameField.setVisible(false);
                nameLabel.setVisible(false);
                nameField.setText("");
                try {
                    new RecipeEditorUI().launchEditor();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        refreshRecipes();
    }

    /**
     * FXML event to make the nameField visible to add a {@link Recipe}
     */

    @FXML
    private void addRecipe() {
        nameField.setVisible(true);
        nameLabel.setVisible(true);

    }

}
