import java .awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main
{
    public static void main(String s[])
    {
        JFrame frame = new JFrame("Reinforcement-Learning-for-Maze-Solving");
        frame.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("carrot.png"));
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

        JButton button2 = new JButton();
        button2.setText("Slowmotion");
        button2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                canvas.ToggleSlowmotion();
            }
        });

        panel.add(button2);

        panel.setBackground(new Color(59,122,87));
        panel.add(canvas);
        frame.add(panel);
        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Canvas extends JPanel
{
    int squareSize = 35;
    boolean simulate=false;
    boolean logValues = false;
    boolean slowmotion = false;

    Maze maze = new Maze(25);
    Agent[] agents = new Agent[10];

    FileWriter outFile;
    PrintWriter out;

    private BufferedImage carrotImage;
    private BufferedImage[] kicajceImages = new BufferedImage[4];
    Color bgColor = new Color(59,122,87);
    public Canvas()
    {
        try
        {
            carrotImage = ImageIO.read(new File("carrot.png"));
            kicajceImages[0] = ImageIO.read(new File("BabyKicajec0.png"));
            kicajceImages[1] = ImageIO.read(new File("BabyKicajec1.png"));
            kicajceImages[2] = ImageIO.read(new File("BabyKicajec2.png"));
            kicajceImages[3] = ImageIO.read(new File("BabyKicajec3.png"));
        } catch (IOException ex) { }

        try
        {
            outFile = new FileWriter("AgentData.txt");
            out = new PrintWriter(outFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for(int i=0;i<agents.length;i++)
        {
            agents[i] = new Agent(0,0,maze.GetSizeX(),maze.GetSizeY(),i);
            System.out.println(agents[i].GetAgentData());
            if(logValues)out.println(agents[i].GetAgentData());
        }
        out.close();

        try
        {
            outFile = new FileWriter("data.txt");
            out = new PrintWriter(outFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
            if(logValues)out.println(a.ReportSuccess());
            else a.ReportSuccess();
        }
        else
        {
            a.giveReward(0,chosenAction,xPos,yPos);
        }
    }

    public void ToggleSlowmotion()
    {
        slowmotion =!slowmotion;
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        for (int i = 0; i < maze.GetSizeX(); i++)
        {
            for (int j = 0; j < maze.GetSizeY(); j++)
            {
                if (maze.GetValue(i,j) == 1)
                {
                    g.setColor(bgColor);
                    g.fillRect(i*squareSize, j*squareSize, squareSize, squareSize);
                }
                else if (maze.GetValue(i,j) == 2)
                {
                    g.drawImage(carrotImage, i*squareSize, j*squareSize,squareSize, squareSize,this);
                }
            }
        }
        g.setColor(new Color(255,0,0));

        for(int i=0;i<agents.length;i++)
        {
            g.drawImage(kicajceImages[i%4], agents[i].getPosX()*squareSize, agents[i].getPosY()*squareSize,squareSize, squareSize,this);
        }
        if (simulate)
        {
            for(int i=0;i<agents.length;i++)
            {
                step(agents[i]);
            }

            if(slowmotion)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException t){}
            }

            repaint();
        }
    }
}