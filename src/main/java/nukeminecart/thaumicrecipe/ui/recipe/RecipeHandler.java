package main.java.nukeminecart.thaumicrecipe.ui.recipe;

import main.java.nukeminecart.thaumicrecipe.ui.home.HomeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.IFileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.File;
import java.io.IOException;

import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.loadedRecipe;

public class RecipeHandler {
    public static File file;
    public static void loadConfigRecipe() throws IOException {
        file = IFileParser.getFolderFile(loadedRecipe);
        String[] contents = IFileParser.readFile(file);
        RecipeManagerUI.loadManager(contents);
    }
    public static void loadFolderRecipe(String filename) throws IOException {
        file = IFileParser.getFolderFile(filename);
        String[] contents = IFileParser.readFile(file);
        RecipeManagerUI.loadManager(contents);

    }
    public static void loadOtherRecipe(String path) throws IOException {
        file = new File(path);
        String[] contents = IFileParser.readFile(IFileParser.checkExists(file) ? file : null) ;
        RecipeManagerUI.loadManager(contents);
    }
    public static void newRecipeGroup(String name) throws IOException {
        file = IFileParser.getFolderFile(name.endsWith(".rcp") ? name : name+".rcp");
        if(file.exists()){
            new HomeUI().throwNewWarning("File already exists");
        }else{
            RecipeManagerUI.loadManager(null);
        }
    }
    public static void newRecipe(String name) throws IOException {
        file = IFileParser.getFolderFile(name.endsWith(".rcp") ? name : name+".rcp");
        if(file.exists()){
            //TODO Fix crash -> new instance of class (Don't do a new instance)
            new HomeUI().throwNewWarning("File already exists");
        }else{
            RecipeManagerUI.loadManager(null);
        }
    }

    public static void saveToFile(File savefile, String[] contents){
        //TODO
    }
}

