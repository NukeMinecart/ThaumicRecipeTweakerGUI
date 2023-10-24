package main.java.nukeminecart.thaumicrecipe.ui.recipe.file;

public class Recipe {

    private final String[] ingredients;
    private final int vis;
    private final String type, aspects;
    private final Object[] shape;
    public Recipe(String type, String[] ingredients, int vis, String aspects, String... shape){
        this.type = type;
        this.ingredients = ingredients;
        this.vis = vis;
        this.aspects = aspects;
        this.shape = shape;
    }
    public String getAspects(){
        return this.aspects;
    }
    public String[] getIngredients(){
        return this.ingredients;
    }
    public Object[] getShape(){
        return this.shape;
    }
    public int getVis(){
        return this.vis;
    }
    public String getType(){
        return this.type;
    }
}
