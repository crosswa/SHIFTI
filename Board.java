import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
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

    }

    private void boardshift(int row , int col){

    }

    Board() {
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        turnLabel = new JLabel("Player Turn", SwingConstants.CENTER);
        GridBagConstraints turnText = new GridBagConstraints();
        turnText.insets = new Insets(0,0,0,0);

        mainPanel.add(turnLabel, turnText);

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
                    }
                });
            }
        }

        //layed backround
        Dimension boardSize = new Dimension(1060,972);
        JLayeredPane backgroundLayer = new JLayeredPane();
        backgroundLayer.setPreferredSize(new Dimension(boardSize));


        JLabel background = new JLabel(new ImageIcon("src/shifti_board.png"));
        background.setBounds(0,0,1060,972);
        backgroundLayer.add(background,JLayeredPane.DEFAULT_LAYER);

        int cellWidth = 1060/13;
        int cellHeight = 972/12;
        int rowShift = 0;

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
            sliders.setBounds(rowShift,(row * 162),1060, 162);
            backgroundLayer.add(sliders,JLayeredPane.PALETTE_LAYER);

        }
        //mainPanel.add(background);
        mainPanel.add(backgroundLayer);
        mainPanel.setBackground(Color.BLACK);
        frame.add(mainPanel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);



    }

}



