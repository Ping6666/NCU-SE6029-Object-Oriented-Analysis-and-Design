package UML.Modes.MouseActions;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import UML.Objects.Port.Side;
import UML.Modes.MouseMode;
import UML.Objects.LineBase;
import UML.Objects.ShapeCore;

public class SelectionMode extends MouseMode {
    private Side side;
    private ShapeCore shapeCore;
    private MouseEvent mouseEvent;

    public SelectionMode() {
        super();
        this.resetState();
    }

    private void resetState() {
        this.mouseEvent = null;
        this.side = Side.Outside;
        this.shapeCore = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        this.mouseEvent = e;
        if (this.mouseEvent == null) {
            System.out.println("\r\tERROR: Fail handle mouse event, null pointer.");
            return;
        }

        if (SwingUtilities.isLeftMouseButton(this.mouseEvent)) {
            this.side = this.canvas.getSingleObject(this.currPoint, false);
            this.shapeCore = this.canvas.getShapeCore(0);
            if (this.side != Side.Outside && this.shapeCore != null) {
                // "USECASE C.1": single object selection
                if (this.side == Side.Inside) {
                    /* select on composite */
                    // do nothing
                } else if (this.side == Side.FrontPort || this.side == Side.BackPort) {
                    /* select on line */
                    this.canvas.moveObjectWorkHouse(this.shapeCore, this.currPoint);
                } else {
                    /* select on object */
                    // do nothing
                }
            } else {
                // "USECASE C.2": multiple object selection
                // do nothing
            }
        } else if (SwingUtilities.isMiddleMouseButton(this.mouseEvent)) {
            /* additional feature: move all object */
            // do nothing
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (this.mouseEvent == null) {
            System.out.println("\r\tERROR: Fail handle mouse event, null pointer.");
            return;
        }

        if (SwingUtilities.isLeftMouseButton(this.mouseEvent)) {
            if (this.side != Side.Outside && this.shapeCore != null) {
                if (this.side == Side.Inside) {
                    /* select on composite */
                    this.canvas.moveObjectWorkHouse(this.shapeCore, this.computePoint());
                } else if (this.side == Side.FrontPort || this.side == Side.BackPort) {
                    /* select on line */
                    this.canvas.moveObjectWorkHouse(this.shapeCore, this.currPoint);
                } else {
                    /* select on object */
                    this.canvas.moveObjectWorkHouse(this.shapeCore, this.computePoint());
                }
            } else {
                // "USECASE C.2": multiple object selection
                // do nothing
            }
        } else if (SwingUtilities.isMiddleMouseButton(this.mouseEvent)) {
            this.canvas.moveAllObjectWorkHouse(this.computePoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (this.mouseEvent == null) {
            System.out.println("\r\tERROR: Fail handle mouse event, null pointer.");
            return;
        }

        if (SwingUtilities.isLeftMouseButton(this.mouseEvent)) {
            if (this.side != Side.Outside && this.shapeCore != null) {
                if (this.side == Side.Inside) {
                    /* select on composite */
                    this.canvas.moveObjectWorkHouse(this.shapeCore, this.computePoint());
                } else if (this.side == Side.FrontPort || this.side == Side.BackPort) {
                    /* select on line */
                    Side tmpSide = this.canvas.getSingleObject(this.currPoint, true); // no line and composite obj.
                    ShapeCore tmpShapeCore = this.canvas.getShapeCore(0);
                    if (tmpSide != Side.Outside && tmpShapeCore != null) {
                        ((LineBase) this.shapeCore).setTargetPort(tmpShapeCore.getPort(tmpSide));
                    } else {
                        ((LineBase) this.shapeCore).resetTargetPort();
                    }
                } else {
                    /* select on object */
                    this.canvas.moveObjectWorkHouse(this.shapeCore, this.computePoint());
                }
            } else {
                // "USECASE C.2": multiple object selection
                Rectangle rectangle = this.computeRectangle(this.pressedPoint, this.currPoint);
                this.canvas.getMultiObject(rectangle);
            }
        } else if (SwingUtilities.isMiddleMouseButton(this.mouseEvent)) {
            this.canvas.moveAllObjectWorkHouse(this.computePoint());
        }
        this.resetState();
    }

    private Point computePoint() {
        Point diffPoint = (Point) this.currPoint.clone();
        diffPoint.translate(-this.prevPoint.x, -this.prevPoint.y);
        return diffPoint;
    }

    private Rectangle computeRectangle(Point p1, Point p2) {
        return new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y),
                Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
    }
}
