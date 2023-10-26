package main.java.nukeminecart.thaumicrecipe.ui.recipe;

import javafx.scene.control.Label;
import main.java.nukeminecart.thaumicrecipe.ui.ErrorWarning;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.IFileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.loadedRecipe;

public class RecipeHandler {
    public static File file;
    private static Label newWarning;
    public static void loadConfigRecipe() throws IOException {
        if(loadedRecipe!=null) {
            file = IFileParser.getFolderFile(loadedRecipe.endsWith(".rcp") ? loadedRecipe : loadedRecipe + ".rcp");
            List<String> contents = IFileParser.readFile(file);
            RecipeManagerUI.loadManager(file.getName(),contents);
        }else{
            ErrorWarning.invalidFileError(newWarning,"No file in config");
        }
    }
    public static void loadFolderRecipe(String filename) throws IOException {
        file = IFileParser.getFolderFile(filename);
        List<String> contents = IFileParser.readFile(file);
        RecipeManagerUI.loadManager(filename,contents);

    }
    public static void loadOtherRecipe(String path) throws IOException {
        file = new File(path);
        List<String> contents = IFileParser.readFile(IFileParser.checkExists(file) ? file : null) ;
        RecipeManagerUI.loadManager(file.getName(),contents);
    }
    public static void newRecipeGroup(String name) throws IOException {
        file = IFileParser.getFolderFile(name.endsWith(".rcp") ? name : name+".rcp");
        if(file.exists()){
            ErrorWarning.invalidFileError(newWarning,"File already exists");
        }else{
            RecipeManagerUI.loadManager(name,null);
        }
    }
    public static void newRecipe(String name) throws IOException {
        file = IFileParser.getFolderFile(name.endsWith(".rcp") ? name : name+".rcp");
        if(file.exists()){
            ErrorWarning.invalidFileError(newWarning,"File already exists");
        }else{
            RecipeManagerUI.loadManager(name,null);
        }
    }
    public static void setLabel(Label newWarning) {
        RecipeHandler.newWarning=newWarning;
    }

    public static void saveToFile(File savefile, String[] contents){
        //TODO
    }

}

