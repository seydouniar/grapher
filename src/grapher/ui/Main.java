package grapher.ui;

import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import grapher.fc.Function;


public class Main extends JFrame {
	JList mylist ; 
	Main(String title, String[] expressions) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Grapher grapher = new Grapher();		
		for(String expression : expressions) {
			grapher.add(expression);
		}
		mylist = new JList<>(expressions);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
		ScrollPane scrollpane = new ScrollPane();
		scrollpane.add(mylist);
		splitPane.add(scrollpane);
		splitPane.add(grapher);
		splitPane.setDividerLocation(150);
		add(splitPane);
		pack();
		// mouse listener
		MouseListener mouselistener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				JList list1 = (JList)e.getSource();
				int index = list1.locationToIndex(e.getPoint());
				Object o =list1.getModel().getElementAt(index);
				//if(o.toString().equals(grapher.functions.get(index).toString())){
					grapher.Setgras(grapher.functions.get(index),o.toString());
				//}
			}
		};
		mylist.addMouseListener(mouselistener);
		pack();
	}

	public static void main(String[] argv) {
		final String[] expressions = argv;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				new Main("grapher", expressions).setVisible(true); 
			}
		});
	}
}
