package main.java.nukeminecart.thaumicrecipe.ui.home;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HomeUI{
    public SplitMenuButton newRecipeButton;
    public SplitMenuButton loadRecipeButton;
    private double x,y;

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
            chooser.showOpenDialog(UIManager.stage.getOwner());
        } else if (loadOption.equals("fromFolder")) {

        }else{

        }
    }

    @FXML private void fromConfig() {
        loadOption = "fromConfig";
        loadRecipeButton.setText("Load From Config");
        loadRecipeButton.setPrefWidth(180);

    }

    @FXML private void fromFolder() {
        loadOption = "fromFolder";
        loadRecipeButton.setText("Load From Folder");
        loadRecipeButton.setPrefWidth(180);
    }

    @FXML private void fromOther() {
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
