package UMLWindow;

import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import UMLWindow.UMLActions.*;
import UMLWindow.UMLCanvases.UMLCanvas;

/** controll the window level */
public class TheWindow {
    private JFrame jf;
    private JPanel jp;
    private JMenuBar jmb;

    /** string array for JMenu */
    private String[] jm_sa;
    private JMenu jm[];

    /** string array for JMenuItem */
    private String[] jmi_sa;
    private JMenuItem jmi[];
    private ActionListener al0, al1, al2;

    /** string array for JLabel */
    private String[][] jl_sa;
    private UMLActionCore jl[];

    private UMLCanvas umlc;
    private UMLObjectsContainer umlocter;
    /** selection status */
    private int ssIdx;

    public TheWindow() {
        this.initVariables();
        this.initTheWindow();
    }

    private void initVariables() {
        this.jm_sa = new String[] {
                "File", "Edit"
        };
        this.jmi_sa = new String[] {
                "Group", "UnGroup", "change object name"
        };
        this.al0 = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setGroupWorkHouse();
            }
        };
        this.al1 = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setUnGroupWorkHouse();
            }
        };
        this.al2 = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initNameWindowWorkHouse();
            }
        };
        this.jl_sa = new String[][] {
                { "../pic/1.select.png", "../pic/1.select.b.png" },
                { "../pic/2.association.png", "../pic/2.association.b.png" },
                { "../pic/3.generalization.png", "../pic/3.generalization.b.png" },
                { "../pic/4.composition.png", "../pic/4.composition.b.png" },
                { "../pic/5.class.png", "../pic/5.class.b.png" },
                { "../pic/6.usecase.png", "../pic/6.usecase.b.png" }
        };
        this.ssIdx = 0;
        this.umlocter = new UMLObjectsContainer();
    }

    private void initTheWindow() {
        this.jf = new JFrame("UML editor");

        // NORTH part of the window
        this.jmb = new JMenuBar();
        this.jm = new JMenu[this.jm_sa.length];
        for (int i = 0; i < this.jm_sa.length; i++) {
            this.jm[i] = new JMenu(this.jm_sa[i]);
            this.jmb.add(this.jm[i]);
        }
        this.jmi = new JMenuItem[this.jmi_sa.length];
        for (int i = 0; i < this.jmi_sa.length; i++) {
            this.jmi[i] = new JMenuItem(this.jmi_sa[i]);
            this.jm[1].add(this.jmi[i]); // add on JMenu "edit"
        }
        this.jmi[0].addActionListener(this.al0); // add on JMenuItem "Group"
        this.jmi[1].addActionListener(this.al1); // add on JMenuItem "UnGroup"
        this.jmi[2].addActionListener(this.al2); // add on JMenuItem "change object name"
        this.jf.add(this.jmb, BorderLayout.NORTH);

        // WEST part of the window
        this.jp = new JPanel();
        this.jp.setLayout(new BoxLayout(this.jp, BoxLayout.Y_AXIS));
        // this.jl = new UMLActions[this.jl_sa.length];
        this.jl = new UMLActionCore[this.jl_sa.length];
        this.jl[0] = (UMLActionCore) new UMLAction_Select(
                new ImageIcon(this.jl_sa[0][0]),
                new ImageIcon(this.jl_sa[0][1]), this, this.umlocter, 0);
        this.jl[1] = (UMLActionCore) new UMLAction_Line_Association(
                new ImageIcon(this.jl_sa[1][0]),
                new ImageIcon(this.jl_sa[1][1]), this, this.umlocter, 1);
        this.jl[2] = (UMLActionCore) new UMLAction_Line_Generalization(
                new ImageIcon(this.jl_sa[2][0]),
                new ImageIcon(this.jl_sa[2][1]), this, this.umlocter, 2);
        this.jl[3] = (UMLActionCore) new UMLAction_Line_Composition(
                new ImageIcon(this.jl_sa[3][0]),
                new ImageIcon(this.jl_sa[3][1]), this, this.umlocter, 3);
        this.jl[4] = (UMLActionCore) new UMLAction_Class(
                new ImageIcon(this.jl_sa[4][0]),
                new ImageIcon(this.jl_sa[4][1]), this, this.umlocter, 4);
        this.jl[5] = (UMLActionCore) new UMLAction_Usecase(
                new ImageIcon(this.jl_sa[5][0]),
                new ImageIcon(this.jl_sa[5][1]), this, this.umlocter, 5);
        for (int i = 0; i < this.jl_sa.length; i++) {
            this.jp.add(this.jl[i].getJLabel());
        }
        this.jf.add(this.jp, BorderLayout.WEST);

        // CENTER part of the window
        this.umlc = new UMLCanvas(this, this.umlocter);
        this.jf.add(this.umlc, BorderLayout.CENTER);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the window and exit program
        this.jf.pack(); // show all things at once properly
        this.jf.setSize(1200, 800);
        this.jf.setVisible(true);
    }

    public void clickedEventWorkHouse(Point p) {
        this.jl[this.ssIdx].clickedEvent(p);
    }

    public void draggedEventWorkHouse(Point p) {
        this.jl[this.ssIdx].draggedEvent(p);
    }

    public void pressedEventWorkHouse(Point p) {
        this.jl[this.ssIdx].pressedEvent(p);
    }

    public void releasedEventWorkHouse(Point p) {
        this.jl[this.ssIdx].releasedEvent(p);
    }

    private void setGroupWorkHouse() {
        this.umlocter.setGroup();
        this.umlc.repaintAll();
    }

    private void setUnGroupWorkHouse() {
        this.umlocter.setUnGroup();
        this.umlc.repaintAll();
    }

    private void initNameWindowWorkHouse() {
        this.umlocter.initNameWindow(this.umlc);
    }

    public void changeSS(int ssIdx_) {
        manipulateSS(this.ssIdx, false);
        this.ssIdx = ssIdx_;
        manipulateSS(this.ssIdx, true);
        System.out.println("SelectStatus: " + this.jl[ssIdx_].iiDescription);
    }

    private void manipulateSS(int ssIdx_, boolean selection) {
        // this may be a dangerous movement
        this.jl[ssIdx_].changeII(selection);
    }
}
