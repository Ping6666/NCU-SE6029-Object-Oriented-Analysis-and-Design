package UML.Objects.ObjectFactorys;

import java.awt.Point;

import UML.Objects.Port;
import UML.Objects.LineBase;
import UML.Objects.GroupBase;
import UML.Objects.ShapeCore;
import UML.Objects.ObjectBases.Object_Class;
import UML.Objects.ObjectBases.Object_Usecase;

public class ObjectFactory_Load implements ObjectFactoryInterface {
    public ShapeCore createObjectCore(ObjectType objectType, Point pointLeftTop) {
        switch (objectType) {
            case Object_Group:
                return new GroupBase();
            case Object_Class:
                return new Object_Class();
            case Object_Usecase:
                return new Object_Usecase();
            default:
                break;
        }
        return null;
    }

    public LineBase createLineBase(ObjectType objectType, Port port) {
        switch (objectType) {
            case Line_Association:
                return new LineBase("association", "../assets/pic/2.association.base.png");
            case Line_Generalization:
                return new LineBase("generalization", "../assets/pic/3.generalization.base.png");
            case Line_Composition:
                return new LineBase("composition", "../assets/pic/4.composition.base.png");
            default:
                break;
        }
        return null;
    }
}
