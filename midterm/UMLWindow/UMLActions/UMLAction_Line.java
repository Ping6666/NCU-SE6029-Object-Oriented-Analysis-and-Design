package UMLWindow.UMLActions;

import java.awt.Point;
import javax.swing.ImageIcon;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjects.UMLObject;
import UMLWindow.UMLObjects.UMLObjectCore;

public abstract class UMLAction_Line extends UMLActionCore {

    public UMLAction_Line(ImageIcon ii1, ImageIcon ii2, TheWindow tw, int ssIdx_) {
        super(ii1, ii2, tw, ssIdx_);
    }

    @Override
    public void draggedEvent(Point p) {
        try {
            UMLObjectCore tmp_umloc = this.singularContainCheck(p);
            if (tmp_umloc != null && ((UMLObject) tmp_umloc).lastSelectedSide >= 0) {
                this.ptw.umlc.curr_drag_umloc = tmp_umloc;
            }
        } catch (Exception e) {
            System.out.println("BAD THINGS HAPPEN, when calling draggedEvent.");
            System.out.println("Maybe set UML line on a UMLObjectCore (aka. Composite)");
            System.out.println(e);
        }
    }

    @Override
    public void releasedEvent(Point p) {
        try {
            if (this.ptw.umlc.curr_drag_umloc != null
                    && ((UMLObject) this.ptw.umlc.curr_drag_umloc).lastSelectedSide >= 0) {
                UMLObjectCore tmp_umloc = this.singularContainCheck(p);
                if (tmp_umloc != null && ((UMLObject) tmp_umloc).lastSelectedSide >= 0
                        && this.ptw.umlc.curr_drag_umloc != tmp_umloc) {
                    this.setUMLLine((UMLObject) this.ptw.umlc.curr_drag_umloc, (UMLObject) tmp_umloc);
                    this.setUMLLine((UMLObject) tmp_umloc, null);
                }
            }
        } catch (Exception e) {
            System.out.println("BAD THINGS HAPPEN, when calling releasedEvent.");
            System.out.println("Maybe set UML line on a UMLObjectCore (aka. Composite)");
            System.out.println(e);
        }
    }

    /** set UML line */
    protected abstract void setUMLLine(UMLObject umlo1, UMLObject umlo2);
}
