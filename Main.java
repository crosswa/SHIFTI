//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.awt.*;
import java.util.Vector;
import java.io.*;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        //Vector<String> Gameboard = new Vector<>();
        JFrame frame = new JFrame();

        frame.setSize(1800, 1000);


      ImageIcon board = new ImageIcon("shifti_board.png");
      JLabel label = new JLabel(board);
      frame.add(label);
      frame.setSize(500,500);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // using no layout managers
        label.setBounds(0, 0, board.getIconWidth(), board.getIconHeight());

        // making the frame visible
        frame.setVisible(true);
    }
}