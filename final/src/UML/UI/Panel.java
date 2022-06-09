package UML.UI;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import UML.Modes.MouseActions.LineMode;
import UML.Modes.MouseActions.ObjectMode;
import UML.Modes.MouseActions.SelectionMode;
import UML.Objects.ObjectFactorys.ObjectFactoryInterface.ObjectType;

public class Panel extends JPanel {
	private Button[] buttons;

	public Panel() {
		this.buttons = new Button[6]; // ImageIcon is not Opaque will be the best.

		this.buttons[0] = new Button("select",
				"../assets/pic/1.select.png",
				new SelectionMode());
		this.add(this.buttons[0]);

		this.buttons[1] = new Button("association",
				"../assets/pic/2.association.png",
				new LineMode(ObjectType.Line_Association));
		this.add(this.buttons[1]);

		this.buttons[2] = new Button("generalization",
				"../assets/pic/3.generalization.png",
				new LineMode(ObjectType.Line_Generalization));
		this.add(this.buttons[2]);

		this.buttons[3] = new Button("composition",
				"../assets/pic/4.composition.png",
				new LineMode(ObjectType.Line_Composition));
		this.add(this.buttons[3]);

		this.buttons[4] = new Button("class",
				"../assets/pic/5.class.png",
				new ObjectMode(ObjectType.Object_Class));
		this.add(this.buttons[4]);

		this.buttons[5] = new Button("usecase",
				"../assets/pic/6.usecase.png",
				new ObjectMode(ObjectType.Object_Usecase));
		this.add(this.buttons[5]);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
}
