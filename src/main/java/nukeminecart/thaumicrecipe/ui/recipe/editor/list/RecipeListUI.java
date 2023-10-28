package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.list;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;

import java.io.IOException;
import java.util.Objects;

public class RecipeListUI {
    @FXML
    private Label title;

    public static String type;
    private double x,y;
    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeListUI.class.getResource("RecipeListUI.fxml")));
    }

    public static void launchEditor(String type, String[] list) throws IOException {
        RecipeListUI.type = type;
        UIManager.loadScreen(getScene());
    }

    @FXML
    public void initialize(){
        title.setText("Recipe Editor: "+ type);
    }

    @FXML private void closeScreen() {
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
}
