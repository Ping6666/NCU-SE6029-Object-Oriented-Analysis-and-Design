package UMLWindow.UMLActions;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.event.MouseInputAdapter;

import UMLWindow.TheWindow;
import UMLWindow.UMLObjectsContainer;

/** controll the window west part (UML action level) */
public abstract class UMLActionCore {
    /** Parent Component THE window */
    protected TheWindow ptw;
    protected UMLObjectsContainer umlocter;

    // Self Component
    protected JLabel jl;
    protected MouseInputAdapter mia;
    /** ii: ii select true, false */
    protected ImageIcon ii_st, ii_sf;
    public String iiDescription;
    protected int ssIdx;

    public UMLActionCore(ImageIcon ii1, ImageIcon ii2, TheWindow tw, UMLObjectsContainer umlocter_, int ssIdx_) {
        this.ii_sf = ii1;
        this.ii_st = ii2;
        this.ptw = tw;
        this.umlocter = umlocter_;
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
}
