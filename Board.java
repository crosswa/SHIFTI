import javax.swing.*;
import javax.swing.ImageIcon;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;


public class Board{
    JFrame frame = new JFrame();
    JPanel boardPanel = new JPanel();

    JButton[][] board = new JButton[12][15];
    int[][] Grid_Matrix = {//1 == true, has a tile //0 == false no tile (water)
            {0,1,1,1,1,0,1,1,1,0,1,1,1,1,0},
            {0,1,1,0,1,0,1,0,1,0,1,0,1,1,0},
            {1,0,0,0,1,1,0,1,0,1,0,1,1,0,0},
            {1,1,0,0,0,1,1,1,0,1,1,1,0,0,0},
            {0,0,1,0,0,0,1,0,0,0,1,0,0,0,1},
            {0,0,1,0,0,0,1,0,0,1,1,1,0,0,1},
            {0,0,1,0,0,1,1,1,0,0,1,0,0,0,1},
            {0,0,1,0,0,0,1,0,0,0,1,0,0,0,1},
            {1,1,0,0,0,1,1,1,0,1,1,1,0,0,1},
            {1,0,0,1,1,1,0,1,0,1,1,0,0,0,0},
            {0,1,1,0,1,0,1,0,1,0,1,1,1,1,0},
            {0,1,1,1,1,0,1,1,1,0,1,0,1,1,0}
    };

    //JLayeredPane sliders = new JLayeredPane();
    //JLabel rowSliders = new JLabel[4];
    //int[] slideroffset = new int[4];


    Board() {
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel background = new JLabel(new ImageIcon("src/shifti_board.png"));
        String[] Pngs = {
                "src/shifti_board_row2.png",
                "src/shifti_board_row3.png",
                "src/shifti_board_row4.png",
                "src/shifti_board_row2.png"
        };

        background.setLayout(new GridLayout(12,15));

        for(int row = 0; row < 12; row++){
            for(int col = 0; col < 15; col++){
                board[row][col] = new JButton();
                board[row][col].setContentAreaFilled(false);
                background.add(board[row][col]);
            }
        }
        frame.add(background);

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}



