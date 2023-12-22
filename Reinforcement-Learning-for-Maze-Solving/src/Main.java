import java .awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;

public class Main{

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
    int wielkosc=20;
    int squareSize = 25;
    int[][] lattice = new int[wielkosc][wielkosc];  // lattice=0 => puste   lattice=1 => czastka
    boolean simulate=false;
    public Canvas()
    {
        lattice[5][10] =1;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(wielkosc*squareSize,wielkosc*squareSize);
    }
    void step()
    {
        for(int x = 0; x<wielkosc; x++)
        {
            for(int y = 0; y<wielkosc; y++)
            {

            }
        }
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        for (int i = 0; i < wielkosc; i++)
        {
            for (int j = 0; j < wielkosc; j++)
            {
                if (lattice[i][j] == 1)
                {
                    g.setColor(new Color(0,0,0));
                    g.fillRect(i*squareSize, j*squareSize, squareSize, squareSize);
                }
            }
        }

        if (simulate)
        {
            step();
            try
            {
                Thread.sleep(16);
            }
            catch (InterruptedException t){}

            repaint();
        }
    }
}