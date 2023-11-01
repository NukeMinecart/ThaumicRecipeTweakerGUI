package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ThaumicRecipeUI {
    //TODO finish this class and remove all instances of these methods
    private double x,y;
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
}
