package main.java.nukeminecart.thaumicrecipe.ui.home;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeUI extends Application {
    public Button closeButton;
    private static Stage stage;
    private double x,y;
    public Rectangle toolbarRect;
    @Override
    public void start(Stage stage) throws Exception {
        HomeUI.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("HomeUI.fxml"));
        Scene scene = new Scene(root, 750, 500);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Thaumic Recipe Tweaker Home");
        loadScreen(scene);
    }
    public Stage getStage(){
        return stage;
    }
    public static void loadScreen(Scene scene) {
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }
    @FXML
    protected void closeScreen(ActionEvent event) {
        Platform.exit();
    }
    @FXML public void panePressed(MouseEvent me){
        x = getStage().getX() - me.getScreenX();
        y = getStage().getY() - me.getScreenY();
    }

    @FXML public void paneDragged(MouseEvent me){
        getStage().setX(x + me.getScreenX());
        getStage().setY(y + me.getScreenY());
    }

}
