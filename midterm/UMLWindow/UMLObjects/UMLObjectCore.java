package UMLWindow.UMLObjects;

import java.awt.Point;
import java.awt.Graphics;

/** the base class of the UML object, typically is THE composite */
public class UMLObjectCore {
    // the basic member
    public String name;
    public Point startCoord;
    public int depth;
    public boolean isSelecting;
    public UMLObjectCore outerComposite;
    /**
     * height & width: class_object (Rect), usecase_object (Oval)
     * this should be a constant
     * WARN: height be the base height, it cannot pass then compute 1/3, 2/3
     * WARN: in other words, total-height will be 3 * height
     */
    public int height, width;

    public UMLObjectCore(String name_, Point startCoord_, int depth_) {
        this.name = name_;
        this.startCoord = startCoord_;
        this.depth = depth_;
        this.isSelecting = false;
        this.outerComposite = null;
    }

    /** include fill & draw border */
    public void drawBasicWorkhouse(Graphics g) {
        // additional self part
        g.drawRect(this.startCoord.x, this.startCoord.y, this.width, this.getTrueHeight());
    }

    /** include name and the 4-line will conneted to other obj. */
    public void drawAdvancedWorkhouse(Graphics g) {
        Point nameCoord = this.getNameCoord();
        // object name
        g.drawString(this.name, nameCoord.x, nameCoord.y);
    }

    /** include anything will show when be selected, cross-line / select box */
    public void drawSelectionWorkhouse(Graphics g) {
        if (this.isSelecting) {
            g.fillOval(this.startCoord.x + 5, this.startCoord.y + 5, 3, 3);
        }
    }

    protected int getTrueHeight() {
        return this.height;
    }

    /** just convenient for compute multiply with ratio */
    protected int mulRatio(double d, double r) {
        return (int) (d * r);
    }

    /** the only middle coordinate */
    protected Point getMiddleCoord() {
        Point midCoord = new Point(this.startCoord.x + this.mulRatio(this.width, (1.0 / 2.0)),
                this.startCoord.y + this.mulRatio(this.getTrueHeight(), (1.0 / 2.0)));
        return midCoord;
    }

    /**
     * one of the four cross line coordinate
     * 0: top-left corner
     * 1: top-right corner
     * 2: bottom-right corner
     * 3: bottom-left corner
     */
    public Point getCrossCoord(int side_) {
        Point crossCoord = null;
        switch (side_) {
            case -1: // bottom-right corner
                crossCoord = new Point(this.startCoord.x + this.width,
                        this.startCoord.y + this.height);
                break;
            default:
                break;
        }
        return crossCoord;
    }

    protected Point getNameCoord() {
        Point nameCoord = new Point(this.startCoord.x + 10, this.startCoord.y + 20);
        return nameCoord;
    }

    /** check if object core contains point(p) */
    public boolean containPoint(Point p) {
        Point midCoord = this.getMiddleCoord();
        Point v = new Point(p.x - midCoord.x, p.y - midCoord.y);
        if ((Math.abs(v.x) <= this.mulRatio(this.width, (1.0 / 2.0))) &&
                (Math.abs(v.y) <= this.mulRatio(this.getTrueHeight(), (1.0 / 2.0)))) {
            return true;
        }
        return false;
    }

    /** check if object core is fully inside rect(p1, p2) */
    public boolean insideRect(Point p1, Point p2) {
        Point pBottomRight = this.getCrossCoord(-1);
        Point diff1, diff2;
        diff1 = new Point(p1.x - this.startCoord.x, p1.y - this.startCoord.y);
        diff2 = new Point(p2.x - pBottomRight.x, p2.y - pBottomRight.y);
        if ((diff1.x <= 0 && diff1.y <= 0) && (diff2.x >= 0 && diff2.y >= 0)) {
            return true;
        }
        return false;
    }

    public void changeName(String name_) {
        this.name = name_;
    }

    public void moveObject(Point p) {
        this.startCoord.x += p.x;
        this.startCoord.y += p.y;
    }

    public boolean canSetSelecting() {
        if (this.outerComposite == null) {
            return true;
        } else if (this.outerComposite == this) {
            return true;
        }
        return false;
    }

    public void setIsSelecting(boolean isSelecting_) {
        this.isSelecting = isSelecting_;
    }

    /** set outer composite, null if none */
    public void setOuterComposite(UMLObjectCore outerComposite_) {
        this.outerComposite = outerComposite_;
    }
}
