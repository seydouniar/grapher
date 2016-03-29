package grapher.ui;
import static java.lang.Math.PI;
import static java.lang.Math.exp;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.round;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.MouseInputListener;

import grapher.fc.Function;
import grapher.fc.FunctionFactory;


public class Grapher extends JPanel  {
	static final int MARGIN = 40;
	static final int STEP = 5;
	JSplitPane splitpane;
	JList<Function> funct_list;
	JScrollPane scrollPane;
	
	static final BasicStroke dash = new BasicStroke(1, BasicStroke.CAP_ROUND,
	                                                   BasicStroke.JOIN_ROUND,
	                                                   1.f,
	                                                   new float[] { 4.f, 4.f },
	                                                   0.f);
	                                                   
	protected int W = 400;
	protected int H = 300;
	
	protected double xmin, xmax;
	protected double ymin, ymax;

	protected Vector<Function> functions;
	
	public Grapher() {
		xmin = -PI/2.; xmax = 3*PI/2;
		ymin = -1.5;   ymax = 1.5;
		functions = new Vector<Function>();
		
		MyListener mia = new MyListener();
	        addMouseListener(mia);
	        addMouseMotionListener(mia);
	        addMouseWheelListener(mia);
        
		}
	
	public void add(String expression) {
		add(FunctionFactory.createFunction(expression));
	}
	
	public void add(Function function) {
		functions.add(function);
		repaint();
	}
		
	public Dimension getPreferredSize() { return new Dimension(W, H); }
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		W = getWidth();
		H = getHeight();

		Graphics2D g2 = (Graphics2D)g;

		// background
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, W, H);
		
		g2.setColor(Color.BLACK);
		
		// box
		g2.translate(MARGIN, MARGIN);
		W -= 2*MARGIN;
		H -= 2*MARGIN;
		if(W < 0 || H < 0) { 
			return; 
		}
		
		g2.drawRect(0, 0, W, H);
		
		g2.drawString("x", W, H+10);
		g2.drawString("y", -10, 0);
		
	
		// plot
		g2.clipRect(0, 0, W, H);
		g2.translate(-MARGIN, -MARGIN);
		// x values
		final int N = W/STEP + 1;
		final double dx = dx(STEP);
		double xs[] = new double[N];
		int    Xs[] = new int[N];
		for(int i = 0; i < N; i++) {
			double x = xmin + i*dx;
			xs[i] = x;
			Xs[i] = X(x);
	
		}
		
		for(Function f : functions) {
			// y values
			int Ys[] = new int[N];
			for(int i = 0; i < N; i++) {
				Ys[i] = Y(f.y(xs[i]));
			}
			//g2.setStroke(new BasicStroke(2f));
			g2.drawPolyline(Xs, Ys, N);
			
		}

		g2.setClip(null);

		// axes
		drawXTick(g2, 0);
		drawYTick(g2, 0);
		
		double xstep = unit((xmax-xmin)/10);
		double ystep = unit((ymax-ymin)/10);

		g2.setStroke(dash);
		for(double x = xstep; x < xmax; x += xstep)  { drawXTick(g2, x); }
		for(double x = -xstep; x > xmin; x -= xstep) { drawXTick(g2, x); }
		for(double y = ystep; y < ymax; y += ystep)  { drawYTick(g2, y); }
		for(double y = -ystep; y > ymin; y -= ystep) { drawYTick(g2, y); }
		
	}
	public void Setgras(Function f,String exp){
		for(Function fun : functions){
			functions.remove(fun);
			
			if(exp.equals(fun.toString())){
				Graphics g = getGraphics();
				Graphics2D g2 = (Graphics2D)g;
				
				g2.setStroke(new BasicStroke(2f));
			}
		}
		repaint();
	}
	
	protected double dx(int dX) { return  (double)((xmax-xmin)*dX/W); }
	protected double dy(int dY) { return -(double)((ymax-ymin)*dY/H); }

	protected double x(int X) { return xmin+dx(X-MARGIN); }
	protected double y(int Y) { return ymin+dy((Y-MARGIN)-H); }
	
	protected int X(double x) { 
		int Xs = (int)round((x-xmin)/(xmax-xmin)*W);
		return Xs + MARGIN; 
	}
	protected int Y(double y) { 
		int Ys = (int)round((y-ymin)/(ymax-ymin)*H);
		return (H - Ys) + MARGIN;
	}
		
	protected void drawXTick(Graphics2D g2, double x) {
		if(x > xmin && x < xmax) {
			final int X0 = X(x);
			g2.drawLine(X0, MARGIN, X0, H+MARGIN);
			g2.drawString((new Double(x)).toString(), X0, H+MARGIN+15);
		}
	}
	
	protected void drawYTick(Graphics2D g2, double y) {
		if(y > ymin && y < ymax) {
			final int Y0 = Y(y);
			g2.drawLine(0+MARGIN, Y0, W+MARGIN, Y0);
			g2.drawString((new Double(y)).toString(), 5, Y0);
		}
	}
	
	protected static double unit(double w) {
		double scale = pow(10, floor(log10(w)));
		w /= scale;
		if(w < 2)      { w = 2; } 
		else if(w < 5) { w = 5; }
		else           { w = 10; }
		return w * scale;
	}
	
	protected void translate(int dX, int dY) {
		double dx = dx(dX);
		double dy = dy(dY);
		xmin -= dx; xmax -= dx;
		ymin -= dy; ymax -= dy;
		repaint();	
	}
	
	protected void zoom(Point center, int dz) {
		double x = x(center.x);
		double y = y(center.y);
		double ds = exp(dz*.01);
		xmin = x + (xmin-x)/ds; xmax = x + (xmax-x)/ds;
		ymin = y + (ymin-y)/ds; ymax = y + (ymax-y)/ds;
		
		repaint();	
	}
	
	protected void zoom(Point p0, Point p1) {
		double x0 = x(p0.x);
		double y0 = y(p0.y);
		double x1 = x(p1.x);
		double y1 = y(p1.y);
		xmin = min(x0, x1); xmax = max(x0, x1);
		ymin = min(y0, y1); ymax = max(y0, y1);
		repaint();	
	}
	
	
	// les etats de la souris
	public enum States {
		rien, gauche, milieu, droite, droitedragged, gauchedragged
    }
	
	States state=States.rien;
	class MyListener implements MouseInputListener,MouseWheelListener {

        int x = 0;
        int y = 0;
        Point p0, p1;
        States state = States.rien;

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            // TODO Auto-generated method stub
          
            int n = e.getWheelRotation();
            if (n < 0) {
                zoom(e.getPoint(), 5);
            } else {
                zoom(e.getPoint(), -5);
            }

        }

        public void mouseDragged(MouseEvent e) {

            switch (state) {
                case gauche:
                    state = States.gauchedragged;
                    break;
                case milieu:
                    p1 = e.getPoint();
                    if (p0.x > p1.x) {
                        zoom(p0, -5);
                    } else if (p0.x < p1.x) {
                        zoom(p0, 5);
                    }
                    ;
                    break;
                case droite:
                    state = States.droitedragged;
                case gauchedragged:
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    translate((x - e.getX()) / 50, (y - e.getY()) / 50);
                    break;
                case droitedragged:
                    p1 = e.getPoint();
                    Graphics g = getGraphics();
                	Graphics2D g2 = (Graphics2D)g;
                	g2.setColor(Color.blue);
                	Rectangle r = new Rectangle(p0);
                    r.add(p1); 
                    if (p1.x < p0.x) {
                        if (p1.y < p0.y) {
                            g2.draw(r);
                            
                        }else{
                        	g2.draw(r);
                        
                        }
                    }else if (p1.x > p0.x) {
                        if (p1.y < p0.y) {
                        	g2.draw(r);
                        	
                        }else{
                        	g2.draw(r);
                        	
                        }
                    }
                  

                    break;
			default:
				break;
            }
        }
        //pressed
        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            p0 = new Point(x, y);
            if (e.getButton() == MouseEvent.BUTTON1) {
                state = States.gauche;

            } else if (e.getButton() == MouseEvent.BUTTON2) {
                state = States.milieu;
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                state = States.droite;
            }
        }

        // Release 
        @Override
        public void mouseReleased(MouseEvent e) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            switch (state) {

                case droitedragged:
                	
                	
                	
                    zoom(p0, p1);
                    break;
                case gauche:
                    zoom(p0, 5);
                    break;
                case droite:
                    zoom(p0, -5);
                    break;
			default:
				break;
            }
	}

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
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

	}
}
	
