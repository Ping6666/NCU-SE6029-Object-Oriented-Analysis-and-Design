package UMLWindow;

import java.awt.Point;
import java.awt.Graphics;
import java.util.ArrayList;

import UMLWindow.UMLCanvases.UMLCanvas;
import UMLWindow.UMLObjects.UMLObjectCore;
import UMLWindow.UMLObjects.UMLObject;
import UMLWindow.UMLObjects.UMLObject_Class;
import UMLWindow.UMLObjects.UMLObject_Usecase;
import UMLWindow.UMLActions.UMLAction_Line;

public class UMLObjectsContainer {
    // all objects
    private ArrayList<UMLObjectCore> umloc;
    /**
     * the uml editor requirement is 0-99 (lower shallower, higher deeper)
     * but nextDepth count start 0 and add up
     */
    private int nextDepth;

    // mouseClicked
    private ArrayList<UMLObjectCore> prev_select_umloc;
    private ArrayList<UMLObjectCore> curr_select_umloc;

    // mouseDragged
    private Point dragSP;
    private UMLObjectCore curr_drag_umloc;

    public UMLObjectsContainer() {
        this.umloc = new ArrayList<UMLObjectCore>();
        this.prev_select_umloc = new ArrayList<UMLObjectCore>();
        this.curr_select_umloc = new ArrayList<UMLObjectCore>();
        this.nextDepth = 0;
        this.dragSP = null;
        this.curr_drag_umloc = null;
    }

    public void paintComponentWorkhouse(Graphics g) {
        for (int i = 0; i < this.umloc.size(); i++) {
            UMLObjectCore curr_umloc = this.umloc.get(i);
            curr_umloc.drawBasicWorkhouse(g);
            curr_umloc.drawAdvancedWorkhouse(g);
            curr_umloc.drawSelectionWorkhouse(g);
        }
    }

    public void clickedEvent_Select(Point p) {
        this.selectionCheck(p, true);
    }

    public void draggedEvent_Select(Point p) {
        if (this.curr_select_umloc.size() == 1 &&
                this.curr_select_umloc.get(0).containPoint(p)) {
            this.curr_drag_umloc = this.curr_select_umloc.get(0);
        } else {
            this.storeSelectionStatus();
            this.selectionClear();
        }
    }

    public void releasedEvent_Select(Point p) {
        if (this.curr_drag_umloc != null) {
            this.moveObjectWorkhouse(this.curr_drag_umloc,
                    new Point(p.x - this.dragSP.x, p.y - this.dragSP.y), true);
        } else {
            this.selectionCheck(p, false);
        }
    }

    public void draggedEvent_Line(Point p) {
        try {
            UMLObjectCore tmp_umloc = this.singularContainCheck(p);
            if (tmp_umloc != null && ((UMLObject) tmp_umloc).lastSelectedSide >= 0) {
                this.curr_drag_umloc = tmp_umloc;
            }
        } catch (Exception e) {
            System.out.println("BAD THINGS HAPPEN, when calling draggedEvent.");
            System.out.println("Maybe set UML line on a UMLObjectCore (aka. Composite)");
            System.out.println(e);
        }
    }

    public void releasedEvent_Line(Point p, UMLAction_Line umlal) {
        try {
            if (this.curr_drag_umloc != null
                    && ((UMLObject) this.curr_drag_umloc).lastSelectedSide >= 0) {
                UMLObjectCore tmp_umloc = this.singularContainCheck(p);
                if (tmp_umloc != null && ((UMLObject) tmp_umloc).lastSelectedSide >= 0
                        && this.curr_drag_umloc != tmp_umloc) {
                    umlal.setUMLLine((UMLObject) this.curr_drag_umloc, (UMLObject) tmp_umloc);
                    // umlal.setUMLLine((UMLObject) tmp_umloc, null);
                }
            }
        } catch (Exception e) {
            System.out.println("BAD THINGS HAPPEN, when calling releasedEvent.");
            System.out.println("Maybe set UML line on a UMLObjectCore (aka. Composite)");
            System.out.println(e);
        }
    }

    public void clickedEvent_Class(Point p) {
        this.umloc.add(new UMLObject_Class("", p, this.nextDepth++, 30, 100));
    }

    public void clickedEvent_Usecase(Point p) {
        this.umloc.add(new UMLObject_Usecase("", p, this.nextDepth++, 50, 100));
    }

    /** move the current object and which also inside the composite if there is */
    private void moveObjectWorkhouse(UMLObjectCore curr_move_umloc, Point p, boolean checkComposite) {
        if (curr_move_umloc == null) {
            return;
        }
        if (checkComposite && curr_move_umloc.getClass() == UMLObjectCore.class) {
            for (int i = 0; i < this.umloc.size(); i++) {
                UMLObjectCore curr_umloc = this.umloc.get(i);
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
    private void selectionCheck(Point p, boolean singularCheck) {
        this.selectionClear();
        if (singularCheck) {
            // select singular objects
            UMLObjectCore tmp_umloc = this.singularContainCheck(p);
            if (tmp_umloc != null) {
                System.out.println("select 1 umloc");
                this.curr_select_umloc.add(tmp_umloc);
            }
        } else {
            // select multiple objects
            this.curr_select_umloc = multipleContainCheck(this.dragSP, p);
            System.out.print("\tmulti selection: ");
            System.out.print(this.curr_select_umloc.size());
            System.out.println(" obj. have been selected.");
            if (this.curr_select_umloc.size() <= 0) {
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
    private ArrayList<UMLObjectCore> multipleContainCheck(Point p1, Point p2) {
        Point p[] = new Point[2];
        p[0] = new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
        p[1] = new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
        ArrayList<UMLObjectCore> select_umloc = new ArrayList<UMLObjectCore>();
        for (int i = 0; i < this.umloc.size(); i++) {
            UMLObjectCore curr_umloc = this.umloc.get(i);
            if (curr_umloc.canSetSelecting() && curr_umloc.insideRect(p[0], p[1])) {
                select_umloc.add(curr_umloc);
            }
        }
        return select_umloc;
    }

    /** check which one contain p and which is shallowest */
    private UMLObjectCore singularContainCheck(Point p) {
        UMLObjectCore select_umloc = null;
        for (int i = 0; i < this.umloc.size(); i++) {
            UMLObjectCore curr_umloc = this.umloc.get(i);
            if (curr_umloc.canSetSelecting() && curr_umloc.containPoint(p)) {
                if ((select_umloc == null) || (select_umloc.depth < curr_umloc.depth)) {
                    select_umloc = curr_umloc;
                }
            }
        }
        return select_umloc;
    }

    private void storeSelectionStatus() {
        this.prev_select_umloc.clear();
        for (int i = 0; i < this.curr_select_umloc.size(); i++) {
            this.prev_select_umloc.add(this.curr_select_umloc.get(i));
        }
    }

    private void restoreSelectionStatus() {
        this.curr_select_umloc.clear();
        for (int i = 0; i < this.prev_select_umloc.size(); i++) {
            this.curr_select_umloc.add(this.prev_select_umloc.get(i));
        }
        this.prev_select_umloc.clear();
    }

    private void selectionSet() {
        for (int i = 0; i < this.curr_select_umloc.size(); i++) {
            if (this.curr_select_umloc.get(i) != null) {
                this.curr_select_umloc.get(i).isSelecting = true;
            }
        }
    }

    private void selectionClear() {
        for (int i = 0; i < this.curr_select_umloc.size(); i++) {
            if (this.curr_select_umloc.get(i) != null) {
                this.curr_select_umloc.get(i).isSelecting = false;
            }
        }
        this.curr_select_umloc.clear();
    }

    public void setGroup() {
        if (this.curr_select_umloc.size() > 1) {
            this.manipulateGroup();
            this.selectionSet();
        }
    }

    public void setUnGroup() {
        if (this.curr_select_umloc.size() == 1) {
            this.manipulateUnGroup(this.curr_select_umloc.get(0));
        }
    }

    private void manipulateGroup() {
        Point minP = (Point) this.curr_select_umloc.get(0).startCoord.clone();
        Point maxP = (Point) this.curr_select_umloc.get(0).getCrossCoord(-1).clone();
        UMLObjectCore outer_umloc = new UMLObjectCore("", minP, this.nextDepth++);
        for (int i = 0; i < this.curr_select_umloc.size(); i++) {
            UMLObjectCore curr_umloc = this.curr_select_umloc.get(i);
            curr_umloc.setOuterComposite(outer_umloc);
            minP.x = Math.min(minP.x, curr_umloc.startCoord.x);
            minP.y = Math.min(minP.y, curr_umloc.startCoord.y);
            maxP.x = Math.max(maxP.x, curr_umloc.getCrossCoord(-1).x);
            maxP.y = Math.max(maxP.y, curr_umloc.getCrossCoord(-1).y);
        }
        outer_umloc.startCoord = new Point(minP.x, minP.y);
        outer_umloc.moveObject(new Point(-15, -15));
        outer_umloc.width = maxP.x - minP.x + 30;
        outer_umloc.height = maxP.y - minP.y + 30;
        outer_umloc.setOuterComposite(outer_umloc);
        this.selectionClear();
        this.umloc.add(outer_umloc);
        this.curr_select_umloc.add(outer_umloc);
    }

    private void manipulateUnGroup(UMLObjectCore outer_umloc) {
        int removeIdx = -1;
        for (int i = 0; i < this.umloc.size(); i++) {
            UMLObjectCore curr_umloc = this.umloc.get(i);
            if (curr_umloc == outer_umloc) {
                removeIdx = i;
            } else {
                if (curr_umloc.outerComposite == outer_umloc) {
                    curr_umloc.setOuterComposite(null);
                    if (curr_umloc.getClass() == UMLObjectCore.class) {
                        curr_umloc.setOuterComposite(curr_umloc);
                    }
                }
            }
        }
        if (removeIdx >= 0 && (this.umloc.get(removeIdx)).getClass() == UMLObjectCore.class) {
            this.umloc.remove(removeIdx);
        }
    }

    public void dragSetStatus(Point p) {
        this.dragSP = p;
        this.curr_drag_umloc = null;
    }

    public void releasedResetStatus() {
        this.dragSP = null;
        this.curr_drag_umloc = null;
    }

    public void initNameWindow(UMLCanvas umlc_) {
        if (this.curr_select_umloc.size() == 1) {
            new NameWindow(umlc_, this.curr_select_umloc.get(0));
        }
    }
}
