import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;


public class ApplicationWindow extends JFrame {

	private static final int FRAME_WIDTH=1000;
	private static final int FRAME_HEIGHT=600;
	
	public ApplicationWindow(Graph<City,Connection,String> map){
		Container contentContainer = getContentPane();
		BorderLayout jBorderLayout = new BorderLayout();
		
		//
		MyPanel j = new MyPanel(0);
		contentContainer.add(j,jBorderLayout.CENTER);
		
		MapPanel i = new MapPanel(map);
		i.setBounds(0, (int)(FRAME_HEIGHT*.1), FRAME_WIDTH,FRAME_HEIGHT-(int)(FRAME_HEIGHT*.1));
		contentContainer.add(i,jBorderLayout.SOUTH);
		
		setLayout(jBorderLayout);
		j.setBounds(0, 0, FRAME_WIDTH, (int)(FRAME_HEIGHT*.1));
		
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
