package UMLWindow.UMLActions;

import javax.swing.ImageIcon;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjects.UMLLine;
import UMLWindow.UMLObjects.UMLObject;

public class UMLAction_Line_Composition extends UMLAction_Line {
    public UMLAction_Line_Composition(ImageIcon ii1, ImageIcon ii2, TheWindow tw, int ssIdx_) {
        super(ii1, ii2, tw, ssIdx_);
    }

    @Override
    protected void setIIName() {
        this.iiDescription = "CompositionLine";
    }

    @Override
    protected void setUMLLine(UMLObject umlo1, UMLObject umlo2) {
        if (umlo1 == null) {
            return;
        }
        if (umlo2 == null) {
            umlo1.umll[umlo1.lastSelectedSide] = null;
            return;
        }
        umlo1.umll[umlo1.lastSelectedSide] = new UMLLine(umlo2,
                umlo2.lastSelectedSide, umlo1.umll_sa[2]);
    }
}
