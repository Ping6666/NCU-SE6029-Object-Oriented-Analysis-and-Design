package UMLWindow.UMLActions;

import java.awt.Point;
import javax.swing.ImageIcon;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjectsContainer;

public class UMLAction_Select extends UMLActionCore {
    public UMLAction_Select(ImageIcon ii1, ImageIcon ii2, TheWindow tw, UMLObjectsContainer umlocter_, int ssIdx_) {
        super(ii1, ii2, tw, umlocter_, ssIdx_);
    }

    @Override
    protected void setIIName() {
        this.iiDescription = "Select";
    }

    @Override
    public void clickedEvent(Point p) {
        this.umlocter.clickedEvent_Select(p);
    }

    @Override
    public void draggedEvent(Point p) {
        this.umlocter.draggedEvent_Select(p);
    }

    @Override
    public void releasedEvent(Point p) {
        this.umlocter.releasedEvent_Select(p);
    }
}
