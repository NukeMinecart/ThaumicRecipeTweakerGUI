package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RecipeShapeUI {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    public static Recipe recipe;
    private double x,y;

    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeShapeUI.class.getResource("RecipeShapeUI.fxml")));
    }
    public static void launchEditor(Recipe recipe) throws IOException {
        RecipeShapeUI.recipe = recipe;
        UIManager.loadScreen(getScene());
    }

    @FXML
    public void initialize(){
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


    private void enableDragAndDrop(List<ListView<String>> listViews, ListView<String> sourceListView) {
        sourceListView.setOnDragDetected(event -> {
            Dragboard dragboard = sourceListView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            int selectedIdx = sourceListView.getSelectionModel().getSelectedIndex();
            content.put(SERIALIZED_MIME_TYPE, selectedIdx);
            dragboard.setContent(content);
            event.consume();
        });

        for (ListView<String> targetListView : listViews) {
            targetListView.setOnDragOver(event -> {
                if (event.getGestureSource() != targetListView && event.getDragboard().hasContent(SERIALIZED_MIME_TYPE)) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            targetListView.setOnDragDropped(event -> {
                Dragboard dragboard = event.getDragboard();
                boolean success = false;

                if (dragboard.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIdx = (Integer) dragboard.getContent(SERIALIZED_MIME_TYPE);
                    String item = sourceListView.getItems().get(draggedIdx);
                    targetListView.getItems().add(item);
                    sourceListView.getItems().remove(draggedIdx);

                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            });
        }
    }
}
