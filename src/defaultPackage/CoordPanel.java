package defaultPackage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.HashSet;

/**
 * The main draw board class. This board contain a x-y axis and it own units.
 * At the initial each 10 px = 0.2 mm. This class has 3 pre-set style, user can
 * choose show the x-y axis with grids or x-y axis or no help line to show. The size
 * for this board in pixel is 720,000 by 720,000, in the relative size (in mm, cm, dm and m)
 * in infinity big.
 *
 * Add elements by click. i.e draw a line by click two times, one for start, one for end.
 *
 * @author Yanlong LI, u5890571
 * */
public class CoordPanel extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener, MenuObs {

    public static final String STANDARD = "Standard";
    public static final String SIMPLE = "SIMPLE";
    public static final String EMPTY = "EMPTY";
    public static final String mm = "mm";
    public static final String cm = "cm";
    public static final String dm = "dm";
    public static final String m = "m";
    private final int initSize = 360000;
    private int centralOX = 720/2, centralOY = 420/2;

    private String style = STANDARD;
    private int width = 0;
    private int height = 0;
    private int offsetX = 0, offsetY = 0;
    private int oldX = 0, oldY = 0, curX = 0, curY = 0;
    private int centralX;
    private int centralY;
    private int zoomFactor = 15;// for the grid display only, 10 <= this <= 20
    private double globalZoomFactor = 0.2;
    private int zoomIndex = 0;
    private String command = "!!";
    private ColourPicker colourPicker = new ColourPicker();
    private ElementControl control;
    private ElementsFactory elementsFactory;
    private MainWindow host;

    private Color globalColor = Color.BLACK;


    /**
     * this panel size with zoom setting is infinity, for the low-level render on display size is 360000 * 360000 (px)
     */
    public CoordPanel(int width, int height, MainWindow host, ElementsFactory elementsFactory) {
        this.host = host;
        this.elementsFactory = elementsFactory;
        control = null;

        this.width = width + 100;
        this.height = height + 100;// set the board. 100px over than main window always.
        // when setting bounds MUST set the initial point at -50, -50;
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.centralX = width / 2;
        this.centralY = height / 2;

        // register the observer for draw, modify and other features from menu.
        host.menu.save.reg(this);
        host.menu.load.reg(this);

        host.menu.edit.reg(this);
        host.menu.lineItem.reg(this);
        host.menu.circleItem.reg(this);
        host.menu.triangleItem.reg(this);
        host.menu.rectangleItem.reg(this);
        host.menu.squareItem.reg(this);
        host.menu.arcItem.reg(this);
        host.menu.polyItem.reg(this);

        host.menu.edit2.reg(this);
        host.menu.showMeasure.reg(this);
        host.menu.setDescribe.reg(this);
        host.menu.setColour.reg(this);
        host.menu.showLabel.reg(this);
        host.menu.group.reg(this);
        host.menu.remove.reg(this);
        host.menu.clear.reg(this);
        host.menu.ungroup.reg(this);

        host.menu.gridItem.reg(this);
        host.menu.simpleItem.reg(this);
        host.menu.emptyItem.reg(this);
        host.menu.colorItem.reg(this);
        host.menu.unitMM.reg(this);
        host.menu.unitCM.reg(this);
        host.menu.unitDM.reg(this);
        host.menu.unitM.reg(this);
        colourPicker.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                globalColor = colourPicker.getOutputColor();
                for(Elements elms : host.draw){
                    elms.setColor(globalColor);
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graph) {
        super.paintComponent(graph);
        Graphics2D g = (Graphics2D) graph;
        int x = centralX, y = centralY;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.width, this.height);
        if (!style.equals(EMPTY)) {
            // x, y axis
            g.setColor(Color.BLACK);
            g.drawLine(x + offsetX, -initSize + offsetY, x + offsetX, initSize + offsetY);
            g.drawLine(-initSize + offsetX, y + offsetY, initSize + offsetX, y + offsetY);
            if (style.equals(STANDARD)) {
                // other line
                int board = 1;
                for (int i = centralX - zoomFactor; i >= -initSize; i -= zoomFactor) {
                    if (board % 5 == 0)
                        g.setColor(new Color(127, 127, 127, 127));
                    else
                        g.setColor(new Color(127, 127, 127, 63));
                    board++;
                    g.drawLine(i + offsetX, -initSize + offsetY, i + offsetX, initSize + offsetY);
                }
                board = 1;
                for (int i = centralX + zoomFactor; i <= initSize; i += zoomFactor) {
                    if (board % 5 == 0)
                        g.setColor(new Color(127, 127, 127, 127));
                    else
                        g.setColor(new Color(127, 127, 127, 63));
                    board++;
                    g.drawLine(i + offsetX, -initSize + offsetY, i + offsetX, initSize + offsetY);
                }
                board = 1;
                for (int i = centralY - zoomFactor; i >= -initSize; i -= zoomFactor) {
                    if (board % 5 == 0)
                        g.setColor(new Color(127, 127, 127, 127));
                    else
                        g.setColor(new Color(127, 127, 127, 63));
                    board++;
                    g.drawLine(-initSize + offsetX, i + offsetY, initSize + offsetX, i + offsetY);
                }
                board = 1;
                for (int i = centralY + zoomFactor; i <= initSize; i += zoomFactor) {
                    if (board % 5 == 0)
                        g.setColor(new Color(127, 127, 127, 127));
                    else
                        g.setColor(new Color(127, 127, 127, 63));
                    board++;
                    g.drawLine(-initSize + offsetX, i + offsetY, initSize + offsetX, i + offsetY);
                }
            }
        }
        //draw the unit and zoom value.
        g.setColor(Color.BLACK);
        g.drawString("x" + getZoomfactor(), width - 50, 25);
        if(!this.command.equals("!!")){
            if(this.command.length()>4)
                g.drawString(this.command,width-50-(this.command.length()-4)*5,40);
            else
                g.drawString(this.command,width-50,40);
        }
        if(selectEnd != null && selectStart != null && showSelect){
            // draw the select area when drag with RMB.
            int startX, startY;
            if(selectStart.getX()<selectEnd.getX())
                startX = (int)selectStart.getX();
            else
                startX = (int)selectStart.getX() - (int)Math.abs(selectEnd.getX()-selectStart.getX());
            if(selectStart.getY()<selectEnd.getY())
                startY = (int)selectStart.getY();
            else
                startY = (int)selectStart.getY() - (int)Math.abs(selectEnd.getY()-selectStart.getY());
            g.setColor(new Color(80,80,80));
            g.drawRect(startX, startY, (int)(Math.abs(selectEnd.getX()-selectStart.getX())), (int)(Math.abs(selectEnd.getY()-selectStart.getY())));
            g.setColor(new Color(80,80,80,50));
            g.fillRect(startX, startY, (int)(Math.abs(selectEnd.getX()-selectStart.getX())), (int)(Math.abs(selectEnd.getY()-selectStart.getY())));
            g.setColor(Color.BLACK);
        }
        // draw elements.
        host.draw.draw(g);
    }

    /**
     * The pan feature, co-work with mouse drag listener.
     * update the all line in board (excl. elements).
     *
     * Also calculate offset information for all elements.
     * @param offsetX , define how long panned in x-axis.
     * @param offsetY , define how long panned in y-axis.
     * */
    public void moveCoord(int offsetX, int offsetY) {
        this.offsetX += offsetX;
        this.offsetY += offsetY;
        if (this.offsetX >= this.initSize - this.width)
            this.offsetX = this.initSize - this.width;
        if (this.offsetX <= -this.initSize + this.width)
            this.offsetX = -this.initSize + this.width;

        if (this.offsetY >= this.initSize - this.height)
            this.offsetY = this.initSize - this.height;
        if (this.offsetY <= -this.initSize + this.height)
            this.offsetY = -this.initSize + this.height;
    }

    /**
     * The pan feature, co-work with mouse wheel listener.
     * update the all line in board (excl. elements).
     *
     * Also calculate zoom information for all elements.
     * @param zoomFactor, how many px user zoomed
     * */
    public void zoom(int zoomFactor) {
        if (this.globalZoomFactor >= 0.0025)
            this.zoomFactor += zoomFactor;
        if (this.globalZoomFactor < 0.0025 && zoomFactor <= 0)
            this.zoomFactor += zoomFactor;
        if (this.zoomFactor < 10) {
            this.zoomFactor = 20;
            if (this.globalZoomFactor == 0.4 * Math.pow(10, zoomIndex)) {
                this.globalZoomFactor = 1 * Math.pow(10, zoomIndex);
                zoomIndex++;
            } else this.globalZoomFactor *= 2;
        }
        if (this.zoomFactor > 20) {
            this.zoomFactor = 10;
            if (this.globalZoomFactor == 1 * Math.pow(10, zoomIndex - 1)) {
                zoomIndex--;
                this.globalZoomFactor = 0.4 * Math.pow(10, zoomIndex);
            } else this.globalZoomFactor /= 2;
        }
    }


    /**
     * Return the location based on x-y axis.
     * the cross point of X and Y is 0, 0,
     * unit mm. 1cm = 10 * 1 mm = 10 * 10 px
     */
    public Point2D getPoint(Point2D pt) {
        return new Point2D.Double((pt.getX() - this.centralX - this.offsetX) / (double) this.zoomFactor * this.globalZoomFactor,
                -(pt.getY() - this.centralY - this.offsetY) / (double) this.zoomFactor * this.globalZoomFactor);
    }

    /**
     * Return the location based on the display.
     * Left top is 0, 0 unit is px.
     */
    public Point2D getLoc(Point2D pt) {
        return new Point2D.Double((pt.getX() / this.globalZoomFactor * (double) this.zoomFactor + this.centralX + this.offsetX),
                -(pt.getY() / this.globalZoomFactor * (double) this.zoomFactor - this.centralY - this.offsetY));
    }

    public double getZoomfactor() {
        return (double) this.globalZoomFactor;
    }

    public void goHome() {
        this.offsetY = 0;
        this.offsetX = 0;
        this.globalZoomFactor = 0.2;
        this.zoomFactor = 10;
    }

    public void resizeComponent(int width, int height) {
        this.width = width;
        this.height = height;
        this.centralX = width / 2;
        this.centralY = height / 2;
        this.setPreferredSize(new Dimension(width, height));
        for(Elements elms : host.draw){
            elms.setOffset(new Point2D.Double(this.centralX-centralOX,this.centralY-centralOY));
        }
        this.centralOX = this.centralX;
        this.centralOY = this.centralY;
        repaint();
    }

    /**
     * Set the style from 3 pre-set style.
     * */
    public void setStyle(String style) {
        this.style = style;
        if (!style.equals(EMPTY) && !style.equals(SIMPLE) && !style.equals(STANDARD))
            this.style = STANDARD;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)){
            if(!this.command.equals("!!") && !this.command.equals("group") && !this.command.equals("Load")){
                if(this.command.equals("DESCRIBE")){
                    // set the label for user choose elements.
                    // each element can be set separately.
                    if(host.draw.findElement(e.getPoint())!=null){
                        StringInputer type = new StringInputer();
                        type.run();
                        type.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent x) {
                                host.draw.findElement(e.getPoint()).setLabel(type.getLabel());
                                repaint();
                            }
                        });
                    }
                }else if(this.command.equals("COLOR")){
                    // set the color for user choose elements.
                    // each element can be set separately.
                    if(host.draw.findElement(e.getPoint())!=null){
                        ColourPicker local = new ColourPicker();
                        local.run();
                        local.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent x) {
                                host.draw.findElement(e.getPoint()).setColor(local.getOutputColor());
                                repaint();
                            }
                        });
                    }
                }else if(this.command.equals("REMOVE")){
                    // remove the element which choose by user.
                    host.draw.remove(host.draw.findElement(e.getPoint()));
                }
                else {
                    // draw the new Elements, type of element choose by user.
                    if (!elements.isFinish())
                        elements.addPoint(e.getPoint());
                    else{
                        elements = new StartPoint();
                        elements = elementsFactory.createElementFromMouseClick(this.command, this.globalColor, e.getPoint());
                        elements.setZoomFactor(this.zoomFactor, this.globalZoomFactor);
                        elements.setShowLabel(isShowLab);
                        elements.setShowMeasure(isShowMea);
                        host.draw.add(elements);
                    }
                    control = new ElementControl(elements, 1);
                }
            }
        }
        repaint();
    }


    private Elements elements = new StartPoint();
    private boolean isShowLab = false;
    private boolean isShowMea = false;
    private Point2D selectStart = null;
    private Point2D selectEnd = null;
    private boolean showSelect = false;
    @Override
    public void mousePressed(MouseEvent e) {
        oldX = e.getX();
        oldY = e.getY();

        if (e.getButton() == 1) {
            if (this.command.equals("EDIT"))
                control = host.draw.findControl(e.getPoint());
        }
        if(e.getButton() == 3){
            selectStart = e.getPoint();
        }
        repaint();
    }

    private void drawControl(boolean draw){
        for (Elements elements : host.draw)
            elements.showControl(draw);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        control = null;
        curX = e.getX();
        curY = e.getY();
        if(e.getButton() == 3){
            selectEnd = e.getPoint();
            showSelect = false;
            selectStart = null;
            if(this.command.equals("group")){
                groupId++;
                this.command = "EDIT";
            }
            if(this.command.equals("ungroup"))
                this.command = "EDIT";
        }

        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    private int groupId = 0;
    @Override
    public void mouseDragged(MouseEvent e) {
        curX = e.getX();
        curY = e.getY();
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (control != null) {
                int group = control.elements.getGroup();
                if(control.control==0){
                    if(group==-1)
                        control.elements.moveControlPoint(0, e.getPoint());
                    else{
                        double ox = control.elements.controlPoints().get(0).getX(), oy = control.elements.controlPoints().get(0).getY();
                        HashSet<Elements> buffer = new HashSet<>();
                        for(Elements elms : host.draw){
                            if (elms.getGroup() == group){
                                buffer.add(elms);
                            }
                        }
                        for(Elements elms : buffer){
                            if (elms.getGroup() == group){
                                double dx = elms.controlPoints().get(0).getX()-ox;
                                double dy = elms.controlPoints().get(0).getY()-oy;
                                elms.moveControlPoint(0,PUtil.add(e.getPoint(), new Point2D.Double(dx,dy)));
                            }
                        }
                    }
                }else{
                    control.elements.moveControlPoint(control.control, e.getPoint());
                    control.elements.setZoomFactor(this.zoomFactor, this.globalZoomFactor);
                    control.elements.updateMeasurements(this.centralX,this.centralY,this.offsetX,this.offsetY,this.zoomFactor,this.globalZoomFactor);
                }
            } else {
                int thisoffsetX = curX - oldX;
                int thisoffsetY = curY - oldY;
                for (Elements elements : host.draw) {
                    elements.setOffset(new Point2D.Double(thisoffsetX, thisoffsetY));
                    elements.setZoomFactor(this.zoomFactor, this.globalZoomFactor);
                }
                this.moveCoord(thisoffsetX, thisoffsetY);
            }
            // pan
        }
        if(SwingUtilities.isRightMouseButton(e)){
            showSelect = true;
            selectEnd = e.getPoint();
            Point2D topLeft, bottomRight;
            if(selectStart.getX()<selectEnd.getX() && selectStart.getY()<selectEnd.getY())
                topLeft = selectStart;
            else if(selectStart.getX()<selectEnd.getX() && selectStart.getY()>selectEnd.getY())
                topLeft = new Point2D.Double(selectStart.getX(),selectEnd.getY());
            else if(selectStart.getX()>selectEnd.getX() && selectStart.getY()<selectEnd.getY())
                topLeft = new Point2D.Double(selectEnd.getX(),selectStart.getY());
            else topLeft = selectEnd;
            if(selectStart.getX()<selectEnd.getX() && selectStart.getY()<selectEnd.getY())
                bottomRight = selectEnd;
            else if(selectStart.getX()<selectEnd.getX() && selectStart.getY()>selectEnd.getY())
                bottomRight = new Point2D.Double(selectEnd.getX(),selectStart.getY());
            else if(selectStart.getX()>selectEnd.getX() && selectStart.getY()<selectEnd.getY())
                bottomRight = new Point2D.Double(selectStart.getX(),selectEnd.getY());
            else bottomRight = selectStart;
            if(this.command.equals("group")){
                // set selected elements in a group, multiple groups supported.
                for(Elements elms : host.draw){
                    boolean isIn = false;
                    for(Point2D pts : elms.controlPoints()){
                        if(pts.getX()>=topLeft.getX()&&pts.getX()<=bottomRight.getX()&&pts.getY()>=topLeft.getY()&&pts.getY()<=bottomRight.getY()){
                            isIn = true;
                            if(elms.getGroup()>-1) groupId = elms.getGroup();
                            break;
                        }else isIn = false;
                    }
                    elms.showControl(isIn);
                    if(isIn){
                        elms.setGroup(groupId);
                    }
                }
            }
            if(this.command.equals("ungroup")){
                // ungroup the selected elements.
                for(Elements elms : host.draw){
                    boolean isIn = false;
                    for(Point2D pts : elms.controlPoints()){
                        if(pts.getX()>=topLeft.getX()&&pts.getX()<=bottomRight.getX()&&pts.getY()>=topLeft.getY()&&pts.getY()<=bottomRight.getY()){
                            isIn = true;
                            break;
                        }else isIn = false;
                    }
                    elms.setGroup(-1);
                    elms.showControl(isIn);
                }
            }
        }
        oldX = curX;
        oldY = curY;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Elements elements : host.draw) {
            if (!elements.isFinish()){
                elements.drawUpdate(e.getPoint());
                elements.updateMeasurements(this.centralX,this.centralY,this.offsetX,this.offsetY,this.zoomFactor,this.globalZoomFactor);
            }
        }

        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //set zoom for both board and all elements in the board.
        this.zoom(e.getWheelRotation());
        for (Elements elements : host.draw) {
            elements.setZoom((double) this.zoomFactor / this.globalZoomFactor, this.centralX, this.centralY,
                    this.offsetX, this.offsetY, this.zoomFactor, this.globalZoomFactor);
        }
        repaint();
    }

    public Point2D getCentralPt() {
        return new Point2D.Double(this.centralX, this.centralY);
    }

    public double getZoomFactor() {
        return this.zoomFactor;
    }


    @Override
    public void update(String command) {
        if(command.equals("Standard"))
            this.setStyle(CoordPanel.STANDARD);
        else if(command.equals("Simple"))
            this.setStyle(CoordPanel.SIMPLE);
        else if(command.equals("Empty"))
            this.setStyle(CoordPanel.EMPTY);
        else if(command.equals("Color"))
            colourPicker.run();
        else if(command.equals("RMALL")){
            //remove all elements in board.
            host.draw.clear();
        }else if(command.equals("Save")){
            // save the board into a file.
            String content = "";
            for(Elements elms : host.draw)
                content+=elms.save((double) this.zoomFactor / this.globalZoomFactor, this.centralX, this.centralY,
                        this.offsetX, this.offsetY, this.zoomFactor, this.globalZoomFactor)+"<>";
            saveFile(content);
        }else if(command.equals("Load")){
            // load board from file.
            host.draw.clear();
            String content = loadFile();
            String[] objects = content.split("<>");
            for(String object : objects){
                Elements elements = elementsFactory.createElementFromSave(object, (double) this.zoomFactor / this.globalZoomFactor, this.centralX, this.centralY,
                        this.offsetX, this.offsetY, this.zoomFactor, this.globalZoomFactor);
                elements.updateMeasurements(centralX,centralY,offsetX,offsetY,zoomFactor,globalZoomFactor);
                host.draw.add(elements);

            }
            this.command="EDIT";
        }else if(command.equals("SHOWMEAS")){
            // set show measure for each elements or not.
            // default is not show, click once to let all elements
            // show measure, click again hide.
            this.isShowMea = !this.isShowMea;
            for(Elements elms : host.draw){
                elms.setShowMeasure(this.isShowMea);
            }
        }else if(command.equals("LABEL")){
            // set show label for each elements or not.
            // default is not show, click once to let all elements
            // show label, click again hide.
            this.isShowLab = !this.isShowLab;
            for(Elements elms : host.draw){
                elms.setShowLabel(this.isShowLab);
            }
        }else if(command.equals(mm)){
            // set draw and elements unit.
            for(Elements elms : host.draw){
                elms.setUnit(mm);
                elms.updateMeasurements(this.centralX,this.centralY,this.offsetX,this.offsetY,this.zoomFactor,this.globalZoomFactor);
            }
        }else if(command.equals(cm)){
            // set draw and elements unit.
            for(Elements elms : host.draw){
                elms.setUnit(cm);
                elms.updateMeasurements(this.centralX,this.centralY,this.offsetX,this.offsetY,this.zoomFactor,this.globalZoomFactor);
            }
        }else if(command.equals(dm)){
            // set draw and elements unit.
            for(Elements elms : host.draw){
                elms.setUnit(dm);
                elms.updateMeasurements(this.centralX,this.centralY,this.offsetX,this.offsetY,this.zoomFactor,this.globalZoomFactor);
            }
        }else if(command.equals(m)){
            // set draw and elements unit.
            for(Elements elms : host.draw){
                elms.setUnit(m);
                elms.updateMeasurements(this.centralX,this.centralY,this.offsetX,this.offsetY,this.zoomFactor,this.globalZoomFactor);
            }
        }
        else{
            this.command = command;
        }
        if (this.command.equals("EDIT") || this.command.equals("COLOR") || this.command.equals("DESCRIBE") || this.command.equals("REMOVE") || this.command.equals("LABEL")) {
            drawControl(true);
        }else
            drawControl(false);
        repaint();
    }

    /**
     * Defined a pop up window for save and also
     * write board state in file. This application used
     * self defined file type: jcd.
     *
     * The internal format is:
     * TYPE         (String TYPE)
     * Describe     (String describe)
     * Color        (int r, int g, int b)
     * Points       (double x1,double y1 double x2, double y2)
     * End object   (<>)
     * @param content, the board state in string.*/
    private void saveFile(String content){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JaCAD save file(*.jcd)", "jcd");
        chooser.setFileFilter(filter);
        int option = chooser.showSaveDialog(null);
        if(option==JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();

            String fname = chooser.getName(file);

            if(!fname.contains(".jcd")){
                file=new File(chooser.getCurrentDirectory(),fname+".jcd");
            }

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(content);
                bw.flush();
                bw.close();

            } catch (IOException e) {
                System.err.println("IO");
                e.printStackTrace();
            }
        }
    }

    /**
     * A pop up window for choose file for load.
     *
     * @return board information in the file int String format.
     * */
    private String loadFile(){
        String content = "";
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JaCAD save file(*.jcd)", "jcd");
        chooser.setFileFilter(filter);
        int option = chooser.showOpenDialog(null);
        if(option==JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str = null;
                while((str = br.readLine()) != null){
                    content+=str;
                }

            } catch (IOException e) {
                System.err.println("IO");
                e.printStackTrace();
            }
        }
        return content;
    }

    public String getCommand(){
        return this.command;
    }
}
