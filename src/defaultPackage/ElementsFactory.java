package defaultPackage;

import java.awt.*;

/**
 * The interface for creat elements.
 * Extend from Eric's code.
 *
 * @author Yanlong LI, u5890571
 * */
public interface ElementsFactory {
    Elements createElementFromMouseClick(String toolcommand, Color color, Point pos);
    Elements createElementFromSave(String object, double zoom, double centralX, double centralY, double offsetX, double offsetY, double zoomFactor, double gZoom);
}
