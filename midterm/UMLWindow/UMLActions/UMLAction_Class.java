package UMLWindow.UMLActions;

import java.awt.Point;
import javax.swing.ImageIcon;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjects.UMLObject_Class;

public class UMLAction_Class extends UMLActionCore {
    public UMLAction_Class(ImageIcon ii1, ImageIcon ii2, TheWindow tw, int ssIdx_) {
        super(ii1, ii2, tw, ssIdx_);
    }

    @Override
    protected void setIIName() {
        this.iiDescription = "Class";
    }

    @Override
    public void clickedEvent(Point p) {
        this.ptw.umlc.umloc.add(new UMLObject_Class("", p, this.ptw.umlc.nextDepth++, 30, 100));
    }
}
