package UML.Objects.ObjectBases;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;

import UML.Objects.Port.Side;
import UML.Objects.ObjectBase;

public class Object_Usecase extends ObjectBase {
    public Object_Usecase() {
        super();
    }

    public Object_Usecase(Rectangle rectangle) {
        super(rectangle);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
        g.setColor(Color.BLACK);
        g.drawOval(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
        this.drawWorkHouse(g);
    }

    @Override
    protected Point getCrossPoint(int side) {
        Point centerPoint = this.getCenterPoint();
        Point crossBasePoint = new Point((int) (2 * 25 * (double) 1 / Math.pow(2, 0.5)),
                (int) (25 * (double) 1 / Math.pow(2, 0.5)));
        Point crossPoint = null;
        switch (side) {
            case -1: // bottom-right corner
                crossPoint = (Point) this.rectangle.getLocation().clone();
                crossPoint.translate(this.rectangle.width, this.rectangle.height);
            case 0: // top-left corner
                crossPoint = new Point(centerPoint.x - crossBasePoint.x, centerPoint.y - crossBasePoint.y);
                break;
            case 1: // top-right corner
                crossPoint = new Point(centerPoint.x + crossBasePoint.x, centerPoint.y - crossBasePoint.y);
                break;
            case 2: // bottom-right corner
                crossPoint = new Point(centerPoint.x + crossBasePoint.x, centerPoint.y + crossBasePoint.y);
                break;
            case 3: // bottom-left corner
                crossPoint = new Point(centerPoint.x - crossBasePoint.x, centerPoint.y + crossBasePoint.y);
                break;
            default:
                break;
        }
        return crossPoint;
    }

    @Override
    public Side containPoint(Point p) {
        Point centerPoint = this.getCenterPoint();
        Point v = new Point(p.x - centerPoint.x, p.y - centerPoint.y);
        if ((Math.pow(v.x / ((double) this.rectangle.width / 2), 2)
                + Math.pow(v.y / ((double) this.rectangle.height / 2), 2)) <= 1) {
            return this.containPointSide(p);
        }
        return Side.Outside;
    }

    @Override
    protected Point getNamePoint() {
        Point namePoint = this.getCenterPoint();
        namePoint.translate(-40, 5);
        return namePoint;
    }
}
