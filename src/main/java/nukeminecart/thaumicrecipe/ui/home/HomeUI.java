package main.java.nukeminecart.thaumicrecipe.ui.home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import main.java.nukeminecart.thaumicrecipe.ui.ThaumicRecipeUI;
import main.java.nukeminecart.thaumicrecipe.ui.Transition;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.RecipeHandler;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.IFileParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.loadedRecipe;
import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.recipeDir;

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
     * Gets the parent container containing all the HomeUI elements
     *
     * @return the parent container
     * @throws IOException if HomeUI.fxml if not found
     */
    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(HomeUI.class.getResource("HomeUI.fxml")));
    }
    /**
     * FXML initialize method
     */
    @FXML
    private void initialize(){
        RecipeHandler.homeUI = this;
    }

    /**
     * FXML event to load the recipe from the selected option
     */
    @FXML
    private void loadRecipe() {
        if (loadOption.equals("fromOther")) {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("RECIPE files (*.rcp)", "*.rcp");
            chooser.getExtensionFilters().add(extFilter);
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File fileChosen = chooser.showOpenDialog(UIManager.stage.getOwner());
            if (fileChosen == null) {
                throwLoadWarning("File not found");
            } else {
                try {
                    RecipeHandler.loadOtherRecipe(fileChosen.getPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (loadOption.equals("fromFolder")) {
            if (loadField.getText().isEmpty() || loadField.getText() == null) {
                throwLoadWarning("Filename is invalid");
            } else if (!filenames.contains(loadField.getText())) {
                throwLoadWarning("File does not exist");
            } else {
                try {
                    RecipeHandler.loadFolderRecipe(loadField.getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (loadedRecipe != null) {
                try {
                    RecipeHandler.loadConfigRecipe();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throwLoadWarning("No recipe in config");
            }
        }
    }

    /**
     * Gets the files in the thaumicrecipe directory or creates a new directory in the minecraft folder
     */
    private void loadRecipeDirectory() {
        if (recipeDir != null) {
            File directory = new File(recipeDir);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new NullPointerException("Could not create directory");
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
        changeLoadOption("From Config", "Loading from configuration file");
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
        changeLoadOption("From Folder", "Loading from the Thaumicrecipe folder");

    }

    /**
     * FXML event to change the selected load option to another location
     */
    @FXML
    private void changeToOther() {
        fileField.setVisible(false);
        loadOption = "fromOther";
        changeLoadOption("From Other", "Loading from other location");
    }

    /**
     * FXML event to create a new recipe or recipe group with the name that the user specifies
     */
    @FXML
    private void newRecipe() {
        if (newField.getText().isEmpty() || newField.getText() == null) {
            throwNewWarning("Recipe " + (newOption.equals("smallRecipe") ? "" : "group ") + "name is blank");
            return;
        } else {
            try {
                File testfile = recipeDir == null ? IFileParser.getFolderFile(newField.getText()) : new File(System.getProperty("user.dir"), newField.getText());
                boolean deleted;
                if (!testfile.createNewFile()) {
                    throw new NullPointerException("File cannot be created");
                }
                deleted = testfile.delete();
                if (!deleted) {
                    throw new NullPointerException("File cannot be deleted");
                }

            } catch (Exception e) {
                throwNewWarning("Filename is invalid");
                return;
            }
        }
        if (newOption.equals("largeRecipe")) {
            try {
                RecipeHandler.setLabel(newWarning);
                RecipeHandler.newLargeRecipe(newField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                RecipeHandler.setLabel(newWarning);
                RecipeHandler.newRecipeGroup(newField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * FXML event to change the type of recipe being created to a recipe group
     */
    @FXML
    private void changeToRecipeGroup() {
        newOption = "recipeGroup";
        changeNewOption("Recipe", 130, "New recipe grouping");
    }

    /**
     * FXML event to change the type of recipe being created to a recipe collection
     */
    @FXML
    private void changeToLargeRecipe() {
        newOption = "largeRecipe";
        changeNewOption("Recipe Collection", 170, "New recipe collection");
    }

    /**
     * Set the text, width, and tooltip of the new recipe menu
     *
     * @param text    the text displayed on the menu
     * @param width   the width to resize the menu
     * @param tooltip the tooltip to display
     */
    private void changeNewOption(String text, int width, String tooltip) {
        newChoice.setText(text);
        newChoice.setPrefWidth(width);
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
