package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * Base class for all ThaumicRecipeGUI scenes containing a standard "toolbar"
 */

public class ThaumicRecipeUI {
    private double x, y;

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
        x = UIManager.stage.getX() - me.getScreenX();
        y = UIManager.stage.getY() - me.getScreenY();
    }

    /**
     * FXML event to change the x and y position of the stage when dragged
     *
     * @param me the JavaFx mouse event
     */
    @FXML
    private void paneDragged(MouseEvent me) {
        UIManager.stage.setX(x + me.getScreenX());
        UIManager.stage.setY(y + me.getScreenY());
    }
}
