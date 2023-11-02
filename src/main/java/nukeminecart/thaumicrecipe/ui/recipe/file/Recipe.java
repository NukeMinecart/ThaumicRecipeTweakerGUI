package main.java.nukeminecart.thaumicrecipe.ui.recipe.file;

import java.util.List;

/**
 * A type used in ThaumicRecipeTweakerGUI that contains information about a specific recipe
 */
public class Recipe {

    private final String name, input, output, type;
    private final String[] ingredients, aspects;
    private final int vis;
    private final String[] shape;

    /**
     * The constructor to create a new {@link Recipe}
     *
     * @param name        the name
     * @param type        the type
     * @param input       the input item
     * @param ingredients a list of ingredients
     * @param output      the output item
     * @param vis         the vis cost
     * @param aspects     a list of aspects
     * @param shape       a string object containing the shape
     */
    public Recipe(String name, String type, String input, String[] ingredients, String output, int vis, String[] aspects, String... shape) {
        this.name = name;
        this.type = type;
        this.input = input;
        this.ingredients = ingredients;
        this.output = output;
        this.vis = vis;
        this.aspects = aspects;
        this.shape = shape;
    }

    /**
     * Gets the name of the recipe
     *
     * @return the name as a {@link String}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the type of recipe
     *
     * @return the type as a {@link String}
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets the input item of a recipe
     *
     * @return the input item as a {@link String}
     */
    public String getInput() {
        return this.input;
    }

    /**
     * Gets the ingredients of a recipe
     *
     * @return the ingredients as a {@link List<String>}
     */
    public String[] getIngredients() {
        return this.ingredients;
    }

    /**
     * Gets the output item of a recipe
     *
     * @return the output item as a {@link String}
     */
    public String getOutput() {
        return this.output;
    }

    /**
     * Gets the vis cost of a recipe
     *
     * @return the vis cost as a {@link Integer}
     */
    public int getVis() {
        return this.vis;
    }

    /**
     * Gets the aspects of a recipe
     *
     * @return the aspects as a {@link List<String>}
     */
    public String[] getAspects() {
        return this.aspects;
    }


    /**
     * Gets the shape of a recipe
     *
     * @return the shape as a {@link String} array
     */
    public String[] getShape() {
        return this.shape;
    }


}
