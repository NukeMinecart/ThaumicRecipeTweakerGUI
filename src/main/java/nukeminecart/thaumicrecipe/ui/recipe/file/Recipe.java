package main.java.nukeminecart.thaumicrecipe.ui.recipe.file;

public class Recipe {

    private final String name;
    private final String[] ingredients;
    private final String output;
    private final int vis;
    private final String type, aspects;
    private final Object[] shape;
    public Recipe(String name, String type, String[] ingredients,String output, int vis, String aspects, String... shape){
        this.name = name;
        this.type = type;
        this.ingredients = ingredients;
        this.output = output;
        this.vis = vis;
        this.aspects = aspects;
        this.shape = shape;
    }
    public String getName(){
        return this.name;
    }

    public String getAspects(){
        return this.aspects;
    }
    public String[] getIngredients(){
        return this.ingredients;
    }
    public String getOutput(){return this.output;}

    public Object[] getShape(){
        return this.shape;
    }
    public int getVis(){
        return this.vis;
    }
    public String getType(){return this.type;}
}
