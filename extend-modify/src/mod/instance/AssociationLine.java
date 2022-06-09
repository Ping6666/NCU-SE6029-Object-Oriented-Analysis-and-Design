package mod.instance;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import Define.AreaDefine;
import Pack.DragPack;
import bgWork.handler.CanvasPanelHandler;
import mod.IFuncComponent;
import mod.ILinePainter;
import java.lang.Math;

public class AssociationLine extends JPanel
		implements IFuncComponent, ILinePainter {
	/* from */
	JPanel from;
	int fromSide;
	Point fp = new Point(0, 0);

	/* to */
	JPanel to;
	int toSide;
	Point tp = new Point(0, 0);

	boolean isSelect = false;
	int selectBoxSize = 5;
	CanvasPanelHandler cph;

	public AssociationLine(CanvasPanelHandler cph) {
		this.setOpaque(false);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(1, 1));
		this.cph = cph;
	}

	@Override
	public void paintComponent(Graphics g) {
		Point fpPrime;
		Point tpPrime;
		renewConnect();

		fpPrime = new Point(fp.x - this.getLocation().x, fp.y - this.getLocation().y);
		tpPrime = new Point(tp.x - this.getLocation().x, tp.y - this.getLocation().y);
		g.drawLine(fpPrime.x, fpPrime.y, tpPrime.x, tpPrime.y);

		paintArrow(g, tpPrime);
		if (isSelect == true) {
			paintSelect(g);
		}
	}

	@Override
	public void reSize() {
		Dimension size = new Dimension(Math.abs(fp.x - tp.x) + 10,
				Math.abs(fp.y - tp.y) + 10);
		this.setSize(size);
		this.setLocation(Math.min(fp.x, tp.x) - 5, Math.min(fp.y, tp.y) - 5);
	}

	@Override
	public void paintArrow(Graphics g, Point point) {
	}

	@Override
	public void setConnect(DragPack dPack) {
		Point mfp = dPack.getFrom();
		Point mtp = dPack.getTo();
		from = (JPanel) dPack.getFromObj();
		to = (JPanel) dPack.getToObj();

		fromSide = new AreaDefine().getArea(from.getLocation(), from.getSize(), mfp);
		toSide = new AreaDefine().getArea(to.getLocation(), to.getSize(), mtp);

		renewConnect();
		System.out.println("from side " + fromSide);
		System.out.println("to side " + toSide);
	}

	void renewConnect() {
		try {
			fp = getConnectPoint(from, fromSide);
			tp = getConnectPoint(to, toSide);
			this.reSize();
		} catch (NullPointerException e) {
			this.setVisible(false);
			cph.removeComponent(this);
		}
	}

	Point getConnectPoint(JPanel jp, int side) {
		Point temp = new Point(0, 0);
		Point jpLocation = this.cph.inGroup(this) ? jp.getLocation() : this.cph.getAbsLocation(jp);

		if (side == new AreaDefine().TOP) {
			temp.x = (int) (jpLocation.x + jp.getSize().getWidth() / 2);
			temp.y = jpLocation.y;
		} else if (side == new AreaDefine().RIGHT) {
			temp.x = (int) (jpLocation.x + jp.getSize().getWidth());
			temp.y = (int) (jpLocation.y + jp.getSize().getHeight() / 2);
		} else if (side == new AreaDefine().LEFT) {
			temp.x = jpLocation.x;
			temp.y = (int) (jpLocation.y + jp.getSize().getHeight() / 2);
		} else if (side == new AreaDefine().BOTTOM) {
			temp.x = (int) (jpLocation.x + jp.getSize().getWidth() / 2);
			temp.y = (int) (jpLocation.y + jp.getSize().getHeight());
		} else {
			temp = null;
			System.err.println("getConnectPoint fail:" + side);
		}
		return temp;
	}

	@Override
	public void paintSelect(Graphics gra) {
		Point fpPrime;
		Point tpPrime;
		fpPrime = new Point(fp.x - this.getLocation().x,
				fp.y - this.getLocation().y);
		tpPrime = new Point(tp.x - this.getLocation().x,
				tp.y - this.getLocation().y);

		/** adjust the from box not to overlay the base object */
		switch (fromSide) {
			case 0: // BOTTOM
				// need not to adjust
				break;
			case 1: // LEFT
				fpPrime.translate(-1 * selectBoxSize, -1 * selectBoxSize);
				break;
			case 2: // RIGHT
				// need not to adjust
				break;
			case 3: // TOP
				fpPrime.translate(-1 * selectBoxSize, -1 * selectBoxSize);
				break;
			default:
				break;
		}

		/** adjust the to box not to overlay the base object */
		switch (toSide) {
			case 0: // BOTTOM
				// need not to adjust
				break;
			case 1: // LEFT
				tpPrime.translate(-1 * selectBoxSize, -1 * selectBoxSize);
				break;
			case 2: // RIGHT
				// need not to adjust
				break;
			case 3: // TOP
				tpPrime.translate(-1 * selectBoxSize, -1 * selectBoxSize);
				break;
			default:
				break;
		}

		gra.setColor(Color.BLACK);
		gra.fillRect(fpPrime.x, fpPrime.y, selectBoxSize, selectBoxSize);
		gra.fillRect(tpPrime.x, tpPrime.y, selectBoxSize, selectBoxSize);
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public boolean checkOnSide(JPanel jPanel, int side) {
		if ((from == jPanel && fromSide == side) || (to == jPanel && toSide == side)) {
			return true;
		}
		return false;
	}
}
