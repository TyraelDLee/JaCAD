package defaultPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This is the sub-application for the JaCAD app.
 * This app contain the color choose features.
 * Color schema used HSB and RGB.
 *
 * @author Yanlong LI, u5890571.
 * */
public class ColourPicker extends JFrame implements Runnable {
    private static final int WINDOW_HEIGHT = 460;
    private static final int WINDOW_WIDTH = 460;

    private Color outputColor = Color.RED;
    private ColorBar colorBar = new ColorBar();
    private ColorWindow colorWindow = new ColorWindow();
    private Preview preview = new Preview();

    private ArrayList<ColorBtn> buttons = new ArrayList<>();
    private JButton done = new JButton("Done");

    public static void main(String[] args) {
        ColourPicker cp = new ColourPicker();
        cp.run();
    }

    @Override
    public void run() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //some pre-set color for choose.
        buttons.add(new ColorBtn(new Color(255, 64, 44), 0));
        buttons.add(new ColorBtn(new Color(235, 19, 96), 1));
        buttons.add(new ColorBtn(new Color(156, 26, 177), 2));
        buttons.add(new ColorBtn(new Color(87, 43, 157), 3));
        buttons.add(new ColorBtn(new Color(61, 77, 183), 4));
        buttons.add(new ColorBtn(new Color(70, 175, 74), 5));
        buttons.add(new ColorBtn(new Color(0, 150, 135), 6));
        buttons.add(new ColorBtn(new Color(4, 186, 213), 7));
        buttons.add(new ColorBtn(new Color(0, 167, 246), 8));
        buttons.add(new ColorBtn(new Color(16, 147, 245), 9));
        buttons.add(new ColorBtn(new Color(135, 196, 64), 10));
        buttons.add(new ColorBtn(new Color(205, 221, 30), 11));
        buttons.add(new ColorBtn(new Color(255, 236, 23), 12));
        buttons.add(new ColorBtn(new Color(255, 193, 0), 13));
        buttons.add(new ColorBtn(new Color(255, 152, 0), 14));
        buttons.add(new ColorBtn(new Color(0, 0, 0), 15));
        buttons.add(new ColorBtn(new Color(94, 123, 138), 16));
        buttons.add(new ColorBtn(new Color(157, 157, 157), 17));
        buttons.add(new ColorBtn(new Color(122, 84, 71), 18));
        buttons.add(new ColorBtn(new Color(255, 85, 6), 19));
        this.setTitle("Colour Picker");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        //declare the color bar for range between hue 0 - 360.
        colorBar.setBounds(50, 50, 25, 360);

        colorBar.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                outputColor = colorBar.listener(e.getPoint());
                colorWindow.update(outputColor);
                preview.update(outputColor);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        //declare the color window for range between hue from color bar, 0-100(S), 0-100(B)
        colorWindow.setBounds(100, 50, 200, 200);
        colorWindow.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                outputColor = colorWindow.listener(e.getPoint());
                preview.update(outputColor);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        preview.setBounds(325, 50, 100, 100);
        this.getContentPane().add(colorBar);
        this.getContentPane().add(colorWindow);
        this.getContentPane().add(preview);
        // set the pre-set color button, in 5 x 4.
        for (int i = 0; i < 20; i++) {
            ColorBtn current = this.buttons.get(i);
            current.setBounds(105 + i%5 * 40, 260+ i/5*37, 35, 35);
            final int currentID = current.getId();
            current.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    outputColor = current.getColor();
                    preview.update(outputColor);
                    colorBar.update(outputColor);
                    colorWindow.update(outputColor);
                    for(ColorBtn btn : buttons){
                        if(btn.getId()!=currentID)btn.setUnselect();;
                    }
                    repaint();
                }
            });
            this.getContentPane().add(current);
        }
        this.done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        this.done.setBounds(370,380,80,30);
        this.done.setPreferredSize(new Dimension(60,35));
        this.getContentPane().add(done);
        this.setVisible(true);
    }

    /**
     * This color bar is the using HSB color schema.
     * range from hue 0 - 360.
     * */
    private class ColorBar extends JComponent {
        private final int width = 25;
        private Line2D index = new Line2D.Double(new Point2D.Double(0, 0), new Point2D.Double(25, 0));
        private ArrayList<Color> colors = new ArrayList<>();

        private ColorBar() {
            for (int i = 0; i <= 360; i++) {
                colors.add(new Color(Color.HSBtoRGB(i / 360.0f, 100 / 100f, 100 / 100f)));
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            int x = 0, y = 0;
            for (int i = 0; i <= 360; i++) {
                g.setColor(new Color(Color.HSBtoRGB(i / 360.0f, 100 / 100f, 100 / 100f)));
                g.fillRect(x, y += 1, width, 1);
            }
            g.setColor(Color.GRAY);
            g.draw(this.index);
        }

        protected Color listener(Point2D point2D) {
            if (point2D.getY() <= 0) point2D = new Point2D.Double(point2D.getX(), 0);
            if (point2D.getY() >= 360) point2D = new Point2D.Double(point2D.getX(), 360);
            index = new Line2D.Double(new Point2D.Double(0, point2D.getY()), new Point2D.Double(25, point2D.getY()));
            this.repaint();
            return this.colors.get((int) point2D.getY());
        }

        protected void update(Color color){
            float[] HSB = convertHSB(color);
            index = new Line2D.Double(new Point2D.Double(0, HSB[0]*360), new Point2D.Double(25, HSB[0]*360));
            this.repaint();
        }
    }

    /**
     * This window is co-working with the color bar above, take the hue from color bar.
     * Then show the all color between hue, 0 - 100(S), 0 - 100(B)*/
    private class ColorWindow extends JComponent {
        private ArrayList<ArrayList<Color>> colors = new ArrayList<>();
        private Color color = Color.RED;
        private Ellipse2D index1 = new Ellipse2D.Double(-2, -2, 6, 6);
        private Ellipse2D index2 = new Ellipse2D.Double(-1, -1, 4, 4);

        ColorWindow() {
            update(this.color);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            int s = 100;
            for (int i = 0; i < 200; i += 2) {
                int b = 100;
                for (int j = 0; j < 200; j += 2) {
                    float h = convertHSB(this.color)[0];
                    g.setColor(new Color(Color.HSBtoRGB(h, s / 100f, b / 100f)));
                    g.fillRect(i, j, 2, 2);
                    b--;
                }
                s--;
            }
            g.setColor(Color.WHITE);
            g.draw(this.index1);
            g.setColor(Color.BLACK);
            g.draw(this.index2);
        }

        protected void update(Color color) {
            this.color = color;
            this.colors = new ArrayList<>();
            float[] HSB = convertHSB(color);
            int s = 100;
            for (int i = 0; i < 200; i += 2) {
                int b = 100;
                ArrayList<Color> eachLine = new ArrayList<>();
                for (int j = 0; j < 200; j += 2) {
                    float h = convertHSB(this.color)[0];
                    eachLine.add(new Color(Color.HSBtoRGB(h, s / 100f, b / 100f)));
                    b--;
                }
                colors.add(eachLine);
                s--;
            }
            this.index1 = new Ellipse2D.Double(200 - HSB[1]*200 - 2, 200 - HSB[2]*200 - 2, 6, 6);
            this.index2 = new Ellipse2D.Double(200 - HSB[1]*200 - 1, 200 - HSB[2]*200 - 1, 4, 4);
            this.repaint();
        }

        protected Color listener(Point2D point2D) {
            if (point2D.getX() <= 0)
                point2D = new Point2D.Double(0, point2D.getY());
            if (point2D.getY() <= 0)
                point2D = new Point2D.Double(point2D.getX(), 0);
            if (point2D.getX() > 199)
                point2D = new Point2D.Double(198, point2D.getY());
            if (point2D.getY() > 199)
                point2D = new Point2D.Double(point2D.getX(), 198);
            this.index1 = new Ellipse2D.Double(point2D.getX() - 2, point2D.getY() - 2, 6, 6);
            this.index2 = new Ellipse2D.Double(point2D.getX() - 1, point2D.getY() - 1, 4, 4);

            this.repaint();
            return this.colors.get((int) point2D.getX() / 2).get((int) point2D.getY() / 2);
        }
    }
    /**
     * The preview window for choosing color.
     * Will be co-work with Color bar, Color window and
     * Color button and interacted by user.
     * */
    private class Preview extends JComponent {
        private Color display = Color.RED;

        Preview() {
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            g.setColor(this.display);
            g.fillRect(0, 20, 50, 50);
            g.setColor(Color.BLACK);
            g.drawString("Preview", 0, 12);
        }

        protected void update(Color color) {
            this.display = color;
            repaint();
        }
    }

    /**
     * The color button will show the pre-set color
     * for user.
     * */
    private class ColorBtn extends JComponent {
        private Color color;
        private boolean onSelect = false;
        private int id = 0;

        ColorBtn(Color color, int id) {
            this.color = color;
            this.id = id;
        }

        protected Color getColor() {
            this.onSelect = true;
            return this.color;
        }

        protected int getId() {
            return this.id;
        }

        protected void setUnselect() {
            this.onSelect = false;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            if (this.onSelect) {
                g.setColor(new Color(174,174,174));
                g.fill(new Ellipse2D.Double(0, 0, 35, 35));
            }
            g.setColor(this.color);
            g.fill(new Ellipse2D.Double(5, 5, 25, 25));
        }
    }

    /**
     * convert a RGB color to HSB color.
     * @param RGB, a RGB color
     * */
    private float[] convertHSB(Color RGB) {
        int r = RGB.getRed();
        int g = RGB.getGreen();
        int b = RGB.getBlue();
        return Color.RGBtoHSB(r, g, b, null);
    }

    /**
     * Return the user choose color when
     * the this app closed.
     * */
    public Color getOutputColor() {
        return this.outputColor;
    }
}
