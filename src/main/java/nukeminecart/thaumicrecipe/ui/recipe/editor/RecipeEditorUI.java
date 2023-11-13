package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor;

import com.sun.xml.internal.ws.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.RecipeListUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.search.RecipeSearchUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape.RecipeShapeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.IOException;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;
import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.stage;

/**
 * The class that contains all the controller elements and logic for the RecipeEditorUI parent
 */

public class RecipeEditorUI extends ThaumicRecipeUI {
    @FXML
    private TextField nameField, visField, inputField, outputField;
    @FXML
    private ListView<String> ingredientsListview, aspectListview;
    @FXML
    private Button aspectsButton, ingredientsButton, inputButton, shapeButton;
    @FXML
    private CheckBox shapelessCheckbox;
    @FXML
    private Label title, inputLabel, ingredientsLabel, visLabel, aspectLabel;
    @FXML
    private MenuButton typeDropdown;

    /**
     * Gets the {@link Parent} container containing all the RecipeEditorUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeEditorUI.fxml if not found
     */
    public Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeEditorUI.class.getResource("RecipeEditorUI.fxml")));
    }

    /**
     * Loads the {@link RecipeEditorUI} scene
     *
     * @throws IOException if RecipeShapeUI.fxml is not found
     */
    public void launchEditor() throws IOException {
        instanceRecipeEditorUI = this;
        if (!cachedScenes.containsKey("editor-" + editorRecipe.getName())) {
            UIManager.loadScreen(getScene(), "editor-" + editorRecipe.getName());
        } else {
            UIManager.loadScreen(cachedScenes.get("editor-" + editorRecipe.getName()));
        }
    }

    /**
     * Load the {@link RecipeEditorUI} from the cachedScenes and with a {@link String} value of the item
     */
    public void launchEditorFromSearch(String item, String type) {
        if (type.equalsIgnoreCase("input")) editorRecipe.setInput(item);
        else editorRecipe.setOutput(item);
        try {
            UIManager.loadScreen(getScene(), "editor-" + editorRecipe.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void launchEditorFromList() {
        try {
            UIManager.loadScreen(getScene(), "editor-" + editorRecipe.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML initialize event
     */
    @FXML
    private void initialize() {
        title.setText("Recipe Editor: " + editorRecipe.getName());
        typeDropdown.setText(StringUtils.capitalize(editorRecipe.getType()));
        inputField.setText(editorRecipe.getInput());
        visField.setText(String.valueOf(editorRecipe.getVis()));
        outputField.setText(editorRecipe.getOutput());
        nameField.setText(editorRecipe.getName());
        ingredientsListview.setItems(FXCollections.observableArrayList(editorRecipe.getIngredients()));
        aspectListview.setItems(FXCollections.observableArrayList(editorRecipe.getAspects()));
        switch (editorRecipe.getType()) {
            case "normal":
                changeToNormalType();
                break;
            case "arcane":
                changeToArcaneType();
                break;
            case "crucible":
                changeToCrucibleType();
                break;
            case "infusion":
                changeToInfusionType();
                break;
        }
    }

    /**
     * FXML event to open the {@link RecipeShapeUI}
     */
    @FXML
    private void openShapeEditor() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeShapeUI().launchShapeEditor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML event to open the {@link RecipeSearchUI} with the input parameter
     */
    @FXML
    private void openInputItemSearch() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeSearchUI().launchItemSearch("input");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML event to open the {@link RecipeSearchUI} with the output parameter
     */
    @FXML
    private void openOutputItemSearch() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeSearchUI().launchItemSearch("output");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML event to open the {@link RecipeListUI} with the aspects parameter
     */
    @FXML
    private void openIngredientsList() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeListUI().launchListEditor("ingredients");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML event to open the {@link RecipeListUI} with the aspects parameter
     */
    @FXML
    private void openAspectsList() {
        try {
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeListUI().launchListEditor("aspects");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns to the {@link RecipeManagerUI} scene without saving the recipe
     */
    @FXML
    private void returnToManager() {
        try {
            RecipeManagerUI.recipeEditorMap.put(originalRecipe.getName(), originalRecipe);
            ThaumicRecipeConstants.instanceRecipeManagerUI.loadManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns to the {@link RecipeManagerUI} scene while saving the recipe in the editor
     */
    @FXML
    private void saveRecipeAndReturn() {
        //TODO Only save the recipe info that needs to be saved
        try {
            if (editorRecipe.getType().equals("normal") || editorRecipe.getType().equals("arcane"))
                editorRecipe.setShape(shapelessCheckbox.isSelected() ? new String[0] : editorRecipe.getShape());
            editorRecipe.setName(nameField.getText());
            RecipeManagerUI.recipeEditorMap.put(editorRecipe.getName(), editorRecipe);
            ThaumicRecipeConstants.instanceRecipeManagerUI.loadManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hide all the {@link Node} specified
     *
     * @param nodes the {@link Node} to hide
     */

    private void hideNodes(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(false);
        }
    }

    /**
     * Show all the {@link Node} specified
     *
     * @param nodes the {@link Node} to show
     */
    private void showNodes(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(true);
        }
    }

    /**
     * FXML event to change the {@link Recipe} type to normal
     */
    @FXML
    private void changeToNormalType() {
        typeDropdown.setText("Normal");
        editorRecipe.setType("normal");
        hideNodes(inputField, inputLabel, inputButton, visLabel, visField, aspectsButton, aspectLabel, aspectListview);
        showNodes(ingredientsButton, ingredientsLabel, ingredientsListview, shapeButton, shapelessCheckbox);
    }

    /**
     * FXML event to change the {@link Recipe} type to arcane
     */
    @FXML
    private void changeToArcaneType() {
        typeDropdown.setText("Arcane");
        editorRecipe.setType("arcane");
        showNodes(inputField, inputLabel, inputButton, visLabel, visField, aspectsButton, aspectLabel, aspectListview, ingredientsButton, ingredientsLabel, ingredientsListview, shapeButton, shapelessCheckbox);
    }

    /**
     * FXML event to change the {@link Recipe} type to crucible
     */
    @FXML
    private void changeToCrucibleType() {
        typeDropdown.setText("Crucible");
        editorRecipe.setType("crucible");
        hideNodes(visField, visLabel, ingredientsLabel, ingredientsButton, ingredientsListview, shapeButton, shapelessCheckbox);
        showNodes(inputLabel, inputField, inputButton, aspectLabel, aspectsButton, aspectListview);
    }

    /**
     * FXML event to change the {@link Recipe} type to infusion
     */
    @FXML
    private void changeToInfusionType() {
        typeDropdown.setText("Infusion");
        editorRecipe.setType("infusion");
        hideNodes(visLabel, visField, shapeButton, shapelessCheckbox);
        showNodes(inputLabel, inputField, inputButton, ingredientsLabel, ingredientsButton, ingredientsListview, aspectLabel, aspectsButton, aspectListview);
    }

    /**
     * FXML event to change if the {@link Recipe} is shapeless
     */
    @FXML
    private void changeShapeless() {
        shapeButton.setVisible(!shapelessCheckbox.isSelected());
    }
}
