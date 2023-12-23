import java.util.HashMap;
public class Agent
{
    private int posX = 0;
    private int posY=0;

    private double epsilon = 0.8f; //exploration probability
    private double a = 0.1;//learning rate
    private double y = 0.9;//discount factor
    private State[][] states;

    private int timesExitFoud =0;
    private int steps =0;

    public Agent(int x, int y, int sizeX, int sizeY)
    {
        posX = x;
        posY = y;
        states= new State[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++)
        {
            for (int j = 0; j < sizeY; j++)
            {
                states[i][j] = new State();
            }
        }
    }
    public int chooseAction()
    {
        steps++;
        if (Math.random() < epsilon)
        {
            //best
            return states[posX][posY].GetBestAction();
        }
        else
        {
            //random
            return (int)(Math.random()*4);
        }
    }
    public int getPosX()
    {
        return posX;
    }
    public int getPosY()
    {
        return posY;
    }

    public void giveReward(int reward,int action, int newX, int newY)
    {
        double prevQ = states[posX][posY].qValues[action];
        double maxQ = states[newX][newY].GetBestValue();
        //Q-Learning: prevQ= Q(t-1)+a(r+ y* max(Q) - prevQ)
        states[posX][posY].qValues[action] = prevQ+a*(reward + y* maxQ - prevQ);


        posX = newX;
        posY = newY;
        if(reward == 100)
        {
            timesExitFoud++;
            System.out.println("Yay, you found exit in "+steps + "steps, in total"+timesExitFoud+" times" );
            steps=0;
        }
    }
}