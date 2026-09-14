import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.io.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.*;


public class Board{
    JFrame frame = new JFrame();
    JPanel boardPanel = new JPanel(new GridBagLayout());

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

    private void ButtonClick(int row, int col){
        ImageIcon icon = new ImageIcon("src/theseus-stand.png");
        JLabel sprite = new JLabel(icon);

        Component cell = board[row][col];



        if(cell instanceof JLayeredPane){
            JLayeredPane layeredcell = (JLayeredPane) cell;
            layeredcell.add(sprite, JLayeredPane.DRAG_LAYER);
            layeredcell.revalidate();
            layeredcell.repaint();
        }else if(cell instanceof Container){
            ((Container) cell).add(sprite);
            ((Container) cell).revalidate();
            ((Container) cell).repaint();
        }

        sprite.add(board[row][col]);

    }

    Board() {
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel background = new JLabel(new ImageIcon("src/shifti_board (2).png"));

        //JLayeredPane sprite = new JLayeredPane(new ImageIcon("src/thesus-stand"));

        Dimension boardSize = new Dimension(1230,972);
        background.setPreferredSize(boardSize);

        String[] Pngs = {
                "/src/shifti_board.png",
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

                final int currentRow = row;
                final int currentCol = col;

                board[row][col].addActionListener(e -> {
                    ButtonClick(currentRow, currentCol);
                });

            }
        }


/*
        for(int i = 0; i < 4; i++)
        {
            JLabel sliders = new JLabel(new ImageIcon(Pngs[i]));
            sliders.setLayout(new GridLayout(2,15));

        }
*/

        //frame.add(background);
        //frame.pack();
        //frame.setLocationRelativeTo(null);

        boardPanel.add(background, new GridBagConstraints());
        frame.add(boardPanel, BorderLayout.CENTER);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setVisible(true);
    }

}



