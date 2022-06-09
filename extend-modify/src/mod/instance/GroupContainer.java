package mod.instance;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import javax.swing.JPanel;

import bgWork.Core;
import bgWork.InitProcess;
import mod.IFuncComponent;

public class GroupContainer extends JPanel implements IFuncComponent {
	boolean isSelect = false;
	InitProcess core;

	public GroupContainer(InitProcess process) {
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		this.setOpaque(false);
		this.core = process;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawRect(0, 0, (int) this.getSize().getWidth() - 1, (int) this.getSize().getHeight() - 1);
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
		for (int i = 0; i < this.getComponentCount(); i++) {
			if (core.isCore(core) == true) {
				switch (((Core) core).isFuncComponent(this.getComponent(i))) {
					case 0:
						((BasicClass) this.getComponent(i)).setSelect(isSelect);
						break;
					case 1:
						((UseCase) this.getComponent(i)).setSelect(isSelect);
						break;
					case 2:
						((AssociationLine) this.getComponent(i)).setSelect(isSelect);
						break;
					case 3:
						((CompositionLine) this.getComponent(i)).setSelect(isSelect);
						break;
					case 4:
						((GeneralizationLine) this.getComponent(i)).setSelect(isSelect);
						break;
					case 5:
						((DependencyLine) this.getComponent(i)).setSelect(isSelect);
						break;
					case 6:
						((GroupContainer) this.getComponent(i)).setSelect(isSelect);
						break;
					default:
						break;
				}
			}
		}
	}

	public GroupContainer(LayoutManager layout) {
		super(layout);
	}

	public GroupContainer(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public GroupContainer(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	@Override
	public void reSize() {
	}
}
