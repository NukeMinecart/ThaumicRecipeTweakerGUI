package main.java.nukeminecart.thaumicrecipe.ui;


import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * A class that contains all the transitions used in ThaumicRecipeTweakerGUI
 */
public class Transition {
    /**
     * Fade the given node linearly
     * @param duration duration for animation
     * @param startOpacity the opacity to start at
     * @param endOpacity the opactiy to end at
     * @param node the node to fade
     */
    public static void Fade(Duration duration, int startOpacity, int endOpacity, Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(duration.toMillis()), node);
        ft.setFromValue(startOpacity);
        ft.setToValue(endOpacity);
        ft.play();
    }
}
