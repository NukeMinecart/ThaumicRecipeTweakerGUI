package nukemincart.thaumicrecipeGUI.ui.recipe;

import nukemincart.thaumicrecipeGUI.ui.HomeUI;

import javax.swing.*;

public class RecipeManager {

    public static void main(String[] args){
        JFrame frame = new JFrame("<class name>");
        frame.setContentPane(new HomeUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
