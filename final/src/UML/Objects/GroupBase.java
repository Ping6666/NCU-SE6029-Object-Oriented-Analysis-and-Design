package UML.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import UML.JsonFile;
import UML.Objects.Port.Side;

/** group base or said composite */
public class GroupBase extends ShapeCore {
    protected String name;
    private ArrayList<ShapeCore> shapeCores;

    public GroupBase() {
        super();
        this.name = "";
        this.shapeCores = null;
    }

    public GroupBase(Rectangle rectangle, ArrayList<ShapeCore> shapeCores) {
        super(rectangle);
        this.name = "";
        this.shapeCores = shapeCores;
        this.setGroupBase();
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
            g.fillOval(this.rectangle.x + 5, this.rectangle.y + 5, 3, 3);
        }
    }

    /** group base has no side (line port) */
    @Override
    public Side containPointSide(Point v) {
        return Side.Inside;
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

    @Override
    public void moveObject(Point point) {
        this.rectangle.translate(point.x, point.y);
        for (int i = 0; i < this.shapeCores.size(); i++) {
            ShapeCore tmpShapeCore = this.shapeCores.get(i);
            if (tmpShapeCore.rectangle != null && tmpShapeCore.groupBase == this) {
                // not a line and is under this composite (in one level)
                tmpShapeCore.moveObject(point);
            }
        }
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

    @Override
    public ArrayList<ShapeCore> getGroupShapeCores() {
        return this.shapeCores;
    }

    private void setGroupBase() {
        if (this.shapeCores == null) {
            return;
        }

        for (int i = 0; i < this.shapeCores.size(); i++) {
            ShapeCore tmpShapeCore = this.shapeCores.get(i);
            if (tmpShapeCore.groupBase == null) {
                tmpShapeCore.groupBase = this;
            }
        }
    }

    @Override
    public boolean resetGroupBase() {
        if (this.shapeCores == null) {
            return false;
        }

        for (int i = 0; i < this.shapeCores.size(); i++) {
            ShapeCore tmpShapeCore = this.shapeCores.get(i);
            if (tmpShapeCore.groupBase == this) {
                tmpShapeCore.groupBase = null;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convertToJSON() {
        JSONArray jsonArray = new JSONArray();
        for (ShapeCore shapeCore : this.shapeCores) {
            jsonArray.add(this.canvas.ShapeCoreToIndex(shapeCore));
        }

        Map<Object, Object> rectangleMap = new HashMap<Object, Object>();
        rectangleMap.put("x", this.rectangle.x);
        rectangleMap.put("y", this.rectangle.y);
        rectangleMap.put("width", this.rectangle.width);
        rectangleMap.put("height", this.rectangle.height);

        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("type", this.getClass().getSimpleName());
        map.put("rectangle", rectangleMap);
        map.put("groupBase", this.canvas.ShapeCoreToIndex(this.groupBase));
        map.put("shapeCores", jsonArray);
        map.put("name", this.name);

        // System.out.println(new JSONObject(map).toJSONString());
        return new JSONObject(map);
    }

    @Override
    public boolean convertFromJSON(JsonFile jsonFileHandler, Object object) {
        Object typeObject = ((JSONObject) object).get("type");
        Object rectangleObject = ((JSONObject) object).get("rectangle");
        Object groupBaseObject = ((JSONObject) object).get("groupBase");
        Object shapeCoresObject = ((JSONObject) object).get("shapeCores");
        Object nameObject = ((JSONObject) object).get("name");

        if (typeObject == null || rectangleObject == null || groupBaseObject == null ||
                shapeCoresObject == null || nameObject == null) {
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

            this.shapeCores = new ArrayList<ShapeCore>();
            for (Object tmpJsonObject : (JSONArray) shapeCoresObject) {
                this.shapeCores.add(jsonFileHandler.idxToShapeCore(((Long) tmpJsonObject).intValue()));
            }
            this.setGroupBase();
        } catch (Exception e) {
            System.out.println("ERROR: Type casting fail.");
            return false;
        }

        if (this.rectangle == null || this.name == null || this.shapeCores == null || this.shapeCores.size() < 1) {
            return false;
        }

        return true;
    }
}
