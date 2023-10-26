package main.java.nukeminecart.thaumicrecipe.ui.recipe.file;

import main.java.nukeminecart.thaumicrecipe.ui.UIManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public interface IFileParser {

    //Load Methods
    /**
     * This will load a line from the file
     **/
    String loadLine(String[] file, int line);
    /**
     * This will get the file from the thaumicrecipe folder
     **/
    static File getFolderFile(String name){
        return new File(UIManager.recipeDir+name);
    }
    static List<String> readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();

        for(String line = reader.readLine(); line!=null; line = reader.readLine()){
            lines.add(line);
        }

        reader.close();
        return lines;
    }
    /**
     * This check if file exists
     **/
    static boolean checkExists(File file){
        return file.exists() && file.isFile();
    }


    //Recipe Methods
    /**
     * This will take a recipe and compress it into a single line
     * to be saved in a file
     **/
    String compressRecipe(Recipe recipe);
    /**
     * This will get the recipe from a line in the file
     **/
    Recipe parseRecipe(String line);
    /**
     * This will get all the recipes from a list of strings (lines)
     **/
    Recipe[] getRecipesFromString(List<String> contents);
}
