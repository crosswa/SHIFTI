//each players piece
//keeps track of their position on the board

public class Piece {
    final int playerId;
    final int pieceIndex;
    int[] winningSquare;
    int startX, startY;
    int x, y;
    boolean finished;

    Piece(int playerId, int pieceIndex, int startX, int startY) {
        this.playerId = playerId;
        this.pieceIndex = pieceIndex;
        //starting point
        this.startX = startX;
        this.startY = startY;
        //current position
        this.x = startX;
        this.y = startY;
    
    }

    public int[] getStartPosition() {
        return new int[]{startX, startY};
    }

    public int[] getWinningSquare(int playerId) {
        switch(playerId){
            case 0: //player 1 starts at bottom left -> winning square is top right
            return new int[]{0,12};
            case 1: //player 2 starts at top right -> winning square is bottom left
            return new int[]{11,0};
            case 2: //player 3 starts at bottom right -> winning square is top left
            return new int[]{0,0};
            case 3: //player 4 starts at top left -> winning square is bottom right
            return new int[]{11,12};
            default:
            return new int[]{-1,-1}; //invalid player
        }
    }

}
