package main.java.nukeminecart.thaumicrecipe.ui.recipe;

import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * Class containing methods which either read .rcp files, or create new .rcp files
 */

public class RecipeFileHandler {
    public static File file;

    /**
     * Loads a recipe from the configuration file and launches the {@link RecipeManagerUI}
     *
     * @throws IOException if the file does not exist and if {@link RecipeManagerUI} cannot find its associated .fxml file
     */

    public static void loadConfigRecipe() throws IOException {
        if (loadedRecipe != null) {
            file = FileParser.getFolderFile(loadedRecipe.endsWith(".rcp") ? loadedRecipe : loadedRecipe + ".rcp");
            List<String> contents = FileParser.readFile(file);
            new RecipeManagerUI().loadManager(file.getName(), contents);
        } else {
            instanceHomeUI.throwNewWarning(noFileInConfigWarning);
        }
    }

    /**
     * Loads a recipe from a file in the thaumicrecipe folder and launches the {@link RecipeManagerUI}
     *
     * @throws IOException if the file does not exist and if {@link RecipeManagerUI} cannot find its associated .fxml file
     */
    public static void loadFolderRecipe(String filename) throws IOException {
        file = FileParser.getFolderFile(filename);
        List<String> contents = FileParser.readFile(file);
        new RecipeManagerUI().loadManager(filename, contents);

    }

    /**
     * Loads a recipe from anywhere and launches the {@link RecipeManagerUI}
     *
     * @throws IOException if the file does not exist and if {@link RecipeManagerUI} cannot find its associated .fxml file
     */
    public static void loadOtherRecipe(String path) throws IOException {
        file = new File(path);
        List<String> contents = FileParser.readFile(FileParser.checkExists(file) ? file : null);
        new RecipeManagerUI().loadManager(file.getName(), contents);
    }

    /**
     * Creates a new recipe group with the specified name and launches the {@link RecipeManagerUI}
     *
     * @param name the name for the recipe group
     * @throws IOException if the file already exists and if {@link RecipeManagerUI} cannot find its associated .fxml file
     */
    public static void newRecipeGroup(String name) throws IOException {
        file = FileParser.getFolderFile(name.endsWith(".rcp") ? name : name + ".rcp");
        if (file.exists()) {
            instanceHomeUI.throwNewWarning(fileExistsWarning);
        } else {
            new RecipeManagerUI().loadManager(name, null);
        }
    }

    /**
     * Creates a new {@link main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe} collection with the specified name and launches the {@link RecipeManagerUI}
     *
     * @param name  the name for the recipe collection
     * @param files the {@link List} of {@link File} to read and import from
     * @throws IOException if the {@link File} already exists and if {@link RecipeManagerUI} cannot find its associated .fxml {@link File}
     */
    public static void newRecipeCluster(String name, List<File> files) throws IOException {
        file = FileParser.getFolderFile(name.endsWith(".rcp") ? name : name + ".rcp");
        if (file.exists()) {
            instanceHomeUI.throwNewWarning(fileExistsWarning);
        } else {
            List<String> contents = new ArrayList<>();
            if(!files.isEmpty()) {
                for (File file : files) {
                    contents.addAll(FileParser.readFile(FileParser.checkExists(file) ? file : null));
                }
            }
            new RecipeManagerUI().loadManager(name, contents);
        }
    }


}

