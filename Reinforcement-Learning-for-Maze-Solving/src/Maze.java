import java.util.Random;
public class Maze
{
    private int[][] maze;
    public Maze(int mazeSize)
    {
        maze = new int[mazeSize][mazeSize];
        // Filling maze with walls
        for (int i = 0; i < GetSizeX(); i++)
        {
            for (int j = 0; j < GetSizeY(); j++)
            {
                maze[i][j] = 1;
            }
        }

        //Start position
        maze[0][0] = 3;

        //End position
        maze[GetSizeX() - 1][GetSizeY() - 1] = 2;

        //Recursive backtracking
        RecursiveBacktracking(0, 0);

        //Dirty fix - removing walls around exit
        maze[GetSizeX() - 2][GetSizeY() - 1] = 0;
    }
    public int GetSizeX()
    {
        return maze[0].length;
    }
    public int GetSizeY()
    {
        return maze.length;
    }
    public int GetValue(int x, int y)
    {
        return maze[x][y];
    }

    private void RecursiveBacktracking(int row, int col)
    {
        int[] directions = {1, 2, 3, 4}; // 1 - góra, 2 - dół, 3 - lewo, 4 - prawo
        Random random = new Random();

        //Shuffle directions
        for (int i = directions.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            int tmp = directions[index];
            directions[index] = directions[i];
            directions[i] = tmp;
        }

        //Random choice
        for (int dir : directions)
        {
            int newRow = row;
            int newCol = col;

            switch (dir) {
                case 1: // góra
                    newRow -= 2;
                    break;
                case 2: // dół
                    newRow += 2;
                    break;
                case 3: // lewo
                    newCol -= 2;
                    break;
                case 4: // prawo
                    newCol += 2;
                    break;
            }

            //check if new coordinates are inside maze, and not visited
            if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length && maze[newRow][newCol] == 1)
            {
                //Remove wall between current cell and new cell
                maze[newRow][newCol] = 0;
                maze[(newRow + row) / 2][(newCol + col) / 2] = 0;

                //recursion
                RecursiveBacktracking(newRow, newCol);
            }
        }
    }
}
