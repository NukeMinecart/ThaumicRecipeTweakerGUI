package main.java.nukeminecart.thaumicrecipe.ui.recipe.manager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import main.java.nukeminecart.thaumicrecipe.ui.UIManager;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.Recipe;
import main.java.nukeminecart.thaumicrecipe.ui.recipe.file.versions.FileParserV1;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RecipeManagerUI {



    private static final ObservableList<Recipe> recipes = FXCollections.observableArrayList();
    public static HashMap<String, Recipe> recipeEditorMap = new HashMap<>();

    @FXML
    public ListView<Recipe> recipeList;
    @FXML
    private Label title;
    private static String stringTitle;
    private double x,y;

    public static Parent getScene() throws IOException {

        return FXMLLoader.load(Objects.requireNonNull(RecipeManagerUI.class.getResource("RecipeManagerUI.fxml")));
    }

    public static void loadManager(String name, List<String> contents) throws IOException {
        if(contents != null && contents.size()>1){
            String version = contents.get(0);
            version = version.replace("V","");
            version = version.replace(";","");
            contents.remove(0);
            if(version.equals("1")) {
                recipes.addAll(new FileParserV1().getRecipesFromString(contents));
            }
        }
        RecipeManagerUI.stringTitle = name.endsWith(".rcp") ? name : name+".rcp";
        UIManager.loadScreen(getScene());
    }


    @FXML
    public void initialize(){
        recipeList.setCellFactory(new RecipeCellFactory());
        recipeList.setItems(recipes);
        title.setText("Recipe Manager: "+stringTitle);
        recipes.addListener((ListChangeListener<Recipe>) c -> refreshHashMap());
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

    public static void openEditor(String recipeName){

    }
    private static void refreshHashMap(){
        for(Recipe recipe: recipes){
            recipeEditorMap.put(recipe.getName(),recipe);
        }
    }
}
