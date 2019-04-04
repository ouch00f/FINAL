package sample;

import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {

    public Menu(){
        setLayout(new BorderLayout());
        add(new JLabel("This test", JLabel.CENTER), BorderLayout.CENTER);
        JButton switchBtn = new JButton("Play");
        switchBtn.addActionListener(e-> System.exit(0));
        add(switchBtn, BorderLayout.SOUTH);
        setSize(600,600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
