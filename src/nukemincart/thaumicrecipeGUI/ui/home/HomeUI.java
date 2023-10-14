package nukemincart.thaumicrecipeGUI.ui.home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("HomeUI.fxml"));

        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("Thaumic Recipe Tweaker Home");
        stage.setHeight(500);
        stage.setWidth(750);
        stage.setScene(scene);
        stage.show();
    }
}
