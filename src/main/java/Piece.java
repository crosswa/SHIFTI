
public class Piece {
    private String type;
    private int x;
    private int y;

    public Piece(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
