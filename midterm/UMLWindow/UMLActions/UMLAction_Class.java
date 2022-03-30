package UMLWindow.UMLActions;

import java.awt.Point;
import javax.swing.ImageIcon;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjectsContainer;

public class UMLAction_Class extends UMLActionCore {
    public UMLAction_Class(ImageIcon ii1, ImageIcon ii2, TheWindow tw, UMLObjectsContainer umlocter_, int ssIdx_) {
        super(ii1, ii2, tw, umlocter_, ssIdx_);
    }

    @Override
    protected void setIIName() {
        this.iiDescription = "Class";
    }

    @Override
    public void clickedEvent(Point p) {
        this.umlocter.clickedEvent_Class(p);
    }
}
