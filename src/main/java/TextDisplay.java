// import java.lang.String;
//helper class for text lol

public class TextDisplay extends Main{
    private String display;
    
    public String turn(int player){
        display = "Player " + player + "turn. Roll the dice";
        return display;
    }
    public String roll(int roll1, int roll2){
        display = "Rolled " + roll1 + " + " + roll2;
        return display;
    }

    public String movesLeft(int player, int moves){
        display = "Player " + player + " moves left: " + moves;
        return display;
    }
    
    public String capture(int player, int opp){
        display = "Player " + player + " has captured Player " + opp + "'s piece!";
        return display;
    }

    public String win(int player){
        display = "Player " + player + " wins!";
        return display;
    }
}
