package UML.Objects;

import org.json.simple.JSONObject;

import java.io.File;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.util.Map;
import java.util.HashMap;

import javax.imageio.ImageIO;

import UML.JsonFile;
import UML.Objects.Port.Side;

public class LineBase extends ShapeCore {
    protected boolean lineAutoAdjust = true;

    protected String type;
    protected String fileName;
    protected BufferedImage bufferedImage;

    protected Port port1, port2, prevPort;
    protected Point startPoint, endPoint, tempPoint;

    public LineBase(String type, String fileName) {
        // do not use rectangle in any part of line
        super();

        this.type = type;
        this.fileName = fileName;
        this.bufferedImage = null;
        this.setBufferedImage();

        this.port1 = null;
        this.port2 = null;
        this.prevPort = null;

        this.startPoint = null;
        this.endPoint = null;
        this.tempPoint = null;
    }

    public LineBase(String type, String fileName, Port port) {
        // do not use rectangle in any part of line
        super(null);

        this.type = type;
        this.fileName = fileName;
        this.bufferedImage = null;
        this.setBufferedImage();

        this.port1 = port;
        this.port2 = null;
        this.prevPort = null;

        this.startPoint = null;
        this.endPoint = null;
        this.tempPoint = null;
    }

    protected void setBufferedImage() {
        try {
            if (this.fileName == null) {
                throw new NullPointerException();
            }
            this.bufferedImage = ImageIO.read(new File(this.fileName));
        } catch (Exception e) {
            System.out.println("ERROR: ImageIO.read fail, " + e);
        }
    }

    @Override
    public void draw(Graphics g) {
        /* check draw valid variable */
        this.computePoint();
        if (this.bufferedImage == null || this.startPoint == null || this.endPoint == null) {
            System.out.println("ERROR: Fail to draw line, null pointer");
            return;
        } else if (this.startPoint == this.endPoint) {
            System.out.println("ERROR: Fail to draw line, start and end are same");
            return;
        }

        /* prepare buffered image */
        BufferedImage bi_clone = new BufferedImage(this.bufferedImage.getWidth(),
                this.bufferedImage.getHeight(), this.bufferedImage.getType());
        Graphics2D g2d = bi_clone.createGraphics();
        g2d.drawImage(this.bufferedImage, 0, 0, null);
        g2d.dispose();

        /* BufferedImage rotate */
        Point vector = new Point(this.endPoint.x - this.startPoint.x,
                this.endPoint.y - this.startPoint.y);
        double adjust_r = vector.x < 0 ? 0 : Math.PI;
        AffineTransform at_ri = AffineTransform.getRotateInstance(
                adjust_r + Math.atan((double) vector.y / (double) vector.x),
                (1.0 / 2.0) * this.bufferedImage.getWidth(null),
                (1.0 / 2.0) * this.bufferedImage.getHeight(null));
        AffineTransformOp at_ri_op = new AffineTransformOp(at_ri,
                AffineTransformOp.TYPE_BILINEAR);

        /* draw the line arrow part */
        // sometime will raise reaching length <= 0 error while draw the image
        // vector transform with multiplier
        double distance = vector.distance(new Point(0, 0));
        Point v_1 = new Point((int) ((15.0 * vector.x) / distance), (int) ((15.0 * vector.y) / distance));
        Point picCoord = new Point(this.endPoint.x - v_1.x - 15, this.endPoint.y - v_1.y - 15);
        try {
            g.drawImage(at_ri_op.filter(bi_clone, null), picCoord.x, picCoord.y, null);
        } catch (Exception e) {
            System.out.println("ERROR: op filter transformed width <= 0.");
            return;
        }
        g.drawLine(this.startPoint.x, this.startPoint.y,
                this.endPoint.x - (int) (1.8 * v_1.x), this.endPoint.y - (int) (1.8 * v_1.y));
        this.drawWorkHouse(g);
    }

    @Override
    protected void drawWorkHouse(Graphics g) {
        /* is selected draw perpendicular bisector */
        if (this.beSelected) {
            double LineRatio = 10;
            Point vector = new Point(this.endPoint.x - this.startPoint.x,
                    this.endPoint.y - this.startPoint.y);
            double distance = vector.distance(new Point(0, 0));
            Point center = this.getCenterPoint();
            Point v_2 = new Point((int) (LineRatio * (double) vector.x / distance),
                    (int) (LineRatio * (double) vector.y / distance));
            g.drawLine(center.x, center.y, center.x + v_2.y, center.y - v_2.x);
            g.drawLine(center.x, center.y, center.x - v_2.y, center.y + v_2.x);
        }
    }

    @Override
    protected Point getCenterPoint() {
        Point center = new Point(0, 0);
        center.x = (this.startPoint.x + this.endPoint.x) / 2;
        center.y = (this.startPoint.y + this.endPoint.y) / 2;
        return center;
    }

    protected void computePoint() {
        if (this.lineAutoAdjust) {
            if (this.port1 != null && this.port2 != null) {
                this.port1.autoAdjust(this.port2.objectBase.getCenterPoint());
                this.port2.autoAdjust(this.port1.objectBase.getCenterPoint());
            }
        }
        this.startPoint = (this.port1 != null) ? this.port1.getPoint() : this.tempPoint;
        this.endPoint = (this.port2 != null) ? this.port2.getPoint() : this.tempPoint;
    }

    protected double computeDistance(Point p) {
        this.computePoint();
        if (this.startPoint == null || this.endPoint == null) {
            return Double.POSITIVE_INFINITY;
        } else if (((this.startPoint.x - p.x) * (this.endPoint.x - p.x) - 10 > 0)
                || ((this.startPoint.y - p.y) * (this.endPoint.y - p.y) - 10 > 0)) {
            return Double.POSITIVE_INFINITY;
        }
        double distanceToPoint = Math.min(this.startPoint.distance(p), this.endPoint.distance(p));
        Line2D line = new Line2D.Double(this.startPoint, this.endPoint);
        double distanceToLine = line.ptLineDist(p);
        return Math.min(distanceToPoint, distanceToLine);
    }

    /**
     * check point is on which side
     */
    @Override
    public Side containPointSide(Point p) {
        this.computePoint();
        if (this.startPoint.distance(p) < this.endPoint.distance(p)) {
            if (this.port1 != null) {
                this.prevPort = this.port1;
            }
            return Side.FrontPort;
        } else {
            if (this.port2 != null) {
                this.prevPort = this.port2;
            }
            return Side.BackPort;
        }
    }

    @Override
    public Side containPoint(Point p) {
        double lineWidth = 5.0;
        if (this.computeDistance(p) < lineWidth) {
            Side result = this.containPointSide(p);
            System.out.println("\t\tselection side: " + result.toString());
            return result;
        }
        return Side.Outside;
    }

    /** in lien base must have to override this method */
    @Override
    public boolean insideRect(Rectangle rectangle) {
        this.computePoint();
        if (this.bufferedImage == null || this.startPoint == null || this.endPoint == null) {
            return false;
        } else if (rectangle.contains(this.startPoint) && rectangle.contains(this.endPoint)) {
            return true;
        }
        return false;
    }

    /** in lien base must have to override this method */
    @Override
    public void moveObject(Point p) {
        if (p == null) {
            return;
        }
        this.tempPoint = (Point) p.clone();
        Side side = this.containPointSide(p);
        switch (side) {
            case FrontPort:
                if (this.port2 != null) {
                    this.port1 = null;
                }
                break;
            case BackPort:
                if (this.port1 != null) {
                    this.port2 = null;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Port getPort(Side side) {
        Port port = null;
        switch (side) {
            case FrontPort:
                port = this.port1;
                break;
            case BackPort:
                port = this.port2;
                break;
            default:
                break;
        }
        return port;
    }

    public boolean setTargetPort(Port port) {
        if (port == null || (this.port1 == null && this.port2 == null)) {
            return false;
        }
        if (this.port1 == null) {
            this.port1 = (port.objectBase != this.port2.objectBase) ? port : this.prevPort;
            if (this.port1 != null) {
                return true;
            }
        } else {
            this.port2 = (port.objectBase != this.port1.objectBase) ? port : this.prevPort;
            if (this.port2 != null) {
                return true;
            }
        }
        return false;
    }

    public void resetTargetPort() {
        if (this.prevPort != null) {
            this.setTargetPort(this.prevPort);
        } else {
            System.out.println("ERROR: line prevPort setting loss.");
        }
    }

    @Override
    public Object convertToJSON() {

        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("type", this.type);
        map.put("groupBase", this.canvas.ShapeCoreToIndex(this.groupBase));
        map.put("port1objectBase", this.canvas.ShapeCoreToIndex(this.port1.objectBase));
        map.put("port2objectBase", this.canvas.ShapeCoreToIndex(this.port2.objectBase));
        map.put("port1", this.port1.objectBase.SideToIndex(this.port1.side));
        map.put("port2", this.port1.objectBase.SideToIndex(this.port2.side));

        // System.out.println(new JSONObject(map).toJSONString());
        return new JSONObject(map);
    }

    @Override
    public boolean convertFromJSON(JsonFile jsonFileHandler, Object object) {
        Object typeObject = ((JSONObject) object).get("type");
        Object groupBaseObject = ((JSONObject) object).get("groupBase");
        Object port1ObjectBaseObject = ((JSONObject) object).get("port1objectBase");
        Object port2ObjectBaseObject = ((JSONObject) object).get("port2objectBase");
        Object port1Object = ((JSONObject) object).get("port1");
        Object port2Object = ((JSONObject) object).get("port2");

        if (typeObject == null || groupBaseObject == null || port1ObjectBaseObject == null ||
                port2ObjectBaseObject == null || port1Object == null || port2Object == null) {
            return false;
        }

        try {
            this.groupBase = (GroupBase) jsonFileHandler.idxToShapeCore(((Long) groupBaseObject).intValue());

            ObjectBase tmpObjectBase_1 = (ObjectBase) jsonFileHandler
                    .idxToShapeCore(((Long) port1ObjectBaseObject).intValue());
            this.port1 = tmpObjectBase_1.ports[((Long) port1Object).intValue()];

            ObjectBase tmpObjectBase_2 = (ObjectBase) jsonFileHandler
                    .idxToShapeCore(((Long) port2ObjectBaseObject).intValue());
            this.port2 = tmpObjectBase_2.ports[((Long) port2Object).intValue()];
        } catch (Exception e) {
            System.out.println("ERROR: Type casting fail.");
            return false;
        }

        if (this.port1 == null || this.port2 == null) {
            return false;
        }

        return true;
    }
}
