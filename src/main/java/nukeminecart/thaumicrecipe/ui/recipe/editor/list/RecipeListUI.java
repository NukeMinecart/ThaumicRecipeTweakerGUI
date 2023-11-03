package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;

import java.io.IOException;
import java.util.Objects;

/**
 * The class that contains all the controller elements and logic for the RecipeListUI parent
 */

public class RecipeListUI extends ThaumicRecipeUI {
    public static String type;
    @FXML
    private Label title;

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
    public void launchListEditor(String type, String[] list) throws IOException {
        RecipeListUI.type = type;
        UIManager.loadScreen(getScene());
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        title.setText("Recipe Editor: " + type);
    }
}
