package UMLWindow.UMLActions;

import java.awt.Point;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.event.MouseInputAdapter;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjects.UMLObjectCore;

/** controll the window west part (UML action level) */
public abstract class UMLActionCore {
    /** Parent Component THE window */
    protected TheWindow ptw;

    // Self Component
    protected JLabel jl;
    protected MouseInputAdapter mia;
    /** ii: ii select true, false */
    protected ImageIcon ii_st, ii_sf;
    protected String iiDescription;
    protected int ssIdx;

    public UMLActionCore(ImageIcon ii1, ImageIcon ii2, TheWindow tw, int ssIdx_) {
        this.ii_sf = ii1;
        this.ii_st = ii2;
        this.ptw = tw;
        this.ssIdx = ssIdx_;
        this.setIIName();
        this.initUMLAction();
    }

    private void initUMLAction() {
        this.initUMLAction("");
    }

    private void initUMLAction(String name) {
        this.jl = new JLabel(name, this.ii_sf, JLabel.CENTER);
        this.jl.setVerticalTextPosition(JLabel.CENTER);
        this.jl.setHorizontalTextPosition(JLabel.LEFT);
        this.jl.setAlignmentX(JLabel.RIGHT_ALIGNMENT);

        this.mia = new MouseInputAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    ptw.changeSS(ssIdx);
                }
            };
        };

        this.jl.setToolTipText(iiDescription); // change after about 1 sec
        this.jl.addMouseListener(this.mia);
    }

    public JLabel getJLabel() {
        return this.jl;
    }

    public void changeII(boolean selection) {
        this.jl.setIcon(selection ? this.ii_st : this.ii_sf);
    }

    /** set the ImageIcon showing text and the number */
    protected abstract void setIIName();

    /** do when THE JPanel (aka. the CANVAS) been mouse clicked on */
    public void clickedEvent(Point p) {
        return;
    }

    /** do when THE JPanel (aka. the CANVAS) been mouse dragged on */
    public void draggedEvent(Point p) {
        return;
    }

    /** do when THE JPanel (aka. the CANVAS) been mouse pressed on */
    public void pressedEvent(Point p) {
        return;
    }

    /** do when THE JPanel (aka. the CANVAS) been mouse released on */
    public void releasedEvent(Point p) {
        return;
    }

    protected void moveObjectWorkhouse(UMLObjectCore curr_move_umloc, Point p, boolean checkComposite) {
        if (curr_move_umloc == null) {
            return;
        }
        if (checkComposite && curr_move_umloc.getClass() == UMLObjectCore.class) {
            for (int i = 0; i < this.ptw.umlc.umloc.size(); i++) {
                UMLObjectCore curr_umloc = this.ptw.umlc.umloc.get(i);
                if (curr_umloc == null) {
                    continue;
                }
                if (curr_umloc.outerComposite == curr_move_umloc) {
                    if (curr_umloc.getClass() == UMLObjectCore.class && curr_umloc != curr_move_umloc) {
                        this.moveObjectWorkhouse(curr_umloc, p, true);
                        curr_umloc.moveObject(p);
                    } else {
                        curr_umloc.moveObject(p);
                    }
                }
            }
        } else {
            curr_move_umloc.moveObject(p);
        }
    }

    /**
     * change curr_select_umloc and selecting status
     */
    protected void selectionCheck(Point p, boolean singularCheck) {
        this.selectionClear();
        if (singularCheck) {
            // select singular objects
            UMLObjectCore tmp_umloc = this.singularContainCheck(p);
            if (tmp_umloc != null) {
                System.out.println("select 1 umloc");
                this.ptw.umlc.curr_select_umloc.add(tmp_umloc);
            }
        } else {
            // select multiple objects
            this.ptw.umlc.curr_select_umloc = multipleContainCheck(this.ptw.umlc.dragSP, p);
            System.out.print("\tmulti selection: ");
            System.out.print(this.ptw.umlc.curr_select_umloc.size());
            System.out.println(" obj. have been selected.");
            if (this.ptw.umlc.curr_select_umloc.size() <= 0) {
                // if didnot select on anything restore prev. select status
                this.restoreSelectionStatus();
            }
        }
        this.selectionSet();
    }

    /**
     * multi. objects contain check
     * return objects that is fully inside rect(p1, p2)
     */
    protected ArrayList<UMLObjectCore> multipleContainCheck(Point p1, Point p2) {
        Point p[] = new Point[2];
        p[0] = new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
        p[1] = new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
        ArrayList<UMLObjectCore> select_umloc = new ArrayList<UMLObjectCore>();
        for (int i = 0; i < this.ptw.umlc.umloc.size(); i++) {
            UMLObjectCore curr_umloc = this.ptw.umlc.umloc.get(i);
            if (curr_umloc.canSetSelecting() && curr_umloc.insideRect(p[0], p[1])) {
                select_umloc.add(curr_umloc);
            }
        }
        return select_umloc;
    }

    /** check which one contain p and which is shallowest */
    protected UMLObjectCore singularContainCheck(Point p) {
        UMLObjectCore select_umloc = null;
        for (int i = 0; i < this.ptw.umlc.umloc.size(); i++) {
            UMLObjectCore curr_umloc = this.ptw.umlc.umloc.get(i);
            if (curr_umloc.canSetSelecting() && curr_umloc.containPoint(p)) {
                if ((select_umloc == null) || (select_umloc.depth < curr_umloc.depth)) {
                    select_umloc = curr_umloc;
                }
            }
        }
        return select_umloc;
    }

    protected void storeSelectionStatus() {
        this.ptw.umlc.prev_select_umloc.clear();
        for (int i = 0; i < this.ptw.umlc.curr_select_umloc.size(); i++) {
            this.ptw.umlc.prev_select_umloc.add(this.ptw.umlc.curr_select_umloc.get(i));
        }
    }

    protected void restoreSelectionStatus() {
        this.ptw.umlc.curr_select_umloc.clear();
        for (int i = 0; i < this.ptw.umlc.prev_select_umloc.size(); i++) {
            this.ptw.umlc.curr_select_umloc.add(this.ptw.umlc.prev_select_umloc.get(i));
        }
        this.ptw.umlc.prev_select_umloc.clear();
    }

    protected void selectionSet() {
        for (int i = 0; i < this.ptw.umlc.curr_select_umloc.size(); i++) {
            if (this.ptw.umlc.curr_select_umloc.get(i) != null) {
                this.ptw.umlc.curr_select_umloc.get(i).isSelecting = true;
            }
        }
    }

    protected void selectionClear() {
        for (int i = 0; i < this.ptw.umlc.curr_select_umloc.size(); i++) {
            if (this.ptw.umlc.curr_select_umloc.get(i) != null) {
                this.ptw.umlc.curr_select_umloc.get(i).isSelecting = false;
            }
        }
        this.ptw.umlc.curr_select_umloc.clear();
    }
}
