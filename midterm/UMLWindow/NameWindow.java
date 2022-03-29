package UMLWindow;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import UMLWindow.UMLCanvases.UMLCanvas;
import UMLWindow.UMLObjects.UMLObjectCore;

public class NameWindow {
    private JFrame jf;
    private JPanel jp1, jp2;
    private JTextArea jta;
    private JButton jb1, jb2;
    private ActionListener al1, al2;

    /** the only canvas */
    private UMLCanvas umlc;
    /** the object umloc or composite which was been selected */
    private UMLObjectCore umloc;

    public NameWindow(UMLCanvas umlc_, UMLObjectCore umloc_) {
        this.umlc = umlc_;
        this.umloc = umloc_;

        this.jf = new JFrame("UML change name");

        this.jta = new JTextArea(this.umloc.name);
        this.jp1 = new JPanel();
        this.jp1.add(this.jta);

        this.al1 = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setName();
            }
        };
        this.al2 = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeWindow();
            }
        };

        this.jb1 = new JButton("OK");
        this.jb1.addActionListener(this.al1);
        this.jb2 = new JButton("Cancel");
        this.jb2.addActionListener(this.al2);
        this.jp2 = new JPanel(new FlowLayout());
        this.jp2.add(this.jb1);
        this.jp2.add(this.jb2);

        this.jf.add(this.jp1, BorderLayout.CENTER);
        this.jf.add(this.jp2, BorderLayout.SOUTH);

        this.jf.pack(); // show all things at once properly
        this.jf.setSize(250, 120);
        this.jf.setResizable(false);
        this.jf.setVisible(true);
    }

    private void setName() {
        String name_ = this.jta.getText();
        name_ = name_ == null ? "" : name_;
        this.umloc.changeName(name_);
        this.umlc.repaintAll();
        this.closeWindow();
    }

    private void closeWindow() {
        this.jf.setVisible(false);
        this.jf.dispose();
    }
}
