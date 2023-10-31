package main.java.nukeminecart.thaumicrecipe.ui.recipe.editor.shape;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeShapeUI {

    public static ObservableList<String> ingredientList = FXCollections.observableArrayList();

    @FXML
    private ListView<String> ingredients, craft1, craft2, craft3, craft4, craft5, craft6, craft7, craft8, craft9;
    List<ListView<String>> targetListViews = new ArrayList<>();
    private boolean largeSize = true;

    private double x,y;

    public static Parent getScene() throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(RecipeShapeUI.class.getResource("RecipeShapeUI.fxml")));
    }
    public static void launchEditor(Recipe recipe) throws IOException {
        RecipeShapeUI.ingredientList.addAll(recipe.getIngredients());
        UIManager.loadScreen(getScene());
    }

    @FXML
    public void initialize(){
        ingredients.setItems(ingredientList);
        targetListViews.add(craft1);
        targetListViews.add(craft2);
        targetListViews.add(craft3);
        targetListViews.add(craft4);
        targetListViews.add(craft5);
        targetListViews.add(craft6);
        targetListViews.add(craft7);
        targetListViews.add(craft8);
        targetListViews.add(craft9);
        ingredients.setOnDragDetected(event -> dragItem(ingredients));

        for (ListView<String> listView : targetListViews){
            listView.setOnDragDetected(event -> dragItem(listView));
            listView.setOnDragOver(event -> allowDrop(listView, event));
            listView.setOnDragDropped(event -> copyItem(listView, event));
        }


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

    @FXML private void changeSize(){
        largeSize = !largeSize;
        craft3.setVisible(largeSize);
        craft6.setVisible(largeSize);
        craft7.setVisible(largeSize);
        craft8.setVisible(largeSize);
        craft9.setVisible(largeSize);
        if(!largeSize){
            craft1.setLayoutX(328);
            craft2.setLayoutX(445);
            craft4.setLayoutX(328);
            craft5.setLayoutX(445);
        }else{
            craft1.setLayoutX(270);
            craft2.setLayoutX(386.6);
            craft4.setLayoutX(270);
            craft5.setLayoutX(386.6);
        }
    }


    private void dragItem(ListView<String> listView) {
        Dragboard db = listView.startDragAndDrop(TransferMode.COPY);
        ClipboardContent content = new ClipboardContent();
        content.putString(listView.getSelectionModel().getSelectedItem());
        if(listView!=ingredients){
            listView.getItems().clear();
        }
        db.setContent(content);
    }

    private void allowDrop(ListView<String> listView, DragEvent event) {
        if (event.getDragboard().hasString()) {
            if (listView.getItems().isEmpty()) {
                // If the target list is empty, allow copy
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                // If the target list already has an item, allow copy and clear
                event.acceptTransferModes(TransferMode.COPY, TransferMode.MOVE);
            }
        }
    }

    private void copyItem(ListView<String> listView, DragEvent event) {
        Dragboard db = event.getDragboard();

        if (db.hasString()) {
            String draggedItem = db.getString();
            ObservableList<String> targetList = listView.getItems();

            if (targetList.isEmpty()) {
                // If the target list is empty, just add the dragged item
                targetList.add(draggedItem);
            } else {
                // If the target list already has an item, clear it and add the dragged item
                targetList.clear();
                targetList.add(draggedItem);
            }
        }
    }
}
