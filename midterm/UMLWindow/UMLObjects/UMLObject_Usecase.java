package UMLWindow.UMLObjects;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;

/** the sub-class of umlo, typically is basic component (Usecase) */
public class UMLObject_Usecase extends UMLObject {
    public UMLObject_Usecase(String name_, Point startCoord_, int depth_,
            int height_, int width_) {
        super(name_, startCoord_, depth_, height_, width_);
    }

    @Override
    public void drawBasicWorkhouse(Graphics g) {
        // self overlay others
        g.setColor(Color.WHITE);
        g.fillOval(this.startCoord.x, this.startCoord.y, this.width, this.height);
        g.setColor(Color.BLACK);
        // self part
        g.drawOval(this.startCoord.x, this.startCoord.y, this.width, this.height);
    }

    @Override
    public Point getCrossCoord(int side_) {
        Point midCoord = this.getMiddleCoord();
        Point crossBaseCoord = new Point((int) (2 * 25 * 1 / Math.pow(2, 0.5)), (int) (25 * 1 / Math.pow(2, 0.5)));
        Point crossCoord = null;
        switch (side_) {
            case -1: // special bottom-right corner
                crossCoord = new Point(this.startCoord.x + this.width, this.startCoord.y + this.height);
                break;
            case 0: // top-left corner
                crossCoord = new Point(midCoord.x - crossBaseCoord.x, midCoord.y - crossBaseCoord.y);
                break;
            case 1: // top-right corner
                crossCoord = new Point(midCoord.x + crossBaseCoord.x, midCoord.y - crossBaseCoord.y);
                break;
            case 2: // bottom-right corner
                crossCoord = new Point(midCoord.x + crossBaseCoord.x, midCoord.y + crossBaseCoord.y);
                break;
            case 3: // bottom-left corner
                crossCoord = new Point(midCoord.x - crossBaseCoord.x, midCoord.y + crossBaseCoord.y);
                break;
            default:
                break;
        }
        return crossCoord;
    }

    @Override
    protected Point getNameCoord() {
        Point nameCoord = this.getMiddleCoord();
        nameCoord.x -= 40;
        nameCoord.y += 5;
        return nameCoord;
    }

    @Override
    public boolean containPoint(Point p) {
        Point midCoord = this.getMiddleCoord();
        Point v = new Point(p.x - midCoord.x, p.y - midCoord.y);
        if ((Math.pow(v.x / this.mulRatio(this.width, (1.0 / 2.0)), 2)
                + Math.pow(v.y / this.mulRatio(this.height, (1.0 / 2.0)), 2)) <= 1) {
            this.containPointSide(v);
            return true;
        }
        return false;
    }
}
