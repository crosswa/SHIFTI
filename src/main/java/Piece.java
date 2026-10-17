public class Piece {
    private int x;
    private int y;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    // path checking is already in BoardController
    public boolean canMove(int row, int col) {
        return true;
    }

    public boolean isWin(int player) {
        if (player == 1) {
            return x == 0 && y == 11;
        } else if (player == 2) {
            return x == 13 && y == 0;
        }
        return false;
    }
}