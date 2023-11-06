package main.java.nukeminecart.thaumicrecipe.ui.recipe.manager;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.instanceRecipeManagerUI;

/**
 * The class that contains all the controller elements and logic for the RecipeManagerUI parent
 */

public class RecipeManagerUI extends ThaumicRecipeUI {


    public static final ObservableList<Recipe> recipes = FXCollections.observableArrayList();
    public static final HashMap<String, Recipe> recipeEditorMap = new HashMap<>();
    private static String stringTitle;
    @FXML
    public ListView<Recipe> recipeList;
    @FXML
    private Label title;

    /**
     * Loads the {@link RecipeEditorUI} and passes it the recipe to be displayed
     *
     * @param recipeName the recipe name to load the recipe editor with
     */
    public static void openEditor(String recipeName) {
        try {
            ThaumicRecipeConstants.changeEditorRecipe(recipeEditorMap.get(recipeName));
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
        recipeList.setCellFactory(new RecipeCellFactory());
        recipeList.setItems(recipes);
        recipes.setAll(recipeEditorMap.values());
        //TODO FIX WARNING MESSAGES
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
     * Loads the RecipeManager scene and adds all the recipes to the listView
     *
     * @param name     the name of the file
     * @param contents the contents of the file
     * @throws IOException if getScene returns an invalid {@link Parent}
     */
    public void loadManager(String name, List<String> contents) throws IOException {
        instanceRecipeManagerUI = this;
        if (contents != null && contents.size() > 1) {
            recipes.addAll(FileParser.getRecipesFromString(contents));
            refreshHashMap();
        }
        stringTitle = name.endsWith(".rcp") ? name : name + ".rcp";
        UIManager.loadScreen(getScene());
    }

    /**
     * Load the {@link RecipeManagerUI} scene and refresh the recipes from the hashmap
     */
    public void loadManager() throws IOException {
        instanceRecipeManagerUI = this;
        UIManager.loadScreen(getScene());
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        refreshRecipes();
        title.setText("Recipe Manager: " + stringTitle);
        recipes.addListener((ListChangeListener<Recipe>) c -> refreshHashMap());
    }


}
