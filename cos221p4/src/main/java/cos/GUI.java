
import java.io.*;
import javax.swing.*;

class GUI
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        JButton button = new JButton("Click");

        button.setBounds(15,15,200,50);

        frame.add(button);

        frame.setSize(500,600);

        frame.setLayout(null);

        frame.setVisible(true);
    }
}