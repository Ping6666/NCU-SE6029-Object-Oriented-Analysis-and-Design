package UML.Objects;

import java.awt.Point;

/**
 * Normalization: on the ObjectBase and Line.
 */
public class Port {
    public enum Side {
        Outside, // any obj.: point was not inside
        Inside, // composite: point was inside, but this obj. has no port
        TopPort, // class or usecase obj.: point was on top port
        RightPort, // class or usecase obj.: point was on right port
        BottomPort, // class or usecase obj.: point was on bottom port
        LeftPort, // class or usecase obj.: point was on left port
        FrontPort, // line obj.: point was on front port (aka. start)
        BackPort, // line obj.: point was on back port (aka. end)
    };

    protected ObjectBase objectBase;
    protected Side side;
    protected Point point;

    public Port(ObjectBase objectBase, Side side) {
        this.objectBase = objectBase;
        this.side = side;
    }

    public ObjectBase getObjectBase() {
        return this.objectBase;
    }

    public Point getPoint() {
        return this.objectBase.getBoxBasePoint(this.side);
    }

    public void autoAdjust(Point centerPoint) {
        this.side = this.objectBase.containPointSide(centerPoint);
    }
}
