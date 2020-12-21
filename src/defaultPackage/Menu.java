package defaultPackage;

import javax.swing.*;
import java.awt.*;

/**
 * This class declared entire menu.
 * Include menu items, menu panes.
 * Menu has 2 layer pages, 4 different pages.
 * Main page - Draw page
 *           - Advance page
 *           Setting page
 *
 * @author Yanlong LI, u5890571
 */
public class Menu extends JPanel implements MenuObs {
    private int width = 0;
    private int height = 0;
    private String currentPage = "";
    private MenuPane mainMenu;

    // 3 layers menu.
    private MenuPane drawMenu;
    private MenuPane advanceMenu;
    private MenuPane settingMenu;

    // Set the blank item for looking nice.
    private MenuItem blank0 = new MenuItem(150,30);
    private MenuItem blank1 = new MenuItem(150,30);

    // set all menu item in menus.
    private MenuItem returnItem1 = new MenuItem(150, 50, "Return", "return");
    private MenuItem returnItem2 = new MenuItem(150, 50, "Return", "return");
    private MenuItem returnItem3 = new MenuItem(150,50,"Return", "return");

    private MenuItem drawItem = new MenuItem(150, 50, "Draw", "draw");

    public MenuItem edit = new MenuItem(150, 50, "Edit", "EDIT");
    public MenuItem lineItem = new MenuItem(150, 50, "Line", "LINE");
    public MenuItem arcItem = new MenuItem(150, 50, "Arc", "ARC");
    public MenuItem triangleItem = new MenuItem(150, 50, "Triangle", "TRIANGLE");
    public MenuItem rectangleItem = new MenuItem(150, 50, "Rectangle", "RECTANGLE");
    public MenuItem squareItem = new MenuItem(150, 50, "Square", "SQUARE");
    public MenuItem polyItem = new MenuItem(150,50,"Polygon", "POLY");
    public MenuItem circleItem = new MenuItem(150, 50, "Circle", "CIRCLE");

    private MenuItem advItem = new MenuItem(150,50,"Advance", "advance");

    public MenuItem edit2 = new MenuItem(150, 50, "Edit", "EDIT");
    public MenuItem setDescribe = new MenuItem(150,50,"Set Label", "DESCRIBE");
    public MenuItem showLabel = new MenuItem(150,50,"Show Label", "LABEL");
    public MenuItem setColour = new MenuItem(150,50,"Color", "COLOR");
    public MenuItem remove = new MenuItem(150,50,"Remove", "REMOVE");
    public MenuItem clear = new MenuItem(150,50,"Clear", "RMALL");
    public MenuItem showMeasure = new MenuItem(150,50,"Show Measure", "SHOWMEAS");
    public MenuItem group = new MenuItem(150,50,"Merge", "group");
    public MenuItem ungroup = new MenuItem(150,50,"Unmerge", "ungroup");

    public MenuItem save = new MenuItem(150,50,"Save", "Save");
    public MenuItem load = new MenuItem(150,50,"Load", "Load");

    private MenuItem settingItem = new MenuItem(150, 50, "Setting", "setting");

    public MenuItem gridItem = new MenuItem(150, 50, "Standard", "Standard");
    public MenuItem simpleItem = new MenuItem(150, 50, "Simple", "Simple");
    public MenuItem emptyItem = new MenuItem(150, 50, "Empty", "Empty");
    public MenuItem colorItem = new MenuItem(150, 50, "Color Setting", "Color");
    private MenuItem blank2 = new MenuItem(150,30);
    public MenuItem unitMM = new MenuItem(150,50,"unit: mm", CoordPanel.mm);
    public MenuItem unitCM = new MenuItem(150,50,"unit: cm", CoordPanel.cm);
    public MenuItem unitDM = new MenuItem(150,50,"unit: dm", CoordPanel.dm);
    public MenuItem unitM = new MenuItem(150,50,"unti: m ", CoordPanel.m);

    Menu(int width, int height) {
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        init();
        initMenu();
    }

    private void init() {
        this.mainMenu = new MenuPane(150, this.height);
        this.drawMenu = new MenuPane(150, this.height);
        this.settingMenu = new MenuPane(150, this.height);
        this.advanceMenu = new MenuPane(150, this.height);
        this.add(this.mainMenu);
        this.add(this.drawMenu);
        this.add(this.advanceMenu);
        this.add(this.settingMenu);
        this.mainMenu.setBounds(0, 0, 150, this.height);
        this.drawMenu.setBounds(-150, 0, 150, this.height);
        this.advanceMenu.setBounds(-150,0,150,this.height);
        this.settingMenu.setBounds(-150, 0, 150, this.height);
    }

    /**
     * Listen the menu item chages from user.
     * By using observer method.
     * */
    @Override
    public void update(String command) {
        this.currentPage = command;
        setPage();
        this.repaint();
    }

    /**
     * This method will according to user to
     * show menu page.
     * */
    private void setPage(){
        if (this.currentPage.equals("draw")) {
            this.mainMenu.setBounds(-150, 0, 150, this.height);
            this.drawMenu.setBounds(0, 0, 150, this.height);
            this.advanceMenu.setBounds(-150,0,150,this.height);
            this.settingMenu.setBounds(-150, 0, 150, this.height);
        }
        if (this.currentPage.equals("advance")) {
            this.mainMenu.setBounds(-150, 0, 150, this.height);
            this.drawMenu.setBounds(-150, 0, 150, this.height);
            this.advanceMenu.setBounds(0,0,150,this.height);
            this.settingMenu.setBounds(-150, 0, 150, this.height);
        }
        if (this.currentPage.equals("setting")) {
            this.mainMenu.setBounds(-150, 0, 150, this.height);
            this.drawMenu.setBounds(-150, 0, 150, this.height);
            this.advanceMenu.setBounds(-150,0,150,this.height);
            this.settingMenu.setBounds(0, 0, 150, this.height);
        }
        if (this.currentPage.equals("return")) {
            this.mainMenu.setBounds(0, 0, 150, this.height);
            this.drawMenu.setBounds(-150, 0, 150, this.height);
            this.advanceMenu.setBounds(-150,0,150,this.height);
            this.settingMenu.setBounds(-150, 0, 150, this.height);
        }

    }

    /**
     * Resize the Menu object.
     * Co-work with the resize listener from MainWindow.
     *
     * @param height, the new menu height.
     * @param width, the new menu width.
     * */
    public void resizeComponent(int width, int height) {
        this.height = height;
        this.setPreferredSize(new Dimension(150, this.height));
        this.setBounds(width,50,150,this.height);
        setPage();
        this.mainMenu.resizeComponent(height);
        this.advanceMenu.resizeComponent(height);
        this.drawMenu.resizeComponent(height);
        this.settingMenu.resizeComponent(height);
        this.setPage();
        this.repaint();
    }

    // add items into correct layer.
    private void initMenu() {
        mainMenu.addMenuItems(drawItem);
        mainMenu.addMenuItems(advItem);
        mainMenu.addMenuItems(save);
        mainMenu.addMenuItems(load);
        mainMenu.addMenuItems(blank0);
        mainMenu.addMenuItems(settingItem);

        drawMenu.addMenuItems(returnItem1);
        drawMenu.addMenuItems(edit);
        drawMenu.addMenuItems(lineItem);
        drawMenu.addMenuItems(arcItem);
        drawMenu.addMenuItems(triangleItem);
        drawMenu.addMenuItems(rectangleItem);
        drawMenu.addMenuItems(squareItem);
        drawMenu.addMenuItems(polyItem);
        drawMenu.addMenuItems(circleItem);


        advanceMenu.addMenuItems(returnItem3);
        advanceMenu.addMenuItems(edit2);
        advanceMenu.addMenuItems(showMeasure);
        advanceMenu.addMenuItems(setDescribe);
        advanceMenu.addMenuItems(showLabel);
        advanceMenu.addMenuItems(setColour);
        advanceMenu.addMenuItems(group);
        advanceMenu.addMenuItems(ungroup);
        advanceMenu.addMenuItems(remove);
        advanceMenu.addMenuItems(blank1);
        advanceMenu.addMenuItems(clear);

        settingMenu.addMenuItems(returnItem2);
        settingMenu.addMenuItems(gridItem);
        settingMenu.addMenuItems(simpleItem);
        settingMenu.addMenuItems(emptyItem);
        settingMenu.addMenuItems(colorItem);
        settingMenu.addMenuItems(blank2);
        settingMenu.addMenuItems(unitMM);
        settingMenu.addMenuItems(unitCM);
        settingMenu.addMenuItems(unitDM);
        settingMenu.addMenuItems(unitM);

        // register the observer for change layers.
        returnItem1.reg(this);
        returnItem2.reg(this);
        returnItem3.reg(this);
        drawItem.reg(this);
        advItem.reg(this);
        settingItem.reg(this);
    }

}
