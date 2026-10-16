
public class Piece {
    private int x;
    private int y;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //checks if the movement is valid, path checking is already in BoardController
    public boolean canMove(int row, int col) {
        return true;
    }
}
