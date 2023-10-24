package main.java.nukeminecart.thaumicrecipe.ui.home;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import main.java.nukeminecart.thaumicrecipe.ui.ErrorWarning;
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

public class HomeUI{

    @FXML
    private MenuButton newChoice, loadChoice, fileField;

    @FXML
    private TextField newField, loadField;

    public Label loadWarning, newWarning;
    private double x,y;
    private File[] files;
    private final List<String> filenames = new ArrayList<>();
    private String loadOption = "fromConfig", newOption = "recipeGroup";
    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(HomeUI.class.getResource("HomeUI.fxml")));
    }
    @FXML
    private void closeScreen() {
        Platform.exit();
    }
    @FXML private void panePressed(MouseEvent me){
        x = UIManager.stage.getX() - me.getScreenX();
        y = UIManager.stage.getY() - me.getScreenY();
    }

    @FXML private void paneDragged(MouseEvent me){
        UIManager.stage.setX(x + me.getScreenX());
        UIManager.stage.setY(y + me.getScreenY());
    }

    @FXML private void loadRecipe(){
        if(loadOption.equals("fromOther")) {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("RECIPE files (*.rcp)", "*.rcp");
            chooser.getExtensionFilters().add(extFilter);
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File fileChosen = chooser.showOpenDialog(UIManager.stage.getOwner());
            if(fileChosen==null){
                throwLoadWarning("File not found");
            }else{
                try {
                    RecipeHandler.loadOtherRecipe(fileChosen.getPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (loadOption.equals("fromFolder")) {
            if(loadField.getText().isEmpty() || loadField.getText() == null){
                throwLoadWarning("Filename is invalid");
            }else if(!filenames.contains(loadField.getText())){
                throwLoadWarning("File does not exist");
            }else{
                try {
                    RecipeHandler.loadFolderRecipe(loadField.getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            if(loadedRecipe!=null){
                try {
                    RecipeHandler.loadConfigRecipe();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                throwLoadWarning("No recipe in config");
            }
        }
    }
    private void loadRecipeDirectory(){
        if(recipeDir != null){
            File directory = new File(recipeDir);
            if(!directory.exists()){
                if(!directory.mkdirs()){
                    throw new NullPointerException("Could not create directory");
                }
            }
            files = directory.listFiles();
        }


    }

    @FXML private void fromConfig() {
        fileField.setVisible(false);
        loadOption = "fromConfig";
        loadChoice.setText("From Config");
        loadChoice.setPrefWidth(120);
        loadChoice.setTooltip(new Tooltip("Loading from configuration file"));
    }

    @FXML private void fromFolder() {
        fileField.setVisible(true);
        loadRecipeDirectory();
        fileField.getItems().clear();
        if(files != null) {
            for (File file : files) {
                if(file.getName().endsWith(".rcp")) {
                    filenames.add(file.getName());
                    MenuItem menuItem = new MenuItem(file.getName());
                    menuItem.setOnAction(event -> loadField.setText(menuItem.getText()));
                    fileField.getItems().add(menuItem);
                }
            }
        }else{
            fileField.getItems().add(new MenuItem("No .rcp files found"));
        }
        loadOption = "fromFolder";
        loadChoice.setText("From Folder");
        loadChoice.setPrefWidth(120);
        loadChoice.setTooltip(new Tooltip("Loading from Thaumicrecipe folder"));
    }

    @FXML private void fromOther() {
        fileField.setVisible(false);
        loadOption = "fromOther";
        loadChoice.setText("From Other");
        loadChoice.setPrefWidth(120);
        loadChoice.setTooltip(new Tooltip("Loading from other location"));
    }



    @FXML private void newRecipe() {
        if(newField.getText().isEmpty() || newField.getText() == null){
            throwNewWarning("Recipe " + (newOption.equals("smallRecipe")? "" : "group ")+ "name is blank");
            return;
        }else {
            try {
                File testfile = IFileParser.getFolderFile(newField.getText());
                boolean deleted;
                if (!testfile.createNewFile()) {
                    throw new NullPointerException("File cannot be created");
                }
                deleted = testfile.delete();
                if(!deleted){
                    throw new NullPointerException("File cannot be deleted");

                }

            } catch (Exception e) {
                throwNewWarning("Filename is invalid");
                return;
            }
        }
        if(newOption.equals("smallRecipe")){
            try {
                RecipeHandler.newRecipe(newField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                RecipeHandler.newRecipeGroup(newField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML private void newRecipeGroup() {
        newOption = "recipeGroup";
        newChoice.setText("Recipe Group");
        newChoice.setPrefWidth(130);
        newChoice.setTooltip(new Tooltip("New recipe grouping"));
    }
    @FXML private void newSmallRecipe() {
        newOption = "smallRecipe";
        newChoice.setText("Recipe");
        newChoice.setPrefWidth(90);
        newChoice.setTooltip(new Tooltip("New singular recipe"));

    }

    @FXML
    private void removeLoadWarning() {
        loadWarning.setText("");
    }
    @FXML
    private void removeNewWarning() {
        newWarning.setText("");
    }

    public void throwNewWarning(String warning){
        ErrorWarning.invalidFileError(newWarning,warning);
    }
    public void throwLoadWarning(String warning){
        ErrorWarning.invalidFileError(loadWarning,warning);
    }
}
