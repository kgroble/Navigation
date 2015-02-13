import java.awt.Color;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.omg.CORBA.PRIVATE_MEMBER;

public class ApplicationWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 600;

	// Serves as the 'background' that the other two panels will be added to:
	private JPanel containerPanel;
	private MapPanel mapPanel;
	private ControlPanel controlPanel;

	private Graph<City, Connection, String> map;
	private AStar aStar;
	private ActionListener restartListen;
	private MyDataList<String> citiesList;
	private JList<String> listBox;

	public ApplicationWindow(Graph<City, Connection, String> map, AStar a) {
		this.aStar = a;
		this.citiesList = new MyDataList<String>();

		// Set up jframe
		this.setTitle("Navigation");

		// Set up container panel
		this.containerPanel = new JPanel();
		this.containerPanel.setLayout(null);
		this.containerPanel.setBackground(new Color(160, 160, 160));
		this.add(containerPanel);

		this.listBox = new JList<String>(citiesList);
		this.listBox.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this.listBox.setBounds(250, ControlPanel.MARGIN - 10, 200, 65);
		this.listBox.setBorder(new BevelBorder(1));

		int controlPanelHeight = 95;

		restartListen = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				remove(controlPanel);
				remove(mapPanel);
				Container contentContainer = getContentPane();
				BoxLayout box = new BoxLayout(contentContainer,
						BoxLayout.Y_AXIS);
				setLayout(box);

				citiesList = new MyDataList<String>();

				listBox = new JList<String>(citiesList);
				listBox.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				listBox.setBounds(250, ControlPanel.MARGIN - 10, 200, 65);
				listBox.setBorder(new BevelBorder(1));

				// create and add myPanel
				controlPanel = new ControlPanel(0, restartListen);
				controlPanel.setBounds(0, 0, FRAME_WIDTH, controlPanelHeight);
				controlPanel.setPreferredSize(new Dimension(FRAME_WIDTH,
						controlPanelHeight));
				contentContainer.add(controlPanel, box);

				// create and add mapPanel
				mapPanel = new MapPanel(map, ApplicationWindow.this);
				mapPanel.setBounds(0, controlPanelHeight, FRAME_WIDTH,
						FRAME_HEIGHT - controlPanelHeight);
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
						controlPanel.setPreferredSize(new Dimension(newWidth,
								controlPanelHeight));

						// manipulate the panels
						if (newWidth != FRAME_WIDTH) {
							controlPanel.setBounds(0, 0, newWidth,
									controlPanelHeight);
							mapPanel.setBounds(0, controlPanelHeight, newWidth,
									mapPanel.getHeight());
						}
						if (newHeight != FRAME_HEIGHT)
							mapPanel.setBounds(0, controlPanelHeight,
									mapPanel.getWidth(), newHeight
											- controlPanelHeight);

						// update the frame
						repaint();
					}
				});
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setVisible(true);
			}

		};

		// Set up controlsPanel
		this.controlPanel = new ControlPanel(0, restartListen);
		this.controlPanel.setBounds(0, 0, FRAME_WIDTH, controlPanelHeight);
		this.controlPanel.setPreferredSize(new Dimension(FRAME_WIDTH,
				controlPanelHeight));
		// this.controlPanel.setBackground(Color.GRAY);
		this.containerPanel.add(controlPanel);

		// Set up mapPanel
		this.mapPanel = new MapPanel(map, this);
		this.mapPanel.setBounds(0, controlPanelHeight, FRAME_WIDTH,
				FRAME_HEIGHT - controlPanelHeight);
		this.mapPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT
				- controlPanelHeight));
		this.map = map;
		this.containerPanel.add(this.mapPanel);

		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				// retrieve new height and width info
				int newWidth = getWidth();
				int newHeight = getHeight();

				mapPanel.setPreferredSize(new Dimension(newWidth, newHeight
						- controlPanelHeight));
				controlPanel.setPreferredSize(new Dimension(newWidth,
						controlPanelHeight));

				// manipulate the panels
				if (newWidth != FRAME_WIDTH) {
					controlPanel.setBounds(0, 0, newWidth, controlPanelHeight);
					mapPanel.setBounds(0, controlPanelHeight, newWidth,
							mapPanel.getHeight());
				}
				if (newHeight != FRAME_HEIGHT)
					mapPanel.setBounds(0, controlPanelHeight,
							mapPanel.getWidth(), newHeight - controlPanelHeight);

				// update the frame
				repaint();
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Adds cityName to the list if it is a proper city name, otherwise it does
	 * nothing
	 * 
	 * @param cityName
	 */
	public void addToList(String cityName) {
		String cityText = cityName.toLowerCase();
		ArrayList<City> cities = map.getElements();
		for (City city : cities) {
			String cityName1 = city.getName().toLowerCase();
			if (cityName1.equals(cityText)) {
				citiesList.add(city.getName());
				listBox.updateUI();
				break;
			}
		}
	}

	/**
	 * removes an element from the specified index if that index is in myList's
	 * range
	 * 
	 * @param index
	 */
	public void removeFromList(int index) {
		if (index >= 0 && index < citiesList.size()) {
			citiesList.remove(index);
			listBox.updateUI();
		}
	}

	/**
	 * @return the list of cities in the path the user has created
	 */
	public String[] getCityNames() {
		return (String[]) this.citiesList.toArray();
	}

	public class ControlPanel extends JComponent {
		private static final long serialVersionUID = 7088760637095647696L;
		private JButton add, restart, pathDistance, pathTime, clear, remove;
		private JTextArea cityName;
		private final static int MARGIN = 25;

		public ControlPanel(int height, ActionListener ab) {
			super();

			// Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
			// TitledBorder border = BorderFactory.createTitledBorder(
			// BorderFactory.createLoweredBevelBorder(), "Control Panel");
			// border.setTitleJustification(TitledBorder.LEFT);
			// this.setBorder(border);

			cityName = new JTextArea();
			cityName.setBounds(MARGIN, height + MARGIN, 100, 20);
			cityName.setBorder(new BevelBorder(1));
			this.add(cityName);

			JScrollPane listScroller = new JScrollPane(listBox);
			listScroller.setBounds(250, MARGIN - 10, 200, 65);
			this.add(listScroller);

			clear = new JButton();
			clear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					citiesList.clear();
					listBox.updateUI();
				}
			});
			clear.setBounds(455, height + MARGIN - 10, 100, 30);
			clear.setText("Clear List");
			this.add(clear);

			add = new JButton();
			add.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addToList(cityName.getText());
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
			add.setBounds(135, height + MARGIN - 10, 100, 30);
			add.setText("Add City");
			this.add(add);

			remove = new JButton();
			remove.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int cityIndex = listBox.getSelectedIndex();
					removeFromList(cityIndex);
				}
			});
			remove.setBounds(135, height + MARGIN + 25, 110, 30);
			remove.setText("Remove City");
			this.add(remove);

			pathTime = new JButton();
			pathTime.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ArrayList<String> waypoints = new ArrayList<String>(
							ApplicationWindow.this.citiesList);
					Path path = ApplicationWindow.this.aStar
							.findFastestPathWithWayPoints(waypoints);
					ApplicationWindow.this.mapPanel.addPath(path);
				}
			});
			pathTime.setBounds(455, height + MARGIN + 25, 100, 30);
			pathTime.setText("Path Time");
			this.add(pathTime);

			pathDistance = new JButton();
			pathDistance.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ArrayList<String> waypoints = new ArrayList<String>(
							ApplicationWindow.this.citiesList);
					Path path = ApplicationWindow.this.aStar
							.findShortestPathWithWayPoints(waypoints);
					ApplicationWindow.this.mapPanel.addPath(path);
				}
			});

			pathDistance.setBounds(560, height + MARGIN + 25, 120, 30);
			pathDistance.setText("Path Distance");
			this.add(pathDistance);

			restart = new JButton();
			restart.setBounds(970, height + MARGIN - 5, 100, 30);
			restart.setText("RESTART");
			restart.addActionListener(ab);
			this.add(restart);

			this.setBackground(Color.GRAY);
			this.setOpaque(true);
		}

	}
}
