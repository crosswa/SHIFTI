//piece class
/*
    this class holds every pieces information, including the player (playerId), which piece is where on the board (pieceIndex), 
    their starting position (startX, startY) + winning position (winningSquare), and if that piece is able to be moved (finished)

    piece index: 0-3 (4 pieces)

    players (min 2, max 4):
    player 1:
        playerId:0

        {starts in bottom left -> ends in top right}
        startX:11
        startY:0
        winningSquare:[0,12]

    player 2:
        playerId:1

        {starts in top right -> ends in bottom left}
        startX:0
        startY:12
        winningSquare:[11,0]

    player 3:
        playerId:2

        {starts in bottom right -> ends in top left}
        startX:11
        startY:12
        winningSquare:[0,0]

    player 4:
        playerId:3

        {starts in top left -> ends in bottom right}
        startX:0
        startY:0
        winningSquare:[11,12]

*/

public class Piece {
    final int playerId;
    final int pieceIndex;
    int[] winningSquare;
    int startX, startY;
    int x, y;
    boolean finished;

    //each piece is built here
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

    //- two helpers below may be adjusted with the correct boards coordinates if needed -

    //finds start position for each player
    public static int[] getStartPosition(int playerId) {
         switch(playerId){
            case 0: //player 1 starts at bottom left
            return new int[]{11,0};
            case 1: //player 2 starts at top right
            return new int[]{0,12};
            case 2: //player 3 starts at bottom right
            return new int[]{11,12};
            case 3: //player 4 starts at top left
            return new int[]{0,0};
            default:
            return new int[]{-1,-1}; //invalid player
        }
    }

    //finds winning position for each player
    public static int[] getWinningSquare(int playerId) {
        switch(playerId){
            case 0: //bottom left -> winning square is top right
            return new int[]{0,12};
            case 1: //top right -> winning square is bottom left
            return new int[]{11,0};
            case 2: //bottom right -> winning square is top left
            return new int[]{0,0};
            case 3: //top left -> winning square is bottom right
            return new int[]{11,12};
            default:
            return new int[]{-1,-1}; //invalid player
        }
    }

}
