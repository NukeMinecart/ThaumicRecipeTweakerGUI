package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.scene.Scene;
import main.java.nukeminecart.thaumicrecipe.ui.home.HomeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.util.HashMap;

public class ThaumicRecipeConstants {
    public static String separator, recipeDirectory, loadedRecipe, MOD_ID, stringArraySeparator, stringSeparator;
    public static String fileExistsWarning, noFileInConfigWarning;
    public static Recipe editorRecipe, originalRecipe;
    public static boolean editorRecipeExisted;
    public static int stageWidth, stageHeight;
    public static HomeUI instanceHomeUI;
    public static RecipeManagerUI instanceRecipeManagerUI;
    public static RecipeEditorUI instanceRecipeEditorUI;
    public static HashMap<String, Scene> cachedScenes;
    public static HashMap<String, String> aspectList;
    public static HashMap<String, String> ingredientsList;
    public static HashMap<String, String> researchList;


    /**
     * Change the {@link Recipe} that is currently loaded in the editor
     *
     * @param recipe the recipe to set the currently loaded recipe to
     */
    public static void changeEditorRecipe(Recipe recipe) {
        editorRecipe = recipe.copy();
        originalRecipe = recipe.copy();
    }

    /**
     * Initializes all constants used by ThaumicRecipeTweakerGUI
     *
     * @param args the arguments passed by the main method
     */
    public void initializeConstants(String[] args) {
        MOD_ID = "thaumicrecipe";
        separator = System.getProperty("file.separator");
        recipeDirectory = args.length > 0 ? args[0] + separator + MOD_ID + separator : null;
        loadedRecipe = args.length > 1 ? args[1] : null;
        stringSeparator = "-=-";
        stringArraySeparator = "-";
        stageWidth = 750;
        stageHeight = 500;
        fileExistsWarning = "File already exist";
        noFileInConfigWarning = "No file in config";
        cachedScenes = new HashMap<>();
        aspectList = new HashMap<>();
        ingredientsList = new HashMap<>();
    }
}
