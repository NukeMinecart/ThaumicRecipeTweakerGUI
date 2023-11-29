package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.scene.Scene;
import main.java.nukeminecart.thaumicrecipe.ui.home.HomeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ThaumicRecipeConstants {
    public static String separator, recipeDirectory, loadedRecipe, MOD_ID, stringArraySeparator, stringSeparator, mapSeparator;
    public static String fileExistsWarning, noFileInConfigWarning;
    public static Recipe editorRecipe, originalRecipe;
    public static boolean editorRecipeExisted;
    public static int stageWidth, stageHeight;
    public static HomeUI instanceHomeUI;
    public static RecipeManagerUI instanceRecipeManagerUI;
    public static RecipeEditorUI instanceRecipeEditorUI;
    public static HashMap<String, Scene> cachedScenes;
    public static HashMap<String, String> aspectList, tempAspectList, ingredientsList, researchList;
    public static HashMap<String, Recipe> recipeList;


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
        mapSeparator = ":";
        stringSeparator = "-=-";
        stringArraySeparator = "-";
        stageWidth = 750;
        stageHeight = 500;
        fileExistsWarning = "File already exist";
        noFileInConfigWarning = "No file in config";
        cachedScenes = new HashMap<>();
        aspectList = new HashMap<>();
        ingredientsList = new HashMap<>();
        tempAspectList = new HashMap<>();
        recipeList = new HashMap<>();
        Thread fileThread =  new Thread(() -> {
            try {
                getListsFromFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
       fileThread.setName("ThaumicRecipeTweakerGUI File Loader Thread");
       fileThread.start();
    }

    /**
     * Gets the {@link List} from a {@link File} in the thaumicrecipe folder
     *
     * @throws IOException if the {@link FileParser} has an error
     */
    private void getListsFromFile() throws IOException {
        File ingredientsFile = new File(recipeDirectory, "ingredients.lst");
        if (ingredientsFile.exists()) ingredientsList = FileParser.parseList(ingredientsFile);

        File aspectsFile = new File(recipeDirectory, "aspects.lst");
        if (aspectsFile.exists()) aspectList = FileParser.parseList(aspectsFile);
        File researchFile = new File(recipeDirectory, "research.lst");
        if (researchFile.exists()) researchList = FileParser.parseList(researchFile);

        File recipesFile = new File(recipeDirectory, "recipes.lst");
        if (recipesFile.exists()) {
            for (Recipe recipe : FileParser.getRecipesFromString(FileParser.readFile(recipesFile))) {
                recipeList.put(recipe.getName(), recipe);
            }
        }

        tempAspectList.put("Air", "thaumcraft");
        tempAspectList.put("Water", "thaumcraft");
        tempAspectList.put("Entropy", "thaumcraft");
        tempAspectList.put("Fire", "thaumcraft");
        tempAspectList.put("Order", "thaumcraft");
        tempAspectList.put("Earth", "thaumcraft");
        Thread.currentThread().interrupt();
    }
}
