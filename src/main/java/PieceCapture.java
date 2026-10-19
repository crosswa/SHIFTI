public class PieceCapture {
    

// Update 10/17: just realized there is no "removed piece area", so i added this int that pushes anything thats captured going off the grid in -1. 
// BoardController.drawPlayer() skips everything ouside the board so theres nothing to worry about in that aspect (Feel free to put that in the presentation I guess)

public static final int OFF_BOARD = -1;

// Boolean to return the piece info of it being on board or not

public static boolean isOnBoard(Piece piece) {

    return piece.getRow() != OFF_BOARD || piece.getCol() != OFF_BOARD;

}

//  made true if both pieces are on the board and on the same tiles


public static boolean isSameTile(Piece a, Piece b) {

    return isOnBoard (a) && isOnBoard(b)
        && a.getRow() == b.getRow()
        && a.getCol() == b.getCol();
}


// Call after the moving piece has moved, even mid-turn.
    // If it landed on the other piece, the other piece is removed from the board.
// Returns true if a capture happened

public static boolean capture(Piece mover, Piece other)
 {
    if (mover == other || !isSameTile(mover, other)) {
        return false;
    }
 
    other.resetToStart();   // was other.moveTo(OFF_BOARD, OFF_BOARD)
    return true;
 
 }

// Same as capture() from earlier, but checks the mover against every piece in the array.
    // Returns true if at least one piece was captured.
public static boolean captureAll(Piece mover, Piece [] pieces) {

    boolean captured = false;
    for (Piece other : pieces) {
        if (capture(mover, other)) {
            captured = true;
        }
    }

    return captured;
}


}
