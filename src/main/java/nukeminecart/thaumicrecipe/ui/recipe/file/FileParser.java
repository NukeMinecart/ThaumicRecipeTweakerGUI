package main.java.nukeminecart.thaumicrecipe.ui.recipe.file;

import main.java.nukeminecart.thaumicrecipe.ui.UIManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class used for all reading of files and changing of a {@link String} to a {@link Recipe}
 */
public class FileParser {
    private static final String separator = "-=-";
    private static final String arraySeparator = "-";

    /**
     * Locates a file from the thaumicrecipe folder and returns the corresponding {@link java.io.File}
     *
     * @param name the name of the recipe file
     * @return a file with the
     */
    public static File getFolderFile(String name) {
        return new File(UIManager.recipeDir + name);
    }

    /**
     * Reads {@link java.io.File} and returns the lines as a list of strings
     *
     * @param file the file to read
     * @return a list of the lines in the file
     */
    public static List<String> readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            lines.add(line);
        }

        reader.close();
        return lines;
    }

    /**
     * Returns if a {@link java.io.File} exists
     *
     * @param file the file to check
     * @return if the file exists
     */
    public static boolean checkExists(File file) {
        return file.exists() && file.isFile();
    }

    /**
     * Takes a recipe and compresses it into a single line
     * to be saved in a file
     *
     * @param recipe a recipe to compress
     * @return String containing the recipe information
     */
    public static String compressRecipe(Recipe recipe){
        StringBuilder returnRecipe;
        returnRecipe = new StringBuilder(recipe.getName() + separator);
        returnRecipe.append(recipe.getType()).append(separator);
        returnRecipe.append(recipe.getInput()).append(separator);

        for (String ingredient : recipe.getIngredients()) {
            returnRecipe.append(ingredient).append(arraySeparator);
        }
        returnRecipe.append(separator);
        returnRecipe.append(recipe.getOutput()).append(separator);

        returnRecipe.append(recipe.getVis()).append(separator);
        for (String aspect : recipe.getAspects()) {
            returnRecipe.append(aspect).append(arraySeparator);
        }
        returnRecipe.append(separator);
        for (Object shape : recipe.getShape()) {
            returnRecipe.append(shape).append(arraySeparator);
        }
        return returnRecipe.toString();
    }

    /**
     * Changes custom string recipe to a {@link main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe}
     *
     * @param line the line to parse
     * @return a recipe class
     * @throws ArrayIndexOutOfBoundsException if the line is missing necessary recipe information
     */
    public static Recipe parseRecipe(String line){
        String[] compressedRecipe = line.split(separator);
        String name = compressedRecipe[0];
        String type = compressedRecipe[1];
        String input = compressedRecipe[2];
        String[] ingredients = compressedRecipe[3].split(arraySeparator);
        String output = compressedRecipe[4];
        int vis = Integer.parseInt(compressedRecipe[5]);
        String[] aspects = compressedRecipe[6].split(arraySeparator);
        String[] shape = compressedRecipe[7].split(arraySeparator);

        return new Recipe(name, type, input, ingredients, output, vis, aspects, shape);
    }

    /**
     * Returns all the recipes from a list of strings
     *
     * @param contents the list to get recipes from
     * @return an array of recipes
     */
    public static Recipe[] getRecipesFromString(List<String> contents){
        List<Recipe> recipes = new ArrayList<>();
        for (String line : contents) {
            recipes.add(parseRecipe(line));
        }
        return recipes.toArray(new Recipe[0]);
    }
}
