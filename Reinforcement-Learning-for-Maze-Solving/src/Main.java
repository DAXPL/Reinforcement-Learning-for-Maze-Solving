import java .awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;

public class Main
{
    public static void main(String s[])
    {
        JFrame frame = new JFrame("Reinforcement-Learning-for-Maze-Solving");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Canvas canvas = new Canvas();
        JButton button = new JButton();
        button.setText("START");
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                canvas.simulate=!canvas.simulate;
                canvas.repaint();
            }
        });

        panel.add(button);
        panel.setBackground(Color.blue);
        panel.add(canvas);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Canvas extends JPanel
{
    int timesExitFoud =0;
    int steps =0;
    int size=10;
    int squareSize = 25;
    int[][] maze = new int[size][size];
    boolean simulate=false;
    Agent agent = new Agent(0,0,size,size);
    public Canvas()
    {
        for(int i=0;i<6;i++) maze[i][3]=1;
        maze[size-1][size-1] = 2;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(size*squareSize,size*squareSize);
    }
    void step()
    {
        int xPos = agent.getPosX();
        int yPos = agent.getPosY();
        int chosenAction = agent.chooseAction();
        switch(chosenAction)
        {
            case 0:
                //left
                xPos--;
                break;
            case 1:
                //up
                yPos++;
                break;
            case 2:
                //right
                xPos++;
                break;
            case 3:
                //down
                yPos--;
                break;
            default:
                System.out.println("Something went wrong");
                break;
        }
        steps++;
        if(xPos<0 || xPos>=maze.length || yPos<0 || yPos>=maze[0].length || maze[xPos][yPos] == 1)//hit wall
        {
            agent.giveReward(-1,chosenAction,agent.getPosX(),agent.getPosY());
        }
        else if(maze[xPos][yPos]==2)//end of maze
        {
            agent.giveReward(100,chosenAction,0,0);
            timesExitFoud++;

            System.out.println("Yay, you found exit "+timesExitFoud + " times, after "+steps+" steps! ");
            steps=0;
        }
        else
        {
            agent.giveReward(0,chosenAction,xPos,yPos);
        }
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        for (int i = 0; i < maze.length; i++)
        {
            for (int j = 0; j < maze[0].length; j++)
            {
                if (maze[i][j] == 1)
                {
                    g.setColor(new Color(0,0,0));
                    g.fillRect(i*squareSize, j*squareSize, squareSize, squareSize);
                }
                if (maze[i][j] == 2)
                {
                    g.setColor(new Color(0,255,0));
                    g.fillRect(i*squareSize, j*squareSize, squareSize, squareSize);
                }
            }
        }
        g.setColor(new Color(255,0,0));
        g.fillRect(agent.getPosX()*squareSize, agent.getPosY()*squareSize, squareSize, squareSize);

        if (simulate)
        {
            step();
            /*
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException t){}
            */
            repaint();
        }
    }
}