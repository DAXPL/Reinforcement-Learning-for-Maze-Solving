public class Maze
{
    int size=15;
    int[][] maze = new int[size][size];

    public Maze()
    {
        for(int i=0;i<6;i++) maze[i][3]=1;
        maze[size-1][size-1] = 2;
    }

    public int GetSizeX()
    {
        return size;
    }
    public int GetSizeY()
    {
        return size;
    }
    public int GetValue(int x, int y)
    {
        return maze[x][y];
    }
}
