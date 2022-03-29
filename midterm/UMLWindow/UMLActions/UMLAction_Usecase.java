package UMLWindow.UMLActions;

import java.awt.Point;
import javax.swing.ImageIcon;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjects.UMLObject_Usecase;

public class UMLAction_Usecase extends UMLActionCore {
    public UMLAction_Usecase(ImageIcon ii1, ImageIcon ii2, TheWindow tw, int ssIdx_) {
        super(ii1, ii2, tw, ssIdx_);
    }

    @Override
    protected void setIIName() {
        this.iiDescription = "Usecase";
    }

    @Override
    public void clickedEvent(Point p) {
        this.ptw.umlc.umloc.add(new UMLObject_Usecase("", p, this.ptw.umlc.nextDepth++, 50, 100));
    }
}
