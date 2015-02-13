import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import sun.security.pkcs11.wrapper.CK_AES_CTR_PARAMS;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class ApplicationWindow extends JFrame {

	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 600;
	private MapPanel mapPanel;
	private ControlPanel myPanel;
	private Graph<City, Connection, String> map;
	private AStar a;
	private ActionListener restartListen;

	public ApplicationWindow(Graph<City, Connection, String> map, AStar a) {
		this.a = a;

		// Create container and establish border layout
		Container contentContainer = getContentPane();
		BoxLayout box = new BoxLayout(contentContainer, BoxLayout.Y_AXIS);
		this.setLayout(box);
		
		int controlPanelHeight=95;

		// create and add myPanel
		restartListen = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				remove(myPanel);
				remove(mapPanel);
				Container contentContainer = getContentPane();
				BoxLayout box = new BoxLayout(contentContainer,
						BoxLayout.Y_AXIS);
				setLayout(box);
				
				// create and add myPanel
				myPanel = new ControlPanel(0, restartListen);
				myPanel.setBounds(0, 0, FRAME_WIDTH, controlPanelHeight);
				myPanel.setPreferredSize(new Dimension(FRAME_WIDTH, controlPanelHeight));
				contentContainer.add(myPanel, box);

				// create and add mapPanel
				mapPanel = new MapPanel(map);
				mapPanel.setBounds(0, controlPanelHeight, FRAME_WIDTH, FRAME_HEIGHT - controlPanelHeight);
				mapPanel.setPreferredSize(new Dimension(FRAME_WIDTH,
						FRAME_HEIGHT - controlPanelHeight));
				contentContainer.add(mapPanel, box);

				setSize(FRAME_WIDTH, FRAME_HEIGHT);
				setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

				addComponentListener(new ComponentAdapter() {

					@Override
					public void componentResized(ComponentEvent e) {
						// retrieve new height and width info
						int newWidth = getWidth();
						int newHeight = getHeight();

						mapPanel.setPreferredSize(new Dimension(newWidth,
								newHeight - controlPanelHeight));
						myPanel.setPreferredSize(new Dimension(newWidth, controlPanelHeight));

						// manipulate the panels
						if (newWidth != FRAME_WIDTH) {
							myPanel.setBounds(0, 0, newWidth, controlPanelHeight);
							mapPanel.setBounds(0, controlPanelHeight, newWidth,
									mapPanel.getHeight());
						}
						if (newHeight != FRAME_HEIGHT)
							mapPanel.setBounds(0, controlPanelHeight, mapPanel.getWidth(),
									newHeight - controlPanelHeight);

						// update the frame
						repaint();
					}
				});
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setVisible(true);
			}

		};
		myPanel = new ControlPanel(0, restartListen);
		myPanel.setBounds(0, 0, FRAME_WIDTH, controlPanelHeight);
		myPanel.setPreferredSize(new Dimension(FRAME_WIDTH, controlPanelHeight));
		contentContainer.add(myPanel, box);

		// create and add mapPanel
		mapPanel = new MapPanel(map);
		mapPanel.setBounds(0, controlPanelHeight, FRAME_WIDTH, FRAME_HEIGHT - controlPanelHeight);
		mapPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - controlPanelHeight));
		this.map = map;
		contentContainer.add(mapPanel, box);

		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				// retrieve new height and width info
				int newWidth = getWidth();
				int newHeight = getHeight();

				mapPanel.setPreferredSize(new Dimension(newWidth,
						newHeight - controlPanelHeight));
				myPanel.setPreferredSize(new Dimension(newWidth, controlPanelHeight));

				// manipulate the panels
				if (newWidth != FRAME_WIDTH) {
					myPanel.setBounds(0, 0, newWidth, controlPanelHeight);
					mapPanel.setBounds(0, controlPanelHeight, newWidth, mapPanel.getHeight());
				}
				if (newHeight != FRAME_HEIGHT)
					mapPanel.setBounds(0, controlPanelHeight, mapPanel.getWidth(),
							newHeight - controlPanelHeight);

				// update the frame
				repaint();
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public class ControlPanel extends JComponent {
		private static final long serialVersionUID = 7088760637095647696L;
		private JButton enter, restart;
		private JTextArea cityName;
		private JList list;
		private MyDataList<String> myList;
		private final static int MARGIN = 25;

		public ControlPanel(int height, ActionListener ab) {
			super();

			TitledBorder border = BorderFactory.createTitledBorder(
					BorderFactory.createLoweredBevelBorder(), "Control Panel");
			border.setTitleJustification(TitledBorder.LEFT);
			this.setBorder(border);

			myList = new MyDataList<String>();

			cityName = new JTextArea();
			cityName.setBounds(MARGIN, height + MARGIN, 100, 20);
			cityName.setBorder(new BevelBorder(1));
			this.add(cityName);

			list = new JList<String>(myList);
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			list.setBounds(250, MARGIN - 10, 200, 40);
			list.setBorder(new BevelBorder(1));
			// this.add(list);

			JScrollPane listScroller = new JScrollPane(list);
			listScroller.setBounds(250, MARGIN - 10, 200, 60);
			this.add(listScroller);

			enter = new JButton();
			enter.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String cityText = cityName.getText().trim().toLowerCase();
					ArrayList<City> cities = map.getElements();
					for (City city : cities) {
						String cityName = city.getName().toLowerCase();
						if (cityName.equals(cityText)) {
							myList.add(city.getName());
							System.out.println(myList);
							break;
						}
					}
					cityName.setText("");
					list.updateUI();
					// //Take input from the user
					// String toS = to.getText().toLowerCase().trim();
					// String fromS = from.getText().toLowerCase().trim();
					// City toCity = null;
					// City fromCity = null;
					//

					// if(cityName.equals(toS)){
					// toCity = city;
					// toS = toCity.getName();
					// }
					// if(cityName.equals(fromS)){
					// fromCity = city;
					// fromS = fromCity.getName();
					// }
					//
					// }
					// if(toCity != null && fromCity != null){
					// ApplicationWindow.this.mapPanel.addPath(a.findShortestPathBetween(fromS,
					// toS));
					// } else {
					// System.out.println("Please make sure you entered valid city names.");
					// }
					//
					// //send input to the map panel and receive informative
					// string
					// //String a=mapPanel.fromTo(toSt, fromS);
					//
					// //display output
					// //distance.setText(toS);
					// //time.setText(fromS);
				}

			});
			enter.setBounds(135, height + MARGIN - 5, 100, 30);
			enter.setText("Add City");
			this.add(enter);

			restart = new JButton();
			restart.setBounds(970, height + MARGIN - 5, 100, 30);
			restart.setText("RESTART");
			restart.addActionListener(ab);
			this.add(restart);
		}

	}
}
