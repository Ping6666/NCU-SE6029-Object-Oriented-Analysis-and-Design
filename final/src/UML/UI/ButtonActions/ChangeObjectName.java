package UML.UI.ButtonActions;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import UML.Canvas;

public class ChangeObjectName implements ActionListener {
    private Canvas canvas;

    public ChangeObjectName() {
        this.canvas = Canvas.getInstance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String targetString = this.canvas.getObjectName();
        if (targetString != null) {
            this.changeObjNameFrame(targetString);
        }
    }

    private void changeObjNameFrame(String targetString) {
        JFrame jFrame = new JFrame("UML change name");

        // jPanel_1 contain JTextArea
        JPanel jPanel_1 = new JPanel();

        // jTextArea
        JTextArea jTextArea = new JTextArea(targetString);
        jPanel_1.add(jTextArea);

        // jPanel_2 contain two JButton
        JPanel jPanel_2 = new JPanel(new FlowLayout());

        // jButton_1
        JButton jButton_1 = new JButton("OK");
        ActionListener actionListener_1 = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setName(jTextArea);
                closeWindow(jFrame);
            }
        };
        jButton_1.addActionListener(actionListener_1);
        jPanel_2.add(jButton_1);

        // jButton_2
        JButton jButton_2 = new JButton("Cancel");
        ActionListener actionListener_2 = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeWindow(jFrame);
            }
        };
        jButton_2.addActionListener(actionListener_2);
        jPanel_2.add(jButton_2);

        jFrame.add(jPanel_1, BorderLayout.CENTER);
        jFrame.add(jPanel_2, BorderLayout.SOUTH);

        jFrame.pack(); // show all things at once properly
        jFrame.setSize(250, 120);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }

    private void setName(JTextArea jTextArea) {
        String name = jTextArea.getText();
        name = (name == null) ? "" : name;
        System.out.println("\tInput name is: " + name);
        this.canvas.setObjectName(name);
    }

    private void closeWindow(JFrame jFrame) {
        jFrame.setVisible(false);
        jFrame.dispose();
    }
}
