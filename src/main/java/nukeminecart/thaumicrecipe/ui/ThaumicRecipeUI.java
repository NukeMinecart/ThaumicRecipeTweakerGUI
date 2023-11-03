package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * Base class for all ThaumicRecipeGUI scenes containing a standard "toolbar"
 */

public class ThaumicRecipeUI {
    private double offsetX, offsetY;

    /**
     * FXML event to close the screen
     */
    @FXML
    private void closeScreen() {
        Platform.exit();
    }

    /**
     * FXML event that sets x and y to the current x and y position of the stage when pressed
     *
     * @param me the JavaFx mouse event
     */
    @FXML
    private void panePressed(MouseEvent me) {
        offsetX = UIManager.stage.getX() - me.getScreenX();
        offsetY = UIManager.stage.getY() - me.getScreenY();
    }

    /**
     * FXML event to change the x and y position of the stage when dragged
     *
     * @param me the JavaFx mouse event
     */
    @FXML
    private void paneDragged(MouseEvent me) {
        UIManager.stage.setX(offsetX + me.getScreenX());
        UIManager.stage.setY(offsetY + me.getScreenY());
    }
}
