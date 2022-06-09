package UML.UI;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import UML.UI.ButtonActions.Save;
import UML.UI.ButtonActions.Open;
import UML.UI.ButtonActions.Group;
import UML.UI.ButtonActions.UnGroup;
import UML.UI.ButtonActions.ChangeObjectName;

public class MenuBar extends JMenuBar {
    private JMenu[] jMenus;
    private JMenuItem[] jMenuItems;

    public MenuBar() {
        this.jMenus = new JMenu[2];
        this.jMenus[0] = new JMenu("File");
        this.add(this.jMenus[0]);

        this.jMenuItems = new JMenuItem[2];
        this.jMenuItems[0] = new JMenuItem("Save");
        this.jMenuItems[0].addActionListener(new Save());
        this.jMenus[0].add(this.jMenuItems[0]);

        this.jMenuItems[1] = new JMenuItem("Open");
        this.jMenuItems[1].addActionListener(new Open());
        this.jMenus[0].add(this.jMenuItems[1]);

        this.jMenus[1] = new JMenu("Edit");
        this.add(this.jMenus[1]);

        this.jMenuItems = new JMenuItem[3];
        this.jMenuItems[0] = new JMenuItem("Group");
        this.jMenuItems[0].addActionListener(new Group());
        this.jMenus[1].add(this.jMenuItems[0]);

        this.jMenuItems[1] = new JMenuItem("UnGroup");
        this.jMenuItems[1].addActionListener(new UnGroup());
        this.jMenus[1].add(this.jMenuItems[1]);

        this.jMenuItems[2] = new JMenuItem("change object name");
        this.jMenuItems[2].addActionListener(new ChangeObjectName());
        this.jMenus[1].add(this.jMenuItems[2]);
    }
}
