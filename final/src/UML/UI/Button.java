package UML.UI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import UML.Modes.MouseMode;
import UML.UI.ButtonActions.Action;

public class Button extends JButton {
    public MouseMode mode;
    private ImageIcon imageIcon;

    public Button(String buttonName, String imageIconFileName, MouseMode mode) {
        this.mode = mode;
        this.setToolTipText(buttonName);

        this.imageIcon = new ImageIcon(imageIconFileName);
        this.setIcon(this.imageIcon);
        this.setBackground(Color.WHITE);

        Dimension dimension = new Dimension(this.imageIcon.getIconWidth(), this.imageIcon.getIconHeight());
        this.setMaximumSize(dimension);

        this.setFocusable(false);
        this.setBorderPainted(false);

        this.addActionListener(new Action());
    }

    public void set() {
        this.setBackground(Color.GRAY);
    }

    public void reset() {
        this.setBackground(Color.WHITE);
    }
}
