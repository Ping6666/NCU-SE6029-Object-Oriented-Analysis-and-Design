package UML.Objects;

import org.json.simple.JSONObject;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Map;

import java.util.HashMap;

import UML.JsonFile;
import UML.Objects.Port.Side;

public abstract class ObjectBase extends ShapeCore {
    protected String name;
    protected int boxSize;

    /**
     * one of the four select box base coordinate
     * which also the exactly line point of UML-line
     * 0: top corner
     * 1: right corner
     * 2: bottom corner
     * 3: left corner
     */
    protected Port[] ports;

    public ObjectBase() {
        super();
        this.name = "";
        this.boxSize = 10;
        this.ports = new Port[4];
        this.ports[0] = new Port(this, Side.TopPort);
        this.ports[1] = new Port(this, Side.RightPort);
        this.ports[2] = new Port(this, Side.BottomPort);
        this.ports[3] = new Port(this, Side.LeftPort);
    }

    public ObjectBase(Rectangle rectangle) {
        super(rectangle);
        this.name = "";
        this.boxSize = 10;
        this.ports = new Port[4];
        this.ports[0] = new Port(this, Side.TopPort);
        this.ports[1] = new Port(this, Side.RightPort);
        this.ports[2] = new Port(this, Side.BottomPort);
        this.ports[3] = new Port(this, Side.LeftPort);
    }

    @Override
    public void draw(Graphics g) {
        g.drawRect(this.rectangle.x, this.rectangle.y,
                this.rectangle.width, this.rectangle.height);
        this.drawWorkHouse(g);
    }

    @Override
    protected void drawWorkHouse(Graphics g) {
        // object name
        Point namePoint = this.getNamePoint();
        g.drawString(this.name, namePoint.x, namePoint.y);
        if (this.beSelected) {
            // cross lines
            for (int i = 0; i < 2; i++) {
                Point p[] = new Point[2];
                p[0] = this.getCrossPoint(i + 0);
                p[1] = this.getCrossPoint(i + 2);
                g.drawLine(p[0].x, p[0].y, p[1].x, p[1].y);
            }
            // 4-line anchor rect
            for (int i = 0; i < 4; i++) {
                Point boxPoint = this.getBoxPoint(this.getSide(i));
                g.fillRect(boxPoint.x, boxPoint.y, this.boxSize, this.boxSize);
            }
        }
    }

    /**
     * one of the four select box base coordinate
     * which also the exactly the line point
     */
    public Point getBoxBasePoint(Side side) {
        Point centerPoint = this.getCenterPoint();
        Point basePoint = null;
        switch (side) {
            case TopPort:
                basePoint = new Point(centerPoint.x, this.rectangle.y);
                break;
            case RightPort:
                basePoint = new Point(this.rectangle.x + this.rectangle.width, centerPoint.y);
                break;
            case BottomPort:
                basePoint = new Point(centerPoint.x, this.rectangle.y + this.rectangle.height);
                break;
            case LeftPort:
                basePoint = new Point(this.rectangle.x + 1, centerPoint.y);
                break;
            default:
                break;
        }
        return basePoint;
    }

    /**
     * one of the four select box start coordinate (came from base point)
     */
    protected Point getBoxPoint(Side side) {
        // all point refer to point on selected box
        Point basePoint = this.getBoxBasePoint(side);
        if (basePoint == null) {
            return null;
        }
        Point startPoint = null;
        switch (side) {
            case TopPort:
                startPoint = new Point(basePoint.x - this.boxSize / 2, basePoint.y - this.boxSize);
                break;
            case RightPort:
                startPoint = new Point(basePoint.x, basePoint.y - this.boxSize / 2);
                break;
            case BottomPort:
                startPoint = new Point(basePoint.x - this.boxSize / 2, basePoint.y);
                break;
            case LeftPort:
                startPoint = new Point(basePoint.x - this.boxSize, basePoint.y - this.boxSize / 2);
                break;
            default:
                break;
        }
        return startPoint;
    }

    /**
     * one of the four cross line coordinate
     * 0: top-left corner
     * 1: top-right corner
     * 2: bottom-right corner
     * 3: bottom-left corner
     */
    protected Point getCrossPoint(int side) {
        Point crossPoint = null;
        switch (side) {
            case -1: // bottom-right corner
                crossPoint = new Point(this.rectangle.x + this.rectangle.width,
                        this.rectangle.y + this.rectangle.height);
            case 0: // top-left corner
                crossPoint = new Point(this.rectangle.x, this.rectangle.y);
                break;
            case 1: // top-right corner
                crossPoint = new Point(this.rectangle.x + this.rectangle.width, this.rectangle.y);
                break;
            case 2: // bottom-right corner
                crossPoint = new Point(this.rectangle.x + this.rectangle.width,
                        this.rectangle.y + this.rectangle.height);
                break;
            case 3: // bottom-left corner
                crossPoint = new Point(this.rectangle.x, this.rectangle.y + this.rectangle.height);
                break;
            default:
                break;
        }
        return crossPoint;
    }

    /**
     * check point is on which side
     * 1: top contain the v
     * 2: right contain the v
     * 3: bottom contain the v
     * 4: left contain the v
     */
    @Override
    public Side containPointSide(Point p) {
        Point centerPoint = this.getCenterPoint();
        Point v = new Point(p.x - centerPoint.x, p.y - centerPoint.y);
        Boolean x, y;
        x = (v.x * this.rectangle.height + v.y * this.rectangle.width) >= 0 ? true : false;
        y = (v.x * (-1) * this.rectangle.height + v.y * this.rectangle.width) >= 0 ? true : false;
        Side result = Side.Outside;
        if (x && y) {
            result = Side.BottomPort;
        } else if (x && !y) {
            result = Side.RightPort;
        } else if (!x && y) {
            result = Side.LeftPort;
        } else {
            result = Side.TopPort;
        }
        return result;
    }

    @Override
    public Side containPoint(Point p) {
        if (this.rectangle.contains(p)) {
            Side result = this.containPointSide(p);
            System.out.println("\t\tselection side: " + result.toString());
            return result;
        }
        return Side.Outside;
    }

    protected Point getNamePoint() {
        Point namePoint = new Point(this.rectangle.x + 10, this.rectangle.y + 20);
        return namePoint;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void changeName(String name) {
        this.name = name;
    }

    protected Side getSide(int i) {
        Side tmpSide = null;
        switch (i) {
            case 0:
                tmpSide = Side.TopPort;
                break;
            case 1:
                tmpSide = Side.RightPort;
                break;
            case 2:
                tmpSide = Side.BottomPort;
                break;
            case 3:
                tmpSide = Side.LeftPort;
                break;
            default:
                break;
        }
        return tmpSide;
    }

    @Override
    public Port getPort(Side side) {
        Port port = null;
        switch (side) {
            case TopPort:
                port = this.ports[0];
                break;
            case RightPort:
                port = this.ports[1];
                break;
            case BottomPort:
                port = this.ports[2];
                break;
            case LeftPort:
                port = this.ports[3];
                break;
            default:
                break;
        }
        return port;
    }

    public int SideToIndex(Side side) {
        int index = -1;
        switch (side) {
            case TopPort:
                index = 0;
                break;
            case RightPort:
                index = 1;
                break;
            case BottomPort:
                index = 2;
                break;
            case LeftPort:
                index = 3;
                break;
            default:
                break;
        }
        return index;
    }

    @Override
    public Object convertToJSON() {
        Map<Object, Object> rectangleMap = new HashMap<Object, Object>();
        rectangleMap.put("x", this.rectangle.x);
        rectangleMap.put("y", this.rectangle.y);
        rectangleMap.put("width", this.rectangle.width);
        rectangleMap.put("height", this.rectangle.height);

        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("type", this.getClass().getSimpleName());
        map.put("rectangle", rectangleMap);
        map.put("groupBase", this.canvas.ShapeCoreToIndex(this.groupBase));
        map.put("name", this.name);

        // System.out.println(new JSONObject(map).toJSONString());
        return new JSONObject(map);
    }

    @Override
    public boolean convertFromJSON(JsonFile jsonFileHandler, Object object) {
        Object typeObject = ((JSONObject) object).get("type");
        Object rectangleObject = ((JSONObject) object).get("rectangle");
        Object groupBaseObject = ((JSONObject) object).get("groupBase");
        Object nameObject = ((JSONObject) object).get("name");

        if (typeObject == null || rectangleObject == null || groupBaseObject == null || nameObject == null) {
            return false;
        }

        Object rectangleObject_x = ((JSONObject) rectangleObject).get("x");
        Object rectangleObject_y = ((JSONObject) rectangleObject).get("y");
        Object rectangleObject_width = ((JSONObject) rectangleObject).get("width");
        Object rectangleObject_height = ((JSONObject) rectangleObject).get("height");

        if (rectangleObject_x == null || rectangleObject_y == null || rectangleObject_width == null
                || rectangleObject_height == null) {
            return false;
        }

        if (!((String) typeObject).equals(this.getClass().getSimpleName())) {
            return false;
        }

        try {
            this.rectangle = new Rectangle(((Long) rectangleObject_x).intValue(), ((Long) rectangleObject_y).intValue(),
                    ((Long) rectangleObject_width).intValue(), ((Long) rectangleObject_height).intValue());
            this.groupBase = (GroupBase) jsonFileHandler.idxToShapeCore(((Long) groupBaseObject).intValue());
            this.name = (String) nameObject;
        } catch (Exception e) {
            System.out.println("ERROR: Type casting fail.");
            return false;
        }

        if (this.rectangle == null || this.name == null) {
            return false;
        }

        return true;
    }
}
