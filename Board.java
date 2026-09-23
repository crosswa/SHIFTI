import javax.swing.*;

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
                "src/player4_sword.png"
            }
    };

    private BoardInput boardInput;

    public void setBoardInput(BoardInput boardInput) {
        this.boardInput = boardInput;
    }


    protected void buttonClick(int row, int col){
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

    }

    public Component boardShift(JPanel mainPanel){
        JPanel buttonPanel = new JPanel();
        JPanel R_buttonPanel = new JPanel();

        buttonPanel.setLayout(null);
        R_buttonPanel.setLayout(null);

        buttonPanel.setBackground(Color.BLACK);
        R_buttonPanel.setBackground(Color.BLACK);

        buttonPanel.setPreferredSize(new Dimension(114,972));
        R_buttonPanel.setPreferredSize(new Dimension(114,972));

        ImageIcon arrowR = new ImageIcon("src/arrow.png");
        ImageIcon arrowL = new ImageIcon("src/arrow left.png");

        JButton L_arrow1 = new JButton(arrowL);
        //L_arrow1.setBounds(0,200,114,80);

        JButton L_arrow2 = new JButton(arrowL);

        JButton L_arrow3 = new JButton(arrowL);

        JButton L_arrow4 = new JButton(arrowL);

        JButton R_arrow1 =  new JButton(arrowR);
        R_arrow1.setBounds(0,200,114,80);

        JButton R_arrow2 =  new JButton(arrowR);
        R_arrow2.setBounds(0,360,114,80);

        JButton R_arrow3 =  new JButton(arrowR);
        R_arrow3.setBounds(0,520,114,80);

        JButton R_arrow4 =  new JButton(arrowR);
        R_arrow4.setBounds(0,685,114,80);

        //shifting left
        /*
        R_arrow1.addActionListener(e -> row1shift = -1);

        R_arrow2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row2shift = -1;
            }
        });

        R_arrow3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row3shift = -1;
            }
        });

        R_arrow1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row4shift = -1;
            }
        });

        //shifting right

        L_arrow1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row1shift = 1;
            }
        });

        L_arrow2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row2shift = 1;
            }
        });

        L_arrow3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row3shift = 1;
            }
        });

        L_arrow1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                row4shift = 1;
            }
        });

         */

        buttonPanel.add(L_arrow1);
        buttonPanel.add(L_arrow2);
        buttonPanel.add(L_arrow3);
        buttonPanel.add(L_arrow4);
        buttonPanel.add(R_arrow1);
        buttonPanel.add(R_arrow2);
        buttonPanel.add(R_arrow3);
        buttonPanel.add(R_arrow4);

        return mainPanel.add(buttonPanel);
    }

    Board() {
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        //JPanel mainPanel = new JPanel(new BorderLayout());
        turnLabel = new JLabel("Player Turn", SwingConstants.CENTER);
        GridBagConstraints turnText = new GridBagConstraints();
        turnText.insets = new Insets(0,0,0,0);

        // mainPanel.add(turnLabel, turnText);

        //make board cells

        for(int row = 0; row < 12; row++){
            for(int col = 0; col < 13; col++){
                JPanel cell = new JPanel(new BorderLayout());
                cell.setOpaque(false);
                board[row][col] = cell;

                final int currentRow = row;
                final int currentCol = col;

                //check for click of cell and save coordinate
                board[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println(currentRow + "," + currentCol);
                        buttonClick(currentRow, currentCol);
                        if(boardInput != null){
                            boardInput.ButtonClick(currentRow, currentCol);
                        }

                    }
                });
            }
        }
        //layered background

        Dimension boardSize = new Dimension(1060,972);
        JLayeredPane backgroundLayer = new JLayeredPane();
        backgroundLayer.setPreferredSize(new Dimension(boardSize));


        JLabel background = new JLabel(new ImageIcon("src/shifti_board.png"));
        background.setBounds(0,0,1060,972);

        backgroundLayer.add(background,JLayeredPane.DEFAULT_LAYER);


        /*
        row shift value is updated by the board shift class
         */


        int cellWidth = 1060/13;
        int cellHeight = 972/12;

        //rowshift will be between -2 and -1 and 0 // -1 being the starting offset
        //int offset = 0;
        int row1Shift = -1; // + BoardShift();
        int row2Shift = -1; // + BoardShift();
        int row3Shift = -1; // + BoardShift();
        int row4Shift = -1; // + BoardShift();

        for(int row = 0; row < 12; row++){
            for(int col = 0; col < 13; col++){
                board[row][col].setBounds(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                backgroundLayer.add(board[row][col],JLayeredPane.MODAL_LAYER);
            }
        }

        String[] Pngs = {
                "src/shifti_board_row2.png",
                "src/shifti_board_row3.png",
                "src/shifti_board_row4.png",
                "src/shifti_board_row5.png"
        };

        for(int i = 0; i < 4; i++)
        {
            int row = i+1;
            JLabel sliders = new JLabel(new ImageIcon(Pngs[i]));
            if(i == 0){
                sliders.setBounds((row1Shift * cellWidth) -1,(row * 162),1230, 162);
            }
            else if(i == 1){
                sliders.setBounds((row2Shift * cellWidth) -1,(row * 162),1230, 162);
            }
            else if(i == 2){
                sliders.setBounds((row3Shift * cellWidth) -1,(row * 162),1230, 162);
            }
            else{
                sliders.setBounds((row4Shift *cellWidth) -1,(row * 162),1230, 162);
            }

            backgroundLayer.add(sliders,JLayeredPane.PALETTE_LAYER);

        }

        boardShift(mainPanel);

        mainPanel.add(backgroundLayer);
        mainPanel.setBackground(Color.BLACK);
        frame.add(mainPanel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    

}



