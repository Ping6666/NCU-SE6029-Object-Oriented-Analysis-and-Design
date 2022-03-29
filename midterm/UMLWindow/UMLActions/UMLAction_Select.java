package UMLWindow.UMLActions;

import java.awt.Point;
import javax.swing.ImageIcon;

import UMLWindow.TheWindow;

public class UMLAction_Select extends UMLActionCore {
    public UMLAction_Select(ImageIcon ii1, ImageIcon ii2, TheWindow tw, int ssIdx_) {
        super(ii1, ii2, tw, ssIdx_);
    }

    @Override
    protected void setIIName() {
        this.iiDescription = "Select";
    }

    @Override
    public void clickedEvent(Point p) {
        this.selectionCheck(p, true);
    }

    @Override
    public void draggedEvent(Point p) {
        if (this.ptw.umlc.curr_select_umloc.size() == 1 &&
                this.ptw.umlc.curr_select_umloc.get(0).containPoint(p)) {
            this.ptw.umlc.curr_drag_umloc = this.ptw.umlc.curr_select_umloc.get(0);
        } else {
            this.storeSelectionStatus();
            this.selectionClear();
        }
    }

    @Override
    public void releasedEvent(Point p) {
        if (this.ptw.umlc.curr_drag_umloc != null) {
            this.moveObjectWorkhouse(this.ptw.umlc.curr_drag_umloc,
                    new Point(p.x - this.ptw.umlc.dragSP.x, p.y - this.ptw.umlc.dragSP.y), true);
        } else {
            this.selectionCheck(p, false);
        }
    }
}
