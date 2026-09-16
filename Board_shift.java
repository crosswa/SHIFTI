import java.util.Random; 



public class Board_shift {

    public static void changeGrid(int[][] Grid_Matrix) {


    Random random = new Random();
    //Completely Randomizes the Grid Matrix array by turning the 0s into 1s
        for (int row = 2; row <= 9; row++ ) {
            for (int col = 0; row < Grid_Matrix[row].length; col++) {


                if (Grid_Matrix[row][col] == 1 && random.nextBoolean()) {
                    Grid_Matrix [row][col] = 0;
                    
                }
            }
        }

    //"Pushes" rows by checking to see what the value is to its right (or left), and then adding 1 or subtracting by 1. In the event in which a tile gets pushed off
    // further than what's visible, it would wrap to the other side and place there. (going to leave this alone for now until the dice roll system is finished)
        
    for (int row = 2; row <= 9; row++) {

    int first = Grid_Matrix[row][0];

   
    for (int col = 0; col < Grid_Matrix[row].length - 1; col++) {
        Grid_Matrix[row][col] = Grid_Matrix[row][col + 1];
    }

    
    Grid_Matrix[row][Grid_Matrix[row].length - 1] = first;
}
    }
}