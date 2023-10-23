package main.java.nukeminecart.thaumicrecipe.ui;

import javafx.scene.control.Label;
import javafx.util.Duration;

public class ErrorWarning {
    public static void invalidFileError(Label node, String message){
        node.setText(message);
        Transition.Fade(Duration.millis(10000),1,0,node);
    }
}
