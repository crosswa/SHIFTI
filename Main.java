//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        BoardInput boardInput = new BoardInput(board);
        PieceMovement movement = new PieceMovement(board);
        
        for(int i = 0; i < 4; i++)
        {
           System.out.println("player " + i);
        }
    }
}