package main.java.nukeminecart.thaumicrecipe.ui.recipe.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * A class used for all reading of files and changing of a {@link String} to a {@link Recipe}
 */
public class FileParser {

    /**
     * Locates a file from the thaumicrecipe folder and returns the corresponding {@link java.io.File}
     *
     * @param name the name of the recipe file
     * @return a file with the
     */
    public static File getFolderFile(String name) {
        return new File(recipeDirectory + name);
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
    public static String compressRecipe(Recipe recipe) {
        StringBuilder returnRecipe;
        returnRecipe = new StringBuilder(recipe.getName() + stringSeparator);
        returnRecipe.append(recipe.getType()).append(stringSeparator);
        returnRecipe.append(recipe.getInput()).append(stringSeparator);

        for (String ingredient : recipe.getIngredients()) {
            returnRecipe.append(ingredient).append(stringArraySeparator);
        }
        returnRecipe.append(stringSeparator);
        returnRecipe.append(recipe.getOutput()).append(stringSeparator);

        returnRecipe.append(recipe.getVis()).append(stringSeparator);
        for (String aspect : recipe.getAspects()) {
            returnRecipe.append(aspect).append(stringArraySeparator);
        }
        returnRecipe.append(stringSeparator);
        for (Object shape : recipe.getShape()) {
            returnRecipe.append(shape).append(stringArraySeparator);
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
    public static Recipe parseRecipe(String line) {
        String[] compressedRecipe = line.split(stringSeparator);
        String name = compressedRecipe[0];
        String type = compressedRecipe[1];
        String input = compressedRecipe[2];
        String[] ingredients = compressedRecipe[3].split(stringArraySeparator);
        String output = compressedRecipe[4];
        int vis = Integer.parseInt(compressedRecipe[5]);
        String[] aspects = compressedRecipe[6].split(stringArraySeparator);
        String[] shape = compressedRecipe[7].split(stringArraySeparator);

        return new Recipe(name, type, input, ingredients, output, vis, aspects, shape);
    }

    /**
     * Returns all the recipes from a list of strings
     *
     * @param contents the list to get recipes from
     * @return an array of recipes
     */
    public static Recipe[] getRecipesFromString(List<String> contents) {
        List<Recipe> recipes = new ArrayList<>();
        for (String line : contents) {
            recipes.add(parseRecipe(line));
        }
        return recipes.toArray(new Recipe[0]);
    }

    /**
     * Writes to the file with each line corresponding to an index in the contents array
     *
     * @param savefile the file to save to
     * @param contents the contents of the file
     */
    public static void saveToFile(File savefile, String[] contents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(savefile));

        for (String line : contents) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();
    }
}
