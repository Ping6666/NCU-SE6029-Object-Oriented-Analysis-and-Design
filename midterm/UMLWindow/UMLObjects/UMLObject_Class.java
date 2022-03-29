package UMLWindow.UMLObjects;

import java.awt.Point;
import java.awt.Graphics;

/** the sub-class of umlo, typically is basic component (Usecase) */
public class UMLObject_Class extends UMLObject {
    public UMLObject_Class(String name_, Point startCoord_, int depth_,
            int height_, int width_) {
        super(name_, startCoord_, depth_, height_, width_);
    }

    @Override
    public void drawBasicWorkhouse(Graphics g) {
        super.drawBasicWorkhouse(g);
        // additional self part
        g.drawRect(this.startCoord.x, this.startCoord.y, this.width, this.height);
        g.drawRect(this.startCoord.x, this.startCoord.y, this.width, (2 * this.height));
    }

    @Override
    protected int getTrueHeight() {
        return 3 * this.height;
    }

    @Override
    public Point getCrossCoord(int side_) {
        Point crossCoord = null;
        switch (side_) {
            case -1: // special bottom-right corner
                crossCoord = new Point(this.startCoord.x + this.width, this.startCoord.y + this.getTrueHeight());
                break;
            case 0: // top-left corner
                crossCoord = this.startCoord;
                break;
            case 1: // top-right corner
                crossCoord = new Point(this.startCoord.x + this.width, this.startCoord.y);
                break;
            case 2: // bottom-right corner
                crossCoord = new Point(this.startCoord.x + this.width, this.startCoord.y + this.getTrueHeight());
                break;
            case 3: // bottom-left corner
                crossCoord = new Point(this.startCoord.x, this.startCoord.y + this.getTrueHeight());
                break;
            default:
                break;
        }
        return crossCoord;
    }

    @Override
    public boolean containPoint(Point p) {
        Point midCoord = this.getMiddleCoord();
        Point v = new Point(p.x - midCoord.x, p.y - midCoord.y);
        if ((Math.abs(v.x) <= this.mulRatio(this.width, (1.0 / 2.0))) &&
                (Math.abs(v.y) <= this.mulRatio(this.height, (3.0 / 2.0)))) {
            this.containPointSide(v);
            return true;
        }
        return false;
    }
}
