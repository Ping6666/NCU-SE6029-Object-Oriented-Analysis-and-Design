package UML;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

import UML.UI.Panel;
import UML.UI.MenuBar;
import UML.Modes.KeyActions.CCPMode;

public class Frame extends JFrame {
    private Panel panel; // contain buttons
    private MenuBar menuBar; // contain menu and menuitem
    private Canvas canvas; // contain all shape (line, object, composite)

    public Frame() {
        canvas = Canvas.getInstance();
        this.canvas.addKeyListener(new CCPMode());
        this.canvas.jsonFileHandler = new JsonFile(); // prevent java.lang.StackOverflowError

        menuBar = new MenuBar();
        panel = new Panel();

        Container container = this.getContentPane();
        container.add(menuBar, BorderLayout.NORTH);
        container.add(panel, BorderLayout.WEST);
        container.add(canvas, BorderLayout.CENTER);

        this.setTitle("UML editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(1200, 800);
        this.setVisible(true);
    }
}
