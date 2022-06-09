package UML.Modes;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import UML.Canvas;

public abstract class KeyMode implements KeyListener {
    protected Canvas canvas;

    public KeyMode() {
        this.canvas = Canvas.getInstance();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
