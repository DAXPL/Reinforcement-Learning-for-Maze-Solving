import java .awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
        int width = 1000;
        int height = 1000;

        int defaultAgentsCount = 10;
        int defaultMazeSize = 10;

        JFrame frame = new JFrame("Reinforcement-Learning-for-Maze-Solving");
        frame.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("carrot.png"));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JSlider speedSlider  = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        JLabel labelSpeedSlider = new JLabel("Delay = " + speedSlider.getValue() + "ms");

        JSlider agentsSlider  = new JSlider(JSlider.HORIZONTAL, 1, 50, defaultAgentsCount);
        JLabel labelAgents = new JLabel("Agents = " + agentsSlider.getValue());

        JSlider mazeSlider  = new JSlider(JSlider.HORIZONTAL, 5, 100, defaultMazeSize);
        JLabel labelMaze = new JLabel("Maze size = " + mazeSlider.getValue());

        Canvas canvas = new Canvas(width,height,agentsSlider.getValue(),mazeSlider.getValue());

        JButton startButton = new JButton();
        startButton.setText("START");

        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                canvas.simulate=!canvas.simulate;
                agentsSlider.setEnabled(!canvas.simulate);
                mazeSlider.setEnabled(!canvas.simulate);
                canvas.repaint();
            }
        });

        speedSlider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                labelSpeedSlider.setText("Delay = " + speedSlider.getValue() + "ms");
                canvas.SetSimulationSpeed(speedSlider.getValue());
            }
        });

        agentsSlider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                labelAgents.setText("Agents = " + agentsSlider.getValue());
                canvas.SetSimulationParams(mazeSlider.getValue(), agentsSlider.getValue(), width);
            }
        });

        mazeSlider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                labelMaze.setText("Maze size = " + mazeSlider.getValue());
                canvas.SetSimulationParams(mazeSlider.getValue(), agentsSlider.getValue(), width);
            }
        });

        panel.add(startButton);
        panel.add(speedSlider);
        panel.add(labelSpeedSlider);

        panel.add(speedSlider);
        panel.add(labelSpeedSlider);

        panel.add(agentsSlider);
        panel.add(labelAgents);

        panel.add(mazeSlider);
        panel.add(labelMaze);

        panel.setBackground(new Color(59,122,87));
        panel.add(canvas);
        frame.add(panel);
        frame.setSize(width, height);
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
    int simulationSpeed = 0;

    Maze maze;
    Agent[] agents;

    FileWriter outFile;
    PrintWriter out;

    private BufferedImage carrotImage;
    private BufferedImage[] kicajceImages = new BufferedImage[4];
    Color bgColor = new Color(59,122,87);
    public Canvas(int w, int h, int agentsCout, int newMazeSize)
    {
        try
        {
            carrotImage = ImageIO.read(new File("gfx/carrot.png"));
            kicajceImages[0] = ImageIO.read(new File("gfx/BabyKicajec0.png"));
            kicajceImages[1] = ImageIO.read(new File("gfx/BabyKicajec1.png"));
            kicajceImages[2] = ImageIO.read(new File("gfx/BabyKicajec2.png"));
            kicajceImages[3] = ImageIO.read(new File("gfx/BabyKicajec3.png"));
        } catch (IOException ex) { }

        SetSimulationParams(newMazeSize,agentsCout, w);
    }

    public void SetSimulationParams(int mazeSize, int agentsAmount, int w)
    {
        maze = new Maze(mazeSize);
        agents = new Agent[agentsAmount];

        squareSize = (int)(w/(mazeSize*1.1f));
        System.out.println(squareSize);

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
        if(maze == null) new Dimension(squareSize,squareSize);
        assert maze != null;
        return new Dimension(maze.GetSizeX()*squareSize,maze.GetSizeY()*squareSize);
    }
    void step(Agent a)
    {
        if(maze == null) return;

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

    public void SetSimulationSpeed(int v)
    {
        simulationSpeed = v;
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        if(maze == null) return;
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

        if(agents == null) return;
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

            if(simulationSpeed > 0)
            {
                try
                {
                    Thread.sleep(simulationSpeed);
                }
                catch (InterruptedException t){}
            }


            repaint();
        }
    }
}