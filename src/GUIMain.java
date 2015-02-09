import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
	private static final int FRAME_HEIGHT=600;
	
	public static void main(String[] args){
		JFrame myFrame = new JFrame();
		
		Container contentContainer = myFrame.getContentPane();
		BorderLayout jBorderLayout = new BorderLayout();
		
		MyPanel j = new MyPanel(100, 0);
		contentContainer.add(j,jBorderLayout.CENTER);
		
		MapPanel i = new MapPanel(new Graph<City,Connection,String>());
		i.setBounds(0, (int)(FRAME_HEIGHT*.1), FRAME_WIDTH,FRAME_HEIGHT-(int)(FRAME_HEIGHT*.1));
		contentContainer.add(i,jBorderLayout.SOUTH);
		
		myFrame.setLayout(jBorderLayout);
		j.setBounds(0, 0, FRAME_WIDTH, (int)(FRAME_HEIGHT*.1));
		
		myFrame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
	}

	
}
