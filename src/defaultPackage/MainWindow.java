package defaultPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The main window for entire app.
 * Implement based on swing.
 *
 * @author Yanlong LI, u5890571
 */
public class MainWindow {
    // The default window size.
    private static final int WINDOW_HEIGHT = 420;
    private static final int WINDOW_WIDTH = 720;

    private JFrame mainWindow = new JFrame("JaCAD");
    private MenuButton menuButton = new MenuButton(20, 16);
    public CoordPanel background;
    private boolean buttonControl = false;
    private boolean resizeSmall = true, resizeBig = false;
    private int showMenu = -150;

    /* Buttons and Menus*/
    public Menu menu = new Menu(150, WINDOW_HEIGHT);

    ElementsFactory drawBoard;
    public Draw draw;

    public static void main(String[] args) {
        //Main window define, set propriety
        MainWindow mainWindow = new MainWindow();
        mainWindow.init();
    }

    /**
     * initial the app page.
     * bounds resized listener to make all components
     * state changed with resize window.
     * */
    public void init() {
        draw = new Draw(drawBoard);
        drawBoard = new LowLevelElements();

        background = new CoordPanel(WINDOW_WIDTH, WINDOW_HEIGHT, this, drawBoard);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setMinimumSize(new Dimension(720, 420));
        mainWindow.setLayout(null);
        mainWindow.setContentPane(background);
        resize();
        //main window resize listener
        mainWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });
        // set the menu button listener.
        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuButton.onclick();
                buttonControl = true;
                if(!menuButton.getState()){
                    menu.setBounds(-150, 50, 150, mainWindow.getHeight() - 50);
                    showMenu = -150;
                }
                else{
                    menu.setBounds(0, 50, 150, mainWindow.getHeight() - 50);
                    showMenu = 0;
                }
            }
        });
        mainWindow.getContentPane().add(menu);
        mainWindow.add(menuButton);
        mainWindow.setVisible(true);
    }

    /**
     * Resize all components which need to be resized.
     * included main window, menu, and draw board.
     * */
    private void resize() {
        background.resizeComponent(mainWindow.getWidth(), mainWindow.getHeight());
        menu.resizeComponent(showMenu, mainWindow.getHeight() - 50);
        menuButton.setBounds(30, 25, menuButton.getEdge(), menuButton.getEdge());
        background.setBounds(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
        // set the menu auto appear when window width >= 1280 and auto close when < 1280.
        if(mainWindow.getWidth() >= 1280 && resizeSmall){
            this.buttonControl = false;
        }
        if(mainWindow.getWidth() < 1280 && resizeBig)
            this.buttonControl = false;
        if (!buttonControl && mainWindow.getWidth() >= 1280 && resizeSmall) {
            resizeSmall = false;
            resizeBig = true;
            menuButton.closeTransform();
            menu.setBounds(0, 50, 150, mainWindow.getHeight() - 50);
            showMenu = 0;
        }
        if (!buttonControl && mainWindow.getWidth() < 1280 && resizeBig) {
            resizeSmall = true;
            resizeBig = false;
            menuButton.menuTransform();
            menu.setBounds(-150, 50, 150, mainWindow.getHeight() - 50);
            showMenu = -150;
        }
    }
}
