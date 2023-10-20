package main.java.nukeminecart.thaumicrecipe.ui.home;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import main.java.nukeminecart.thaumicrecipe.ui.Transition;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.recipeDir;

public class HomeUI{
    public MenuButton newChoice;
    public MenuButton loadChoice;
    public MenuButton fileField;
    public TextField newField;
    public TextField loadField;
    public Label loadWarning;
    public Label newWarning;
    private double x,y;
    private File[] files;

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
                loadWarning.setText("File not found");
                Transition.Fade(Duration.millis(10000),1,0,loadWarning);
            }

        } else if (loadOption.equals("fromFolder")) {
            if(loadField.getText().isEmpty() || loadField.getText() == null){
                loadWarning.setText("Filename not valid");
                Transition.Fade(Duration.millis(10000),1,0,loadWarning);
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

    }

    @FXML private void fromFolder() {
        fileField.setVisible(true);
        loadRecipeDirectory();
        fileField.getItems().clear();
        if(files != null) {
            for (File file : files) {
                if(file.getName().endsWith(".rcp")) {
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
    }

    @FXML private void fromOther() {
        fileField.setVisible(false);
        loadOption = "fromOther";
        loadChoice.setText("From Other");
        loadChoice.setPrefWidth(120);
    }



    @FXML private void newRecipe() {
        if(newField.getText().isEmpty() || newField.getText() == null){
            newWarning.setText("Enter a name for the recipe"+ (newOption.equals("recipeGroup") ? " group" : ""));
            Transition.Fade(Duration.millis(10000),1,0,newWarning);
        }
        try{
            File testfile = new File(newField.getText());
            if(!testfile.delete()){
                throw new NullPointerException("File cannot be deleted");
            }
        }catch (Exception e){
            newWarning.setText("Filename is invalid");
            Transition.Fade(Duration.millis(10000),1,0,newWarning);
        }
        if(newOption.equals("smallRecipe")){

        }else{

        }
    }
    @FXML private void newRecipeGroup() {
        newOption = "recipeGroup";
        newChoice.setText("Recipe Group");
        newChoice.setPrefWidth(130);
    }
    @FXML private void newSmallRecipe() {
        newOption = "smallRecipe";
        newChoice.setText("Recipe");
        newChoice.setPrefWidth(90);
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
