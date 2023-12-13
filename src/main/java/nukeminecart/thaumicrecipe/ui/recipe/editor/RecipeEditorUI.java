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
        if (!cachedScenes.containsKey("editor-" + editorRecipe.getName().replace(" ", ""))) {
            UIManager.loadScreen(getScene(), "editor-" + editorRecipe.getName().replace(" ", ""));
        } else {
            UIManager.loadScreen(cachedScenes.get("editor-" + editorRecipe.getName().replace(" ", "")));
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
        //Todo Fix crash and shapeless
        title.setText("Recipe Editor: " + editorRecipe.getName());
        typeDropdown.setText(StringUtils.capitalize(editorRecipe.getType()));
        inputField.setText(editorRecipe.getInput() == null ? "" : editorRecipe.getInput());
        visField.setText(String.valueOf(editorRecipe.getVis()));
        outputField.setText(editorRecipe.getOutput() == null ? "" : editorRecipe.getOutput());
        nameField.setText(editorRecipe.getName());
        researchField.setText(editorRecipe.getResearch() == null ? "" : editorRecipe.getResearch());
        shapelessCheckbox.setSelected(editorRecipe.getShape() == null || editorRecipe.getShape().length == 0);
        shapeButton.setVisible(editorRecipe.getShape() != null || editorRecipe.getShape().length != 0);

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
        visField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                visField.setText(newValue.replaceAll("\\D", ""));
            }
            if (!visField.getText().isEmpty()) {
                int vis = Integer.parseInt(visField.getText());
                editorRecipe.setVis(vis);
            }
        });
        visField.setOnKeyPressed(this::changeVisValue);
    }

    /**
     * Changes the vis value in the visField according to the {@link KeyCode} pressed
     *
     * @param event the {@link KeyEvent}
     */
    private void changeVisValue(KeyEvent event) {
        int vis = editorRecipe.getVis();
        if (vis >= 0) {
            switch (event.getCode()) {
                case UP:
                    visField.setText(String.valueOf(vis + 1));
                    break;
                case DOWN:
                    visField.setText(String.valueOf((vis - 1) == -1 ? 0 : vis - 1));
                    break;
            }
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
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * FXML event to open the {@link RecipeSearchUI} with the input parameter
     */
    @FXML
    private void openInputItemSearch() {
        try {
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
        Alert alert = new Alert(Alert.AlertType.ERROR, "One or more field(s) are missing", ButtonType.CLOSE);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setHeaderText("Invalid inputs");
        dialogPane.getStylesheets().add(Objects.requireNonNull(ThaumicRecipeConstants.class.getResource("resources/Dark.css")).toExternalForm());
        dialogPane.setStyle("-fx-background-color: -fx-color");
        alert.initOwner(stage.getOwner());

        if (checkIfEmpty(nameField.getText(), outputField.getText())) {
            alert.show();
            return;
        }
        switch (editorRecipe.getType()) {
            case "normal":
                if (editorRecipe.getIngredients() == null || (editorRecipe.getShape() == null && !shapelessCheckbox.isSelected())) {
                    alert.show();
                    return;
                }
                break;
            case "arcane":
                if (checkIfEmpty(visField.getText()) || ingredientsListview.getItems().isEmpty() || aspectListview.getItems().isEmpty() || editorRecipe.getShape() == null) {
                    alert.show();
                    return;
                }
                break;
            case "crucible":
                if (checkIfEmpty(inputField.getText()) || aspectListview.getItems().isEmpty()) {
                    alert.show();
                    return;
                }
                break;
            case "infusion":
                if (checkIfEmpty(inputField.getText()) || ingredientsListview.getItems().isEmpty() || aspectListview.getItems().isEmpty()) {
                    alert.show();
                    return;
                }
                break;
        }
        if (editorRecipe.getType().equals("normal") || editorRecipe.getType().equals("arcane"))
            editorRecipe.setShape(shapelessCheckbox.isSelected() ? new String[0] : editorRecipe.getShape());
        editorRecipe.setName(nameField.getText());
        editorRecipe.setResearch(researchField.getText());
        RecipeManagerUI.recipeEditorMap.put(editorRecipe.getName(), editorRecipe);
        try {
            ThaumicRecipeConstants.instanceRecipeManagerUI.loadManager();
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
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
        showNodes(inputLabel, inputField, inputButton, ingredientsLabel, ingredientsButton, ingredientsListview, aspectLabel, aspectsButton, aspectListview, researchField, researchLabel, researchButton);
        hideNodes(visLabel, visField, shapeButton, shapelessCheckbox);
    }

    /**
     * FXML event to change if the {@link Recipe} is shapeless
     */
    @FXML
    private void changeShapeless() {
        shapeButton.setVisible(!shapelessCheckbox.isSelected());
    }
}
