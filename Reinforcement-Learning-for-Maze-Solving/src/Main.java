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
        panel.setBackground(Color.black);
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
    int squareSize = 30;
    boolean simulate=false;

    Maze maze = new Maze(35);
    Agent[] agents = new Agent[1];

    public Canvas()
    {
        for(int i=0;i<agents.length;i++)agents[i] = new Agent(0,0,maze.GetSizeX(),maze.GetSizeY());
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(maze.GetSizeX()*squareSize,maze.GetSizeY()*squareSize);
    }
    void step(Agent a)
    {
        int xPos = a.getPosX();
        int yPos = a.getPosY();
        int chosenAction = a.chooseAction();
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

        if(xPos<0 || xPos>=maze.GetSizeX() || yPos<0 || yPos>=maze.GetSizeY() || maze.GetValue(xPos,yPos) == 1)//hit wall
        {
            a.giveReward(-1,chosenAction,a.getPosX(),a.getPosY());
        }
        else if(maze.GetValue(xPos,yPos)==2)//end of maze
        {
            a.giveReward(100,chosenAction,0,0);
        }
        else
        {
            a.giveReward(0,chosenAction,xPos,yPos);
        }
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        for (int i = 0; i < maze.GetSizeX(); i++)
        {
            for (int j = 0; j < maze.GetSizeY(); j++)
            {
                if (maze.GetValue(i,j) == 1)
                {
                    g.setColor(new Color(0,0,0));
                    g.fillRect(i*squareSize, j*squareSize, squareSize, squareSize);
                }
                else if (maze.GetValue(i,j) == 2)
                {
                    g.setColor(new Color(0,255,0));
                    g.fillRect(i*squareSize, j*squareSize, squareSize, squareSize);
                }
            }
        }
        g.setColor(new Color(255,0,0));

        for(int i=0;i<agents.length;i++)
        {
            g.fillRect(agents[i].getPosX()*squareSize, agents[i].getPosY()*squareSize, squareSize, squareSize);
        }

        if (simulate)
        {
            for(int i=0;i<agents.length;i++)
            {
                step(agents[i]);
            }

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