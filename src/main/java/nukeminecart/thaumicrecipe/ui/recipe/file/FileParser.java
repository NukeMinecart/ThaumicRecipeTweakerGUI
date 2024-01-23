package main.java.nukeminecart.thaumicrecipe.ui.recipe.file;

import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * A class used for all reading of files and changing of a {@link String} to a {@link Recipe}
 */
public class FileParser {
    private static boolean parseError;


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
     * @throws IOException if the file cannot be read, found, and if an i/o exception occurs
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
     * Parses a {@link List} file
     *
     * @param file the file to parse to a list
     * @return a {@link java.util.HashMap} of the file contents separated
     */
    public static List<String> parseList(File file) throws IOException {
        List<String> lines = readFile(file);
        return new ArrayList<>(lines);
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
        returnRecipe.append(recipe.getResearch()).append(stringSeparator);
        returnRecipe.append(recipe.getModid()).append(stringSeparator);
        if (!recipe.getInput().contains(" x"))
            returnRecipe.append(recipe.getInput()).append(mapSeparator).append(1).append(stringSeparator);
        else
            returnRecipe.append(recipe.getInput().replace(" x", mapSeparator)).append(stringSeparator);
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty())
            for (String ingredient : recipe.getIngredients().keySet())
                returnRecipe.append(ingredient).append(mapSeparator).append(recipe.getIngredients().get(ingredient)).append(stringArraySeparator);
        returnRecipe.append(stringSeparator);
        if (!recipe.getOutput().contains(" x"))
            returnRecipe.append(recipe.getOutput()).append(mapSeparator).append(1).append(stringSeparator);
        else
            returnRecipe.append(recipe.getOutput().replace(" x", mapSeparator)).append(stringSeparator);

        returnRecipe.append(recipe.getVis()).append(stringSeparator);
        if (recipe.getAspects() != null && !recipe.getAspects().isEmpty())
            for (String aspect : recipe.getAspects().keySet())
                returnRecipe.append(aspect).append(mapSeparator).append(recipe.getAspects().get(aspect)).append(stringArraySeparator);

        returnRecipe.append(stringSeparator);
        if (recipe.getShape() != null) for (Object shape : recipe.getShape())
            returnRecipe.append(shape).append(stringArraySeparator);


        return returnRecipe.toString();
    }

    /**
     * Split a {@link String} according to the splitString
     *
     * @param string      the {@link String to split}
     * @param splitString the {@link String} to split with
     * @return a {@link String} array
     */
    public static String[] splitString(String string, String splitString) {
        List<String> strings = new ArrayList<>();
        for (String endString = string; string.contains(splitString); ) {
            if (endString.contains(splitString)) strings.add(endString.substring(0, endString.indexOf(splitString)));
            else {
                strings.add(endString);
                break;
            }
            endString = endString.substring(endString.indexOf(splitString) + splitString.length());
        }
        return strings.toArray(new String[0]);
    }

    /**
     * Changes custom string recipe to a {@link main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe}
     *
     * @param line the line to parse
     * @return a recipe class
     * @throws ArrayIndexOutOfBoundsException if the line is missing necessary recipe information
     */
    public static Recipe parseRecipe(String line) {
        String[] compressedRecipe = splitString(line, stringSeparator);
        String name = null;
        String type = null;
        String research = null;
        String modid = null;
        String input = null;
        HashMap<String, Integer> ingredients = new HashMap<>();
        String output = null;
        int vis = 0;
        HashMap<String, Integer> aspects = new HashMap<>();
        List<String> shape = new ArrayList<>();
        try {
            name = compressedRecipe[0];
            type = compressedRecipe[1];
            research = compressedRecipe[2];
            modid = compressedRecipe[3];
            input = compressedRecipe[4];
            ingredients = new HashMap<>();
            if (!compressedRecipe[5].isEmpty())
                for (String ingredient : compressedRecipe[5].split(stringArraySeparator))
                    ingredients.put(ingredient.split(mapSeparator)[0], Integer.parseInt(ingredient.split(mapSeparator)[1]));
            output = compressedRecipe[6];
            try {
                vis = Integer.parseInt(compressedRecipe[7]);
            } catch (NumberFormatException ignored) {
            }
            aspects = new HashMap<>();
            if (!compressedRecipe[8].isEmpty())
                for (String aspect : compressedRecipe[8].split(stringArraySeparator))
                    aspects.put(aspect.split(mapSeparator)[0], Integer.parseInt(aspect.split(mapSeparator)[1]));


            for (String shapeElement : compressedRecipe[9].split(stringArraySeparator)) {
                for (int i = 0; i < countOccurrences(shapeElement, "null"); i++)
                    shape.add("");

                shapeElement = shapeElement.replace("null", "");
                shape.add(shapeElement);
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            parseError = true;
        }
        if (shape.toArray(new String[0]).length > 9)
            shape.remove(9);
        return new Recipe(name, type, research, modid, input, ingredients, output, vis, aspects, shape.toArray(new String[0]));
    }

    /**
     * Returns the count of how many times a substring appears in the given string.
     *
     * @param str    The string to search in.
     * @param subStr The substring to find in the string.
     * @return The count of occurrences of the substring.
     */
    public static int countOccurrences(String str, String subStr) {
        int count = 0;
        int idx = 0;

        while ((idx = str.indexOf(subStr, idx)) != -1) {
            count++;
            idx += subStr.length();
        }

        return count;
    }

    /**
     * Returns all the recipes from a list of strings
     *
     * @param contents the list to get recipes from
     * @return an array of recipes
     */
    public static Recipe[] getRecipesFromString(List<String> contents) {
        List<Recipe> recipes = new ArrayList<>();
        parseError = false;
        for (String line : contents) {
            recipes.add(parseRecipe(line));
        }
        if (parseError) new ThaumicRecipeUI().throwAlert(ThaumicRecipeUI.WarningType.PARSE);
        return recipes.toArray(new Recipe[0]);
    }

    /**
     * Writes to the file with each line corresponding to an index in the contents array
     *
     * @param savefile the file to save to
     * @param contents the contents of the file
     * @throws IOException if the file cannot be written to, doesn't exist, and if an i/o error occurs
     */
    public static void saveToFile(File savefile, List<String> contents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(savefile));

        for (String line : contents) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();
    }

    /**
     * Saves a {@link List} of {@link Recipe} to a {@link File} after compressing them
     *
     * @param recipes      the {@link List} of {@link Recipe} to save
     * @param saveLocation the location to save the {@link Recipe} {@link File}
     * @throws IOException if an i/o error occurs
     */
    public static void saveRecipesToFile(File saveLocation, List<Recipe> recipes) throws IOException {
        List<String> compressedRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            compressedRecipes.add(compressRecipe(recipe));
        }
        saveToFile(saveLocation, compressedRecipes);
    }

    /**
     * Sets the current ThaumicRecipeTweakerGUI config file to the selected options
     *
     * @param activeRecipe set the active recipe
     * @param openGUI      if the GUI should open the next time the game launches
     * @throws IOException if the file cannot be written to, read, doesn't exist, and if an i/o error occurs
     */
    public static void setConfigOptions(String activeRecipe, boolean openGUI) throws IOException {
        File configFile = new File(recipeDirectory, "recipe.cfg");
        List<String> contents = new ArrayList<>();
        if (!configFile.createNewFile()) {
            contents = readFile(configFile);
        }
        if (contents.isEmpty()) {
            contents.add(activeRecipe.isEmpty() ? null : ("active-recipe:" + activeRecipe));
        } else {
            String currentActiveRecipe = contents.get(0).replace("active-recipe:", "");
            contents.clear();
            contents.add("active-recipe:" + (activeRecipe.isEmpty() ? currentActiveRecipe : activeRecipe));
        }
        contents.add("open-gui:" + openGUI);
        saveToFile(configFile, contents);
    }
}
