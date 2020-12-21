package defaultPackage;

/**
 * ElementControlPoint - this class is used to reference the draw element of the control point and
 * the index of the control of the list of control points of the element.
 *
 * @author Yanlong LI, u5890571
 * */
public class ElementControl {
    Elements elements;
    int control;

    public ElementControl(Elements elements, int control) {
        super();
        this.elements = elements;
        this.control = control;
    }
}
