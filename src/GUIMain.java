import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;


public class GUIMain {
	
	private static final int FRAME_WIDTH=1000;
	private static final int FRAME_HEIGHT=1000;
	
	public static void main(String[] args){
		JFrame myFrame = new JFrame();
		MyPanel j = new MyPanel(FRAME_WIDTH, FRAME_HEIGHT);
		myFrame.add(j);
		myFrame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
	}

	
}
