package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.RecipeListUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.cachedScenes;
import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.editorRecipe;

/**
 * The class that contains all the controller elements and logic for the RecipeShapeUI parent
 */

public class RecipeShapeUI extends ThaumicRecipeUI {

    public static final ObservableList<String> ingredientList = FXCollections.observableArrayList();
    private static final List<ListView<String>> targetListViews = new ArrayList<>();
    private static boolean largeSize = true;
    @FXML
    private ListView<String> ingredients, craft1, craft2, craft3, craft4, craft5, craft6, craft7, craft8, craft9;

    /**
     * Gets the {@link Parent} container containing all the RecipeShapeUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeShapeUI.fxml if not found
     */
    public Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeShapeUI.class.getResource("RecipeShapeUI.fxml")));
    }

    /**
     * Loads the shapeEditor scene and adds all the ingredients to a {@link ListView}
     *
     * @throws IOException if RecipeShapeUI.fxml is not found
     */
    public void launchShapeEditor() throws IOException {
        ingredientList.clear();
        largeSize = true;
        if (editorRecipe.getIngredients() != null) {
            ingredientList.addAll(editorRecipe.getIngredients().keySet());
        }
        if (!cachedScenes.containsKey("shape-" + editorRecipe.getName())) {
            UIManager.loadScreen(getScene(), "shape-" + editorRecipe.getName());
        } else {
            UIManager.loadScreen(cachedScenes.get("shape-" + editorRecipe.getName()));
        }
        updateShape();
    }

    /**
     * Take the shape of the {@link Recipe} and put it into all the {@link ListView}
     */
    private void updateShape() {
        for (ListView<String> listView : targetListViews) {
            listView.getItems().clear();
        }
        if (editorRecipe.getShape() != null) {
            for (int index = 0; index < editorRecipe.getShape().length; index++) {
                ObservableList<String> item = targetListViews.get(index).getItems();
                if (item.isEmpty()) {
                    item.add(Objects.equals(editorRecipe.getShape()[index], "") || Objects.equals(editorRecipe.getShape()[index], null) ? "" : editorRecipe.getShape()[index]);
                } else {
                    item.set(0, Objects.equals(editorRecipe.getShape()[index], "") ? "" : editorRecipe.getShape()[index]);
                }
            }
        }
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        ingredients.setItems(ingredientList);
        ingredients.setTooltip(new Tooltip("Currently loaded items\n drag onto the crafting grid to create the shape"));

        targetListViews.clear();
        targetListViews.add(craft1);
        targetListViews.add(craft2);
        targetListViews.add(craft3);
        targetListViews.add(craft4);
        targetListViews.add(craft5);
        targetListViews.add(craft6);
        targetListViews.add(craft7);
        targetListViews.add(craft8);
        targetListViews.add(craft9);
        ingredients.setOnDragDetected(event -> dragItem(ingredients));

        for (ListView<String> listView : targetListViews) {
            listView.setOnDragDetected(event -> dragItem(listView));
            listView.setOnDragOver(event -> allowDrop(listView, event));
            listView.setOnDragDropped(event -> copyItem(listView, event));
            listView.setItems(FXCollections.observableArrayList());
            listView.setOnMouseClicked(event -> handleDoubleClick(event, listView));
        }
    }

    /**
     * FXML event to change the size of the shape of the {@link Recipe}
     */
    @FXML
    private void changeSize() {
        largeSize = !largeSize;
        craft3.setVisible(largeSize);
        craft6.setVisible(largeSize);
        craft7.setVisible(largeSize);
        craft8.setVisible(largeSize);
        craft9.setVisible(largeSize);
        if (!largeSize) {
            craft1.setLayoutX(328);
            craft2.setLayoutX(445);
            craft4.setLayoutX(328);
            craft5.setLayoutX(445);
        } else {
            craft1.setLayoutX(270);
            craft2.setLayoutX(386.6);
            craft4.setLayoutX(270);
            craft5.setLayoutX(386.6);
        }
    }

    @FXML
    private void openIngredients() {
        try {
            new RecipeListUI().launchListEditor("ingredients", false);
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * FXML event to save the shape in the given {@link Recipe}
     */
    @FXML
    private void saveShape() {
        editorRecipe.setShape(getShapeFromLists());
        returnToEditor();
    }

    /**
     * Gets the shape from all the {@link ListView} elements selected
     *
     * @return {@link String} array containing the shape of the recipe
     */
    private String[] getShapeFromLists() {
        List<String> shape = new ArrayList<>();
        for (int index = 0; index < 9; index++) {
            shape.add(targetListViews.get(index).getItems().isEmpty() ? "" : targetListViews.get(index).getItems().get(0));
        }
        if (!largeSize) {
            shape.set(2, "");
            shape.set(5, "");
            shape.set(6, "");
            shape.set(7, "");
            shape.set(8, "");
        }
        return shape.toArray(new String[0]);
    }

    /**
     * FXML event to return to {@link RecipeEditorUI}
     */
    @FXML
    private void returnToEditor() {
        try {
            new RecipeEditorUI().launchEditor();
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
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
     * Event method called when the {@link ListView} calls its dragDetected
     *
     * @param listView the {@link ListView} to enable dragging items
     */
    private void dragItem(ListView<String> listView) {
        Dragboard db = listView.startDragAndDrop(TransferMode.COPY);
        ClipboardContent content = new ClipboardContent();
        content.putString(listView.getSelectionModel().getSelectedItem());
        if (listView != ingredients) {
            listView.getItems().clear();
        }
        db.setContent(content);
    }

    /**
     * Event method called when the {@link ListView} has an item dragged over it
     *
     * @param listView the {@link ListView} to allow dropping items in this {@link ListView}
     * @param event    the {@link DragEvent}
     */
    private void allowDrop(ListView<String> listView, DragEvent event) {
        if (event.getDragboard().hasString()) {
            if (listView.getItems().isEmpty()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.acceptTransferModes(TransferMode.COPY, TransferMode.MOVE);
            }
        }
    }

    /**
     * Event method called when the {@link ListView} has an item dropped over it
     *
     * @param listView the {@link ListView} to copy to
     * @param event    the {@link DragEvent}
     */
    private void copyItem(ListView<String> listView, DragEvent event) {
        Dragboard db = event.getDragboard();

        if (db.hasString()) {
            String draggedItem = db.getString();
            ObservableList<String> targetList = listView.getItems();

            if (targetList.isEmpty()) {
                targetList.add(draggedItem);
            } else {
                targetList.clear();
                targetList.add(draggedItem);
            }
        }
    }
}
