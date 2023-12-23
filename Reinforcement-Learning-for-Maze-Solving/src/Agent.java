import java.util.HashMap;
public class Agent
{
    private int agentID;
    private int posX = 0;
    private int posY=0;

    private double epsilon = 0.8f; //exploration probability
    private double a = 0.1;//learning rate
    private double y = 0.9;//discount factor
    private State[][] states;

    private int timesExitFoud =0;
    private int steps =0;

    public Agent(int pX, int pY, int sizeX, int sizeY, int ID)
    {
        agentID = ID;
        posX = pX;
        posY = pY;
        states= new State[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++)
        {
            for (int j = 0; j < sizeY; j++)
            {
                states[i][j] = new State();
            }
        }

        epsilon = 0.75 + Math.random() * (0.75 - 0.5);
        a = 0.05 + Math.random() * (0.5 - 0.05);
        y = 0.5 + Math.random() * (0.99 - 0.5 );
    }
    public String GetAgentData()
    {
        return agentID+";"+epsilon+";"+a+";"+y;
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
    }
    public String ReportSuccess()
    {
        this.timesExitFoud++;
        String raport = agentID+";"+timesExitFoud+";"+steps;
        System.out.println("Yay, agent "+agentID+" found exit in "+steps + "steps, in total "+timesExitFoud+" times" );
        this.steps=0;
        return raport;
    }
}