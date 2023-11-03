package main.java.nukeminecart.thaumicrecipe.ui.recipe.manager;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

/**
 * Class that contains the cell factory for {@link RecipeManagerUI}
 */
public class RecipeCellFactory implements Callback<ListView<Recipe>, ListCell<Recipe>> {

    @Override
    public ListCell<Recipe> call(ListView<Recipe> param) {
        return new RecipeCell();
    }

    /**
     * Recipe Cell formatting and layout
     */
    public static class RecipeCell extends ListCell<Recipe> {
        final Button button = new Button("Edit");

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

            /*
             Event for when the "edit" button is pressed
             */
            button.setOnAction(event -> {
                String recipeName = getItem().getName();
                new RecipeManagerUI().openEditor(recipeName);
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
                Label label = new Label(recipe.getName());
                label.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(label, Priority.ALWAYS);

                HBox content = new HBox(label, button);
                HBox.setHgrow(content, Priority.ALWAYS);
                setGraphic(content);
            }
        }
    }
}