package bgWork.handler;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Define.AreaDefine;
import Listener.CPHActionListener;
import Pack.DragPack;
import Pack.SendText;
import bgWork.InitProcess;
import mod.instance.AssociationLine;
import mod.instance.BasicClass;
import mod.instance.CompositionLine;
import mod.instance.DependencyLine;
import mod.instance.GeneralizationLine;
import mod.instance.GroupContainer;
import mod.instance.UseCase;

public class CanvasPanelHandler extends PanelHandler {
	Vector<JPanel> members = new Vector<>();
	Vector<JPanel> selectComp = new Vector<>();
	int boundShift = 10;

	public CanvasPanelHandler(JPanel Container, InitProcess process) {
		super(Container, process);
		boundDistance = 10;
		initContextPanel();
		Container.add(this.contextPanel);
	}

	@Override
	void initContextPanel() {
		JPanel fphContextPanel = core.getFuncPanelHandler().getContectPanel();
		contextPanel = new JPanel();
		contextPanel.setBounds(fphContextPanel.getLocation().x + fphContextPanel.getSize().width + boundShift,
				fphContextPanel.getLocation().y, 800, 600);
		contextPanel.setLayout(null);
		contextPanel.setVisible(true);
		contextPanel.setBackground(Color.WHITE);
		contextPanel.setBorder(new LineBorder(Color.BLACK));
		contextPanel.addMouseListener(new CPHActionListener(this));
	}

	@Override
	public void ActionPerformed(MouseEvent e) {
		switch (core.getCurrentFuncIndex()) {
			case 0:
				selectByClick(e);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				break;
			case 5:
			case 6:
				addObject(core.getCurrentFunc(), e.getPoint());
				break;
			default:
				break;
		}
		repaintComp();
	}

	public void ActionPerformed(DragPack dp) {
		switch (core.getCurrentFuncIndex()) {
			case 0:
				selectByDrag(dp);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				addLine(core.getCurrentFunc(), dp);
				break;
			case 5:
			case 6:
				break;
			default:
				break;
		}
		repaintComp();
	}

	public void repaintComp() {
		for (int i = 0; i < members.size(); i++) {
			members.elementAt(i).repaint();
		}
		contextPanel.updateUI();
	}

	void selectByClick(MouseEvent e) {
		boolean isSelect = false;
		selectComp = new Vector<>();
		for (int i = 0; i < members.size(); i++) {
			JPanel currJPanel = members.elementAt(i);
			if (isInside(currJPanel, e.getPoint()) == true && isSelect == false) {
				switch (core.isFuncComponent(currJPanel)) {
					// check what type or class is this object (currJPanel)
					case 0:
						((BasicClass) currJPanel).setSelect(true);
						selectComp.add(currJPanel);
						isSelect = true;
						selectBySide(currJPanel, e.getPoint());
						break;
					case 1:
						((UseCase) currJPanel).setSelect(true);
						selectComp.add(currJPanel);
						isSelect = true;
						selectBySide(currJPanel, e.getPoint());
						break;
					case 6:
						Point p = e.getPoint();
						p.x -= currJPanel.getLocation().x;
						p.y -= currJPanel.getLocation().y;
						if (groupIsSelect((GroupContainer) currJPanel, p)) {
							((GroupContainer) currJPanel).setSelect(true);
							selectComp.add(currJPanel);
							isSelect = true;
						} else {
							((GroupContainer) currJPanel).setSelect(false);
						}
						break;
					default:
						break;
				}
			} else {
				setSelectAllType(currJPanel, false);
			}
		}
		repaintComp();
	}

	void selectBySide(JPanel selectedJPanel, Point clickedPoint) {
		// find side by clickedPoint on selectedJPanel (already inside the JPanel)

		/** currArea (aka. the side where clickedPoint is in the selectedJPanel) */
		int currArea = new AreaDefine().getArea(selectedJPanel.getLocation(), selectedJPanel.getSize(), clickedPoint);

		for (int i = 0; i < members.size(); i++) {
			JPanel currJPanel = members.elementAt(i);
			switch (core.isFuncComponent(currJPanel)) {
				case 2:
					if (((AssociationLine) currJPanel).checkOnSide(selectedJPanel, currArea)) {
						((AssociationLine) currJPanel).setSelect(true);
					} else {
						((AssociationLine) currJPanel).setSelect(false);
					}
					break;
				case 3:
					if (((CompositionLine) currJPanel).checkOnSide(selectedJPanel, currArea)) {
						((CompositionLine) currJPanel).setSelect(true);
					} else {
						((CompositionLine) currJPanel).setSelect(false);
					}
					break;
				case 4:
					if (((GeneralizationLine) currJPanel).checkOnSide(selectedJPanel, currArea)) {
						((GeneralizationLine) currJPanel).setSelect(true);
					} else {
						((GeneralizationLine) currJPanel).setSelect(false);
					}
					break;
				case 5:
					if (((DependencyLine) currJPanel).checkOnSide(selectedJPanel, currArea)) {
						((DependencyLine) currJPanel).setSelect(true);
					} else {
						((DependencyLine) currJPanel).setSelect(false);
					}
					break;
				default:
					break;
			}
		}
	}

	boolean groupIsSelect(GroupContainer container, Point point) {
		for (int i = 0; i < container.getComponentCount(); i++) {
			if (core.isGroupContainer(container.getComponent(i))) {
				point.x -= container.getComponent(i).getLocation().x;
				point.y -= container.getComponent(i).getLocation().y;
				if (groupIsSelect((GroupContainer) container.getComponent(i),
						point) == true) {
					return true;
				} else {
					point.x += container.getComponent(i).getLocation().x;
					point.y += container.getComponent(i).getLocation().y;
				}
			} else if (core.isJPanel(container.getComponent(i))) {
				if (isInside((JPanel) container.getComponent(i), point)) {
					return true;
				}
			}
		}
		return false;
	}

	boolean selectByDrag(DragPack dp) {
		if (isInSelect(dp.getFrom()) == true) {
			// dragging components
			Dimension shift = new Dimension(dp.getTo().x - dp.getFrom().x,
					dp.getTo().y - dp.getFrom().y);
			for (int i = 0; i < selectComp.size(); i++) {
				JPanel jp = selectComp.elementAt(i);
				jp.setLocation(jp.getLocation().x + shift.width,
						jp.getLocation().y + shift.height);
				if (jp.getLocation().x < 0) {
					jp.setLocation(0, jp.getLocation().y);
				}
				if (jp.getLocation().y < 0) {
					jp.setLocation(jp.getLocation().x, 0);
				}
			}
			return true;
		}

		// groupSelect(dp);
		// groupInversSelect(dp);

		groupSelectPLUS(dp);
		return true;
	}

	public void setGroup() {
		if (selectComp.size() > 1) {
			GroupContainer gContainer = new GroupContainer(core);
			gContainer.setVisible(true);
			Point p1 = new Point(selectComp.elementAt(0).getLocation().x,
					selectComp.elementAt(0).getLocation().y);
			Point p2 = new Point(selectComp.elementAt(0).getLocation().x,
					selectComp.elementAt(0).getLocation().y);
			Point testP;
			for (int i = 0; i < selectComp.size(); i++) {
				testP = selectComp.elementAt(i).getLocation();
				if (p1.x > testP.x) {
					p1.x = testP.x;
				}
				if (p1.y > testP.y) {
					p1.y = testP.y;
				}
				if (p2.x < testP.x + selectComp.elementAt(i).getSize().width) {
					p2.x = testP.x + selectComp.elementAt(i).getSize().width;
				}
				if (p2.y < testP.y + selectComp.elementAt(i).getSize().height) {
					p2.y = testP.y + selectComp.elementAt(i).getSize().height;
				}
			}
			p1.x--;
			p1.y--;
			gContainer.setLocation(p1);
			gContainer.setSize(p2.x - p1.x + 2, p2.y - p1.y + 2);
			for (int i = 0; i < selectComp.size(); i++) {
				JPanel temp = selectComp.elementAt(i);
				removeComponent(temp);
				gContainer.add(temp, i);
				temp.setLocation(temp.getLocation().x - p1.x,
						temp.getLocation().y - p1.y);
			}
			addComponent(gContainer);
			selectComp = new Vector<>();
			selectComp.add(gContainer);
			repaintComp();
		}
	}

	public void setUngroup() {
		int size = selectComp.size();
		for (int i = 0; i < size; i++) {
			if (core.isGroupContainer(selectComp.elementAt(i))) {
				GroupContainer gContainer = (GroupContainer) selectComp
						.elementAt(i);
				Component temp;
				int j = 0;
				while (gContainer.getComponentCount() > 0) {
					temp = gContainer.getComponent(0);
					temp.setLocation(
							temp.getLocation().x + gContainer.getLocation().x,
							temp.getLocation().y + gContainer.getLocation().y);
					addComponent((JPanel) temp, j);
					selectComp.add((JPanel) temp);
					gContainer.remove(temp);
					j++;
				}
				removeComponent(gContainer);
				selectComp.remove(gContainer);
			}
			repaintComp();
		}
	}

	// void groupSelect(DragPack dp)
	// void groupInversSelect(DragPack dp)

	void groupSelectPLUS(DragPack dp) {
		Point p1 = dp.getFrom();
		Point p2 = dp.getTo();

		JPanel jp = new JPanel();
		jp.setLocation(new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y)));
		jp.setSize(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));

		selectComp = new Vector<>();
		for (int i = 0; i < members.size(); i++) {
			JPanel currJPanel = members.elementAt(i);
			if (isInside(jp, currJPanel) == true) {
				selectComp.add(currJPanel);
				setSelectAllType(currJPanel, true);
			} else {
				setSelectAllType(currJPanel, false);
			}
		}
	}

	boolean isInSelect(Point point) {
		for (int i = 0; i < selectComp.size(); i++) {
			if (isInside(selectComp.elementAt(i), point) == true) {
				return true;
			}
		}
		return false;
	}

	void addLine(JPanel funcObj, DragPack dPack) {
		for (int i = 0; i < members.size(); i++) {
			if (isInside(members.elementAt(i), dPack.getFrom()) == true) {
				dPack.setFromObj(members.elementAt(i));
			}
			if (isInside(members.elementAt(i), dPack.getTo()) == true) {
				dPack.setToObj(members.elementAt(i));
			}
		}

		if (dPack.getFromObj() == dPack.getToObj()
				|| dPack.getFromObj() == contextPanel || dPack.getFromObj() == null
				|| dPack.getToObj() == contextPanel || dPack.getToObj() == null) {
			return;
		}

		switch (members.size()) {
			case 0:
			case 1:
				break;
			default:
				switch (core.isLine(funcObj)) {
					case 0:
						((AssociationLine) funcObj).setConnect(dPack);
						break;
					case 1:
						((CompositionLine) funcObj).setConnect(dPack);
						break;
					case 2:
						((GeneralizationLine) funcObj).setConnect(dPack);
						break;
					case 3:
						((DependencyLine) funcObj).setConnect(dPack);
						break;
					default:
						break;
				}
				members.insertElementAt(funcObj, 0);
				contextPanel.add(funcObj, 0);
				break;
		}
	}

	void addObject(JPanel funcObj, Point point) {
		if (members.size() > 0) {
			members.insertElementAt(funcObj, 0);
		} else {
			members.add(funcObj);
		}
		members.elementAt(0).setLocation(point);
		members.elementAt(0).setVisible(true);

		contextPanel.add(members.elementAt(0), 0);
	}

	public boolean isInside(JPanel container, Point point) {
		Point cLocat = container.getLocation();
		Dimension cSize = container.getSize();
		if (point.x >= cLocat.x && point.y >= cLocat.y) {
			if (point.x <= cLocat.x + cSize.width
					&& point.y <= cLocat.y + cSize.height) {
				return true;
			}
		}
		return false;
	}

	public boolean isInside(JPanel container, JPanel test) {
		// container should be larger than test

		Point cLocat = container.getLocation();
		Dimension cSize = container.getSize();
		Point tLocat = test.getLocation();
		Dimension tSize = test.getSize();

		if (cLocat.x <= tLocat.x && cLocat.y <= tLocat.y) {
			if ((cLocat.x + cSize.width >= tLocat.x + tSize.width)
					&& (cLocat.y + cSize.height >= tLocat.y + tSize.height)) {
				return true;
			}
		}

		return false;
	}

	public JPanel getSingleSelectJP() {
		if (selectComp.size() == 1) {
			return selectComp.elementAt(0);
		}
		return null;
	}

	public void setContext(SendText tr) {
		System.out.println(tr.getText());
		try {
			switch (core.isClass(tr.getDest())) {
				case 0:
					((BasicClass) tr.getDest()).setText(tr.getText());
					break;
				case 1:
					((UseCase) tr.getDest()).setText(tr.getText());
					break;
				default:
					break;
			}
		} catch (Exception e) {
			System.err.println("CPH error");
		}
	}

	void addComponent(JPanel comp) {
		contextPanel.add(comp, 0);
		members.insertElementAt(comp, 0);
	}

	void addComponent(JPanel comp, int index) {
		contextPanel.add(comp, index);
		members.insertElementAt(comp, index);
	}

	public void removeComponent(JPanel comp) {
		contextPanel.remove(comp);
		members.remove(comp);
	}

	void setSelectAllType(Object obj, boolean isSelect) {
		switch (core.isFuncComponent(obj)) {
			case 0:
				((BasicClass) obj).setSelect(isSelect);
				break;
			case 1:
				((UseCase) obj).setSelect(isSelect);
				break;
			case 2:
				((AssociationLine) obj).setSelect(isSelect);
				break;
			case 3:
				((CompositionLine) obj).setSelect(isSelect);
				break;
			case 4:
				((GeneralizationLine) obj).setSelect(isSelect);
				break;
			case 5:
				((DependencyLine) obj).setSelect(isSelect);
				break;
			case 6:
				((GroupContainer) obj).setSelect(isSelect);
				break;
			default:
				break;
		}
	}

	public boolean inGroup(Container panel) {
		return panel.getParent() != contextPanel;
	}

	public Point getAbsLocation(Container panel) {
		Point location = panel.getLocation();
		while (panel.getParent() != contextPanel) {
			panel = panel.getParent();
			location.x += panel.getLocation().x;
			location.y += panel.getLocation().y;
		}
		return location;
	}
}
