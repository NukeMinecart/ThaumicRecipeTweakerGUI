package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeShapeUI extends ThaumicRecipeUI {

    public static final ObservableList<String> ingredientList = FXCollections.observableArrayList();
    final List<ListView<String>> targetListViews = new ArrayList<>();
    @FXML
    private ListView<String> ingredients, craft1, craft2, craft3, craft4, craft5, craft6, craft7, craft8, craft9;
    private boolean largeSize = true;

    /**
     * Gets the {@link Parent} container containing all the RecipeShapeUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeShapeUI.fxml if not found
     */
    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeShapeUI.class.getResource("RecipeShapeUI.fxml")));
    }

    /**
     * Loads the shapeEditor scene and adds all the ingredients to a {@link ListView}
     *
     * @param recipe the recipe to have its shape edited
     * @throws IOException if RecipeShapeUI.fxml is not found
     */
    public static void launchShapeEditor(Recipe recipe) throws IOException {
        RecipeShapeUI.ingredientList.addAll(recipe.getIngredients());
        UIManager.loadScreen(getScene());
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        ingredients.setItems(ingredientList);
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

    /**
     * FXML event to save the shape in the given {@link Recipe}
     */
    @FXML
    private void saveShape() {
        //TODO
    }

    /**
     * FXML event to return to {@link RecipeEditorUI}
     */
    @FXML
    private void returnToEditor() {
        //TODO
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
                // If the target list is empty, allow copy
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                // If the target list already has an item, allow copy and clear
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
