package UMLWindow.UMLActions;

import javax.swing.ImageIcon;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjectsContainer;
import UMLWindow.UMLObjects.UMLObject;
import UMLWindow.UMLObjects.UMLObject_Line;

public class UMLAction_Line_Composition extends UMLAction_Line {
    public UMLAction_Line_Composition(ImageIcon ii1, ImageIcon ii2, TheWindow tw, UMLObjectsContainer umlocter_,
            int ssIdx_) {
        super(ii1, ii2, tw, umlocter_, ssIdx_);
    }

    @Override
    protected void setIIName() {
        this.iiDescription = "CompositionLine";
    }

    @Override
    public void setUMLLine(UMLObject umlo1, UMLObject umlo2) {
        if (umlo1 == null) {
            return;
        }
        if (umlo2 == null) {
            umlo1.umlol[umlo1.lastSelectedSide] = null;
            return;
        }
        umlo1.umlol[umlo1.lastSelectedSide] = new UMLObject_Line(umlo2,
                umlo2.lastSelectedSide, umlo1.umll_sa[2]);
    }
}
