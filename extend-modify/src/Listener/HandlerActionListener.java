package Listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bgWork.handler.PanelHandler;

public class HandlerActionListener implements MouseListener {
	PanelHandler handler;

	public HandlerActionListener(PanelHandler h) {
		this.handler = h;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		handler.ActionPerformed(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
