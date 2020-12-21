package defaultPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**************************************************************************
 *                                                                        *
 *                      GUI component by Yanlong LI                       *
 *    MenuItem, inherited from JComponent. Define the menu item with      *
 *    mouse listener. Interactive component, include hover and click.     *
 *                    Copyright (c) 2020 Yanlong LI                       *
 *                       @author Yanlong LI u5890571                      *
 *                            @version 1.0                                *
 **************************************************************************/
public class MenuItem extends JComponent implements MouseListener {
    private int width = 150;
    private int height = 0;
    private String content = "type in menu text.";
    private int fontSize = 16;
    private Color onClick = Color.WHITE;
    private ArrayList<MenuObs> menuObs = new ArrayList<>();
    private String command = "null";

    MenuItem(int width, int height){
        this.resizeComponent(width, height);
        this.content = "";
    }

    MenuItem(int width, int height, String content){
        this.content = content;
        this.resizeComponent(width, height);
        this.addMouseListener(this);
    }

    MenuItem(int width, int height, String content, String command){
        this.content = content;
        this.command = command;
        this.resizeComponent(width, height);
        this.addMouseListener(this);
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setFontSize(int fontSize){
        this.fontSize = fontSize;
    }

    public void resizeComponent(int width, int height){
        this.width = width;
        this.height = height;
        this.setBounds(0,0, width,height);
        this.setPreferredSize(new Dimension(this.width, this.height));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // set display font in items. And also keep text in middle.
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        FontMetrics fm = g.getFontMetrics(font);
        int textWidth = fm.stringWidth(content);
        int fontX = (this.width - textWidth) / 2;
        g.setFont(font);
        g.setColor(this.onClick);
        g.fillRect(0,0,this.width,this.height);
        g.setColor(Color.BLACK);
        g.drawString(content, fontX, (this.height+this.fontSize)/2);
    }

    /**
     * Change color which mouse over it.
     * */
    private void over(boolean in){
        if(in)
            this.onClick = Color.GRAY;
        else
            this.onClick = Color.WHITE;
        repaint();
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    /**
     * Update the object state
     * When clicked.
     * */
    @Override
    public void mouseClicked(MouseEvent e) {
        this.upd(this.command);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.over(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.over(false);
    }

    /**
     * The observer mode function
     * */
    public void reg(MenuObs o) {
        this.menuObs.add(o);
    }

    /**
     * Update the command respond the structure and command:
     * */
    public void upd(String command) {
        for (MenuObs o : this.menuObs) o.update(command);
    }
}
