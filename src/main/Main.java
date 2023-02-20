package main;

import Graphic.GameProcess;
import Graphic.UI;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);

        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(false); // <-- the title bar is removed here
        window.setTitle("2D Game");



        //JPanel playerStat = new JPanel();

        GameProcess gameProcess = new GameProcess();
//        BoxLayout boxlayout = new BoxLayout(gameProcess, BoxLayout.Y_AXIS);
//        gameProcess.setLayout(boxlayout);

        //UI ui = new UI(gameProcess);
        window.add(gameProcess);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);


        gameProcess.setupGame();
        gameProcess.startGameThread();

    }
}
