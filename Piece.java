//each players piece
//keeps track of their position on the board

public class Piece {
    final int playerId;
    final int pieceIndex;
    int winningSquare;
    int x, y;
    boolean finished;

    Piece(int playerId, int pieceIndex, int startX, int startY) {
        this.playerId = playerId;
        this.pieceIndex = pieceIndex;
        this.x = startX;
        this.y = startY;
    
    }

    public int[] getStartPosition() {
        return new int[]{x, y};
    }

}
