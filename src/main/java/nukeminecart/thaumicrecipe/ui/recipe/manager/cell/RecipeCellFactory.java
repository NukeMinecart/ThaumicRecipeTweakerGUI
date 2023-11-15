package main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.cell;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.IOException;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.editorRecipeExisted;

/**
 * Class that contains the cell factory for {@link RecipeManagerUI}
 */
public class RecipeCellFactory implements Callback<ListView<Recipe>, ListCell<Recipe>> {

    private static Recipe recipe;
    @FXML
    private Label recipeName;

    private static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeCellFactory.class.getResource("RecipeCell.fxml")));
    }

    @Override
    public ListCell<Recipe> call(ListView<Recipe> param) {
        return new RecipeCell();
    }

    /**
     * Launches the {@link main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI} with the recipe in the cell
     */
    @FXML
    private void launchEditor() {
        editorRecipeExisted = true;
        RecipeManagerUI.openEditor(recipeName.getText());
    }

    /**
     * FXML initialize method
     */
    @FXML
    private void initialize() {
        recipeName.setText(recipe.getName());
    }

    /**
     * Recipe Cell formatting and layout
     */
    public static class RecipeCell extends ListCell<Recipe> {

        /**
         * Constructor for {@link RecipeCell}
         */
        public RecipeCell() {
            setOnDragDetected(event -> {
                if (!isEmpty()) {
                    Dragboard db = startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(getItem().getName());
                    db.setContent(content);
                    event.consume();
                }
            });
            /*
            Check when an item is dragged over the list
             */
            setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                }
            });
            /*
             Check when an item is dropped over the list
             */
            setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    String draggedRecipeName = db.getString();
                    Recipe draggedRecipe = null;

                    for (Recipe item : getListView().getItems()) {
                        if (item.getName().equals(draggedRecipeName)) {
                            draggedRecipe = item;
                            break;
                        }
                    }

                    if (draggedRecipe != null) {
                        int draggedIndex = getListView().getItems().indexOf(draggedRecipe);
                        int thisIndex = getListView().getItems().indexOf(getItem());

                        if (thisIndex != -1) {
                            getListView().getItems().set(draggedIndex, getItem());
                            getListView().getItems().set(thisIndex, draggedRecipe);
                            success = true;
                        }
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            });
        }

        /*
         Update an item in the list
         */
        @Override
        protected void updateItem(Recipe recipe, boolean empty) {
            super.updateItem(recipe, empty);
            if (empty || recipe == null) {
                setText(null);
                setGraphic(null);
            } else {
                try {
                    RecipeCellFactory.recipe = recipe;
                    setGraphic(RecipeCellFactory.getScene());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}