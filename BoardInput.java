import java.util.LinkedList;
import java.util.Queue;

//class for handling user input on the board with button clicks
public class BoardInput {
    
    private final Board board;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean squareSelected = false;

    public BoardInput(Board board) {
        this.board = board;
    }


    protected void ButtonClick(int row, int col) {
        selectSquare(row, col);
        board.ButtonClick(row, col);   
    }

    // call this from ButtonClick when the player is picking a destination
    public void selectSquare(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        squareSelected = true;
    }

    public int[] getSelectedSquare() {
        if (!squareSelected) return null;
        return new int[]{selectedRow, selectedCol};
    }

    public void clearSelection() {
        squareSelected = false;
        selectedRow = -1;
        selectedCol = -1;
    }

    // BFS shortest walkable-path distance between two grid cells
    public int getPathDistance(int startRow, int startCol, int destRow, int destCol) {
        int rows = board.Grid_Matrix.length;
        int cols = board.Grid_Matrix[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol, 0});
        visited[startRow][startCol] = true;

        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (current[0] == destRow && current[1] == destCol) {
                return current[2];
            }
            for (int[] d : directions) {
                int nr = current[0] + d[0];
                int nc = current[1] + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                    && !visited[nr][nc] && board.Grid_Matrix[nr][nc] == 1) {
                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc, current[2] + 1});
                }
            }
        }
        return -1; // unreachable
    }

     public int[] getDestination(int startX, int startY, int steps, int player) {
        int[] selected = getSelectedSquare();
        if (selected == null) {
            return null; // player hasn't clicked a square yet
        }

        int distance = getPathDistance(startX, startY, selected[0], selected[1]);

        if (distance != steps) {
            System.out.println("Selected square is " + distance + " tiles away, roll was " + steps);
            return null;
        }

        return selected;
    }
    
}
