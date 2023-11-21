package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.cell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.RecipeManagerUI;

import java.io.IOException;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.stringArraySeparator;

/**
 * Class that contains the cell factory for {@link RecipeManagerUI}
 */
public class EditorRecipeCellFactory implements Callback<ListView<String>, ListCell<String>> {

    private static String itemName;
    private static String itemModid;
    @FXML
    private Label searchItem, searchItemModid;

    private static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(EditorRecipeCellFactory.class.getResource("EditorRecipeCell.fxml")));
    }

    @Override
    public ListCell<String> call(ListView<String> param) {
        return new SearchRecipeCell();
    }

    /**
     * FXML initialize method
     */
    @FXML
    private void initialize() {
        searchItem.setText(itemName);
        searchItemModid.setText(itemModid);
    }

    /**
     * Recipe Cell formatting and layout
     */
    public static class SearchRecipeCell extends ListCell<String> {
        /*
         Update an item in the list
         */
        @Override
        protected void updateItem(String string, boolean empty) {

            super.updateItem(string, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                try {
                    EditorRecipeCellFactory.itemName = string.split(stringArraySeparator)[0];
                    EditorRecipeCellFactory.itemModid = string.split(stringArraySeparator)[1];
                    setGraphic(EditorRecipeCellFactory.getScene());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}