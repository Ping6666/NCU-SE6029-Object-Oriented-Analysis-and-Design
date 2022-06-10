package UML;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JPanel;

import org.json.simple.JSONObject;

import UML.UI.Button;
import UML.Objects.GroupBase;
import UML.Objects.ShapeCore;
import UML.Objects.Port.Side;

/**
 * Singleton Design Pattern, use when a object guarantee to be unique in a
 * program.
 */
public class Canvas extends JPanel {
    /* Singleton: guarantee only one copy in the program */
    private static Canvas canvas;

    public Button currButton;
    public JsonFile jsonFileHandler;

    // the first the older / deeper or said with lower depth number
    private ArrayList<ShapeCore> shapeCores;
    private ArrayList<ShapeCore> currSelectedShapeCores;
    private ArrayList<ShapeCore> prevSelectedShapeCores;

    private Canvas() {
        this.currButton = null;
        this.shapeCores = new ArrayList<ShapeCore>();
        this.currSelectedShapeCores = new ArrayList<ShapeCore>();
        this.prevSelectedShapeCores = new ArrayList<ShapeCore>();
        this.jsonFileHandler = null;

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /*
     * dangerous on multi-thread programming (use final, synchronized or volatile)
     */
    public static Canvas getInstance() {
        if (canvas == null) {
            canvas = new Canvas();
        }
        return canvas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < this.shapeCores.size(); i++) {
            this.shapeCores.get(i).draw(g);
        }
    }

    public void addObject(ShapeCore shapeCore) {
        if (shapeCore != null) {
            this.resetSelection();
            this.shapeCores.add(shapeCore);
            this.repaintWorkHouse();
        }
    }

    /**
     * 
     * @param point
     * @param onlyObjectBase true: only select on object base; false: otherwise.
     * @return
     */
    public Side getSingleObject(Point point, boolean onlyObjectBase) {
        this.resetSelection();

        System.out.println("\n\tStart check select on single object");
        for (int i = this.shapeCores.size() - 1; i >= 0; i--) {
            ShapeCore currShapeCore = this.shapeCores.get(i);
            Side side = currShapeCore.containPoint(point);
            if (side != Side.Outside) {
                if (onlyObjectBase && (side == Side.Inside || side == Side.FrontPort || side == Side.BackPort)) {
                    // in onlyObjectBase mode
                    // exclude composite and line object
                    continue;
                }

                this.currSelectedShapeCores.add(currShapeCore);
                this.setSelectionStateWorkHouse(true);

                System.out.println("\tGot one!!!");
                return side;
            }
        }

        System.out.println("\tFail to get one!!!\n");
        return Side.Outside;
    }

    public boolean getMultiObject(Rectangle rectangle) {
        if (rectangle.isEmpty()) {
            // This should be seen as click event "USECASE C.1 ALTERNATIVE 4.A"
            System.out.println("\n\tThis should be seen as click event, return now.");
            this.resetSelection();
            return false;
        }

        System.out.println("\n\tStart check select on multiple object");
        for (int i = this.shapeCores.size() - 1; i >= 0; i--) {
            if (this.shapeCores.get(i).insideRect(rectangle)) {
                this.currSelectedShapeCores.add(this.shapeCores.get(i));
            }
        }

        if (this.currSelectedShapeCores.size() != 0) {
            this.setSelectionStateWorkHouse(true);
            System.out.println("\tGot multiple!!!");
            return true;
        } else {
            // "USECASE C.2 ALTERNATIVE 4.A"
            this.restoreSelection();
            System.out.println("\tFail to get one, reset now!!!\n");
            return false;
        }
    }

    public void moveAllObjectWorkHouse(Point p) {
        for (ShapeCore currShapeCore : this.shapeCores) {
            if (currShapeCore.rectangle != null) {
                // make sure this obj. is not line object
                if (currShapeCore.groupBase == null) {
                    // make sure this obj. is not in a group object
                    currShapeCore.moveObject(p);
                }
            }
        }
        this.repaintWorkHouse();
    }

    public void moveObjectWorkHouse(ShapeCore currShapeCore, Point p) {
        // guarantee only object can be selected
        if (currShapeCore == null) {
            return;
        }
        currShapeCore.moveObject(p);
        this.repaintWorkHouse();
    }

    /*
     * this method is quite dangerous and high time complexity,
     * precondition need to guarentee obj. is been selected.
     */
    public int ShapeCoreToIndex(ShapeCore tmpShapeCore) {
        if (tmpShapeCore == null || !this.currSelectedShapeCores.contains(tmpShapeCore)) {
            return -1;
        }

        for (int i = 0; i < this.currSelectedShapeCores.size(); i++) {
            if (this.currSelectedShapeCores.get(i) == tmpShapeCore) {
                return i;
            }
        }
        return -1;
    }

    private HashSet<ShapeCore> getGroupHashSet(ShapeCore groupShapeCore) {
        HashSet<ShapeCore> hashSetShapeCores = new HashSet<>();
        if (groupShapeCore == null) {
            return hashSetShapeCores;
        }

        /* get object under same group */
        ArrayList<ShapeCore> tmpShapeCores = groupShapeCore.getGroupShapeCores();
        if (tmpShapeCores != null) {
            for (ShapeCore tmpShapeCore : tmpShapeCores) {
                hashSetShapeCores.add(tmpShapeCore);
                hashSetShapeCores.addAll(this.getGroupHashSet(tmpShapeCore));
            }
        }
        return hashSetShapeCores;
    }

    // TODO fix some BUG
    // when only select on the group base object will got BUG
    // but use multi. select on all member in the group base object can work well
    /* this method is quite dangerous and high time complexity */
    private ArrayList<ShapeCore> copyShapeCoresWorkHouse() {
        System.out.println("\tStart copy sequence.");
        /* check valid obj. sequence */
        ArrayList<ShapeCore> removerShapeCores = this.getInvalidShapeCores();
        for (ShapeCore tmpShapeCore : removerShapeCores) {
            tmpShapeCore.setSelectionState(false);
            this.currSelectedShapeCores.remove(tmpShapeCore);
        }
        this.repaintWorkHouse();

        /* precondition check sequence */
        if (this.currSelectedShapeCores.isEmpty()) {
            System.out.println("\tCopy fail, precondition doesn't satisfy.");
            return null;
        }

        HashSet<ShapeCore> hashSetShapeCores = new HashSet<ShapeCore>();

        /* add valid obj. and the group obj. in it */
        for (ShapeCore tmpShapeCore : this.currSelectedShapeCores) {
            hashSetShapeCores.add(tmpShapeCore);
            hashSetShapeCores.addAll(this.getGroupHashSet(tmpShapeCore));

            /* fix some BUG when copy on line object */
            if (tmpShapeCore.rectangle == null) {
                ShapeCore frontPortShapeCore = tmpShapeCore.getPort(Side.FrontPort).getObjectBase();
                hashSetShapeCores.add(frontPortShapeCore);
                hashSetShapeCores.addAll(this.getGroupHashSet(frontPortShapeCore));

                ShapeCore backPortShapeCore = tmpShapeCore.getPort(Side.BackPort).getObjectBase();
                hashSetShapeCores.add(backPortShapeCore);
                hashSetShapeCores.addAll(this.getGroupHashSet(backPortShapeCore));
            }
        }

        int length = hashSetShapeCores.size();

        /* adjust valid obj. order */
        this.currSelectedShapeCores.clear();
        ArrayList<ShapeCore> copyShapeCores = new ArrayList<ShapeCore>();
        for (ShapeCore tmpShapeCore : this.shapeCores) {
            if (hashSetShapeCores.contains(tmpShapeCore)) {
                copyShapeCores.add(tmpShapeCore);
                this.currSelectedShapeCores.add(tmpShapeCore);
            }
        }

        if (length != this.currSelectedShapeCores.size()) {
            System.out.println("\tERROR: copy poison by line object.\n");
        } else {
            System.out.println("\tCopy successful!!!");
        }
        return copyShapeCores;
    }

    private void removeCutShapeCoresWorkHouse(ArrayList<ShapeCore> tmpShapeCores) {
        if (tmpShapeCores == null) {
            return;
        }
        for (ShapeCore tmpShapeCore : tmpShapeCores) {
            this.shapeCores.remove(tmpShapeCore);
        }
        this.repaintWorkHouse();
        // this.resetSelection();
    }

    public JSONObject cutShapeCores() {
        ArrayList<ShapeCore> cutShapeCores = this.copyShapeCoresWorkHouse();
        this.removeCutShapeCoresWorkHouse(cutShapeCores);
        return jsonFileHandler.shapeCoresToJSON(cutShapeCores);
    }

    public JSONObject copyShapeCores() {
        return jsonFileHandler.shapeCoresToJSON(this.copyShapeCoresWorkHouse());
    }

    public void pasteShapeCores(JSONObject tmpShapeCores) {
        ArrayList<ShapeCore> tmpPasteShapeCores = jsonFileHandler.openWorkHouse(tmpShapeCores);
        if (tmpPasteShapeCores == null) {
            return;
        }

        for (ShapeCore tmpPasteShapeCore : tmpPasteShapeCores) {
            this.addObject(tmpPasteShapeCore);
        }
        this.resetSelection();
        this.repaintWorkHouse();
    }

    public void save() {
        ArrayList<ShapeCore> tmpShapeCores = new ArrayList<ShapeCore>();
        this.currSelectedShapeCores.clear();
        for (ShapeCore tmpShapeCore : this.shapeCores) {
            tmpShapeCores.add(tmpShapeCore);
            this.currSelectedShapeCores.add(tmpShapeCore);
        }
        jsonFileHandler.saveWorkHouse(tmpShapeCores);
        this.currSelectedShapeCores.clear();
        this.repaintWorkHouse();
    }

    public void open() {
        JSONObject jsonObject = jsonFileHandler.readJSON();
        ArrayList<ShapeCore> loadShapeCores = jsonFileHandler.openWorkHouse(jsonObject);
        if (loadShapeCores == null) {
            return;
        }

        this.shapeCores.clear();
        for (ShapeCore tmpShapeCore : loadShapeCores) {
            this.shapeCores.add(tmpShapeCore);
        }
        this.repaintWorkHouse();
    }

    /** invalid for group or ccp */
    private ArrayList<ShapeCore> getInvalidShapeCores() {
        ArrayList<ShapeCore> invalidShapeCores = new ArrayList<ShapeCore>();
        for (ShapeCore tmpShapeCore : this.currSelectedShapeCores) {
            if (tmpShapeCore.rectangle == null) {
                // line obj.: check two side / port was also be selected.
                ShapeCore frontShapeCore = tmpShapeCore.getPort(Side.FrontPort).getObjectBase();
                ShapeCore backShapeCore = tmpShapeCore.getPort(Side.BackPort).getObjectBase();
                if (!this.currSelectedShapeCores.contains(frontShapeCore)
                        || !this.currSelectedShapeCores.contains(backShapeCore)) {
                    invalidShapeCores.add(tmpShapeCore);
                    System.out.println("\t\tALERT: Grouping remove partial line obj.!");
                }
            } else if (tmpShapeCore.groupBase != null) {
                // other obj.: that at least under one composite obj.
                invalidShapeCores.add(tmpShapeCore);
                System.out.println("\t\tALERT: Grouping remove obj. under composite obj.!");
            }
        }
        return invalidShapeCores;
    }

    /*
     * UML the group objects is not necessary to be which is not in any group,
     * but in this program only the objects which are not in any group can be group.
     */
    public void group() {
        System.out.println("\tStart grouping sequence.");

        /* check valid obj. sequence */
        ArrayList<ShapeCore> removerShapeCores = this.getInvalidShapeCores();
        for (ShapeCore tmpShapeCore : removerShapeCores) {
            tmpShapeCore.setSelectionState(false);
            this.currSelectedShapeCores.remove(tmpShapeCore);
        }
        this.repaintWorkHouse();

        /* precondition check sequence */
        if (this.currSelectedShapeCores.size() <= 1) {
            // "USECASE D.1" precondition
            System.out.println("\tGroup fail, D.1 precondition doesn't satisfy.");
            return;
        }

        /* grouping sequence */
        ArrayList<ShapeCore> tmpShapeCores = new ArrayList<ShapeCore>();
        Rectangle rectangle = null;
        for (int i = 0; i < this.currSelectedShapeCores.size(); i++) {
            ShapeCore shapeCore = this.currSelectedShapeCores.get(i);
            rectangle = (rectangle == null && shapeCore.rectangle != null) ? shapeCore.rectangle : rectangle;
            if (shapeCore != null && shapeCore.rectangle != null) {
                // do not consider line obj. and error
                if (shapeCore.groupBase != null && !this.currSelectedShapeCores.contains(shapeCore.groupBase)) {
                    // self define condition all obj. is not in any group
                    // or in the group which is also being selected now
                    System.out.println("\tGroup fail, condition doesn't satisfy (all obj. should not in group).");
                    return;
                } else {
                    if (shapeCore.rectangle != null) {
                        // line obj. rectangle is null
                        rectangle = rectangle.union(shapeCore.rectangle);
                    }
                    tmpShapeCores.add(shapeCore);
                }
            }
        }

        // make group obj. a little bit larger than all selected obj. in it
        rectangle.setRect(rectangle.x - 15, rectangle.y - 15, rectangle.width + 30, rectangle.height + 30);
        this.addObject(new GroupBase(rectangle, tmpShapeCores));
        System.out.println("\tGroup successful!!!");
    }

    /*
     * UML the ungroup composite object is not necessary to be the upper one,
     * but in this program only the most upper composite object can be ungroup.
     */
    public void unGroup() {
        System.out.println("\tStart ungrouping sequence.");
        if (this.currSelectedShapeCores.size() != 1) {
            // "USECASE D.2" precondition
            System.out.println("\tGroup fail, D.2 precondition doesn't satisfy.");
            return;
        }

        ShapeCore shapeCore = this.getShapeCore(0);
        if (shapeCore != null && shapeCore.groupBase == null) {
            if (shapeCore.resetGroupBase()) {
                this.shapeCores.remove(shapeCore);
                System.out.println("\tUnGroup successful!!!");
                this.repaintWorkHouse();
                return;
            } else {
                System.out.println("\tGroup fail, condition doesn't satisfy (not a composite).");
                return;
            }
        } else {
            System.out.println("\tGroup fail, condition doesn't satisfy (not the most upper one).");
            return;
        }
    }

    /**
     * danger operator need to check first on
     * 1. currSelectedShapeCores size == 1
     * 2. getSingleObject and return > 0
     * 
     * @return
     */
    public ShapeCore getShapeCore(int index) {
        if (this.currSelectedShapeCores.size() <= index) {
            // check selected number
            return null;
        }
        ShapeCore shapeCore = this.currSelectedShapeCores.get(index);
        System.out.println("\tSelected obj.'s is: " + shapeCore.getClass() + "\n");
        return shapeCore;
    }

    public String getObjectName() {
        ShapeCore currShapeCore = this.getShapeCore(0);
        if (currShapeCore == null) {
            return null;
        }
        return currShapeCore.getName();
    }

    public void setObjectName(String name) {
        ShapeCore currShapeCore = this.getShapeCore(0);
        if (currShapeCore == null) {
            System.out.println("Change ObjectCore's obj. name fail.");
            return;
        }
        currShapeCore.changeName(name);
        System.out.println("Change ObjectCore's obj. name successful.");
        this.repaintWorkHouse();
    }

    public void mountListener(Button button) {
        this.unmountListener(); // safety first
        if (button != null) {
            System.out.println("\nCurrent Mode: " + button.getToolTipText());
            this.currButton = button;
            this.currButton.set();
            this.addMouseListener(this.currButton.mode);
            this.addMouseMotionListener(this.currButton.mode);
        }
    }

    public void unmountListener() {
        if (this.currButton != null) {
            this.currButton.reset();
            this.removeMouseListener(this.currButton.mode);
            this.removeMouseMotionListener(this.currButton.mode);
        }
        this.currButton = null;
    }

    private void resetSelection() {
        this.storeSelection();
        this.currSelectedShapeCores.clear();
        this.repaintWorkHouse();
    }

    private void storeSelection() {
        // curr -> prev
        this.prevSelectedShapeCores.clear();
        for (int i = 0; i < this.currSelectedShapeCores.size(); i++) {
            this.prevSelectedShapeCores.add(this.currSelectedShapeCores.get(i));
        }
        this.setSelectionStateWorkHouse(false);
    }

    private void restoreSelection() {
        // prev -> curr, reset prev
        this.currSelectedShapeCores.clear();
        for (int i = 0; i < this.prevSelectedShapeCores.size(); i++) {
            this.currSelectedShapeCores.add(this.prevSelectedShapeCores.get(i));
        }
        this.setSelectionStateWorkHouse(true);
    }

    public void popLastShapeCores() {
        this.shapeCores.remove(this.shapeCores.size() - 1);
    }

    private void setSelectionStateWorkHouse(boolean check) {
        for (int i = 0; i < this.currSelectedShapeCores.size(); i++) {
            this.currSelectedShapeCores.get(i).setSelectionState(check);
        }
        this.repaintWorkHouse();
    }

    public void repaintWorkHouse() {
        this.repaint();
    }
}
