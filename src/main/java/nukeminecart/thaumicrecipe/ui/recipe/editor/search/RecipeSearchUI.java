package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.search;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.search.cell.SearchRecipeCellFactory;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * The class that contains all the controller elements and logic for the RecipeSearchUI parent
 */

public class RecipeSearchUI extends ThaumicRecipeUI {

    private static String searchType;
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
        UIManager.loadScreen(getScene(), "search-" + searchType);
        //TODO Fix runtime crash -> test (open the search with an input, then output)
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
        if (ingredientsList.isEmpty() || researchList.isEmpty()) {
            try {
                getListFromFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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
     * Gets the ingredients {@link List} from a {@link File} in the thaumicrecipe folder
     *
     * @throws IOException if the {@link FileParser} has an error
     */
    private void getListFromFile() throws IOException {
        File ingredientsFile = new File(recipeDirectory, "ingredients.lst");
        if (ingredientsFile.exists()) {
            ingredientsList = FileParser.parseList(ingredientsFile);
        }
        File researchFile = new File(recipeDirectory, "research.lst");
        if (researchFile.exists()) {
            researchList = FileParser.parseList(researchFile);
        }
    }

    /**
     * FXML initialize event
     */
    @FXML
    private void initialize() {
        searchList.setCellFactory(new SearchRecipeCellFactory());
        searchField.textProperty().addListener((observableValue, oldText, newText) -> displaySearchPattern(newText));
        displaySearchPattern("");
    }
}
