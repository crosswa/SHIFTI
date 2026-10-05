/*
game cycle workflow (cycle 1)

get board gui initially set up
have pieces functional
have dice rolled connected


*/


public class Main {
    static Board board;
    static BoardInput boardInput;
    static PieceMovement movement;
    static DiceRoll dice;
    static Client client;
    static Server server;

    public static void main(String[] args) {
        //server =  new Server(5000);
        client = new Client("127.0.0.1",5000);
        //board = new Board();
        //boardInput = new BoardInput(board);
        //board.setBoardInput(boardInput);
        //start();
    }
/*
    public static void start(){
        movement = new PieceMovement(board);
        int currentPlayer = 0;
        boolean win = false;

        while(!win)
        {
            System.out.println("Player " + currentPlayer + "'s turn. Rolling dice...");

            DiceRoll.Face dice1 = DiceRoll.dieA().roll();
            DiceRoll.Face dice2 = DiceRoll.dieB().roll();
            System.out.println("Die A: " + dice1 + "   Die B: " + dice2);

            // handling only the "both numbers" case for now — star/slider
            // logic isn't wired to Board_shift yet, so those turns are skipped
            if (dice1.type == DiceRoll.FaceType.NUMBER && dice2.type == DiceRoll.FaceType.NUMBER) {
                int steps = dice1.value + dice2.value;

                System.out.println("Click a square " + steps + " tiles away for piece 0...");

                int[] piecePos = movement.getPiecePosition(currentPlayer, 0);
                java.util.List<int[]> options = boardInput.getReachableSquares(piecePos[0], piecePos[1], steps);

                if (options.isEmpty()) {
                    System.out.println("No legal moves for piece 0 with a roll of " + steps + " — skipping.");
                } else {
                    // board.highlightSquares(options);

                    System.out.println("Click one of the highlighted squares...");
                    waitForSelection();

                    movement.movePiece(currentPlayer, 0, steps, boardInput);
                    boardInput.clearSelection();
                    // board.clearHighlights();
                }

            if (movement.checkWin(currentPlayer)) {
                System.out.println("Player " + currentPlayer + " wins!");
                win = true;
                break;
            }

            currentPlayer = (currentPlayer + 1) % board.numPLayers;
        }
        }
    }

    // blocks the main thread until a click has registered a selection in BoardInput
    private static void waitForSelection() {
        while (boardInput.getSelectedSquare() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

 */
}