package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.cell;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.RecipeListUI;

import java.io.IOException;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.instanceRecipeEditorUI;
import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.mapSeparator;
import static main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list.RecipeListUI.amountMap;

/**
 * Class that contains the cell factory for {@link RecipeListUI}
 */
public class ListRecipeCellFactory implements Callback<ListView<String>, ListCell<String>> {


    private static String item;
    private static int amount;
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
        itemAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            itemAmount.setText(newValue.replaceAll("[^0-9]", ""));
            if (!itemAmount.getText().isEmpty()) {
                amount = Integer.parseInt(itemAmount.getText());
                amountMap.put(item.split(mapSeparator)[0], amount);
            }

        });
        itemAmount.setOnKeyPressed(this::changeItemAmount);
    }

    /**
     * Changes the amount of items value in the visField according to the {@link KeyCode} pressed
     *
     * @param event the {@link KeyEvent}
     */
    private void changeItemAmount(KeyEvent event) {
        itemAmount.setText(itemAmount.getText().replaceAll("[^0-9]", ""));

        if (amount >= 0) {
            switch (event.getCode()) {
                case UP:
                    itemAmount.setText(String.valueOf(amount + 1));
                    break;
                case DOWN:
                    itemAmount.setText(String.valueOf((amount - 1) == 0 ? 1 : amount - 1));
                    break;
            }
        }
        amount = Integer.parseInt(itemAmount.getText());
        amountMap.put(item.split(mapSeparator)[0], amount);
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

            super.updateItem(string, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                try {
                    item = string;
                    setGraphic(ListRecipeCellFactory.getScene());
                } catch (IOException e) {
                    instanceRecipeEditorUI.throwAlert(ThaumicRecipeUI.WarningType.SCENE);
                }
            }
        }
    }
}
