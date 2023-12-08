package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.search;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.cell.EditorRecipeCellFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * The class that contains all the controller elements and logic for the RecipeSearchUI parent
 */

public class RecipeSearchUI extends ThaumicRecipeUI {

    private static String searchType;
    @FXML
    private Label title;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> searchList;

    /**
     * Gets the {@link Parent} container containing all the RecipeSearchUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeSearchUI.fxml if not found
     */
    public Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeSearchUI.class.getResource("RecipeSearchUI.fxml")));
    }

    /**
     * Launch the {@link RecipeSearchUI} scene
     *
     * @throws IOException if RecipeSearchUI.fxml if not found
     */
    public void launchRecipeSearch(String searchType) throws IOException {
        RecipeSearchUI.searchType = searchType;
        if (!cachedScenes.containsKey("search-" + searchType)) {
            UIManager.loadScreen(getScene(), "search-" + searchType);
        } else {
            UIManager.loadScreen(cachedScenes.get("search-" + searchType));
        }
    }

    /**
     * Set the selected item to the {@link javafx.scene.control.ListCell} that was double-clicked
     *
     * @param event the {@link MouseEvent}
     */
    @FXML
    private void handleDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            instanceRecipeEditorUI.launchEditorFromSearch(searchList.getSelectionModel().getSelectedItem().split(stringArraySeparator)[0], searchType);
        }
    }

    /**
     * FXML event to return to {@link RecipeEditorUI} without saving
     */
    @FXML
    private void returnToEditor() {
        instanceRecipeEditorUI.launchEditorFromSearch(null, searchType);
    }

    /**
     * Display a search {@link Pattern} onto the listview
     *
     * @param searchTerm the {@link String} to search for
     */
    private void displaySearchPattern(String searchTerm) {
        searchList.getItems().clear();
        for (String item : searchType.equals("input") || searchType.equals("output") ? ingredientsList.keySet() : researchList.keySet()) {
            if (Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE).matcher(item).find())
                addToListView(item + stringArraySeparator + (searchType.equals("input") || searchType.equals("output") ? ingredientsList.get(item) : researchList.get(item)));
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
     * FXML initialize event
     */
    @FXML
    private void initialize() {
        searchList.setCellFactory(new EditorRecipeCellFactory());
        searchField.textProperty().addListener((observableValue, oldText, newText) -> displaySearchPattern(newText));
        searchList.setTooltip(new Tooltip("Double click an item to select it"));
        searchField.setTooltip(new Tooltip("Filter the list of " + (searchType.equals("research") ? "research" : "items")));
        title.setText(searchType.equals("research") ? "Recipe Research Search" : "Recipe Item Search");
        displaySearchPattern("");
    }
}
