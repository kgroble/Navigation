import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.omg.CORBA.PRIVATE_MEMBER;

public class ApplicationWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 1100;
	private static final int FRAME_HEIGHT = 700;
	private static final int CONTROL_PANEL_WIDTH = 180;

	// Serves as the 'background' that the other two panels will be added to:
	private JPanel containerPanel;
	private MapPanel mapPanel;
	private ControlPanel controlPanel;

	private Graph<City, Connection, String> map;
	private AStar aStar;
	private ActionListener restartListen;
	private MyDataList<String> citiesList, displayList;
	private JList<String> listBox, displayBox;

	public ApplicationWindow(Graph<City, Connection, String> map, AStar a) {
		this.aStar = a;
		this.citiesList = new MyDataList<String>();
		this.displayList = new MyDataList<String>();

		// Set up jframe
		this.setTitle("Navigation");

		// Set up container panel
		this.containerPanel = new JPanel();
		this.containerPanel.setLayout(null);
		this.containerPanel.setBackground(new Color(160, 160, 160));
		this.add(containerPanel);

		// set up box that displays the list in control panel
		this.listBox = new JList<String>(citiesList);
		this.listBox.setLayoutOrientation(JList.VERTICAL_WRAP);
		this.listBox.setVisibleRowCount(100);

		// sets up the box that displays path info in control panel
		this.displayBox = new JList<String>(displayList);
		this.displayBox.setLayoutOrientation(JList.VERTICAL_WRAP);
		this.displayBox.setVisibleRowCount(11);

		restartListen = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// remove and clear components
				containerPanel.remove(mapPanel);
				citiesList.clear();
				displayList.clear();
				controlPanel.clearTxtBoxes();

				// update boxes
				listBox.updateUI();
				displayBox.updateUI();

				// create and add mapPanel
				mapPanel = new MapPanel(map, ApplicationWindow.this);
				mapPanel.setBounds(CONTROL_PANEL_WIDTH, 0, getWidth()
						- CONTROL_PANEL_WIDTH, getHeight());
				mapPanel.setPreferredSize(new Dimension(getWidth()
						- CONTROL_PANEL_WIDTH, getHeight()));
				containerPanel.add(mapPanel);
				mapPanel.centerMap();
				repaint();
			}

		};

		// Set up controlsPanel
		this.controlPanel = new ControlPanel(0, restartListen);
		this.controlPanel.setBounds(0, 0, CONTROL_PANEL_WIDTH, FRAME_HEIGHT);
		this.controlPanel.setPreferredSize(new Dimension(CONTROL_PANEL_WIDTH,
				FRAME_HEIGHT));
		// this.controlPanel.setBackground(Color.GRAY);
		this.containerPanel.add(controlPanel);

		// Set up mapPanel
		this.mapPanel = new MapPanel(map, this);
		this.mapPanel.setBounds(CONTROL_PANEL_WIDTH, 0, FRAME_WIDTH
				- CONTROL_PANEL_WIDTH, FRAME_HEIGHT);
		this.mapPanel.setPreferredSize(new Dimension(FRAME_WIDTH
				- CONTROL_PANEL_WIDTH, FRAME_HEIGHT));
		this.mapPanel.centerMap();
		this.map = map;
		this.containerPanel.add(this.mapPanel);

		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				// retrieve new height and width info
				int newWidth = getWidth();
				int newHeight = getHeight();

				mapPanel.setPreferredSize(new Dimension(newWidth
						- CONTROL_PANEL_WIDTH, newHeight));
				controlPanel.setPreferredSize(new Dimension(
						CONTROL_PANEL_WIDTH, newHeight));

				// manipulate the panels
				controlPanel.setBounds(0, 0, CONTROL_PANEL_WIDTH, newHeight);
				mapPanel.setBounds(CONTROL_PANEL_WIDTH, 0, newWidth
						- CONTROL_PANEL_WIDTH, newHeight);

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
		mapPanel.clearPaths();
	}

	/**
	 * adds string to the display list
	 * 
	 * @param string
	 */
	public void addToDisplay(String string) {
		displayList.add(string);
		displayBox.updateUI();
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
		this.clearMapPaths();
		clearDisplay();
	}

	/**
	 * @return the list of cities in the path the user has created
	 */
	public String[] getCityNames() {
		return (String[]) this.citiesList.toArray();
	}

	/**
	 * clears the display list
	 * 
	 */
	public void clearDisplay() {
		this.displayList.clear();
		this.displayBox.updateUI();
	}

	/**
	 * clears the highlighted paths from the map
	 */
	public void clearMapPaths() {
		mapPanel.clearPaths();
	}

	public class ControlPanel extends JComponent {
		private static final long serialVersionUID = 7088760637095647696L;
		private JButton add, restart, pathDistance, pathTime, clear, remove;
		private JTextField cityName, distance, time;
		private JScrollPane listScroller, displayScroller;
		private final static int MARGIN = 10;

		public ControlPanel(int height, ActionListener restartListener) {
			super();

			// adds the city name txtbox to the panel
			this.cityName = new JTextField();
			this.cityName.setBounds(MARGIN, MARGIN, CONTROL_PANEL_WIDTH - 2
					* MARGIN, 20);
			this.cityName.setBorder(new BevelBorder(1));
			this.add(this.cityName);
			this.cityName.setForeground(Color.gray);
			TextPrompt cityNamePrompt = new TextPrompt("City Name", this.cityName);
			cityNamePrompt.setHorizontalAlignment(SwingConstants.CENTER);
			this.cityName.setForeground(Color.black);


			// adds the time txtbox to the panel
			this.time = new JTextField();
			this.time.setBounds(MARGIN, MARGIN * 6 + 240, CONTROL_PANEL_WIDTH
					- 2 * MARGIN, 20);
			this.time.setBorder(new BevelBorder(1));
			this.add(this.time);
			this.time.setForeground(Color.gray);
			TextPrompt timePrompt = new TextPrompt("Time from city (hr)", this.time);
			timePrompt.setHorizontalAlignment(SwingConstants.CENTER);
			this.time.setForeground(Color.black);
			
			// adds the distance txtbox to the panel
			this.distance = new JTextField();
			this.distance.setBounds(MARGIN, MARGIN * 6 + 180,
					CONTROL_PANEL_WIDTH - MARGIN * 2, 20);
			this.distance.setBorder(new BevelBorder(1));
			this.add(this.distance);
			this.distance.setForeground(Color.gray);
			TextPrompt distancePrompt = new TextPrompt("Distance from city (mi)", this.distance);
			distancePrompt.setHorizontalAlignment(SwingConstants.CENTER);
			this.distance.setForeground(Color.black);

			// adds the display to the panel
			this.displayScroller = new JScrollPane(displayBox);
			this.displayScroller.setBounds(MARGIN, MARGIN * 3 + 330,
					CONTROL_PANEL_WIDTH - MARGIN * 2, 260);
			this.displayScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			this.displayScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			this.add(displayScroller);

			// adds the list to the panel
			this.listScroller = new JScrollPane(listBox);
			this.listScroller.setBounds(MARGIN, MARGIN * 3 + 40,
					CONTROL_PANEL_WIDTH - MARGIN * 2, 100);
			this.listScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			this.listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			this.add(listScroller);

			// adds all the buttons
			clear = new JButton();
			clear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					citiesList.clear();
					displayList.clear();
					clearMapPaths();
					listBox.updateUI();
					displayBox.updateUI();
				}
			});
			clear.setBounds(MARGIN, MARGIN * 5 + 160, CONTROL_PANEL_WIDTH
					- MARGIN * 2, 20);
			clear.setText("Clear List");
			this.add(clear);

			add = new JButton();
			add.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addToList(cityName.getText());
				}

			});
			add.setBounds(MARGIN, MARGIN * 2 + 20, CONTROL_PANEL_WIDTH - MARGIN
					* 2, 20);
			add.setText("Add City");
			this.add(add);

			remove = new JButton();
			remove.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (listBox.isSelectionEmpty())
						return;
					int cityIndex = listBox.getSelectedIndex();
					removeFromList(cityIndex);
				}
			});
			remove.setBounds(MARGIN, MARGIN * 4 + 140, CONTROL_PANEL_WIDTH
					- MARGIN * 2, 20);
			remove.setText("Remove City");
			this.add(remove);

			pathTime = new JButton();
			pathTime.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int size = citiesList.size();
					if (size == 0) {
						JOptionPane.showMessageDialog(ApplicationWindow.this,
								"ENTER CITIES", "error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if ((size != 1 && !time.getText().equals(""))) {
						JOptionPane.showMessageDialog(ApplicationWindow.this,
								"YOU CAN ONLY HAVE ONE CITY", "error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (size == 1 && time.getText().equals("")) {
						JOptionPane
								.showMessageDialog(
										ApplicationWindow.this,
										"YOU CAN ONLY ROUTE BETWEEN TWO OR MORE CITIES",
										"error", JOptionPane.ERROR_MESSAGE);
						return;
					}

					clearMapPaths();
					clearDisplay();
					String txt = time.getText();

					if (txt.equals("")) {
						ArrayList<String> waypoints = new ArrayList<String>(
								ApplicationWindow.this.citiesList);
						Path path = ApplicationWindow.this.aStar
								.findFastestPathWithWayPoints(waypoints);
						ApplicationWindow.this.mapPanel.addPath(path);
						ArrayList<Path> paths = mapPanel.getPaths();
						double distance = 0;
						double time = 0;
						for (Path p : paths) {
							distance += p.getPathLength();
							time += p.getPathTravelTime();
						}

						addToDisplay("distance = " + (float) distance);
						addToDisplay("time = " + (float) time);
					} else if (!isInt(txt.trim())) {
						JOptionPane.showMessageDialog(ApplicationWindow.this,
								"ENTER IN AN INTEGER", "error",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						System.out.println("ooo");
						time.setText("");
					}
				}
			});
			pathTime.setBounds(MARGIN, MARGIN * 6 + 270, CONTROL_PANEL_WIDTH
					- MARGIN * 2, 20);
			pathTime.setText("Path Time");
			this.add(pathTime);

			pathDistance = new JButton();
			pathDistance.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int size = citiesList.size();
					if (size == 0) {
						JOptionPane.showMessageDialog(ApplicationWindow.this,
								"ENTER CITIES", "error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if ((size != 1 && !distance.getText().equals(""))) {
						JOptionPane.showMessageDialog(ApplicationWindow.this,
								"YOU CAN ONLY HAVE ONE CITY", "error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (size == 1 && distance.getText().equals("")) {
						JOptionPane
								.showMessageDialog(
										ApplicationWindow.this,
										"YOU CAN ONLY ROUTE BETWEEN TWO OR MORE CITIES",
										"error", JOptionPane.ERROR_MESSAGE);
						return;
					}

					clearMapPaths();
					clearDisplay();

					String txt = distance.getText();

					if (txt.equals("")) {
						ArrayList<String> waypoints = new ArrayList<String>(
								ApplicationWindow.this.citiesList);
						Path path = ApplicationWindow.this.aStar
								.findShortestPathWithWayPoints(waypoints);
						ApplicationWindow.this.mapPanel.addPath(path);
						ArrayList<Path> paths = mapPanel.getPaths();
						double distance = 0;
						double time = 0;
						for (Path p : paths) {
							distance += p.getPathLength();
							time += p.getPathTravelTime();
						}

						addToDisplay("distance = " + (float) distance);
						addToDisplay("time = " + (float) time);
					} else if (!isInt(txt.trim())) {
						JOptionPane.showMessageDialog(ApplicationWindow.this,
								"ENTER IN AN INTEGER", "error",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						int range = Integer.parseInt(txt);

						Path[] paths=null;
						
						try{
						paths = aStar
								.findPathsWithTravelDistance(
										ApplicationWindow.this.citiesList
												.get(0), range * .9,
										range * 1.1, 10);
						}catch(Exception ex){
							if(ex instanceof NoSuchElementException){
								JOptionPane
								.showMessageDialog(
										ApplicationWindow.this,
										"ROUTE IS NOT POSSIBLE",
										"error", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
						
						displayList.add("path name = (distance,time)");
						int i=1;
						for (Path path : paths)
						{
							displayList.add("path "+i+" = ("+Math.round(path.getPathLength())+", "+Math.round(path.getPathTravelTime())+")");
							ApplicationWindow.this.mapPanel.addPath(path);
							i++;
						}

						System.out.println(paths.length);
						distance.setText("");
					}
				}
			});
			pathDistance.setBounds(MARGIN, MARGIN * 6 + 210,
					CONTROL_PANEL_WIDTH - MARGIN * 2, 20);
			pathDistance.setText("Path Distance");
			this.add(pathDistance);

			restart = new JButton();
			restart.setBounds(MARGIN, FRAME_HEIGHT - 58 - MARGIN,
					CONTROL_PANEL_WIDTH - MARGIN * 2, 20);
			restart.setText("RESTART");
			restart.addActionListener(restartListener);
			this.add(restart);

			this.setBackground(Color.GRAY);
			this.setOpaque(true);
		}

		public void clearTxtBoxes() {
			this.cityName.setText("");
			this.distance.setText("");
			this.time.setText("");
		}

		public boolean isInt(String s) {
			for (int i = 0; i < s.length() - 1; i++) {
				char c = s.charAt(i);
				if (c != '1' && c != '2' && c != '3' && c != '4' && c != '5'
						&& c != '6' && c != '7' && c != '8' && c != '9'
						&& c != '0')
					return false;
			}
			return true;
		}

	}
}
