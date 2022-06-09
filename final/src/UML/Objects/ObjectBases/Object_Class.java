package UML.Objects.ObjectBases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import UML.Objects.ObjectBase;

public class Object_Class extends ObjectBase {
	public Object_Class() {
		super();
	}

	public Object_Class(Rectangle rectangle) {
		super(rectangle);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(this.rectangle.x, this.rectangle.y,
				this.rectangle.width, this.rectangle.height);
		g.setColor(Color.BLACK);
		g.drawRect(this.rectangle.x, this.rectangle.y,
				this.rectangle.width, this.rectangle.height / 3);
		g.drawRect(this.rectangle.x, this.rectangle.y,
				this.rectangle.width, 2 * this.rectangle.height / 3);
		g.drawRect(this.rectangle.x, this.rectangle.y,
				this.rectangle.width, this.rectangle.height);
		this.drawWorkHouse(g);
	}
}
