package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

import static main.java.nukeminecart.thaumicrecipe.ui.UIManager.stage;

/**
 * Base class for all ThaumicRecipeGUI scenes containing a standard "toolbar"
 */

public class ThaumicRecipeUI {
    private static Stage dialogeStage;
    private double offsetX, offsetY;

    /**
     * FXML event to close the screen
     */
    @FXML
    private void closeScreen() {
        try {
            //TODO FIX RUNTIME CRASH WITH CLOSING
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setDialogPane(FXMLLoader.load(Objects.requireNonNull(ThaumicRecipeUI.class.getResource("warnings/CloseUi.fxml"))));
            alert.setTitle("Close Confirmation");
            alert.initOwner(stage.getOwner());
            dialogeStage = (Stage) alert.getDialogPane().getScene().getWindow();
            dialogeStage.initStyle(StageStyle.UNDECORATED);
            alert.show();
            dialogeStage.setX((stage.getX() + stage.getWidth() / 2) - (dialogeStage.getWidth() / 2));
            dialogeStage.setY((stage.getY() + stage.getHeight() / 2) - (dialogeStage.getHeight() / 2));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * FXML event that sets x and y to the current x and y position of the stage when pressed
     *
     * @param me the JavaFx mouse event
     */
    @FXML
    private void panePressed(MouseEvent me) {
        offsetX = stage.getX() - me.getScreenX();
        offsetY = stage.getY() - me.getScreenY();
    }

    /**
     * FXML event to change the x and y position of the stage when dragged
     *
     * @param me the JavaFx mouse event
     */
    @FXML
    private void paneDragged(MouseEvent me) {
        stage.setX(offsetX + me.getScreenX());
        stage.setY(offsetY + me.getScreenY());
    }


    /**
     * FXML event to exit the editor
     */
    @FXML
    private void confirmExit() {
        Platform.exit();
    }

    /**
     * FXML event to not exit the editor
     */
    @FXML
    private void cancelExit() {
        dialogeStage.close();
    }
}
