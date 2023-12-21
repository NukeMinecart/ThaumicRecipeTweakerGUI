package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.search;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private void filterAndSortData(String filterText) {
        Pattern pattern = filterText == null || filterText.isEmpty() ? null :
                Pattern.compile(Pattern.quote(filterText), Pattern.CASE_INSENSITIVE);
        ObservableList<String> items = FXCollections.observableArrayList((searchType.equals("research") ? researchList : ingredientsList).stream()
                .filter(item -> pattern == null || pattern.matcher(item).find())
                .sorted((item1, item2) -> {
                    int score1 = getMatchScore(item1, filterText);
                    int score2 = getMatchScore(item2, filterText);
                    if (score1 == score2) {
                        return item1.compareToIgnoreCase(item2);
                    }
                    return Integer.compare(score2, score1);
                })
                .collect(Collectors.toList()));
        Platform.runLater(() -> searchList.setItems(items));
    }

    /**
     * Get the score of an item to decide order of display in the search {@link ListView}
     *
     * @param itemName   the name of the item
     * @param filterText the filter text
     * @return an {@link Integer} representing the score
     */
    private int getMatchScore(String itemName, String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return 0;
        }
        int score = 0;
        Pattern pattern = Pattern.compile(Pattern.quote(filterText), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(itemName);
        if (matcher.find())
            score += 100 - matcher.start();
        return score;
    }

    /**
     * FXML initialize event
     */
    @FXML
    private void initialize() {
        searchList.setCellFactory(new EditorRecipeCellFactory());
        final Thread[] searchThread = new Thread[1];
        searchField.textProperty().addListener((observableValue, oldText, newText) -> {
            searchThread[0] = new Thread(() -> filterAndSortData(newText));
            searchThread[0].start();
        });
        searchThread[0] = new Thread(() -> filterAndSortData(""));
        searchThread[0].start();
        searchField.requestFocus();
        searchList.setTooltip(new Tooltip("Double click an item to select it"));
        searchField.setTooltip(new Tooltip("Filter the list of " + (searchType.equals("research") ? "research" : "items")));
        title.setText(searchType.equals("research") ? "Recipe Research Search" : "Recipe Item Search");
        filterAndSortData("");
    }
}
