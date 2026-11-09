public class Piece {
    private int x;
    private int y;

    // The tile this piece started on (a captured piece is sent back here)
    private final int startX;
    private final int startY;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
    }

    // Sends the piece back to its starting tile (used by PieceCapture)
    public void resetToStart() {
        this.x = startX;
        this.y = startY;
    }

    // x = row, y = column on the board grid
    public int getRow() {
        return x;
    }

    public int getCol() {
        return y;
    }

    // updates the piece's stored position (BoardController calls this after a legal move)
    public void moveTo(int row, int col) {
        this.x = row;
        this.y = col;
    }

    //checks if the movement is valid, path checking is already in BoardController
    public boolean canMove(int row, int col) {
        return true;
    }

    public static BoardLayout.Door goalFor(int player) {
        return switch (player) {
            case 1 -> BoardLayout.TEAL;
            case 2 -> BoardLayout.RED;
            default -> throw new IllegalArgumentException("Unknown player: " + player);
        };
    }

    public boolean isWin(int player) {
        return goalFor(player).isEntrance(x, y);
    }
}
