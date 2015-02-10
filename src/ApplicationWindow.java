import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class ApplicationWindow extends JFrame {

	private static final int FRAME_WIDTH=1000;
	private static final int FRAME_HEIGHT=600;
	private MapPanel mapPanel;
	private MyPanel myPanel;
	
	public ApplicationWindow(Graph<City,Connection,String> map){
		//Create container and establish border layout
		Container contentContainer = getContentPane();
		BorderLayout jBorderLayout = new BorderLayout();
		this.setLayout(jBorderLayout);
		
		//create and add myPanel
		myPanel = new MyPanel(0);
		contentContainer.add(myPanel,jBorderLayout.CENTER);
		myPanel.setBounds(0, 0, FRAME_WIDTH, 75);
		
		//create and add mapPanel
		mapPanel = new MapPanel(map);
		mapPanel.setBounds(0, 75, FRAME_WIDTH,FRAME_HEIGHT-75);
		contentContainer.add(mapPanel,jBorderLayout.SOUTH);
		
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		
		this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
            	//retrieve new height and width info
            	int newWidth=getWidth();
            	int newHeight=getHeight();
            	
            	//manipulate the panels
            	if(newWidth>=FRAME_WIDTH){
            		myPanel.setBounds(0, 0, newWidth, 75);
            		mapPanel.setBounds(0,75,newWidth,mapPanel.getHeight());
            	}
            	if(newHeight>=FRAME_HEIGHT)
        			mapPanel.setBounds(0, 75, mapPanel.getWidth(),newHeight-75);
            	
            	//update the frame
            	repaint();
            }
        });
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public class MyPanel extends JComponent {
		private static final long serialVersionUID = 7088760637095647696L;
		private JButton enter;
		private JTextArea from, to;
		private JLabel toLabel, fromLabel, distanceLabel, timeLabel;
		private JTextPane distance, time;
		private final static int MARGIN = 25;

		public MyPanel(int height) {
			super();
			
			TitledBorder border = BorderFactory.createTitledBorder(
					BorderFactory.createLoweredBevelBorder(), "Control Panel");
			border.setTitleJustification(TitledBorder.LEFT);
			this.setBorder(border);
			
			distance=new JTextPane();
			distance.setBorder(new BevelBorder(1));
			distance.setBounds(850,height+MARGIN,100,20);
			this.add(distance);
			
			distanceLabel= new JLabel();
			distanceLabel.setText("Distance in ...:");
			distanceLabel.setBounds(765,height+MARGIN,100,20);
			this.add(distanceLabel);
			
			time = new JTextPane();
			time.setBorder(new BevelBorder(1));
			time.setBounds(650,height+MARGIN,100,20);
			this.add(time);
			
			timeLabel = new JLabel();
			timeLabel.setText("Time in ... rate:");
			timeLabel.setBounds(565,height+MARGIN,100,20);
			this.add(timeLabel);

			from = new JTextArea();
			from.setBounds(240, height+MARGIN, 100, 20);
			from.setBorder(new BevelBorder(1));
			this.add(from);

			fromLabel = new JLabel();
			fromLabel.setBounds(135, height+MARGIN, 100, 20);
			fromLabel.setText("FROM CITY NAME:");
			this.add(fromLabel);

			to = new JTextArea();
			to.setBounds(450, height+MARGIN, 100, 20);
			to.setBorder(new BevelBorder(1));
			this.add(to);

			toLabel = new JLabel();
			toLabel.setBounds(350, height+MARGIN, 100, 20);
			toLabel.setText("     TO CITY NAME:");
			this.add(toLabel);

			enter = new JButton();
			enter.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					//Take input from the user
					String toS = to.getText();
					String fromS = from.getText();
					
					//send input to the map panel and receive informative string
					String a=mapPanel.fromTo(toS, fromS);
					
					//display output
					distance.setText(toS);
					time.setText(fromS);
				}
			
			});
			enter.setBounds(MARGIN, height+MARGIN-5, 100, 30);
			enter.setText("ENTER");
			this.add(enter);
		}
		
	}
}
