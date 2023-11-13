package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list;

import com.sun.xml.internal.ws.util.StringUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
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
    private ListView<String> searchList, currentList;
    @FXML
    private Label title, listName;
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
        UIManager.loadScreen(getScene(), "list");
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
        for (String item : type.equals("ingredients") ? ingredientsList.keySet() : aspectList.keySet()) {
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
        ingredientsList.put("test", "test");
    }

    /**
     * Set up the drag and drop between two {@link ListView}
     *
     * @param source the source of the drag
     * @param target the target of the drag
     */
    private void setUpDragAndDrop(ListView<String> source, ListView<String> target) {
        source.setOnDragDetected(event -> onDragDetected(event, source));
        target.setOnDragOver(event -> allowDragOver(event, target));
        target.setOnDragDropped(event -> handleDrop(event, target));
        target.setOnMouseClicked(event -> handleDoubleClick(event, target));
    }

    /**
     * Remove an item in the {@link ListView} when double-clicked
     *
     * @param event    the {@link MouseEvent}
     * @param listView the {@link ListView} to enable double-clicking to clear
     */
    private void handleDoubleClick(MouseEvent event, ListView<String> listView) {
        if (event.getClickCount() == 2) {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                listView.getItems().remove(selectedItem);
            }
        }
    }

    /**
     * Start the drag when the user starts dragging
     *
     * @param event    the {@link MouseEvent}
     * @param listView the {@link ListView} to drag from
     */
    private void onDragDetected(MouseEvent event, ListView<String> listView) {
        if (!listView.getSelectionModel().isEmpty()) {
            Dragboard db = listView.startDragAndDrop(TransferMode.COPY);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(listView.getSelectionModel().getSelectedItem());
            db.setContent(cc);
            event.consume();
        }
    }


    /**
     * Allow the drop and display the result
     *
     * @param event    the {@link DragEvent}
     * @param listView the {@link ListView} to allow the drag over for
     */
    private void allowDragOver(DragEvent event, ListView<String> listView) {
        if (event.getGestureSource() != listView && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    /**
     * Handle the drop event
     *
     * @param event    the {@link DragEvent}
     * @param listView the {@link ListView} to allow the drop for
     */

    private void handleDrop(DragEvent event, ListView<String> listView) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            listView.getItems().add(db.getString());
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        title.setText("Recipe Editor: " + StringUtils.capitalize(type));
        setUpDragAndDrop(searchList, currentList);
        searchList.setItems(FXCollections.observableArrayList());
        listName.setText("Current " + StringUtils.capitalize(type));
        searchField.textProperty().addListener((observableValue, oldText, newText) -> displaySearchPattern(newText));
        displaySearchPattern("");
        currentList.setItems(FXCollections.observableArrayList(type.equals("ingredients") ? editorRecipe.getIngredients() : editorRecipe.getAspects()));
    }

    /**
     * FXML event to save the list and return to the {@link RecipeEditorUI}
     */

    public void saveList() {
        if (type.equals("ingredients")) {
            editorRecipe.setIngredients(currentList.getItems().toArray(new String[0]));
        } else {
            editorRecipe.setAspects(currentList.getItems().toArray(new String[0]));
        }
        returnToEditor();
    }

    /**
     * FXML event to return to {@link RecipeEditorUI}
     */
    @FXML
    private void returnToEditor() {
        instanceRecipeEditorUI.launchEditorFromList();
    }
}
