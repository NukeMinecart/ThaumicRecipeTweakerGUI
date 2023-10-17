package main.java.nukeminecart.thaumicrecipe.ui.home;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;

import java.io.IOException;

public class HomeUI{
    public Button closeButton;
    private double x,y;
    public Rectangle toolbarRect;

    public static Parent getScene() throws IOException {
        return FXMLLoader.load(HomeUI.class.getResource("HomeUI.fxml"));
    }
    @FXML
    protected void closeScreen() {
        Platform.exit();
    }
    @FXML public void panePressed(MouseEvent me){
        x = UIManager.stage.getX() - me.getScreenX();
        y = UIManager.stage.getY() - me.getScreenY();
    }

    @FXML public void paneDragged(MouseEvent me){
        UIManager.stage.setX(x + me.getScreenX());
        UIManager.stage.setY(y + me.getScreenY());
    }

}
