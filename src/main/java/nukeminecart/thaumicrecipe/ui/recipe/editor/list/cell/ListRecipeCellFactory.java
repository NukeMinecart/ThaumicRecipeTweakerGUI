package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.cell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.RecipeListUI;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.mapSeparator;
import static main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.RecipeListUI.staticCurrentList;

/**
 * Class that contains the cell factory for {@link RecipeListUI}
 */
public class ListRecipeCellFactory implements Callback<ListView<String>, ListCell<String>> {


    private static String item;
    private int amount;
    @FXML
    private Label searchItem;
    @FXML
    private TextField itemAmount;

    private static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(ListRecipeCellFactory.class.getResource("ListRecipeCell.fxml")));
    }

    @Override
    public ListCell<String> call(ListView<String> param) {
        return new CurrentRecipeCell();
    }

    /**
     * FXML initialize method
     */
    @FXML
    private void initialize() {
        searchItem.setText(item.split(mapSeparator)[0]);
        itemAmount.setText(item.split(mapSeparator)[1]);
        amount = Integer.parseInt(itemAmount.getText());
        UnaryOperator<TextFormatter.Change> integerFilter = change -> change.getControlNewText().matches("^([1-9]\\d*)?$") ? change : null;
        itemAmount.setTextFormatter(new TextFormatter<>(integerFilter));
        itemAmount.setOnKeyPressed(this::changeItemAmount);
    }

    /**
     * Changes the amount of items value in the visField according to the {@link KeyCode} pressed
     *
     * @param event the {@link KeyEvent}
     */
    private void changeItemAmount(KeyEvent event) {
        try {
            amount = Integer.parseInt(itemAmount.getText());
        } catch (NumberFormatException e) {
            amount = 0;
        }

        switch (event.getCode()) {
            case UP:
                amount = amount + 1;
                itemAmount.setText(String.valueOf(amount));
                break;

            case DOWN:
                amount = amount - 1 == -1 ? 0 : amount - 1;
                itemAmount.setText(String.valueOf(amount));
                break;

            case BACK_SPACE:
                itemAmount.deletePreviousChar();
                break;

            case DELETE:
                itemAmount.deleteNextChar();
                break;
        }
        if (amount < 1) amount = 1;
        updateListValues(item, item.split(mapSeparator)[0] + mapSeparator + amount);
        event.consume();
    }


    /**
     * Updates the {@link ListView} with new items
     *
     * @param oldItem the old item to delete
     * @param newItem the new item to add
     */

    public void updateListValues(String oldItem, String newItem) {
        //TODO FIX ADDING BUG
        System.out.println(staticCurrentList.getItems());
        staticCurrentList.getItems().remove(oldItem);
        System.out.println(staticCurrentList.getItems());
        staticCurrentList.getItems().add(newItem);
        System.out.println(staticCurrentList.getItems());
    }

    /**
     * Recipe Cell formatting and layout
     */
    public static class CurrentRecipeCell extends ListCell<String> {
        /*
         Update an item in the list
         */
        @Override
        protected void updateItem(String string, boolean empty) {
            if (!empty && item != null && string.split(mapSeparator)[0].equals(item.split(mapSeparator)[0]))
                string = item;
            else item = string;
            super.updateItem(string, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                try {
                    setGraphic(ListRecipeCellFactory.getScene());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                    //instanceRecipeEditorUI.throwAlert(ThaumicRecipeUI.WarningType.SCENE);
                }
            }
        }
    }
}
