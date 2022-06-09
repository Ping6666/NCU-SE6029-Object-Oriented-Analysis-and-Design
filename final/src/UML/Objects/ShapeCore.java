package UML.Objects;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import UML.Canvas;
import UML.JsonFile;
import UML.Objects.Port.Side;

/**
 * NOTE
 * Compostie Design Pattern, percolating up often to be a bad design, but when
 * the scale come to compostie. It will become a design that benefit >
 * disadvantage.
 */
public abstract class ShapeCore {
    protected Canvas canvas = Canvas.getInstance(); // for save op

    public Rectangle rectangle;
    protected boolean beSelected;
    public GroupBase groupBase;

    public ShapeCore() {
        this.rectangle = null;
        this.beSelected = false;
        this.groupBase = null;
    }

    public ShapeCore(Rectangle rectangle) {
        this.rectangle = rectangle;
        this.beSelected = false;
        this.groupBase = null;
    }

    public void setSelectionState(boolean check) {
        this.beSelected = check;
    }

    /** basic draw things: background, self part */
    public abstract void draw(Graphics g);

    /** additional draw things: name, cross line, selected box */
    protected abstract void drawWorkHouse(Graphics g);

    /** the only middle coordinate */
    protected Point getCenterPoint() {
        Point center = new Point(0, 0);
        center.x = (int) this.rectangle.getCenterX();
        center.y = (int) this.rectangle.getCenterY();
        return center;
    }

    public abstract Side containPointSide(Point v);

    public abstract Side containPoint(Point p);

    /** check if object core is fully inside rect(p1, p2) */
    public boolean insideRect(Rectangle rectangle) {
        if ((rectangle.contains(this.rectangle))) {
            return true;
        }
        return false;
    }

    /** p is difference on point */
    public void moveObject(Point point) {
        this.rectangle.translate(point.x, point.y);
    }

    /** Compostie Design Pattern, percolating up for GroupBase & ObjectBase */
    public String getName() {
        return null;
    }

    /** Compostie Design Pattern, percolating up for GroupBase & ObjectBase */
    public void changeName(String name) {
        return;
    }

    /** Compostie Design Pattern, percolating up for GroupBase */
    public ArrayList<ShapeCore> getGroupShapeCores() {
        return null;
    }

    /** percolating up for ObjectBase & LineBase */
    public Port getPort(Side side) {
        return null;
    }

    /** Compostie Design Pattern, percolating up for GroupBase */
    public boolean resetGroupBase() {
        return false;
    }

    public abstract Object convertToJSON();

    public abstract boolean convertFromJSON(JsonFile jsonFileHandler, Object object);
}
