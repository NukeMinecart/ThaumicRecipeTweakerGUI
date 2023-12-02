package main.java.nukeminecart.thaumicrecipe.ui.recipe.importer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.cell.EditorRecipeCellFactory;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * The class that contains all the controller elements and logic for the RecipeImporterUI parent
 */

public class RecipeImporterUI extends ThaumicRecipeUI {

    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> searchList;

    /**
     * Gets the {@link Parent} container containing all the RecipeImporterUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeImporterUI.fxml if not found
     */

    private Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeImporterUI.class.getResource("RecipeImporterUI.fxml")));
    }

    /**
     * Load the {@link RecipeImporterUI} scene
     */
    public void loadImporter() throws IOException {
        UIManager.loadScreen(getScene(), "importer");
    }

    /**
     * Display a search {@link Pattern} onto the listview
     *
     * @param searchTerm the {@link String} to search for
     */
    private void displaySearchPattern(String searchTerm) {
        searchList.getItems().clear();
        if (recipeList.isEmpty()) return;
        for (String key : recipeList.keySet()) {
            if (Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE).matcher(key).find())
                addToListView(key + stringArraySeparator + recipeList.get(key).getModid());
        }
    }

    /**
     * Add an item to the {@link ListView}
     *
     * @param item the item to add as a {@link String}
     */
    private void addToListView(String item) {
        Platform.runLater(() -> {
            searchList.getItems().add(item);
            searchList.getItems().sort(Comparator.comparing(String::toString));
        });
    }

    /**
     * FXML event to return to {@link RecipeManagerUI} without saving
     */
    @FXML
    private void returnToManager() {
        try {
            new RecipeManagerUI().loadManager();
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * Loads a {@link Recipe} from the list of them in a {@link java.io.File}
     *
     * @param event the {@link MouseEvent}
     */
    @FXML
    private void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedItem = searchList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Recipe changeRecipe = recipeList.get(selectedItem);
                changeEditorRecipe(recipeList.get(changeRecipe.getName()).copy());
                try {
                    new RecipeEditorUI().launchEditor();
                } catch (IOException e) {
                    throwAlert(WarningType.SCENE);
                }
            }

        }
    }

    @FXML
    private void initialize() {
        searchField.textProperty().addListener((observableValue, oldText, newText) -> displaySearchPattern(newText));
        searchField.setTooltip(new Tooltip("Double click an recipe to import it"));
        searchList.setCellFactory(new EditorRecipeCellFactory());
        searchList.setTooltip(new Tooltip("Double click a recipe to import it"));
        searchField.setTooltip(new Tooltip("Filter the list of recipes"));
        displaySearchPattern("");
    }

}
