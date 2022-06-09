package UML.UI.ButtonActions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import UML.Canvas;

public class UnGroup implements ActionListener {
    private Canvas canvas;

    public UnGroup() {
        this.canvas = Canvas.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.canvas.unGroup();
    }
}
