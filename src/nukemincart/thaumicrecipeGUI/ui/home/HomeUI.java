package nukemincart.thaumicrecipeGUI.ui.home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("HomeUI.fxml"));

        Scene scene = new Scene(root, 750, 500);

        stage.initStyle(StageStyle.UNDECORATED);

        stage.setTitle("Thaumic Recipe Tweaker Home");
        stage.setScene(scene);
        stage.show();
    }
}
