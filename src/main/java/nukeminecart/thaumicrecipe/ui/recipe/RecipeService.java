package main.java.nukeminecart.thaumicrecipe.ui.recipe;

import main.java.nukeminecart.thaumicrecipe.ui.home.HomeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.loadedRecipe;
import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.recipeDirectory;

public class RecipeService {
    /**
     * Performs error handling on the loadOtherRecipe
     *
     * @param homeUI the instance of {@link HomeUI}
     * @param path   the path of the {@link File}
     */
    public static void loadOtherRecipe(HomeUI homeUI, String path) {
        if (path == null) {
            homeUI.throwLoadWarning("File not found");
        } else {
            try {
                RecipeFileHandler.loadOtherRecipe(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Performs error handling on the loadFolderRecipe
     *
     * @param homeUI     the instance of {@link HomeUI}
     * @param recipeName the name of the {@link main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe} to load
     * @param filenames  the names of the files that reside in the thaumicrecipe folder
     */

    public static void loadFolderRecipe(HomeUI homeUI, String recipeName, List<String> filenames) {
        if (recipeName.isEmpty()) {
            homeUI.throwLoadWarning("Filename is invalid");
        } else if (!filenames.contains(recipeName)) {
            homeUI.throwLoadWarning("File does not exist");
        } else {
            try {
                RecipeFileHandler.loadFolderRecipe(recipeName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Loads the recipe from the configuration file
     *
     * @param homeUI the instance of {@link HomeUI}
     */
    public static void loadConfigRecipe(HomeUI homeUI) {
        if (loadedRecipe != null) {
            try {
                RecipeFileHandler.loadConfigRecipe();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            homeUI.throwLoadWarning("No recipe in config");
        }
    }

    /**
     * @param homeUI the instance of {@link HomeUI}
     * @param name   the name of the {@link Recipe} to create
     * @param option the selected option in {@link HomeUI}
     * @return true if the {@link Recipe} can be created with the name and false if it cannot
     */

    public static boolean checkNewErrors(HomeUI homeUI, String name, String option) {
        if (name.isEmpty()) {
            homeUI.throwNewWarning("Recipe " + (option.equals("smallRecipe") ? "" : "group ") + "name is blank");
            return false;
        }
        try {
            File testfile = recipeDirectory == null ? FileParser.getFolderFile(name) : new File(System.getProperty("user.dir"), name);
            boolean deleted;
            if (!testfile.createNewFile()) {
                throw new NullPointerException("File cannot be created");
            }
            deleted = testfile.delete();
            if (!deleted) {
                throw new NullPointerException("File cannot be deleted");
            }

        } catch (Exception e) {
            homeUI.throwNewWarning("Filename is invalid");
            return false;
        }
        return true;
    }
}
