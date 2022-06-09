package UML.Objects.ObjectFactorys;

import java.awt.Point;
import java.awt.Rectangle;

import UML.Objects.Port;
import UML.Objects.LineBase;
import UML.Objects.ObjectBase;
import UML.Objects.ObjectBases.Object_Class;
import UML.Objects.ObjectBases.Object_Usecase;

public class ObjectFactory_Default implements ObjectFactoryInterface {

    public ObjectBase createObjectCore(ObjectType objectType, Point pointLeftTop) {
        Rectangle rectangle = null;
        switch (objectType) {
            case Object_Class:
                // sample size for class object
                rectangle = new Rectangle(pointLeftTop.x, pointLeftTop.y, 100, 90);
                return new Object_Class(rectangle);
            case Object_Usecase:
                // sample size for usecase object
                rectangle = new Rectangle(pointLeftTop.x, pointLeftTop.y, 100, 50);
                return new Object_Usecase(rectangle);
            default:
                break;
        }
        return null;
    }

    public LineBase createLineBase(ObjectType objectType, Port port) {
        if (port == null) {
            return null;
        }
        switch (objectType) {
            case Line_Association:
                return new LineBase("association", "../assets/pic/2.association.base.png", port);
            case Line_Generalization:
                return new LineBase("generalization", "../assets/pic/3.generalization.base.png", port);
            case Line_Composition:
                return new LineBase("composition", "../assets/pic/4.composition.base.png", port);
            default:
                break;
        }
        return null;
    }
}
