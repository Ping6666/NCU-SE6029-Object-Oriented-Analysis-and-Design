package UML.UI.ButtonActions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import UML.Canvas;

public class Group implements ActionListener {
    private Canvas canvas;

    public Group() {
        this.canvas = Canvas.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.canvas.group();
    }
}
