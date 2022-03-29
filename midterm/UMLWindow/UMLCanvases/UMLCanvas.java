package UMLWindow.UMLCanvases;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjects.UMLObjectCore;

/**
 * controll the window center part (UML objects level),
 * this JPanel is also known as the CANVAS
 */
public class UMLCanvas extends JPanel {
    /** Parent Component */
    private TheWindow ptw;

    public ArrayList<UMLObjectCore> umloc;
    /**
     * the uml editor requirement is 0-99 (lower shallower, higher deeper)
     * but nextDepth count start 0 and add up
     */
    public int nextDepth;

    // mouseClicked
    public ArrayList<UMLObjectCore> prev_select_umloc;
    public ArrayList<UMLObjectCore> curr_select_umloc;

    // mouseDragged
    private boolean canDrag, draging;
    /** drag start point */
    public Point dragSP;
    public UMLObjectCore curr_drag_umloc;

    private MouseInputAdapter mia = new MouseInputAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            super.mouseClicked(e);
            if (e.getButton() == MouseEvent.BUTTON1) {
                canvasClickedEvent(e.getPoint());
            }
        };

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (canDrag) {
                canvasDraggedEvent(e.getPoint());
            }
        };

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            super.mousePressed(e);
            if (e.getButton() == MouseEvent.BUTTON1) {
                canDrag = true;
            } else {
                canDrag = false;
            }
            canvasPressedEvent(e.getPoint());
        };

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            canvasReleasedEvent(e.getPoint());
        };
    };

    public UMLCanvas(TheWindow tw) {
        this.ptw = tw;
        this.umloc = new ArrayList<UMLObjectCore>();
        this.nextDepth = 0;
        this.prev_select_umloc = new ArrayList<UMLObjectCore>();
        this.curr_select_umloc = new ArrayList<UMLObjectCore>();
        this.addMouseListener(this.mia);
        this.addMouseMotionListener(this.mia);
        this.canDrag = false;
        this.draging = false;
        this.dragSP = null;
        this.curr_drag_umloc = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for (int i = 0; i < this.umloc.size(); i++) {
            UMLObjectCore curr_umloc = this.umloc.get(i);
            curr_umloc.drawBasicWorkhouse(g);
            curr_umloc.drawAdvancedWorkhouse(g);
            curr_umloc.drawSelectionWorkhouse(g);
        }
    }

    private void canvasClickedEvent(Point p) {
        this.ptw.clickedEventWorkHouse(p);
        System.out.println("clickedEvent: " + p);
        this.repaintAll();
    }

    private void canvasDraggedEvent(Point p) {
        if (!this.draging) {
            this.dragSetStatus(p);
            this.ptw.draggedEventWorkHouse(p);
        }
        System.out.println("draggedEvent: " + p);
        this.repaintAll();
    }

    private void canvasPressedEvent(Point p) {
        this.ptw.pressedEventWorkHouse(p);
        System.out.println("pressedEvent: " + p);
        this.repaintAll();
    }

    private void canvasReleasedEvent(Point p) {
        if (this.draging) {
            this.ptw.releasedEventWorkHouse(p);
        }
        System.out.println("releasedEvent: " + p);
        this.releasedResetStatus();
        this.repaintAll();
    }

    public void manipulateGroup() {
        Point minP = (Point) this.curr_select_umloc.get(0).startCoord.clone();
        Point maxP = (Point) this.curr_select_umloc.get(0).getCrossCoord(-1).clone();
        UMLObjectCore outer_umloc = new UMLObjectCore("", minP, this.nextDepth);
        this.nextDepth += 1;
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
        this.umloc.add(outer_umloc);
        this.repaintAll();
    }

    public void manipulateUnGroup(UMLObjectCore outer_umloc) {
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
        this.repaintAll();
    }

    private void dragSetStatus(Point p) {
        this.draging = true;
        this.dragSP = p;
        this.curr_drag_umloc = null;
    }

    private void releasedResetStatus() {
        this.draging = false;
        this.dragSP = null;
        this.curr_drag_umloc = null;
    }

    public void repaintAll() {
        this.repaint();
    }
}
