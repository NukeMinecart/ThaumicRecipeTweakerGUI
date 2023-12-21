package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor;

import com.sun.xml.internal.ws.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.RecipeListUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.search.RecipeSearchUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape.RecipeShapeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;
import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.stage;

/**
 * The class that contains all the controller elements and logic for the RecipeEditorUI parent
 */

public class RecipeEditorUI extends ThaumicRecipeUI {
    @FXML
    private TextField nameField, visField, inputField, outputField, researchField;
    @FXML
    private ListView<String> ingredientsListview, aspectListview;
    @FXML
    private Button aspectsButton, ingredientsButton, inputButton, shapeButton, researchButton;
    @FXML
    private CheckBox shapelessCheckbox;
    @FXML
    private Label title, inputLabel, ingredientsLabel, visLabel, aspectLabel, researchLabel;
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
     * @throws IOException if RecipeEditorUI.fxml is not found
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
        if (item != null) {
            if (type.equalsIgnoreCase("input")) editorRecipe.setInput(item);
            else if (type.equalsIgnoreCase("output")) editorRecipe.setOutput(item);
            else editorRecipe.setResearch(item);
        }
        try {
            UIManager.loadScreen(getScene(), "editor-" + editorRecipe.getName());
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * Launch the {@link RecipeEditorUI} from the {@link RecipeListUI} since the {@link Scene} needs to update with new values
     */
    public void launchEditorFromList() {
        try {
            UIManager.loadScreen(getScene(), "editor-" + editorRecipe.getName());
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * FXML initialize event
     */
    @FXML
    private void initialize() {
        title.setText("Recipe Editor: " + editorRecipe.getName());
        typeDropdown.setText(StringUtils.capitalize(editorRecipe.getType()));
        inputField.setText(editorRecipe.getInput() == null ? "" : editorRecipe.getInput());
        visField.setText(String.valueOf(editorRecipe.getVis()));
        outputField.setText(editorRecipe.getOutput() == null ? "" : editorRecipe.getOutput());
        nameField.setText(editorRecipe.getName());
        researchField.setText(editorRecipe.getResearch() == null ? "" : editorRecipe.getResearch());
        shapelessCheckbox.setSelected((editorRecipe.getShape() == null || editorRecipe.getShape().length == 0) && editorRecipeExisted);
        shapeButton.setVisible(!shapelessCheckbox.isSelected());
        if (editorRecipe.getIngredients() != null) {
            List<String> tempList = new ArrayList<>();

            for (String key : editorRecipe.getIngredients().keySet()) {
                if (editorRecipe.getIngredients().get(key) == 0) continue;
                tempList.add(key + " x" + editorRecipe.getIngredients().get(key));
            }
            ingredientsListview.setItems(FXCollections.observableArrayList(tempList));
        }
        if (editorRecipe.getAspects() != null) {
            List<String> tempList = new ArrayList<>();

            for (String key : editorRecipe.getAspects().keySet()) {
                if (editorRecipe.getAspects().get(key) == 0) continue;
                tempList.add(key + " x" + editorRecipe.getAspects().get(key));
            }
            aspectListview.setItems(FXCollections.observableArrayList(tempList));
        }
        if (editorRecipe.getType() == null) editorRecipe.setType("normal");
        switch (editorRecipe.getType()) {
            default:
                changeToNormalType();
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
        UnaryOperator<TextFormatter.Change> integerFilter = change -> change.getControlNewText().matches("^([0-9]\\d*)?$") ? change : null;
        visField.setTextFormatter(new TextFormatter<>(integerFilter));

        visField.setOnKeyPressed(this::changeVisValue);
    }

    /**
     * Changes the vis value in the visField according to the {@link KeyCode} pressed
     *
     * @param event the {@link KeyEvent}
     */
    private void changeVisValue(KeyEvent event) {
        int vis;
        try {
            vis = Integer.parseInt(visField.getText());
        } catch (NumberFormatException e) {
            vis = 0;
        }

        switch (event.getCode()) {
            case UP:
                vis = vis + 1;
                visField.setText(String.valueOf(vis));
                break;

            case DOWN:
                vis = vis - 1 == -1 ? 0 : vis - 1;
                visField.setText(String.valueOf(vis));
                break;

            case BACK_SPACE:
                visField.deletePreviousChar();
                break;

            case DELETE:
                visField.deleteNextChar();
                break;
        }
        if (vis < 0) vis = 0;
        editorRecipe.setVis(vis);
        event.consume();
    }

    /**
     * FXML event to open the {@link RecipeShapeUI}
     */
    @FXML
    private void openShapeEditor() {
        try {
            saveRecipe();
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeShapeUI().launchShapeEditor();
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * FXML event to open the {@link RecipeSearchUI} with the input parameter
     */
    @FXML
    private void openInputItemSearch() {
        try {
            saveRecipe();
            new RecipeSearchUI().launchRecipeSearch("input");
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * FXML event to open the {@link RecipeSearchUI} with the output parameter
     */
    @FXML
    private void openOutputItemSearch() {
        try {
            saveRecipe();
            new RecipeSearchUI().launchRecipeSearch("output");
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * FXML event to open the {@link RecipeSearchUI} with the research parameter
     */
    @FXML
    private void openResearchSearch() {
        try {
            saveRecipe();
            new RecipeSearchUI().launchRecipeSearch("research");
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * FXML event to open the {@link RecipeListUI} with the aspects parameter
     */
    @FXML
    private void openIngredientsList() {
        try {
            saveRecipe();
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeListUI().launchListEditor("ingredients", false);
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * FXML event to open the {@link RecipeListUI} with the aspects parameter
     */
    @FXML
    private void openAspectsList() {
        try {
            saveRecipe();
            cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
            new RecipeListUI().launchListEditor("aspects", typeDropdown.getText().equalsIgnoreCase("arcane"));
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * Returns to the {@link RecipeManagerUI} scene without saving the {@link Recipe}
     */
    @FXML
    private void returnToManager() {
        try {
            if (editorRecipeExisted) RecipeManagerUI.recipeEditorMap.put(originalRecipe.getName(), originalRecipe);
            else cachedScenes.remove("editor-" + editorRecipe.getName());
            ThaumicRecipeConstants.instanceRecipeManagerUI.loadManager();
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * Returns to the {@link RecipeManagerUI} scene while saving the {@link Recipe} in the editor
     */
    @FXML
    private void saveRecipeAndReturn() {
        boolean showAlert = checkIfEmpty(nameField.getText(), outputField.getText());

        switch (editorRecipe.getType()) {
            case "normal":
                if (editorRecipe.getIngredients() == null || (editorRecipe.getShape() == null && !shapelessCheckbox.isSelected()))
                    showAlert = true;
                break;
            case "arcane":
                if (checkIfEmpty(visField.getText()) || ingredientsListview.getItems().isEmpty() || editorRecipe.getShape() == null)
                    showAlert = true;
                break;
            case "crucible":
                if (checkIfEmpty(inputField.getText()) || aspectListview.getItems().isEmpty()) showAlert = true;
                break;
            case "infusion":
                if (checkIfEmpty(inputField.getText()) || ingredientsListview.getItems().isEmpty() || aspectListview.getItems().isEmpty())
                    showAlert = true;
                break;
        }
        if (showAlert) {
            throwAlert(WarningType.MISSING);
            return;
        }
        saveRecipe();
        RecipeManagerUI.recipeEditorMap.put(editorRecipe.getName(), editorRecipe);
        cachedScenes.put("editor-" + editorRecipe.getName(), stage.getScene());
        try {
            ThaumicRecipeConstants.instanceRecipeManagerUI.loadManager();
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * Save the current {@link Recipe} parameters
     */
    private void saveRecipe() {
        if (editorRecipe.getType().equals("normal") || editorRecipe.getType().equals("arcane"))
            editorRecipe.setShape(shapelessCheckbox.isSelected() ? new String[0] : editorRecipe.getShape());
        editorRecipe.setName(nameField.getText().isEmpty() ? "" : nameField.getText());
        editorRecipe.setResearch(researchField.getText());
        editorRecipe.setInput(inputField.getText().isEmpty() ? "" : inputField.getText());
        editorRecipe.setOutput(outputField.getText().isEmpty() ? "" : outputField.getText());
        editorRecipe.setVis(Integer.parseInt(visField.getText().isEmpty() ? "0" : visField.getText()));
    }

    /**
     * Check if any of the given {@link String} are empty
     *
     * @param strings the {@link String} to check
     * @return if any {@link String} are empty
     */

    private boolean checkIfEmpty(String... strings) {
        for (String string : strings) {
            if (string == null || string.isEmpty()) {
                return true;
            }
        }
        return false;
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
        visLabel.setText("Vis Amount");
        showNodes(ingredientsButton, ingredientsLabel, ingredientsListview, shapeButton, shapelessCheckbox);
        hideNodes(inputField, inputLabel, inputButton, visLabel, visField, aspectsButton, aspectLabel, aspectListview, researchField, researchLabel, researchButton);
    }

    /**
     * FXML event to change the {@link Recipe} type to arcane
     */
    @FXML
    private void changeToArcaneType() {
        typeDropdown.setText("Arcane");
        editorRecipe.setType("arcane");
        visLabel.setText("Vis Amount");
        showNodes(visLabel, visField, aspectsButton, aspectLabel, aspectListview, ingredientsButton, ingredientsLabel, ingredientsListview, shapeButton, shapelessCheckbox, researchField, researchLabel, researchButton);
        hideNodes(inputLabel, inputField, inputButton);
    }

    /**
     * FXML event to change the {@link Recipe} type to crucible
     */
    @FXML
    private void changeToCrucibleType() {
        typeDropdown.setText("Crucible");
        editorRecipe.setType("crucible");
        visLabel.setText("Vis Amount");
        showNodes(inputLabel, inputField, inputButton, aspectLabel, aspectsButton, aspectListview, researchField, researchLabel, researchButton);
        hideNodes(visField, visLabel, ingredientsLabel, ingredientsButton, ingredientsListview, shapeButton, shapelessCheckbox);
    }

    /**
     * FXML event to change the {@link Recipe} type to infusion
     */
    @FXML
    private void changeToInfusionType() {
        typeDropdown.setText("Infusion");
        editorRecipe.setType("infusion");
        visLabel.setText("Instability");
        showNodes(visLabel, visField, inputLabel, inputField, inputButton, ingredientsLabel, ingredientsButton, ingredientsListview, aspectLabel, aspectsButton, aspectListview, researchField, researchLabel, researchButton);
        hideNodes(shapeButton, shapelessCheckbox);
    }

    /**
     * FXML event to change if the {@link Recipe} is shapeless
     */
    @FXML
    private void changeShapeless() {
        shapeButton.setVisible(!shapelessCheckbox.isSelected());
    }
}
