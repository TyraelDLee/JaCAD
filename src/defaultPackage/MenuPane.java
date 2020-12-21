package defaultPackage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**************************************************************************
 *                                                                        *
 *                      GUI component by Yanlong LI                       *
 *    MenuPane, inherited from JScrollPane. A container for all menu      *
 *                  item. And also multi-layer menu.                      *
 *                    Copyright (c) 2020 Yanlong LI                       *
 *                       @author Yanlong LI u5890571                      *
 *                            @version 1.0                                *
 **************************************************************************/
public class MenuPane extends JScrollPane {
    private int width = 150;
    private int height = 0;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();


    private static final int menuHeight = 50;


    MenuPane(int width, int height){
        this.width = width;
        this.height = height;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(width, height));
        this.render();
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setBackground(Color.WHITE);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }


    public void setMenuItems(String[] menuItem){
        for(String item : menuItem)
            this.menuItems.add(new MenuItem(this.width, menuHeight,item));
        this.render();
    }

    void addMenuItems(MenuItem menuItem){
        this.menuItems.add(menuItem);
        this.render();
    }

    /**
     * Set the panel for menu items.
     * */
    private void render(){
        JPanel host = new JPanel();
        host.setBackground(Color.WHITE);
        host.setLayout(null);
        int y = 0;
        for(MenuItem menuItem : this.menuItems){
            menuItem.setBounds(0,y,this.getWidth(),menuHeight);
            host.add(menuItem);
            y+=menuItem.getHeight();
        }
        host.setPreferredSize(new Dimension(150,y+50));
        this.setViewportView(host);
    }

    void resizeComponent(int height){
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
    }
}
