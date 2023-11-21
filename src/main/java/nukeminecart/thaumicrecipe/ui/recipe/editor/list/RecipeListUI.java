package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list;

import com.sun.xml.internal.ws.util.StringUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.cell.EditorRecipeCellFactory;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.Comparator;
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
    private static boolean restricted;

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
     * @param type the type of {@link Recipe}
     * @param restricted if the Aspectlist should contain fewer entries
     * @throws IOException if RecipeListUI.fxml is not found
     */
    public void launchListEditor(String type, boolean restricted) throws IOException {
        RecipeListUI.type = type;
        RecipeListUI.restricted = restricted;
        UIManager.loadScreen(getScene(), "list");
    }

    /**
     * Display a search {@link Pattern} onto the listview
     *
     * @param searchTerm the {@link String} to search for
     */
    private void displaySearchPattern(String searchTerm) {
        searchList.getItems().clear();
        if(restricted){
            for (String item : tempAspectList.keySet())
                if (Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE).matcher(item).find())
                    addToListView(item + stringArraySeparator + tempAspectList.get(item));
        }else {
            for (String item : type.equals("ingredients") ? ingredientsList.keySet() : aspectList.keySet())
                if (Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE).matcher(item).find())
                    addToListView(item + stringArraySeparator + (type.equals("ingredients") ? ingredientsList.get(item) : aspectList.get(item)));
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
     * Set up the drag and drop between two {@link ListView}
     *
     * @param source the source of the drag
     * @param target the target of the drag
     */
    private void setUpDragAndDrop(ListView<String> source, ListView<String> target) {
        source.setOnDragDetected(event -> onDragDetected(event, source));
        target.setOnDragOver(event -> allowDragOver(event, target));
        target.setOnDragDropped(event -> handleDrop(event, target));
        target.setOnMouseClicked(event -> handleRemoveDoubleClick(event, target));
    }

    /**
     * Remove an item in the {@link ListView} when double-clicked
     *
     * @param event    the {@link MouseEvent}
     * @param listView the {@link ListView} to enable double-clicking to clear
     */
    private void handleRemoveDoubleClick(MouseEvent event, ListView<String> listView) {
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
            cc.putString(listView.getSelectionModel().getSelectedItem().split(stringArraySeparator)[0]);
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
            if (type.equals("ingredients") && (editorRecipe.getType().equals("normal") || editorRecipe.getType().equals("arcane"))) {
                if (listView.getItems().size() < 9) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
            } else {
                event.acceptTransferModes(TransferMode.COPY);
            }
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
        searchList.setCellFactory(new EditorRecipeCellFactory());

        title.setText("Recipe Editor: " + StringUtils.capitalize(type));
        setUpDragAndDrop(searchList, currentList);
        searchList.setItems(FXCollections.observableArrayList());
        listName.setText("Current " + StringUtils.capitalize(type));
        searchField.textProperty().addListener((observableValue, oldText, newText) -> displaySearchPattern(newText));
        displaySearchPattern("");
        if ((type.equals("ingredients") && editorRecipe.getIngredients() != null)) {
            currentList.setItems(FXCollections.observableArrayList(editorRecipe.getIngredients()));

        } else if ((type.equals("aspects") && editorRecipe.getAspects() != null)) {
            currentList.setItems(FXCollections.observableArrayList(editorRecipe.getAspects()));
        }

    }

    /**
     * Adds the selected {@link javafx.scene.control.ListCell} to the current {@link ListView}
     *
     * @param event the {@link MouseEvent}
     */

    @FXML
    private void handleAddDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (type.equals("ingredients") && (editorRecipe.getType().equals("normal") || editorRecipe.getType().equals("arcane"))) {
                if (currentList.getItems().size() < 9) {
                    currentList.getItems().add(searchList.getSelectionModel().getSelectedItem().split(stringArraySeparator)[0]);
                }
            } else {
                currentList.getItems().add(searchList.getSelectionModel().getSelectedItem());
            }
        }
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

    /**
     * FXML event to clear the items in the current {@link ListView}
     */
    @FXML
    private void clearItems() {
        currentList.getItems().clear();
    }

    /**
     * FXML event to remove the last added item in the current {@link ListView}
     */
    @FXML
    private void removeLastItem() {
        ObservableList<String> items = currentList.getItems();
        if (!items.isEmpty()) {
            items.remove(items.size() - 1);
        }
    }
}
