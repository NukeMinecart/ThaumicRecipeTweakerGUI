package main.java.nukeminecart.thaumicrecipe.ui.recipe.file.versions;

import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.IFileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.util.ArrayList;
import java.util.List;

public class FileParserV1 implements IFileParser {

    private final String separator = "-=-";
    private final String arraySeparator = "-";
    @Override
    public String loadLine(String[] file, int line) {
        return file[line];
    }
    @Override
    public String compressRecipe(Recipe recipe) {
        StringBuilder returnRecipe;
        returnRecipe = new StringBuilder(recipe.getName()+separator);
        returnRecipe.append(recipe.getType()).append(separator);
        for(String ingredient : recipe.getIngredients()){
            returnRecipe.append(ingredient).append(arraySeparator);
        }
        returnRecipe.append(separator);
        returnRecipe.append(recipe.getOutput()).append(separator);

        returnRecipe.append(recipe.getVis()).append(separator);
        for(String aspect : recipe.getAspects()){
            returnRecipe.append(aspect).append(arraySeparator);
        }
        returnRecipe.append(separator);
        for(Object shape : recipe.getShape()){
            returnRecipe.append(shape).append(arraySeparator);
        }
        return returnRecipe.toString();
    }

    @Override
    public Recipe parseRecipe(String line) {
        String[] compressedRecipe = line.split(separator);
        String name = compressedRecipe[0];
        String type = compressedRecipe[1];
        String[] ingredients = compressedRecipe[2].split(arraySeparator);
        String output = compressedRecipe[3];
        int vis = Integer.parseInt(compressedRecipe[4]);
        String[] aspects = compressedRecipe[5].split(arraySeparator);
        String[] shape = compressedRecipe[6].split(arraySeparator);

        return new Recipe(name, type, ingredients, output, vis, aspects, shape);
    }
    @Override
    public Recipe[] getRecipesFromString(List<String> contents){
        List<Recipe> recipes = new ArrayList<>();
        for (String line : contents){
            recipes.add(new FileParserV1().parseRecipe(line));
        }
        return recipes.toArray(new Recipe[0]);
    }
}
