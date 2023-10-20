package main.java.nukeminecart.thaumicrecipe.ui;


import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Transition {
    public static void Fade(Duration durationMillis, int startOpacity, int endOpacity, Node node){
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis.toMillis()), node);
        ft.setFromValue(startOpacity);
        ft.setToValue(endOpacity);
        ft.setAutoReverse(true);
        ft.play();
    }
}
