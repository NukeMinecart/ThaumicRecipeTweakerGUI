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
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.cell.EditorRecipeCellFactory;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.cell.ListRecipeCellFactory;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * The class that contains all the controller elements and logic for the RecipeListUI parent
 */

public class RecipeListUI extends ThaumicRecipeUI {
    public static HashMap<String, Integer> amountMap = new HashMap<>();
    private static String type;
    private static List<String> set;
    @FXML
    private ListView<String> searchList, currentList;
    @FXML
    private Label title, currentListName, allListName;
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
     * @param type       the type of {@link Recipe}
     * @param restricted if the Aspectlist should contain fewer entries
     * @throws IOException if RecipeListUI.fxml is not found
     */
    public void launchListEditor(String type, boolean restricted) throws IOException {
        instanceRecipeListUI = this;
        RecipeListUI.type = type;
        amountMap = type.equals("ingredients") ? editorRecipe.getIngredients() : editorRecipe.getAspects();
        if (amountMap == null) amountMap = new HashMap<>();

        if (restricted) set = tempAspectList;
        else set = type.equals("ingredients") ? ingredientsList : aspectList;
        if (!cachedScenes.containsKey("list-" + type + ":" + restricted))
            UIManager.loadScreen(getScene(), "list-" + editorRecipe.getName() + ":" + type + ":" + restricted);
        else UIManager.loadScreen(cachedScenes.get("list-" + editorRecipe.getName() + ":" + type + ":" + restricted));
    }

    /**
     * Filter the {@link ListView} according to the search {@link TextField}
     *
     * @param filterText the text to filter the {@link ListView}
     */
    private void filterAndSortData(String filterText) {
        Pattern pattern = filterText == null || filterText.isEmpty() ? null : Pattern.compile(Pattern.quote(filterText), Pattern.CASE_INSENSITIVE);

        ObservableList<String> items = FXCollections.observableArrayList(set.stream().filter(item -> pattern == null || pattern.matcher(item).find()).sorted((item1, item2) -> {
            int score1 = getMatchScore(item1, filterText);
            int score2 = getMatchScore(item2, filterText);
            if (score1 == score2) {
                return item1.compareToIgnoreCase(item2);
            }
            return Integer.compare(score2, score1);
        }).collect(Collectors.toList()));
        items.forEach(StringUtils::capitalize);
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
        if (matcher.find()) score += 100 - matcher.start();

        return score;
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
        target.setOnDragDropped(this::handleDrop);
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
                amountMap.remove(selectedItem.split(mapSeparator)[0]);
                listView.getItems().remove(selectedItem);
            }
        }
        event.consume();
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
            String selectedItem = listView.getSelectionModel().getSelectedItem().split(mapSeparator)[0];
            cc.putString(selectedItem);
            db.setContent(cc);
        }
        event.consume();
    }


    /**
     * Allow the drop and display the result
     *
     * @param event    the {@link DragEvent}
     * @param listView the {@link ListView} to allow the drag over for
     */
    private void allowDragOver(DragEvent event, ListView<String> listView) {
        if (event.getGestureSource() != listView && event.getDragboard().hasString())
            if (allowAdd(event.getDragboard().getString()))
                event.acceptTransferModes(TransferMode.COPY);

        event.consume();
    }

    /**
     * Handle the drop event
     *
     * @param event the {@link DragEvent}
     */

    private void handleDrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            amountMap.put(db.getString(), 1);
            updateList();
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
        instanceRecipeListUI = this;
        searchList.setCellFactory(new EditorRecipeCellFactory());
        currentList.setCellFactory(new ListRecipeCellFactory());
        title.setText("Recipe Editor: " + StringUtils.capitalize(type));
        setUpDragAndDrop(searchList, currentList);
        searchList.setItems(FXCollections.observableArrayList());
        currentListName.setText("Current " + StringUtils.capitalize(type));
        allListName.setText("All " + StringUtils.capitalize(type));
        final Thread[] searchThread = new Thread[1];
        searchField.textProperty().addListener((observableValue, oldText, newText) -> {
            searchThread[0] = new Thread(() -> filterAndSortData(newText));
            searchThread[0].start();
        });
        searchThread[0] = new Thread(() -> filterAndSortData(""));
        searchThread[0].start();
        searchField.requestFocus();
        if ((type.equals("ingredients") && editorRecipe.getIngredients() != null)) {
            List<String> ingredientList = new ArrayList<>();
            for (String key : editorRecipe.getIngredients().keySet()) {
                ingredientList.add(key + mapSeparator + editorRecipe.getIngredients().get(key));
            }
            currentList.setItems(FXCollections.observableArrayList(ingredientList));

        } else if ((type.equals("aspects") && editorRecipe.getAspects() != null)) {
            List<String> aspectsList = new ArrayList<>();
            for (String key : editorRecipe.getAspects().keySet()) {
                aspectsList.add(key + mapSeparator + editorRecipe.getAspects().get(key));
            }
            currentList.setItems(FXCollections.observableArrayList(aspectsList));
        }
        currentList.getItems().sort(Comparator.comparing(String::toString));
        updateList();
        currentList.setTooltip(new Tooltip("The current list of ingredients \n Double click to remove"));
        searchList.setTooltip(new Tooltip("The list of " + type + " currently loaded"));
        searchField.setTooltip(new Tooltip("Filter the list of " + type));
    }

    /**
     * Updates the {@link ListView} with values from the amountMap {@link HashMap}
     */
    public void updateList() {
        List<String> tempList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : amountMap.entrySet())
            tempList.add(entry.getKey() + mapSeparator + entry.getValue());
        currentList.setItems(FXCollections.observableArrayList(tempList));
        currentList.getItems().sort(Comparator.comparing(String::toString));
    }

    /**
     * Adds an item to the {@link ListView} if it matches the criteria of allowAdd
     *
     * @param event the {@link MouseEvent}
     */
    @FXML
    private void handleAddDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2 && !searchList.getSelectionModel().isEmpty()) {
            String selectedItem = searchList.getSelectionModel().getSelectedItem().split(mapSeparator)[0];
            if (allowAdd(selectedItem)) {
                amountMap.put(selectedItem, 1);
                updateList();
            }
        }
        event.consume();
    }


    /**
     * Checks if an item should be allowed to be added to the {@link ListView}
     *
     * @param selectedItem the item to check
     * @return if it can be added
     */
    private boolean allowAdd(String selectedItem) {
        boolean add = true;
        if (type.equals("ingredients") && (editorRecipe.getType().equals("normal") || editorRecipe.getType().equals("arcane"))) {
            if (currentList.getItems().size() < 9)
                for (String item : currentList.getItems())
                    if (item.split(mapSeparator)[0].equals(selectedItem)) {
                        add = false;
                        break;
                    }
        } else
            for (String item : currentList.getItems())
                if (item.split(mapSeparator)[0].equals(selectedItem)) {
                    add = false;
                    break;
                }
        return add;
    }

    /**
     * FXML event to save the list and return to the {@link RecipeEditorUI}
     */

    public void saveList() {
        updateList();
        for (String item : currentList.getItems())
            amountMap.put(item.split(mapSeparator)[0], Integer.parseInt(item.split(mapSeparator)[1]));

        if (type.equals("ingredients")) editorRecipe.setIngredients(amountMap);
        else editorRecipe.setAspects(amountMap);

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

    /**
     * Sets the current focus to the search bar
     */
    @FXML
    private void setSearchFocus() {
        searchField.requestFocus();
    }
}
