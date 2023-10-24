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
        returnRecipe = new StringBuilder(recipe.getType() + separator);
        for(String ingredient : recipe.getIngredients()){
            returnRecipe.append(ingredient).append(arraySeparator);
        }
        returnRecipe.append(separator);
        returnRecipe.append(recipe.getVis()).append(separator);
        returnRecipe.append(recipe.getAspects()).append(separator);
        for(Object shape : recipe.getShape()){
            returnRecipe.append(shape).append(arraySeparator);
        }
        return returnRecipe.toString();
    }

    @Override
    public Recipe parseRecipe(String line) {
        String[] compressedRecipe = line.split(separator);
        String type = compressedRecipe[0];
        String[] ingredients = compressedRecipe[1].split(arraySeparator);
        int vis = Integer.parseInt(compressedRecipe[2]);
        String aspects = compressedRecipe[3];
        String[] shape = compressedRecipe[4].split(arraySeparator);

        return new Recipe(type, ingredients, vis, aspects, shape);
    }

    @Override
    public String signRecipe(String recipe) {
        return "V1:"+recipe;
    }
    @Override
    public Recipe[] getRecipesFromString(String[] contents){
        List<Recipe> recipes = new ArrayList<>();
        for (String line : contents){
            recipes.add(new FileParserV1().parseRecipe(line));
        }
        return recipes.toArray(new Recipe[0]);
    }
}
