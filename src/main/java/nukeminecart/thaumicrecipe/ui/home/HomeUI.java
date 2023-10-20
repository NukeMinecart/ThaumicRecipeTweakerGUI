package main.java.nukeminecart.thaumicrecipe.ui.home;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.minecraftDir;
import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.separator;

public class HomeUI{
    public SplitMenuButton newRecipeButton;
    public SplitMenuButton loadRecipeButton;
    public MenuButton fileField;
    private double x,y;
    private List<String> fileList;


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


        } else if (loadOption.equals("fromFolder")) {


        }else{
            
        }
    }
    private void loadRecipeDirectory(){
        if(minecraftDir != null){
            File directory = new File(minecraftDir+separator+"thaumicrecipe");
            if(!directory.exists()){
                directory.mkdirs();
            }

            fileList.addAll(Arrays.asList(Objects.requireNonNull(directory.list())));
        }


    }

    @FXML private void fromConfig() {
        fileField.setVisible(false);
        loadOption = "fromConfig";
        loadRecipeButton.setText("Load From Config");
        loadRecipeButton.setPrefWidth(180);

    }

    @FXML private void fromFolder() {
        fileField.setVisible(true);
        loadRecipeDirectory();
        fileField.getItems().clear();
        if(fileList != null) {
            for (String name : fileList) {
                fileField.getItems().add(new MenuItem(name));
            }
        }else{
            fileField.getItems().add(new MenuItem("No .rcp files found"));
        }
        loadOption = "fromFolder";
        loadRecipeButton.setText("Load From Folder");
        loadRecipeButton.setPrefWidth(180);
    }

    @FXML private void fromOther() {
        fileField.setVisible(false);
        loadOption = "fromOther";
        loadRecipeButton.setText("Load From Other");
        loadRecipeButton.setPrefWidth(170);
    }



    @FXML private void newRecipe() {
    }
    @FXML private void newRecipeGroup() {
        newOption = "recipeGroup";
        newRecipeButton.setText("New Recipe Group");
        newRecipeButton.setPrefWidth(180);
    }
    @FXML private void newSmallRecipe() {
        newOption = "smallRecipe";
        newRecipeButton.setText("New Recipe");
        newRecipeButton.setPrefWidth(130);
    }
}
