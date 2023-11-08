package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * The class that contains all the controller elements and logic for the RecipeListUI parent
 */

public class RecipeListUI extends ThaumicRecipeUI {
    private static String type;
    @FXML
    private ListView<String> searchList;
    @FXML
    private Label title, warningLabel;
    @FXML
    private TextField searchField;

    /**
     * Gets the {@link Parent} container containing all the RecipeListUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeListUI.fxml if not found
     */
    public Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeListUI.class.getResource("RecipeListUI.fxml")));
    }

    /**
     * Loads the listEditor scene and sets the type of editor
     *
     * @throws IOException if RecipeListUI.fxml is not found
     */
    public void launchListEditor(String type) throws IOException {
        RecipeListUI.type = type;
        UIManager.loadScreen(getScene(), "shape");
    }

    /**
     * Display a search {@link Pattern} onto the listview
     *
     * @param searchTerm the {@link String} to search for
     */
    private void displaySearchPattern(String searchTerm) {
        if (ingredientsList.isEmpty() || aspectList.isEmpty()) {
            try {
                getListsFromFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        searchList.getItems().clear();
        for (String item : ingredientsList.keySet()) {
            if (Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE).matcher(item).find()) addToListView(item);
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
     * Gets the {@link List} from a {@link File} in the thaumicrecipe folder
     *
     * @throws IOException if the {@link FileParser} has an error
     */
    private void getListsFromFile() throws IOException {
        File ingredientsFile = new File(recipeDirectory, "ingredients.lst");
        if (ingredientsFile.exists()) {
            ingredientsList = FileParser.parseList(ingredientsFile);
        }
        File aspectsFile = new File(recipeDirectory, "aspects.lst");
        if (aspectsFile.exists()) {
            aspectList = FileParser.parseList(aspectsFile);
        }
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        title.setText("Recipe Editor: " + type);
        searchList.setItems(FXCollections.observableArrayList());
        searchField.textProperty().addListener((observableValue, oldText, newText) -> displaySearchPattern(newText));
        displaySearchPattern("");
    }
}
