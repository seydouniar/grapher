package grapher.ui;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ChooseColor extends JFrame {
	
	 JDialog dialog= new JDialog();
     Color c;
     
	public ChooseColor(Color c) {
		super();
		dialog.add(new JColorChooser());
		dialog.setVisible(true);
		c=new  JColorChooser().getColor();
		dialog.pack();
	}
	
	public void main(String[] argv){
		new ChooseColor(Color.BLUE);
	}
}
