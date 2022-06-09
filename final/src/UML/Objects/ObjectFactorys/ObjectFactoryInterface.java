package UML.Objects.ObjectFactorys;

import java.awt.Point;

import UML.Objects.Port;
import UML.Objects.LineBase;
import UML.Objects.ShapeCore;

/**
 * Factory Design Pattern, encapsulation in object create.
 */
public interface ObjectFactoryInterface {
    public enum ObjectType {
        Object_Group,
        Object_Class,
        Object_Usecase,
        Line_Association,
        Line_Generalization,
        Line_Composition,
    }

    public ShapeCore createObjectCore(ObjectType objectType, Point pointLeftTop);

    public LineBase createLineBase(ObjectType objectType, Port port);
}
