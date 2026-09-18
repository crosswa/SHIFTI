//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

/*
            game workflow

            players select how many players (atleast 2)
            assign each player their pieces (piece class)
            start game (roll who goes first? random? player 1 is who starts first)
            player one rolls dice 
            moves/shifts board based on dice roll
            ^ uses diceroll, piecemovement, and/or boardshift
            player two rolls dice
            moves/shifts board based on dice roll
            ^ uses diceroll, piecemovement, and/or boardshift
            (repeat for players 3-4; if they exist)
            loop until a player wins

        */


public class Main {
    static Board board;
    static BoardInput boardInput;
    static PieceMovement movement;
    static DiceRoll dice;

    public static void main(String[] args) {
        board = new Board();
        boardInput = new BoardInput(board);
        // start();
    }

    // public static void start(){
    //     movement = new PieceMovement(board);
    //     dice
    //     System.out.println("Player 1: roll dice");
        
    // }
}