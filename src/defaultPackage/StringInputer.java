package defaultPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This is the sub-application for the JaCAD app.
 * This app contain the text input features.
 * For helping user set the label for each element.
 *
 * @author Yanlong LI, u5890571.
 * */
public class StringInputer extends JFrame implements Runnable {
    private static final int WINDOW_HEIGHT = 150;
    private static final int WINDOW_WIDTH = 460;
    private String label = "";
    private JTextArea input = new JTextArea();
    private JButton clear = new JButton("Clear");
    private JButton done = new JButton("Done");

    public static void main(String[] args) {
        StringInputer si = new StringInputer();
        si.run();
    }

    @Override
    public void run() {
        this.setTitle("Set Label");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);
        this.setBackground(Color.WHITE);

        this.input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                label+=e.getKeyChar();
            }
        });
        this.clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.setText("");
                label = "";
            }
        });

        this.done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        this.input.setBounds(10,10,WINDOW_WIDTH-20,75);
        this.clear.setPreferredSize(new Dimension(80,30));
        this.done.setPreferredSize(new Dimension(80,30));
        this.clear.setBounds(WINDOW_WIDTH-170, 90,80,30);
        this.done.setBounds(WINDOW_WIDTH-90, 90,80,30);
        this.getContentPane().add(input);
        this.getContentPane().add(clear);
        this.getContentPane().add(done);
        this.setVisible(true);
    }

    /**
     * Return the label text when this app closed.
     * */
    public String getLabel(){
        return this.label;
    }
}
