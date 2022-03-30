package UMLWindow.UMLActions;

import java.awt.Point;
import javax.swing.ImageIcon;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjectsContainer;
import UMLWindow.UMLObjects.UMLObject;

public abstract class UMLAction_Line extends UMLActionCore {

    public UMLAction_Line(ImageIcon ii1, ImageIcon ii2, TheWindow tw, UMLObjectsContainer umlocter_, int ssIdx_) {
        super(ii1, ii2, tw, umlocter_, ssIdx_);
    }

    @Override
    public void draggedEvent(Point p) {
        this.umlocter.draggedEvent_Line(p);
    }

    @Override
    public void releasedEvent(Point p) {
        this.umlocter.releasedEvent_Line(p, this);
    }

    /** set UML line */
    public abstract void setUMLLine(UMLObject umlo1, UMLObject umlo2);
}
