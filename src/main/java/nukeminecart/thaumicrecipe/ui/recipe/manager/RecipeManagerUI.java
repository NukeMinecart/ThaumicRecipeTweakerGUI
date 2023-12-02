package main.java.nukeminecart.thaumicrecipe.ui.recipe.manager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.DirectoryChooser;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.RecipeEditorUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.importer.RecipeImporterUI;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.manager.cell.ManagerRecipeCellFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.*;

/**
 * The class that contains all the controller elements and logic for the RecipeManagerUI parent
 */

public class RecipeManagerUI extends ThaumicRecipeUI {


    public static final ObservableList<Recipe> recipes = FXCollections.observableArrayList();
    public static final HashMap<String, Recipe> recipeEditorMap = new HashMap<>();
    private static String stringTitle;
    @FXML
    private ListView<Recipe> recipeList;
    @FXML
    private TextField nameField;
    @FXML
    private Label title, recipeListLabel;
    @FXML
    private CheckBox closeSelection, activeSelection, displaySelection;

    /**
     * Loads the {@link RecipeEditorUI} and passes it the recipe to be displayed
     *
     * @param recipeName the recipe name to load the recipe editor with
     */
    public void openEditor(String recipeName) {
        try {
            ThaumicRecipeConstants.changeEditorRecipe(recipeEditorMap.get(recipeName));
            recipeEditorMap.remove(recipeName);
            new RecipeEditorUI().launchEditor();
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }

    /**
     * Updates the recipeEditorMap {@link HashMap} with the key as the name of the recipe and the values as the {@link Recipe} from the recipes {@link ObservableList}
     */
    private void refreshHashMap() {
        for (Recipe recipe : recipes) {
            recipeEditorMap.put(recipe.getName(), recipe);
        }
    }

    /**
     * Updates the recipes {@link ObservableList} with the values from the recipeEditorMap {@link HashMap}
     */
    private void refreshRecipes() {
        recipes.setAll(recipeEditorMap.values());
    }

    /**
     * Gets the {@link Parent} container containing all the RecipeManagerUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if RecipeManagerUI.fxml if not found
     */
    private Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeManagerUI.class.getResource("RecipeManagerUI.fxml")));
    }

    /**
     * Gets the cached {@link Scene} from {@link ThaumicRecipeConstants}
     *
     * @return the cached {@link Scene}
     */
    public Scene getCachedScene() {
        return cachedScenes.get("manager");
    }

    /**
     * Loads the RecipeManager scene and adds all the recipes to the listView
     *
     * @param name         the name of the file
     * @param fileContents the contents of the file
     * @throws IOException if getScene returns an invalid {@link Parent}
     */
    public void loadManager(String name, List<String> fileContents) throws IOException {
        instanceRecipeManagerUI = this;
        if (fileContents != null && fileContents.size() > 1) {
            recipes.addAll(FileParser.getRecipesFromString(fileContents));
            refreshHashMap();
        }
        stringTitle = name.endsWith(".rcp") ? name : name + ".rcp";
        UIManager.loadScreen(getScene(), "manager");
    }

    /**
     * Load the {@link RecipeManagerUI} scene and refresh the recipes from the {@link HashMap}
     */
    public void loadManager() throws IOException {
        instanceRecipeManagerUI = this;
        UIManager.loadScreen(getCachedScene());
        Platform.runLater(this::refreshRecipes);
    }

    /**
     * FXML initialize event
     */
    @FXML
    public void initialize() {
        recipeList.setCellFactory(new ManagerRecipeCellFactory());
        recipeList.setItems(recipes);
        recipeList.setTooltip(new Tooltip("All the recipes"));
        title.setText("Recipe Manager: " + stringTitle);
        recipes.addListener((ListChangeListener<Recipe>) c -> refreshHashMap());
        recipeListLabel.setText("Recipes in " + stringTitle);
        nameField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                addRecipe();
            }
        });
        refreshRecipes();
    }

    /**
     * FXML event to make the nameField visible to add a {@link Recipe}
     */

    @FXML
    private void addRecipe() {
        if (nameField.isVisible()) {
            if (nameField.getText().isEmpty()) return;

            changeEditorRecipe(new Recipe(nameField.getText()));
            editorRecipeExisted = false;
            nameField.setVisible(false);
            nameField.setText("");

            try {
                new RecipeEditorUI().launchEditor();
            } catch (IOException e) {
                throwAlert(WarningType.SCENE);
            }
        } else {
            nameField.setVisible(true);
        }
    }

    /**
     * FXML event to save the current {@link Recipe} as a {@link File} in the thaumicrecipe folder
     */
    @FXML
    private void saveRecipeToFolder() {
        try {
            FileParser.saveRecipesToFile(new File(recipeDirectory, stringTitle), recipes);
        } catch (IOException e) {
            throwAlert(WarningType.SAVE);
        }
        checkOptions();
    }

    /**
     * FXML event to save the current {@link Recipe} as a {@link File} in some other location
     */
    @FXML
    private void saveRecipeToOther() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select A Directory");

        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File directoryChosen = chooser.showDialog(UIManager.stage.getOwner());
        try {
            FileParser.saveRecipesToFile(new File(directoryChosen, stringTitle), recipes);
        } catch (IOException e) {
            throwAlert(WarningType.SAVE);
        }
        checkOptions();
    }

    /**
     * Performs actions for all the selected {@link CheckBox}
     */
    private void checkOptions() {
        try {
            FileParser.setConfigOptions(activeSelection.isSelected() ? stringTitle : null, displaySelection.isSelected());
        } catch (IOException e) {
            throwAlert(WarningType.SAVE);
        }
        if (closeSelection.isSelected()) Platform.exit();
    }

    /**
     * FXML event to import an existing recipe
     */
    @FXML
    private void importExistingRecipe() {
        try {
            new RecipeImporterUI().loadImporter();
        } catch (IOException e) {
            throwAlert(WarningType.SCENE);
        }
    }
}
