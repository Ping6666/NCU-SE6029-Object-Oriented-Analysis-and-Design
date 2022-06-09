package UML.Modes.MouseActions;

import java.awt.event.MouseEvent;

import UML.Objects.Port.Side;
import UML.Modes.MouseMode;
import UML.Objects.LineBase;
import UML.Objects.ShapeCore;
import UML.Objects.ObjectFactorys.ObjectFactory_Default;
import UML.Objects.ObjectFactorys.ObjectFactoryInterface;

public class LineMode extends MouseMode {
    private ObjectFactoryInterface objectFactory;
    private ObjectFactoryInterface.ObjectType objectType;

    private LineBase temporaryLine;

    public LineMode(ObjectFactoryInterface.ObjectType objectType) {
        super();
        this.objectFactory = new ObjectFactory_Default();
        this.objectType = objectType;
        this.temporaryLine = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        this.temporaryLine = null;
        Side side = this.canvas.getSingleObject(this.currPoint, true); // no line and composite obj.
        ShapeCore shapeCore = this.canvas.getShapeCore(0);
        if (side != Side.Outside && shapeCore != null) {
            this.temporaryLine = this.objectFactory.createLineBase(this.objectType, shapeCore.getPort(side));
            this.canvas.addObject(this.temporaryLine);
        } else {
            // "USECASE B.1 ALTERNATIVE 1.A"
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        this.canvas.moveObjectWorkHouse(this.temporaryLine, this.currPoint);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (this.temporaryLine != null) {
            Side side = this.canvas.getSingleObject(this.currPoint, true); // no line and composite obj.
            ShapeCore shapeCore = this.canvas.getShapeCore(0);
            if (side != Side.Outside && shapeCore != null) {
                if (!this.temporaryLine.setTargetPort(shapeCore.getPort(side))) {
                    // "USECASE B.1" rule 3 - another object
                    System.out.println("ERROR: pop out a bad line B.1 rule 3.");
                    this.canvas.popLastShapeCores();
                }
            } else {
                // "USECASE B.1 ALTERNATIVE 3.A"
                System.out.println("ERROR: pop out a bad line B.1 3.A.");
                this.canvas.popLastShapeCores();
            }
        }
    }
}
