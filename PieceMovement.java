import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.basic.BasicBorders;

//class for character piece movement
//controls the movement of pieces on the board, tracks the positions, 

public class PieceMovement {

    private static int players = 2; // number of players
    private static int piecesPerPlayer = 4; // number of pieces per player

    private Piece[][] pieces = new Piece[players][piecesPerPlayer];
    private Board board;

    //using getStartPosition, getDestination, and isWinningSquare as placeholders for board methods 
    
    public PieceMovement (Board board) {
        this.board = board;
        for (int p = 0; p < players; p++) {
            for (int i = 0; i < piecesPerPlayer; i++) {
                int[] start = board.getStartPosition(p, i); //start position helper needs to be added in board
                pieces[p][i] = new Piece(p, i, start[0], start[1]);
            }
        }
    }

    //steps= number the dice rolled
    public void movePiece(int player, int pieceIndex, int steps) {
        Piece piece = pieces[player][pieceIndex];

        int[] dest = board.getDestination(piece.x, piece.y, steps, player);

        if (isValidMove(dest, player, pieceIndex)) {
            piece.x = dest[0];
            piece.y = dest[1];
            checkCollision(player, pieceIndex);

            if (board.isWinningSquare(dest[0], dest[1], player)) {
                piece.finished = true;
                System.out.println("Player " + player + " piece " + pieceIndex + " reached the finish!");
            }
            if (checkWin(player)) {
                System.out.println("Player " + player + " has won the game!");
            }
        } else {
            System.out.println("Invalid move, please select another position");
        }
    }

    public int[] getPiecePosition(int player, int pieceIndex) {
        Piece currentPiece = pieces[player][pieceIndex];
        return new int[] { currentPiece.x, currentPiece.y };
    }

    public boolean isValidMove(int[] dest, int player, int pieceIndex) {
        Piece piece = pieces[player][pieceIndex];

        //checks to make sure if the piece isn't already at the winning square
        if (piece.finished) {
            System.out.println("That piece has already finished and can't be moved.");
            return false;
        }

        if (dest == null) {
            return false; // no legal destination
        }

        //returns true/false whether there is a path or not
        return board.Grid_Matrix[dest[0]][dest[1]] == 1;
    }

    //checks whether the player has won
    public boolean checkWin(int player) {
        for (Piece piece : pieces[player]) {
            if (!piece.finished) return false;
        }
        return true;
    }

    public void checkCollision(int movingPlayer, int pieceIndex) {
        // code to check if the player's piece has collided with the opponent's piece
        for (int p = 0; p < players; p++) {
            if (p == movingPlayer) continue;
            for (Piece opp : pieces[p]) {
                if (!opp.finished && opp.x == pieces[movingPlayer][pieceIndex].x && opp.y == pieces[movingPlayer][pieceIndex].y) 
                    {
                    resetPiece(opp.playerId, opp.pieceIndex);
                    System.out.println("Player " + movingPlayer + " captured Player " + p + "'s piece!");
                }
            }
        }
    }

    public void resetPiece(int player, int pieceIndex) {
        int[] start = board.getStartPosition(player, pieceIndex);
        pieces[player][pieceIndex].x = start[0];
        pieces[player][pieceIndex].y = start[1];
    }
}

