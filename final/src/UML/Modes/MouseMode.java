package UML.Modes;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import UML.Canvas;

/** do not implement any mode action here */
public abstract class MouseMode implements MouseInputListener {
    protected Canvas canvas;
    protected Point pressedPoint, currPoint, prevPoint;

    public MouseMode() {
        this.canvas = Canvas.getInstance();
        this.pressedPoint = null;
        this.currPoint = null;
        this.prevPoint = null;
    }

    protected void setPoint(Point p) {
        this.prevPoint = this.currPoint;
        this.currPoint = p;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        this.setPoint(e.getPoint());
        this.pressedPoint = this.currPoint;
        System.out.print("\r\tMouse pressed on " + this.currPoint);
    }

    public void mouseReleased(MouseEvent e) {
        this.setPoint(e.getPoint());
        System.out.print("\r\tMouse released on " + this.currPoint);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        this.setPoint(e.getPoint());
        System.out.print("\r\tMouse dragged on " + this.currPoint);
    }

    public void mouseMoved(MouseEvent e) {
    }
}
