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
    static String[] readFile(File file) throws IOException {
        FileReader fileReader;
        fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        while (reader.readLine() != null){
            lines.add(reader.readLine());
        }
        reader.close();
        return lines.toArray(new String[0]);
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
     * This will sign the recipe after compression with a
     * version number
     **/
    String signRecipe(String recipe);

    Recipe[] getRecipesFromString(String[] contents);
}
