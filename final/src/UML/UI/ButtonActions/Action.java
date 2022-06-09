package UML.UI.ButtonActions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import UML.Canvas;
import UML.UI.Button;

public class Action implements ActionListener {
    private Canvas canvas;

    public Action() {
        this.canvas = Canvas.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // change the mode and current button
        Button currButton = (Button) e.getSource();
        if (this.canvas.currButton == currButton) {
            // unselect current button
            this.canvas.unmountListener();
        } else {
            // select a (another) button
            if (this.canvas.currButton != null) {
                this.canvas.unmountListener();
            }
            this.canvas.mountListener(currButton);
        }
    }
}
