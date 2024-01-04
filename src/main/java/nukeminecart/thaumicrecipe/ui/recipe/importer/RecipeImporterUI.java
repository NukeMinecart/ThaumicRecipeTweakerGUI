package main.java.nukeminecart.thaumicrecipe.ui.recipe.importer;

import javafx.collections.FXCollections;
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
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
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
     * Filter the {@link ListView} according to the search {@link TextField}
     *
     * @param filterText the text to filter the {@link ListView}
     */
    private void filterAndSortData(String filterText) {
        Pattern pattern = filterText == null || filterText.isEmpty() ? null :
                Pattern.compile(Pattern.quote(filterText), Pattern.CASE_INSENSITIVE);

        List<Map.Entry<String, Recipe>> toSort = new ArrayList<>();
        for (Map.Entry<String, Recipe> entry : recipeList.entrySet()) {
            if (pattern == null || pattern.matcher(entry.getKey().split(";")[0]).find()) {
                toSort.add(entry);
            }
        }
        toSort.sort((entry1, entry2) -> {
            int score1 = getMatchScore(entry1.getKey().split(";")[0], filterText);
            int score2 = getMatchScore(entry2.getKey().split(";")[0], filterText);
            if (score1 == score2) {
                return entry1.getKey().split(";")[0].compareToIgnoreCase(entry2.getKey().split(";")[0]);
            }
            return Integer.compare(score2, score1);
        });
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Recipe> entry : toSort) {
            list.add(entry.getKey().replace(";", stringArraySeparator));
        }

        searchList.setItems(FXCollections.observableArrayList(list));
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
                Recipe changeRecipe = recipeList.get(selectedItem.replace(stringArraySeparator, ";"));
                changeRecipe.setName(changeRecipe.getName().split(";")[0]);
                changeEditorRecipe(changeRecipe.copy());
                try {
                    new RecipeEditorUI().launchEditor();
                } catch (IOException e) {
                    throwAlert(WarningType.SCENE);
                    throw new RuntimeException(FileParser.compressRecipe(editorRecipe));
                }
            }

        }
    }

    @FXML
    private void initialize() {
        final Thread[] searchThread = new Thread[1];
        searchField.textProperty().addListener((observableValue, oldText, newText) -> {
            searchThread[0] = new Thread(() -> filterAndSortData(newText));
            searchThread[0].start();
        });
        searchThread[0] = new Thread(() -> filterAndSortData(""));
        searchThread[0].start();
        searchField.requestFocus();
        searchField.setTooltip(new Tooltip("Double click an recipe to import it"));
        searchList.setCellFactory(new EditorRecipeCellFactory());
        searchList.setTooltip(new Tooltip("Double click a recipe to import it"));
        searchField.setTooltip(new Tooltip("Filter the list of recipes"));

    }

    /**
     * Sets the current focus to the search bar
     */
    @FXML
    private void setSearchFocus() {
        searchField.requestFocus();
    }

}
