package main.java.nukeminecart.thaumicrecipe.ui.recipe;

import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.home.HomeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

public class RecipeService {
    /**
     * Performs error handling on the loadOtherRecipe
     *
     * @param path   the path of the {@link File}
     */
    public static void loadOtherRecipe(String path) {
        if (path == null) {
            instanceHomeUI.throwLoadWarning("File not found");
        } else {
            try {
                RecipeFileHandler.loadOtherRecipe(path);
            } catch (IOException e) {
                instanceHomeUI.throwAlert(ThaumicRecipeUI.WarningType.LOAD);
            }
        }
    }

    /**
     * Performs error handling on the loadFolderRecipe
     *
     * @param recipeName the name of the {@link main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe} to load
     * @param filenames  the names of the files that reside in the thaumicrecipe folder
     */

    public static void loadFolderRecipe(String recipeName, List<String> filenames) {
        if (recipeName.isEmpty()) {
            instanceHomeUI.throwLoadWarning("Filename is invalid");
        } else if (!filenames.contains(recipeName)) {
            instanceHomeUI.throwLoadWarning("File does not exist");
        } else {
            try {
                RecipeFileHandler.loadFolderRecipe(recipeName);
            } catch (IOException e) {
                instanceHomeUI.throwAlert(ThaumicRecipeUI.WarningType.LOAD);
            }
        }
    }

    /**
     * Loads the recipe from the configuration file
     *
     */
    public static void loadConfigRecipe() {
        if (loadedRecipe != null) {
            try {
                RecipeFileHandler.loadConfigRecipe();
            } catch (IOException e) {
                instanceHomeUI.throwAlert(ThaumicRecipeUI.WarningType.LOAD);
            }
        } else {
            instanceHomeUI.throwLoadWarning("No recipe in config");
        }
    }

    /**
     * @param name   the name of the {@link Recipe} to create
     * @param option the selected option in {@link HomeUI}
     * @return true if the {@link Recipe} can be created with the name and false if it cannot
     */

    public static boolean checkNewErrors(String name, String option) {
        if (name.isEmpty()) {
            instanceHomeUI.throwNewWarning("Recipe " + (option.equals("recipeCluster") ? "cluster " : "group ") + "name is blank");
            return false;
        }
        try {
            File testfile = recipeDirectory == null ? FileParser.getFolderFile(name) : new File(System.getProperty("user.dir"), name);
            boolean deleted;
            if (!testfile.createNewFile())
                instanceHomeUI.throwAlert(ThaumicRecipeUI.WarningType.LOAD);

            deleted = testfile.delete();
            if (!deleted) instanceHomeUI.throwAlert(ThaumicRecipeUI.WarningType.LOAD);
        }
        catch (Exception e) {
            instanceHomeUI.throwNewWarning("Filename is invalid");
            return false;
        }
        return true;
    }
}
