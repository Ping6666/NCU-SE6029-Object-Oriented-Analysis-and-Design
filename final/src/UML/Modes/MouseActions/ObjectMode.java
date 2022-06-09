package UML.Modes.MouseActions;

import java.awt.event.MouseEvent;

import UML.Objects.ObjectFactorys.ObjectFactory_Default;
import UML.Modes.MouseMode;
import UML.Objects.ObjectFactorys.ObjectFactoryInterface;

public class ObjectMode extends MouseMode {
    private ObjectFactoryInterface objectFactory;
    private ObjectFactoryInterface.ObjectType objectType;

    public ObjectMode(ObjectFactoryInterface.ObjectType objectType) {
        super();
        this.objectFactory = new ObjectFactory_Default();
        this.objectType = objectType;
    }

    /** mouseClicked maybe can do much better? */
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        this.canvas.addObject(this.objectFactory.createObjectCore(this.objectType, this.currPoint));
    }
}
