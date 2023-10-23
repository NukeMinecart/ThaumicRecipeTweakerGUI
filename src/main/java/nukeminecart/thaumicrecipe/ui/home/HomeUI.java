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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.recipeDir;

public class HomeUI{

    @FXML
    private MenuButton newChoice;
    @FXML
    private MenuButton loadChoice;

    @FXML
    private MenuButton fileField;

    @FXML
    private TextField newField;
    @FXML
    private TextField loadField;

    @FXML
    private Label loadWarning;
    @FXML
    private Label newWarning;
    private double x,y;
    private File[] files;
    private final List<String> filenames = new ArrayList<>();

    private String loadOption = "fromConfig";
    private String newOption = "recipeGroup";
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

    @FXML private void loadRecipe() {
        if(loadOption.equals("fromOther")) {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("RECIPE files (*.rcp)", "*.rcp");
            chooser.getExtensionFilters().add(extFilter);
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File fileChosen = chooser.showOpenDialog(UIManager.stage.getOwner());
            if(fileChosen==null){
                ErrorWarning.invalidFileError(loadWarning,"File not found");

            }

        } else if (loadOption.equals("fromFolder")) {
            if(loadField.getText().isEmpty() || loadField.getText() == null){
                ErrorWarning.invalidFileError(loadWarning,"Filename is invalid");
            }else if(!filenames.contains(loadField.getText())){
                ErrorWarning.invalidFileError(loadWarning, "File does not exist");
            }

        }else{
            
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
            ErrorWarning.invalidFileError(newWarning,"Recipe " + (newOption.equals("smallRecipe")? "" : "group ")+ "name is blank");
        }else {
            try {
                File testfile = new File(newField.getText());
                if (!testfile.delete()) {
                    throw new NullPointerException("File cannot be deleted");
                }
            } catch (Exception e) {
                ErrorWarning.invalidFileError(newWarning, "Filename is invalid");
            }
        }
        if(newOption.equals("smallRecipe")){

        }else{

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
}
