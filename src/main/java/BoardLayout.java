public class BoardLayout {

    /*
        SHIFTI board layout
        
        Board size: 15 rows x 13 columns
        
        13x 13 should be flip flopped and the 1 and 15th rows should be not accessable since they are shifted on and off the board.
        
        S = Stone/path (walkable)
        W = Water (blocked)
        
        0 = Red door    (top-left)
        1 = Teal door   (top-right)
        2 = Green door  (bottom-left)
        3 = Yellow door (bottom-right)
        
        Opposite door pairs:
        Red   <-> Yellow
        Green <-> Teal
        
        The green-to-teal route is connected.

        there is a default layout for now, in future cycles this class will be used 
        to create more randomized layouts and handles board shifting.
     */

    public static final String[] DEFAULT = {

        "0SWWSSSWWSS1S",
        "SSWSSWSWSSSSS",
        "SWWSSSSWSWSSW",
        "SWWWSSSSSWSWS",
        "SSWWWSSSSWSSW",
        "WSWWWSWWWSWWW",
        "WSWWWSWWWSWWW",
        "WSSSSSSWWSWWW",
        "WSWWWSWWWSWWW",
        "WSWWWSWWWSWWW",
        "WSWWSSSWWSWWW",
        "WSWWWSWWWSWWW",
        "WSWWWSWWWSWWW",
        "2SSSWSWSWSS3S",
        "SSSWWSWSWSWSS"

    };
}