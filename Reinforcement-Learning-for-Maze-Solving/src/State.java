public class State
{
    public double [] qValues = {0,0,0,0};

    public int GetBestAction()
    {
        int bestAction = 0;
        double bestValue = -100;

        for(int i=0; i<qValues.length; i++)
        {
            if(qValues[i]>bestValue)
            {
                bestAction=i;
                bestValue=qValues[i];
            }
            else if (qValues[i] == bestValue)
            {
                if(Math.random() > 0.5f)
                {
                    bestAction=i;
                    bestValue=qValues[i];
                }
            }
        }

        return  bestAction;
    }
    public double GetBestValue()
    {
        double bestValue = -100;

        for(int i=0; i<qValues.length; i++)
        {
            if(qValues[i]>bestValue)
            {
                bestValue=qValues[i];
            }
        }

        return  bestValue;
    }
}
