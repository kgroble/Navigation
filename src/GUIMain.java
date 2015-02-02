import javax.swing.JFrame;


public class GUIMain {
	
	private static final int FRAME_WIDTH=1000;
	private static final int FRAME_HEIGHT=1000;
	
	public static void main(String[] args){
		JFrame myFrame = new JFrame();
		MyPanel j = new MyPanel();
		myFrame.add(j);
		myFrame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
	}
	
}
