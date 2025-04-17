package cos;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class GUI
{
    private JFrame frame;
    public GUI()
    {
        this.frame = new JFrame();
        JButton button = new JButton("Click");

        button.setBounds(15,15,200,50);

        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });



        this.frame.add(button);

        this.frame.setSize(500,600);

        this.frame.setLayout(null);

        this.frame.setUndecorated(true);
    }

    public GUI(String var)
    {

    }

    private void close()
    {
        this.frame.setVisible(false);
        this.frame.dispose();
    }

    public void display()
    {
        this.frame.setVisible(true);
    }

    public void addButton(String name, int function)
    {
        switch(function)
        {
            case 0:
            break;
            case 1:
            break;
            default:
            break;
        }
    }

    


}