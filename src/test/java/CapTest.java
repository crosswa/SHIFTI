public class CapTest {
    private static void check(boolean condition, String name) {
        if (!condition) {
            throw new AssertionError(name);
        }
        System.out.println("PASS " + name);
    }

    public static void main(String[] args) {
        Piece mover = new Piece(4, 5);
        Piece target = new Piece(7, 8);
        Piece[] pieces = {mover, target};

        check(!PieceCapture.isSameTile(mover, target), "Different squares do not match");
        check(!PieceCapture.captureAll(mover, pieces), "No capture on separate squares");
        check(!PieceCapture.capture(mover, mover), "A piece cannot capture itself");

        target.moveTo(4, 5);
        check(PieceCapture.isSameTile(mover, target), "Matching coordinates share a square");
        check(PieceCapture.captureAll(mover, pieces), "A capture is reported");
        check(target.getRow() == 7 && target.getCol() == 8, "Captured piece returns to its start");
        check(mover.getRow() == 4 && mover.getCol() == 5, "Capturing piece stays where it landed");
        System.out.println("7/7 capture checks passed.");
    }
}
