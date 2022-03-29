package UMLWindow.UMLObjects;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;

/** the sub-class of umloc, typically is basic component */
public abstract class UMLObject extends UMLObjectCore {
    // the basic member
    protected int selectBoxLength;
    public int lastSelectedSide;

    // the advanced member
    public UMLLine umll[];
    /** string array for UMLLine */
    public String[] umll_sa;

    public UMLObject(String name_, Point startCoord_, int depth_,
            int height_, int width_) {
        super(name_, startCoord_, depth_);
        this.height = height_;
        this.width = width_;
        this.selectBoxLength = 10;
        this.lastSelectedSide = -1;
        this.umll = new UMLLine[4];
        for (int i = 0; i < 4; i++) {
            this.umll[i] = null;
        }
        this.umll_sa = new String[] {
                "../pic/2.association.base.png",
                "../pic/3.generalization.base.png",
                "../pic/4.composition.base.png",
        };
    }

    @Override
    public void drawBasicWorkhouse(Graphics g) {
        // self overlay others
        g.setColor(Color.WHITE);
        g.fillRect(this.startCoord.x, this.startCoord.y, this.width, this.getTrueHeight());
        g.setColor(Color.BLACK);
        super.drawBasicWorkhouse(g);
    }

    @Override
    public void drawAdvancedWorkhouse(Graphics g) {
        super.drawAdvancedWorkhouse(g);
        // the 4-line if there is
        for (int i = 0; i < 4; i++) {
            if (this.umll[i] != null && this.umll[i].umlo != null) {
                Point start_p = this.getSelectBoxBaseCoord(i);
                Point end_p = this.umll[i].umlo.getSelectBoxBaseCoord(this.umll[i].targetSide);
                if (start_p != null && end_p != null) {
                    this.umll[i].drawLineWorkhouse(g, new Point(start_p.x, start_p.y), new Point(end_p.x, end_p.y));
                }
            }
        }
    }

    @Override
    public void drawSelectionWorkhouse(Graphics g) {
        if (this.isSelecting) {
            // cross lines
            for (int i = 0; i < 2; i++) {
                Point p[] = new Point[2];
                p[0] = this.getCrossCoord(i + 0);
                p[1] = this.getCrossCoord(i + 2);
                g.drawLine(p[0].x, p[0].y, p[1].x, p[1].y);
            }
            // 4-line anchor rect
            for (int i = 0; i < 4; i++) {
                Point selectBoxStartCoord = this.getSelectBoxStartCoord(i);
                g.fillRect(selectBoxStartCoord.x, selectBoxStartCoord.y,
                        this.selectBoxLength, this.selectBoxLength);
            }
        }
    }

    /**
     * one of the four select box base coordinate
     * which also the exactly line point of UML-line
     * 0: top corner
     * 1: right corner
     * 2: bottom corner
     * 3: left corner
     */
    protected Point getSelectBoxBaseCoord(int side_) {
        Point selectBoxBaseCoord = null;
        Point midCoord = this.getMiddleCoord();
        switch (side_) {
            case 0: // top corner
                selectBoxBaseCoord = new Point(midCoord.x, this.startCoord.y);
                break;
            case 1: // right corner
                selectBoxBaseCoord = new Point(this.getCrossCoord(-1).x, midCoord.y);
                break;
            case 2: // bottom corner
                selectBoxBaseCoord = new Point(midCoord.x, this.getCrossCoord(-1).y);
                break;
            case 3: // left corner
                selectBoxBaseCoord = new Point(this.startCoord.x + 1, midCoord.y);
                break;
            default:
                selectBoxBaseCoord = new Point(0, 0);
                break;
        }
        return selectBoxBaseCoord;
    }

    /**
     * one of the four select box start coordinate (came from base coord)
     * 0: top corner
     * 1: right corner
     * 2: bottom corner
     * 3: left corner
     */
    protected Point getSelectBoxStartCoord(int side_) {
        Point selectBoxBaseCoord = this.getSelectBoxBaseCoord(side_);
        Point selectBoxStartCoord = null;
        switch (side_) {
            case 0: // top corner
                selectBoxStartCoord = new Point(selectBoxBaseCoord.x - this.mulRatio(this.selectBoxLength, (1.0 / 2.0)),
                        selectBoxBaseCoord.y - this.selectBoxLength);
                break;
            case 1: // right corner
                selectBoxStartCoord = new Point(selectBoxBaseCoord.x,
                        selectBoxBaseCoord.y - this.mulRatio(this.selectBoxLength, (1.0 / 2.0)));
                break;
            case 2: // bottom corner
                selectBoxStartCoord = new Point(selectBoxBaseCoord.x - this.mulRatio(this.selectBoxLength, (1.0 / 2.0)),
                        selectBoxBaseCoord.y);
                break;
            case 3: // left corner
                selectBoxStartCoord = new Point(selectBoxBaseCoord.x - this.selectBoxLength,
                        selectBoxBaseCoord.y - this.mulRatio(this.selectBoxLength, (1.0 / 2.0)));
                break;
            default:
                selectBoxStartCoord = new Point(0, 0);
                break;
        }
        return selectBoxStartCoord;
    }

    /**
     * check point is on which side
     * return 0 left contain the v
     * return 1 right contain the v
     * return 2 down contain the v
     * return 3 up contain the v
     */
    protected void containPointSide(Point v) {
        Boolean x, y;
        x = (v.x * this.getTrueHeight() + v.y * this.width) >= 0 ? true : false;
        y = (v.x * (-1) * this.getTrueHeight() + v.y * this.width) >= 0 ? true : false;
        int result = -1;
        if (x && y) {
            // bottom
            result = 2;
        } else if (x && !y) {
            // right
            result = 1;
        } else if (!x && y) {
            // left
            result = 3;
        } else {
            // top
            result = 0;
        }
        this.lastSelectedSide = result;
        System.out.println("\tselection side: " + this.lastSelectedSide);
    }
}
