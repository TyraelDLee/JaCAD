package defaultPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**************************************************************************
 *                                                                        *
 *                      GUI component by Yanlong LI                       *
 *               MenuButton, with an amazing animation effect.            *
 *                            swing ver.   Stage 1                        *
 *                    Copyright (c) 2020 Yanlong LI                       *
 *                       @author Yanlong LI u5890571                      *
 *                            @version 1.0                                *
 **************************************************************************/
public class MenuButton extends JComponent{
    private int height = 12;
    private int width = 20;

    private Graphics2D render;

    private int clickCount = 0;

    private static Timer timer;
    private static final int DELAY = 25;
    private int disappear = 0;
    private int x = 0;
    private double y = 0, y1 = 0;
    private double degree = 0;
    private boolean state = true;
    private boolean animFinish = true;


    /**
     * This constructor set a new button with default height, width.
     * With the basic button style. The default background color is gray,
     * {@code new Color(.0, .0, .0, 1)}.
     */
    MenuButton(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width,height));
    }

    /**
     * Style setting
     * Set a new display color for button.
     * This method will override the default color which
     * defined in constructors. This method also may called
     * in listener for change color dynamically.
     * <p>
     * The color value rage is 0.0 ~ 1.0
     *
     * @param r the red channel for bar color
     * @param g the green channel for bar color
     * @param b the blue channel for bar color
     * @param a the alpha channel for bar color
     */
    void setColor(int r, int g, int b, int a) {
        Color color = new Color(r, g, b, a);
        this.render.setColor(color);
    }

    void setTheme(String style) {
        if (style.equals("DARK")) {
            this.render.setColor(Color.WHITE);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        int width = this.width, height = this.height;
        render = (Graphics2D) graphics.create();
        render.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        render.setColor(Color.WHITE);
        render.fillRect(0,0,this.getEdge(),this.getEdge());
        render.setColor(new Color(73, 73, 73, 255));
        render.rotate(y, (double) height/2, (double) width/2+1);
        render.fillRect(0, 0 + x, width, 2);//top
        render.rotate(y*2,(double) height/2+1, (double) width/2-2);
        render.fillRect(0, height - 2 - x, width, 2);// bottom
        render.setColor(new Color(73, 73, 73, 255 - disappear));
        render.fillRect(0, height / 2 - 1, width, 2);// middle
        render.dispose();
    }

    /**
     * Declare the rebound listener for animation.
     * */
    private class ReboundListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (clickCount == 1) {
                if(x < height/2){
                    x++;
                    disappear+=25;
                    if(disappear>255) disappear = 255;
                }else{
                    degree+=5;
                    y=deg2rad(degree);
                    y1 = deg2rad(360-degree);
                }
                if(degree>=45) {
                    timer.stop();
                    animFinish = true;
                }
            }//menu -> close
            else {
                if(degree>0){
                    degree-=5;
                    y=deg2rad(degree);
                    y1 = deg2rad(360-degree);
                }else{
                    x--;
                    disappear-=25;
                    if(disappear<0) disappear = 0;
                }
                if(x<=0) {
                    timer.stop();
                    animFinish = true;
                }
            }//close -> menu
            repaint();
        }
    }

    public void onclick() {
        if (clickCount == 0) clickCount++;
        else clickCount--;

        if (animFinish){
            animFinish = false;
            timer = new Timer(DELAY, new ReboundListener());
            timer.start();
        }
        this.state = clickCount == 1;
    }

    public void closeTransform(){
        this.clickCount = 0;
        System.out.println(this.clickCount);
        onclick();
    }

    public void menuTransform(){
        this.clickCount = 1;
        onclick();
    }

    /**
     * Convert the degree to radians.
     * For the click animation.
     * */
    private double deg2rad(double degree){
        return degree * (Math.PI/180);
    }

    /**
     * find the max edge for this button.
     * */
    public int getEdge(){
        return Math.max(this.height, this.width);
    }

    /**
     * Return the button state.
     * true -> show the menu.
     * false -> hide the menu.
     * */
    public boolean getState(){
        return this.state;
    }
}
