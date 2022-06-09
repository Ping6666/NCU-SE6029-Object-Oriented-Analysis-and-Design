package UML.Modes.KeyActions;

import java.awt.event.KeyEvent;

// import javax.swing.KeyStroke;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import UML.Modes.KeyMode;

/** CCP: Cut Copy Paste. */
public class CCPMode extends KeyMode {
    private JSONObject ccpJSONObject;

    public CCPMode() {
        super();
        this.ccpJSONObject = null;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_X) {
            System.out.println("\nGet keyPressed: CTRL+X");

            this.ccpJSONObject = this.canvas.cutShapeCores();
            this.printCCPShapeCores();
        } else if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_C) {
            System.out.println("\nGet keyPressed: CTRL+C");

            this.ccpJSONObject = this.canvas.copyShapeCores();
            this.printCCPShapeCores();
        } else if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK && e.getKeyCode() == KeyEvent.VK_V) {
            System.out.println("\nGet keyPressed: CTRL+V");
            this.printCCPShapeCores();

            try {
                JSONParser jsonParser = new JSONParser();
                this.ccpJSONObject = (JSONObject) jsonParser.parse(this.ccpJSONObject.toJSONString());
                this.canvas.pasteShapeCores(this.ccpJSONObject);
            } catch (Exception exception) {
                System.out.println("ERROR: read json fail, " + exception);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    private void printCCPShapeCores() {
        if (this.ccpJSONObject == null) {
            return;
        }
        System.out.println("UML diagram to JSON:\n" + this.ccpJSONObject.toJSONString());
    }
}
