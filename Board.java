import javax.swing.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;


public class Board{
    JFrame frame = new JFrame();
    JPanel[][] board = new JPanel[12][13];

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

    //Player icons
    int numPLayers = 4;
    int spriteNumber = 3;

    JLabel[][] sprites = new JLabel[numPLayers][spriteNumber];

    int[][] spriteRow = new int[numPLayers][spriteNumber];
    int[][] spriteCol = new int[numPLayers][spriteNumber];

    int currentplayer = 0;

    JLabel selectedSprite = null;

    int selectedPlayer = -1;
    int spriteIndex = -1;

    JLabel turnLabel;

    String[][] SpriteIcons = {
            {
                "src/player1_stand.png",
                "src/player1_sword.png",
                "src/player1_swing.png"
            },
            {
                "src/player2_stand.png",
                "src/player2_sword.png",
                "src/player2_swing.png"
            },
            {
                "src/player3_stand.png",
                "src/player3_sword.png",
                "src/player3_swing.png"
            },
            {
                "src/player4_stand.png",
                "src/player4_sword.png",
                "src/pkayer4_sword.png"
            }
    };

    protected void ButtonClick(int row, int col){
        ImageIcon icon = new ImageIcon("src/player1_stand.png");
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

        //board[row][col].add(sprite);

    }

    Board() {
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        turnLabel = new JLabel("Player Turn", SwingConstants.CENTER);
        GridBagConstraints turnText = new GridBagConstraints();
        turnText.insets = new Insets(5,0,5,0);

        mainPanel.add(turnLabel, turnText);

        for(int row = 0; row < 12; row++){
            for(int col = 0; col < 13; col++){
                JPanel cell = new JPanel(new BorderLayout());
                cell.setOpaque(false);
                board[row][col] = cell;

                final int currentRow = row;
                final int currentCol = col;


                board[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println(currentRow + "," + currentCol);
                        ButtonClick(currentRow, currentCol);
                    }
                });
            }
        }


        JLabel background = new JLabel(new ImageIcon("src/shifti_board (2).png"));
        background.setLayout(new GridLayout(12,13));
        Dimension boardSize = new Dimension(1060,972);
        background.setPreferredSize(boardSize);

        for(int row = 0; row < 12; row++){
            for(int col = 0; col < 13; col++){
                background.add(board[row][col]);
            }
        }

        mainPanel.add(background);
        mainPanel.setBackground(Color.BLACK);
        frame.add(mainPanel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

/*
        String[] Pngs = {
                "/src/shifti_board.png",
                "src/shifti_board_row2.png",
                "src/shifti_board_row3.png",
                "src/shifti_board_row4.png",
                "src/shifti_board_row2.png"
        };

        for(int i = 0; i < 4; i++)
        {
            JLabel sliders = new JLabel(new ImageIcon(Pngs[i]));
            sliders.setLayout(new GridLayout(2,13));

        }

        //frame.add(background);
        //frame.pack();
        //frame.setLocationRelativeTo(null);

        */

    }

}



