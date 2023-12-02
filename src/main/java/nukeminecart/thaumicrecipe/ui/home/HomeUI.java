package main.java.nukeminecart.thaumicrecipe.ui.home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.Transition;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.RecipeFileHandler;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.RecipeService;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.instanceHomeUI;
import static main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeConstants.recipeDirectory;

/**
 * The class that contains all the controller elements and logic for the HomeUI parent
 */

public class HomeUI extends ThaumicRecipeUI {

    private final List<String> filenames = new ArrayList<>();
    @FXML
    private Label loadWarning, newWarning;
    @FXML
    private MenuButton newChoice, loadChoice, fileField;
    @FXML
    private TextField newField, loadField;
    private File[] files;
    private String loadOption = "fromConfig", newOption = "recipeGroup";

    /**
     * Gets the {@link Parent} container containing all the HomeUI elements
     *
     * @return the {@link Parent} container
     * @throws IOException if HomeUI.fxml if not found
     */
    public Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(HomeUI.class.getResource("HomeUI.fxml")));
    }

    /**
     * FXML initialize method
     */
    @FXML
    private void initialize() {
        instanceHomeUI = this;
        newField.setTooltip(new Tooltip("The name of the recipe"));
        loadField.setTooltip(new Tooltip("The filepath of a .rcp file"));
        newField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                newRecipe();
        });
        loadField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) loadRecipe();
        });
    }

    /**
     * FXML event to load the recipe from the selected option
     */
    @FXML
    private void loadRecipe() {
        if (loadOption.equals("fromOther")) {
            if (FileParser.checkExists(new File(loadField.getText()))) {
                try {
                    RecipeFileHandler.loadOtherRecipe(new File(loadField.getText()).getPath());
                } catch (IOException e) {
                    throwAlert(WarningType.LOAD);
                }
            } else {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Open File");

                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("RECIPE files (*.rcp)", "*.rcp");
                chooser.getExtensionFilters().add(extFilter);
                chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                File fileChosen = chooser.showOpenDialog(UIManager.stage.getOwner());
                if(fileChosen==null) return;
                RecipeService.loadOtherRecipe(fileChosen.getPath());
            }

        } else if (loadOption.equals("fromFolder")) {
            RecipeService.loadFolderRecipe(loadField.getText(), filenames);
        } else {
            RecipeService.loadConfigRecipe();
        }
    }

    /**
     * Gets the files in the thaumicrecipe directory or creates a new directory in the minecraft folder
     */
    private void loadRecipeDirectory() {
        if (recipeDirectory != null) {
            File directory = new File(recipeDirectory);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throwAlert(WarningType.DIRECTORY);
                }
            }
            files = directory.listFiles();
        }


    }

    /**
     * FXML event to change the selected load option to config
     */
    @FXML
    private void changeToConfig() {
        fileField.setVisible(false);
        loadOption = "fromConfig";
        changeLoadOption("From Config", "Load from the configuration file");
    }

    /**
     * FXML event to change the selected load option to the thaumicrecipe folder
     */
    @FXML
    private void changeToFolder() {
        fileField.setVisible(true);
        loadRecipeDirectory();
        //Get all the names of the recipes in the thaumicrecipe folder and put them into a menu item and add them to a dropdown
        fileField.getItems().clear();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".rcp")) {
                    filenames.add(file.getName());
                    MenuItem menuItem = new MenuItem(file.getName());
                    menuItem.setOnAction(event -> loadField.setText(menuItem.getText()));
                    fileField.getItems().add(menuItem);
                }
            }
        } else {
            fileField.getItems().add(new MenuItem("No .rcp files found"));
        }
        loadOption = "fromFolder";
        changeLoadOption("From Folder", "Load from the Thaumicrecipe folder");

    }

    /**
     * FXML event to change the selected load option to another location
     */
    @FXML
    private void changeToOther() {
        fileField.setVisible(false);
        loadOption = "fromOther";
        changeLoadOption("From Other", "Load from other file location");
    }

    /**
     * FXML event to create a new recipe or recipe group with the name that the user specifies
     */
    @FXML
    private void newRecipe() {
        if (!RecipeService.checkNewErrors(newField.getText(), newOption)) return;
        if (newOption.equals("recipeCluster")) {
            try {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Open Multiple Files");

                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("RECIPE files (*.rcp)", "*.rcp");
                chooser.getExtensionFilters().add(extFilter);
                chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
                List<File> filesChosen = chooser.showOpenMultipleDialog(UIManager.stage.getOwner());

                RecipeFileHandler.newRecipeCluster(newField.getText(), filesChosen);
            } catch (IOException e) {
                throwAlert(WarningType.LOAD);
            }
        } else {
            try {
                RecipeFileHandler.newRecipeGroup(newField.getText());
            } catch (IOException e) {
                throwAlert(WarningType.LOAD);
            }
        }
    }

    /**
     * FXML event to change the type of recipe being created to a recipe group
     */
    @FXML
    private void changeToRecipeGroup() {
        newOption = "recipeGroup";
        changeNewOption("Recipe Group", "Create a new recipe grouping");
    }

    /**
     * FXML event to change the type of recipe being created to a recipe collection
     */
    @FXML
    private void changeToLargeRecipe() {
        newOption = "recipeCluster";
        changeNewOption("Recipe Cluster", "Import several recipes");
    }

    /**
     * Set the text, width, and tooltip of the new recipe menu
     *
     * @param text    the text displayed on the menu
     * @param tooltip the tooltip to display
     */
    private void changeNewOption(String text, String tooltip) {
        newChoice.setText(text);
        newChoice.setPrefWidth(130);
        newChoice.setTooltip(new Tooltip(tooltip));
    }

    /**
     * Set the text and tooltip of the load recipe menu
     *
     * @param text    the text displayed on the menu
     * @param tooltip the tooltip to display
     */

    private void changeLoadOption(String text, String tooltip) {
        loadChoice.setText(text);
        loadChoice.setPrefWidth(120);
        loadChoice.setTooltip(new Tooltip(tooltip));
    }

    /**
     * Remove the load warning if it is active
     */
    @FXML
    private void removeLoadWarning() {
        loadWarning.setText("");
    }

    /**
     * Remove the new recipe warning if it is active
     */
    @FXML
    private void removeNewWarning() {
        newWarning.setText("");
    }

    /**
     * Throw a new warning for creating a new recipe
     *
     * @param warning the warning text to display
     */
    public void throwNewWarning(String warning) {
        newWarning.setText(warning);
        Transition.Fade(Duration.millis(10000), 1, 0, newWarning);
    }

    /**
     * Throw a new warning for loading recipe file
     *
     * @param warning the warning text to display
     */
    public void throwLoadWarning(String warning) {
        loadWarning.setText(warning);
        Transition.Fade(Duration.millis(10000), 1, 0, loadWarning);
    }
}
