package UMLWindow.UMLCanvases;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjectsContainer;

/**
 * controll the window center part (UML objects level),
 * this JPanel is also known as the CANVAS
 */
public class UMLCanvas extends JPanel {
    /** Parent Component */
    private TheWindow ptw;

    // mouseDragged
    private boolean canDrag, draging;
    private MouseInputAdapter mia;

    private UMLObjectsContainer umlocter;

    public UMLCanvas(TheWindow ptw_, UMLObjectsContainer umlocter_) {
        this.ptw = ptw_;
        this.umlocter = umlocter_;

        this.canDrag = false;
        this.draging = false;

        this.mia = new MouseInputAdapter() {
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

        this.addMouseListener(this.mia);
        this.addMouseMotionListener(this.mia);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        this.umlocter.paintComponentWorkhouse(g);
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

    private void dragSetStatus(Point p) {
        this.draging = true;
        this.umlocter.dragSetStatus(p);
    }

    private void releasedResetStatus() {
        this.draging = false;
        this.umlocter.releasedResetStatus();
    }

    public void repaintAll() {
        this.repaint();
    }
}
