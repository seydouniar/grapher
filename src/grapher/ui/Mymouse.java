package grapher.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

public class Mymouse implements MouseInputListener,MouseWheelListener{
	Grapher g = new Grapher();
	Point p;
	boolean mouseDown;

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton()== MouseEvent.BUTTON1) {
			mouseDown =true;
			p.x=e.getX();
			p.y=e.getY();
			System.out.println("presseddddddddddddddddd");
			System.out.println(p.getX()+","+p.getY());
		}
		mouseDown = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton()== MouseEvent.BUTTON1) {
			mouseDown =false;
		}
		
		if(mouseDown==false) {
			
			g.zoom(p, 5);
			System.out.println("releaseddddddddddddddddddd1111111111111111");
			System.out.println(p.getX()+","+p.getY());
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

}
