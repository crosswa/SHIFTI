//class for character piece movement
//controls the movement of pieces on the board, tracks the positions, uses board input to determine moves chosen

public class PieceMovement {

    private Board board;

    private int players; // number of players
    private static int piecesPerPlayer = 4; // static since every player always has 4 pieces

    private Piece[][] pieces;

    private int currentPlayer;

    //constructor, uses piece class to build each piece for the player
    public PieceMovement (Board board) {
        this.board = board;
        this.players = board.numPLayers;
        this.pieces = new Piece[players][piecesPerPlayer];
        for (int p = 0; p < players; p++) {
            for (int i = 0; i < piecesPerPlayer; i++) {
                int[] start = Piece.getStartPosition(p);
                pieces[p][i] = new Piece(p, i, start[0], start[1]);
                pieces[p][i].winningSquare = Piece.getWinningSquare(p);
            }
        }
    }

    //moves the piece on the board, uses board input to determine moves chosen
    public void movePiece(int player, int pieceIndex, int steps, BoardInput boardInput) {
        Piece piece = pieces[player][pieceIndex];

        currentPlayer = player;

        int[] dest = boardInput.getDestination(piece.x, piece.y, steps, player); //steps= number the dice rolled

        //checks if the move is valid first, then moves the piece by updating the coordinates
        if (isValidMove(dest, player, pieceIndex)) {
            piece.x = dest[0];
            piece.y = dest[1];
            checkCollision(player, pieceIndex); //checkCollision is used to check if there is an opponents piece in the same square

            //if the player reaches the winning square, that piece is marked as finished and can not be moved
            if (isWinningSquare(dest[0], dest[1], player)) {
                piece.finished = true;
                System.out.println("Player " + player + " piece " + pieceIndex + " reached the finish!");
            }
            //check if the player has won
            if (checkWin(player)) {
                System.out.println("Player " + player + " has won the game!");
            }
        } else {
            //if a move is invalid, the players piece does not move and is told to select another position
            System.out.println("Invalid move, please select another position");
        }
    }

    //helper to return piece position
    public int[] getPiecePosition(int player, int pieceIndex) {
        Piece currentPiece = pieces[player][pieceIndex];
        return new int[] { currentPiece.x, currentPiece.y };
    }

    //valid move checker, checks if the destination is valid and if the piece hasn't already finished
    public boolean isValidMove(int[] dest, int player, int pieceIndex) {
        Piece piece = pieces[player][pieceIndex];

        //checks to make sure if the piece isn't already at the winning square or is on water.
        if (piece.finished || board.Grid_Matrix[piece.x][piece.y] == 0) {
            System.out.println("That piece can't be moved.");
            return false;
        }

        if (dest == null) {
            return false; // no legal destination
        }

        //returns true/false whether there is a path or not
        return board.Grid_Matrix[dest[0]][dest[1]] == 1;
    }

    //checks whether the player has won by checking if all of their pieces have reached the winning square
    public boolean checkWin(int player) {
        for (Piece piece : pieces[player]) {
            if (!piece.finished) return false;
        }
        return true;
    }

    // code to check if the player's piece has collided with the opponent's piece
    // if the player's piece is on the same square as an opponent's piece, the opponent's piece is captured and reset
    public void checkCollision(int movingPlayer, int pieceIndex) {
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

    // resets the piece to its starting position
    public void resetPiece(int player, int pieceIndex) {
        int[] start = Piece.getStartPosition(player);
        pieces[player][pieceIndex].x = start[0];
        pieces[player][pieceIndex].y = start[1];
    }

    //checks if the square is the winning square
    public boolean isWinningSquare(int row, int col, int player) {
        //returns t/f based on if the player is on their winning square
        return row == pieces[player][0].winningSquare[0] && col == pieces[player][0].winningSquare[1];
    }

    public int getPlayer(){
        return currentPlayer;
    }

}

